package com.example.android.trailfinder;

import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.android.trailfinder.db.api.NetworkDataSource;
import com.example.android.trailfinder.db.api.TrailDummyData;
import com.example.android.trailfinder.db.api.TrailList;
import com.example.android.trailfinder.db.dao.TrailDao;
import com.example.android.trailfinder.db.entity.Trail;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class TrailRepository {

    private static final int SECONDS_IN_MILLIS = 1000;
    private static final int MINUTES_IN_MILLIS = SECONDS_IN_MILLIS * 60;
    private static final int HOURS_IN_MILLIS = MINUTES_IN_MILLIS * 60;

    private static TrailRepository trailRepository;

    private final TrailDao trailDao;
    private final NetworkDataSource networkDataSource;
    private final AppExecutors appExecutors;

    private TrailRepository(final TrailDao trailDao,
                            final NetworkDataSource networkDataSource,
                            final AppExecutors appExecutors) {
        this.trailDao = trailDao;
        this.networkDataSource = networkDataSource;
        this.appExecutors = appExecutors;
    }

    public static TrailRepository getInstance(final TrailDao trailDao,
                                              final NetworkDataSource networkDataSource,
                                              final AppExecutors appExecutors) {
        if (trailRepository == null) {
            synchronized (TrailRepository.class) {
                trailRepository = new TrailRepository(
                        trailDao,
                        networkDataSource,
                        appExecutors);
                Timber.d("Repository has been created");

            }
            Timber.d("Instance of repository has been called");
        }
        return trailRepository;
    }

    public LiveData<List<Trail>> getAllTrails() {
        refreshTrailData();
        return trailDao.getAllTrails();
    }

    private void refreshTrailData() {
        appExecutors.diskIO().execute(() -> {

            boolean trailExists = (trailDao.hasTrails(getMaxRefreshTime()).size() != 0);
            Timber.d("trailExists = %s", trailExists);

            if(!trailExists) {

                networkDataSource.getAllCurrentTrails(
                        TrailDummyData.LAT,
                        TrailDummyData.LON,
                        TrailDummyData.MAX_DISTANCE,
                        TrailDummyData.KEY).enqueue(new Callback<TrailList>() {

                    @Override
                    public void onResponse(Call<TrailList> call, Response<TrailList> response) {
                        if(response.body() != null) {
                            List<Trail> trails = response.body().getTrails();
                            for (Trail trail : trails) {
                                trail.setLastRefresh(System.currentTimeMillis());
                            }

                            appExecutors.diskIO().execute(() -> {
                                trailDao.insertAll(trails);
                                Timber.d("New data inserted.");
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<TrailList> call, Throwable t) {
                        Timber.d(t.toString());
                    }
                });
            }

        });
    }

    private long getMaxRefreshTime() {
        // Subtracts one hour from current time.
        // If last entered refresh time is still less,
        // then refresh has not occurred in over an hour.
        return System.currentTimeMillis() - HOURS_IN_MILLIS;
    }

}

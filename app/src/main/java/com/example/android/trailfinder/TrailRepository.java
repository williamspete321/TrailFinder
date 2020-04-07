package com.example.android.trailfinder;

import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.android.trailfinder.db.api.NetworkDataSource;
import com.example.android.trailfinder.db.api.TrailDummyData;
import com.example.android.trailfinder.db.api.TrailList;
import com.example.android.trailfinder.db.dao.TrailDao;
import com.example.android.trailfinder.db.entity.Trail;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrailRepository {

    private static final String LOG_TAG = TrailRepository.class.getSimpleName();

    private static final int REFRESH_TIME_IN_HOURS = 1;

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
                Log.d(LOG_TAG, "Repository has been created");

            }
            Log.d(LOG_TAG, "Instance of repository has been called");
        }
        return trailRepository;
    }

    public LiveData<List<Trail>> getAllTrails() {
        refreshTrailData();
        return trailDao.getAllTrails();
    }

    private void refreshTrailData() {
        appExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {

                boolean trailExists = (trailDao.hasTrails(getMaxRefreshTime(new Date())).size() != 0);
                Log.d(LOG_TAG, "trailExists = " + trailExists);

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
                                    trail.setLastRefresh(new Date());
                                }

                                appExecutors.diskIO().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        trailDao.insertAll(trails);
                                        Log.d(LOG_TAG, "New data inserted.");
                                    }
                                });
                            }
                        }

                        @Override
                        public void onFailure(Call<TrailList> call, Throwable t) {
                            if(t instanceof IOException) {
                                Log.d(LOG_TAG, "Network connection failed.");
                            } else {
                                Log.d(LOG_TAG, "Data conversion failed.");

                            }
                        }
                    });
                }

            }
        });
    }

    private Date getMaxRefreshTime(Date currentDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        cal.add(Calendar.HOUR, -REFRESH_TIME_IN_HOURS);
        return cal.getTime();
    }

}

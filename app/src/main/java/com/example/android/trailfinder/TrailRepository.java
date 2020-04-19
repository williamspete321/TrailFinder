package com.example.android.trailfinder;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

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

    private final MediatorLiveData<List<Trail>> allTrails;
    private final MediatorLiveData<Trail> randomTrail;

    private TrailRepository(final TrailDao trailDao,
                            final NetworkDataSource networkDataSource,
                            final AppExecutors appExecutors) {
        this.trailDao = trailDao;
        this.networkDataSource = networkDataSource;
        this.appExecutors = appExecutors;

        allTrails = new MediatorLiveData<>();
        randomTrail = new MediatorLiveData<>();
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

    public LiveData<Trail> getRandomTrail() {
        final LiveData<Trail> currentRandomTrail = trailDao.getRandomTrail(getMaxRefreshTime());
        randomTrail.setValue(null);

        randomTrail.addSource(currentRandomTrail, trail -> {
            if (trail == null) {
                loadTrailsFromNetwork();
            } else {
                randomTrail.removeSource(currentRandomTrail);
                randomTrail.postValue(trail);
            }
        });
        return randomTrail;
    }

    public LiveData<Trail> getTrailById(Integer id) {
        return trailDao.getTrailById(id);
    }

    public LiveData<List<Trail>> getAllTrails() {
        final LiveData<List<Trail>> currentTrails = trailDao.getAllTrails(getMaxRefreshTime());
        allTrails.setValue(null);

        allTrails.addSource(currentTrails, trailList -> {
            if (trailList == null || trailList.isEmpty()) {
                loadTrailsFromNetwork();
            } else {
                allTrails.removeSource(currentTrails);
                allTrails.postValue(trailList);
            }
        });
        return allTrails;
    }

    private void loadTrailsFromNetwork() {
        Timber.d("inside loadTrailsFromNetwork");

        networkDataSource.getAllCurrentTrails(
                TrailDummyData.LAT_ATL,
                TrailDummyData.LON_ATL,
                TrailDummyData.MAX_DISTANCE,
                TrailDummyData.KEY)
                .enqueue(new Callback<TrailList>() {
                    @Override
                    public void onResponse(Call<TrailList> call, Response<TrailList> response) {
                        if(response.body() != null) {
                            List<Trail> trails = response.body().getTrails();
                            for (Trail trail : trails) {
                                trail.setLastRefresh(System.currentTimeMillis());
                            }
                            appExecutors.diskIO().execute(() -> {
                                trailDao.deleteAllTrails();
                                trailDao.insertAll(trails);
                                Timber.d("New data inserted.");
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<TrailList> call, Throwable t) {

                    }
                });
    }

    private long getMaxRefreshTime() {
//        return System.currentTimeMillis() - HOURS_IN_MILLIS;
        // For testing
        return System.currentTimeMillis();
    }

}

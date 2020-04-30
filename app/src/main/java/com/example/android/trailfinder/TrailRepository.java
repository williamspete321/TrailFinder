package com.example.android.trailfinder;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.example.android.trailfinder.db.api.NetworkDataSource;
import com.example.android.trailfinder.db.api.TrailDummyData;
import com.example.android.trailfinder.db.api.TrailList;
import com.example.android.trailfinder.db.dao.TrailDao;
import com.example.android.trailfinder.db.entity.Trail;
import com.example.android.trailfinder.utilities.LocationUtils;

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
    private final LocationUtils locationUtils;

    private final MediatorLiveData<List<Trail>> allTrails;
    private final MediatorLiveData<Trail> randomTrail;

    private TrailRepository(final TrailDao trailDao,
                            final NetworkDataSource networkDataSource,
                            final AppExecutors appExecutors,
                            final LocationUtils locationUtils) {

        this.trailDao = trailDao;
        this.networkDataSource = networkDataSource;
        this.appExecutors = appExecutors;
        this.locationUtils = locationUtils;

        allTrails = new MediatorLiveData<>();
        randomTrail = new MediatorLiveData<>();
    }

    public static TrailRepository getInstance(final TrailDao trailDao,
                                              final NetworkDataSource networkDataSource,
                                              final AppExecutors appExecutors,
                                              final LocationUtils locationUtils) {

        if (trailRepository == null) {
            synchronized (TrailRepository.class) {
                trailRepository = new TrailRepository(
                        trailDao,
                        networkDataSource,
                        appExecutors,
                        locationUtils);

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
        Timber.d("Location latitude = %.8f", locationUtils.getLatitude());
        Timber.d("Location longitude = %.8f", locationUtils.getLongitude());
        networkDataSource.getAllCurrentTrails(
                locationUtils.getLatitude(),
                locationUtils.getLongitude(),
                locationUtils.getMaxDistance(),
                locationUtils.getApiKey())
                .enqueue(new Callback<TrailList>() {
                    @Override
                    public void onResponse(Call<TrailList> call, Response<TrailList> response) {
                        if(response.body() != null) {
                            List<Trail> trails = response.body().getTrails();
                            Timber.d("num of trails = " + trails.size());
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

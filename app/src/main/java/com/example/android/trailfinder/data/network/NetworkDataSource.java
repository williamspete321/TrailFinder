package com.example.android.trailfinder.data.network;

import com.example.android.trailfinder.data.executor.AppExecutors;
import com.example.android.trailfinder.data.database.model.TrailList;


import retrofit2.Call;

public class NetworkDataSource {
    private static final String LOG_TAG = NetworkDataSource.class.getSimpleName();

    private final AppExecutors appExecutors;

    private static NetworkDataSource trailNetworkDataSource;
    private static TrailWebService dataService;

    private NetworkDataSource(final AppExecutors appExecutors) {
        this.appExecutors = appExecutors;
        dataService = RetrofitClientInstance.getInstance().create(TrailWebService.class);
    }

    public static NetworkDataSource getInstance(AppExecutors appExecutors) {
        if (trailNetworkDataSource == null) {
            synchronized (NetworkDataSource.class) {
                trailNetworkDataSource = new NetworkDataSource(appExecutors);
            }
        }
        return trailNetworkDataSource;
    }

    public static Call<TrailList> getAllCurrentTrails(double lat,
                                                      double lon,
                                                      int maxDistance,
                                                      String key) {

        return dataService.getAllTrails(lat, lon, maxDistance, key);
    }
}

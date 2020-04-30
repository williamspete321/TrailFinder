package com.example.android.trailfinder.utilities;

import android.content.Context;
import android.location.Location;

import com.example.android.trailfinder.AppExecutors;
import com.example.android.trailfinder.TrailRepository;
import com.example.android.trailfinder.db.AppDatabase;
import com.example.android.trailfinder.db.api.NetworkDataSource;
import com.example.android.trailfinder.viewmodel.TrailDetailViewModelFactory;
import com.example.android.trailfinder.viewmodel.TrailListViewModelFactory;

public class InjectorUtils {

    public static TrailRepository provideRepository(Context context, Location location) {

        AppDatabase appDatabase = AppDatabase.getInstance(context.getApplicationContext());
        AppExecutors appExecutors = AppExecutors.getInstance();
        NetworkDataSource networkDataSource = NetworkDataSource.getInstance(appExecutors);
        LocationUtils locationUtils = LocationUtils.getInstance(location);

        return TrailRepository.getInstance(appDatabase.trailDao(),
                networkDataSource,
                appExecutors,
                locationUtils);
    }

    public static TrailListViewModelFactory provideTrailListViewModelFactory(Context context,
                                                                             Location location) {

        TrailRepository trailRepository = provideRepository(context.getApplicationContext(),
                location);

        return new TrailListViewModelFactory(trailRepository);
    }

    public static TrailDetailViewModelFactory provideTrailDetailViewModelFactory(
            Context context, int trailId, Location location) {

        TrailRepository trailRepository = provideRepository(context.getApplicationContext(),
                location);

        return new TrailDetailViewModelFactory(trailRepository, trailId);
    }


}

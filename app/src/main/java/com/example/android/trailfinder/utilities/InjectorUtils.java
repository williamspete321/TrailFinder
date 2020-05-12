package com.example.android.trailfinder.utilities;

import android.content.Context;
import android.location.Location;

import com.example.android.trailfinder.data.executor.AppExecutors;
import com.example.android.trailfinder.data.repository.TrailRepository;
import com.example.android.trailfinder.data.database.AppDatabase;
import com.example.android.trailfinder.ui.traildetail.TrailDetailViewModelFactory;
import com.example.android.trailfinder.ui.alltrails.AllTrailsViewModelFactory;

public class InjectorUtils {

    public static TrailRepository provideRepository(Context context) {
        AppDatabase appDatabase = AppDatabase.getInstance(context.getApplicationContext());
        AppExecutors appExecutors = AppExecutors.getInstance();
        return TrailRepository.getInstance(appDatabase.trailDao(), appExecutors);
    }

    public static AllTrailsViewModelFactory provideTrailListViewModelFactory(
            Context context, Location userLocation) {
        TrailRepository trailRepository = provideRepository(context.getApplicationContext());
        return new AllTrailsViewModelFactory(trailRepository, userLocation);
    }

    public static TrailDetailViewModelFactory provideTrailDetailViewModelFactory(
            Context context, int trailId, Location userLocation) {
        TrailRepository trailRepository = provideRepository(context.getApplicationContext());
        return new TrailDetailViewModelFactory(trailRepository, trailId, userLocation);
    }


}

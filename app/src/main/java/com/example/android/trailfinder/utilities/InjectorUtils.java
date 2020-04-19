package com.example.android.trailfinder.utilities;

import android.content.Context;

import com.example.android.trailfinder.AppExecutors;
import com.example.android.trailfinder.TrailRepository;
import com.example.android.trailfinder.db.AppDatabase;
import com.example.android.trailfinder.db.api.NetworkDataSource;
import com.example.android.trailfinder.viewmodel.TrailDetailViewModelFactory;
import com.example.android.trailfinder.viewmodel.TrailListViewModelFactory;

public class InjectorUtils {

    public static TrailRepository provideRepository(Context context) {
        AppDatabase appDatabase = AppDatabase.getInstance(context.getApplicationContext());
        AppExecutors appExecutors = AppExecutors.getInstance();
        NetworkDataSource networkDataSource = NetworkDataSource.getInstance(appExecutors);
        return TrailRepository.getInstance(appDatabase.trailDao(), networkDataSource, appExecutors);
    }

    public static TrailListViewModelFactory provideTrailListViewModelFactory(Context context) {
        TrailRepository trailRepository = provideRepository(context.getApplicationContext());
        return new TrailListViewModelFactory(trailRepository);
    }

    public static TrailDetailViewModelFactory provideTrailDetailViewModelFactory(
            Context context, int trailId) {
        TrailRepository trailRepository = provideRepository(context.getApplicationContext());
        return new TrailDetailViewModelFactory(trailRepository, trailId);
    }

}

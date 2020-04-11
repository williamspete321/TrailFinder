package com.example.android.trailfinder.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.android.trailfinder.TrailRepository;
import com.example.android.trailfinder.db.entity.Trail;

import java.util.List;

public class TrailListViewModel extends ViewModel {
    private static final String LOG_TAG = TrailListViewModel.class.getSimpleName();

    private final TrailRepository trailRepository;
    private final LiveData<List<Trail>> allTrails;

    public TrailListViewModel(TrailRepository trailRepository) {
        this.trailRepository = trailRepository;
        Log.d(LOG_TAG, "Actively retrieving trails from the database");
        allTrails = trailRepository.getAllTrails();
    }

    public LiveData<List<Trail>> getAllTrails() {
        return allTrails;
    }

}

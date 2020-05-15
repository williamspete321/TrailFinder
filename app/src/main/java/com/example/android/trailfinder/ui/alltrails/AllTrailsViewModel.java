package com.example.android.trailfinder.ui.alltrails;

import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.example.android.trailfinder.data.repository.TrailRepository;
import com.example.android.trailfinder.data.database.model.Trail;

import java.util.List;

import timber.log.Timber;

public class AllTrailsViewModel extends ViewModel {

    private final LiveData<List<Trail>> allTrails;

    public AllTrailsViewModel(TrailRepository trailRepository, Location userLocation) {
        allTrails = trailRepository.getAllTrails(userLocation);
    }

    public LiveData<List<Trail>> getTrails() {
        return allTrails;
    }

}

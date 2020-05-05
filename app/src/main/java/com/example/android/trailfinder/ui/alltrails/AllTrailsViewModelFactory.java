package com.example.android.trailfinder.ui.alltrails;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.android.trailfinder.data.repository.TrailRepository;

public class AllTrailsViewModelFactory implements ViewModelProvider.Factory {

    private final TrailRepository trailRepository;
    private final Location userLocation;

    public AllTrailsViewModelFactory(TrailRepository repository, Location location) {
        trailRepository = repository;
        userLocation = location;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new AllTrailsViewModel(trailRepository, userLocation);
    }
}

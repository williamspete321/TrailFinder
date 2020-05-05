package com.example.android.trailfinder.ui.traildetail;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.android.trailfinder.data.repository.TrailRepository;
import com.google.android.gms.location.LocationServices;

import timber.log.Timber;

public class TrailDetailViewModelFactory implements ViewModelProvider.Factory {

    private final TrailRepository trailRepository;
    private final int trailId;
    private final Location userLocation;

    public TrailDetailViewModelFactory(TrailRepository repository, int id, Location location) {
        trailRepository = repository;
        trailId = id;
        userLocation = location;
        Timber.d("instance has been created");
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new TrailDetailViewModel(trailRepository, trailId, userLocation);
    }
}

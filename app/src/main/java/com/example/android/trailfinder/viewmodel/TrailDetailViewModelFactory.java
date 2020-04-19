package com.example.android.trailfinder.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.android.trailfinder.TrailRepository;

import timber.log.Timber;

public class TrailDetailViewModelFactory implements ViewModelProvider.Factory {

    private final TrailRepository trailRepository;
    private final int trailId;

    public TrailDetailViewModelFactory(TrailRepository trailRepository, int trailId) {
        this.trailRepository = trailRepository;
        this.trailId = trailId;
        Timber.d("instance has been created");
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new TrailDetailViewModel(trailRepository, trailId);
    }
}

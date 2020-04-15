package com.example.android.trailfinder.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.android.trailfinder.TrailRepository;

public class TrailDetailViewModelFactory implements ViewModelProvider.Factory {

    private final TrailRepository trailRepository;

    public TrailDetailViewModelFactory(TrailRepository trailRepository) {
        this.trailRepository = trailRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new TrailDetailViewModel(trailRepository);
    }
}

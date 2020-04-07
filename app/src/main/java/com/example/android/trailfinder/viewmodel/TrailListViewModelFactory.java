package com.example.android.trailfinder.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.android.trailfinder.TrailRepository;

public class TrailListViewModelFactory implements ViewModelProvider.Factory {

    private final TrailRepository trailRepository;

    public TrailListViewModelFactory(TrailRepository trailRepository) {
        this.trailRepository = trailRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new TrailListViewModel(trailRepository);
    }
}

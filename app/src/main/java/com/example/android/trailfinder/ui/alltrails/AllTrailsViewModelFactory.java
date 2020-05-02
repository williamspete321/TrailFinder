package com.example.android.trailfinder.ui.alltrails;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.android.trailfinder.data.repository.TrailRepository;

public class AllTrailsViewModelFactory implements ViewModelProvider.Factory {

    private final TrailRepository trailRepository;

    public AllTrailsViewModelFactory(TrailRepository trailRepository) {
        this.trailRepository = trailRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new AllTrailsViewModel(trailRepository);
    }
}

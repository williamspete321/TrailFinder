package com.example.android.trailfinder.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.android.trailfinder.TrailRepository;
import com.example.android.trailfinder.db.entity.Trail;

import java.util.List;

public class TrailListViewModel extends ViewModel {

    private final TrailRepository trailRepository;
    private final MediatorLiveData<List<Trail>> allTrails;

    public TrailListViewModel(TrailRepository trailRepository) {
        this.trailRepository = trailRepository;

        allTrails = new MediatorLiveData<>();
        allTrails.setValue(null);

        allTrails.addSource(trailRepository.getAllTrails(), trailList -> {
            if (trailList != null) allTrails.postValue(trailList);
        });

    }

    public LiveData<List<Trail>> getAllTrails() {
        return allTrails;
    }

}

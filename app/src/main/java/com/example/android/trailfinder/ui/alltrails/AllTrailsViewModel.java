package com.example.android.trailfinder.ui.alltrails;

import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.android.trailfinder.data.TrailDummyData;
import com.example.android.trailfinder.data.repository.TrailRepository;
import com.example.android.trailfinder.data.database.model.Trail;

import java.util.List;

public class AllTrailsViewModel extends ViewModel {

    private final LiveData<List<Trail>> allTrails;

    public AllTrailsViewModel(TrailRepository trailRepository, Location userLocation) {
        allTrails = getAllTrails(trailRepository, userLocation);
    }

    public LiveData<List<Trail>> getTrails() {
        return allTrails;
    }

    private LiveData<List<Trail>> getAllTrails(TrailRepository repository, Location location) {
        final LiveData<List<Trail>> currentTrails = repository.getAllTrails(location);

        MediatorLiveData<List<Trail>> updatedTrails = new MediatorLiveData<>();
        updatedTrails.postValue(null);

        updatedTrails.addSource(currentTrails, trails -> {
            if (trails.size() != 0) {
                updatedTrails.removeSource(currentTrails);
                updatedTrails.postValue(trails);
            }
        });
        return updatedTrails;
    }


}

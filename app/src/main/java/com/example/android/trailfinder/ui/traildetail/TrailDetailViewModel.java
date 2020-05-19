package com.example.android.trailfinder.ui.traildetail;


import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.example.android.trailfinder.data.repository.TrailRepository;
import com.example.android.trailfinder.data.database.model.Trail;

import java.util.List;
import java.util.Random;

import timber.log.Timber;

public class TrailDetailViewModel extends ViewModel {

    private final LiveData<Trail> selectedTrail;

    public TrailDetailViewModel(TrailRepository trailRepository, int trailId, Location userLocation) {
        selectedTrail = selectTrail(trailRepository, trailId, userLocation);
        Timber.d("ViewModel has been created");

    }

    public LiveData<Trail> getTrail() {
        return selectedTrail;
    }

    private LiveData<Trail> selectTrail(TrailRepository repository, int id, Location location) {
        if (id == 0) {
            return getRandomTrail(repository, location);
        } else {
            return getTrailById(repository, id, location);
        }
    }

    private LiveData<Trail> getRandomTrail(TrailRepository repository, Location location) {
        LiveData<List<Trail>> currentAllTrails = repository.getAllTrails(location);

        // When this method is called, it may be the first time the user is accessing trail data.
        // Because LiveData will first return null (as it works asynchronously,
        // off the main thread), we need to use MediatorLiveData to wait for a valid LiveData Trail
        // After trails have been loaded, we'll select a random one for the selectedTrail object.
        MediatorLiveData<Trail> randomTrail = new MediatorLiveData<>();
        randomTrail.postValue(null);

        randomTrail.addSource(currentAllTrails, allTrails -> {
            if (allTrails.size() != 0) {
                Trail trail = allTrails.get(new Random().nextInt(allTrails.size()));
                randomTrail.removeSource(currentAllTrails);
                randomTrail.postValue(trail);
            }
        });
        return randomTrail;
    }

    private LiveData<Trail> getTrailById(TrailRepository repository, int id, Location location) {
        // MediatorLiveData is not necessary, as this method will only be called if valid Trail
        // objects are already populated in the database.
        return repository.getTrailById(id, location);
    }

}

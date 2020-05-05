package com.example.android.trailfinder.ui.traildetail;


import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.example.android.trailfinder.data.repository.TrailRepository;
import com.example.android.trailfinder.data.database.model.Trail;

import java.util.List;
import java.util.Random;

public class TrailDetailViewModel extends ViewModel {

    private final LiveData<Trail> selectedTrail;

    public TrailDetailViewModel(TrailRepository trailRepository, int trailId, Location userLocation) {
        selectedTrail = selectTrail(trailRepository, trailId, userLocation);
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

    private LiveData<Trail> getTrailById(TrailRepository repository, int id, Location location) {
        LiveData<Trail> currentTrail = repository.getTrailById(id, location);

        MediatorLiveData<Trail> trailById = new MediatorLiveData<>();
        trailById.postValue(null);

        trailById.addSource(currentTrail, trail -> {
            if(trail != null) {
                trailById.removeSource(currentTrail);
                trailById.postValue(trail);
            }
        });
        return trailById;
    }

    private LiveData<Trail> getRandomTrail(TrailRepository repository, Location location) {
        LiveData<List<Trail>> currentAllTrails = repository.getAllTrails(location);

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

}

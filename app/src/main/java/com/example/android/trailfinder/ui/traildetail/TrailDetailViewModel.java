package com.example.android.trailfinder.ui.traildetail;


import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.example.android.trailfinder.data.TrailDummyData;
import com.example.android.trailfinder.data.repository.TrailRepository;
import com.example.android.trailfinder.data.database.model.Trail;

import java.util.List;
import java.util.Random;

public class TrailDetailViewModel extends ViewModel {

    private final TrailRepository trailRepository;
    private final LiveData<Trail> selectedTrail;

    public TrailDetailViewModel(TrailRepository trailRepository, int trailId) {

        Location currentLocation = new Location("");
        currentLocation.setLatitude(TrailDummyData.LAT_ATL);
        currentLocation.setLongitude(TrailDummyData.LON_ATL);

        this.trailRepository = trailRepository;
        selectedTrail = selectTrail(trailId, currentLocation);
    }

    public LiveData<Trail> getTrail() {
        return selectedTrail;
    }

    private LiveData<Trail> selectTrail(int id, Location location) {
        if (id == 0) {
            return getRandomTrail(location);
        } else {
            return getTrailById(id, location);
        }
    }

    private LiveData<Trail> getTrailById(int id, Location location) {
        LiveData<Trail> currentTrail = trailRepository.getTrailById(id, location);

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

    private LiveData<Trail> getRandomTrail(Location location) {
        LiveData<List<Trail>> currentAllTrails = trailRepository.getAllTrails(location);

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

package com.example.android.trailfinder.ui.alltrails;

import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.android.trailfinder.data.TrailDummyData;
import com.example.android.trailfinder.data.repository.TrailRepository;
import com.example.android.trailfinder.data.database.model.Trail;

import java.util.List;

public class AllTrailsViewModel extends ViewModel {

    private final TrailRepository trailRepository;
    private final LiveData<List<Trail>> allTrails;

    public AllTrailsViewModel(TrailRepository trailRepository) {
        this.trailRepository = trailRepository;

        Location currentLocation = new Location("");
        currentLocation.setLatitude(TrailDummyData.LAT_ATL);
        currentLocation.setLongitude(TrailDummyData.LON_ATL);

        allTrails = trailRepository.getAllTrails(currentLocation);
    }

    public LiveData<List<Trail>> getAllTrails() {
        return allTrails;
    }

}

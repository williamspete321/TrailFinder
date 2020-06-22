package first.project.android.trailfinder.ui.alltrails;

import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import first.project.android.trailfinder.data.repository.TrailRepository;
import first.project.android.trailfinder.data.database.model.Trail;

import java.util.List;

public class AllTrailsViewModel extends ViewModel {

    private final LiveData<List<Trail>> allTrails;

    public AllTrailsViewModel(TrailRepository trailRepository, Location userLocation) {
        allTrails = trailRepository.getAllTrails(userLocation);
    }

    public LiveData<List<Trail>> getTrails() {
        return allTrails;
    }

}

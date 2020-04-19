package com.example.android.trailfinder.viewmodel;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.android.trailfinder.TrailRepository;
import com.example.android.trailfinder.db.entity.Trail;

public class TrailDetailViewModel extends ViewModel {

    private final TrailRepository trailRepository;
    private final LiveData<Trail> trail;
    private final LiveData<Trail> randomTrail;

    public TrailDetailViewModel(TrailRepository trailRepository, int trailId) {
        this.trailRepository = trailRepository;
        this.trail = trailRepository.getTrailById(trailId);
        this.randomTrail = trailRepository.getRandomTrail();
    }

    public LiveData<Trail> getTrail(int id) {
        if (id == 0) {
            return randomTrail;
        } else {
            return trail;
        }
    }

}

package com.example.android.trailfinder.viewmodel;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.example.android.trailfinder.TrailRepository;
import com.example.android.trailfinder.db.entity.Trail;

public class TrailDetailViewModel extends ViewModel {

    private final TrailRepository trailRepository;
    private final MediatorLiveData<Trail> randomTrail;

    public TrailDetailViewModel(TrailRepository trailRepository) {
        this.trailRepository = trailRepository;

        randomTrail = new MediatorLiveData<>();
        randomTrail.setValue(null);

        randomTrail.addSource(trailRepository.getRandomTrail(), trail -> {
            if(trail != null) randomTrail.postValue(trail);
        });
    }

    public LiveData<Trail> getTrailById(int id) {
        return trailRepository.getTrailById(id);
    }

    public LiveData<Trail> getRandomTrail() {
        return randomTrail;
    }

}

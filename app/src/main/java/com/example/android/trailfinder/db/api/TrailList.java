package com.example.android.trailfinder.db.api;

import com.example.android.trailfinder.db.entity.Trail;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrailList {
    @SerializedName("trails")
    private List<Trail> trails;

    public TrailList(List<Trail> trails) {
        this.trails = trails;
    }

    public List<Trail> getTrails() {
        return trails;
    }

    public void setTrails(List<Trail> trails) {
        this.trails = trails;
    }
}

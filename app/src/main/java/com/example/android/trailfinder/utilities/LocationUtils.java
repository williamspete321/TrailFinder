package com.example.android.trailfinder.utilities;

import android.location.Location;

import com.google.android.gms.location.FusedLocationProviderClient;

public class LocationUtils {

    private static LocationUtils instance;

    private final double latitude;
    private final double longitude;
    private final int maxDistance = 25;
    private final String apiKey = "insert-key-here";

    private LocationUtils(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }

    public static LocationUtils getInstance(Location location) {

        if (instance == null) {
            synchronized (LocationUtils.class) {
                instance = new LocationUtils(location);
            }
        }
        return instance;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getMaxDistance() {
        return maxDistance;
    }

    public String getApiKey() {
        return apiKey;
    }

}

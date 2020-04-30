package com.example.android.trailfinder.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.android.trailfinder.databinding.FragmentMainActivityBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainActivityFragment extends Fragment {

    private FragmentMainActivityBinding binding;

    private static final int REQUEST_LOCATION_PERMISSION = 1;
    public static final String LOCATION_LAT = "user-location-latitude";
    public static final String LOCATION_LON = "user-location-longitude";
    public static final String LOCATION_COORD = "user-location-coordinates";

    private Location lastLocation;
    private FusedLocationProviderClient fusedLocationClient;

    public MainActivityFragment() {
    }

    public static MainActivityFragment newInstance() {
        return new MainActivityFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMainActivityBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.selectRandomTrailButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), TrailDetailActivity.class);
            startTrailActivity(intent);
        });

        binding.selectTrailListButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), TrailListActivity.class);
            startTrailActivity(intent);
        });

        requestLocationPermission();

        return view;
    }

    private void startTrailActivity(Intent intent) {
        if(lastLocation != null) {
            Bundle coordinates = new Bundle();
            coordinates.putDouble(LOCATION_LAT, lastLocation.getLatitude());
            coordinates.putDouble(LOCATION_LON, lastLocation.getLongitude());
            intent.putExtra(LOCATION_COORD, coordinates);
            startActivity(intent);
        } else {
            Toast.makeText(getActivity(), "Location Is Unavailable", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void requestLocationPermission() {
        if(ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        } else {
            getLastLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION:
                if(grantResults.length > 0) {
                    if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        requestLocationPermission();
                    } else {
                        getActivity().finishAndRemoveTask();
                    }
                }
                break;
        }
    }

    private void getLastLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                    if(location != null) {
                        lastLocation = location;
                        Timber.d("LOCATION: \n" + "Latitude: " + lastLocation.getLatitude()
                                + "\n Longitude: " + lastLocation.getLongitude());
                    }
                });
    }

}

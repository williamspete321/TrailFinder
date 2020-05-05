package com.example.android.trailfinder.ui.traildetail;

import android.location.Location;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.trailfinder.R;
import com.example.android.trailfinder.databinding.FragmentTrailDetailBinding;
import com.example.android.trailfinder.data.database.model.Trail;
import com.example.android.trailfinder.utilities.InjectorUtils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Locale;

import timber.log.Timber;

public class TrailDetailFragment extends Fragment implements OnMapReadyCallback {

    private FragmentTrailDetailBinding binding;

    public static final String ID = "trail-id";

    private int trailId;

    private TrailDetailViewModel viewModel;
    private TrailDetailViewModelFactory factory;
    private Trail currentTrail;

    private FusedLocationProviderClient fusedLocationClient;
    private Location userLocation;
    private SupportMapFragment mapFragment;
    private GoogleMap map;

    public TrailDetailFragment() {
    }

    public static TrailDetailFragment newInstance(Bundle arguments) {
        TrailDetailFragment fragment = new TrailDetailFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentTrailDetailBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.fragmentDetailToolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        if(getArguments() != null) {
            trailId = getArguments().getInt(ID);
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        getLastLocation();

        return view;
    }

    private void getLastLocation() {
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), location -> {
                    if(location != null) {
                        userLocation = location;
                        setupUI();
                    }
                });
    }

    private void setupUI() {
        setupViewModel();
    }

    private void setupViewModel() {
        factory = InjectorUtils.provideTrailDetailViewModelFactory(
                getActivity().getApplicationContext(), trailId, userLocation);

        viewModel = new ViewModelProvider(this, factory).get(TrailDetailViewModel.class);

        viewModel.getTrail().observe(getViewLifecycleOwner(), trail -> {
            if(trail != null) {
                currentTrail = trail;
                binding.setTrail(currentTrail);
                setupMap();
                Timber.d("Non-null trail has been returned by LiveData");
            }
            Timber.d("Updating trail from LiveData in ViewModel");
        });
    }

    private void setupMap() {
        mapFragment = SupportMapFragment.newInstance();
        getChildFragmentManager().beginTransaction()
                .add(R.id.map_fragment_container, mapFragment).commit();
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        map = googleMap;

        double lat = currentTrail.getLatitude();
        double lon = currentTrail.getLongitude();
        float zoom = 10;
        LatLng trailLocation = new LatLng(lat,lon);
        String snippet = String.format(Locale.getDefault(),
                getString(R.string.trail_lat_lon_snippet),
                lat,lon);

        map.addMarker(new MarkerOptions()
                .position(trailLocation)
                .title(currentTrail.getName())
                .snippet(snippet));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(trailLocation,zoom));
        map.setMyLocationEnabled(true);
    }

}

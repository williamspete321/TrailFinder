package com.example.android.trailfinder.ui.traildetail;

import android.location.Location;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.trailfinder.R;
import com.example.android.trailfinder.databinding.FragmentTrailDetailBinding;
import com.example.android.trailfinder.utilities.InjectorUtils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.appbar.AppBarLayout;

import java.util.Locale;

import timber.log.Timber;

public class TrailDetailFragment extends Fragment implements OnMapReadyCallback {

    private FragmentTrailDetailBinding binding;

    public static final String ID = "trail-id";
    private int trailId;

    private SupportMapFragment mapFragment;
    private GoogleMap map;

    private NestedScrollView nestedScrollView;
    private boolean toolbarLocked = false;

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

        if(getArguments() != null) {
            trailId = getArguments().getInt(ID);
        }

        binding = FragmentTrailDetailBinding.inflate(inflater, container, false);
        nestedScrollView = binding.trailDetailScrollingLayout.nestedScrollView;

        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.fragmentDetailToolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        enableToolbarLock();
        setupMap();
        getLastLocation();

        return binding.getRoot();
    }

    private void getLastLocation() {
        Timber.d("getLastLocation called");

        FusedLocationProviderClient fusedLocationClient = LocationServices
                .getFusedLocationProviderClient(getActivity());
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), location -> {
                    if(location != null) {
                        setupViewModel(location);
                    }
                });
    }

    private void setupViewModel(Location location) {
        TrailDetailViewModelFactory factory = InjectorUtils.provideTrailDetailViewModelFactory(
                getActivity().getApplicationContext(), trailId, location);

        TrailDetailViewModel viewModel = new ViewModelProvider(this, factory)
                .get(TrailDetailViewModel.class);

        viewModel.getTrail().observe(getViewLifecycleOwner(), trail -> {
            if(trail != null) {
                binding.setTrail(trail);
                addTrailMarker();
                Timber.d("Non-null trail has been returned by LiveData");
            }
            Timber.d("Updating trail from LiveData in ViewModel");
        });

    }

    private void setupMap() {
        if(mapFragment == null) {

            mapFragment = (MyMapFragment) getChildFragmentManager()
                    .findFragmentById(R.id.map_fragment_container);
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Timber.d("onMapReady called");
        googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        map = googleMap;
        map.setMyLocationEnabled(true);

        // Disable scrollview when touch event is over map
        ((MyMapFragment) getChildFragmentManager().findFragmentById(R.id.map_fragment_container))
                .setOnTouchListener(() ->
                        nestedScrollView.requestDisallowInterceptTouchEvent(true));
    }

    private void addTrailMarker() {
        double lat = binding.getTrail().getLatitude();
        double lon = binding.getTrail().getLongitude();
        float zoom = 10;
        LatLng trailLocation = new LatLng(lat,lon);
        String snippet = String.format(Locale.getDefault(),
                getString(R.string.trail_lat_lon_snippet),
                lat,lon);

        map.addMarker(new MarkerOptions()
                .position(trailLocation)
                .title(binding.getTrail().getName())
                .snippet(snippet));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(trailLocation,zoom));
    }

    private void enableToolbarLock() {
        AppBarLayout appBar = binding.appBar;
        appBar.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            if(canDisableScrollView()) disableScrollViewWhenNeeded(appBarLayout, verticalOffset);
        });
    }

    private boolean canDisableScrollView() {
        int scrollViewHeight = nestedScrollView.getHeight();
        int rootLayoutHeight = binding.getRoot().getHeight();
        // If the entire scrollview's content can fit on the screen after the toolbar is collapsed
        // then it is safe to disable scrolling
        return scrollViewHeight < rootLayoutHeight;
    }

    private void disableScrollViewWhenNeeded(AppBarLayout appBarLayout, int verticalOffset) {
        if(Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange()) {
            // Toolbar is completely collapsed, we want to "lock" it by disabling scrollview
            if(!toolbarLocked) {
                toolbarLocked = true;
                ViewCompat.setNestedScrollingEnabled(nestedScrollView, false);
            }
        } else if(verticalOffset <= 0) {
            // Toolbar is being expanded, we enable scrolling again so all UI content is visible
            if(toolbarLocked) {
                toolbarLocked = false;
                ViewCompat.setNestedScrollingEnabled(nestedScrollView, true);
            }
        }
    }

}

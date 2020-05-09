package com.example.android.trailfinder.ui.traildetail;

import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

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
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.Locale;

import timber.log.Timber;

public class TrailDetailFragment extends Fragment implements OnMapReadyCallback {

    private FragmentTrailDetailBinding binding;

    public static final String ID = "trail-id";

    private int trailId;

    private FusedLocationProviderClient fusedLocationClient;
    private Location userLocation;
    private SupportMapFragment mapFragment;
    private GoogleMap map;

    private AppBarLayout appBar;
    private NestedScrollView nestedScrollView;
    private ViewGroup rootLayout;
    private boolean toolbarLocked = false;

    private TrailDetailViewModel viewModel;
    private TrailDetailViewModelFactory factory;
    private Trail currentTrail;

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

        getLastLocation();

        return view;
    }

    private void getLastLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), location -> {
                    if(location != null) {
                        userLocation = location;
                        setupUI();
                    }
                });
    }

    private void setupUI() {
        setupAppBar();
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
        if(mapFragment == null) {

            mapFragment = (MyMapFragment) getChildFragmentManager()
                    .findFragmentById(R.id.map_fragment_container);
            mapFragment.getMapAsync(this);
        }
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

        // Disable scrollview when touch event is over map
        ((MyMapFragment) getChildFragmentManager().findFragmentById(R.id.map_fragment_container))
                .setOnTouchListener(() -> nestedScrollView.requestDisallowInterceptTouchEvent(true));
    }

    private void setupAppBar() {
        appBar = binding.appBar;
        nestedScrollView = binding.trailDetailScrollingLayout.nestedScrollView;
        rootLayout = (ViewGroup) binding.getRoot();

        appBar.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {

            if(canDisableScrollView()) disableScrollViewWhenNeeded(appBarLayout, verticalOffset);

        });
    }

    private boolean canDisableScrollView() {
        int scrollViewHeight = nestedScrollView.getHeight();
        int rootLayoutHeight = rootLayout.getHeight();
        // If the entire scrollview's content can fit on the screen after the toolbar is collapsed
        // then it is safe to disable scrolling
        if(scrollViewHeight < rootLayoutHeight) {
            return true;
        }
        return false;
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

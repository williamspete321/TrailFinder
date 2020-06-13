package com.example.android.trailfinder.ui.alltrails;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.trailfinder.R;
import com.example.android.trailfinder.databinding.FragmentTrailListBinding;
import com.example.android.trailfinder.data.database.model.Trail;
import com.example.android.trailfinder.ui.OnTrailLoadedListener;
import com.example.android.trailfinder.ui.main.MainActivityFragment;
import com.example.android.trailfinder.ui.traildetail.TrailDetailActivity;
import com.example.android.trailfinder.ui.traildetail.TrailDetailFragment;
import com.example.android.trailfinder.utilities.InjectorUtils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import timber.log.Timber;

public class AllTrailsFragment extends Fragment
        implements AllTrailsAdapter.OnItemClickListener {

    private OnTrailLoadedListener listener;

    private FragmentTrailListBinding binding;

    private AllTrailsAdapter adapter;

    private FusedLocationProviderClient fusedLocationClient;
    private Location userLocation;

    public AllTrailsFragment() {
    }

    public static AllTrailsFragment newInstance() {
        return new AllTrailsFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (OnTrailLoadedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
            + getString(R.string.class_cast_exception_message));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentTrailListBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        setupRecyclerView();
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

    private void setupRecyclerView() {
        RecyclerView recyclerView = binding.trailListRecyclerview;
        int numberOfColumns = 1;
        int viewMargin = 16;
        if(isLandscape()) {
            numberOfColumns = 2;
        }
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),numberOfColumns));
        recyclerView.addItemDecoration(new AllTrailsRecyclerViewMargin(numberOfColumns,viewMargin));
        adapter = new AllTrailsAdapter(getActivity());
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    private boolean isLandscape() {
        return getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;
    }

    private void setupViewModel() {
        AllTrailsViewModelFactory factory = InjectorUtils
                .provideTrailListViewModelFactory(getActivity()
                .getApplicationContext(), userLocation);

        AllTrailsViewModel viewModel = new ViewModelProvider(this, factory)
                .get(AllTrailsViewModel.class);

        viewModel.getTrails().observe(getViewLifecycleOwner(), trails -> {
            Timber.d("LiveData has been returned");
            adapter.setData(trails);
            listener.updateProgressBar();
        });
    }

    @Override
    public void onDestroyView() {
        binding = null;
        adapter = null;
        super.onDestroyView();
    }

    @Override
    public void onItemClick(View view, Trail trail) {
        int trailId = trail.getId();
        Intent intent = new Intent(getActivity(), TrailDetailActivity.class);
        intent.putExtra(TrailDetailFragment.ID, trailId);
        startActivity(intent);
    }
}

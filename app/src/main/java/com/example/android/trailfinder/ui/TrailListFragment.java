package com.example.android.trailfinder.ui;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.trailfinder.databinding.FragmentTrailListBinding;
import com.example.android.trailfinder.db.entity.Trail;
import com.example.android.trailfinder.utilities.InjectorUtils;
import com.example.android.trailfinder.viewmodel.TrailListViewModel;
import com.example.android.trailfinder.viewmodel.TrailListViewModelFactory;

import timber.log.Timber;

public class TrailListFragment extends Fragment
        implements TrailListAdapter.OnItemClickListener {

    private FragmentTrailListBinding binding;

    private Location location;

    private RecyclerView recyclerView;
    private TrailListAdapter adapter;
    private TrailListViewModel viewModel;
    private TrailListViewModelFactory factory;

    public TrailListFragment() {
    }

    public static TrailListFragment newInstance() {
        return new TrailListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentTrailListBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        if(getArguments() != null) {
            location = new Location("");
            location.setLatitude(getArguments().getDouble(MainActivityFragment.LOCATION_LAT));
            location.setLongitude(getArguments().getDouble(MainActivityFragment.LOCATION_LON));
        }

        setupUI();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViewModel();
    }

    private void setupUI() {
        recyclerView = binding.trailListRecyclerview;
        adapter = new TrailListAdapter(getActivity());
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    private void setupViewModel() {
        factory = InjectorUtils.provideTrailListViewModelFactory(
                getActivity().getApplicationContext(), location);

        viewModel = new ViewModelProvider(this, factory).get(TrailListViewModel.class);

        viewModel.getAllTrails().observe(getViewLifecycleOwner(), trails -> {
            Timber.d("Updating list of trails from LiveData in ViewModel");
            adapter.setData(trails);
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

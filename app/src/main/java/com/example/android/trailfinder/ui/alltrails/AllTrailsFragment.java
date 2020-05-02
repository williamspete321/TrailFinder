package com.example.android.trailfinder.ui.alltrails;

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
import com.example.android.trailfinder.data.database.model.Trail;
import com.example.android.trailfinder.ui.main.MainActivityFragment;
import com.example.android.trailfinder.ui.traildetail.TrailDetailActivity;
import com.example.android.trailfinder.ui.traildetail.TrailDetailFragment;
import com.example.android.trailfinder.utilities.InjectorUtils;

import timber.log.Timber;

public class AllTrailsFragment extends Fragment
        implements AllTrailsAdapter.OnItemClickListener {

    private FragmentTrailListBinding binding;

    private Location location;

    private RecyclerView recyclerView;
    private AllTrailsAdapter adapter;
    private AllTrailsViewModel viewModel;
    private AllTrailsViewModelFactory factory;

    public AllTrailsFragment() {
    }

    public static AllTrailsFragment newInstance() {
        return new AllTrailsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentTrailListBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

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
        adapter = new AllTrailsAdapter(getActivity());
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    private void setupViewModel() {
        factory = InjectorUtils.provideTrailListViewModelFactory(getActivity()
                .getApplicationContext());

        viewModel = new ViewModelProvider(this, factory).get(AllTrailsViewModel.class);

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

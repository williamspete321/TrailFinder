package com.example.android.trailfinder.ui.traildetail;

import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.trailfinder.databinding.FragmentTrailDetailBinding;
import com.example.android.trailfinder.data.database.model.Trail;
import com.example.android.trailfinder.utilities.InjectorUtils;

import timber.log.Timber;

public class TrailDetailFragment extends Fragment {

    private FragmentTrailDetailBinding binding;

    public static final String ID = "trail-id";

    private int trailId;

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

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViewModel();
    }

    private void setupViewModel() {
        factory = InjectorUtils.provideTrailDetailViewModelFactory(
                getActivity().getApplicationContext(), trailId);

        viewModel = new ViewModelProvider(this, factory).get(TrailDetailViewModel.class);

        viewModel.getTrail().observe(getViewLifecycleOwner(), trail -> {
            currentTrail = trail;
            binding.setTrail(currentTrail);
            Timber.d("Updating trail from LiveData in ViewModel");
        });
    }

}

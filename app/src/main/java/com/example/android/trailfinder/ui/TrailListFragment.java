package com.example.android.trailfinder.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.trailfinder.databinding.FragmentTrailListBinding;
import com.example.android.trailfinder.utilities.InjectorUtils;
import com.example.android.trailfinder.viewmodel.TrailListViewModel;
import com.example.android.trailfinder.viewmodel.TrailListViewModelFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class TrailListFragment extends Fragment {
    private static final String LOG_TAG = TrailListFragment.class.getSimpleName();

    private RecyclerView recyclerView;
    private TrailListAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private TrailListViewModel viewModel;
    private TrailListViewModelFactory factory;

    private FragmentTrailListBinding binding;

    public TrailListFragment() {
        // Required empty public constructor
    }

    public static TrailListFragment newInstance() {
        return new TrailListFragment();
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
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        adapter = new TrailListAdapter(getActivity());
        recyclerView.setAdapter(adapter);
    }

    private void setupViewModel() {
        factory = InjectorUtils.provideTrailListViewModelFactory(
                getActivity().getApplicationContext());

        viewModel = new ViewModelProvider(this, factory).get(TrailListViewModel.class);

        viewModel.getAllTrails().observe(getViewLifecycleOwner(), trails -> {
            Log.d(LOG_TAG, "Updating list of trails from LiveData in ViewModel");
            adapter.setData(trails);
        });
    }

    @Override
    public void onDestroyView() {
        binding = null;
        adapter = null;
        super.onDestroyView();
    }

}

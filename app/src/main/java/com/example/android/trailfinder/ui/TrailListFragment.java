package com.example.android.trailfinder.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.trailfinder.databinding.FragmentTrailListBinding;
import com.example.android.trailfinder.db.entity.Trail;
import com.example.android.trailfinder.utilities.InjectorUtils;
import com.example.android.trailfinder.viewmodel.TrailListViewModel;
import com.example.android.trailfinder.viewmodel.TrailListViewModelFactory;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TrailListFragment extends Fragment {
    private static final String LOG_TAG = TrailListFragment.class.getSimpleName();

    private RecyclerView recyclerView;
    private TrailListAdapter adapter;

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

    private void setupUI() {
        recyclerView = binding.trailListRecyclerview;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        adapter = new TrailListAdapter(getActivity());
        recyclerView.setAdapter(adapter);

        TrailListViewModelFactory factory
                = InjectorUtils.provideTrailListViewModelFactory(
                        getActivity().getApplicationContext());

        TrailListViewModel viewModel
                = new ViewModelProvider(this, factory).get(TrailListViewModel.class);

        viewModel.getAllTrails()
                .observe(getViewLifecycleOwner(),
                new Observer<List<Trail>>() {
            @Override
            public void onChanged(List<Trail> trails) {
                Log.d(LOG_TAG, "onChanged is called from Observer in ViewModel");
                adapter.setData(trails);
            }
        });
    }

    @Override
    public void onDestroyView() {
        binding = null;
        adapter = null;
        super.onDestroyView();
    }

}

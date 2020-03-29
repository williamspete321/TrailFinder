package com.example.android.trailfinder.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.trailfinder.databinding.FragmentTrailListBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class TrailListFragment extends Fragment {

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
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

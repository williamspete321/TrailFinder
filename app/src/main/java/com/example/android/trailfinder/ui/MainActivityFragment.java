package com.example.android.trailfinder.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.trailfinder.R;
import com.example.android.trailfinder.databinding.FragmentMainActivityBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainActivityFragment extends Fragment {

    private FragmentMainActivityBinding binding;

    public MainActivityFragment() {
    }

    public static MainActivityFragment newInstance() {
        return new MainActivityFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMainActivityBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.selectTrailListButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivityFragment.this.getActivity(), TrailListActivity.class);
            MainActivityFragment.this.startActivity(intent);
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

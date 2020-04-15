package com.example.android.trailfinder.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.android.trailfinder.R;

public class TrailDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trail_detail);

        if(savedInstanceState == null) {

            int trailId = 0;

            if(getIntent().hasExtra(TrailDetailFragment.ID)) {
               trailId = getIntent().getIntExtra(TrailDetailFragment.ID, 0);
            }

            TrailDetailFragment trailDetailFragment = TrailDetailFragment.newInstance(trailId);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.fragment_detail_container, trailDetailFragment).commit();
        }
    }
}

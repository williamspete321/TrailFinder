package com.example.android.trailfinder.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.android.trailfinder.R;
import com.example.android.trailfinder.databinding.ActivityTrailListBinding;

public class TrailListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityTrailListBinding activityTrailListBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_trail_list);
        setSupportActionBar(activityTrailListBinding.activityListToolbar);

        if (savedInstanceState == null) {

            TrailListFragment trailListFragment = TrailListFragment.newInstance();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.fragment_list_container, trailListFragment)
                    .commit();

        }

    }
}

package com.example.android.trailfinder.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;

import com.example.android.trailfinder.R;

public class TrailListActivity extends AppCompatActivity {

    private static final String TAG = TrailListActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trail_list);

        if (savedInstanceState == null) {

            TrailListFragment trailListFragment = TrailListFragment.newInstance();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.fragment_list_container, trailListFragment)
                    .commit();

        }

    }
}

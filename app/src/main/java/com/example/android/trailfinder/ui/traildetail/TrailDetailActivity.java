package com.example.android.trailfinder.ui.traildetail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.android.trailfinder.R;
import com.example.android.trailfinder.TrailWidgetProvider;
import com.example.android.trailfinder.databinding.ActivityTrailDetailBinding;
import com.example.android.trailfinder.ui.main.MainActivityFragment;

public class TrailDetailActivity extends AppCompatActivity {

    private int trailId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityTrailDetailBinding activityTrailDetailBinding
                = DataBindingUtil.setContentView(this, R.layout.activity_trail_detail);

        if(savedInstanceState == null) {

            trailId = 0;

            if(getIntent().hasExtra(TrailDetailFragment.ID)) {
                trailId = getIntent().getIntExtra(TrailDetailFragment.ID, 0);
            }

            Bundle arguments = new Bundle();
            arguments.putInt(TrailDetailFragment.ID, trailId);

            TrailDetailFragment trailDetailFragment = TrailDetailFragment.newInstance(arguments);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.fragment_detail_container, trailDetailFragment).commit();

        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();

    }
}

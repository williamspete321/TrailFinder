package com.example.android.trailfinder.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.android.trailfinder.R;
import com.example.android.trailfinder.databinding.ActivityTrailDetailBinding;

import timber.log.Timber;

public class TrailDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityTrailDetailBinding activityTrailDetailBinding
                = DataBindingUtil.setContentView(this, R.layout.activity_trail_detail);

        if(savedInstanceState == null) {

            int trailId = 0;
            double locationLatitude = 0;
            double locationLongitude = 0;

            if(getIntent().hasExtra(TrailDetailFragment.ID)) {
                trailId = getIntent().getIntExtra(TrailDetailFragment.ID, 0);
            }

            if(getIntent().hasExtra(MainActivityFragment.LOCATION_COORD)) {
                Bundle extras = getIntent().getBundleExtra(MainActivityFragment.LOCATION_COORD);

                locationLatitude = extras.getDouble(MainActivityFragment.LOCATION_LAT);
                locationLongitude = extras.getDouble(MainActivityFragment.LOCATION_LON);

            }

            Bundle arguments = new Bundle();
            arguments.putInt(TrailDetailFragment.ID, trailId);
            arguments.putDouble(MainActivityFragment.LOCATION_LAT, locationLatitude);
            arguments.putDouble(MainActivityFragment.LOCATION_LON, locationLongitude);


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

}

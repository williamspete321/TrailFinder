package com.example.android.trailfinder.ui.traildetail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.android.trailfinder.R;
import com.example.android.trailfinder.databinding.ActivityTrailDetailBinding;
import com.example.android.trailfinder.ui.main.MainActivityFragment;

public class TrailDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityTrailDetailBinding activityTrailDetailBinding
                = DataBindingUtil.setContentView(this, R.layout.activity_trail_detail);

        if(savedInstanceState == null) {

            int trailId = 0;

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

}

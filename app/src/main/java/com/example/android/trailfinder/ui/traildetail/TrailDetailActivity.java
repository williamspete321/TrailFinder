package com.example.android.trailfinder.ui.traildetail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.example.android.trailfinder.R;
import com.example.android.trailfinder.databinding.ActivityTrailDetailBinding;
import com.example.android.trailfinder.ui.OnTrailLoadedListener;

public class TrailDetailActivity extends AppCompatActivity
        implements OnTrailLoadedListener {

    private ProgressBar progressBar;
    private FrameLayout frameLayoutFragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityTrailDetailBinding binding
                = DataBindingUtil.setContentView(this, R.layout.activity_trail_detail);

        progressBar = binding.progressBarTrailDetail;
        frameLayoutFragmentContainer = binding.fragmentDetailContainer;

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

    @Override
    public void updateProgressBar() {
        if(progressBar.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.INVISIBLE);
            frameLayoutFragmentContainer.setVisibility(View.VISIBLE);
        }
    }
}

package com.example.android.trailfinder.ui.alltrails;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.example.android.trailfinder.R;
import com.example.android.trailfinder.databinding.ActivityTrailListBinding;
import com.example.android.trailfinder.ui.OnTrailLoadedListener;

public class AllTrailsActivity extends AppCompatActivity
        implements OnTrailLoadedListener {

    private ProgressBar progressBar;
    private FrameLayout frameLayoutFragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityTrailListBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_trail_list);

        progressBar = binding.progressBarTrailList;
        frameLayoutFragmentContainer = binding.fragmentListContainer;

        setSupportActionBar(binding.activityListToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        if (savedInstanceState == null) {

            AllTrailsFragment allTrailsFragment = AllTrailsFragment.newInstance();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.fragment_list_container, allTrailsFragment)
                    .commit();

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

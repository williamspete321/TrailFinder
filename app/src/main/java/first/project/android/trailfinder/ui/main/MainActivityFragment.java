package first.project.android.trailfinder.ui.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.android.trailfinder.R;
import first.project.android.trailfinder.data.executor.AppExecutors;
import com.example.android.trailfinder.databinding.FragmentMainActivityBinding;

import first.project.android.trailfinder.ui.traildetail.TrailDetailActivity;
import first.project.android.trailfinder.ui.alltrails.AllTrailsActivity;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import timber.log.Timber;

public class MainActivityFragment extends Fragment {

    private FragmentMainActivityBinding binding;

    private static final int REQUEST_LOCATION_PERMISSION = 1;

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

        binding.selectRandomTrailButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), TrailDetailActivity.class);
            startTrailActivity(intent);
        });

        binding.selectTrailListButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AllTrailsActivity.class);
            startTrailActivity(intent);
        });

        requestLocationPermission();

        return view;
    }

    private void startTrailActivity(Intent intent) {

        // Check network connection first on worker thread
        AppExecutors.getInstance().diskIO().execute(() -> {
            try {
                int timeoutMs = 1500;
                Socket socket = new Socket();
                SocketAddress socketAddress = new InetSocketAddress("8.8.8.8", 53);

                socket.connect(socketAddress, timeoutMs);
                socket.close();

                startActivity(intent);
            } catch (IOException e) {
                Timber.d(e);
                // Display message to UI thread if network connection fails
                new Handler(Looper.getMainLooper()).post(() -> {
                    Toast.makeText(getActivity(),
                            getString(R.string.network_not_available_message),
                            Toast.LENGTH_SHORT).show();
                });
            }

        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void requestLocationPermission() {
        if(ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION:
                if(grantResults.length > 0) {
                    if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        requestLocationPermission();
                    } else {
                        getActivity().finishAndRemoveTask();
                    }
                }
                break;
        }
    }

}

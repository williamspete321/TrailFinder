package first.project.android.trailfinder.data.repository;

import android.location.Location;

import androidx.lifecycle.LiveData;

import com.example.android.trailfinder.BuildConfig;

import first.project.android.trailfinder.data.executor.AppExecutors;
import first.project.android.trailfinder.data.database.model.TrailList;
import first.project.android.trailfinder.data.database.TrailDao;
import first.project.android.trailfinder.data.database.model.Trail;
import first.project.android.trailfinder.data.network.RetrofitClientInstance;
import first.project.android.trailfinder.data.network.TrailWebService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrailRepository {

    private static final String API_KEY = BuildConfig.HIKING_PROJECT_DATA_API_KEY;
    private static final int MAX_DISTANCE_TO_TRAIL = 25;

    private static final int SECONDS_IN_MILLIS = 1000;
    private static final int MINUTES_IN_MILLIS = SECONDS_IN_MILLIS * 60;
    private static final int HOURS_IN_MILLIS = MINUTES_IN_MILLIS * 60;

    private static TrailRepository trailRepository;

    private final TrailDao trailDao;
    private final AppExecutors appExecutors;
    private final TrailWebService trailWebService;

    private TrailRepository(final TrailDao trailDao, final AppExecutors appExecutors) {
        this.trailDao = trailDao;
        this.appExecutors = appExecutors;
        this.trailWebService = RetrofitClientInstance.getInstance().create(TrailWebService.class);
    }

    public static TrailRepository getInstance(final TrailDao trailDao,
                                              final AppExecutors appExecutors) {
        if (trailRepository == null) {
            synchronized (TrailRepository.class) {
                trailRepository = new TrailRepository(trailDao, appExecutors);
            }
        }
        return trailRepository;
    }

    public LiveData<Trail> getTrailById(Integer id, Location location) {
        loadTrailsFromNetwork(location);
        return trailDao.getTrailById(id);
    }

    public LiveData<List<Trail>> getAllTrails(Location location) {
        loadTrailsFromNetwork(location);
        return trailDao.getAllTrails(getMaxRefreshTime());
    }

    private void loadTrailsFromNetwork(Location location) {
        appExecutors.diskIO().execute(() -> {

            boolean trailsExist = (trailDao.hasTrails(getMaxRefreshTime()).size() != 0);

            if (!trailsExist) {

                trailWebService.getAllTrails(
                        location.getLatitude(),
                        location.getLongitude(),
                        MAX_DISTANCE_TO_TRAIL,
                        API_KEY)
                        .enqueue(new Callback<TrailList>() {
                            @Override
                            public void onResponse(Call<TrailList> call, Response<TrailList> response) {
                                if (response.body() != null) {
                                    List<Trail> trails = response.body().getTrails();
                                    for (Trail trail : trails) {
                                        trail.setLastRefresh(System.currentTimeMillis());
                                    }
                                    appExecutors.diskIO().execute(() -> {
                                        trailDao.deleteAllTrails();
                                        trailDao.insertAll(trails);
                                    });
                                }
                            }

                            @Override
                            public void onFailure(Call<TrailList> call, Throwable t) {

                            }
                        });

            }

        });

    }

    private long getMaxRefreshTime() {
        return System.currentTimeMillis() - HOURS_IN_MILLIS;
    }

}

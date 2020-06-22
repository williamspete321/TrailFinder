package first.project.android.trailfinder.data.network;

import first.project.android.trailfinder.data.database.model.TrailList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TrailWebService {

    @GET("/data/get-trails")
    Call<TrailList> getAllTrails(
            @Query("lat") double lat,
            @Query("lon") double lon,
            @Query("maxDistance") int maxDistance,
            @Query("key") String key);
}

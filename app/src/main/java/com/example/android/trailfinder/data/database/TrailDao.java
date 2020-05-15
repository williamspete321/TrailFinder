package com.example.android.trailfinder.data.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.android.trailfinder.data.database.model.Trail;

import java.util.List;

@Dao
public interface TrailDao {

    @Query("SELECT * FROM trail_table WHERE id = :id")
    LiveData<Trail> getTrailById(int id);

    @Query("SELECT * FROM trail_table WHERE lastRefresh > :lastRefreshMax ORDER BY id ASC")
    LiveData<List<Trail>> getAllTrails(long lastRefreshMax);

    @Query("SELECT * FROM trail_table WHERE lastRefresh > :lastRefreshMax ORDER BY id ASC")
    List<Trail> hasTrails(long lastRefreshMax);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Trail> trails);

    @Query("DELETE FROM trail_table")
    void deleteAllTrails();
}

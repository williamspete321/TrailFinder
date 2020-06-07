package com.example.android.trailfinder.data.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.android.trailfinder.data.database.model.Trail;

import timber.log.Timber;

@Database(entities = {Trail.class}, version = 6)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "trails";

    private static AppDatabase appDatabase;

    public abstract TrailDao trailDao();

    public static AppDatabase getInstance(final Context context) {
        if (appDatabase == null) {
            synchronized (AppDatabase.class) {
                appDatabase = Room.databaseBuilder(
                        context.getApplicationContext(), AppDatabase.class, DATABASE_NAME)
                        .fallbackToDestructiveMigration()
                        .build();
                Timber.d("Database has been created");
            }
        }
        Timber.d("Instance of database has been called");
        return appDatabase;
    }

}

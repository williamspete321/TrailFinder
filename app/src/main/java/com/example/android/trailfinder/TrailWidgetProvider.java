package com.example.android.trailfinder;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.widget.RemoteViews;

import com.example.android.trailfinder.data.database.model.Trail;
import com.example.android.trailfinder.data.executor.AppExecutors;
import com.example.android.trailfinder.ui.traildetail.TrailDetailActivity;
import com.example.android.trailfinder.ui.traildetail.TrailDetailFragment;
import com.example.android.trailfinder.utilities.InjectorUtils;
import com.google.android.gms.tasks.Task;

import timber.log.Timber;

/**
 * Implementation of App Widget functionality.
 */
public class TrailWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.preference_file_key),context.MODE_PRIVATE);

        int trailId = sharedPreferences
                .getInt(context.getString(R.string.last_viewed_trail_id_key),0);

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                Trail trail = InjectorUtils.provideRepository(context)
                        .getLastViewedTrailById(trailId);

                if(trail != null) {
                    RemoteViews views = new RemoteViews(context.getPackageName(),
                            R.layout.trail_widget_provider);
                    views.setTextViewText(R.id.appwidget_textview_trail_name, trail.getName());
                    views.setTextViewText(R.id.appwidget_textview_trail_length,
                            String.valueOf(trail.getLength()));

                    Intent intent = new Intent(context, TrailDetailActivity.class);
                    intent.putExtra(TrailDetailFragment.ID, trailId);

                    TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
                    taskStackBuilder.addNextIntentWithParentStack(intent);

                    PendingIntent pendingIntent = taskStackBuilder
                            .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                    views.setOnClickPendingIntent(R.id.appwidget_textview_trail_name, pendingIntent);
                    appWidgetManager.updateAppWidget(appWidgetId, views);
                }
            }
        });
    }

    public static void updateTrailWidget(Context context, AppWidgetManager appWidgetManager,
                                          int[] appWidgetIds) {
        for(int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

}


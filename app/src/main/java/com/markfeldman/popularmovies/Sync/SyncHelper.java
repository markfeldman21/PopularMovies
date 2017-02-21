package com.markfeldman.popularmovies.Sync;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.markfeldman.popularmovies.activities.MainActivity;
import com.markfeldman.popularmovies.utilities.NotificationUtils;

public class SyncHelper {
    public static final String DISMISS_ACTION_NOTIFICATION = "dimiss_notification";
    public static final String EXECUTE_NOW = "execute_now";
    public static final String NAVIGATE_TO_APP_NOTIFICATION = "navigate";

    public static void executeTask(Context context, String action) {
        if (DISMISS_ACTION_NOTIFICATION.equals(action)) {
            NotificationUtils.clearAllNotificationServices(context);
        }if (EXECUTE_NOW.equals(action)){
            MovieSyncTask.syncMovieDB(context);
        }
    }
}

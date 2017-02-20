package com.markfeldman.popularmovies.Sync;

import android.content.Context;
import android.util.Log;

import com.markfeldman.popularmovies.utilities.NotificationUtils;

/**
 * Created by markfeldman on 2/20/17.
 */

public class SyncHelper {
    public static final String DISMISS_ACTION_NOTIFICATION = "dimiss_notification";
    public static final String EXECUTE_NOW = "execute_now";
    public static void executeTask(Context context, String action) {
        Log.v("TAG", "IN SYNC HELPER!!!!!!!!!!");
        if (DISMISS_ACTION_NOTIFICATION.equals(action)) {
            NotificationUtils.clearAllNotificationServices(context);
        }if (EXECUTE_NOW.equals(action)){
            MovieSyncTask.syncMovieDB(context);
        }
    }
}

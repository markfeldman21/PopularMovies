package com.markfeldman.popularmovies.Sync;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.text.format.DateUtils;
import android.util.Log;
import com.markfeldman.popularmovies.database.MovieContract;
import com.markfeldman.popularmovies.utilities.JSONParser;
import com.markfeldman.popularmovies.utilities.MovSharedPreferences;
import com.markfeldman.popularmovies.utilities.NetworkUtils;
import com.markfeldman.popularmovies.utilities.NotificationUtils;

import java.net.URL;
import java.util.Date;

public class MovieSyncTask {

    synchronized public static void syncMovieDB(Context context){
        try{
            URL movieRequestURL = NetworkUtils.getUrl(context);
            String httpResponse = NetworkUtils.getResponseFromHttpUrl(movieRequestURL);
            ContentValues[] movieValues = JSONParser.getJsonContentValue(httpResponse);
            Uri uri = MovieContract.MovieDataContract.CONTENT_URI;
            context.getContentResolver().delete(uri, null,null);
            context.getContentResolver().bulkInsert(uri,movieValues);

            long timeSinceLastNotification = MovSharedPreferences.getTimeSinceLastNotification(context);
            Log.v("TAG", "ORIGINAL ======== " + timeSinceLastNotification + "=====");
            boolean minutePassed = false;

            if(timeSinceLastNotification>= DateUtils.MINUTE_IN_MILLIS) {
                Log.v("TAG", "MIN PASSED ======== " + timeSinceLastNotification + "=====" + DateUtils.MINUTE_IN_MILLIS);
                minutePassed = true;
            }

            if (MovSharedPreferences.areNotificationsEnabled(context) && minutePassed){
                Log.v("TAG", "NOTIFIED!!!!!!!!");
                NotificationUtils.notifyUser(context);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

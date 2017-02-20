package com.markfeldman.popularmovies.Sync;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.IntentFilter;
import android.net.Uri;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.markfeldman.popularmovies.database.MovieContract;
import com.markfeldman.popularmovies.utilities.JSONParser;
import com.markfeldman.popularmovies.utilities.NetworkUtils;
import com.markfeldman.popularmovies.utilities.NotificationUtils;

import java.net.URL;

public class MovieSyncTask {

    synchronized public static void syncMovieDB(Context context){
        try{
            Log.v("TAG","IN SYNC MAIN");
            URL movieRequestURL = NetworkUtils.getUrl(context);
            String httpResponse = NetworkUtils.getResponseFromHttpUrl(movieRequestURL);
            ContentValues[] movieValues = JSONParser.getJsonContentValue(httpResponse);
            Uri uri = MovieContract.MovieDataContract.CONTENT_URI;
            context.getContentResolver().delete(uri, null,null);
            context.getContentResolver().bulkInsert(uri,movieValues);

            //ADD NOTIFICATION HERE
            NotificationUtils.notifyUser(context);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

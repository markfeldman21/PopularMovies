package com.markfeldman.popularmovies.Sync;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.markfeldman.popularmovies.database.MovieContract;
import com.markfeldman.popularmovies.utilities.JSONParser;
import com.markfeldman.popularmovies.utilities.NetworkUtils;
import java.net.URL;

public class MovieSyncTask {


    synchronized public static void syncMovieDB(Context context){
        try{
            URL movieRequestURL = NetworkUtils.getUrl(context);
            String httpResponse = NetworkUtils.getResponseFromHttpUrl(movieRequestURL);
            ContentValues[] movieValues = JSONParser.getJsonContentValue(httpResponse);
            Uri uri = MovieContract.MovieDataContract.CONTENT_URI;
            context.getContentResolver().delete(uri, null,null);
            context.getContentResolver().bulkInsert(uri,movieValues);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

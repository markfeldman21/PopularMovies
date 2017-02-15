package com.markfeldman.popularmovies.Sync;

import android.content.Context;
import android.util.Log;

import com.markfeldman.popularmovies.utilities.NetworkUtils;

import java.net.URL;

public class MovieSyncTask {

    synchronized public static void syncMovieDB(Context context){
        try{
            URL movieRequestURL = NetworkUtils.getUrl(context);
            Log.v("TAG", "IN SYNC TASK URL =========== " + movieRequestURL);


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //NETWORK UTILS - Get URL for most_popular
    //NETWORK UTILS - Get URL for top_rated
    //NETWORK UTILS - Get Json String response using URL for most_popular
    //NETWORK UTILS - Get Json String response using URL for top_rated
    //ContentValues[] cv;
    //JSON PARSER - cv = JsonParser.getJsonCVForMost_Pop
    //JSON PARSER - cv = JsonParser.getJsonCVForTopRated
}

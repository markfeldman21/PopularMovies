package com.markfeldman.popularmovies.Sync;

import android.content.Context;

import com.markfeldman.popularmovies.utilities.NetworkUtils;

import java.net.URL;

public class MovieSyncTask {

    synchronized public static void syncMovieDB(Context context){
        try{
            URL movieRequestURL = NetworkUtils.getUrl(context);


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

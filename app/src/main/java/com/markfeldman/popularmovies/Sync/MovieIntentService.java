package com.markfeldman.popularmovies.Sync;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class MovieIntentService extends IntentService {


    public MovieIntentService(){

        super("MovieIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.v("TAG", "IN INTENT SERVICE!!!!!!!!!!!!!!!!!");
        MovieSyncTask.syncMovieDB(this);
    }
}

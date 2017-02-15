package com.markfeldman.popularmovies.Sync;

import android.app.IntentService;
import android.content.Intent;

public class MovieIntentService extends IntentService {

    public MovieIntentService(){
        super("MovieIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();
        //Execute the SyncTask Class
    }
}

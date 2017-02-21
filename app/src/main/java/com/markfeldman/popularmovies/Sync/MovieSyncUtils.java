package com.markfeldman.popularmovies.Sync;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import java.util.concurrent.TimeUnit;
import android.support.annotation.NonNull;
import android.util.Log;


import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;
import com.markfeldman.popularmovies.database.MovieContract;

public class MovieSyncUtils {
    private static final int SYNC_INTERVAL_MINUTES = 0;
    private static final int SYNC_INTERVAL_SECONDS = (int)TimeUnit.MINUTES.toSeconds(SYNC_INTERVAL_MINUTES);
    private static final int SYNC_NEXT = SYNC_INTERVAL_SECONDS;
    private static boolean initialized;
    private static final String MOVIE_SYNC_TAG = "movie_sync";

    private static void scheduleFirebaseJobSync(@NonNull final Context context){
        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher firebaseJobDispatcher = new FirebaseJobDispatcher(driver);

        Job syncJob = firebaseJobDispatcher.newJobBuilder()
                .setService(FireBaseJobService.class)
                .setTag(MOVIE_SYNC_TAG)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                // start between 0 and X seconds from now
                .setTrigger(Trigger.executionWindow(0,20))
                .setReplaceCurrent(true)
                .build();
        firebaseJobDispatcher.schedule(syncJob);

        Log.v("TAG","JOB TRIGGERED!!!!!!!!!!!!!");
    }



    synchronized public static void initialize(@NonNull final Context context){
        if (initialized){
            return;
        }
        initialized = true;

        scheduleFirebaseJobSync(context);


        //What is the class of the object that we've passed to Thread() exactly?
        // It's not an instance of Runnable; Runnable is just an interface.
        // It's an instance of a class that has no name -- an anonymous class -- that implements Runnable.
        //https://www.caveofprogramming.com/javatutorial/java-tutorial-8-starting-threads-and-using-anonymous-classes.html
        Thread checkForEmpty = new Thread(new Runnable() {
            @Override
            public void run() {
                Uri uri = MovieContract.MovieDataContract.CONTENT_URI;
                String[] projection = {MovieContract.MovieDataContract._ID,MovieContract.MovieDataContract.MOVIE_TITLE,
                        MovieContract.MovieDataContract.MOVIE_RELEASE,MovieContract.MovieDataContract.MOVIE_RATING,
                        MovieContract.MovieDataContract.MOVIE_POSTER_TAG,MovieContract.MovieDataContract.MOVIE_PLOT,};

                Cursor cursor = context.getContentResolver().query(uri,projection,null,null,null);

                if (cursor == null|| cursor.getCount()==0){
                    startImmediateSync(context);
                }
                cursor.close();
            }
        });
        checkForEmpty.start();
    }

    public static void startImmediateSync(@NonNull final Context context) {
        Intent intentToSyncImmediately = new Intent(context, MovieIntentService.class);
        intentToSyncImmediately.setAction(SyncHelper.EXECUTE_NOW);
        context.startService(intentToSyncImmediately);
    }

}

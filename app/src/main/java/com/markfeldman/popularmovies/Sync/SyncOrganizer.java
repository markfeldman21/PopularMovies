package com.markfeldman.popularmovies.Sync;

import android.content.Context;

/**
 * Created by markfeldman on 2/13/17.
 */

public class SyncOrganizer {

    public static final String SYNC_MAIN_DATA = "sync_movie_data";

    public static void executeSync(Context context, String action) {
            if (SYNC_MAIN_DATA.equals(action)){
                //Execute Movie Sync Task
            }
        }
    }




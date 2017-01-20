package com.markfeldman.popularmovies.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import com.markfeldman.popularmovies.R;

public class MovSharedPreferences {
    public static String getPreferredMovieCategory(Context context) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        String keyForLocation = context.getString(R.string.movie_pref_key);
        String defaultLocation = context.getString(R.string.pref_default_unit);
        return prefs.getString(keyForLocation, defaultLocation);
    }
}

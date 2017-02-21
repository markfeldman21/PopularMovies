package com.markfeldman.popularmovies.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;
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

    public static boolean areNotificationsEnabled(Context context){
        final String displayNotificationsKey = context.getString(R.string.pref_enable_notifications_key);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean defaultNotification = context.getResources().getBoolean(R.bool.default_show_notifications);
        return sharedPreferences.getBoolean(displayNotificationsKey, defaultNotification);
    }

    public static void saveLastNotification(Context context, long time){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String key = context.getString(R.string.pref_last_notification);
        editor.putLong(key,time);
        editor.apply();
    }

    public static long getTimeSinceLastNotification(Context context){
        String key = context.getString(R.string.pref_last_notification);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        long lastNotification = sharedPreferences.getLong(key,0);
        return System.currentTimeMillis() - lastNotification;
    }

}

package com.markfeldman.popularmovies.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;

import com.markfeldman.popularmovies.R;
import com.markfeldman.popularmovies.Sync.MovieSyncUtils;


public class SettingsFragment extends PreferenceFragmentCompat implements
        SharedPreferences.OnSharedPreferenceChangeListener{
    //These preferences will automatically save to SharedPreferences as the user interacts with them
    //You retrive them using SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
    //Then it finds it by the key that you assign your preference widget:
    //String sort = prefs.getString(getString(R.string.movie_pref_key),getString(R.string.pref_default_unit));
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_general);
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.v("TAG","PREF CHANGED KEY =======!!!!!!!!!!!!!!" + key);
        if (key.equals("movie pref key")){
            Log.v("TAG","PREF CHANGED!!!!!!!!!!!!!!");
            MovieSyncUtils.startImmediateSync(getActivity());
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }
}

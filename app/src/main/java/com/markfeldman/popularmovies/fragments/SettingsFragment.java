package com.markfeldman.popularmovies.fragments;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.markfeldman.popularmovies.R;


public class SettingsFragment extends PreferenceFragmentCompat{
    //These preferences will automatically save to SharedPreferences as the user interacts with them
    //You retrive them using SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
    //Then it finds it by the key that you assign your preference widget:
    //String sort = prefs.getString(getString(R.string.movie_pref_key),getString(R.string.pref_default_unit));
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_general);
    }
}

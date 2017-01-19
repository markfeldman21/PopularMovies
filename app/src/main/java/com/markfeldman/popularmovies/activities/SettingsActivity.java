package com.markfeldman.popularmovies.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.markfeldman.popularmovies.R;
import com.markfeldman.popularmovies.fragments.MoviesFragment;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

    }
}
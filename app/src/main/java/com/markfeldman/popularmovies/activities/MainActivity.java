package com.markfeldman.popularmovies.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.markfeldman.popularmovies.fragments.MoviesFragment;
import com.markfeldman.popularmovies.R;
//LEFT TO DO
//1. IMPLEMENT DYNAMIC BROADCAST TO CHECK FOR INTERNET
//2. CREATE NOTIFICATIONS FOR USER WHEN JOB IS PERFORMED
//3. ADD NOTFICATIONS TO PREFS
//4. ADD BUTTON FOR USERS FAV MOVIE LOCALLY
//5. OPTIMIZE FOR TABLET
//6. WRITE TESTS
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         if (savedInstanceState==null){
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, new MoviesFragment())
                        .commit();
            }
        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

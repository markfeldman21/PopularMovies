package com.markfeldman.popularmovies.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.markfeldman.popularmovies.database.MovieContract;
import com.markfeldman.popularmovies.fragments.MoviesFragment;
import com.markfeldman.popularmovies.R;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

//LEFT TO DO
//1. IMPLEMENT DYNAMIC BROADCAST TO CHECK FOR INTERNET
//2. CREATE NOTIFICATIONS FOR USER WHEN JOB IS PERFORMED
//3. ADD NOTFICATIONS TO PREFS
//4. ADD BUTTON FOR USERS FAV MOVIE LOCALLY
//5. OPTIMIZE FOR TABLET
//6. WRITE TESTS
public class MainActivity extends AppCompatActivity {
    private IntentFilter onlineIntentFilter;
    private CheckOnlineReceiver checkOnlineReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        onlineIntentFilter = new IntentFilter();
        checkOnlineReceiver = new CheckOnlineReceiver();
        onlineIntentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);

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
    protected void onResume() {
        super.onResume();
        registerReceiver(checkOnlineReceiver, onlineIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(checkOnlineReceiver);
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

    private class CheckOnlineReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(final Context context, Intent intent) {
            ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
            if (!isConnected){
                Toast.makeText(context,"Could Not Retrieve Data. You Are Not Online",Toast.LENGTH_LONG).show();
            }
        }
    }
}

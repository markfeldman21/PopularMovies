package com.markfeldman.popularmovies.activities;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.markfeldman.popularmovies.fragments.DetailFragment;
import com.markfeldman.popularmovies.fragments.MoviesFragment;
import com.markfeldman.popularmovies.R;
import com.markfeldman.popularmovies.utilities.MovieRecyclerAdapter;


//4. ADD BUTTON FOR USERS FAV MOVIE LOCALLY
//5. OPTIMIZE FOR TABLET
//6. WRITE TESTS
public class MainActivity extends AppCompatActivity implements MovieRecyclerAdapter.MovieClickedListener {
    private IntentFilter onlineIntentFilter;
    private CheckOnlineReceiver checkOnlineReceiver;
    private final String BUNDLE_EXTRA = "Intent Extra";
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

    @Override
    public void onCLicked(int id) {
        FragmentTransaction ft;
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_EXTRA,id);
        DetailFragment detailFrag = new DetailFragment();
        detailFrag.setArguments(bundle);

        if (findViewById(R.id.container_detail_720dp_land) != null){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container_detail_720dp_land, detailFrag)
                    .addToBackStack(null)
                    .commit();
        } else{
            ft=getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container,detailFrag)
                    .addToBackStack(null)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
        }
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

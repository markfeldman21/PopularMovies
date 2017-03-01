package com.markfeldman.popularmovies.activities;


import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.markfeldman.popularmovies.fragments.DetailFragment;
import com.markfeldman.popularmovies.fragments.MoviesFragment;
import com.markfeldman.popularmovies.R;
import com.markfeldman.popularmovies.utilities.MovieRecyclerAdapter;

//1.Create on Back Pressed For Fragment
//2. Add Logic to check for Tablwt Layout upon Launch
//3. Change Layout for Phone - Movie Vertical, Movie Horizontal, Detail Vertical, Detail Horizonal
//   sw600dp - MoviePoster, Movie Vertical, Movie Horizontal, Detail Vertical, Detail Horizonal
//   sw720dp - MoviePoster, Movie Vertical, Movie Horizontal, Detail Vertical, Detail Horizonal
//4. ADD BUTTON FOR USERS FAV MOVIE LOCALLY
//5. OPTIMIZE FOR TABLET
//6. WRITE TESTS
public class MainActivity extends AppCompatActivity implements MovieRecyclerAdapter.MovieClickedListener {
    private final String BUNDLE_EXTRA = "Intent Extra";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState==null){
            //CHECK FOR LANDSCAPR MOVIE XML HERE AS WELL
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
}

package com.markfeldman.popularmovies.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.markfeldman.popularmovies.R;
import com.markfeldman.popularmovies.activities.DetailActivity;
import com.markfeldman.popularmovies.data_helpers.JSONParser;
import com.markfeldman.popularmovies.data_helpers.MovieRecyclerAdapter;
import com.markfeldman.popularmovies.data_helpers.NetworkUtils;
import com.markfeldman.popularmovies.objects.MovieObj;
import org.json.JSONException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class MoviesFragment extends Fragment implements MovieRecyclerAdapter.MovieClickedListener {
    private RecyclerView recyclerView;
    private MovieRecyclerAdapter movieRecyclerAdapter;
    private MovieObj[] movieObjs = null;
    private final String INTENT_EXTRA = "Intent Extra";
    private ProgressDialog progressDialog;

    public MoviesFragment() {
    }

    @Override
    public void onStart() {
        loadMovies();
        super.onStart();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies, container, false);

        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2);

        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        movieRecyclerAdapter = new MovieRecyclerAdapter(this);
        recyclerView.setAdapter(movieRecyclerAdapter);
        return view;
    }

    public void loadMovies(){
        new RetrieveMovieInfo().execute();
    }

    @Override
    public void onCLicked(MovieObj movieChosen) {
        Intent i = new Intent(getActivity(), DetailActivity.class);
        i.putExtra(INTENT_EXTRA, movieChosen);
        startActivity(i);
    }

    public class RetrieveMovieInfo extends AsyncTask<Void,Void,MovieObj[]>{

        HttpURLConnection urlConnection;
        BufferedReader reader;
        String moviesJsonStr;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.show();
        }

        @Override
        protected MovieObj[] doInBackground(Void...params) {
            MovieObj [] movieObjs = null;
            try {
                URL urlResponse = NetworkUtils.buildUrlPopular();
                if (sortBy().equals("Top Rated")){
                    urlResponse = NetworkUtils.buildUrlTopRated();
                }
                String jsonResponse = NetworkUtils.getResponseFromHttpUrl(urlResponse);
                JSONParser jsonParser = new JSONParser();
                movieObjs = jsonParser.getMovieObjectsL(jsonResponse);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return movieObjs;
        }

        @Override
        protected void onPostExecute(MovieObj[] moviObjects) {
            super.onPostExecute(moviObjects);
            progressDialog.dismiss();
            movieObjs = moviObjects;
            movieRecyclerAdapter.setMovieData(moviObjects);

        }
    }

    public String sortBy(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sort = prefs.getString(getString(R.string.movie_pref_title),getString(R.string.pref_default_unit));

        return sort;
    }
}

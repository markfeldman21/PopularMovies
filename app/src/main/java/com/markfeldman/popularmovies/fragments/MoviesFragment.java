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
import com.markfeldman.popularmovies.objects.MovieObj;
import org.json.JSONException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
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
                final String BASE_URL = "http://api.themoviedb.org/";
                String SEARCH_BY = "3/movie/popular";
                final String API_KEY_SEARCH = "?api_key=";
                final String API_KEY ="657b6c53f883538fe1f57b0e84031c09";
                final String LANGUAGE_PARAM = "&language=";
                final String LANGUAGE = "en-US";
                final String PAGE_PARAM = "&page=";
                final String PAGE_NUM = "1";

                if (sortBy().equals("Top Rated")){
                     SEARCH_BY = "3/movie/top_rated";
                }

                Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendEncodedPath(SEARCH_BY + API_KEY_SEARCH + API_KEY + LANGUAGE_PARAM + LANGUAGE + PAGE_PARAM + PAGE_NUM)
                        .build();
                URL url = new URL(builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = null;

                inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    Log.v("1", "nothing retrieved");
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;

                try {
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line + "\n");//Helpful for debugging
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (buffer.length() == 0) {
                    Log.v("1", "nothing in bufferedString");
                }else{
                    moviesJsonStr = buffer.toString();
                    Log.v("MOVIES", "JSON + " + moviesJsonStr);
                    JSONParser jsonParser = new JSONParser();


                    movieObjs = jsonParser.getMovieObjectsL(moviesJsonStr);

                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
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

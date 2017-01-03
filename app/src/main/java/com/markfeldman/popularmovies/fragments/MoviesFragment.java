package com.markfeldman.popularmovies.fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.markfeldman.popularmovies.activities.DetailActivity;
import com.markfeldman.popularmovies.data_helpers.CustomGridAdapter;
import com.markfeldman.popularmovies.R;
import com.markfeldman.popularmovies.data_helpers.JSONParser;
import org.json.JSONException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class MoviesFragment extends Fragment {
    private GridView gridView;
    private String[] movieImages = null;
    private final String INTENT_EXTRA = "Intent Extra";

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


        gridView = (GridView)view.findViewById(R.id.gridView);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String imageUrl = movieImages[position];
                Intent i = new Intent(getActivity(), DetailActivity.class);
                i.putExtra(INTENT_EXTRA,imageUrl);
                startActivity(i);
            }
        });

        return view;
    }

    public void loadMovies(){
        new RetrieveMovieInfo().execute();
    }

    public class RetrieveMovieInfo extends AsyncTask<Void,Void,String[]>{

        HttpURLConnection urlConnection;
        BufferedReader reader;
        String moviesJsonStr;

        RetrieveMovieInfo(){
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String moviesJsonStr = null;
        }

        @Override
        protected String[] doInBackground(Void...params) {
            String movieImageURL[] = null;

            try {
                final String BASE_URL = "http://api.themoviedb.org/";
                String SEARCH_BY = "3/movie/popular";
                final String API_KEY_SEARCH = "?api_key=";
                final String API_KEY ="OWN KEY";
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
                    movieImageURL = jsonParser.getMovieImagesURL(moviesJsonStr);
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
            return movieImageURL;
        }

        @Override
        protected void onPostExecute(String[] movieImageURL) {
            super.onPostExecute(movieImageURL);
            movieImages = movieImageURL;

            DisplayMetrics metrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int displayWidth = metrics.widthPixels ;
            int displayHeight = metrics.heightPixels;

            gridView.setAdapter(new CustomGridAdapter(getActivity(),movieImageURL, displayWidth,displayHeight));


        }
    }

    public String sortBy(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sort = prefs.getString(getString(R.string.movie_pref_title),getString(R.string.pref_default_unit));

        return sort;
    }
}

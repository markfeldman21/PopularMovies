package com.markfeldman.popularmovies.data_helpers;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.GridView;

import com.markfeldman.popularmovies.objects.MovieObj;
import com.squareup.picasso.Picasso;

public class CustomGridAdapter extends BaseAdapter {
    private Context mContext;
    private final String MOVIE_DB_URL_START = "http://image.tmdb.org/t/p/w185/";
    private int width, height;

    private MovieObj[] movies;

    public CustomGridAdapter(Context context, MovieObj[] items, int width, int height) {
        this.mContext = context;
        this.movies = items;
        this.width = width/2;
        this.height = height/2;
    }

    @Override
    public int getCount() {
        return movies.length;
    }

    @Override
    public Object getItem(int position) {
        return movies[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        /*
        Holder holder = null;

        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.mov_poster, parent, false);

            holder = new Holder();
            holder.poster = (ImageView) convertView.findViewById(R.id.individual_movie);
            convertView.setLayoutParams(new GridView.LayoutParams(width, height));

            convertView.setTag(holder);
        }  else{
            holder = (Holder)convertView.getTag();
        }

        Picasso.with(mContext).load(MOVIE_DB_URL_START + mThumbIds[position]).fit().into(holder.poster);

        return convertView; */

        ImageView imageView;
        if (convertView == null){
            imageView = new ImageView(mContext);
            imageView.setPadding(50,100,50,50);
            imageView.setLayoutParams(new GridView.LayoutParams(width, height));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        }else {
            imageView = (ImageView) convertView;
        }
        Picasso.with(mContext).load(MOVIE_DB_URL_START + movies[position].getMoviePosterTag()).fit().into(imageView);

        return imageView;
    }

    /*static class Holder{
        ImageView poster;
    }*/

}


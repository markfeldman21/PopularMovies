package com.markfeldman.popularmovies.objects;

import android.os.Parcel;
import android.os.Parcelable;

public class MovieObj implements Parcelable {
    private String title;
    private String plot;
    private Double rating;
    private String release;
    private String moviePosterTag;
    //
    public MovieObj(){
        title = "";
        plot = "";
        rating = 0.0;
        release = "";
        moviePosterTag = "";
    }

    public MovieObj(Parcel in){
        title = "";
        plot = "";
        rating = 0.0;
        release = "";
        moviePosterTag = "";
        readFromParcel(in);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {

        this.title = title;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getRelease() {
        return release;
    }

    public void setRelease(String release) {
        this.release = release;
    }

    public String getMoviePosterTag() {
        return moviePosterTag;
    }

    public void setMoviePosterTag(String moviePosterTag) {
        this.moviePosterTag = moviePosterTag;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(plot);
        dest.writeDouble(rating);
        dest.writeString(release);
        dest.writeString(moviePosterTag);

    }

    private void readFromParcel(Parcel in) {

        // We just need to read back each
        // field in the order that it was
        // written to the parcel
        title = in.readString();
        plot = in.readString();
        rating = in.readDouble();
        release = in.readString();
        moviePosterTag = in.readString();
    }

    public static final Parcelable.Creator CREATOR =
            new Parcelable.Creator() {
                public MovieObj createFromParcel(Parcel in) {
                    return new MovieObj(in);
                }

                public MovieObj[] newArray(int size) {
                    return new MovieObj[size];
                }
            };
}

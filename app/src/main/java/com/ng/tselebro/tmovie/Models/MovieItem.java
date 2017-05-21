package com.ng.tselebro.tmovie.Models;

import android.os.Parcel;
import android.os.Parcelable;




public class MovieItem implements Parcelable {

    private long movieId;
    private String posterPath;
    private String overview;
    private String releaseDate;
    private String backdropPath;
    private String title;
    private double userRating;
    private boolean isFav;



    public MovieItem(long movieId, String posterPath, String overview, String releaseDate, String backdropPath, String title, double userRating) {
        this.movieId = movieId;
        this.posterPath = posterPath;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.backdropPath = backdropPath;
        this.title = title;
        this.userRating = userRating;
    }

    private MovieItem(Parcel in) {
        movieId = in.readLong();
        posterPath = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
        backdropPath = in.readString();
        title = in.readString();
        userRating = in.readDouble();
        this.isFav = in.readByte() != 0;

    }

    public static final Creator<MovieItem> CREATOR = new Creator<MovieItem>() {
        @Override
        public MovieItem createFromParcel(Parcel in) {
            return new MovieItem(in);
        }

        @Override
        public MovieItem[] newArray(int size) {
            return new MovieItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(this.movieId);
        parcel.writeString(this.posterPath);
        parcel.writeString(this.overview);
        parcel.writeString(this.releaseDate);
        parcel.writeString(this.backdropPath);
        parcel.writeString(this.title);
        parcel.writeDouble(this.userRating);
        parcel.writeByte(this.isFav ? (byte) 1 : (byte) 0);


    }


    public String getPosterPath() {
        return posterPath;
    }


    public String getOverview() {
        return overview;
    }


    public String getReleaseDate() {
        return releaseDate;
    }


    public String getBackdropPath() {
        return backdropPath;
    }


    public String getTitle() {
        return title;
    }


    public double getUserRating() {
        return userRating;
    }

    public long getMovieId() {
        return movieId;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public void setUserRating(double userRating) {
        this.userRating = userRating;
    }

    public void setFav(boolean fav) {
        isFav = fav;
    }
    public boolean isFav() {
        return isFav;
    }
}




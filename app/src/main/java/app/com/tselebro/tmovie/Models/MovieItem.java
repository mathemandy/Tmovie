package app.com.tselebro.tmovie.Models;

import android.os.Parcel;
import android.os.Parcelable;




public class MovieItem implements Parcelable {


    private int movieId;
    private String posterPath;
    private String overview;
    private String releaseDate;
    private String backdropPath;
    private String title;
    private int userRating;


    public MovieItem(int id, String posterPath,
                     String overview, String releaseDate, String backdropPath,
                     String title, int userRating) {
        this.movieId = id;
        this.posterPath = posterPath;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.backdropPath = backdropPath;
        this.title = title;
        this.userRating = userRating;
    }

    protected MovieItem(Parcel in) {
        movieId = in.readInt();
        posterPath = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
        backdropPath = in.readString();
        title = in.readString();
        userRating = in.readInt();

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
        parcel.writeInt(movieId);
        parcel.writeString(posterPath);
        parcel.writeString(overview);
        parcel.writeString(releaseDate);
        parcel.writeString(backdropPath);
        parcel.writeString(title);
        parcel.writeInt(userRating);

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


    public int getUserRating() {
        return userRating;
    }

    public int getMovieId() {
        return movieId;
    }

}




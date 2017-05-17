package app.com.tselebro.tmovie.utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import app.com.tselebro.tmovie.Models.MovieItem;
import app.com.tselebro.tmovie.Models.ReviewItem;
import app.com.tselebro.tmovie.Models.TrailerItem;


public final class MovieJsonUtils {


    /**
     * code based on Udacity course on Sunshine App
     *
     * This method parses JSON from a web response and returns an array of Strings
     * displaying the details for various movies.
     * <p/>
     *
     * @param moviePosterJson JSON response from server
     *
     * @return List of Strings describing weather data
     *
     * @throws JSONException If JSON data cannot be properly parsed
     */

    public  static List<MovieItem> getSimpleMovieStringsFromJson (String moviePosterJson) throws JSONException{
         List<MovieItem> loadedMovies =new ArrayList<>();

        final String MOVIE_LIST = "results";

        final String POSTER_PATH = "poster_path";

        final String PLOT_SYNOPSIS = "overview";

        final String RELEASE_DATE = "release_date";

        final String ORIGINAL_TITLE = "original_title";

        final String BACKDROP_PATH = "backdrop_path";

        final String USER_RATING = "vote_average";

        final String MOVIE_ID = "id";



        JSONObject moviePostJson = new JSONObject(moviePosterJson);

        JSONArray moviePoster = moviePostJson.getJSONArray(MOVIE_LIST);

        for (int i = 0; i<moviePoster.length(); i++){
            JSONObject singlePoster = moviePoster.getJSONObject(i);
//            get the id of the movie
            int movieId = singlePoster.getInt(MOVIE_ID);
//       Get The Title of the movie
            String title = singlePoster.getString(ORIGINAL_TITLE);
//       Get The Poster Path
            String posterPath = singlePoster.getString(POSTER_PATH);
//       Get  The Overview
            String overview = singlePoster.getString(PLOT_SYNOPSIS);
//       Get the ERRelease Date
            String releaseDate = singlePoster.getString(RELEASE_DATE);
//       Get the backDrop path
            String backDropPath = singlePoster.getString(BACKDROP_PATH);
//           Get the user Rating
            int user_rating = singlePoster.getInt(USER_RATING);
            MovieItem movies = new MovieItem(movieId, posterPath, overview, releaseDate, backDropPath, title, user_rating);
            loadedMovies.add(movies);
        }
        return  loadedMovies;

        }

    public static  List<TrailerItem> getSimpleTrailerStringsFromJson (String trailerJson) throws JSONException{
        List<TrailerItem> loadedTrailers = new ArrayList<>();

        final String TRAILER_LIST = "results";
        final String TRAILE_ID = "id";
        final String TRAILER_KEY = "key";
        final String TRAILER_NAME = "name";
        final String TRAILER_TYPE = "type";

        JSONObject trailerObject = new JSONObject(trailerJson);
        JSONArray trailerArray = trailerObject.getJSONArray(TRAILER_LIST);

        for (int i = 0; i < trailerArray.length(); i ++ ){
            JSONObject trailers  =  trailerArray.getJSONObject(i);

//           Get the id of the trailers
            String trailerId = trailers.getString(TRAILE_ID);
//            get the youtube key for the trailer
            String key  = trailers.getString(TRAILER_KEY);
//            get the name for the trailer
            String name = trailers.getString(TRAILER_NAME);
//            get the type of trailers received
            String type = trailers.getString(TRAILER_TYPE);

            TrailerItem trailer = new TrailerItem(trailerId, key, name, type);
            loadedTrailers.add(trailer);

        }

        return  loadedTrailers;
    }




    public static  List<ReviewItem> getSimpleReviewStringsFromJson (String reviewJson) throws JSONException{
        List<ReviewItem> loadedReviews = new ArrayList<>();

        final String TRAILER_LIST = "results";
        final String REVIEW_ID = "id";
        final String REVIEW_AUTHOR = "author";
        final String REVIEW_CONTENT= "content";
        final String REVIEW_URL = "url";

        JSONObject reviewObject = new JSONObject(reviewJson);
        JSONArray reviewArray = reviewObject.getJSONArray(TRAILER_LIST);

        for (int i = 0; i < reviewArray.length(); i ++ ){
            JSONObject reviews  =  reviewArray.getJSONObject(i);

//           Get the id of the review
            String reviewId = reviews.getString(REVIEW_ID);
//            get the author name for the review
            String author  = reviews.getString(REVIEW_AUTHOR);
//            get the content of the review
            String content = reviews.getString(REVIEW_CONTENT);
//            get the url siting the addresso f the trailer
            String url = reviews.getString(REVIEW_URL);

            ReviewItem review = new ReviewItem(reviewId, author, content,url);
            loadedReviews.add(review);

        }

        return  loadedReviews;
    }



}

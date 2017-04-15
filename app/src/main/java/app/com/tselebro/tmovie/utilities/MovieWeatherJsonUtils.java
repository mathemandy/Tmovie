package app.com.tselebro.tmovie.utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import app.com.tselebro.tmovie.Models.MovieItem;



public final class MovieWeatherJsonUtils {


    /**
     * code based on Udacity course on Sunshine App
     *
     * This method parses JSON from a web response and returns an array of Strings
     * displaying the details for various movies.
     * <p/>
     *
     * @param moviePosterJson JSON response from server
     *
     * @return Array of Strings describing weather data
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


        JSONObject moviePostJson = new JSONObject(moviePosterJson);

        JSONArray moviePoster = moviePostJson.getJSONArray(MOVIE_LIST);

        for (int i = 0; i<moviePoster.length(); i++){
            JSONObject singlePoster = moviePoster.getJSONObject(i);

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

            MovieItem movies = new MovieItem(posterPath, overview, releaseDate, backDropPath, title, user_rating);
            loadedMovies.add(movies);
        }
        return  loadedMovies;

        }
}

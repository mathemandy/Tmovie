package app.com.tselebro.tmovie.utilities;


import android.net.Uri;
import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import static app.com.tselebro.tmovie.BuildConfig.MY_MOVIEDB_API_KEY;

/**
 * Code Adapted from Project work on Udacity Android FastTrack Course
 *
 * These utilities will be used to communicate with the MovieDB servers.
 */
public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String FORECAST_BASE_URL = Constants.BASE_URL;

    private final static String APP_ID_PARAM = "api_key";

    /**
     * Builds the URL used to talk to the MovieDb Server server using sortOrder as our query.
     * @param sortOrder The sortOrder that will be queried for to display our movies.
     * @return The URL to use to query the MovieDb server.
     */
    public static URL buildUrl(String sortOrder) {
        Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                .appendEncodedPath(sortOrder)
                .appendQueryParameter(APP_ID_PARAM, MY_MOVIEDB_API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }


    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
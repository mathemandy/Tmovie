package com.ng.tselebro.tmovie;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.ng.tselebro.tmovie.Adapter.MovieAdapter;
import com.ng.tselebro.tmovie.Models.MovieItem;
import com.ng.tselebro.tmovie.data.MovieContract;
import com.ng.tselebro.tmovie.utilities.Constants;
import com.ng.tselebro.tmovie.utilities.MovieJsonUtils;
import com.ng.tselebro.tmovie.utilities.MovieUtil;
import com.ng.tselebro.tmovie.utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import app.ng.tselebro.tmovie.R;
import app.ng.tselebro.tmovie.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler, LoaderManager.LoaderCallbacks<List<MovieItem>> {
    /*
        * Code Adapted from Project work on Udacity's Android FastTrack Course
        * */
    private  static  final  String TAG = MainActivity.class.getSimpleName();
    private static final int MOVIE_LOADER_ID = 1;
    private static final String SEARCH_QUERY_EXTRA = "query";
    private static final String LIST_STATE_KEY = "list_state";
    private static final String SORT_QUERY = "sort";
    private SharedPreferences prefs;

    private String queryType;
    private Parcelable mListState = null;
    private MovieAdapter mMovieAdapter;
    private ActivityMainBinding mBinding;

    /*
    * Code Adapted from Project work on Udacity's Android FastTrack Course
    * */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mMovieAdapter = new MovieAdapter(this, this);
        queryType = Constants.POPULAR_MOVIE_URL;
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(SORT_QUERY)) {
                queryType = savedInstanceState.getString(SORT_QUERY);
            }
        }


        if (getApplication().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mBinding.recyclerViewMovies.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            mBinding.recyclerViewMovies.setLayoutManager(new GridLayoutManager(this, 4));
        }
        mBinding.recyclerViewMovies.setItemAnimator(new DefaultItemAnimator());

        mBinding.recyclerViewMovies.setHasFixedSize(true);

        mBinding.recyclerViewMovies.setAdapter(mMovieAdapter);


        mBinding.errorInfo.btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchMovies(queryType);
            }
        });
        fetchMovies (queryType);

        LoaderManager.LoaderCallbacks<List<MovieItem>> callback = MainActivity.this;
        getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, null, callback);
        prefs = getSharedPreferences("com.ng.tselebro.tmovie", MODE_PRIVATE);

    }

    private void fetchMovies(String bqueryType) {
        if (MovieUtil.isNetworkAvailable(this)){
            loadMovies(bqueryType);
        }
        else {
            showErrorMessage();
        }
    }

    private void loadMovies(String query) {
        setSubTitle(query);

        if (query.equals(Constants.FAVOURITED))
            new FetchFavorites().execute();
       else {
            URL movieUrl = NetworkUtils.buildMovieUrl(query);
            Log.i(TAG, "Formed URL: " + movieUrl);
            Bundle queryBundle = new Bundle();

            queryBundle.putString(SEARCH_QUERY_EXTRA, movieUrl.toString() );
            LoaderManager loaderManager = getSupportLoaderManager();

            Loader<List<MovieItem>>  movieSearchLoader = loaderManager.getLoader(MOVIE_LOADER_ID);
            if (movieSearchLoader == null) {
                loaderManager.initLoader(MOVIE_LOADER_ID, queryBundle, this);
                Log.i(TAG, "Loader Initialised");
            } else  {
                loaderManager.restartLoader(MOVIE_LOADER_ID, queryBundle, this);
                Log.i(TAG, "Loader Restarted");
            }

        }
    }


    private void setSubTitle(String mqueryType) {
        if (getSupportActionBar() != null && mqueryType != null){
            switch (mqueryType){
                case Constants.POPULAR_MOVIE_URL:
                    default:
                        getSupportActionBar().setSubtitle("Most Popular Movie");
                        break;
                case Constants.TOP_RATED_URL:
                    getSupportActionBar().setSubtitle("Top Rated Movie");
                    break;
                case  Constants.FAVOURITED:
                    getSupportActionBar().setSubtitle("My Favourite Collection");
            }

        }
    }

    @Override
    public void onClick(MovieItem movieItem) {
        Context context = MainActivity.this;
        Class destinationClass = MovieDetails.class;
        Intent intentToStartActivity = new Intent(context, destinationClass);
      intentToStartActivity.putExtra("movies", movieItem);
        startActivity(intentToStartActivity);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        MenuItem item = menu.findItem(R.id.spinner);
        Spinner mSpinner = (Spinner) MenuItemCompat.getActionView(item);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spinner_list_item_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                switch (i) {
                    case 0:
                        if (!queryType.equals(Constants.POPULAR_MOVIE_URL)){
                            queryType = Constants.POPULAR_MOVIE_URL;
                            fetchMovies(queryType);
                        }
                            break;
                    case 1:
                        if (!queryType.equals(Constants.TOP_RATED_URL)){
                        queryType = Constants.TOP_RATED_URL;
                            invalidateData();
                        fetchMovies(queryType);}
                        break;
                    case 2:
                        if (!queryType.equals(Constants.FAVOURITED)){
                            queryType = Constants.FAVOURITED;
                            fetchMovies(queryType);
                        }
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        return true;

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (prefs.getBoolean(getString(R.string.sharedPreference_FirstRun), true)){
            prefs.edit().putBoolean(getString(R.string.sharedPreferences_firstRun), false).apply();
        }

        if (queryType == Constants.FAVOURITED){
            fetchMovies(queryType);
        }
    }

    @Override
    protected void onPause() {
        mListState = mBinding.recyclerViewMovies.getLayoutManager().onSaveInstanceState();
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("sort", queryType);
        mListState = mBinding.recyclerViewMovies.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(LIST_STATE_KEY, mListState);
        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        if (savedInstanceState != null) {
            queryType = savedInstanceState.getString(SORT_QUERY);
            mListState = savedInstanceState.getParcelable(LIST_STATE_KEY);
            super.onRestoreInstanceState(savedInstanceState);
        }


    }

    public class FetchFavorites extends AsyncTask<Void, Void, Cursor>{
        String[] projection = {
                MovieContract.MovieEntry.COLUMN_MOVIE_ID,
                MovieContract.MovieEntry.COLUMN_MOVIE_TITLE,
                MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH,
                MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW,
                MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE,
                MovieContract.MovieEntry.COLUMN_MOVIE_BACKDROP_PATH,
                MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE
        };

        @Override
        protected Cursor doInBackground(Void... voids) {
            try {
                return getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                        projection,
                        null,
                        null,
                        null);
            } catch (Exception e) {
                Log.e(TAG, "Failed to asynchronously load data.");
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(Cursor data) {

            List<MovieItem> moviedsFromCursor = new ArrayList<>();
            if (data != null) {
                if (data.moveToFirst()) {
                    do {

                        long movie_id = Long.parseLong(data.getString(data.getColumnIndex(MovieContract
                                .MovieEntry.COLUMN_MOVIE_ID)));
                        String title = data.getString(data.getColumnIndex(MovieContract.MovieEntry
                                .COLUMN_MOVIE_TITLE));
                        String posterPath = data.getString(data.getColumnIndex(MovieContract.MovieEntry
                                .COLUMN_MOVIE_POSTER_PATH));
                        String backdropPath = data.getString(data.getColumnIndex(MovieContract
                                .MovieEntry.COLUMN_MOVIE_BACKDROP_PATH));
                        String overview = data.getString(data.getColumnIndex(MovieContract.MovieEntry
                                .COLUMN_MOVIE_OVERVIEW));
                        String releaseDate = data.getString(data.getColumnIndex(MovieContract
                                .MovieEntry.COLUMN_MOVIE_RELEASE_DATE));
                        double userRating = Double.parseDouble(data.getString(data.
                                getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE)));
                        MovieItem movies = new MovieItem(movie_id, posterPath, overview, releaseDate, backdropPath, title, userRating);
                        moviedsFromCursor.add(movies);
                    } while (data.moveToNext());


                    mMovieAdapter.setMovieData(moviedsFromCursor);
                    if (mListState != null) {
                        mBinding.recyclerViewMovies.getLayoutManager().onRestoreInstanceState(mListState);
                    }
                    showMovieDataView();
                    mBinding.pbLoadingIndicator.setVisibility(View.INVISIBLE);
                } else {
                    Toast.makeText(MainActivity.this, "No Movies added yet, Please add movies",
                            Toast.LENGTH_LONG).show();
                }
            }
        }

        @Override
        protected void onPreExecute() {
           mBinding.pbLoadingIndicator.setVisibility(View.VISIBLE);
            mBinding.recyclerViewMovies.setVisibility(View.INVISIBLE);
        }
    }


    private void showMovieDataView() {
        mBinding.errorInfo.errorTest.setVisibility(View.INVISIBLE);
        mBinding.recyclerViewMovies.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mBinding.recyclerViewMovies.setVisibility(View.INVISIBLE);
        mBinding.errorInfo.errorTest.setVisibility(View.VISIBLE);
        mBinding.pbLoadingIndicator.setVisibility(View.INVISIBLE);
        }

    @Override
    public Loader<List<MovieItem>> onCreateLoader(final int id, final Bundle args) {



        return new AsyncTaskLoader<List<MovieItem>>(this) {
            List<MovieItem> mMovie = new ArrayList<>();

            @Override
            public List<MovieItem> loadInBackground() {


                        String urlQuery = null;
                if (args != null) {
                    urlQuery = args.getString(SEARCH_QUERY_EXTRA);
                    if (urlQuery == null || TextUtils.isEmpty(urlQuery)){
                        Log.i(TAG, "Loader : Request url is null or is empty");
                        return  null;
                    }
                }

                        try {
                            URL movieUrl= new URL(urlQuery);
                            String jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(movieUrl);
                            return MovieJsonUtils.getSimpleMovieStringsFromJson(jsonMovieResponse);

                        } catch (Exception e) {
                            e.printStackTrace();
                            return null;

                        }




                }




            @Override
            protected void onStartLoading() {
                if (mMovie != null && !mMovie.isEmpty() ) {
                    deliverResult(mMovie);
                } else {
                    mBinding.pbLoadingIndicator.setVisibility(View.VISIBLE);
                    mBinding.recyclerViewMovies.setVisibility(View.INVISIBLE);
                    forceLoad();
                }
            }

            public void deliverResult(List<MovieItem> data) {
                mMovie = data;
                super.deliverResult(data);
            }
        };
    }


    @Override
    public void onLoadFinished(Loader<List<MovieItem>> loader, List<MovieItem> data) {

        if (data != null) {
            mBinding.pbLoadingIndicator.setVisibility(View.INVISIBLE);
            mMovieAdapter.setMovieData(data);
            showMovieDataView();

            if (mListState != null) {
                mBinding.recyclerViewMovies.getLayoutManager().onRestoreInstanceState(mListState);
            }

        } else {
            showErrorMessage();
        }
    }


    @Override
    public void onLoaderReset(Loader<List<MovieItem>> loader) {
            invalidateData();
    }

    private void invalidateData() {
        mMovieAdapter.setMovieData(null);
    }

    public void storeSelection(String sortOrder){

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("SORT", sortOrder);
        editor.apply();
    }


}


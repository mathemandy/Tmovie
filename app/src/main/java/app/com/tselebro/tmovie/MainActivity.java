package app.com.tselebro.tmovie;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import app.com.tselebro.tmovie.Adapter.MovieAdapter;
import app.com.tselebro.tmovie.Models.MovieItem;
import app.com.tselebro.tmovie.databinding.ActivityMainBinding;
import app.com.tselebro.tmovie.utilities.Constants;
import app.com.tselebro.tmovie.utilities.MovieJsonUtils;
import app.com.tselebro.tmovie.utilities.NetworkUtils;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler, LoaderManager.LoaderCallbacks<List<MovieItem>> {

    private static final String BUNDLE_RECYCLER_LAYOUT = "recycler";
    private static final int MOVIE_LOADER_ID = 1;
    SharedPreferences prefs;
    private List<MovieItem> mMovie;

    /*
    * Code Adapted from Project work on Udacity's Android FastTrack Course
    * */

    private MovieAdapter mMovieAdapter;
    ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mMovieAdapter = new MovieAdapter(this, this);

        if (getApplication().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mBinding.recyclerViewMovies.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            mBinding.recyclerViewMovies.setLayoutManager(new GridLayoutManager(this, 4));
        }

        mBinding.recyclerViewMovies.setHasFixedSize(true);

        mBinding.recyclerViewMovies.setAdapter(mMovieAdapter);

        mBinding.errorInfo.btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, null,MainActivity.this);
            }
        });
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        LoaderManager.LoaderCallbacks<List<MovieItem>> callback = MainActivity.this;
        getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, null, callback);

    }

    @Override
    public void onClick(MovieItem movieItem) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("movie", movieItem);
        Context context = this;
        Class destinationClass = MovieDetails.class;
        Intent intentToStartActivity = new Intent(context, destinationClass);
        intentToStartActivity.putExtras(bundle);
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
                String queryType;

                switch (i) {
                    case 0:
                        queryType = Constants.POPULAR_MOVIE_URL;
                        storeSelection(queryType);
                        getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, null,MainActivity.this);
                        break;
                    case 1:
                        queryType = Constants.TOP_RATED_URL;
                        storeSelection(queryType);
                        getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, null,MainActivity.this);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        return true;

    }


    private void showMovieDataView() {
        mBinding.errorInfo.errorTest.setVisibility(View.INVISIBLE);
        mBinding.recyclerViewMovies.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mBinding.recyclerViewMovies.setVisibility(View.INVISIBLE);
        mBinding.errorInfo.errorTest.setVisibility(View.VISIBLE);
    }

    @Override
    public Loader<List<MovieItem>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<List<MovieItem>>(this) {

            @Override
            public List<MovieItem> loadInBackground() {
                String mSortOrder = prefs.getString("SORT", "");
                URL movieUrl = NetworkUtils.buildMovieUrl(mSortOrder);

                try {
                    String jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(movieUrl);
                    return MovieJsonUtils.getSimpleMovieStringsFromJson(jsonMovieResponse);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }



            @Override
            protected void onStartLoading() {
                if (mMovie != null) {
                    deliverResult(mMovie);
                } else {
                    mBinding.pbLoadingIndicator.setVisibility(View.VISIBLE);
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
        mBinding.pbLoadingIndicator.setVisibility(View.INVISIBLE);
        mMovieAdapter.setMovieData(data);
        if (data != null) {
            showMovieDataView();

        } else {
            showErrorMessage();
        }
    }


    @Override
    public void onLoaderReset(Loader<List<MovieItem>> loader) {

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


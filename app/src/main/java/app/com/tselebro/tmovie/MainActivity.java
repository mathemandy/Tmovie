package app.com.tselebro.tmovie;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;

import java.net.URL;
import java.util.List;

import app.com.tselebro.tmovie.Adapter.MovieAdapter;
import app.com.tselebro.tmovie.Models.MovieItem;
import app.com.tselebro.tmovie.utilities.Constants;
import app.com.tselebro.tmovie.utilities.MovieWeatherJsonUtils;
import app.com.tselebro.tmovie.utilities.NetworkUtils;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    /*
    * Code Adapted from Project work on Udacity's Android FastTrack Course
    * */

    private String mSortOrder;
    private LinearLayout mContainer;
    private RecyclerView mRecyclerView;
    private ProgressBar mLoadingIndicator;
    private MovieAdapter mMovieAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContainer = (LinearLayout) findViewById(R.id.recycler_container);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView_movies);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        Button retry = (Button) findViewById(R.id.btn_retry);


        if(getApplication().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        }
        else{
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        }


        mRecyclerView.setHasFixedSize(true);
        mMovieAdapter = new MovieAdapter(this, this);
        mRecyclerView.setAdapter(mMovieAdapter);

        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadMovieData(mSortOrder);
            }
        });


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
                        loadMovieData(queryType);
                        break;
                    case 1:
                        queryType = Constants.TOP_RATED_URL;
                        loadMovieData(queryType);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        return true;

    }

    private void loadMovieData(String sortOrder) {
        mSortOrder = sortOrder;
        showMovieDataView();
        new FetchMoviePosterTask().execute(mSortOrder);

    }

    private void showMovieDataView() {
        mContainer.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mContainer.setVisibility(View.VISIBLE);
    }

    private class FetchMoviePosterTask extends AsyncTask<String, Void, List<MovieItem>> {


        @Override
        protected List<MovieItem> doInBackground(String... strings) {

            if (strings.length == 0) {
                return null;
            }

            String sortOrder = strings[0];
            URL movieRequestUrl = NetworkUtils.buildUrl(sortOrder);

            try {
                String jsonMovieResponse = NetworkUtils
                        .getResponseFromHttpUrl(movieRequestUrl);

                return MovieWeatherJsonUtils
                        .getSimpleMovieStringsFromJson(jsonMovieResponse);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(List<MovieItem> movieItems) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (movieItems != null) {
                showMovieDataView();
                mMovieAdapter.setMovieData(movieItems);
            } else {
                showErrorMessage();
            }
        }
    }


}

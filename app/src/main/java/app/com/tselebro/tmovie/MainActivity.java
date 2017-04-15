package app.com.tselebro.tmovie;

import android.content.res.Configuration;
import android.os.AsyncTask;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
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

public class MainActivity extends AppCompatActivity  implements MovieAdapter.MovieAdapterOnClickHandler{

    private String mSortorder;
    private Spinner mSpinner;

    LinearLayout mContainer;
    private RecyclerView mRecyclerView;
    private ProgressBar mLoadingIndicator;
    private MovieAdapter mMovieAdapter;
    private Button retry;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContainer = (LinearLayout) findViewById(R.id.recycler_container);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView_movies);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        retry = (Button) findViewById(R.id.btn_retry);


            int spanCount = 2;
            GridLayoutManager layoutManager
                    = new GridLayoutManager(this, spanCount);
            mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);
        mMovieAdapter = new MovieAdapter(this, this);
        mRecyclerView.setAdapter(mMovieAdapter);

        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadMovieData(mSortorder);
            }
        });



    }

    @Override
    public void onClick(MovieItem movieItem) {

    }


    public class FetchMoviePosterTask extends AsyncTask<String, Void, List<MovieItem>>{


        @Override
        protected List<MovieItem> doInBackground(String... strings) {

            if (strings.length == 0){
                return  null;
            }

            String sortOrder = strings[0];
            URL movieRequestUrl = NetworkUtils.buildUrl(sortOrder);

            try {
                String jsonMovieResponse = NetworkUtils
                        .getResponseFromHttpUrl(movieRequestUrl);
                List<MovieItem> simpleJsonMovieData = MovieWeatherJsonUtils
                        .getSimpleMovieStringsFromJson(jsonMovieResponse);

                return  simpleJsonMovieData;
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return  null;
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
            if (movieItems != null)
            {
                showMovieDataView();
                mMovieAdapter.setMovieData(movieItems);
            }
            else {
                showErrorMessage();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        MenuItem item = menu.findItem(R.id.spinner);
        mSpinner =(Spinner) MenuItemCompat.getActionView(item);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spinner_list_item_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String queryType;

                switch (i){
                    case 0 :
                        queryType = Constants.POPULAR_DESC_PARAMETER;
                        loadMovieData(queryType);
                        break;
                    case 1:
                        queryType = Constants.VOTED_DESC;
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
        mSortorder = sortOrder;
        showMovieDataView();

        new FetchMoviePosterTask().execute(mSortorder);

    }

    private void showMovieDataView() {
        mContainer.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mContainer.setVisibility(View.VISIBLE);
    }


}

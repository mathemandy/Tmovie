package com.ng.tselebro.tmovie;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ng.tselebro.tmovie.Adapter.Adapter;
import com.ng.tselebro.tmovie.Models.MovieItem;
import com.ng.tselebro.tmovie.data.MovieContract;
import com.ng.tselebro.tmovie.data.MoviesDBHelper;
import com.ng.tselebro.tmovie.fragments.AboutFragment;
import com.ng.tselebro.tmovie.fragments.ReviewFragment;
import com.ng.tselebro.tmovie.fragments.TrailerFragment;
import com.ng.tselebro.tmovie.utilities.Constants;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import app.ng.tselebro.tmovie.R;


public class MovieDetails extends AppCompatActivity {

    private static  final String TAG = MovieDetails.class.getSimpleName();
    private MovieItem movieItem;
    private ImageView mPosterView, mPosterView2;
    private ProgressBar loading_indicator;
    private TextView  max_rating, currentRating, currentRating2, mreleaseDate, Title;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private MoviesDBHelper mMovieDbHelper;
    private CollapsingToolbarLayout cab;
    private FloatingActionButton fab;
    private SQLiteDatabase db;
    private int movie_id;
    private static String[] selectionArgs;
    private String selection = MovieContract.MovieEntry.COLUMN_MOVIE_ID + "=?";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);



        Toolbar mtoolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        cab = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        if (cab != null){
            cab.setTitleEnabled(true);
        }


        mMovieDbHelper = new MoviesDBHelper(this);


        Intent  i = getIntent();
        if (i != null){
            Bundle bundle = i.getExtras();
            if (bundle != null) {

                movieItem = bundle.getParcelable("movies");
                init_views();
                getSupportActionBar().setTitle(movieItem.getTitle());
                fillData(movieItem);
            }


        }



    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        new AboutFragment();
        adapter.addFragment(AboutFragment.newInstance(movieItem), "About");
        new TrailerFragment();
        adapter.addFragment(TrailerFragment.newInstance(movieItem), "Trailers");
        new ReviewFragment();
        adapter.addFragment(ReviewFragment.newInstance(movieItem), "Reviews");

        viewPager.setAdapter(adapter);
        if (movieIsStored()) {
            fab.setImageResource(R.drawable.favorite);
        } else {
            fab.setImageResource(R.drawable.favorite_border);
        }
    }

    private void init_views() {


        mPosterView = (ImageView) findViewById(R.id.backdrop);
        mPosterView2 = (ImageView) findViewById(R.id.backdroplarge);
        loading_indicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        currentRating = (TextView)  findViewById(R.id.current_rating);
        currentRating2 = (TextView) findViewById(R.id.current_ratingLarge);
        max_rating = (TextView) findViewById(R.id.max_rating);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        fab =(FloatingActionButton)findViewById(R.id.fab);
        mreleaseDate = (TextView) findViewById(R.id.releaseDateLarge);
        Title = (TextView)findViewById(R.id.titleLarge);
    }

    private boolean movieIsStored() {
        db = mMovieDbHelper.getReadableDatabase();
        String whereClause = MovieContract.MovieEntry.COLUMN_MOVIE_ID + "=?";
        String[] whereArgs = {String.valueOf(movie_id)};
        Cursor cursor = db.query(
                MovieContract.MovieEntry.TABLE_NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        if (cursor.getCount() == 0) {
            cursor.close();
            return false;

        } else {
            cursor.close();
            return true;

        }

    }

    private void fillData(MovieItem movieItem) {
         /*Get the Resource from the database*/


        movie_id = (int) movieItem.getMovieId();
        final String userRating = Double.toString(movieItem.getUserRating());
        final String title = movieItem.getTitle();
        final String posterPath = movieItem.getPosterPath();
        final String overview = movieItem.getOverview();
        final String releaseDate = movieItem.getReleaseDate();
        final String backDropPath = movieItem.getBackdropPath();
        String backDropUrl = Constants.BACKDROP_URL_BASE_PATH + backDropPath;
        String posterUrl = Constants.THUMB_URL_BASE_PATH + posterPath;

        selectionArgs = new String[]{String.valueOf(movie_id)};
        currentRating.setText(String.valueOf(userRating));
        max_rating.setText(R.string.demo_max_rating);
        int orientations = this.getResources().getConfiguration().orientation;
        int screenWiidth = this.getResources().getConfiguration().smallestScreenWidthDp;
        if (orientations == Configuration.ORIENTATION_PORTRAIT && screenWiidth == 600) {
            Title.setText(title);
            currentRating2.setText(String.valueOf(userRating));
            mreleaseDate.setText(releaseDate);
            Picasso.with(getApplicationContext()).load(posterUrl).into(mPosterView2);
        }


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (movieIsStored()) {
                    fab.setImageResource(R.drawable.favorite);
                    int deletedMovie = getContentResolver().delete(MovieContract.MovieEntry
                            .CONTENT_URI, selection, selectionArgs);
                    Log.i("Movie deleted", String.valueOf(deletedMovie));
                    Toast.makeText(MovieDetails.this, title +
                            " has been removed from My Favorites", Toast.LENGTH_LONG).show();
                } else {
                    fab.setImageResource(R.drawable.favorite_border);
                    ContentValues values = new ContentValues();
                    values.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE, title);
                    values.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, String.valueOf(movie_id));
                    values.put(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW, overview);
                    values.put(MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE, userRating);
                    values.put(MovieContract.MovieEntry.COLUMN_MOVIE_BACKDROP_PATH, backDropPath);
                    values.put(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH, posterPath);
                    values.put(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE, releaseDate);
                    Uri updatedMovie = getContentResolver().insert(MovieContract.MovieEntry
                            .CONTENT_URI, values);
                    Log.i("Movie Added ", String.valueOf(updatedMovie) + " | Title: " + title);
                    Toast.makeText(MovieDetails.this, title +
                            " has being added to My Favorites", Toast.LENGTH_LONG).show();
                }

            }
        });


        /*Setup the view Pager*/
        if (mViewPager != null) {
            setupViewPager(mViewPager);
        }
        /*Attach ViewPager to the tab Layout*/
        mTabLayout.setupWithViewPager(mViewPager);

        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                mPosterView.setImageBitmap(bitmap);
                loading_indicator.setVisibility(View.GONE);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                mPosterView.setBackground(getApplicationContext().getDrawable(R.drawable.ic_video_cam_off_black_48dp));
               loading_indicator.setVisibility(View.GONE);
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                mPosterView.setBackground(placeHolderDrawable);
                           loading_indicator.setVisibility(View.VISIBLE);
            }
        };
        Picasso.with(getApplicationContext()).load(backDropUrl).into(target);



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}


package app.com.tselebro.tmovie;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import app.com.tselebro.tmovie.Models.MovieItem;
import app.com.tselebro.tmovie.utilities.Constants;


public class MovieDetails extends AppCompatActivity{


    private MovieItem movieItem;
    private TextView mTitleView;
    private ImageView mPosterView;
    private TextView mMDYear;
    private TextView mRatingAndVotes;
    private TextView mSynopsis;
    private ProgressBar mLoadProgress;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);



                Bundle bundle = getIntent().getExtras();

        if (bundle != null){
            if (bundle.containsKey("movie")){
                movieItem = bundle.getParcelable("movie");
                init_views();
            }
        }



    }


        private void init_views(){
            mSynopsis = (TextView) findViewById(R.id.movie_detail_synopsis);
            mTitleView = (TextView) findViewById(R.id.movie_detail_title);
            mPosterView = (ImageView) findViewById(R.id.movie_detail_poster_image);
            mMDYear = (TextView) findViewById(R.id.movie_detail_year);
            mRatingAndVotes = (TextView) findViewById(R.id.movie_detail_rating_and_votes);
            mLoadProgress = (ProgressBar) findViewById(R.id.movie_detail_progress_circle);
            fillData();
        }

    private void fillData (){

                mSynopsis.setText(movieItem.getOverview());
                mTitleView.setText(movieItem.getTitle());
                mMDYear.setText(movieItem.getReleaseDate());
                mRatingAndVotes.setText(String.valueOf(movieItem.getUserRating()));

                String backDropUrl = Constants.THUMB_URL_BASE_PATH + movieItem.getPosterPath();
                Target target = new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        mPosterView.setImageBitmap(bitmap);
                        mLoadProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {
                        mPosterView.setBackground(errorDrawable);
                        mLoadProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                        mPosterView.setBackground(placeHolderDrawable);
                        mLoadProgress.setVisibility(View.VISIBLE);
                    }
                };
                Picasso.with(getApplicationContext()).load(backDropUrl).into(target);
            }


}

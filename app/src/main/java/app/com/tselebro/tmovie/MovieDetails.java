package app.com.tselebro.tmovie;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import app.com.tselebro.tmovie.Adapter.Adapter;
import app.com.tselebro.tmovie.Models.MovieItem;
import app.com.tselebro.tmovie.fragments.AboutFragment;
import app.com.tselebro.tmovie.fragments.ReviewFragment;
import app.com.tselebro.tmovie.fragments.TrailerFragment;
import app.com.tselebro.tmovie.utilities.Constants;


public class MovieDetails extends AppCompatActivity {


    private MovieItem movieItem;
    private ImageView mPosterView;
    private ProgressBar loading_indicator;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            if (bundle.containsKey("movie")) {
                movieItem = bundle.getParcelable("movie");

                ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
                if (viewPager != null) {
                    setupViewPager(viewPager);
                }
                TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
                tabLayout.setupWithViewPager(viewPager);

                init_views();
            }
        }


    }
    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        new TrailerFragment();
        adapter.addFragment(TrailerFragment.newInstance(movieItem), "Trailers");
        new ReviewFragment();
        adapter.addFragment(ReviewFragment.newInstance(movieItem), "Reviews");
        new AboutFragment();
        adapter.addFragment(AboutFragment.newInstance(movieItem), "About");
        viewPager.setAdapter(adapter);
    }

    private void init_views() {
        mPosterView = (ImageView) findViewById(R.id.backdrop);
        loading_indicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        fillData();
    }

    private void fillData() {
                String backDropUrl = Constants.BACKDROP_URL_BASE_PATH + movieItem.getBackdropPath();
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


}


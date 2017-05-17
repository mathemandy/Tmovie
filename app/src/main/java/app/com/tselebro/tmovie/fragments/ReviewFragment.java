package app.com.tselebro.tmovie.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.net.URL;
import java.util.List;

import app.com.tselebro.tmovie.Adapter.ReviewAdapter;
import app.com.tselebro.tmovie.Models.MovieItem;
import app.com.tselebro.tmovie.Models.ReviewItem;
import app.com.tselebro.tmovie.R;
import app.com.tselebro.tmovie.databinding.FragmentReviewBinding;
import app.com.tselebro.tmovie.utilities.MovieJsonUtils;
import app.com.tselebro.tmovie.utilities.MovieUtil;
import app.com.tselebro.tmovie.utilities.NetworkUtils;


public class ReviewFragment extends Fragment  implements ReviewAdapter.ReviewAdapterOnClickHandler, LoaderManager.LoaderCallbacks<List<ReviewItem>>{

    private static final int REVIEW_LOADER = 3;
    private List<ReviewItem> mReview;
    ReviewAdapter mReviewAdapter;
    MovieItem mMovieItem;
    FragmentReviewBinding mBinding;


    public ReviewFragment() {

    }

    public static  ReviewFragment newInstance (MovieItem movie) {
        ReviewFragment fragment = new ReviewFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("movie", movie);
        fragment.setArguments(bundle);
        return  fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setRetainInstance(true);
        super.onCreate(savedInstanceState);
        mMovieItem = getArguments().getParcelable(getString(R.string.MOVIE_KEY));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mReviewAdapter = new ReviewAdapter(getContext(), this);
        mBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_review, container, false);

        return  mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRecyclerView();
    }

    private void setupRecyclerView (){

        mBinding.recyclerViewReview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mBinding.recyclerViewReview.setHasFixedSize(true);
        mBinding.recyclerViewReview.setAdapter(mReviewAdapter);

    }

    @Override
    public void onClick(ReviewItem reviewItem) {
        Context context  = getContext();
        String reviewItemUrlUrl = reviewItem.getUrl();

        if (reviewItemUrlUrl != null){
            MovieUtil.openUrlInBrowser(reviewItemUrlUrl, context);
        }

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        getLoaderManager().initLoader(REVIEW_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<List<ReviewItem>> onCreateLoader(int id, Bundle args) {
        return  new AsyncTaskLoader<List<ReviewItem>>(getContext()) {
            @Override
            public List<ReviewItem> loadInBackground() {
                int movie_id = mMovieItem.getMovieId();
                URL reviewUrl = NetworkUtils.buildReviewUrl(movie_id);

                try {
                    String jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(reviewUrl);
                    return MovieJsonUtils.getSimpleReviewStringsFromJson(jsonMovieResponse);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onStartLoading() {
                if (mReview != null) {
                   deliverResult(mReview);
                } else{
                    forceLoad();
                }
            }


            public void deliverResult (List<ReviewItem> data){
                mReview = data;
                super.deliverResult(data);
            }
        };
    }
    @Override
    public void onLoadFinished(Loader<List<ReviewItem>> loader, List<ReviewItem> data) {
                mReviewAdapter.setReviewData(data);
        if (data != null) {
            mBinding.recyclerViewReview.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<ReviewItem>> loader) {

    }
}







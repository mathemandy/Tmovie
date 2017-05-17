package app.com.tselebro.tmovie.fragments;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchUIUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.net.URL;
import java.util.List;

import app.com.tselebro.tmovie.Adapter.TrailerAdapter;
import app.com.tselebro.tmovie.Models.MovieItem;
import app.com.tselebro.tmovie.Models.TrailerItem;
import app.com.tselebro.tmovie.R;
import app.com.tselebro.tmovie.databinding.FragmentTrailerBinding;
import app.com.tselebro.tmovie.utilities.MovieJsonUtils;
import app.com.tselebro.tmovie.utilities.MovieUtil;
import app.com.tselebro.tmovie.utilities.NetworkUtils;


public class TrailerFragment extends Fragment implements TrailerAdapter.TrailerAdapterOnClickHandler, LoaderManager.LoaderCallbacks<List<TrailerItem>> {
    private static final int TRAILER_LOADER = 2;
    TrailerAdapter mTrailerAdapter;
    private List<TrailerItem> mTrailer;
    MovieItem mMovieItem;
    FragmentTrailerBinding mBinding;


    public TrailerFragment() {
    }


    public static TrailerFragment newInstance(MovieItem movie) {
        TrailerFragment fragment = new TrailerFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("movie", movie);
        fragment.setArguments(bundle);

        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        super.onCreate(savedInstanceState);
        mMovieItem = getArguments().getParcelable(getString(R.string.MOVIE_KEY));

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRecyclerView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mTrailerAdapter = new TrailerAdapter(getContext(), this);
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_trailer, container, false);

        return mBinding.getRoot();
    }

    private void setupRecyclerView() {
        mBinding.recyclerViewReview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mBinding.recyclerViewReview.setHasFixedSize(true);
        mBinding.recyclerViewReview.setAdapter(mTrailerAdapter);
    }

    @Override
    public void onClick(TrailerItem trailerItem) {
        Context context = getContext();

        String urlId = trailerItem.getmId();

        if (urlId != null) {
            MovieUtil.watchYoutubeVideo(urlId, context);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

       getLoaderManager().initLoader(TRAILER_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public Loader<List<TrailerItem>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<List<TrailerItem>>(getContext()) {
            @Override
            public List<TrailerItem> loadInBackground() {
                int movie_id = mMovieItem.getMovieId();

                URL Trailerurl = NetworkUtils.buildTrailerUrl((movie_id));

                try {
                    String jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(Trailerurl);
                    return MovieJsonUtils.getSimpleTrailerStringsFromJson(jsonMovieResponse);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onStartLoading() {
                if (mTrailer != null){
                    deliverResult(mTrailer);
                }else  {
                    forceLoad();
                }
            }

            public void deliverResult(List<TrailerItem> data){
                mTrailer = data;
                super.deliverResult(data);
            }
        };
    }
    @Override
    public void onLoadFinished(Loader<List<TrailerItem>> loader, List<TrailerItem> data) {
      mTrailerAdapter.setTrailerData(data);
        if (data != null){
        mBinding.recyclerViewReview.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onLoaderReset(Loader<List<TrailerItem>> loader) {

    }
}

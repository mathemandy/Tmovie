package app.com.tselebro.tmovie.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import app.com.tselebro.tmovie.Models.MovieItem;
import app.com.tselebro.tmovie.R;
import app.com.tselebro.tmovie.databinding.FragmentAboutBinding;

public class AboutFragment extends Fragment {
    MovieItem mMovieItem;
    FragmentAboutBinding mBinding;

    public AboutFragment() {

    }

    public static AboutFragment newInstance(MovieItem movie){
        AboutFragment fragment = new AboutFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("movie", movie);
        fragment.setArguments(bundle);

        return  fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        super.onCreate(savedInstanceState);
        mMovieItem = getArguments().getParcelable(getString(R.string.MOVIE_KEY));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_about, container, false);
        return  mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (view != null ){

            if (mMovieItem != null){

                String description = mMovieItem.getOverview();
                mBinding.description.setText(description);

                String releaseDate = mMovieItem.getReleaseDate();
                mBinding.releasedate.setText(releaseDate);
            }

        }

    }
}

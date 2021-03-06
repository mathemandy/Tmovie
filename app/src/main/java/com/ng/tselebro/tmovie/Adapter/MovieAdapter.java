package com.ng.tselebro.tmovie.Adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ng.tselebro.tmovie.Models.MovieItem;
import com.ng.tselebro.tmovie.utilities.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import app.ng.tselebro.tmovie.R;

public class MovieAdapter  extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder>{


    private final MovieAdapterOnClickHandler mClickHandler;

    private List<MovieItem> movies = new ArrayList<>();

    private final Context mContext;

    /**
     * The interface that receives onClick messages.
     */

    public interface MovieAdapterOnClickHandler {
        void onClick (MovieItem movieItem);
    }


    public MovieAdapter(Context context, MovieAdapterOnClickHandler clickHandler){
        mContext = context;
        mClickHandler = clickHandler;
    }


    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context  = parent.getContext();
        int layoutIdForGridItem = R.layout.grid_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForGridItem, parent, false);

        return  new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position) {
        holder.mUserrating.setText(String.valueOf(movies.get(position).getUserRating()));
        String  imgUrl = Constants.THUMB_URL_BASE_PATH  + movies.get(position).getPosterPath() ;
        Picasso.with(mContext).load(imgUrl).into(holder.mImageView);
    }


    @Override
    public int getItemCount() {
        if (null == movies ) return  0;

        return  movies.size();
    }


    class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        final ImageView mImageView;
        final TextView mUserrating;

         MovieAdapterViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.imageView);
             mUserrating = (TextView) itemView.findViewById(R.id.tv_average);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            MovieItem movieItem = movies.get(adapterPosition);
            mClickHandler.onClick(movieItem);
        }
    }
    public void setMovieData(List<MovieItem> movieData){
        movies = movieData;
        notifyDataSetChanged();
    }
}

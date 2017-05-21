package com.ng.tselebro.tmovie.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import com.ng.tselebro.tmovie.Models.TrailerItem;
import app.ng.tselebro.tmovie.R;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerAdapterViewHolder> {

    private final TrailerAdapterOnClickHandler mClickHandler;

    private List<TrailerItem> trailers = new ArrayList<>();

    private final Context mContext;
    private final String THUMBNAIL_BASE_URL = "https://img.youtube.com/vi/";
    private final String THUMBNAIL_END_URL = "/hqdefault.jpg";



    public TrailerAdapter(Context context, TrailerAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    @Override
    public TrailerAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.traileritem;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, parent, false);

        return  new TrailerAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerAdapterViewHolder holder, int position) {

        String imgUrl = THUMBNAIL_BASE_URL +  trailers.get(position).getmKey() + THUMBNAIL_END_URL;
        Picasso.with(mContext).load(imgUrl).placeholder(R.drawable.ic_video_cam_off_black_48dp).into(holder.defTrailerPoster);

        String name = trailers.get(position).getmName();
        holder.name.setText(name);

        String type = trailers.get(position).getmType();
        holder.type.setText(type);

    }

    @Override
    public int getItemCount() {
        if (null == trailers) return  0;
        return  trailers.size();
    }

    class TrailerAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView defTrailerPoster;
        final TextView  name;
        final TextView type;


        TrailerAdapterViewHolder(View view){
            super(view);
            defTrailerPoster = (ImageView) view.findViewById(R.id.trailer_poster_imageView);
            name = (TextView) view.findViewById(R.id.trailer_name_textView);
            type = (TextView)  view.findViewById(R.id.trailer_clip_textview);
            view.setOnClickListener(this);

        }


        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            TrailerItem trailerItem = trailers.get(adapterPosition);
            mClickHandler.onClick(trailerItem);

        }
    }

    public void setTrailerData (List<TrailerItem> trailerData ){
        trailers = trailerData;
        notifyDataSetChanged();
    }
    public interface TrailerAdapterOnClickHandler {
        void onClick(TrailerItem trailerItem);
    }


}




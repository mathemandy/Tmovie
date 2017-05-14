package app.com.tselebro.tmovie.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import app.com.tselebro.tmovie.Models.TrailerItem;
import app.com.tselebro.tmovie.R;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerAdapterViewHolder> {

    private final TrailerOnclickHandler mClickHandler;

    private List<TrailerItem> trailers = new ArrayList<>();

    private final Context mContext;

    private final String THUMBNAIL_BASE_URL = "https://img.youtube.com/vi/";
    private final String THUMBNAIL_END_URL = "/hqdefault.jpg";


    public interface TrailerAdapterOnClickHandler {
        void onClick(TrailerItem trailerItem);
    }

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
    public void onBindViewHolder(TrailerAdapter.TrailerAdapterViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }



}




package app.com.tselebro.tmovie.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import app.com.tselebro.tmovie.Models.ReviewItem;
import app.com.tselebro.tmovie.R;

public class ReviewAdapter extends  RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder> {

    private ReviewAdapterOnClickHandler mOnclickHandler;

    private List<ReviewItem> reviews = new ArrayList<>();

    private Context mContext;


    public ReviewAdapter(Context context, ReviewAdapterOnClickHandler reviewHandler) {
        mContext = context;
        mOnclickHandler = reviewHandler;

    }

    @Override
    public ReviewAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int LayoutIdForListItem = R.layout.review_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(LayoutIdForListItem, parent, false);

        return new ReviewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewAdapterViewHolder holder, int position) {

        String author = reviews.get(position).getAuthor();
        holder.author.setText(author);

        String content = reviews.get(position).getContent();
        holder.content.setText(content);

        String url = reviews.get(position).getUrl();
        holder.url.setText(url);


    }

    @Override
    public int getItemCount() {
        if (null == reviews) return 0;
        return reviews.size();
    }

    class ReviewAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final ImageView defReviewImage;
        final TextView author;
        final TextView content;
        final TextView url;

        ReviewAdapterViewHolder(View view) {
            super(view);
            defReviewImage = (ImageView) view.findViewById(R.id.review_avatar_imageView);
            author = (TextView) view.findViewById(R.id.review_author_textView);
            content = (TextView) view.findViewById(R.id.review_content_textview);
            url = (TextView) view.findViewById(R.id.review_url_textview);
            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            ReviewItem reviewItem = reviews.get(adapterPosition);
            mOnclickHandler.onClick(reviewItem);

        }
    }

    public interface ReviewAdapterOnClickHandler {
        void onClick(ReviewItem reviewItem);
    }


        public void setReviewData (List<ReviewItem> reviewData){
            reviews = reviewData;
            notifyDataSetChanged();
        }
}

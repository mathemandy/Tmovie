package com.ng.tselebro.tmovie.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class ReviewItem implements Parcelable
{

    private String mId;
    private String author;
    private String content;
    private String url;

    public ReviewItem(String id, String author, String content, String url) {
        this.mId = id;
        this.author = author;
        this.content = content;
        this.url = url;
    }


    private ReviewItem(Parcel in) {
        mId = in.readString();
        author = in.readString();
        content = in.readString();
        url = in.readString();
    }

    public static final Creator<ReviewItem> CREATOR = new Creator<ReviewItem>() {
        @Override
        public ReviewItem createFromParcel(Parcel in) {
            return new ReviewItem(in);
        }

        @Override
        public ReviewItem[] newArray(int size) {
            return new ReviewItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mId);
        parcel.writeString(author);
        parcel.writeString(content);
        parcel.writeString(url);
    }


    public String getmId() {
        return mId;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }
}

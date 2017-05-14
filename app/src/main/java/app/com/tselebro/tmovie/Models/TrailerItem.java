package app.com.tselebro.tmovie.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mathemandy on 5/13/17.
 */

public class TrailerItem implements Parcelable {


    private String mId;
    private String key;
    private String name;
    private String type;

    public TrailerItem(String mId, String key, String name, String type) {
        this.mId = mId;
        this.key = key;
        this.name = name;
        this.type = type;
    }


    protected TrailerItem(Parcel in) {
        mId = in.readString();
        key = in.readString();
        name = in.readString();
        type = in.readString();
    }

    public static final Creator<TrailerItem> CREATOR = new Creator<TrailerItem>() {
        @Override
        public TrailerItem createFromParcel(Parcel in) {
            return new TrailerItem(in);
        }

        @Override
        public TrailerItem[] newArray(int size) {
            return new TrailerItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mId);
        parcel.writeString(key);
        parcel.writeString(name);
        parcel.writeString(type);
    }

    public String getmId() {
        return mId;
    }

    public String getmKey() {
        return key;
    }

    public String getmName() {
        return name;
    }

    public String getmType() {
        return type;
    }

}

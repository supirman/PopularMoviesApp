package com.example.android.popularmoviesapp.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by firman on 07/07/17.
 * Review Parcelable Model
 */

public class Review implements Parcelable {
    public String getId() {
        return id;
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

    private final String id;
    private final String author;
    private final String content;
    private final String url;

    protected Review(Parcel in) {
        id      = in.readString();
        author  = in.readString();
        content = in.readString();
        url     = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(author);
        dest.writeString(content);
        dest.writeString(url);
    }

    public static final Parcelable.Creator<Review> CREATOR = new Parcelable.Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    public Review(JSONObject review) throws JSONException {
        id      = review.getString("id");
        author  = review.getString("author");
        content = review.getString("content");
        url     = review.getString("url");
    }
}

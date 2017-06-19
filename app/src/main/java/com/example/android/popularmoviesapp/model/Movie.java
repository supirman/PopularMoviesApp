package com.example.android.popularmoviesapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by firman on 19/06/17.
 */

public class Movie implements Parcelable {

    private final int id;
    private final String title;
    private final String image;
    private final String detailImage;
    private final String overview;
    private final int rating;
    private final String release_date;

    protected Movie(Parcel in) {
        id          = in.readInt();
        title       = in.readString();
        image       = in.readString();
        detailImage = in.readString();
        overview    = in.readString();
        rating      = in.readInt();
        release_date= in.readString();
    }

    public Movie(JSONObject movie) throws JSONException {

        this.id         = movie.getInt("id");
        this.title      = movie.getString("original_title");
        this.image      = movie.getString("poster_path");
        this.detailImage= movie.getString("backdrop_path");
        this.overview   = movie.getString("overview");
        this.rating     = movie.getInt("vote_average");
        this.release_date = movie.getString("release_date");

    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public String getDetailImage() {
        return detailImage;
    }

    public String getOverview() {
        return overview;
    }

    public int getRating() {
        return rating;
    }

    public String getRelease_date() {
        return release_date;
    }


    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(image);
        dest.writeString(detailImage);
        dest.writeString(overview);
        dest.writeInt(rating);
        dest.writeString(release_date);
    }
}

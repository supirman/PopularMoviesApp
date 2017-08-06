package com.example.android.popularmoviesapp.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.android.popularmoviesapp.data.FavoriteContract;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by firman on 19/06/17.
 * Movie Model
 */

public class Movie implements Parcelable {

    private final int id;
    private final String title;
    private final String image;
    private final String detailImage;
    private final String overview;
    private final double rating;
    private final String release_date;

    protected Movie(Parcel in) {
        id          = in.readInt();
        title       = in.readString();
        image       = in.readString();
        detailImage = in.readString();
        overview    = in.readString();
        rating      = in.readDouble();
        release_date= in.readString();
    }

    public Movie(JSONObject movie) throws JSONException {

        this.id         = movie.getInt("id");
        this.title      = movie.getString("original_title");
        this.image      = movie.getString("poster_path");
        this.detailImage= movie.getString("backdrop_path");
        this.overview   = movie.getString("overview");
        this.rating     = movie.getDouble("vote_average");
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

    public double getRating() {
        return rating;
    }

    public String getReleaseDate() {
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
        dest.writeDouble(rating);
        dest.writeString(release_date);
    }

    public ContentValues toContentValues(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(FavoriteContract.FavoriteEntry._ID, this.getId());
        contentValues.put(FavoriteContract.FavoriteEntry.COLUMN_TITLE, this.getTitle());
        contentValues.put(FavoriteContract.FavoriteEntry.COLUMN_POSTER_PATH, this.getImage());
        contentValues.put(FavoriteContract.FavoriteEntry.COLUMN_BACKDROP_PATH, this.getDetailImage());
        contentValues.put(FavoriteContract.FavoriteEntry.COLUMN_OVERVIEW, this.getOverview());
        contentValues.put(FavoriteContract.FavoriteEntry.COLUMN_VOTE_AVERAGE, this.getRating());
        contentValues.put(FavoriteContract.FavoriteEntry.COLUMN_RELEASE_DATE, this.getReleaseDate());
        return contentValues;
    }


    public Movie(Cursor cursor){
        this.id         = cursor.getInt(cursor.getColumnIndex(FavoriteContract.FavoriteEntry._ID));
        this.title      = cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_TITLE));
        this.image      = cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_POSTER_PATH));
        this.detailImage= cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_BACKDROP_PATH));
        this.overview   = cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_OVERVIEW));
        this.rating     = cursor.getDouble(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_VOTE_AVERAGE));
        this.release_date = cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_RELEASE_DATE));
    }

}

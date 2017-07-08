package com.example.android.popularmoviesapp.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by firman on 08/07/17.
 * Trailer Model
 */

public class Trailer implements Parcelable{
    private final String id, key, name, site, type;

    public String getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getSite() {
        return site;
    }

    public String getType() {
        return type;
    }

    public int getSize() {
        return size;
    }

    private final int size;

    private Trailer(Parcel in) {
        id   = in.readString();
        key  = in.readString();
        name = in.readString();
        site = in.readString();
        type = in.readString();
        size = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(key);
        dest.writeString(name);
        dest.writeString(site);
        dest.writeString(type);
        dest.writeInt(size);
    }

    public static final Parcelable.Creator<Trailer> CREATOR = new Parcelable.Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel in) {
            return new Trailer(in);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };
    public Trailer(JSONObject trailer) throws JSONException {
        id   = trailer.getString("id");
        key  = trailer.getString("key");
        name = trailer.getString("name");
        site = trailer.getString("site");
        type = trailer.getString("type");
        size = trailer.getInt("size");
    }
}

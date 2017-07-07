package com.example.android.popularmoviesapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import android.text.TextUtils;

import com.example.android.popularmoviesapp.utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;

/**
 * Created by firman on 06/07/17.
 * TMDB API AsyncTaskLoader
 */

class TMDBJSONAsyncTaskLoader extends AsyncTaskLoader<JSONArray> {
    private Bundle Args;
    private JSONArray mData;

    TMDBJSONAsyncTaskLoader(Context context, Bundle args) {
        super(context);
        Args = args;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if (Args == null) return;
        if (mData != null) {
            deliverResult(mData);
        } else {
            forceLoad();
        }
    }

    @Override
    public JSONArray loadInBackground() {
        String url = Args.getString(NetworkUtils.URL_EXTRA);
        if (TextUtils.isEmpty(url)) return null;
        JSONArray resultJSONArray = null;
        try {
            String stringResult = NetworkUtils.getResponseFromHttpUrl(new URL(url));
            JSONObject resultJson = new JSONObject(stringResult);
            resultJSONArray = resultJson.getJSONArray("results");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultJSONArray;
    }

    @Override
    public void deliverResult(JSONArray data) {
        mData = data;
        super.deliverResult(data);
    }
}

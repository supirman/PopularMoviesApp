package com.example.android.popularmoviesapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import android.text.TextUtils;

import com.example.android.popularmoviesapp.model.Movie;
import com.example.android.popularmoviesapp.utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by firman on 06/07/17.
 * Movie AsyncTasLoader
 */

class MovieAsyncTaskLoader extends AsyncTaskLoader<List<Movie>> {
    private Bundle Args;
    private List<Movie> mMovieList;

    MovieAsyncTaskLoader(Context context, Bundle args) {
        super(context);
        Args = args;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if (Args == null) return;
        if(mMovieList!=null) {
            deliverResult(mMovieList);
        }
        else {
            forceLoad();
        }
    }

    @Override
    public List<Movie> loadInBackground() {
        String url = Args.getString(NetworkUtils.MOVIE_QUERY_URL_EXTRA);
        if (TextUtils.isEmpty(url)) return null;
        List<Movie> movieList = null;
        try {
            String movieQueryResult = NetworkUtils.getResponseFromHttpUrl(new URL(url));
            JSONObject movieJson = new JSONObject(movieQueryResult);
            JSONArray movieArray = movieJson.getJSONArray("results");
            movieList = new ArrayList<>();
            for (int i = 0; i < movieArray.length(); i++) {
                JSONObject movie = movieArray.getJSONObject(i);
                Movie m = new Movie(movie);
                movieList.add(m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return movieList;
    }

    @Override
    public void deliverResult(List<Movie> data) {
        mMovieList = data;
        super.deliverResult(data);
    }
}

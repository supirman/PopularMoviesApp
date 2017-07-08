/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.popularmoviesapp.utilities;

import android.net.Uri;
import android.util.Log;

import com.example.android.popularmoviesapp.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * These utilities will be used to communicate with the movie db servers.
 * Taken from Sunshine NetworkUtils in Android Udacity course with some modification.
 */
public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String TMDB_BASE_URL = "https://api.themoviedb.org/3/movie/";

    private final static String SORT_BY_PARAM = "sort_by";
    private final static String API_KEY_PARAM = "api_key";
    private final static String MOVIE_ID_PARAM= "movie_id";
    private final static String LANG_PARAM    = "lang";
    private final static String DEFAULT_LANG  = "en-US";

    private final static String REVIEWS_PATH  = "/reviews";
    private final static String VIDEOS_PATH   = "/videos";

    private static final String IMAGE_BASE_URL= "http://image.tmdb.org/t/p/";
    private static final String POSTER_SIZE   = "w342";
    public static final String URL_EXTRA      = "url";

    private static final String YT_THUMB_BASE_URL   = "https://img.youtube.com/vi/";
    private static final String YT_THUMB_SIZE       = "/default.jpg";
    /**
     * Builds the URL used to talk to the TMDB server using a sort by.
     *
     * @param sortby The location that will be queried for.
     * @return The URL to use to query the weather server.
     */
    public static URL buildMovieUrl(String sortby) {
        String api_key = BuildConfig.THE_MOVIE_DB_API_TOKEN;
        Uri builtUri = Uri.parse(TMDB_BASE_URL+sortby).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, api_key)
                .build();
        return _parseURI(builtUri);
    }

    public static URL buildReviewUrl(int movie_id) {
        String api_key = BuildConfig.THE_MOVIE_DB_API_TOKEN;
        Uri builtUri = Uri.parse(TMDB_BASE_URL+movie_id+ REVIEWS_PATH).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, api_key)
                .appendQueryParameter(MOVIE_ID_PARAM, String.valueOf(movie_id))
                .appendQueryParameter(LANG_PARAM, DEFAULT_LANG)
                .build();
        return _parseURI(builtUri);
    }

    public static URL buildVideoUrl(int movie_id) {
        String api_key = BuildConfig.THE_MOVIE_DB_API_TOKEN;
        Uri builtUri = Uri.parse(TMDB_BASE_URL+movie_id+VIDEOS_PATH).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, api_key)
                .appendQueryParameter(MOVIE_ID_PARAM, String.valueOf(movie_id))
                .appendQueryParameter(LANG_PARAM, DEFAULT_LANG)
                .build();
        return _parseURI(builtUri);
    }

    public static String YTThumbnailBuilder(String key){
        return YT_THUMB_BASE_URL+key+YT_THUMB_SIZE;
    }

    private static URL _parseURI(Uri uri){
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        if(BuildConfig.DEBUG) Log.v(TAG, "Built URI " + url);
        return url;
    }


    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static String getPosterURL(String imageIdentifier){
        return IMAGE_BASE_URL + POSTER_SIZE + imageIdentifier;
    }
}
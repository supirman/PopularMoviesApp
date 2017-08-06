package com.example.android.popularmoviesapp.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by firman on 06/08/17.
 * Favorite Contract
 */

public class FavoriteContract {
    public static final String SCHEME = "content://";
    public static final String AUTHORITY = "com.example.android.popularmoviesapp";
    public static final Uri BASE_CONTENT_URI = Uri.parse(SCHEME+AUTHORITY);
    public static final String FAVORITE_PATH = "favorites";

    public static final class FavoriteEntry implements BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(FAVORITE_PATH).build();

        public static final String TABLE_NAME = "favorites";

        public static final String COLUMN_TITLE = "name";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_BACKDROP_PATH = "backdrop_path";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_RELEASE_DATE = "release_date";

    }
}

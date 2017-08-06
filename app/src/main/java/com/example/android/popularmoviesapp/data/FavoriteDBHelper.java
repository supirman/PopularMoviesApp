package com.example.android.popularmoviesapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * Created by firman on 06/08/17.
 * FavoriteDBHelper
 */

class FavoriteDBHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "favsDb.db";
    private static final int VERSION = 1;

    public FavoriteDBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE = "CREATE TABLE "  + FavoriteContract.FavoriteEntry.TABLE_NAME + " (" +
                FavoriteContract.FavoriteEntry._ID                + " INTEGER PRIMARY KEY, " +
                FavoriteContract.FavoriteEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                FavoriteContract.FavoriteEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                FavoriteContract.FavoriteEntry.COLUMN_BACKDROP_PATH + " TEXT NOT NULL, " +
                FavoriteContract.FavoriteEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                FavoriteContract.FavoriteEntry.COLUMN_VOTE_AVERAGE + " REAL NOT NULL, " +
                FavoriteContract.FavoriteEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL);";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FavoriteContract.FavoriteEntry.TABLE_NAME);
        onCreate(db);
    }
}

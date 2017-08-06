package com.example.android.popularmoviesapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmoviesapp.data.FavoriteContract;
import com.example.android.popularmoviesapp.model.Movie;
import com.example.android.popularmoviesapp.utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler, LoaderManager.LoaderCallbacks<JSONArray> {

    private RecyclerView mRecyclerView;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;
    private SharedPreferences sharedPreferences;

    private static final int MOVIE_LOADER = 100;
    private static final int FAVORITE_LOADER = 200;
    LoaderManager.LoaderCallbacks<Cursor> cursorLoaderCallback;

    private static final String MOVIE_DATA= "movie_data";
    
    public static final String POPULAR = "popular";
    public static final String TOP_RATED = "top_rated";
    public static final String FAVORITES = "favorites";
    private MovieAdapter movieAdapter;

    String sort_by;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupSharedPreference();

        mRecyclerView = findViewById(R.id.rv_movies);
        mErrorMessageDisplay = findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);

        int numberOfColumns =
                (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) ?
                        3 : 5;
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        mRecyclerView.setHasFixedSize(true);
        movieAdapter = new MovieAdapter(MainActivity.this);
        mRecyclerView.setAdapter(movieAdapter);
        sort_by = sharedPreferences.getString(getString(R.string.sort_key),POPULAR);

        loadMovieData(sort_by);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(MOVIE_DATA, (ArrayList<Movie>) movieAdapter.getDataset());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        ArrayList<Movie> movies = savedInstanceState.getParcelableArrayList(MOVIE_DATA);
        movieAdapter.setDataset(movies);
    }

    private void loadMovieData(String sort_by) {
        LoaderManager loaderManager = getSupportLoaderManager();
        if(!sort_by.equals(FAVORITES)) {
            Loader<JSONArray> movieLoader = loaderManager.getLoader(MOVIE_LOADER);

            URL url = NetworkUtils.buildMovieUrl(sort_by);
            Bundle queryBundle = new Bundle();
            queryBundle.putString(NetworkUtils.URL_EXTRA, url.toString());

            if (movieLoader == null) loaderManager.initLoader(MOVIE_LOADER, queryBundle, this);
            else loaderManager.restartLoader(MOVIE_LOADER, queryBundle, this);
        } else {
            Loader<Cursor> movieLoader = loaderManager.getLoader(FAVORITE_LOADER);
            if (cursorLoaderCallback == null) {
                cursorLoaderCallback = new LoaderManager.LoaderCallbacks<Cursor>() {
                    @Override
                    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                        return new CursorLoader(getApplicationContext(),
                                FavoriteContract.FavoriteEntry.CONTENT_URI,
                                null,
                                null,
                                null,
                                FavoriteContract.FavoriteEntry._ID);
                    }

                    @Override
                    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                        if(sharedPreferences.getString(getString(R.string.sort_key),POPULAR)
                                .equals(FAVORITES)) {
                            List<Movie> movieList = new ArrayList<>();
                            while (data.moveToNext()) {
                                movieList.add(new Movie(data));
                            }
                            if (movieList.size() != 0) {
                                showMovieData();
                                movieAdapter.setDataset(movieList);
                            } else {
                                showErrorMessage();
                            }
                        }
                    }

                    @Override
                    public void onLoaderReset(Loader<Cursor> loader) {

                    }
                };
            }
            if (movieLoader == null) loaderManager.initLoader(FAVORITE_LOADER, null, cursorLoaderCallback);
            else loaderManager.restartLoader(FAVORITE_LOADER, null, cursorLoaderCallback);
        }
        mLoadingIndicator.setVisibility(View.VISIBLE);
        mRecyclerView.getLayoutManager().scrollToPosition(0);


        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.sort_key), sort_by);
        editor.apply();
    }

    private void setupSharedPreference() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sort_by, menu);
        int _menu = sort_by.equals(POPULAR) ? R.id.action_sort_by_popularity:
                sort_by.equals(TOP_RATED) ? R.id.action_sort_by_rating : R.id.action_favorites ;
        MenuItem menuItem = menu.findItem(_menu);
        menuItem.setChecked(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_sort_by_popularity:
                item.setChecked(!item.isChecked());
                loadMovieData(POPULAR);
                return true;
            case R.id.action_sort_by_rating:
                item.setChecked(!item.isChecked());
                loadMovieData(TOP_RATED);
                return true;
            case R.id.action_favorites:
                item.setChecked(!item.isChecked());
                loadMovieData(FAVORITES);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    private void showMovieData() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCLick(Movie movie) {
        Intent intent = new Intent(getBaseContext(), DetailsActivity.class);
        intent.putExtra(DetailsActivity.DETAIL_MOVIE, movie);
        startActivity(intent);
    }

    @Override
    public Loader<JSONArray> onCreateLoader(int id, final Bundle args) {
        return new TMDBJSONAsyncTaskLoader(this, args);
    }

    @Override
    public void onLoadFinished(Loader<JSONArray> loader, JSONArray movieArray) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        List<Movie> movieList = null;
        if (movieArray != null) {
            try {
                movieList = new ArrayList<>();
                for (int i = 0; i < movieArray.length(); i++) {
                    JSONObject movie = movieArray.getJSONObject(i);
                    Movie m = new Movie(movie);
                    movieList.add(m);
                }
                showMovieData();
                movieAdapter.setDataset(movieList);
            } catch (JSONException e) {
                e.printStackTrace();
                showErrorMessage();
            }
        } else {
            showErrorMessage();
        }
    }

    @Override
    public void onLoaderReset(Loader<JSONArray> loader) {

    }
}

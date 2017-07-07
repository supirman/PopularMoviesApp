package com.example.android.popularmoviesapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
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

    private static final int MOVIE_LOADER = 100;

    private MovieAdapter movieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupSharedPreference();

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_movies);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        int numberOfColumns =
                (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) ?
                        3 : 5;
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        mRecyclerView.setHasFixedSize(true);
        movieAdapter = new MovieAdapter(MainActivity.this);
        mRecyclerView.setAdapter(movieAdapter);
        String sort_by = "popular";
        loadMovieData(sort_by);
    }

    private void loadMovieData(String sort_by) {
        URL url = NetworkUtils.buildMovieUrl(sort_by);

        Bundle queryBundle = new Bundle();
        queryBundle.putString(NetworkUtils.URL_EXTRA, url.toString());

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<JSONArray> movieLoader = loaderManager.getLoader(MOVIE_LOADER);

        if (movieLoader == null) loaderManager.initLoader(MOVIE_LOADER, queryBundle, this);
        else loaderManager.restartLoader(MOVIE_LOADER, queryBundle, this);
        mLoadingIndicator.setVisibility(View.VISIBLE);
        mRecyclerView.getLayoutManager().scrollToPosition(0);
    }

    private void setupSharedPreference() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sort_by, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_sort_by_popularity:
                item.setChecked(!item.isChecked());
                loadMovieData("popular");
                return true;
            case R.id.action_sort_by_rating:
                item.setChecked(!item.isChecked());
                loadMovieData("top_rated");
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
        try {
            movieList = new ArrayList<>();
            for (int i = 0; i < movieArray.length(); i++) {
                JSONObject movie = movieArray.getJSONObject(i);
                Movie m = new Movie(movie);
                movieList.add(m);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (movieList.size()!=0) {
            showMovieData();
            movieAdapter.setDataset(movieList);
        } else {
            showErrorMessage();
        }
    }

    @Override
    public void onLoaderReset(Loader<JSONArray> loader) {

    }
}

package com.example.android.popularmoviesapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.android.popularmoviesapp.utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    TextView mMoviesTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupSharedPreference();

        mMoviesTextView = (TextView) findViewById(R.id.movies_tv);

        loadMovieData();
    }

    private void loadMovieData() {
        String sort_by = "popular";
        new FetchMoviesTask().execute(sort_by);
    }

    private void setupSharedPreference() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_settings){
            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity);
            return  true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }

    private class FetchMoviesTask extends AsyncTask<String, Void, List<String>> {

        @Override
        protected List<String> doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            URL url = NetworkUtils.buildUrl(params[0]);
            String movie_data_json = null;
            List<String> movie_list = new ArrayList<>();
            try {
                movie_data_json = NetworkUtils.getResponseFromHttpUrl(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            JSONObject movieJson ;
            try {
                movieJson = new JSONObject(movie_data_json);
                JSONArray movieArray = movieJson.getJSONArray("results");


                for(int i = 0; i < movieArray.length(); i++) {
                    JSONObject movie = movieArray.getJSONObject(i);
                    String movie_name = movie.getString("original_title");
                    movie_list.add(movie_name);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return movie_list;
        }

        @Override
        protected void onPostExecute(List<String> ls) {
            for(String s : ls)
                mMoviesTextView.append(s + "\n\n");
        }
    }
}

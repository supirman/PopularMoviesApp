package com.example.android.popularmoviesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.android.popularmoviesapp.model.Movie;
import com.example.android.popularmoviesapp.model.Review;
import com.example.android.popularmoviesapp.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<JSONArray> {

    public static final String DETAIL_MOVIE = "DETAIL_MOVIE";
    private static final int REVIEW_LOADER = 101;
    private Movie mMovie;
    @BindView(R.id.title_tv)
    TextView mTitle;
    @BindView(R.id.release_date_tv)
    TextView mReleaseDate;
    @BindView(R.id.synopsis_tv)
    TextView mSynopsis;
    @BindView(R.id.rating_tv)
    TextView mRatingText;
    @BindView(R.id.rating_bar)
    RatingBar mRating;
    @BindView(R.id.image_detail)
    ImageView mPoster;
    @BindView(R.id.review_rv)
    RecyclerView mReviewRecyclerView;

    private ReviewAdapter reviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity != null && intentThatStartedThisActivity.hasExtra(DETAIL_MOVIE)) {
            mMovie = intentThatStartedThisActivity.getExtras().getParcelable(DetailsActivity.DETAIL_MOVIE);
            ButterKnife.bind(this);
            initMainInfo();

            mReviewRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mReviewRecyclerView.setNestedScrollingEnabled(false);

            reviewAdapter = new ReviewAdapter();
            mReviewRecyclerView.setAdapter(reviewAdapter);

            loadReviewData();
        }

        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    void initMainInfo() {
        mTitle.setText(mMovie.getTitle());
        mSynopsis.setText(mMovie.getOverview());
        mRating.setRating((float) mMovie.getRating());
        mReleaseDate.setText(mMovie.getRelease_date());
        mRatingText.setText(String.format("%s/10", mMovie.getRating()));

        Picasso.with(mPoster.getContext())
                .load(NetworkUtils.getPosterURL(mMovie.getImage()))
                .into(mPoster);
    }

    void loadReviewData() {
        URL url = NetworkUtils.buildReviewUrl(mMovie.getId());

        Bundle queryBundle = new Bundle();
        queryBundle.putString(NetworkUtils.URL_EXTRA, url.toString());

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<List<JSONArray>> movieLoader = loaderManager.getLoader(REVIEW_LOADER);

        if (movieLoader == null) loaderManager.initLoader(REVIEW_LOADER, queryBundle, this);
        else loaderManager.restartLoader(REVIEW_LOADER, queryBundle, this);
    }

    @Override
    public Loader<JSONArray> onCreateLoader(int id, Bundle args) {
        return new TMDBJSONAsyncTaskLoader(this, args);
    }

    @Override
    public void onLoadFinished(Loader<JSONArray> loader, JSONArray movieArray) {
        if (loader.getId() == REVIEW_LOADER) {
            List<Review> resultList = null;
            try {
                resultList = new ArrayList<>();
                for (int i = 0; i < movieArray.length(); i++) {
                    JSONObject jsonObject = movieArray.getJSONObject(i);
                    resultList.add(new Review(jsonObject));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //noinspection ConstantConditions
            if (resultList != null && resultList.size() != 0) {
                reviewAdapter.setDataset(resultList);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<JSONArray> loader) {

    }
}

package com.example.android.popularmoviesapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmoviesapp.data.FavoriteContract;
import com.example.android.popularmoviesapp.model.Movie;
import com.example.android.popularmoviesapp.model.Review;
import com.example.android.popularmoviesapp.model.Trailer;
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

public class DetailsActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<JSONArray>, TrailerAdapter.TrailerAdapterOnClickHandler {

    public static final String DETAIL_MOVIE = "DETAIL_MOVIE";
    private static final int REVIEW_LOADER = 101;
    private static final int VIDEO_LOADER = 102;
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
    @BindView(R.id.trailer_rv)
    RecyclerView mTrailerRecyclerView;

    private ReviewAdapter reviewAdapter;
    private TrailerAdapter trailerAdapter;

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

            mTrailerRecyclerView.setLayoutManager(
                    new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            trailerAdapter = new TrailerAdapter(this);
            mTrailerRecyclerView.setAdapter(trailerAdapter);
            mTrailerRecyclerView.setHasFixedSize(true);

            loadReviewData();
            loadTrailerData();

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
        mReleaseDate.setText(mMovie.getReleaseDate());
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
        Loader<JSONArray> movieLoader = loaderManager.getLoader(REVIEW_LOADER);

        if (movieLoader == null) loaderManager.initLoader(REVIEW_LOADER, queryBundle, this);
        else loaderManager.restartLoader(REVIEW_LOADER, queryBundle, this);
    }

    private void loadTrailerData() {
        URL url = NetworkUtils.buildVideoUrl(mMovie.getId());

        Bundle queryBundle = new Bundle();
        queryBundle.putString(NetworkUtils.URL_EXTRA, url.toString());

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<JSONArray> movieLoader = loaderManager.getLoader(VIDEO_LOADER);

        if (movieLoader == null) loaderManager.initLoader(VIDEO_LOADER, queryBundle, this);
        else loaderManager.restartLoader(VIDEO_LOADER, queryBundle, this);
    }

    @Override
    public Loader<JSONArray> onCreateLoader(int id, Bundle args) {
        return new TMDBJSONAsyncTaskLoader(this, args);
    }

    private static <T> List<T> jsonToList(JSONArray jsonArray, Class<T> cl) {
        List<T> resultList = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (isValid(jsonObject, cl)) {
                    resultList.add(cl.getDeclaredConstructor(JSONObject.class)
                            .newInstance(jsonObject));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultList;
    }

    private static boolean isValid(JSONObject jsonObject, Class cl) throws JSONException {
        if (cl == Review.class) {
            return true;
        } else if (cl == Trailer.class
                && jsonObject.getString("key").length() == 11
                && jsonObject.getString("type").equals("Trailer")
                && jsonObject.getString("site").equals("YouTube")) {
            return true;
        }
        return false;
    }

    @Override
    public void onLoadFinished(Loader<JSONArray> loader, JSONArray jsonArray) {
        if (jsonArray == null) return;
        switch (loader.getId()) {
            case REVIEW_LOADER: {
                List<Review> resultList = jsonToList(jsonArray, Review.class);
                if (resultList.size() != 0) {
                    reviewAdapter.setDataset(resultList);
                }
                break;
            }
            case VIDEO_LOADER: {
                List<Trailer> resultList = jsonToList(jsonArray, Trailer.class);
                if (resultList.size() != 0) {
                    trailerAdapter.setDataset(resultList);
                }
                break;
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<JSONArray> loader) {

    }

    @Override
    public void onCLick(Trailer trailer) {
        if (trailer != null)
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.youtube.com/watch?v=" + trailer.getKey())));
    }

    public void onClickToggleFavorite(View view) {
        Uri uri = getContentResolver().insert(FavoriteContract.FavoriteEntry.CONTENT_URI,
                mMovie.toContentValues());
        if(uri != null) {
            Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_LONG).show();
        }
    }
}

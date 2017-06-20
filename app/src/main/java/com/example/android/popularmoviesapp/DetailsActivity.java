package com.example.android.popularmoviesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.android.popularmoviesapp.model.Movie;
import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {

    public static final String DETAIL_MOVIE = "DETAIL_MOVIE";
    private Movie mMovie;
    private TextView mTitle;
    private TextView mReleaseDate;
    private TextView mSynopsis;
    private TextView mRatingText;
    private RatingBar mRating;
    private ImageView mPoster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        setTitle("Movie Detail");
        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity != null && intentThatStartedThisActivity.hasExtra(DETAIL_MOVIE)) {
            mMovie = intentThatStartedThisActivity.getExtras().getParcelable(DetailsActivity.DETAIL_MOVIE);
            mTitle = (TextView) findViewById(R.id.title_tv);
            mSynopsis = (TextView) findViewById(R.id.synopsis_tv);
            mRating = (RatingBar) findViewById(R.id.rating_bar);
            mPoster = (ImageView) findViewById(R.id.image_detail);
            mReleaseDate = (TextView) findViewById(R.id.release_date_tv);
            mRatingText = (TextView) findViewById(R.id.rating_tv);
            mTitle.setText(mMovie.getTitle());
            mSynopsis.setText(mMovie.getOverview());
            mRating.setRating((float)mMovie.getRating());
            mReleaseDate.setText(mMovie.getRelease_date());
            mRatingText.setText(mMovie.getRating()+"/10");
            String base_url = "http://image.tmdb.org/t/p/";
            String grid_image= "w342";
            Picasso.with(mPoster.getContext()).load(base_url + grid_image + mMovie.getImage()).into(mPoster);
        }

        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
}

package com.example.android.popularmoviesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.android.popularmoviesapp.model.Movie;
import com.example.android.popularmoviesapp.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity {

    public static final String DETAIL_MOVIE = "DETAIL_MOVIE";
    private Movie mMovie;
    @BindView(R.id.title_tv) TextView mTitle;
    @BindView(R.id.release_date_tv) TextView mReleaseDate;
    @BindView(R.id.synopsis_tv) TextView mSynopsis;
    @BindView(R.id.rating_tv) TextView mRatingText;
    @BindView(R.id.rating_bar) RatingBar mRating;
    @BindView(R.id.image_detail) ImageView mPoster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity != null && intentThatStartedThisActivity.hasExtra(DETAIL_MOVIE)) {
            mMovie = intentThatStartedThisActivity.getExtras().getParcelable(DetailsActivity.DETAIL_MOVIE);
            ButterKnife.bind(this);
            mTitle.setText(mMovie.getTitle());
            mSynopsis.setText(mMovie.getOverview());
            mRating.setRating((float)mMovie.getRating());
            mReleaseDate.setText(mMovie.getRelease_date());
            mRatingText.setText(String.format("%s/10",mMovie.getRating()));

            Picasso.with(mPoster.getContext())
                    .load(NetworkUtils.getPosterURL(mMovie.getImage()))
                    .into(mPoster);
        }

        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
}

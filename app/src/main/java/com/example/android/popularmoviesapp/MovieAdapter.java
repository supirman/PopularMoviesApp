package com.example.android.popularmoviesapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmoviesapp.model.Movie;
import com.example.android.popularmoviesapp.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by firman on 19/06/17.
 * MovieAdapter class
 */

class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.PosterViewHolder> {
    private List<Movie> mDataset;
    private final MovieAdapterOnClickHandler mClickHandler;
    MovieAdapter(MovieAdapterOnClickHandler clickHandler){
        mClickHandler = clickHandler;
    }

    @Override
    public PosterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_list_item, parent, false);
        return new PosterViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PosterViewHolder holder, int position) {
        holder.setMovie(mDataset.get(position));
    }

    @Override
    public int getItemCount() {
        if (mDataset == null) return 0;
        return mDataset.size();
    }

    void setDataset(List<Movie> mDataset) {
        this.mDataset = mDataset;
        notifyDataSetChanged();
    }

    List<Movie> getDataset(){
        return mDataset;
    }

    interface MovieAdapterOnClickHandler{
        void onCLick(Movie movie);
    }

    class PosterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.movie_grid_name) TextView mTextView;
        @BindView(R.id.movie_grid_image) ImageView mPosterImage;

        Movie mMovie;

        PosterViewHolder(View v) {
            super(v);
            ButterKnife.bind(this,v);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mClickHandler.onCLick(mMovie);
        }

        void setMovie(Movie movie) {
            this.mMovie = movie;
            mTextView.setText(movie.getTitle());
            Picasso.with(mPosterImage.getContext())
                    .load(NetworkUtils.getPosterURL(movie.getImage()))
                    .into(mPosterImage);
        }
    }
}

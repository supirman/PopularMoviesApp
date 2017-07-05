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

/**
 * Created by firman on 19/06/17.
 * MovieAdapter class
 */

class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private List<Movie> mDataset;
    private final MovieAdapterOnClickHandler mClickHandler;
    MovieAdapter(MovieAdapterOnClickHandler clickHandler){
        mClickHandler = clickHandler;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Movie movie = mDataset.get(position);
        holder.setMovie(movie);
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

    interface MovieAdapterOnClickHandler{
        void onCLick(Movie movie);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mTextView;
        ImageView mPosterImage;

        Movie mMovie;

        ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.movie_grid_name);
            mPosterImage = (ImageView) v.findViewById(R.id.movie_grid_image);
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

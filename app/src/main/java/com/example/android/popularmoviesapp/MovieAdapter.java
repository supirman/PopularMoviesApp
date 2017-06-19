package com.example.android.popularmoviesapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmoviesapp.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by firman on 19/06/17.
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
    public void onBindViewHolder(final ViewHolder holder, int position) {
        String base_url = "http://image.tmdb.org/t/p/";
        String grid_image= "w185";
        Movie movie = mDataset.get(position);
        final String imageUrl = base_url + grid_image + movie.getImage();
        holder.mTextView.setText(movie.getTitle());
        holder.mPosterImage.post(new Runnable() {
            @Override
            public void run() {
                Picasso.with(holder.mPosterImage.getContext()).load(imageUrl).resize(holder.mPosterImage.getWidth(),holder.mPosterImage.getHeight()).centerCrop().into(holder.mPosterImage);
            }
        });

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
        void onCLick(String movie_name);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        TextView mTextView;
        ImageView mPosterImage;
        ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.movie_grid_name);
            mPosterImage = (ImageView) v.findViewById(R.id.movie_grid_image);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Movie movie = mDataset.get(adapterPosition);
            mClickHandler.onCLick(movie.getTitle());
        }
    }
}

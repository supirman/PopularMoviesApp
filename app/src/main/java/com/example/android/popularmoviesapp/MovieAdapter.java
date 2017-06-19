package com.example.android.popularmoviesapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by firman on 19/06/17.
 */

class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private List<String> mDataset;
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
        String movieName = mDataset.get(position);
        holder.mTextView.setText(movieName);
    }


    @Override
    public int getItemCount() {
        if (mDataset == null) return 0;
        return mDataset.size();
    }

    void setDataset(List<String> mDataset) {
        this.mDataset = mDataset;
        notifyDataSetChanged();
    }

    interface MovieAdapterOnClickHandler{
        void onCLick(String movie_name);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        TextView mTextView;
        ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.movie_grid_name);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            String movie_name = mDataset.get(adapterPosition);
            mClickHandler.onCLick(movie_name);
        }
    }
}

package com.example.android.popularmoviesapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmoviesapp.model.Trailer;
import com.example.android.popularmoviesapp.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by firman on 08/07/17
 * Trailer RecycleView Adapter
 */

class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {
    private List<Trailer> dataset;
    private final TrailerAdapterOnClickHandler mClickHandler;

    TrailerAdapter(TrailerAdapterOnClickHandler clickHandler) {
        this.mClickHandler = clickHandler;
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trailer_list_item, parent, false);
        return new TrailerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        Trailer trailer = dataset.get(position);
        holder.setTrailer(trailer);
    }

    @Override
    public int getItemCount() {
        if(dataset == null) return 0;
        return dataset.size();
    }

    void setDataset(List<Trailer> dataset) {
        this.dataset = dataset;
        notifyDataSetChanged();
    }

    interface TrailerAdapterOnClickHandler{
        void onCLick(Trailer trailer);
    }

    class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.MediaPreview)
        ImageView trailerThumbnail_iv;
        @BindView(R.id.VideoPreviewPlayButton)
        ImageView trailerPlayButton_iv;
        View itemView;
        Trailer data;

        TrailerViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        void setTrailer(Trailer trailer) {
            if(trailer.getKey().length() == 11) {
                data = trailer;
                Picasso.with(trailerThumbnail_iv.getContext())
                        .load(NetworkUtils.YTThumbnailBuilder(trailer.getKey()))
                        .placeholder(R.drawable.no_thumbnail)
                        .into(trailerThumbnail_iv);
            } else {
                this.itemView.setVisibility(View.GONE);
                trailerThumbnail_iv.setVisibility(View.GONE);
                this.trailerPlayButton_iv.setVisibility(View.GONE);
            }
        }

        @Override
        public void onClick(View v) {
            mClickHandler.onCLick(data);
        }
    }
}

package com.example.android.popularmoviesapp;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmoviesapp.model.Review;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by firman on 07/07/17.
 * ReviewAdapter
 */

class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    private List<Review> reviewList;

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_list_item, parent, false);
        return new ReviewViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        Review review = reviewList.get(position);
        holder.setReview(review);
    }

    @Override
    public int getItemCount() {
        if (reviewList == null) return 0;
        return reviewList.size();
    }

    void setDataset(List<Review> dataset) {
        this.reviewList = dataset;
        notifyDataSetChanged();
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.review_author_tv)
        TextView mReviewAuthor_tv;
        @BindView(R.id.review_content_tv)
        TextView mReviewContent_tv;

        ReviewViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void setReview(Review review) {
            this.mReviewAuthor_tv.setText(review.getAuthor());
            this.mReviewContent_tv.setText(fromHtml(review.getContent()));
        }
    }

    @SuppressWarnings("deprecation")
    private static Spanned fromHtml(String html) {
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }
}

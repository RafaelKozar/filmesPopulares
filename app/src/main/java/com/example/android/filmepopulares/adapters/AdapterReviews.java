package com.example.android.filmepopulares.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.filmepopulares.R;
import com.example.android.filmepopulares.model.Review;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rako on 22/03/2017.
 */
public class AdapterReviews extends RecyclerView.Adapter<AdapterReviews.ReviewAdapterViewHolder>{
    private List<Review> reviews = new ArrayList<Review>();

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
        notifyDataSetChanged();
    }


    @Override
    public ReviewAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_review, parent, false);
        return new ReviewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewAdapterViewHolder holder, int position) {
        if(position == (reviews.size() - 1)){
            holder.linha.setVisibility(View.GONE);
        }
        Review review =  reviews.get(position);
        holder.coment.setText(Html.fromHtml(review.getComent()));
        holder.autor.setText(review.getAutor());
    }


    class ReviewAdapterViewHolder extends RecyclerView.ViewHolder{
        TextView autor;
        TextView coment;
        View linha;


        public ReviewAdapterViewHolder(View itemView) {
            super(itemView);
            autor = (TextView) itemView.findViewById(R.id.autor_review);
            coment = (TextView) itemView.findViewById(R.id.comentario_review);
            linha = (View) itemView.findViewById(R.id.linha_review);
        }
    }
    public int getItemCount() {
        if(reviews != null){
            return reviews.size();
        }
        else {
            return 0;
        }
    }
}

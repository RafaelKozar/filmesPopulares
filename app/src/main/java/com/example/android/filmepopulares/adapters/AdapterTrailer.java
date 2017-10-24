package com.example.android.filmepopulares.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.filmepopulares.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rako on 22/03/2017.
 */
public class AdapterTrailer extends RecyclerView.Adapter<AdapterTrailer.TrailerAdapterViewHolder>{
    private List<String> urlTrailers = new ArrayList<String>();

    private final clickTrailer listennerClick;

    public AdapterTrailer(clickTrailer listennerClick) {
        this.listennerClick = listennerClick;
    }

    public void setUrlTrailers(List<String> urlTrailers) {
        this.urlTrailers = urlTrailers;
        notifyDataSetChanged();
    }

    public interface clickTrailer{
        void onClickTrailer(int position);
    }

    @Override
    public TrailerAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_trailer, parent, false);
        return new TrailerAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerAdapterViewHolder holder, int position) {
        position += 1;
        holder.numTrailler = "Trailer "+String.valueOf(position);
        Log.i("t", holder.numTrailler);
        holder.trailer.setText(holder.numTrailler);
    }


    class TrailerAdapterViewHolder extends RecyclerView.ViewHolder{
        TextView trailer;
        String numTrailler;

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listennerClick.onClickTrailer(getAdapterPosition());
            }
        };

        public TrailerAdapterViewHolder(View itemView) {
            super(itemView);
            trailer = (TextView) itemView.findViewById(R.id.trailer_text);
            itemView.setOnClickListener(listener);
        }
    }
    public int getItemCount() {
        if(urlTrailers != null){
            return urlTrailers.size();
        }
        else {
            return 0;
        }
    }
}

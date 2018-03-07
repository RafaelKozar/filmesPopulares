package com.example.android.filmepopulares.adapters;

import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.filmepopulares.R;
import com.example.android.filmepopulares.interfaces.CardAdapter;
import com.example.android.filmepopulares.model.CardItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rako on 07/03/2018.
 */

public class CardPagerAdapter extends PagerAdapter implements CardAdapter{
    private List<CardView> mViews;
    private List<CardItem> mData;
    private float mBaseElevation;

    public CardPagerAdapter() {
        mData = new ArrayList<>();
        mViews = new ArrayList<>();
    }

    public void addCardItem(CardItem item) {
        mViews.add(null);
        mData.add(item);
    }


    @Override
    public float getBaseElevation() {
        return mBaseElevation;
    }

    @Override
    public CardView getCardViewAt(int position) {
        return mViews.get(position);
    }

    @Override
    public int getCount() {
        return  mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext())
                .inflate(R.layout.item_filme, container, false);

        container.addView(view);
        bind(mData.get(position), view);
        CardView cardView = (CardView) view.findViewById(R.id.filme_card);

        if (mBaseElevation == 0) {
            mBaseElevation = cardView.getCardElevation();
        }

        cardView.setMaxCardElevation(mBaseElevation * MAX_ELEVATION_FACTOR);
        mViews.set(position, cardView);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        mViews.set(position, null);
    }

    private void bind(CardItem item, View view) {
        TextView titleTextView = (TextView) view.findViewById(R.id.nome_filme);
        ImageView imageView = (ImageView) view.findViewById(R.id.image_movie_item);
        ImageView btnFavoritar = (ImageView) view.findViewById(R.id.button_favoritar);

        titleTextView.setText(item.getTitle());
        imageView.setImageBitmap(item.getImgBitmap());
        if (item.isFavorito()) {
            btnFavoritar.setImageResource(R.drawable.img_favorito_green);
        } else {
            btnFavoritar.setImageResource(R.drawable.img_favorito);
        }
    }
}

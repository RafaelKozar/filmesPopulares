package com.example.android.filmepopulares.interfaces;

import android.support.v7.widget.CardView;

/**
 * Created by rako on 07/03/2018.
 */


public interface CardAdapter {
    int MAX_ELEVATION_FACTOR = 4;

    float getBaseElevation();

    CardView getCardViewAt(int position);

    int getCount();
}

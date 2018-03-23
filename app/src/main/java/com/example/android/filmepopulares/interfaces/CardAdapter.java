package com.example.android.filmepopulares.interfaces;

import android.support.v7.widget.CardView;
import android.widget.FrameLayout;

/**
 * Created by rako on 07/03/2018.
 */


public interface CardAdapter {
    int MAX_ELEVATION_FACTOR = 2;

    float getBaseElevation();

    CardView getCardViewAt(int position);

    int getCount();
}

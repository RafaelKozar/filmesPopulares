package com.example.android.filmepopulares.model;

import android.graphics.Bitmap;

/**
 * Created by rako on 07/03/2018.
 */

public class CardItem {
    private String title;
    private boolean isFavorito;
    private Bitmap imgBitmap;


    public CardItem(String title, boolean isFavorito, Bitmap img) {
        this.title = title;
        this.isFavorito = isFavorito;
        this.imgBitmap = img;
    }

    public String getTitle() {
        return title;
    }

    public boolean isFavorito() {
        return isFavorito;
    }

    public Bitmap getImgBitmap() {
        return imgBitmap;
    }

}

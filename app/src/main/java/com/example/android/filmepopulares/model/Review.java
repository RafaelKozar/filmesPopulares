package com.example.android.filmepopulares.model;

import org.json.JSONObject;

/**
 * Created by rako on 17/05/2017.
 */

public class Review {
    private String autor;
    private String coment;

    public Review(String autor, String coment){
        this.autor = autor;
        this.coment = coment;
    }

    public Review(JSONObject jsonObject){
        this.autor = jsonObject.optString("author");
        this.coment = jsonObject.optString("content");
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getComent() {
        return coment;
    }

    public void setComent(String coment) {
        this.coment = coment;
    }
}

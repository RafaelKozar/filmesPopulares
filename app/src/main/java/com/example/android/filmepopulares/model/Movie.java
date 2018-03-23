package com.example.android.filmepopulares.model;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.animation.PathInterpolator;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rako on 21/03/2017.
 */
public class Movie implements Parcelable {

    public static final String PARCELABLE_KEY = "movie";
    private String title;
    private String releaseDate;
    private String year;
    private String overview;
    private String average;
    private String urlImage;
    private ImageView imageView;
    private Bitmap bitmap;
    private int idFilme;

    public Movie(String title, String releaseDate, String year, String overview,
                 String average, String urlImage, int idFilme){
        this.title = title;
        this.releaseDate = releaseDate;
        this.year = year;
        this.overview = overview;
        this.average = average;
        this.urlImage = urlImage;
        this.idFilme = idFilme;
    }

    public Movie(JSONObject filme) throws Exception{
        this.title = filme.optString("title");
        if (filme.optString("release_date") != null && !"".contains(filme.optString("release_date"))) {
            String[] data = filme.optString("release_date").split("-");
            this.releaseDate = data[2] + "/" + data[1] + "/" + data[0];
            this.year = data[0];
        }

        this.overview = filme.optString("overview");
        this.average = filme.optString("vote_average");
        this.idFilme =  filme.optInt("id");
        this.urlImage = filme.optString("poster_path");
    }

    public Movie(Parcel parcel) {
        this.title = parcel.readString();
        this.releaseDate = parcel.readString();
        this.year = parcel.readString();
        this.overview = parcel.readString();
        this.average = parcel.readString();
        this.urlImage = parcel.readString();
        this.idFilme = parcel.readInt();
    }

    public Movie (){};


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getAverage() {
        return average;
    }

    public void setAverage(String average) {
        this.average = average;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }


    public int getIdFilme() {
        return idFilme;
    }

    public void setIdFilme(int idFilme) {
        this.idFilme = idFilme;
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>(){

        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int i) {
            return new Movie[0];
        }
    };

    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(releaseDate);
        parcel.writeString(year);
        parcel.writeString(overview);
        parcel.writeString(average);
        parcel.writeString(urlImage);
        parcel.writeInt(idFilme);
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}

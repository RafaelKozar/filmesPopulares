package com.example.android.filmepopulares.recursos;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;

import com.example.android.filmepopulares.R;
import com.example.android.filmepopulares.activitys.MainActivity;
import com.example.android.filmepopulares.connection.NetworkConnection;
import com.example.android.filmepopulares.model.Movie;
import com.example.android.filmepopulares.model.Review;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rako on 22/03/2017.
 */
public class FeedProcessor{

    public static List<Movie> process(JSONObject json) throws Exception {
        List<Movie> resultsFilmes = new ArrayList<>();
        if (json != null && !json.isNull("results")) {
            try {
                JSONArray results = json.getJSONArray("results");

                if (results.length() > 0) {
                    for (int i = 0; results.length() > i; i++) {
                        JSONObject jsonObject = results.getJSONObject(i);
                        Movie filme = new Movie(jsonObject);
                                //.encodedQuery(filme.getUrlImage()).build();
                                //.appendEncodedPath(filme.getUrlImage()).build();
                        /***
                         * Tentei usar o Picasso aqui, porém dava erro na momento da execução,
                         * outra forma seria usar o carregamento das imagens com o picasso dentro do adapter de filmes,
                         * porém ele demorava muito para para mostrar as imagens, desse modo, ele carregava
                         * a recycler sem as imagens do filme (apenas mostrava o nome do filme)
                         * e assim a experiencia do usuário seria horrível, pois as imagens demoravam
                         * para aparecer, portanto decidi
                         * carregar as imagens aqui, mas é claro estou aberto para sugestões
                         *  :)
                        ***/
                        if(filme.getUrlImage() != null && !"".contains(filme.getUrlImage())) {
                            Uri uriImage = Uri.parse("http://image.tmdb.org").buildUpon()
                                    .path("/t/p/w780" + filme.getUrlImage()).build();
                            URL urlImage = new URL(uriImage.toString());
                            Bitmap bitmap = NetworkConnection.getBitmapFromURL(urlImage);
                            filme.setBitmap(bitmap);
                        }
                        resultsFilmes.add(filme);
                    }
                    return resultsFilmes;
                } else {
                    return null;
                }
            } catch (JSONException e) {
                //e.printStackTrace();
                Log.e("RetornoProcess", e.getMessage(), e);
                return resultsFilmes;
            }
        } else {
            return null;
        }
    }

    public static List<Movie> setImages(List<Movie> filmes) throws Exception {
        List<Movie> filmesWithImage = new ArrayList<>();
        for (Movie filme: filmes) {
            Uri uriImage = Uri.parse("http://image.tmdb.org").buildUpon()
                    .path("/t/p/w780"+filme.getUrlImage()).build();

            URL urlImage = new URL(uriImage.toString());
            Bitmap bitmap = NetworkConnection.getBitmapFromURL(urlImage);
            filme.setBitmap(bitmap);
            filmesWithImage.add(filme);
        }
        return filmes;
    }

    public static List<String> processTrailers(JSONObject jsonObject) {
        JSONArray trailers = jsonObject.optJSONArray("results");
        List<String> resultTrailers = new ArrayList<>();
        if (trailers != null) {
            for (int i = 0; i < trailers.length(); i++) {
                JSONObject objectTrailer = (JSONObject) trailers.opt(i);
                resultTrailers.add(objectTrailer.optString("key"));

            }
            return resultTrailers;
        } else {
            return null;
        }
    }

    public static List<Review> processReviews(JSONObject jsonObject) {
        JSONArray reviews = jsonObject.optJSONArray("results");
        List<Review> resultReviews = new ArrayList<>();
        if (reviews != null) {
            for (int i = 0; i < reviews.length(); i++) {
                Review review = new Review(reviews.optJSONObject(i));
                resultReviews.add(review);
            }
            return resultReviews;
        } else {
            return null;
        }
    }

    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

}

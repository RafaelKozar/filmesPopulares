package com.example.android.filmepopulares.services;


import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.content.Context;

import android.os.Bundle;
import android.util.Log;

import com.example.android.filmepopulares.R;
import com.example.android.filmepopulares.dao.MovieDAO;
import com.example.android.filmepopulares.interfaces.AsyncTaskDelegate;
import com.example.android.filmepopulares.connection.NetworkConnection;
import com.example.android.filmepopulares.model.Movie;
import com.example.android.filmepopulares.recursos.FeedProcessor;

import org.json.JSONObject;

import java.net.URL;
import java.util.List;

import static com.example.android.filmepopulares.activitys.MainActivity.LOADER_MOVIES_ID_FAVORITOS;

/**
 * Created by rako on 21/03/2017.
 */
public class MovieService implements LoaderManager.LoaderCallbacks<List<Movie>>{

    public static final String POPULAR = "https://api.themoviedb.org/3/movie/popular";
    public static final String TOP_RATED = "https://api.themoviedb.org/3/movie/top_rated";
    private Context context;
    private LoaderManager loaderManager;
    private AsyncTaskDelegate delegate;
    private Bundle bundle;
    private int movies_id = 20;


    public MovieService(Context applicationContext, android.support.v4.app.LoaderManager supportLoaderManager, Bundle bundle, int id) {
        this.context = applicationContext;
        this.delegate = (AsyncTaskDelegate) applicationContext;
        this.loaderManager = supportLoaderManager;
        this.bundle = bundle;
        this.movies_id = id;
        loaderManager.initLoader(movies_id, bundle, this);
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(final int id, final Bundle args) {
        return new AsyncTaskLoader<List<Movie>>(context) {
            @Override
            protected void onStartLoading() {
                if(args ==  null && id != LOADER_MOVIES_ID_FAVORITOS){
                    return;
                }else{
                    forceLoad();
                }
            }

            @Override
            public List<Movie> loadInBackground() {
                if (id == LOADER_MOVIES_ID_FAVORITOS) {
                    MovieDAO movieDAO = new MovieDAO(context);
                    try {
                        return FeedProcessor.setImages(movieDAO.listar());
                    } catch (Exception e) {
                        e.printStackTrace();
                        return  null;
                    }
                } else {
                    String urlString = args.getString("url");
                    Uri buildUri = Uri.parse(urlString).buildUpon()
                            .appendQueryParameter("api_key", context.getString(R.string.KEY_API))
                            .appendQueryParameter("language", "pt-BR")
                            .build();
                    try {
                        URL url = new URL(buildUri.toString());
                        JSONObject jsonObject = NetworkConnection.getResponseFromHttpUrl(url);
                        List<Movie> filmes = FeedProcessor.process(jsonObject);
                        return filmes;
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("RetornoDoInBackground", e.getMessage(), e);
                        return null;
                    }
                }
            }
        };
    }


    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {
        delegate.processFinish(data);
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {

    }

}

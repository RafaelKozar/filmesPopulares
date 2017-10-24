package com.example.android.filmepopulares.services;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import com.example.android.filmepopulares.connection.NetworkConnection;
import com.example.android.filmepopulares.interfaces.AsyncTaskDelegateTrailer;
import com.example.android.filmepopulares.recursos.FeedProcessor;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * Created by rako on 22/03/2017.
 */
public class TrailerService implements LoaderManager.LoaderCallbacks<List<String>>{
    private AsyncTaskDelegateTrailer delegateTrailer;

    private Context context;
    private LoaderManager loaderManager;
    private Bundle bundle;
    private static final int LOADER_MOVIES_ID = 21;


    public TrailerService(Context applicationContext, android.support.v4.app.LoaderManager supportLoaderManager, Bundle bundle) {
        this.context = applicationContext;
        this.delegateTrailer = (AsyncTaskDelegateTrailer) applicationContext;
        this.loaderManager = supportLoaderManager;
        this.bundle = bundle;
        loaderManager.initLoader(LOADER_MOVIES_ID, bundle, this);
    }

    @Override
    public Loader<List<String>> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<List<String>>(context) {
            @Override
            protected void onStartLoading() {
                if(args ==  null){
                    return;
                }else{
                    forceLoad();
                }
            }

            @Override
            public List<String> loadInBackground() {
                try {
                    URL url = new URL(args.getString("urlString"));
                    JSONObject jsonObject = NetworkConnection.getResponseFromHttpUrl(url);
                    List<String> trailers =  FeedProcessor.processTrailers(jsonObject);
                    return trailers;
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("RetornoDoInBackgroundt", e.getMessage(), e);
                    return null;
                }
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<String>> loader, List<String> data) {
        delegateTrailer.processFinish(data);
    }

    @Override
    public void onLoaderReset(Loader<List<String>> loader) {

    }
}

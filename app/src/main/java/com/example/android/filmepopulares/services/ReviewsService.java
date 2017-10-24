package com.example.android.filmepopulares.services;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import com.example.android.filmepopulares.connection.NetworkConnection;
import com.example.android.filmepopulares.interfaces.AsyncTaskDelegateReview;
import com.example.android.filmepopulares.model.Review;
import com.example.android.filmepopulares.recursos.FeedProcessor;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * Created by rako on 22/03/2017.
 */
public class ReviewsService implements LoaderManager.LoaderCallbacks<List<Review>>{
    private AsyncTaskDelegateReview delegateReviews;

    private Context context;
    private LoaderManager loaderManager;
    private Bundle bundle;
    private static final int LOADER_MOVIES_ID = 22;


    public ReviewsService(Context applicationContext, LoaderManager supportLoaderManager, Bundle bundle) {
        this.context = applicationContext;
        this.delegateReviews = (AsyncTaskDelegateReview) applicationContext;
        this.loaderManager = supportLoaderManager;
        this.bundle = bundle;
        loaderManager.initLoader(LOADER_MOVIES_ID, bundle, this);
    }


    @Override
    public Loader<List<Review>> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<List<Review>>(context) {
            @Override
            protected void onStartLoading() {
                if(args ==  null){
                    return;
                }else{
                    forceLoad();
                }
            }

            @Override
            public List<Review> loadInBackground() {
                try {
                    URL url = new URL(args.getString("urlString"));
                    JSONObject jsonObject = NetworkConnection.getResponseFromHttpUrl(url);
                    List<Review> reviews =  FeedProcessor.processReviews(jsonObject);
                    return reviews;
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("RetornoDoInBackgroundt", e.getMessage(), e);
                    return null;
                }
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<Review>> loader, List<Review> data) {
        delegateReviews.processFinishedReview(data);
    }

    @Override
    public void onLoaderReset(Loader<List<Review>> loader) {

    }
}

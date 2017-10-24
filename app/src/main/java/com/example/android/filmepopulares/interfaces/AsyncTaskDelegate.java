package com.example.android.filmepopulares.interfaces;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;

import com.example.android.filmepopulares.model.Movie;

import java.util.List;

/**
 * Created by rako on 21/03/2017.
 */
public interface AsyncTaskDelegate  {
     void processFinish(List<Movie> filmes);

}

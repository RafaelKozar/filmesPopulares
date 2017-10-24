package com.example.android.filmepopulares.interfaces;

import com.example.android.filmepopulares.model.Movie;
import com.example.android.filmepopulares.model.Review;

import java.util.List;

/**
 * Created by rako on 21/03/2017.
 */
public interface AsyncTaskDelegateReview {
     void processFinishedReview(List<Review> reviews);

}

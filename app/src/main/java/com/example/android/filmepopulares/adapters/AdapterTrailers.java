package com.example.android.filmepopulares.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.android.filmepopulares.R;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by rako on 16/03/2017.
 */
public class AdapterTrailers extends BaseAdapter {
    private JSONArray trailers;
    private Activity activity;

    public AdapterTrailers(JSONArray trailers, Activity activity){
        this.trailers = trailers;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return trailers.length();
    }

    @Override
    public Object getItem(int i) {
        try {
            return trailers.get(i);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View mView = activity.getLayoutInflater()
                .inflate(R.layout.item_trailer, viewGroup, false);
        TextView tr = (TextView) mView.findViewById(R.id.trailer_text);
        tr.setText("Trailer "+(i+1));
        return mView;
    }
}

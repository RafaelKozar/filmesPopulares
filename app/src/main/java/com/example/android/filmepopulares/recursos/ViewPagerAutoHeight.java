package com.example.android.filmepopulares.recursos;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

/**
 * Created by rako on 08/03/2018.
 */

public class ViewPagerAutoHeight extends ViewPager {
    private int mCurrentPosition = 0;

    public ViewPagerAutoHeight(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightMeaseure = heightMeasureSpec;
        try {
            View child = getChildAt(mCurrentPosition);
            if (child != null) {
                child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0,
                        MeasureSpec.UNSPECIFIED));
                float px = FeedProcessor.convertDpToPixel(1, getContext());
                int h = (int) (child.getMeasuredHeight() + px);
                heightMeaseure = MeasureSpec.makeMeasureSpec(h, MeasureSpec.UNSPECIFIED);
            }
        } catch (Exception e) {
            Log.e("Error - ViewPagerAutoHeight", e.toString());
        }
        super.onMeasure(widthMeasureSpec, heightMeaseure);
    }
}

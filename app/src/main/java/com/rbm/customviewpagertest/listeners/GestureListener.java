package com.rbm.customviewpagertest.listeners;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Doonamis
 * Created by Ramon on 1/3/17.
 */

public class GestureListener extends GestureDetector.SimpleOnGestureListener {

    public interface GestureEventListener{
        void onGoRight(float distance);
        void onGoLeft(float distance);
    }

    private GestureEventListener mCallback;

    private static final float SWIPE_MIN_DISTANCE = 1;
    private static final float SWIPE_THRESHOLD_VELOCITY = 500;

    public GestureListener(GestureEventListener mCallback) {
        this.mCallback = mCallback;
    }


    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
            Log.d("GESTURE","Right to left");
            if(mCallback!=null)
                mCallback.onGoRight(e1.getX() - e2.getX());
            return true; // Right to left
        }  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
            Log.d("GESTURE"," Left to right");
            if(mCallback!=null)
                mCallback.onGoLeft(e2.getX() - e1.getX());
            return true; // Left to right
        }

//        if(e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
//            Log.d("GESTURE","Bottom to top");
//            return false; // Bottom to top
//        }  else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
//            Log.d("GESTURE","Top to bottom");
//            return false; // Top to bottom
//        }
        return false;
    }

}

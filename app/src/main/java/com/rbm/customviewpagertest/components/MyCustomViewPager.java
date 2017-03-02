package com.rbm.customviewpagertest.components;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rbm.customviewpagertest.listeners.GestureListener;

import java.util.ArrayList;

/**
 * Doonamis
 * Created by Ramon on 1/3/17.
 */

public class MyCustomViewPager extends LinearLayout implements GestureListener.GestureEventListener {

    private ArrayList<String> mData;
    private ArrayList<ViewGroup> mScreens;
    private static final int MAX_SCREENS = 3;
    private Context mContext;
    private int mScreenWidth;

    public MyCustomViewPager(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public MyCustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    public MyCustomViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    private void init() {
        setUpLayoutParams();
        mData = new ArrayList<>();
        setUpGestureListner();
    }

    private void setUpLayoutParams() {
        int screenWidth = getScreenWidth();
        mScreenWidth = screenWidth;
        int maxWidth = screenWidth * MAX_SCREENS;
        setOrientation(LinearLayout.HORIZONTAL);
        LayoutParams layoutParams = new LayoutParams(maxWidth, ViewGroup.LayoutParams.MATCH_PARENT);
        setLayoutParams(layoutParams);
    }

    private int getScreenWidth() {
        WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }


    public int yDelta;
    public int xDelta;

    private void setUpGestureListner() {
        final GestureDetector gestureDetector = new GestureDetector(mContext, new GestureListener(this));
        setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                final int X = (int) event.getRawX();
                final int Y = (int) event.getRawY();
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                        xDelta = X - lParams.leftMargin;
//                        yDelta = Y - lParams.topMargin;
                        break;
                    case MotionEvent.ACTION_UP:
                        checkPagePosition();
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        break;
                    case MotionEvent.ACTION_POINTER_UP:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                        layoutParams.leftMargin = X - xDelta;

                        // layoutParams.topMargin = Y - yDelta;
//                        layoutParams.rightMargin = 250;
//                        layoutParams.bottomMargin = 250;
                        view.setLayoutParams(layoutParams);
                        break;
                }
                invalidate();

                gestureDetector.onTouchEvent(event);
                return true;
            }


        });
    }

    public void setData(ArrayList<String> data) {
        mData = data;
        mScreens = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
            TextView textView = new TextView(mContext);
            textView.setText(data.get(i));
            ViewGroup screen = getScreen(i);
            mScreens.add(screen);
            screen.addView(textView);
            addView(screen);
        }

        setCurrentItem(1, Direction.IDLE);

    }

    private ViewGroup getScreen(int i) {
        ViewGroup view = new RelativeLayout(mContext);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(mScreenWidth, ViewGroup.LayoutParams.MATCH_PARENT);

        view.setLayoutParams(params);

        switch (i) {
            case 0:
                view.setBackgroundColor(Color.RED);
                break;
            case 1:
                view.setBackgroundColor(Color.BLUE);
                break;
            case 2:
                view.setBackgroundColor(Color.GREEN);
                break;
        }
        return view;
    }


    //    GESTURE CALLBACKS
    @Override
    public void onGoRight(float v) {
        setCurrentItem(mCurrentPage + 1, Direction.RIGHT);
    }

    @Override
    public void onGoLeft(float v) {
        setCurrentItem(mCurrentPage - 1, Direction.LEFT);
    }


    private void checkScroll(float x) {

    }


    private int mCurrentPage;

    private void checkPagePosition() {
        Rect rect = new Rect();
        getLocalVisibleRect(rect);
        int checkPointLeft = mCurrentPage * mScreenWidth;
        int checkpointRight;
        if (mCurrentPage != 0) {
            checkpointRight = checkPointLeft + mScreenWidth;
        } else {
            checkpointRight = mScreenWidth;
        }

        if (rect.contains(checkPointLeft, 0)) {//GOING TO LEFT SCREEN
            if ((checkPointLeft - rect.left) > (rect.right - checkPointLeft)) {//GO TO LEFT
                setCurrentItem(mCurrentPage - 1, Direction.LEFT);
            } else {//STAY AT CURRENT
                setCurrentItem(mCurrentPage, Direction.IDLE);
            }
        } else {//GOING TO RIGHT SCREEN
            if ((checkpointRight - rect.left) > (rect.right - checkpointRight)) {//STAY AT CURRENT
                setCurrentItem(mCurrentPage, Direction.IDLE);
            } else {//GO TO RIGHT
                setCurrentItem(mCurrentPage + 1, Direction.RIGHT);
            }
        }
    }


    private void setCurrentItem(int i, Direction direction) {
        mCurrentPage = i;
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) getLayoutParams();
        params.leftMargin = -(mScreenWidth * i);
        setLayoutParams(params);
        invalidate();

        switch (direction) {
            case LEFT:

                ViewGroup lastScreen = mScreens.get(2);
                mScreens.remove(lastScreen);
                mScreens.add(0, lastScreen);

                removeAllViews();
                for(ViewGroup v : mScreens){
                    addView(v);
                }
                setCurrentItem(1, Direction.IDLE);
                break;
            case RIGHT:
                ViewGroup firstScreen = mScreens.get(0);
                mScreens.remove(firstScreen);
                mScreens.add(2, firstScreen);

                removeAllViews();
                for(ViewGroup v : mScreens){
                    addView(v);
                }
                setCurrentItem(1, Direction.IDLE);
                break;
        }
    }

    private enum Direction {
        LEFT,
        RIGHT,
        IDLE
    }


}

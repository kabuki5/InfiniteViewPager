package com.rbm.customviewpagertest.components;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rbm.customviewpagertest.R;
import com.rbm.customviewpagertest.listeners.GestureListener;

import java.util.ArrayList;

/**
 * Doonamis
 * Created by Ramon on 1/3/17.
 */

public class MyCustomViewPager extends LinearLayout implements GestureListener.GestureEventListener {

    private ArrayList<ViewGroup> mScreens;
    private static final int MAX_SCREENS = 3;
    private Context mContext;
    private int mScreenWidth;

    private int monthCounter = 0;
    private int year = 2017;
    private String[] months = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};

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

                if (gestureDetector.onTouchEvent(event)) {
                    return true;
                }

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

                return true;
            }
        });
    }

    public void setData() {
        mScreens = new ArrayList<>();

        for (int i = 0; i < MAX_SCREENS; i++) {
            ViewGroup screen = getScreen(i);
            mScreens.add(screen);
            addView(screen);
        }

        setCurrentItem(1, Direction.IDLE);
    }

    private ViewGroup getScreen(int i) {
        ViewGroup view = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.pager_screen, null);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(mScreenWidth, ViewGroup.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(params);

        switch (i) {
            case 0:
                view.setBackgroundColor(Color.MAGENTA);
                break;
            case 1:
                view.setBackgroundColor(Color.BLUE);
                break;
            case 2:
                view.setBackgroundColor(Color.GRAY);
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

        final int newLeftMargin = -(mScreenWidth * i);

        switch (direction) {
            case IDLE:
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) getLayoutParams();
                params.leftMargin = newLeftMargin;
                setLayoutParams(params);
                ((TextView) mScreens.get(i).findViewById(R.id.month)).setText(months[monthCounter] + " " + year);
                break;
            case LEFT:
                monthCounter--;
                //Managing the limits and changing the year
                if (monthCounter < 0) {
                    monthCounter = months.length - 1;
                    year--;
                }
                ViewGroup lastScreen = mScreens.get(2);
                ((TextView) lastScreen.findViewById(R.id.month)).setText(months[monthCounter] + " " + year);

                mScreens.remove(lastScreen);
                mScreens.add(0, lastScreen);

                removeAllViews();
                for (ViewGroup v : mScreens) {
                    addView(v);
                }
                setCurrentItem(1, Direction.IDLE);
                break;
            case RIGHT:
                monthCounter++;
                //Managing the limits and changing the year
                if (monthCounter > 11) {
                    monthCounter = 0;
                    year++;
                }
                ViewGroup firstScreen = mScreens.get(0);
                ((TextView) firstScreen.findViewById(R.id.month)).setText(months[monthCounter] + " " + year);
                mScreens.remove(firstScreen);
                mScreens.add(2, firstScreen);

                removeAllViews();
                for (ViewGroup v : mScreens) {
                    addView(v);
                }
                setCurrentItem(1, Direction.IDLE);
               // animateTransition(-mScreenWidth);

                break;
        }



    }

    Animation mAnimation;

    private void animateTransition(final int sourcePoint) {
        if (mAnimation != null)
            mAnimation.cancel();
        mAnimation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) getLayoutParams();
                params.leftMargin = (int) (sourcePoint * interpolatedTime);
                setLayoutParams(params);
            }
        };
        mAnimation.setDuration(3000); // in ms
        startAnimation(mAnimation);
    }


    private enum Direction {
        LEFT,
        RIGHT,
        IDLE
    }


}

package com.widetech.mobile.mitaxiapp.components.ui.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;

/**
 * Created with IntelliJ IDEA.
 * User: AnderWeb
 * Date: 2/1/13
 * Time: 2:27 PM
 */
public class TouchSlideMenu extends SlideMenu {
    //Constants for the different touch states
    private static final int TOUCH_STATE_REST = 0;
    private static final int TOUCH_STATE_SLIDING = 1;

    //Field to know the current touch state
    private int mTouchState = TOUCH_STATE_REST;

    //Fields to record the touch coordinates
    private float mLastMotionX;

    //Field to hold the amount of pixels needed for a touch movement to be considered a "page scroll"
    private int mTouchSlop;

    //Fields to hold the device maximum and minimum pixel velocity for scrolling
    private int mMaximumVelocity;
    private int mMinimumVelocity;

    //This will track our touch events and gather pixel velocity from them
    private VelocityTracker mVelocityTracker = null;

    public TouchSlideMenu(Context context) {
        super(context);
        init(context);
    }

    public TouchSlideMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TouchSlideMenu(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    /**
     * Initialize the touch fields and objects for this Context
     * @param context
     */
    private void init(Context context){
        final ViewConfiguration configuration = ViewConfiguration
                .get(getContext());
        mTouchSlop = configuration.getScaledPagingTouchSlop();
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
        mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
    }

    /**
     * Change touch state if moved beyond the touch thresold
     * @param x the touch event's X coordinate
     */
    private void setStateIfNeeded(float x){
        final int xDiff = (int) Math.abs(x - mLastMotionX);

        final int touchSlop = mTouchSlop;
        boolean xMoved = xDiff > touchSlop;

        if (xMoved) {
            mTouchState = TOUCH_STATE_SLIDING;
            mLastMotionX = x;
        }
    }

    /**
     * Check if a fling gesture was delivered and react accordingly
     */
    private void computeFling() {
        mVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
        final int velocityX = (int) mVelocityTracker.getXVelocity();
        if (mTouchState == TOUCH_STATE_SLIDING) {
            if (Math.abs(velocityX) > mMinimumVelocity) {
                snap(velocityX);
            } else {
                snap(0);
            }
        }

        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = ev.getActionMasked();
        if ((action == MotionEvent.ACTION_MOVE)
                && (mTouchState != TOUCH_STATE_REST)) {
            return true;
        }
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(ev);

        final float x = ev.getX();

        switch (action) {
            case MotionEvent.ACTION_MOVE:
                setStateIfNeeded(x);
                break;
            case MotionEvent.ACTION_DOWN:
                mLastMotionX = x;
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                computeFling();
                mTouchState = TOUCH_STATE_REST;
                break;
            default:
                break;
        }
        return mTouchState != TOUCH_STATE_REST;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        final int action = ev.getActionMasked();
        final float x = ev.getX();
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(ev);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastMotionX = x;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mTouchState == TOUCH_STATE_SLIDING) {
                    int deltaX = (int) (mLastMotionX - x);
                    mLastMotionX = x;
                    slide(deltaX);
                }else{
                    setStateIfNeeded(x);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                computeFling();
                mTouchState = TOUCH_STATE_REST;
                break;
        }
        return true;
    }

    /**
     * Open/close the menu depending the gesture direction and velocity
     * @param velocity
     */
    public void snap(float velocity) {
        if (velocity < 0) {
            close();
        } else if(velocity > 0) {
            open();
        } else {
            if(getCurrentPosition() < getOpenPosition()/2){
                close();
            }else{
                open();
            }
        }
    }

    /**
     * Move the menu an specified amount of pixels
     * @param pixels
     */
    public void slide(float pixels) {
        setCurrentPosition(pixels);
    }

}

package com.example.viewsample.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


/**
 * Created by 龙泉 on 2017/2/10.
 */

public class DragArea extends View {

    private static final int MAX_PROGRESS_PER_DRAG = 20;
    private static final int MAX_VALUE = 100;
    private static final int MIN_VALUE = 0;

    private static final int ORIENTATION_VERTICAL = 0;
    private static final int ORIENTATION_HORIZONTAL = 1;

    private boolean mHint;
    private int mLastX;
    private int mLastY;
    private int mProgress;

    private int mOrientation;

    private DragAreaListener mDragAreaListener;
    private static final String TAG = "DragArea";

    public DragArea(Context context) {
        this(context, null);
    }

    public DragArea(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragArea(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setDragAreaListener(DragAreaListener dragAreaListener) {
        this.mDragAreaListener = dragAreaListener;
    }


    private void init() {
        mOrientation = ORIENTATION_VERTICAL;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = x;
                mLastY = y;
                mHint = isHint(x, y);
                Log.i(TAG, " drag area on action down");
                break;
            case MotionEvent.ACTION_MOVE:
                if (mHint) {
                    Log.i(TAG, " drag area on action move");
                    int distance = getDistance(x, y);
                    calculateProgress(distance);
                    if (mDragAreaListener != null) {
                        mDragAreaListener.onDragProgress(mProgress);
                    }
                    mLastX = x;
                    mLastY = y;
                }
                break;
            case MotionEvent.ACTION_UP:
                mHint = false;
                Log.i(TAG, " drag area on action up");
                break;
            default:
                break;
        }
        return mHint;
    }

    private int getDistance(int touchedX, int touchedY) {
        return mOrientation == ORIENTATION_VERTICAL ? touchedY - mLastY : touchedX - mLastX;
    }

    private void calculateProgress(int distance) {
        boolean isIncrease = distance > 0;
        int totalLength = mOrientation == ORIENTATION_VERTICAL ? getHeight() : getWidth();

        int progress = (int) Math.abs(Math.min(distance * 1.0 * 100 / totalLength, MAX_PROGRESS_PER_DRAG));
        if (isIncrease) {
            int changedProgress = progress + mProgress;
            mProgress = changedProgress > MAX_VALUE ? MAX_VALUE : changedProgress;
        } else {
            int changedProgress = progress - mProgress;
            mProgress = changedProgress < MIN_VALUE ? MIN_VALUE : changedProgress;
        }
    }

    private boolean isHint(int x, int y) {
        return getX() + getWidth() >= x && x >= 0 && getY() + getHeight() >= y && y >= 0;
    }

    public interface DragAreaListener {
        void onDragProgress(int progress);
    }
}

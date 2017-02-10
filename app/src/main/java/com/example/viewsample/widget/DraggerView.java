package com.example.viewsample.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.viewsample.R;

public class DraggerView extends View {


    private static final int SHADOW_COLOR = 0x88000000;
    private static final int GRAY_LINE_COLOR = 0xff999999;

    private static final int DEFAULT_VIEW_COLOR = 0xffffff;

    private int mBetweenColor;
    private int mDragBarColor;
    private int mProgressBarColor;

    private final Paint mViewPaint;

    private boolean mHitStart, mHitEnd;
    //
    private float lastX, lastY;
    //
    private float mDragBarRadius = 30;
    private float mProgressHeight = 10;

    private Point mPointStart;
    private Point mPointEnd;

    private DragListener mDragListener;

    public DraggerView(Context context) {
        this(context, null);
    }

    public DraggerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DraggerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DraggerView, defStyleAttr, 0);
        mProgressBarColor = a.getColor(R.styleable.DraggerView_progress_bar_color, DEFAULT_VIEW_COLOR);
        mDragBarColor = a.getColor(R.styleable.DraggerView_drag_bar_color, DEFAULT_VIEW_COLOR);
        mBetweenColor = a.getColor(R.styleable.DraggerView_between_color, DEFAULT_VIEW_COLOR);
        a.recycle();

        mViewPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mPointStart = new Point();
        mPointEnd = new Point();

        mHitStart = false;
        mHitEnd = false;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mPointStart.x = (int) (mDragBarRadius);
        mPointStart.y = getHeight() / 2;

        mPointEnd.x = (int) (getMeasuredWidth() - mDragBarRadius);
        mPointEnd.y = getHeight() / 2;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mHitStart = hitDrager(x, y, mPointStart);
                mHitEnd = hitDrager(x, y, mPointEnd);
                lastX = x;
                lastY = y;
                if(mHitStart || mHitEnd){
                    if(mDragListener != null){
                        mDragListener.onDragStarted();
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = x - lastX;
                float dy = y - lastY;
                lastX = x;
                lastY = y;
                if (mHitStart) {
                    mPointStart.x = (int) Math.max(Math.min(mPointEnd.x - mDragBarRadius * 2, mPointStart.x + dx), getX() + mDragBarRadius);
                    invalidate();
                }

                if (mHitEnd) {
                    mPointEnd.x = (int) Math.min(Math.max(mPointStart.x + mDragBarRadius * 2, mPointEnd.x + dx), getX() + getMeasuredWidth() - mDragBarRadius);
                    invalidate();
                }


                break;
            case MotionEvent.ACTION_UP:
                if (mDragListener != null) {
                    post(new Runnable() {
                        @Override
                        public void run() {
                            int startProgress = calculateProgress(mPointStart);
                            int endProgress = calculateProgress(mPointEnd);
                            mDragListener.onDragged(startProgress, endProgress);
                        }
                    });
                }
                mHitStart = false;
                mHitEnd = false;
                break;
            default:
                break;
        }
        return mHitStart || mHitEnd;
    }

    private int calculateProgress(Point point) {
        int progressLen = (int) (point.x - (getX() + mDragBarRadius));
        int progressBarLength = (int) (getMeasuredWidth() - mDragBarRadius * 2);
        return (int) (progressLen * 100.0f / progressBarLength);
    }



    private boolean hitDrager(float x, float y, Point point) {
        return (Math.pow(x - point.x, 2) + Math.pow(y - point.y, 2)) <= Math.pow(mDragBarRadius, 2);
    }

    public void setDragListener(DragListener dragListener){
        this.mDragListener = dragListener;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float progressBarTop = getHeight() / 2 - mProgressHeight / 2;


        //line
        mViewPaint.setColor(mProgressBarColor);
        canvas.drawRect(mDragBarRadius, progressBarTop, getMeasuredWidth() - mDragBarRadius, progressBarTop + mProgressHeight, mViewPaint);

        //between line
        mViewPaint.setColor(mBetweenColor);
        canvas.drawRect(mPointStart.x, progressBarTop, mPointEnd.x, progressBarTop + mProgressHeight, mViewPaint);

        //drag bar
        mViewPaint.setColor(mDragBarColor);
        canvas.drawCircle(mPointStart.x, mPointStart.y, mDragBarRadius, mViewPaint);
        canvas.drawCircle(mPointEnd.x, mPointEnd.y, mDragBarRadius, mViewPaint);
    }


    public static interface DragListener {

        void onDragged(int startProgress, int endProgress);

        void onDragStarted();
    }

}

package com.example.viewsample.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by 龙泉 on 2017/2/7.
 */

public class Wheel extends View {

    private final String TAG = "Wheel";

    Paint mPaint;

    private int mLayoutWidth;
    private int mLayoutHeight;

    private RectF mBoundRect;
    private float mInnerCircleMargin = 8;
    private float mInnerCircleRadius = 8;

    private boolean mHint;
    private float lastX;
    private float lastY;
    private float mDegree;

    public Wheel(Context context) {
        this(context, null);
    }

    public Wheel(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Wheel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.GREEN);
        mPaint.setAntiAlias(true);
        mBoundRect = new RectF();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int dw = 0;
        int dh = 0;

        dw += getPaddingLeft() + getPaddingRight();
        dh += getPaddingTop() + getPaddingTop();

        final int measuredWidth = resolveSizeAndState(dw, widthMeasureSpec, 0);
        final int measuredHeight = resolveSizeAndState(dh, heightMeasureSpec, 0);
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mLayoutWidth = w;
        mLayoutHeight = h;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(Color.BLUE);
        mBoundRect.set(0, 0, getWidth(), getHeight());

        int cx = getWidth() /2;
        int cy = getHeight() /2;
        int circleRadius = getWidth() /2;
        canvas.drawRect(mBoundRect, mPaint);
        canvas.rotate(mDegree, cx, cy);

        //paint shadow

        mPaint.setColor(Color.RED);
        canvas.drawCircle(cx,cy, circleRadius, mPaint);


        mPaint.setColor(Color.WHITE);
        canvas.save();
        int translateX = (int) (getWidth() - (mInnerCircleRadius * 2 + mInnerCircleMargin));
        canvas.translate(translateX, 0);
        canvas.drawCircle(mInnerCircleRadius, cy, mInnerCircleRadius, mPaint);
        canvas.restore();




    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = x;
                lastY = y;
                mHint = isHint(x,y);
                break;
            case MotionEvent.ACTION_MOVE:
                calculateRotationAndInvalidate(x, y);
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        return mHint;
    }

    private void calculateRotationAndInvalidate(float x, float y) {
        float dx = x - lastX;
        float dy = y - lastY;

        boolean isClockwise = (dx > 0 && Math.abs(dx) >= Math.abs(dy)) || (dy > 0 && Math.abs(dy) >= Math.abs(dx));
        float arcLength = (int) calculateDistance(dx, dy);
        float movedDegree = (float) ((180 * arcLength / (Math.PI * getWidth() / 2)) % 360);
        if (isClockwise) {
            mDegree += movedDegree;
        } else {
            mDegree -= movedDegree;
        }
        lastX = x;
        lastY = y;
        invalidate();
    }

    private boolean isHint(float x, float y) {
        float cx = getWidth() /2;
        float cy = getHeight() /2;
        double radius = getWidth() /2;
        return distanceBetweenToPoint(x,y, cx ,cy) <= radius;
    }

    private double distanceBetweenToPoint(float x, float y, float cx, float cy) {
        return calculateDistance(x - cx, y - cy);
    }

    /**
     *
     * @param dx x distance
     * @param dy y distance
     * @return
     */
    private double calculateDistance(float dx, float dy){
        return Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
    }

}

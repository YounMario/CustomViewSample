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
        Log.d(TAG, "onDraw left:" + getLeft() + " top:" + getTop() + " right:" + (getLeft() + getWidth()) + " bottom:" + (getTop() + getHeight()) + " translationX:" + getTranslationX() + " getX:" + getX());
        mPaint.setColor(Color.BLUE);
        mBoundRect.set(0, 0, getWidth(), getHeight());
        canvas.drawRect(mBoundRect, mPaint);

        mPaint.setColor(Color.RED);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2, mPaint);
        mPaint.setColor(Color.WHITE);


        canvas.save();
        int translateX = (int) (getWidth() - (mInnerCircleRadius * 2 + mInnerCircleMargin));
        canvas.translate(translateX, 0);
        canvas.drawCircle(mInnerCircleRadius, getHeight() / 2, mInnerCircleRadius, mPaint);
        canvas.restore();

    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

}

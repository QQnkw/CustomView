package com.nkw.customview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class ScanView extends View{

    private Paint mPaint;
    private SweepGradient mSweepGradient;
    private int mSize;

    public ScanView(Context context) {
        this(context,null);
    }

    public ScanView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ScanView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public ScanView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        this(context, attrs, defStyleAttr);
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mSize = w;
        //设置扫描渲染的shader
        mSweepGradient = new SweepGradient(0, 0, new int[]{Color.TRANSPARENT, Color.parseColor("#84B5CA")}, null);
    }

    private float degress = 0f;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(mSize/2,mSize/2);
        mPaint.setShader(mSweepGradient);
        canvas.rotate(degress);
        canvas.drawCircle(0,0,mSize/2,mPaint);
        degress+=0.5f;
        if (degress==360) {
            degress = 0;
        }
        invalidate();
    }
}

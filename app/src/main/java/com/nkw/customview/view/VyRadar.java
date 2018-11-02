package com.nkw.customview.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;

import com.nkw.customview.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class VyRadar extends View {

    private Bitmap  mCenterBitmap;//中心圖片
    private int     mCenterWidth;//中心图片宽度
    private int     mCenterHeight;//中心图片宽度
    private int     mTrackRedius;//轨道半径
    private Paint   mPaint;
    private Bitmap  mTrackBitmap;//轨道上的图片
    private boolean mIsRunning;
    private int      mSpeed        = 2000;   // 波纹的创建速度，每500ms创建一个
    private long mLastCreateTime;//上一个创建时间
    private List<Circle> mCircleList = new ArrayList<Circle>();
    private Interpolator mInterpolator = new LinearOutSlowInInterpolator();
    private long mDuration = 8000; // 一个波纹从创建到消失的持续时间
    private int mSize;
    private int mInitCircleWaveRedius;//波纹初始半径
    private Runnable mCreateCircle = new Runnable() {
        @Override
        public void run() {
            if (mIsRunning) {
                newCircle();
                postDelayed(mCreateCircle, mSpeed);
            }
        }
    };

    public void setInitCircleWaveRedius(int initCircleWaveRedius) {
        mInitCircleWaveRedius = initCircleWaveRedius;
    }

    public VyRadar(Context context) {
        this(context, null);
    }

    public VyRadar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VyRadar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    public VyRadar(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        this(context, attrs, defStyleAttr);
    }

    private void init() {
        mCenterBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.bg_earth);
        mCenterHeight = mCenterBitmap.getHeight();
        mCenterWidth = mCenterBitmap.getWidth();
        mTrackRedius = mCenterWidth / 2 + 50;
        //画笔的初始化
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mTrackBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_aircraft);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(width, width);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mSize = w;
    }

    private float mDegrees = 135;

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.translate(mSize / 2, mSize / 2);
        drawWave(canvas);
        drawCenterBitmap(canvas);
        drawTrack(canvas);
        drawTrackBitmap(canvas);
        if (mCircleList.size() > 0) {
            invalidate();
        }
    }

    private void drawTrackBitmap(Canvas canvas) {
        canvas.rotate(mDegrees);
        canvas.drawBitmap(mTrackBitmap, -mTrackBitmap.getWidth() / 2, -mTrackRedius - mTrackBitmap.getHeight() / 2, null);
        mDegrees += 0.25;
        if (mDegrees == 360) {
            mDegrees = 0;
        }
    }

    private void drawTrack(Canvas canvas) {
        mPaint.setAlpha(0);
        mPaint.setColor(getResources().getColor(R.color.app_grayC4C2C2));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(1);
        canvas.drawCircle(0, 0, mTrackRedius, mPaint);
    }

    private void drawCenterBitmap(Canvas canvas) {
        canvas.drawBitmap(mCenterBitmap, -mCenterWidth / 2, -mCenterHeight / 2, null);
    }


    private void drawWave(Canvas canvas) {
        mPaint.setColor(getResources().getColor(R.color.app_grayF0F0F0));
        mPaint.setStyle(Paint.Style.FILL);
        Iterator<Circle> iterator = mCircleList.iterator();
        while (iterator.hasNext()) {
            Circle circle = iterator.next();
            float radius = circle.getCurrentRadius();
            if (System.currentTimeMillis() - circle.mCreateTime < mDuration) {
                //            Log.d("NKW--->","控件半径"+mSize/2+"||radius"+radius);
                //                Log.d("NKW--->","绘制");
                mPaint.setAlpha(circle.getAlpha());
                canvas.drawCircle(0, 0, radius, mPaint);
            } else {
                //                Log.d("NKW--->","删除");
                iterator.remove();
            }
        }
        //        Log.d("NKW--->","集合大小"+mCircleList.size());
    }


    /**
     * 开始
     */
    public void start() {
        if (!mIsRunning) {
            mIsRunning = true;
            mCreateCircle.run();
            invalidate();
        }
    }

    /**
     * 缓慢停止
     */
    public void stop() {
        mIsRunning = false;
    }

    /**
     * 立即停止
     */
    public void stopImmediately() {
        mIsRunning = false;
        mCircleList.clear();
        invalidate();
    }


    /*LinearOutSlowInInterpolator水波纹推挤这个*/
    public void setInterpolator(Interpolator interpolator) {
        mInterpolator = interpolator;
    }

    public void setSpeed(int speed) {
        mSpeed = speed;
    }

    private void newCircle() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - mLastCreateTime < mSpeed) {
            return;
        }
        Circle circle = new Circle();
        mCircleList.add(circle);
        mLastCreateTime = currentTime;
    }

    private class Circle {
        private long mCreateTime;

        Circle() {
            mCreateTime = System.currentTimeMillis();
        }

        int getAlpha() {
            float percent = (getCurrentRadius() - mInitCircleWaveRedius) / (mSize/2 - mInitCircleWaveRedius);
            return (int) (255 - mInterpolator.getInterpolation(percent) * 255);
            /*float percent = (System.currentTimeMillis() - mCreateTime) * 1.0f / (mDuration);
            return (int) (255 - mInterpolator.getInterpolation(percent) * 255);*/
        }

        float getCurrentRadius() {
            float percent = (System.currentTimeMillis() - mCreateTime) * 1.0f / (mDuration);
            return mInitCircleWaveRedius + mInterpolator.getInterpolation(percent) * (mSize / 2 - mInitCircleWaveRedius);
            /*float percent = (System.currentTimeMillis() - mCreateTime) * 1.0f / (mDuration);
            return mInterpolator.getInterpolation(percent) * (mSize / 2);*/
        }
    }
}

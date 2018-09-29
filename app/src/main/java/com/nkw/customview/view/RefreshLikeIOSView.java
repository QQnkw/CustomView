package com.nkw.customview.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;

import com.nkw.customview.utils.SizeUtils;


public class RefreshLikeIOSView extends View {

    private Paint mPaint;
    private int mWidth  = SizeUtils.dp2px(30);
    private int mHeight = SizeUtils.dp2px(30);

    private int mCenterH;
    private int mCenterW;
    private   int      mRotateNum = 0;
    private   String[] colorArr   = {"#ff000000", "#eb000000", "#d6000000",
            "#c2000000", "#ad000000", "#99000000",
            "#85000000", "#70000000", "#5c000000",
            "#47000000", "#33000000", "#1e000000",};
    protected Path     mPath      = new Path();
    private ValueAnimator  mValueAnimator;
    private ScaleAnimation mScaleAnimation;

    public RefreshLikeIOSView(Context context) {
        this(context, null);
    }

    public RefreshLikeIOSView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshLikeIOSView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(SizeUtils.dp2px(1));
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.parseColor(colorArr[0]));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        if (mode == MeasureSpec.EXACTLY) {

        } else {
            size = mWidth;
        }
        int measureSpec = MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY);
        setMeasuredDimension(measureSpec, measureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mWidth = w;
        mHeight = h;
        mCenterW = w / 2;
        mCenterH = h / 2;
    }

    private int mRotateDegress = 0;

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.translate(mCenterW, mCenterH);
        canvas.rotate(150);
        if (isColorSeted) {
            canvas.save();
            canvas.rotate(mRotateDegress);
            for (int i = 0; i < mRotateNum; i++) {
                canvas.rotate(-30);
                mPaint.setColor(Color.parseColor(colorArr[i]));
                //                canvas.drawLine(0, mHeight / 4 - 15, 0, mHeight / 2 - 15, mPaint);
                canvas.drawLine(0, (mHeight / 4)-5, 0, mHeight / 2-10, mPaint);
            }
            canvas.restore();
           /* final int r = Math.max(1, mCenterW / 10);
            mPath.reset();
            mPath.addCircle(r, mCenterW / 2, r, Path.Direction.CW);
            mPath.addRect(r, mCenterW / 2 +r, mCenterW - r, mCenterW / 2 - r, Path.Direction.CCW);
            mPath.addCircle(mCenterW - r, mCenterW / 2, r, Path.Direction.CW);

            canvas.save();
            canvas.rotate(mRotateDegress);
            for (int i = 0; i < 1; i++) {
                canvas.rotate(30);
                mPaint.setColor(Color.parseColor(colorArr[i]));
                canvas.drawPath(mPath, mPaint);
            }
            canvas.restore();*/

        } else {
            mPaint.setColor(Color.parseColor(colorArr[0]));
            for (int i = 0; i < mRotateNum; i++) {
                canvas.rotate(30);
                //                canvas.drawLine(0, mHeight / 4 - 15, 0, mHeight / 2 - 15, mPaint);
                canvas.drawLine(0, (mHeight / 4)-5, 0, mHeight / 2-10, mPaint);
            }
        }
    }

    private boolean isAnimateIng = false;

    public void setProgress(int num) {
        if (isAnimateIng) {

        } else {
            float degress = 360 * 0.01f * num;
            float v = degress / 30;
            mRotateNum = (int) v;
            if (mRotateNum >= 12) {
                mRotateNum = 12;
                scaleAnima();
            } else {
                invalidate();
            }
        }
    }

    private boolean isColorSeted = false;

    private void scaleAnima() {
        isAnimateIng = true;
        mScaleAnimation = new ScaleAnimation(1.0f, 1.1f, 1.0f, 1.1f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
        );
        mScaleAnimation.setDuration(500);
        mScaleAnimation.setInterpolator(new BounceInterpolator());
        mScaleAnimation.setFillAfter(true);
        mScaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isColorSeted = true;
                rotateAnima();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        this.startAnimation(mScaleAnimation);
    }

    private void rotateAnima() {
        mValueAnimator = ValueAnimator.ofInt(30, 3600).setDuration(10000);
        mValueAnimator.setInterpolator(new LinearInterpolator());
        mValueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mValueAnimator.setRepeatMode(ValueAnimator.RESTART);
        mValueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                isColorSeted = false;
                isAnimateIng = false;
            }
        });
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                mRotateDegress = 30 * (value / 30);
                invalidate();
            }
        });
        mValueAnimator.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mValueAnimator != null) {
            mValueAnimator.cancel();
        }
    }

    public void stopAnimator() {
        if (mValueAnimator != null) {
            mValueAnimator.cancel();
        }
    }
}

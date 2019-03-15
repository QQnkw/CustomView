package com.nkw.customview.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.animation.LinearInterpolator;

import com.nkw.customview.R;


public class MatchingTimeDrawable extends Drawable {
    private final Paint         mPaint;
    private       int           mValue;
    private final Bitmap        mBitmap;
    private final int           mSize;
    private final ValueAnimator mValueAnimator;
    private       boolean       isDraw = false;

    public MatchingTimeDrawable(Context context) {
        mBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon_matching_time);
        mSize = Math.max(mBitmap.getHeight(), mBitmap.getWidth());
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mValueAnimator = ValueAnimator.ofInt(0, 360).setDuration(3000);
        mValueAnimator.setInterpolator(new LinearInterpolator());
        mValueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mValueAnimator.setRepeatMode(ValueAnimator.RESTART);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (isDraw) {
                    mValue = (int) animation.getAnimatedValue();
                    isDraw = false;
                    invalidateSelf();
                }else{
                    stop();
                }
            }
        });
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        isDraw = true;
        int level = getLevel();
        Log.d("LEVEL", level + "");
        int dx = mSize / 2;
        canvas.translate(dx, dx);
        canvas.rotate(mValue);
        canvas.scale(0.9f, 0.9f);
        canvas.drawBitmap(mBitmap, -mBitmap.getWidth() * 1f / 2, -mBitmap.getHeight() * 1f / 2, mPaint);
        start();
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    //drawable根据这个返回值确定自身大小
    @Override
    public int getIntrinsicWidth() {
        return mSize;
    }
    //drawable根据这个返回值确定自身大小
    @Override
    public int getIntrinsicHeight() {
        return mSize;
    }

    private void start() {
        if (!mValueAnimator.isRunning()) {
            mValueAnimator.start();
        }
    }

    private void stop() {
        if (mValueAnimator.isRunning()) {
            mValueAnimator.removeAllListeners();
            mValueAnimator.removeAllUpdateListeners();
            mValueAnimator.cancel();
        }
    }
}

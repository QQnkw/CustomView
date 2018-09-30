package com.nkw.customview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;

import com.nkw.customview.R;
import com.nkw.customview.utils.SizeUtils;


public class VyLoading extends View {

    private Paint mPaint;
    private int   mArcColor;
    private int   mArcWith;
    private RectF mRectF;
    private float   firstStart  = 0;
    private float   firstSweep  = 0;
    private float   secondSweep = 0;
    private float   secondStart = 0;
    private float   thirdSweep  = 0;
    private float   thirdStart  = 0;
    private float   fouthSweep  = 0;
    private float   fouthStart  = 0;
    private boolean reversal    = false;
    private boolean isStartDraw = false;
    private ScaleAnimation mScaleAnimation;
    private AlphaAnimation mAlphaAnimation;
    private AnimationSet   mAnimationSet;

    public VyLoading(Context context) {
        this(context, null);
    }

    public VyLoading(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VyLoading(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getArcParams(context, attrs);
        initPaint();

        mScaleAnimation = new ScaleAnimation(1f, 0f, 1f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mAlphaAnimation = new AlphaAnimation(1f, 0f);
        mAnimationSet = new AnimationSet(true);
        mAnimationSet.addAnimation(mScaleAnimation);
        mAnimationSet.addAnimation(mAlphaAnimation);
        mAnimationSet.setDuration(2000);
        mAnimationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setVisibility(GONE);
                isStartDraw = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void getArcParams(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.VyLoading);
        mArcColor = typedArray.getColor(R.styleable.VyLoading_arcColor, getResources().getColor(R.color.app_purple_6445F3));
        mArcWith = typedArray.getDimensionPixelSize(R.styleable.VyLoading_arcWidth, SizeUtils.dp2px(3));
        typedArray.recycle();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setColor(mArcColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mArcWith);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int size = MeasureSpec.getSize(widthMeasureSpec);
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        setMeasuredDimension((mode == MeasureSpec.EXACTLY ? size : SizeUtils.dp2px(70)), (mode == MeasureSpec.EXACTLY ? size :SizeUtils.dp2px(70)));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRectF = new RectF(0 + mArcWith, 0 + mArcWith, w - mArcWith, h - mArcWith);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isStartDraw) {
            //            canvas.drawColor(Color.WHITE, PorterDuff.Mode.CLEAR);
            return;
        }
        canvas.drawArc(mRectF, firstStart, -firstSweep, false, mPaint);
        canvas.drawArc(mRectF, secondStart, -secondSweep, false, mPaint);
        canvas.drawArc(mRectF, thirdStart, -thirdSweep, false, mPaint);
        canvas.drawArc(mRectF, fouthStart, -fouthSweep, false, mPaint);
        if (firstStart == 1080) {
            reversal = true;
        }
        if (firstStart == 360) {
            reversal = false;
        }
        if (reversal) {
            firstStart -= 5;
        } else {
            firstStart += 5;
        }

        if (firstStart <= 45) {
            firstSweep = firstStart;
        }

        if (firstStart >= 90) {
            secondStart = firstStart - 90;
            if (secondSweep <= 45) {
                secondSweep = secondStart;
            }
        }
        if (firstStart >= 180) {
            thirdStart = firstStart - 180;
            if (thirdSweep <= 45) {
                thirdSweep = thirdStart;
            }
        }
        if (firstStart >= 270) {
            fouthStart = firstStart - 270;
            if (fouthSweep <= 45) {
                fouthSweep = fouthStart;
            }
        }
        invalidate();
    }

    public void stopLoading() {
        this.startAnimation(mAnimationSet);
    }

    public void startLoading() {
        isStartDraw = true;
        resetValue();
        reversal = false;
        setVisibility(VISIBLE);
        invalidate();
    }

    private void resetValue() {
        firstStart = 0;
        firstSweep = 0;
        secondStart = 0;
        secondSweep = 0;
        thirdStart = 0;
        thirdSweep = 0;
        fouthStart = 0;
        fouthSweep = 0;
    }
}


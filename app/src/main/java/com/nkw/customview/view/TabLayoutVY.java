package com.nkw.customview.view;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.nkw.customview.R;


public class TabLayoutVY extends LinearLayout implements View.OnClickListener {
    private TabClickedListener mListener;
    private Paint              mRectPaint;
    private int                mUnderLineColor;
    private int                mUnderLineHeight;
    private float mLineRight = 0;
    private float mLineLeft  = 0;
    private AnimatorSet mAnimatorSet;
    private View        mOldView;
    private boolean isFirstDraw = true;

    public TabLayoutVY(Context context) {
        this(context, null);
    }

    public TabLayoutVY(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabLayoutVY(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
        setOrientation(LinearLayout.HORIZONTAL);
        getAttribute(context, attrs);
        mRectPaint = new Paint();
        mRectPaint.setAntiAlias(true);
        mRectPaint.setStyle(Paint.Style.FILL);
        mRectPaint.setColor(mUnderLineColor);

        mAnimatorSet = new AnimatorSet();

    }

    private ValueAnimator animatorRight(float start, float middle, float end) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(start, middle, end);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mLineRight = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        return valueAnimator;
    }

    private ValueAnimator animatorLeft(float start, float middle, float end) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(start, middle, end);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mLineLeft = (float) animation.getAnimatedValue();
//                                invalidate();
            }
        });
        return valueAnimator;
    }

    private void getAttribute(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TabLayoutVY);
        mUnderLineColor = typedArray.getColor(R.styleable.TabLayoutVY_underLineColor, getResources().getColor(R.color.app_purple_6445F3));
        mUnderLineHeight = typedArray.getDimensionPixelSize(R.styleable.TabLayoutVY_underLineHeight, 5);
        typedArray.recycle();

    }

    public TabLayoutVY(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        this(context, attrs, defStyleAttr);

    }

    @Override
    public void onClick(View v) {
        if (v == mOldView) {
            return;
        }
        mOldView.setSelected(false);
        v.setSelected(true);
        mListener.onTabClicked(v.getId());
        startLineAnimator(v);
        isFirstDraw = false;
        mOldView = v;
    }

    private void startLineAnimator(View v) {
        ValueAnimator valueAnimatorLeft = animatorLeft(mOldView.getLeft(), getChildAt(0).getLeft(), v.getLeft());
        ValueAnimator valueAnimatorRight = animatorRight(mOldView.getRight(), getChildAt(getChildCount() - 1).getRight(), v.getRight());
        mAnimatorSet.play(valueAnimatorLeft).with(valueAnimatorRight);
        mAnimatorSet.setDuration(500);
        mAnimatorSet.start();
    }

    public interface TabClickedListener {
        void onTabClicked(int clickedResID);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (getChildCount() <= 0) {
            return;
        }
        if (isFirstDraw) {
            mLineLeft = mOldView.getLeft();
            mLineRight = mOldView.getRight();
        }
        canvas.drawRect(mLineLeft, mOldView.getBottom()-mUnderLineHeight, mLineRight, mOldView.getBottom(), mRectPaint);
    }

    public void addSelectorListener(TabClickedListener listener) {
        mListener = listener;
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            childAt.setOnClickListener(this);
            if (i == 0) {
                mOldView = childAt;
                childAt.setSelected(true);
                listener.onTabClicked(childAt.getId());
            }
        }
    }
}

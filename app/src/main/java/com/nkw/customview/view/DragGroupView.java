package com.nkw.customview.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.FrameLayout;

public class DragGroupView extends FrameLayout {

    private float                        mPreMoveY;
    private float                        mPreMoveX;
    private int                          mParentHeight;
    private int                          mParentWidth;
    private float                        mDownX;
    private float                        mDownY;
    private ValueAnimator                mValueAnimator;
    private OnDragGroupViewClickListener mListner;

    public DragGroupView(@NonNull Context context) {
        super(context, null);
    }

    public DragGroupView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public DragGroupView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initData();
    }

    private void initData() {
        ViewGroup viewGroup = (ViewGroup) getParent();
        mParentHeight = viewGroup.getHeight();
        mParentWidth = viewGroup.getWidth();
        mValueAnimator = new ValueAnimator();
        mValueAnimator.setIntValues();
        mValueAnimator.setDuration(1000);
        mValueAnimator.setRepeatCount(0);
        mValueAnimator.setInterpolator(new BounceInterpolator());
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int updataLeft = (int) animation.getAnimatedValue();
                setX(updataLeft);
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = mPreMoveX = (int) event.getRawX();
                mDownY = mPreMoveY = (int) event.getRawY();
                if (mValueAnimator.isRunning()) {
                    mValueAnimator.cancel();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float difX = event.getRawX() - mPreMoveX;
                int x = (int) (difX + getTranslationX());
                float difY = event.getRawY() - mPreMoveY;
                int y = (int) (difY + getTranslationY());
                if (x < 0) {
                    x = 0;
                }
                if (x + getWidth() > mParentWidth) {
                    x = mParentWidth - getWidth();
                }
                if (y < 0) {
                    y = 0;
                }
                if (y + getHeight() > mParentHeight) {
                    y = mParentHeight - getHeight();
                }
                setStartLocation(x,y);
                mPreMoveX = (int) event.getRawX();
                mPreMoveY = (int) event.getRawY();
//                LogUtils.d("getTranslationX--->"+getTranslationX());
                break;
            case MotionEvent.ACTION_UP:
                if (Math.abs(event.getRawX() - mDownX) < 5 && Math.abs(event.getRawY() - mDownY) < 5) {
                    if (mListner != null) {
                        mListner.onClick(this);
                    }
                } else {
                    moveNearEdge();
                }
                break;
        }
        return true;
    }

    /**
     * 移至最近的边沿
     */

    private void moveNearEdge() {
        int x = (int) getTranslationX();
        int lastX;
        if ((x + getWidth() / 2) < mParentWidth / 2) {
            lastX = 0;
        } else {
            lastX = mParentWidth - getWidth();
        }
        mValueAnimator.setIntValues(x, lastX);
        mValueAnimator.start();
    }


    /**
     * 定位初始位置,单位像素
     *
     * @param x
     * @param y
     */

    public void setStartLocation(int x, int y) {
        setX(x);
        setY(y);
    }

    public interface OnDragGroupViewClickListener {
        void onClick(View view);
    }

    public void setOnDragGroupViewClickListener(OnDragGroupViewClickListener listner) {
        mListner = listner;
    }
}

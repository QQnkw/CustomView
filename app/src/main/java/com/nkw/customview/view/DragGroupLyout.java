package com.nkw.customview.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class DragGroupLyout extends FrameLayout {
    private int       mWidth;
    private int       mHeight;
    private int       mParentHeight;
    private int       mParentWidth;
    private ViewGroup mParent;
    private int       mLeft;
    private int       mTop;
    private int       mRight;
    private int       mBottom;

    public DragGroupLyout(@NonNull Context context) {
        this(context, null);
    }

    public DragGroupLyout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragGroupLyout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DragGroupLyout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        this(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.d("NKW-onSizeChanged-->", "width：" + w + "||height:" + h + "||oldw:" + oldw + "||oldh:" + oldh);
        mWidth = w;
        mHeight = h;
        /*mOldw = oldw;
        mOldh = oldh;*/
        mParent = (ViewGroup) getParent();
        mParentWidth = mParent.getWidth();
        mParentHeight = mParent.getHeight();
    }

    public void setMoveMargin(int left, int top, int right, int bottom) {
        mLeft = left;
        mTop = top;
        mRight = right;
        mBottom = bottom;

    }

    public void setStartLocation(int x, int y) {
        FrameLayout.LayoutParams flLayoutParams = (LayoutParams) getLayoutParams();
        flLayoutParams.leftMargin = x;
        flLayoutParams.topMargin = y;
        setLayoutParams(flLayoutParams);
    }

    private void setLocation(int x, int y) {
        FrameLayout.LayoutParams layoutParams = (LayoutParams) getLayoutParams();
        layoutParams.leftMargin = layoutParams.leftMargin + x;
        layoutParams.topMargin = layoutParams.topMargin + y;
        if (layoutParams.leftMargin < (mParent.getPaddingLeft() + mLeft)) {
            layoutParams.leftMargin = mParent.getPaddingLeft() + mLeft;
        }
        if (layoutParams.leftMargin >= (mParentWidth - mWidth - mParent.getPaddingRight() - mRight)) {
            layoutParams.leftMargin = mParentWidth - mWidth - mParent.getPaddingRight() - mRight;
        }
        Log.d("NKW-setLocation-->", "mParentWidth=" + mParentWidth + "||mWidth=" + mWidth + "||mFlParams.leftMargin=" + layoutParams.leftMargin);
        if (layoutParams.topMargin < (mParent.getPaddingTop() + mTop)) {
            layoutParams.topMargin = mParent.getPaddingTop() + mTop;
        }
        if (layoutParams.topMargin >= (mParentHeight - mHeight - mParent.getPaddingBottom() - mBottom)) {
            layoutParams.topMargin = mParentHeight - mHeight - mParent.getPaddingBottom() - mBottom;
        }
        //            Log.d("NKW-setLocation-->", "maxLeftMargin=" + (mParentWidth - mWidth) + "||leftMargin=" + mFlParams.leftMargin + "||maxTopMargin=" + (mParentHeight - mHeight) + "||topMargin=" + mFlParams.topMargin);
        setLayoutParams(layoutParams);
    }

    private float mDownX    = 0;
    private float mDownY    = 0;
    private float mMovePreX = 0;
    private float mMovePreY = 0;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = ev.getRawX();
                mDownY = ev.getRawY();
                mMovePreX = mDownX;
                mMovePreY = mDownY;
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(ev.getX() - mDownX) > 5 || Math.abs(ev.getY() - mDownY) > 5) {
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float moveX = event.getRawX();
                float moveY = event.getRawY();
                float difX = moveX - mMovePreX;
                float difY = moveY - mMovePreY;
                Log.d("NKW-onTouchEvent-->", "moveX =" + moveX + "|||moveY =" + moveY);
                setLocation((int) difX, (int) difY);
                mMovePreX = moveX;
                mMovePreY = moveY;
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

    public int[] getCurrentLocation() {
        FrameLayout.LayoutParams layoutParams = (LayoutParams) getLayoutParams();
        return new int[]{layoutParams.leftMargin, layoutParams.topMargin};
    }
}
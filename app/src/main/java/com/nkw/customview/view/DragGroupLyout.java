package com.nkw.customview.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class DragGroupLyout extends FrameLayout {
    private int                         mWidth;
    private int                         mHeight;
    private int                         mParentHeight;
    private int                         mParentWidth;
    private LayoutParams                mFlParams;
    private LinearLayout.LayoutParams   mLlParams;
    private RelativeLayout.LayoutParams mRlParams;
    private ViewGroup                   mParent;
    private int                         mLeft;
    private int                         mTop;
    private int                         mRight;
    private int                         mBottom;
    private int                         mStartY;
    private int                         mStartX;

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
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mWidth != 0 || mHeight != 0) {
            setMeasuredDimension(mWidth, mHeight);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
        //        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
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
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (mParent instanceof FrameLayout) {
            mFlParams = (LayoutParams) layoutParams;
        } else if (mParent instanceof LinearLayout) {
            mLlParams = (LinearLayout.LayoutParams) layoutParams;
        } else if (mParent instanceof RelativeLayout) {
            mRlParams = (RelativeLayout.LayoutParams) layoutParams;
        } else {
            throw new ClassCastException("父布局必须是FrameLayout，LinearLayout或RelativeLayout其中一种");
        }
        //        setLocation(mStartX,mStartY);
    }

    public void setMoveMargin(int left, int top, int right, int bottom) {
        mLeft = left;
        mTop = top;
        mRight = right;
        mBottom = bottom;

    }

    public void setStartLocation(String typeLayoutParams, int x, int y) {
        switch (typeLayoutParams) {
            case "FrameLayout":
                FrameLayout.LayoutParams flLayoutParams = (LayoutParams) getLayoutParams();
                flLayoutParams.leftMargin = x;
                flLayoutParams.topMargin = y;
                setLayoutParams(flLayoutParams);
                break;
            case "LinearLayout":
                LinearLayout.LayoutParams llLayoutParams = (LinearLayout.LayoutParams) getLayoutParams();
                llLayoutParams.leftMargin = x;
                llLayoutParams.topMargin = y;
                setLayoutParams(llLayoutParams);
                break;
            case "RelativeLayout":
                RelativeLayout.LayoutParams rlLayoutParams = (RelativeLayout.LayoutParams) getLayoutParams();
                rlLayoutParams.leftMargin = x;
                rlLayoutParams.topMargin = y;
                setLayoutParams(rlLayoutParams);
                break;
            default:
                throw new ClassCastException("父布局必须是FrameLayout，LinearLayout或RelativeLayout其中一种");
        }
    }

    private void setLocation(int x, int y) {
        if (mFlParams != null) {
            mFlParams.leftMargin = mFlParams.leftMargin + x;
            mFlParams.topMargin = mFlParams.topMargin + y;
            if (mFlParams.leftMargin < (mParent.getPaddingLeft() + mLeft)) {
                mFlParams.leftMargin = mParent.getPaddingLeft() + mLeft;
            }
            if (mFlParams.leftMargin >= (mParentWidth - mWidth - mParent.getPaddingRight() - mRight)) {
                mFlParams.leftMargin = mParentWidth - mWidth - mParent.getPaddingRight() - mRight;
            }
            Log.d("NKW-setLocation-->", "mParentWidth=" + mParentWidth + "||mWidth=" + mWidth + "||mFlParams.leftMargin=" + mFlParams.leftMargin);
            if (mFlParams.topMargin < (mParent.getPaddingTop() + mTop)) {
                mFlParams.topMargin = mParent.getPaddingTop() + mTop;
            }
            if (mFlParams.topMargin >= (mParentHeight - mHeight - mParent.getPaddingBottom() - mBottom)) {
                mFlParams.topMargin = mParentHeight - mHeight - mParent.getPaddingBottom() - mBottom;
            }
            setLayoutParams(mFlParams);
            //            Log.d("NKW-setLocation-->", "maxLeftMargin="+(mParentWidth - mWidth)+"||leftMargin=" + mFlParams.leftMargin + "||maxTopMargin="+(mParentHeight - mHeight)+"||topMargin=" + mFlParams.topMargin);
        } else if (mLlParams != null) {
            mLlParams.leftMargin = mLlParams.leftMargin + x;
            mLlParams.topMargin = mLlParams.topMargin + y;
            if (mLlParams.leftMargin < (mParent.getPaddingLeft() + mLeft)) {
                mLlParams.leftMargin = mParent.getPaddingLeft() + mLeft;
            }
            if (mLlParams.leftMargin >= (mParentWidth - mWidth - mParent.getPaddingRight() - mRight)) {
                mLlParams.leftMargin = mParentWidth - mWidth - mParent.getPaddingRight() - mRight;
            }
            Log.d("NKW-setLocation-->", "mParentWidth=" + mParentWidth + "||mWidth=" + mWidth + "||mFlParams.leftMargin=" + mFlParams.leftMargin);
            if (mLlParams.topMargin < (mParent.getPaddingTop() + mTop)) {
                mLlParams.topMargin = mParent.getPaddingTop() + mTop;
            }
            if (mLlParams.topMargin >= (mParentHeight - mHeight - mParent.getPaddingBottom() - mBottom)) {
                mLlParams.topMargin = mParentHeight - mHeight - mParent.getPaddingBottom() - mBottom;
            }
            setLayoutParams(mLlParams);
            //            Log.d("NKW-setLocation-->", "maxLeftMargin="+(mParentWidth - mWidth)+"||leftMargin=" + mFlParams.leftMargin + "||maxTopMargin="+(mParentHeight - mHeight)+"||topMargin=" + mFlParams.topMargin);
        } else if (mRlParams != null) {
            mRlParams.leftMargin = mRlParams.leftMargin + x;
            mRlParams.topMargin = mRlParams.topMargin + y;
            if (mRlParams.leftMargin < (mParent.getPaddingLeft() + mLeft)) {
                mRlParams.leftMargin = mParent.getPaddingLeft() + mLeft;
            }
            if (mRlParams.leftMargin >= (mParentWidth - mWidth - mParent.getPaddingRight() - mRight)) {
                mRlParams.leftMargin = mParentWidth - mWidth - mParent.getPaddingRight() - mRight;
            }
            Log.d("NKW-setLocation-->", "mParentWidth=" + mParentWidth + "||mWidth=" + mWidth + "||mFlParams.leftMargin=" + mFlParams.leftMargin);
            if (mRlParams.topMargin < (mParent.getPaddingTop() + mTop)) {
                mRlParams.topMargin = mParent.getPaddingTop() + mTop;
            }
            if (mRlParams.topMargin >= (mParentHeight - mHeight - mParent.getPaddingBottom() - mBottom)) {
                mRlParams.topMargin = mParentHeight - mHeight - mParent.getPaddingBottom() - mBottom;
            }
            setLayoutParams(mRlParams);
            //            Log.d("NKW-setLocation-->", "maxLeftMargin="+(mParentWidth - mWidth)+"||leftMargin=" + mFlParams.leftMargin + "||maxTopMargin="+(mParentHeight - mHeight)+"||topMargin=" + mFlParams.topMargin);
        }
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
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (mParent instanceof FrameLayout) {
            mFlParams = (LayoutParams) layoutParams;
            return new int[]{mFlParams.leftMargin, mFlParams.topMargin};
        } else if (mParent instanceof LinearLayout) {
            mLlParams = (LinearLayout.LayoutParams) layoutParams;
            return new int[]{mLlParams.leftMargin, mLlParams.topMargin};
        } else if (mParent instanceof RelativeLayout) {
            mRlParams = (RelativeLayout.LayoutParams) layoutParams;
            return new int[]{mRlParams.leftMargin, mRlParams.topMargin};
        } else {
            return new int[]{0, 0};

        }
    }
}

package com.nkw.customview.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class DragFrameLayout extends FrameLayout {
    private int                    mHeight;
    private int                    mWidth;
    private int                    mParentWidth;
    private int                    mParentHeight;
    private DragFrameClickListener mListener;
    private boolean mIsDrag              = false;
    private int     marginLeft           = 0;
    private int     marginTop            = 0;
    private int     marginRight          = 0;
    private int     marginBottom         = 0;
    private int     mParentPaddingLeft   = 0;
    private int     mParentPaddingTop    = 0;
    private int     mParentPaddingRight  = 0;
    private int     mParentPaddingBottom = 0;

    public DragFrameLayout(@NonNull Context context) {
        this(context, null);
    }

    public DragFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
        Log.d("NKW--->", "DragFrameLayout");
    }

    public DragFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DragFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        this(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.d("NKW--->", "onSizeChanged");
        mWidth = w;
        mHeight = h;
        ViewGroup parentView = (ViewGroup) getParent();
        mParentPaddingLeft = parentView.getPaddingLeft();
        mParentPaddingTop = parentView.getPaddingTop();
        mParentPaddingRight = parentView.getPaddingRight();
        mParentPaddingBottom = parentView.getPaddingBottom();
        mParentWidth = parentView.getWidth();
        mParentHeight = parentView.getHeight();

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        Log.d("NKW--->", "mIsDrag:"+mIsDrag+"   changed:" + changed + "   left:" + left + "   top:" + top + "   right:" + right + "   bottom:" + bottom);
        if (mIsDrag && changed) {
            setLayoutLocation(mLeft, mTop);
        } else {
            super.onLayout(changed, left, top, right, bottom);
        }
    }

    private int startRawX = 0;
    private int preRawX   = 0;
    private int startRawY = 0;
    private int preRawY   = 0;
    private int mLeft     = 0;
    private int mTop      = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                mIsDrag = false;
                int moveRawX = (int) event.getRawX();
                int moveRawY = (int) event.getRawY();
                int difX = moveRawX - preRawX;
                int difY = moveRawY - preRawY;
                int left = getLeft();
                int top = getTop();

                int newLeft = left + difX;
                int newTop = top + difY;
                if (newLeft < marginLeft + mParentPaddingLeft) {
                    newLeft = marginLeft + mParentPaddingLeft;
                } else {
                    if (mParentWidth - mWidth - marginRight - mParentPaddingRight < newLeft) {
                        newLeft = mParentWidth - mWidth - marginRight - mParentPaddingRight;
                    }
                }
                if (newTop < marginTop + mParentPaddingTop) {
                    newTop = marginTop + mParentPaddingTop;
                } else {
                    if (mParentHeight - mHeight - marginBottom - mParentPaddingBottom < newTop) {
                        newTop = mParentHeight - mHeight - marginBottom - mParentPaddingBottom;
                    }
                }
                layout(newLeft, newTop, newLeft + mWidth, newTop + mHeight);
                preRawX = moveRawX;
                preRawY = moveRawY;
                break;
            case MotionEvent.ACTION_UP:
                int upRawX = (int) event.getRawX();
                int upRawY = (int) event.getRawY();
                if (Math.abs(upRawX - startRawX) < 2f && Math.abs(upRawY - startRawY) < 2f) {
                    if (mListener != null) {
                        mListener.onDragFrameClick();
                    }
                }
                mIsDrag = true;
                mLeft = getLeft();
                mTop = getTop();
                break;
        }
        return true;
    }

    public void setLayoutLocation(int left, int top) {
        if (left < 0) {
            left = 0;
        } else {
            if (mParentWidth - mWidth < left) {
                left = mParentWidth - mWidth;
            }
        }
        if (top < 0) {
            top = 0;
        } else {
            if (mParentHeight - mHeight < top) {
                top = mParentHeight - mHeight;
            }
        }
        layout(left, top, left + mWidth, top + mHeight);
    }


    public interface DragFrameClickListener {
        void onDragFrameClick();
    }

    public void setDragFrameClickListener(DragFrameClickListener listener) {
        mListener = listener;
    }

    /**
     * 在父控件中距边缘能够滑动的位置，单位是像素
     *
     * @param marginLeft
     * @param marginTop
     * @param marginRight
     * @param marginBottom
     */
    public void setDragMarginParent(int marginLeft, int marginTop, int marginRight, int marginBottom) {
        this.marginLeft = marginLeft;
        this.marginTop = marginTop;
        this.marginRight = marginRight;
        this.marginBottom = marginBottom;
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startRawX = (int) ev.getRawX();
                startRawY = (int) ev.getRawY();
                preRawX = startRawX;
                preRawY = startRawY;
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                if (ev.getRawX() - startRawX > 5 || ev.getRawY() - startRawY > 5) {
                    return true;
                }
                break;
        }

        return super.onInterceptTouchEvent(ev);
    }
}
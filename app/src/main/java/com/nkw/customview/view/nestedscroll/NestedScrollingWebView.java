package com.nkw.customview.view.nestedscroll;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingChild2;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.webkit.WebView;
import android.widget.Scroller;

public class NestedScrollingWebView extends WebView implements NestedScrollingChild2 {
    private boolean mIsSelfFling;//是否正在fling
    private boolean mHasFling;

    private final int TOUCH_SLOP;
    private int mMaximumVelocity;
    private int mFirstY;
    private int mLastY;
    private int mMaxScrollY;//最大网页滚动距离
    private int mWebViewContentHeight;//网页高低
    private int mJsCallWebViewContentHeight;//js代码通知的高度

    private final int[] mScrollConsumed = new int[2];//消耗的滑动

    private final float DENSITY;

    private NestedScrollingChildHelper mChildHelper;
    private NestedScrollingDetailContainer mParentView;
    private Scroller mScroller;
    private VelocityTracker mVelocityTracker;

    public NestedScrollingWebView(Context context) {
        this(context, null);
    }

    public NestedScrollingWebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NestedScrollingWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mChildHelper = new NestedScrollingChildHelper(this);

        setNestedScrollingEnabled(true);

        mScroller = new Scroller(getContext());

        final ViewConfiguration configuration = ViewConfiguration.get(getContext());
        //获得允许执行fling （抛）的最大速度值
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
        //  获取touchSlop （系统 滑动距离的最小值，大于该值可以认为滑动）
        TOUCH_SLOP = configuration.getScaledTouchSlop();

        DENSITY = context.getResources().getDisplayMetrics().density;
    }

    /**
     * 设置JS回调的Web内容高度,解决内部计算不准导致的问题
     */
    public void setJsCallWebViewContentHeight(int webViewContentHeight) {
        if (webViewContentHeight > 0 && webViewContentHeight != mJsCallWebViewContentHeight) {
            mJsCallWebViewContentHeight = webViewContentHeight;
            if (mJsCallWebViewContentHeight < getHeight()) {
                // 内部高度<控件高度时,调整控件高度为内容高度
                DimenHelper.updateLayout(this, DimenHelper.NOT_CHANGE, mJsCallWebViewContentHeight);
            }
        }
    }

    public int getWebViewContentHeight() {
        if (mWebViewContentHeight == 0) {
            mWebViewContentHeight = mJsCallWebViewContentHeight;
        }

        if (mWebViewContentHeight == 0) {
            mWebViewContentHeight = (int) (getContentHeight() * DENSITY);
        }

        return mWebViewContentHeight;
    }

    public boolean canScrollDown() {
        final int range = getWebViewContentHeight() - getHeight();
        if (range <= 0) {
            return false;
        }
        //当前滚动的位置
        final int offset = getScrollY();
        return offset < range - TOUCH_SLOP;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mWebViewContentHeight = 0;
                mLastY = (int) event.getRawY();
                mFirstY = mLastY;
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                initOrResetVelocityTracker();
                mIsSelfFling = false;
                mHasFling = false;
                mMaxScrollY = getWebViewContentHeight() - getHeight();
                startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL);
                if (getParent() != null) {
                    //设置父布局,忽略后续的动作
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                initVelocityTrackerIfNotExists();
                mVelocityTracker.addMovement(event);
                int y = (int) event.getRawY();
                int dy = y - mLastY;
                mLastY = y;
                if (getParent() != null) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                if (!dispatchNestedPreScroll(0, -dy, mScrollConsumed, null)) {
                    scrollBy(0, -dy);
                }
                if (Math.abs(mFirstY - y) > TOUCH_SLOP) {
                    //屏蔽WebView本身的滑动，滑动事件自己处理
                    event.setAction(MotionEvent.ACTION_CANCEL);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (isParentResetScroll() && mVelocityTracker != null) {
                    // (一般在手指up方法中)设置单位时间内的速度(注意: 设置单位时间内的速度,
                    // 1000就是表示在1s中的速度, 那下面的getYVelocity()就是获取的是在1s中的速度值)
                    mVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                    int yVelocity = (int) -mVelocityTracker.getYVelocity();
                    recycleVelocityTracker();
                    mIsSelfFling = true;
                    flingScroll(0, yVelocity);
                }
                break;
        }
        super.onTouchEvent(event);
        return true;
    }

    @Override
    public void flingScroll(int vx, int vy) {
        mScroller.fling(0, getScrollY(), 0, vy, 0, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        invalidate();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        recycleVelocityTracker();
        stopScroll();
        mChildHelper = null;
        mScroller = null;
        mParentView = null;
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            final int currY = mScroller.getCurrY();
            if (!mIsSelfFling) {
                // parent flying
                scrollTo(0, currY);
                invalidate();
                return;
            }

            if (isWebViewCanScroll()) {
                scrollTo(0, currY);
                invalidate();
            }
            if (!mHasFling
                    && mScroller.getStartY() < currY
                    && !canScrollDown()//滚动到底了
                    && startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL)//垂直滚动
                    && !dispatchNestedPreFling(0, mScroller.getCurrVelocity())) {
                //滑动到底部时，将fling传递给父控件和RecyclerView
                mHasFling = true;
                dispatchNestedFling(0, mScroller.getCurrVelocity(), false);
            }
        }
    }

    @Override
    public void scrollTo(int x, int y) {
        if (y < 0) {
            y = 0;
        }
        if (mMaxScrollY != 0 && y > mMaxScrollY) {
            y = mMaxScrollY;
        }
        if (isParentResetScroll()) {
            super.scrollTo(x, y);
        }
    }

    void scrollToBottom() {
        int y = getWebViewContentHeight();
        super.scrollTo(0, y - getHeight());
    }

    private NestedScrollingChildHelper getNestedScrollingHelper() {
        if (mChildHelper == null) {
            mChildHelper = new NestedScrollingChildHelper(this);
        }
        return mChildHelper;
    }

    private void initOrResetVelocityTracker() {
        //滑动速度跟踪器VelocityTracker, 这个类可以用来监听手指移动改变的速度;
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        } else {
            mVelocityTracker.clear();
        }
    }

    private void initVelocityTrackerIfNotExists() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
    }

    private void recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    private void initWebViewParent() {
        if (this.mParentView != null) {
            return;
        }
        View parent = (View) getParent();
        while (parent != null) {
            if (parent instanceof NestedScrollingDetailContainer) {
                this.mParentView = (NestedScrollingDetailContainer) parent;
                break;
            } else {
                parent = (View) parent.getParent();
            }
        }
    }

    private boolean isParentResetScroll() {
        if (mParentView == null) {
            initWebViewParent();
        }
        if (mParentView != null) {
            return mParentView.getScrollY() == 0;
        }
        return true;
    }

    private void stopScroll() {
        if (mScroller != null && !mScroller.isFinished()) {
            mScroller.abortAnimation();
        }
    }

    private boolean isWebViewCanScroll() {
        return getWebViewContentHeight() > getHeight();
    }

    /****** NestedScrollingChild BEGIN ******/
    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        getNestedScrollingHelper().setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        return getNestedScrollingHelper().isNestedScrollingEnabled();
    }

    @Override
    public boolean startNestedScroll(int axes) {
        return getNestedScrollingHelper().startNestedScroll(axes);
    }

    @Override
    public void stopNestedScroll() {
        getNestedScrollingHelper().stopNestedScroll();
    }

    @Override
    public boolean hasNestedScrollingParent() {
        return getNestedScrollingHelper().hasNestedScrollingParent();
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, @Nullable int[] consumed, @Nullable int[] offsetInWindow) {
        return getNestedScrollingHelper().dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, @Nullable int[] offsetInWindow) {
        return getNestedScrollingHelper().dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return getNestedScrollingHelper().dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return getNestedScrollingHelper().dispatchNestedPreFling(velocityX, velocityY);
    }

    @Override
    public boolean startNestedScroll(int axes, int type) {
        return getNestedScrollingHelper().startNestedScroll(axes, type);
    }

    @Override
    public void stopNestedScroll(int type) {
        getNestedScrollingHelper().stopNestedScroll(type);
    }

    @Override
    public boolean hasNestedScrollingParent(int type) {
        return getNestedScrollingHelper().hasNestedScrollingParent(type);
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, @Nullable int[] offsetInWindow, int type) {
        return getNestedScrollingHelper().dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow, type);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, @Nullable int[] consumed, @Nullable int[] offsetInWindow, int type) {
        return getNestedScrollingHelper().dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow, type);
    }
}

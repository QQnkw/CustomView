package com.nkw.customview.view;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class DiamondLayoutManager extends RecyclerView.LayoutManager {

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        return layoutParams;
    }

    private int mVerticalOffset;//竖直偏移量 每次换行时，要根据这个offset判断
    private int mFirstVisiPos;//屏幕可见的第一个View的Position
    private int mLastVisiPos;//屏幕可见的最后一个View的Position

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        Log.d("NKW--->","onLayoutChildren");
        if (getItemCount() == 0) {//没有Item，界面空着吧
            detachAndScrapAttachedViews(recycler);//轻量回收
            return;
        }
        if (getChildCount() == 0 && state.isPreLayout()) {//state.isPreLayout()是支持动画的
            return;
        }
        //onLayoutChildren方法在RecyclerView 初始化时 会执行两遍onMeasure,onLayout
        detachAndScrapAttachedViews(recycler);
        //初始化
        mVerticalOffset = 0;
        mFirstVisiPos = 0;
        mLastVisiPos = getItemCount();
        //初始化时调用 填充childView
        fill(recycler, state);
    }

    private void fill(RecyclerView.Recycler recycler, RecyclerView.State state) {
        int topOffset = getPaddingTop();//布局时的上偏移
        int minPos = mFirstVisiPos;//初始化时，我们不清楚究竟要layout多少个子View，所以就假设从0~itemcount-1
        mLastVisiPos = getItemCount() - 1;
        addItemToLayout(recycler, topOffset, minPos);
    }

    @Override
    public boolean canScrollVertically() {
        return true;
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (dy == 0 || getChildCount() == 0) {
            return 0;
        }

        int realOffset = dy;//实际滑动的距离， 可能会在边界处被修复
        //边界修复代码
        if (mVerticalOffset + realOffset < 0) {//上边界
            realOffset = -mVerticalOffset;
        } else if (realOffset > 0) {//下边界
            //利用最后一个子View比较修正
            View lastChild = getChildAt(getChildCount() - 1);
            if (getPosition(lastChild) == getItemCount() - 1) {
                int gap = getHeight() - getDecoratedBottom(lastChild);
                if (gap > 0) {
                    realOffset = -gap;
                } else if (gap == 0) {
                    realOffset = 0;
                } else {
                    realOffset = Math.min(realOffset, -gap);
                }
            }
        }

        realOffset = fill(recycler, state, realOffset);
        mVerticalOffset += realOffset;//累加实际滑动距离
        offsetChildrenVertical(-realOffset);
        Log.d("NKW--->", "scrollVerticallyBy:dy--->" + dy+"<>realOffset--->"+realOffset);
        return realOffset;
    }

    private int fill(RecyclerView.Recycler recycler, RecyclerView.State state, int dy) {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (dy > 0) {
                if (getDecoratedBottom(child) - dy < 0) {
                    removeAndRecycleView(child, recycler);
                    mFirstVisiPos++;
                }
            } else if (dy < 0) {
                if (getDecoratedTop(child) - dy > getHeight()) {
                    removeAndRecycleView(child, recycler);
                    mLastVisiPos--;
                }
            }
        }

        if (dy >= 0) {
            int startAddViewPos = mFirstVisiPos;
            int top = 0;
            mLastVisiPos = getItemCount() - 1;
            if (getChildCount() > 0) {
                View child = getChildAt(getChildCount() - 1);//获取最后一个view
                startAddViewPos = getPosition(child) + 1;//获取最后一个view下一个的索引
                top = getDecoratedTop(child);
            }
            top += getWidth() / 4;
            addItemToLayout(recycler, top, startAddViewPos);
            //添加完后，判断是否已经没有更多的ItemView，并且此时屏幕仍有空白，则需要修正dy
            View lastChild = getChildAt(getChildCount() - 1);
            if (getPosition(lastChild) == getItemCount() - 1) {
                int gap = getHeight() - getDecoratedBottom(lastChild);
                if (gap > 0) {
                    dy -= gap;
                }
            }
        } else {
            if (getChildCount() > 0) {
                View firstView = getChildAt(0);
                if (getDecoratedTop(firstView) + getWidth() / 4 >= 0) {
                    mFirstVisiPos = 0;
                    int endAddViewPos = getPosition(firstView) - 1;
                    int leftOffset;
                    int width = getWidth();
                    for (int i = endAddViewPos; i >= mFirstVisiPos; i--) {
                        View child = recycler.getViewForPosition(i);
                        addView(child, 0);
                        View view = getChildAt(1);
                        measureChildWithMargins(child, 0, 0);
                        int rowNumOfChild = i % 3;
                        if (rowNumOfChild == 0) {
                            leftOffset = width / 4;
                            layoutDecoratedWithMargins(child, leftOffset, getDecoratedTop(view) - getWidth()
                                            / 4, leftOffset +
                                            getDecoratedMeasuredWidth(child),
                                    getDecoratedTop(view) + getWidth()
                                            / 4);
                        } else {
                            if (rowNumOfChild == 1) {
                                leftOffset = 0;
                                layoutDecoratedWithMargins(child, leftOffset, getDecoratedTop(view), leftOffset
                                                + getDecoratedMeasuredWidth(child)
                                        , getDecoratedBottom(view));
                            } else {
                                leftOffset = width / 2;
                                layoutDecoratedWithMargins(child, leftOffset, getDecoratedTop(view) - getWidth()
                                                / 4, leftOffset +
                                                getDecoratedMeasuredWidth(child),
                                        getDecoratedTop(view) + getWidth()
                                                / 4);
                            }
                        }
                        if (getDecoratedBottom(child) < 0) {
                            removeAndRecycleView(child, recycler);
                            mFirstVisiPos++;
                            break;
                        }
                    }
                }

            }
        }
        return dy;
    }


    private void addItemToLayout(RecyclerView.Recycler recycler, int topOffset, int minPos) {
        int leftOffset;
        int width = getWidth();
        for (int i = minPos; i <=mLastVisiPos; i++) {
            View child = recycler.getViewForPosition(i);
            addView(child);
            measureChildWithMargins(child, 0, 0);
            if (topOffset> getHeight()) {
                removeAndRecycleView(child, recycler);//回收到回收池里
                mLastVisiPos = i - 1;
                continue;
            }
            int rowNumOfChild = i % 3;
            if (rowNumOfChild == 0) {
                leftOffset = width / 4;
                layoutDecoratedWithMargins(child, leftOffset, topOffset, leftOffset + getDecoratedMeasuredWidth(child),
                        topOffset + getDecoratedMeasuredHeight(child));
                topOffset += getDecoratedMeasuredHeight(child) / 2;
            } else {
                if (rowNumOfChild == 1) {
                    leftOffset = 0;
                    layoutDecoratedWithMargins(child, leftOffset, topOffset, leftOffset + getDecoratedMeasuredWidth(child)
                            , topOffset + getDecoratedMeasuredHeight(child));
                } else {
                    leftOffset = width / 2;
                    layoutDecoratedWithMargins(child, leftOffset, topOffset, leftOffset + getDecoratedMeasuredWidth(child)
                            , topOffset + getDecoratedMeasuredHeight(child));
                    topOffset += getDecoratedMeasuredHeight(child) / 2;
                }
            }
        }
    }
}

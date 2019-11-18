package com.nkw.customview.view.NineGridImage;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

public class NineGridImageLayout extends ViewGroup {
    private final String TAG = NineGridImageLayout.class.getSimpleName();
    private float mSingleImageVerticalPer = 0.6f;//单图的图片宽小于高时,图片宽占本布局的比例
    private float mSingleImageVerticalWidthHeightPer = 0.84f;//单图的图片宽小于高时,图片宽比高的比例
    private float mSingleImageHorizontalPer = 0.84f;//单图的图片宽大于高时,图片宽占本布局的比例
    private float mSingleImageHorizontalWidthHeightPer = 1.75f;//单图的图片宽大于高时,图片宽比高的比例
    private float mMoreImageSpacePer = 0.012f;//多图时,间隙占本布局宽度的比值
    private int mItemWidth = 0;//条目宽
    private int mItemHeight = 0;//条目高
    private int mMaxWidth;//去除padding最大宽度
    private float mSpaceWidth;//多图时的图片间隙宽度
    private int mChildRow;//将展示的行数
    private int mChildColumn;//将展示的列数
    private BaseNineGridImageAdapter mAdapter;//内部view的适配器
    private AdapterObserver mDataSetObserver;//观察者
    // 缓存器，存储 item，用于复用
    private List<SoftReference<TypeImageView>> mCacheViewList;
    private OnImageItemClickListener mOnImageItemClickListener;

    public NineGridImageLayout(Context context) {
        super(context);
        init(context, null);
    }

    public NineGridImageLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public NineGridImageLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measureHeight = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (mAdapter == null || getChildCount() == 0) {
            setMeasuredDimension(measureWidth==MeasureSpec.EXACTLY?measureWidth:getPaddingStart()+getPaddingEnd(),
                    heightMode == MeasureSpec.EXACTLY ?
                    measureHeight :
                    getPaddingTop() + getPaddingBottom());
            return;
        }
        //以宽为基准
        mMaxWidth = measureWidth - getPaddingLeft() - getPaddingRight();
        mSpaceWidth = mMaxWidth * mMoreImageSpacePer;
        int childCount = getChildCount();
        if (childCount == 1) {
            // 如果只有一个 child ，且当前模式为九宫格模式
            View child = getChildAt(0);
            int onlyOneChildWms, onlyOneChildHms;
            float firstImagePer = mAdapter.getFirstImagePer();
            if (firstImagePer > 1) {
                //横向长图
                int childWidth = (int) (mMaxWidth * mSingleImageHorizontalPer);
                onlyOneChildWms = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY);
                mItemWidth = childWidth;

                int childHeight = (int) (childWidth / mSingleImageHorizontalWidthHeightPer);
                onlyOneChildHms = MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY);
                mItemHeight = childHeight;

            } else if (firstImagePer < 1) {
                //竖向长图
                int childWidth = (int) (mMaxWidth * mSingleImageVerticalPer);
                onlyOneChildWms = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY);
                mItemWidth = childWidth;

                int childHeight = (int) (childWidth / mSingleImageVerticalWidthHeightPer);
                onlyOneChildHms = MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY);
                mItemHeight = childHeight;
            } else {
                //正方形
                int childSize = (int) (mMaxWidth * mSingleImageHorizontalPer);
                onlyOneChildWms = onlyOneChildHms = MeasureSpec.makeMeasureSpec(childSize, MeasureSpec.EXACTLY);
                mItemWidth = mItemHeight = childSize;
            }
            child.measure(onlyOneChildWms, onlyOneChildHms);
        } else {
            //超过一张图
            //多图时获取图片尺寸
            int itemSize = (int) ((mMaxWidth - mSpaceWidth * 2) / 3);
            mItemWidth = mItemHeight = itemSize;
            int measureSpec = MeasureSpec.makeMeasureSpec(itemSize, MeasureSpec.EXACTLY);
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if (child != null) {
                    child.measure(measureSpec, measureSpec);
                }
            }
        }
        int widthMeasure =
                getMeasureChildAfterWidthAndHeight(childCount)[0] + getPaddingLeft() + getPaddingRight();
        int heightMeasure = getMeasureChildAfterWidthAndHeight(childCount)[1] + getPaddingTop() + getPaddingBottom();
        setMeasuredDimension(widthMode == MeasureSpec.EXACTLY ? measureWidth : widthMeasure, heightMode == MeasureSpec.EXACTLY ? measureHeight : heightMeasure);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (mAdapter == null || getChildCount() == 0) {
            return;
        }
        // 遍历 child，设置每个 child 所在的位置
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            int row = i / mChildColumn;//在第几行
            int column = i - row * mChildColumn;//在第几列
            int left = (int) (getPaddingLeft() + column * (mItemWidth + mSpaceWidth));
            int top = (int) (getPaddingTop() + row * (mItemHeight + mSpaceWidth));
            int right = left + mItemWidth;
            int bottom = top + mItemHeight;
            View child = getChildAt(i);
            child.layout(left, top, right, bottom);
        }
    }

    /**
     * 获取测量子类控件后的该布局的最小的宽度和高度
     *
     * @param childCount
     * @return
     */
    private int[] getMeasureChildAfterWidthAndHeight(int childCount) {
        int[] widthHeightArr = new int[2];
        // 只有一个 item 时
        View childAt = getChildAt(0);
        if (childCount == 1) {
            mChildRow = mChildColumn = 1;
            widthHeightArr[0] = childAt.getMeasuredWidth();
            widthHeightArr[1] = childAt.getMeasuredHeight();
            return widthHeightArr;
        } else {
            // 判断 item 的个数是否可以开平方（即判断是否可以将 itemView 拼成正方形放置）
            int temp = (int) Math.sqrt(childCount);
            Log.d(TAG,"getMeasureChildAfterWidthAndHeight--->childCount:"+childCount+"---temp:"+temp);
            if (temp * temp == childCount) {
                // 如果 item 的个数不满一行，则不做正方形放置，否则做正方形放置
                if (childCount <= 3) {
                    mChildRow = 1;
                    mChildColumn = childCount;
                } else {
                    mChildRow = temp;
                    mChildColumn = temp;
                }
            } else {
                mChildRow = childCount / 3 + (childCount % 3 == 0 ? 0 : 1);
                mChildColumn = 3;
            }
            widthHeightArr[0] = (int) (mChildColumn * childAt.getMeasuredWidth() + (mChildColumn - 1) * mSpaceWidth);
            widthHeightArr[1] = (int) (mChildRow * childAt.getMeasuredWidth() + (mChildRow - 1) * mSpaceWidth);
            return widthHeightArr;
        }
    }

    public void setAdapter(BaseNineGridImageAdapter adapter) {
        if (mAdapter != null && mDataSetObserver != null) {
            // 删除已经存在的观察者
            mAdapter.unregisterDataSetObserver(mDataSetObserver);
        }
        removeAllViews();

        if (mCacheViewList == null) {
            mCacheViewList = new ArrayList<>();
        } else {
            mCacheViewList.clear();
        }
        // 重置 adapter
        mAdapter = adapter;
        if (mAdapter != null) {
            mDataSetObserver = new AdapterObserver();
            // 注册观察者
            mAdapter.registerDataSetObserver(mDataSetObserver);
            if (mAdapter.getImageCount() > 0) {
                addItemViews();
            }
        }
    }

    /**
     * 添加 item
     */
    private void addItemViews() {
        int totalCount = mAdapter.getImageCount();
        int childCount = totalCount > 9 ? 9 : totalCount;
        for (int i = 0; i < childCount; i++) {
            createViewAndAddToParent(i);
        }
    }

    private void createViewAndAddToParent(int position) {
        TypeImageView typeImageView = new TypeImageView(getContext());
        String imageType = mAdapter.getImageType(position);
        typeImageView.setImageType(imageType);
        mAdapter.bindData(position, typeImageView.getImageView(), this);
        // 将新建的 item 存入列表中
        mCacheViewList.add(new SoftReference<TypeImageView>(typeImageView));
        addItemClickListener(typeImageView, position);
        /** 一定要有 LayoutParams ，addView 的时候，item 在 xml 中的 width 和 height 设置的值，都是没效果的（变成 wrap_content ），
         * 可能会出现控件的宽高显示偏差，所以必须设置此句）*/
        addView(typeImageView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    private void addItemClickListener(View view, final int position) {
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnImageItemClickListener != null) {
                    mOnImageItemClickListener.onImageItemClick(position, v);
                }
            }
        });
    }

    public interface OnImageItemClickListener {
        void onImageItemClick(int position, View view);
    }

    private class AdapterObserver extends DataSetObserver {

        @Override
        public void onChanged() {
            super.onChanged();
            notifyChanged();
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
        }
    }

    /**
     * 通知刷新
     */
    private void notifyChanged() {
        if (mAdapter != null) {
            int newSize = mAdapter.getImageCount();
            if (newSize == 0) {
                if (mCacheViewList != null) {
                    mCacheViewList.clear();
                }
            } else {
                if (mCacheViewList == null) {
                    mCacheViewList = new ArrayList<>();
                }
            }
            removeAllViews();
            for (int i = 0; i < newSize; i++) {
                /** 此处做简单的缓存复用处理 */
                if (mCacheViewList != null && mCacheViewList.size() > 0) {
                    boolean isAddItem = false;
                    for (SoftReference<TypeImageView> softReference : mCacheViewList) {
                        if (softReference != null && softReference.get() != null) {
                            TypeImageView typeImageView = (TypeImageView) softReference.get();
                            // 如果列表中的 item 还没有父容器，则复用该 item
                            if (typeImageView.getParent() == null) {
                                String imageType = mAdapter.getImageType(i);
                                typeImageView.setImageType(imageType);
                                mAdapter.bindData(i, typeImageView.getImageView(), this);
                                addItemClickListener(typeImageView, i);
                                /** 一定要有 LayoutParams ，addView 的时候，item 在 xml 中的 width 和 height 设置的值，都是没效果的（变成 wrap_content ），
                                 * 可能会出现控件的宽高显示偏差，所以必须设置此句）*/
                                addView(typeImageView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                                isAddItem = true;
                                mCacheViewList.remove(softReference);
                                break;
                            }
                        }
                    }
                    // 如果从 mCacheViewList 中未找到匹配的 item ，则新建 item
                    if (!isAddItem) {
                        createViewAndAddToParent(i);
                    }
                } else {
                    createViewAndAddToParent(i);
                }
            }
        }
        requestLayout();
        invalidate();
    }

    @Override
    protected void onDetachedFromWindow() {
        if (mAdapter != null) {
            if (mDataSetObserver != null) {
                mAdapter.unregisterDataSetObserver(mDataSetObserver);
            }
            mAdapter = null;
        }
        if (mCacheViewList != null) {
            mCacheViewList.clear();
            mCacheViewList = null;
        }
        mOnImageItemClickListener = null;
        super.onDetachedFromWindow();
    }
}

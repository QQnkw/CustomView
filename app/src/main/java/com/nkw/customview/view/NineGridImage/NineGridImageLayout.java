package com.nkw.customview.view.NineGridImage;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class NineGridImageLayout extends ViewGroup {
    private final String TAG = NineGridImageLayout.class.getSimpleName();
    private int mItemWidth = 0;//条目宽
    private int mItemHeight = 0;//条目高
    private float mSpaceWidth;//多图时的图片间隙宽度
    private int mChildRow;//将展示的行数
    private int mChildColumn;//将展示的列数
    private int[] mChildAndSpaceSumWidthHeight = new int[2];//所有子空间加间隙的总宽高
    private List<TypeImageView> mNormalCacheViewList;
    private float mSingleImageType = 1;//1矩形,大于1横向长图,小于1纵向长图
    private OnImageLoadListener mOnImageLoadListener;
    private List<String> mImageDataList;
    private TypeImageView mHorizontalSingleTypeImageView;
    private TypeImageView mVerticalSingleTypeImageView;
    private TypeImageView mRectangleSingleTypeImageView;

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
        if (mOnImageLoadListener == null || getChildCount() == 0) {
            setMeasuredDimension(widthMode == MeasureSpec.EXACTLY ? measureWidth : getPaddingStart() + getPaddingEnd(),
                    heightMode == MeasureSpec.EXACTLY ?
                            measureHeight :
                            getPaddingTop() + getPaddingBottom());
            return;
        }
        //以宽为基准,去除padding最大宽度
        int maxWidth = measureWidth - getPaddingLeft() - getPaddingRight();
        //0.012多图时,间隙占本布局宽度的比值
        mSpaceWidth = maxWidth * 0.012f;
        int childCount = getChildCount();
        if (childCount == 1) {
            // 如果只有一个 child ，且当前模式为九宫格模式
            View child = getChildAt(0);
            int onlyOneChildWms, onlyOneChildHms;
            if (mSingleImageType > 1) {
                //横向长图,//0.84单图的图片宽大于高时,图片宽占本布局的比例
                int childWidth = (int) (maxWidth * 0.84f);
                onlyOneChildWms = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY);
                mItemWidth = childWidth;
                //1.75单图的图片宽大于高时,图片宽比高的比例
                int childHeight = (int) (childWidth / 1.75f);
                onlyOneChildHms = MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY);
                mItemHeight = childHeight;

            } else if (mSingleImageType < 1) {
                //竖向长图,//0.6单图的图片宽小于高时,图片宽占本布局的比例
                int childWidth = (int) (maxWidth * 0.6f);
                onlyOneChildWms = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY);
                mItemWidth = childWidth;
                //0.84单图的图片宽小于高时,图片宽比高的比例
                int childHeight = (int) (childWidth / 0.84f);
                onlyOneChildHms = MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY);
                mItemHeight = childHeight;
            } else {
                //正方形
                int childSize = (int) (maxWidth * 0.84f);
                onlyOneChildWms = onlyOneChildHms = MeasureSpec.makeMeasureSpec(childSize, MeasureSpec.EXACTLY);
                mItemWidth = mItemHeight = childSize;
            }
            child.measure(onlyOneChildWms, onlyOneChildHms);
        } else {
            //超过一张图
            //多图时获取图片尺寸
            int itemSize = (int) ((maxWidth - mSpaceWidth * 2) / 3);
            mItemWidth = mItemHeight = itemSize;
            int measureSpec = MeasureSpec.makeMeasureSpec(itemSize, MeasureSpec.EXACTLY);
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if (child != null) {
                    child.measure(measureSpec, measureSpec);
                }
            }
        }
        computeChildAndSpaceSumWidthHeight(childCount);
        int widthMeasure = mChildAndSpaceSumWidthHeight[0] + getPaddingLeft() + getPaddingRight();
        int heightMeasure = mChildAndSpaceSumWidthHeight[1] + getPaddingTop() + getPaddingBottom();
        setMeasuredDimension(widthMode == MeasureSpec.EXACTLY ? measureWidth : widthMeasure, heightMode == MeasureSpec.EXACTLY ? measureHeight : heightMeasure);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (mOnImageLoadListener == null || getChildCount() == 0) {
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
    private void computeChildAndSpaceSumWidthHeight(int childCount) {
        // 只有一个 item 时
        View childAt = getChildAt(0);
        if (childCount == 1) {
            mChildRow = mChildColumn = 1;
            mChildAndSpaceSumWidthHeight[0] = childAt.getMeasuredWidth();
            mChildAndSpaceSumWidthHeight[1] = childAt.getMeasuredHeight();
        } else {
            // 判断 item 的个数是否可以开平方（即判断是否可以将 itemView 拼成正方形放置）
            int temp = (int) Math.sqrt(childCount);
            Log.d(TAG, "getMeasureChildAfterWidthAndHeight--->childCount:" + childCount + "---temp:" + temp);
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
                mChildColumn = childCount < 3 ? childCount : 3;
            }
            mChildAndSpaceSumWidthHeight[0] = (int) (mChildColumn * childAt.getMeasuredWidth() + (mChildColumn - 1) * mSpaceWidth);
            mChildAndSpaceSumWidthHeight[1] = (int) (mChildRow * childAt.getMeasuredWidth() + (mChildRow - 1) * mSpaceWidth);
        }
    }

    public void changeChildLayoutAndLoadImageListener(List<String> imageDataList, OnImageLoadListener onImageLoadListener) {
        mOnImageLoadListener = onImageLoadListener;
        mImageDataList = imageDataList;
        if (imageDataList == null || imageDataList.isEmpty()) {
            //数据空,移除所有的子控件
            removeAllViews();
            requestLayout();
            invalidate();
        } else {
            refreshDataAndLayout();
        }
    }

    private void refreshDataAndLayout() {
        if (mOnImageLoadListener != null) {
            int dataSize = mImageDataList.size();
            if (dataSize > 1) {
                if (mNormalCacheViewList == null) {
                    mNormalCacheViewList = new ArrayList<>();
                }
                dataSize = dataSize > 9 ? 9 : dataSize;
                if (dataSize == getChildCount()) {
                    //数据源的数量和子类控件数量一致
                    for (int i = 0; i < dataSize; i++) {
                        TypeImageView childAt = (TypeImageView) getChildAt(i);
                        String imageType = mOnImageLoadListener.getImageType(i);
                        childAt.setImageType(imageType);
                        mOnImageLoadListener.bindData(i, childAt.getImageView(), this);
                        addItemClickListener(childAt,i);
                    }
                } else {
                    //不一样移除所有子控件
                    removeAllViews();
                    for (int i = 0; i < dataSize; i++) {
                        TypeImageView typeImageView = getNormalItemView(i);
                        /** 一定要有 LayoutParams ，addView 的时候，item 在 xml 中的 width 和 height 设置的值，都是没效果的（变成 wrap_content ），
                         * 可能会出现控件的宽高显示偏差，所以必须设置此句）*/
                        addView(typeImageView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                        String imageType = mOnImageLoadListener.getImageType(i);
                        typeImageView.setImageType(imageType);
                        mOnImageLoadListener.bindData(i, typeImageView.getImageView(), this);
                        addItemClickListener(typeImageView,i);
                    }
                }
            } else {
                removeAllViews();
                //只有一张图片
                TypeImageView typeImageView;
                mSingleImageType = mOnImageLoadListener.getFirstImagePer();
                if (mSingleImageType > 1) {
                    if (mHorizontalSingleTypeImageView == null) {
                        typeImageView = new TypeImageView(getContext());
                        mHorizontalSingleTypeImageView = typeImageView;
                    } else {
                        typeImageView = mHorizontalSingleTypeImageView;
                    }
                } else if (mSingleImageType < 1) {
                    if (mVerticalSingleTypeImageView == null) {
                        typeImageView = new TypeImageView(getContext());
                        mVerticalSingleTypeImageView = typeImageView;
                    } else {
                        typeImageView = mVerticalSingleTypeImageView;
                    }
                } else {
                    if (mRectangleSingleTypeImageView == null) {
                        typeImageView = new TypeImageView(getContext());
                        mRectangleSingleTypeImageView = typeImageView;
                    } else {
                        typeImageView = mRectangleSingleTypeImageView;
                    }
                }
                addView(typeImageView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                typeImageView.setImageType(mOnImageLoadListener.getImageType(0));
                mOnImageLoadListener.bindData(0, typeImageView.getImageView(), this);
                addItemClickListener(typeImageView,0);
            }
            Log.d(TAG, "refreshDataAndLayout--->加载图片");
            requestLayout();
            invalidate();
        }
    }

    private TypeImageView getNormalItemView(int i) {
        TypeImageView typeImageView;
        if (mNormalCacheViewList.size() > i) {
            typeImageView = mNormalCacheViewList.get(i);
        } else {
            typeImageView = new TypeImageView(getContext());
            mNormalCacheViewList.add(typeImageView);
        }
        return typeImageView;
    }
    /**
     * 设置 item 的点击监听事件
     *
     * @param view
     * @param position
     */
    private void addItemClickListener(View view, final int position) {
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnImageLoadListener != null) {
                    mOnImageLoadListener.onImageItemClick(position, v);
                }
            }
        });
    }
    public interface OnImageLoadListener {
        /**
         * 点击条目监听
         *
         * @param position
         * @param view
         */
        void onImageItemClick(int position, View view);

        /**
         * 获取第一张图片宽高的比例
         *
         * @return
         */
        float getFirstImagePer();

        /**
         * 获取图片的类型
         *
         * @param position
         * @return
         */
        String getImageType(int position);

        /**
         * 绑定数据
         *
         * @param position
         * @param imageView
         * @param nineGridImageLayout
         */
        void bindData(int position, ImageView imageView, NineGridImageLayout nineGridImageLayout);
    }
}

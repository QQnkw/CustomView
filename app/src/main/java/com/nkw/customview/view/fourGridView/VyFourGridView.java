package com.nkw.customview.view.fourGridView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nkw.customview.utils.SizeUtils;

import java.util.ArrayList;
import java.util.List;

public class VyFourGridView extends ViewGroup {
    private ArrayList<TextSignImageView> mImageViewArrayList = new ArrayList<>();
    private int                          gridWidth           = 0;
    private int                          gridHeight          = 0;
    private int              firstImgWidth;
    private int              firstImgHeight;
    private int              mGap;
    private List<String>     mImgUrlList;
    private ImageDisplayUtil mImageDisplayUtil;
    private boolean mLoadImage = false;

    public VyFourGridView(Context context) {
        this(context, null);
    }

    public VyFourGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VyFourGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mGap = SizeUtils.dp2px(10);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = 0;
        int size = getChildCount();
        int totalWidth = width - getPaddingLeft() - getPaddingRight();
        if (size == 1) {
            firstImgWidth = totalWidth;
            firstImgHeight = totalWidth / 2;
            height = firstImgHeight + getPaddingBottom() + getPaddingTop();
            int imgWidthMeasureSpec = MeasureSpec.makeMeasureSpec(firstImgWidth, MeasureSpec.EXACTLY);
            int imgHeightMeasureSpec = MeasureSpec.makeMeasureSpec(firstImgHeight, MeasureSpec.EXACTLY);
            measureChildren(imgWidthMeasureSpec, imgHeightMeasureSpec);
        } else if (size == 2) {
            gridWidth = (totalWidth - mGap) / 2;
            gridHeight = gridWidth;
            height = gridHeight + getPaddingBottom() + getPaddingTop();
            int imgWidthMeasureSpec = MeasureSpec.makeMeasureSpec(gridWidth, MeasureSpec.EXACTLY);
            int imgHeightMeasureSpec = MeasureSpec.makeMeasureSpec(gridHeight, MeasureSpec.EXACTLY);
            measureChildren(imgWidthMeasureSpec, imgHeightMeasureSpec);
        } else if (size == 3) {
            gridWidth = (totalWidth - mGap * 2) / 3;
            gridHeight = gridWidth;
            height = gridHeight + getPaddingBottom() + getPaddingTop();
            int imgWidthMeasureSpec = MeasureSpec.makeMeasureSpec(gridWidth, MeasureSpec.EXACTLY);
            int imgHeightMeasureSpec = MeasureSpec.makeMeasureSpec(gridHeight, MeasureSpec.EXACTLY);
            measureChildren(imgWidthMeasureSpec, imgHeightMeasureSpec);
        } else {
            firstImgWidth = totalWidth;
            firstImgHeight = totalWidth / 2;
            int firstWidthMeasureSpec = MeasureSpec.makeMeasureSpec(firstImgWidth, MeasureSpec.EXACTLY);
            int firstHeightMeasureSpec = MeasureSpec.makeMeasureSpec(firstImgHeight, MeasureSpec.EXACTLY);
            measureChild(getChildAt(0), firstWidthMeasureSpec, firstHeightMeasureSpec);

            gridWidth = (totalWidth - mGap * 2) / 3;
            gridHeight = gridWidth;
            int otherWidthMeasureSpec = MeasureSpec.makeMeasureSpec(gridWidth, MeasureSpec.EXACTLY);
            int otherHeightMeasureSpec = MeasureSpec.makeMeasureSpec(gridHeight, MeasureSpec.EXACTLY);
            for (int i = 1; i < 4; i++) {
                measureChild(getChildAt(i), otherWidthMeasureSpec, otherHeightMeasureSpec);
            }
            height = gridHeight + firstImgHeight + getPaddingBottom() + getPaddingTop() + mGap;
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childrenCount = getChildCount();
        if (childrenCount == 1 || childrenCount >= 4) {
            View firstView = getChildAt(0);
            firstView.layout(getPaddingLeft(), getPaddingTop(), getPaddingLeft() + firstImgWidth, getPaddingTop() + firstImgHeight);
            if (childrenCount > 3) {
                for (int i = 1; i < childrenCount; i++) {
                    View otherView = getChildAt(i);
                    int left = (gridWidth + mGap) * (i - 1) + getPaddingLeft();
                    int top = getPaddingTop() + mGap + firstImgWidth / 2;
                    otherView.layout(left, top, left + gridWidth, top + gridHeight);
                }
            }
        } else {
            for (int i = 0; i < childrenCount; i++) {
                View childAt = getChildAt(i);
                int left = (gridWidth + mGap) * i + getPaddingLeft();
                int top = getPaddingTop();
                childAt.layout(left, top, left + gridWidth, top + gridHeight);
            }
        }
        if (mLoadImage) {
            for (int i = 0; i < mImgUrlList.size(); i++) {
                TextSignImageView iv = (TextSignImageView) getChildAt(i);
                mImageDisplayUtil.displayImage(iv, mImgUrlList.get(i));
            }
            mLoadImage = false;
        }
    }

    public void setVyFourGridViewData(List<String> imgUrlList, ImageDisplayUtil imageDisplayUtil) {
        if (imgUrlList == null || imgUrlList.isEmpty()) {
            setVisibility(GONE);
            return;
        } else {
            setVisibility(VISIBLE);
        }

        int imageCount = imgUrlList.size();
        if (imageCount > 4) {
            imageCount = 4;
        }
        int oldViewCount = getChildCount();
        int newViewCount = imageCount;
        if (oldViewCount > newViewCount) {
            removeViews(newViewCount, oldViewCount - newViewCount);
        } else if (oldViewCount < newViewCount) {
            for (int i = oldViewCount; i < newViewCount; i++) {
                TextSignImageView iv = getImageView(i);
                if (iv == null) {
                    return;
                }
                addView(iv, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            }
        }
        //修改最后一个条目，决定是否显示更多
        if (imgUrlList.size() > 4) {
            View child = getChildAt(4 - 1);
            TextSignImageView iv = (TextSignImageView) child;
            iv.setTextNum(imgUrlList.size() - 4, true);
            imgUrlList = imgUrlList.subList(0, 4);
        } else {
            int lastViewIndex = getChildCount() - 1;
            TextSignImageView iv = (TextSignImageView) getChildAt(lastViewIndex);
            iv.setTextNum(0, false);
        }
        mLoadImage = true;
        mImgUrlList = imgUrlList;
        mImageDisplayUtil = imageDisplayUtil;
        requestLayout();
    }

    private TextSignImageView getImageView(int i) {
        /** 获得 ImageView 保证了 ImageView 的重用 */
        TextSignImageView imageView;
        if (i < mImageViewArrayList.size()) {
            imageView = mImageViewArrayList.get(i);
            imageView.setTextNum(0, false);
        } else {
            imageView = new TextSignImageView(getContext());
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            mImageViewArrayList.add(imageView);
        }
        return imageView;
    }

    public interface ImageDisplayUtil {
        void displayImage(ImageView imageView, String imgUrl);
    }
}

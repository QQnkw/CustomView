package com.nkw.customview.view.fourGridView;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.nkw.customview.utils.SizeUtils;

import java.util.ArrayList;

public class ImageViewGroup extends FrameLayout {

    private ArrayList<TextSignImageView>   mImageViewList;
    private int                            mWidth      = 0;//父控件宽度
    private int                            mMediumGap  = 0;//中等图片间距
    private int                            mSmallGap   = 0;//小图片间距
    private int                            mMediumSize = 0;//中等图片尺寸
    private int                            mSmallSize  = 0;//小图片尺寸
    private ArrayList<String>              mPicUrl;
    private LoadImageLayoutSuccessListener mListener;

    public ImageViewGroup(@NonNull Context context) {
        this(context, null);
    }

    public ImageViewGroup(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageViewGroup(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mImageViewList = new ArrayList<>();
    }

    public ImageViewGroup(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        this(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mMediumGap = SizeUtils.dp2px(5);
        mSmallGap = SizeUtils.dp2px(2);
        mMediumSize = (mWidth - mMediumGap) / 2;
        mSmallSize = (mWidth - mSmallGap * 2) / 3;
        int childCount = getChildCount();
        int height = 0;
        switch (childCount) {
            case 1:
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                return;
            case 2:
                height = mMediumSize;
                break;
            case 3:
                height = mSmallSize;
                break;
            case 4:
                height = mMediumSize * 2 + mMediumGap;
                break;
            case 5:
                height = mMediumSize + mSmallSize + mSmallGap;
                break;
            case 6:
                height = mSmallSize * 2 + mSmallGap;
                break;
            default:
                break;
        }
        setMeasuredDimension(mWidth, height);
    }

    public void loadImageLayout(ArrayList<String> picUrl, LoadImageLayoutSuccessListener listener) {
        if (listener == null || picUrl == null) {
            setVisibility(GONE);
            return;
        }
        if (picUrl.isEmpty()) {
            setVisibility(GONE);
            return;
        }
        mPicUrl = picUrl;
        mListener = listener;
        int picUrlSize = picUrl.size();
        int childCount = getChildCount();
        Log.d("NKW--->","ImageViewGroup--->老的子控件数" + childCount);
        if (picUrlSize > 6) {
            //图片数量大于6
            if (childCount == 6) {
                //布局中正好有6个图片布局
            } else {
                //图片数大于布局中的控件数
                //布局中少于6个
                for (int i = childCount; i < 6; i++) {
                    addView(getImageView(i));
                }
            }
        } else {
            //图片数量小于等于6
            if (picUrlSize > childCount) {
                //图片数大于布局中的控件数
                for (int i = childCount; i < picUrlSize; i++) {
                    addView(getImageView(i));
                }
            } else if (picUrlSize == childCount) {
                //图片数量等于布局内的控件数

            } else {
                //图片数量小于布局内的控件数
                int removeViewNum = childCount - picUrlSize;
                removeViews(picUrlSize, removeViewNum);
            }

        }
        //加载图片
        childCount = getChildCount();
        Log.d("NKW--->","ImageViewGroup--->新的子控件数" + childCount);
        //当展示一张图片时,恢复控件状态
        if (childCount == 1) {
            getChildAt(0).setLayoutParams(generateDefaultLayoutParams());
        } else {
            getChildAt(0).setLayoutParams(new LayoutParams(mMediumSize,
                    mMediumSize));
        }
        Log.d("NKW--->","先走设置布局");
        requestLayout();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            if (childAt instanceof ImageView) {
                TextSignImageView iv = (TextSignImageView) childAt;
                listener.loadPicUrl(iv, picUrl.get(i));
                if (i == 5) {
                    if (picUrlSize > 6) {
                        iv.setTextNum(picUrlSize - 6, true);
                    } else {
                        iv.setTextNum(0, false);
                    }
                }
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.d("NKW--->","后走排版布局");
        int childCount = getChildCount();
        switch (childCount) {
            case 1:
                super.onLayout(changed, l, t, r, b);
                break;
            case 2:
                for (int i = 0; i < childCount; i++) {
                    View childAt = getChildAt(i);
                    int left = (i % 2) * (mMediumSize + mMediumGap);
                    int top = 0;
                    int right = left + mMediumSize;
                    int bottom = top + mMediumSize;
                    childAt.layout(left, top, right, bottom);
                }
                break;
            case 3:
                for (int i = 0; i < childCount; i++) {
                    View childAt = getChildAt(i);
                    int left = (i % 3) * (mSmallSize + mSmallGap);
                    int top = 0;
                    int right = left + mSmallSize;
                    int bottom = top + mSmallSize;
                    childAt.layout(left, top, right, bottom);
                }
                break;
            case 4:
                for (int i = 0; i < childCount; i++) {
                    View childAt = getChildAt(i);
                    int left = (i % 2) * (mMediumSize + mMediumGap);
                    int top = 0;
                    if (i > 1) {
                        top = mMediumSize + mMediumGap;
                    }
                    int right = left + mMediumSize;
                    int bottom = top + mMediumSize;
                    childAt.layout(left, top, right, bottom);
                }
                break;
            case 5:
                for (int i = 0; i < childCount; i++) {
                    View childAt = getChildAt(i);
                    int left = 0;
                    int top = 0;
                    int bottom = 0;
                    int right = 0;
                    if (i < 2) {
                        left = (i % 2) * (mMediumSize + mMediumGap);
                        right = left + mMediumSize;
                        bottom = top + mMediumSize;
                        childAt.layout(left, top, right, bottom);
                    } else {
                        left = ((i - 2) % 3) * (mSmallSize + mSmallGap);
                        top = mMediumSize + mSmallGap;
                        right = left + mSmallSize;
                        bottom = top + mSmallSize;
                        childAt.layout(left, top, right, bottom);
                    }
                }
                break;
            case 6:
                for (int i = 0; i < childCount; i++) {
                    View childAt = getChildAt(i);
                    int left = (i % 3) * (mSmallSize + mSmallGap);
                    int top = 0;
                    if (i > 2) {
                        top = mSmallGap + mSmallSize;
                    }
                    int right = left + mSmallSize;
                    int bottom = top + mSmallSize;
                    childAt.layout(left, top, right, bottom);
                }
                break;
            default:
                break;
        }
    }


    //获取图片控件
    private View getImageView(final int index) {
        final TextSignImageView imageView;
        if (index < mImageViewList.size()) {
            imageView = mImageViewList.get(index);
            imageView.setTextNum(0, false);
        } else {
            imageView = new TextSignImageView(getContext());
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onClickImage((ImageView) v, index, mPicUrl);
                    }
                }
            });
            mImageViewList.add(imageView);
        }
        return imageView;
    }

    public interface LoadImageLayoutSuccessListener {
        void loadPicUrl(ImageView imageView, String url);

        void onClickImage(ImageView imageView, int position, ArrayList<String> picUrlList);
    }
}

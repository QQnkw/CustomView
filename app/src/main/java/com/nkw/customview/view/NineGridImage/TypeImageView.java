package com.nkw.customview.view.NineGridImage;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class TypeImageView extends FrameLayout {

    private ImageView mImageView;
    private TextView mTextView;

    public TypeImageView(Context context) {
        super(context);
        initChild(context);
    }

    public TypeImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initChild(context);
    }

    public TypeImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initChild(context);
    }

    private void initChild(Context context) {
        mImageView = new ImageView(context);
        addView(mImageView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT));
        mTextView = new TextView(context);
        LayoutParams tvLayoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tvLayoutParams.gravity = Gravity.TOP | Gravity.END;
        addView(mTextView, tvLayoutParams);
    }

    public ImageView getImageView() {
        return mImageView;
    }

    /**
     * 设置图片的类型
     * @param imageType
     */
    public void setImageType(String imageType) {
        if ("动图".equals(imageType) || "长图".equals(imageType)) {
            mTextView.setVisibility(VISIBLE);
            mTextView.setText(imageType);
        } else {
            mTextView.setVisibility(INVISIBLE);
        }
    }
}

package com.nkw.customview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.nkw.customview.R;

public class AngleImageView extends AppCompatImageView {
    private int mAngle;
    private Matrix mMatrix;
    private int mImageWidth;//图片原始宽度
    private int mImageHeight;//图片高度
    private double mScaleImageWidth;//图片放大后的宽度
    private double mScaleImageHeight;//图片放大后的高度
    private int mViewDisplayWidth;//控件宽度
    private int mViewDisplayHeight;//控件高度

    public AngleImageView(Context context) {
        this(context, null);
    }

    public AngleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AngleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AngleImageView);
        try {
            mAngle = typedArray.getInt(R.styleable.AngleImageView_angle, 30);
            if (mAngle > 360) {
                mAngle = 360;
            } else if (mAngle < -360) {
                mAngle = -360;
            }
        } finally {
            typedArray.recycle();
        }
        init();
    }

    private void init() {
        super.setScaleType(ScaleType.MATRIX);
        mMatrix = new Matrix();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //获取真实显示宽高
        mViewDisplayWidth = w - getPaddingStart() - getPaddingEnd();
        mViewDisplayHeight = h - getPaddingTop() - getPaddingBottom();
        computeScaleImageSize();
        updateAll();
    }

    private void computeScaleImageSize() {
        if (mViewDisplayWidth == 0 || mViewDisplayHeight == 0) {
            return;
        }
        double radians = Math.toRadians(Math.abs(mAngle));
        mScaleImageWidth = mViewDisplayWidth * Math.cos(radians) + mViewDisplayHeight * Math.sin(radians);
        mScaleImageHeight = mViewDisplayHeight * Math.cos(radians) + mViewDisplayWidth * Math.sin(radians);
    }

    private void updateAll() {
        if (getDrawable() != null) {
            updateImageSize();
            computeImageScale();
        }
    }

    /**
     * 获取图片宽高
     */
    private void updateImageSize() {
        mImageWidth = getDrawable().getIntrinsicWidth();
        mImageHeight = getDrawable().getIntrinsicHeight();
    }

    /**
     * 不要重写该方法
     */
    @Override
    @Deprecated
    public void setScaleType(ScaleType scaleType) {
        //super.setScaleType(scaleType);
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        updateAll();
    }

    @Override
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        updateAll();
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        updateAll();
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        updateAll();
    }

    /**
     * 计算图片的缩放比例,并调整图片大小
     *
     * @return
     */

    private void computeImageScale() {
        if (mMatrix == null) {
            return;
        }
        mMatrix.reset();
        //获取要显示的图片尺寸和控件宽高两边的比值
        float sW = (float) (mScaleImageWidth / mImageWidth);
        float sH = (float) (mScaleImageHeight / mImageHeight);
        float scaleValue = Math.max(sW, sH);
        mMatrix.setScale(scaleValue, scaleValue);
        double dx = (mViewDisplayWidth - mImageWidth * scaleValue) * 0.5f;
        double dy = (mViewDisplayHeight - mImageHeight * scaleValue) * 0.5f;
        mMatrix.postTranslate(Math.round(dx), Math.round(dy));
        mMatrix.postRotate(mAngle, mViewDisplayWidth * 1.0f / 2, mViewDisplayHeight * 1.0f / 2);
        setImageMatrix(mMatrix);
    }
}

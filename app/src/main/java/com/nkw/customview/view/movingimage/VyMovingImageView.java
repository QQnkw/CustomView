package com.nkw.customview.view.movingimage;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.nkw.customview.R;

public class VyMovingImageView extends android.support.v7.widget.AppCompatImageView {
    private final String TAG = VyMovingImageView.class.getSimpleName();

    private int mMoveSpeed;//每秒移动像素
    private boolean mAutoMove;//是否自动移动
    private int mDisplayWidth;//真实显示内容宽度
    private int mDisplayHeight;//真实显示内容高度
    private int mImageWidth;//图片宽度
    private int mImageHeight;//图片高度
    private Matrix mMatrix;//图形变换矩阵
    private final int VERTICAL_MOVE = 1;
    private final int HORIZONTAL_MOVE = 2;
    private final int NONE_MOVE = 0;
    private int mMoveType = NONE_MOVE;//移动类型
    private ObjectAnimator mObjectAnimator;

    public VyMovingImageView(Context context) {
        this(context, null);
    }

    public VyMovingImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VyMovingImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.VyMovingImageView);
        try {
            mMoveSpeed = typedArray.getInt(R.styleable.VyMovingImageView_move_speed, 50);
            mAutoMove = typedArray.getBoolean(R.styleable.VyMovingImageView_auto_move, true);
        } finally {
            typedArray.recycle();
        }
        init();
    }

    private void init() {
        //设置矩阵模式
        super.setScaleType(ScaleType.MATRIX);
        mMatrix = new Matrix();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //获取真实显示宽高
        mDisplayWidth = w - getPaddingStart() - getPaddingEnd();
        mDisplayHeight = h - getPaddingTop() - getPaddingBottom();
        updateAll();
    }

    private void updateAll() {
        if (getDrawable() != null) {
            updateImageSize();
            updateAnimator();
        }
    }

    /**
     * 更新动画
     */
    private void updateAnimator() {
        if (mDisplayWidth == 0 && mDisplayHeight == 0) {
            return;
        }
        float scaleValue = computeImageScaleValue();
        if (scaleValue == 0) {
            return;
        }

        float widthOffset = (mImageWidth * scaleValue) - mDisplayWidth;
        float heightOffset = (mImageHeight * scaleValue) - mDisplayHeight;
        String scrollOrientation;
        int offset;
        long animatorTime;
        switch (mMoveType) {
            case VERTICAL_MOVE:
                animatorTime = (long) ((heightOffset / mMoveSpeed) * 1000);
                scrollOrientation = "scrollY";
                offset = (int) heightOffset;
                break;
            case HORIZONTAL_MOVE:
                animatorTime = (long) ((widthOffset / mMoveSpeed) * 1000);
                scrollOrientation = "scrollX";
                offset = (int) widthOffset;
                break;
            default:
                return;
        }
        if (mObjectAnimator == null) {
            mObjectAnimator = ObjectAnimator
                    .ofInt(this, scrollOrientation, 0, offset + 1)
                    .setDuration(animatorTime);
            mObjectAnimator.setRepeatCount(ValueAnimator.INFINITE);
            mObjectAnimator.setRepeatMode(ValueAnimator.REVERSE);
            mObjectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        } else {
            mObjectAnimator.setTarget(this);
            mObjectAnimator.setPropertyName(scrollOrientation);
            mObjectAnimator.setIntValues(0, (offset + 1));
            mObjectAnimator.setDuration(animatorTime);
        }
        if (mAutoMove) {
            mObjectAnimator.start();
        }
    }

    /**
     * 计算图片的缩放比例,并调整图片大小
     *
     * @return
     */

    private float computeImageScaleValue() {
        float scaleValue;
        mMatrix.reset();
        //图片尺寸等于或小于控件尺寸,需要放大
        //获取图片和控件宽高两边的比值
        float sW = mDisplayWidth * 1.0f / mImageWidth;
        float sH = mDisplayHeight * 1.0f / mImageHeight;
        if (sW > sH) {
            //以宽为比例缩放图片,上下移动
            mMoveType = VERTICAL_MOVE;
            scaleValue = sW;
        } else if (sW < sH) {
            //以高为比例缩放图片,左右移动
            mMoveType = HORIZONTAL_MOVE;
            scaleValue = sH;
        } else {
            //图片的形状和控件形状一致
            if (sW == 1.0f) {
                //图片尺寸和控件尺寸一致,不移动
                scaleValue = 1.0f;
            } else {
                //缩放图片大小,不移动
                scaleValue = sW;
            }
            mMoveType = NONE_MOVE;
        }
        mMatrix.setScale(scaleValue, scaleValue);
        setImageMatrix(mMatrix);
        return scaleValue;
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

    public void startImageAnimator() {
        if (mObjectAnimator != null) {
            mObjectAnimator.start();
        }
    }

    public void cancelImageAnimator() {
        if (mObjectAnimator != null) {
            mObjectAnimator.cancel();
        }
    }

    public void pauseImageAnimator() {
        if (mObjectAnimator != null) {
            if (mObjectAnimator.isRunning()) {
                mObjectAnimator.pause();
            }
        }
    }

    public void resumeImageAnimator() {
        if (mObjectAnimator != null) {
            if (mObjectAnimator.isPaused()) {
                mObjectAnimator.resume();
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        cancelImageAnimator();
    }
}

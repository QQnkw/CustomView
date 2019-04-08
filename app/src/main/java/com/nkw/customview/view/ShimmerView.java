package com.nkw.customview.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;

import com.nkw.customview.R;


public class ShimmerView extends View {
    private static final int DEFAULT_ANIMATION_DURATION = 1500;

    private static final byte DEFAULT_ANGLE = 20;

    private static final byte MIN_ANGLE_VALUE      = -45;
    private static final byte MAX_ANGLE_VALUE      = 45;
    private static final byte MIN_MASK_WIDTH_VALUE = 0;
    private static final byte MAX_MASK_WIDTH_VALUE = 1;

    private static final byte MIN_GRADIENT_CENTER_COLOR_WIDTH_VALUE = 0;
    private static final byte MAX_GRADIENT_CENTER_COLOR_WIDTH_VALUE = 1;

    private int           maskOffsetX;
    private Rect          maskRect;
    private Paint         gradientTexturePaint;
    private ValueAnimator maskAnimator;

    private boolean                            isAnimationReversed;
    private boolean                            isAnimationStarted;
    private boolean                            autoStart;
    private int                                shimmerAnimationDuration;
    private int                                shimmerColor;
    private int                                shimmerAngle;
    private float                              maskWidth;
    private float                              gradientCenterColorWidth;
    private ViewTreeObserver.OnPreDrawListener startAnimationPreDrawListener;

    public ShimmerView(Context context) {
        this(context, null);
    }

    public ShimmerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShimmerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public ShimmerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        this(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ShimmerView,
                0, 0);

        try {
            shimmerAngle = a.getInteger(R.styleable.ShimmerView_shimmer_angle, DEFAULT_ANGLE);
            shimmerAnimationDuration = a.getInteger(R.styleable.ShimmerView_shimmer_animation_duration, DEFAULT_ANIMATION_DURATION);
            shimmerColor = a.getColor(R.styleable.ShimmerView_shimmer_color, getColor(R.color.shimmer_color));
            autoStart = a.getBoolean(R.styleable.ShimmerView_shimmer_auto_start, false);
            maskWidth = a.getFloat(R.styleable.ShimmerView_shimmer_mask_width, 0.5F);
            gradientCenterColorWidth = a.getFloat(R.styleable.ShimmerView_shimmer_gradient_center_color_width, 0.1F);
            isAnimationReversed = a.getBoolean(R.styleable.ShimmerView_shimmer_reverse_animation, false);
        } finally {
            a.recycle();
        }
        setMaskWidth(maskWidth);
        setGradientCenterColorWidth(gradientCenterColorWidth);
        setShimmerAngle(shimmerAngle);
        if (autoStart && getVisibility() == VISIBLE) {
            startShimmerAnimation();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        resetShimmering();
        super.onDetachedFromWindow();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!isAnimationStarted || getWidth() <= 0 || getHeight() <= 0) {
        } else {
            createShimmerPaint();
            canvas.save();
            canvas.translate(maskOffsetX, 0);
            canvas.drawRect(maskRect.left, 0, maskRect.width(), maskRect.height(), gradientTexturePaint);
            canvas.restore();
        }
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (visibility == VISIBLE) {
            if (autoStart) {
                startShimmerAnimation();
            }
        } else {
            stopShimmerAnimation();
        }
    }

    public void startShimmerAnimation() {
        if (isAnimationStarted) {
            return;
        }

        if (getWidth() == 0) {
            startAnimationPreDrawListener = new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    getViewTreeObserver().removeOnPreDrawListener(this);
                    startShimmerAnimation();

                    return true;
                }
            };

            getViewTreeObserver().addOnPreDrawListener(startAnimationPreDrawListener);

            return;
        }

        Animator animator = getShimmerAnimation();
        animator.start();
        isAnimationStarted = true;
    }

    public void stopShimmerAnimation() {
        if (startAnimationPreDrawListener != null) {
            getViewTreeObserver().removeOnPreDrawListener(startAnimationPreDrawListener);
        }

        resetShimmering();
    }

    public void setShimmerColor(int shimmerColor) {
        this.shimmerColor = shimmerColor;
        resetIfStarted();
    }

    public void setShimmerAnimationDuration(int durationMillis) {
        this.shimmerAnimationDuration = durationMillis;
        resetIfStarted();
    }

    public void setAnimationReversed(boolean animationReversed) {
        this.isAnimationReversed = animationReversed;
        resetIfStarted();
    }

    /**
     * Set the angle of the shimmer effect in clockwise direction in degrees.
     * The angle must be between {@value #MIN_ANGLE_VALUE} and {@value #MAX_ANGLE_VALUE}.
     *
     * @param angle The angle to be set
     */
    public void setShimmerAngle(int angle) {
        if (angle < MIN_ANGLE_VALUE || MAX_ANGLE_VALUE < angle) {
            throw new IllegalArgumentException(String.format("shimmerAngle value must be between %d and %d",
                    MIN_ANGLE_VALUE,
                    MAX_ANGLE_VALUE));
        }
        this.shimmerAngle = angle;
        resetIfStarted();
    }

    /**
     * Sets the width of the shimmer line to a value higher than 0 to less or equal to 1.
     * 1 means the width of the shimmer line is equal to half of the width of the ShimmerLayout.
     * The default value is 0.5.
     *
     * @param maskWidth The width of the shimmer line.
     */
    public void setMaskWidth(float maskWidth) {
        if (maskWidth <= MIN_MASK_WIDTH_VALUE || MAX_MASK_WIDTH_VALUE < maskWidth) {
            throw new IllegalArgumentException(String.format("maskWidth value must be higher than %d and less or equal to %d",
                    MIN_MASK_WIDTH_VALUE, MAX_MASK_WIDTH_VALUE));
        }

        this.maskWidth = maskWidth;
        resetIfStarted();
    }

    /**
     * Sets the width of the center gradient color to a value higher than 0 to less than 1.
     * 0.99 means that the whole shimmer line will have this color with a little transparent edges.
     * The default value is 0.1.
     *
     * @param gradientCenterColorWidth The width of the center gradient color.
     */
    public void setGradientCenterColorWidth(float gradientCenterColorWidth) {
        if (gradientCenterColorWidth <= MIN_GRADIENT_CENTER_COLOR_WIDTH_VALUE
                || MAX_GRADIENT_CENTER_COLOR_WIDTH_VALUE <= gradientCenterColorWidth) {
            throw new IllegalArgumentException(String.format("gradientCenterColorWidth value must be higher than %d and less than %d",
                    MIN_GRADIENT_CENTER_COLOR_WIDTH_VALUE, MAX_GRADIENT_CENTER_COLOR_WIDTH_VALUE));
        }

        this.gradientCenterColorWidth = gradientCenterColorWidth;
        resetIfStarted();
    }

    private void resetIfStarted() {
        if (isAnimationStarted) {
            resetShimmering();
            startShimmerAnimation();
        }
    }

    private void resetShimmering() {
        if (maskAnimator != null) {
            maskAnimator.end();
            maskAnimator.removeAllUpdateListeners();
        }
        maskAnimator = null;
        gradientTexturePaint = null;
        isAnimationStarted = false;
    }

    private void createShimmerPaint() {
        if (gradientTexturePaint != null) {
            return;
        }

        final int edgeColor = reduceColorAlphaValueToZero(shimmerColor);
        final float shimmerLineWidth = getWidth() / 2 * maskWidth;
        final float yPosition = (0 <= shimmerAngle) ? getHeight() : 0;

        LinearGradient gradient = new LinearGradient(
                0, yPosition,
                (float) Math.cos(Math.toRadians(shimmerAngle)) * shimmerLineWidth,
                yPosition + (float) Math.sin(Math.toRadians(shimmerAngle)) * shimmerLineWidth,
                new int[]{edgeColor, shimmerColor, shimmerColor, edgeColor},
                getGradientColorDistribution(),
                Shader.TileMode.CLAMP);
        gradientTexturePaint = new Paint();
        gradientTexturePaint.setAntiAlias(true);
        gradientTexturePaint.setDither(true);
        gradientTexturePaint.setFilterBitmap(true);
        gradientTexturePaint.setShader(gradient);
    }

    private Animator getShimmerAnimation() {
        if (maskAnimator != null) {
            return maskAnimator;
        }

        if (maskRect == null) {
            maskRect = calculateBitmapMaskRect();
        }

        final int animationToX = getWidth();
        final int animationFromX;

        if (getWidth() > maskRect.width()) {
            animationFromX = -animationToX;
        } else {
            animationFromX = -maskRect.width();
        }

        final int shimmerBitmapWidth = maskRect.width();
        final int shimmerAnimationFullLength = animationToX - animationFromX;

        maskAnimator = isAnimationReversed ? ValueAnimator.ofInt(shimmerAnimationFullLength, 0)
                : ValueAnimator.ofInt(0, shimmerAnimationFullLength);
        maskAnimator.setDuration(shimmerAnimationDuration);
        maskAnimator.setRepeatCount(ObjectAnimator.INFINITE);

        maskAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                maskOffsetX = animationFromX + (int) animation.getAnimatedValue();

                if (maskOffsetX + shimmerBitmapWidth >= 0) {
                    invalidate();
                }
            }
        });

        return maskAnimator;
    }

    private int getColor(int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return getContext().getColor(id);
        } else {
            //noinspection deprecation
            return getResources().getColor(id);
        }
    }

    private int reduceColorAlphaValueToZero(int actualColor) {
        return Color.argb(0, Color.red(actualColor), Color.green(actualColor), Color.blue(actualColor));
    }

    private Rect calculateBitmapMaskRect() {
        return new Rect(0, 0, calculateMaskWidth(), getHeight());
    }

    private int calculateMaskWidth() {
        final double shimmerLineBottomWidth = (getWidth() / 2 * maskWidth) / Math.cos(Math.toRadians(Math.abs(shimmerAngle)));
        final double shimmerLineRemainingTopWidth = getHeight() * Math.tan(Math.toRadians(Math.abs(shimmerAngle)));

        return (int) (shimmerLineBottomWidth + shimmerLineRemainingTopWidth);
    }

    private float[] getGradientColorDistribution() {
        final float[] colorDistribution = new float[4];

        colorDistribution[0] = 0;
        colorDistribution[3] = 1;

        colorDistribution[1] = 0.5F - gradientCenterColorWidth / 2F;
        colorDistribution[2] = 0.5F + gradientCenterColorWidth / 2F;

        return colorDistribution;
    }
}

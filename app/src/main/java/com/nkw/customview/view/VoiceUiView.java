package com.nkw.customview.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import com.nkw.customview.R;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class VoiceUiView extends View {

    //默认列数
    private int mLineColumnNum;
    //线的颜色
    private int mLineColor;
    //线的高度集合
    private LinkedList<Float> mLineHeightList;
    //测量宽
    private int mMeasureWidth;
    //测量高
    private int mMeasureHeight;
    //每列的宽度
    private float mColumnWidth;
    //线的画笔
    private Paint mPaint;
    //自动标志
    private boolean isAutoRefresh = false;

    public VoiceUiView(Context context) {
        this(context, null);
    }

    public VoiceUiView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VoiceUiView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initProperty(context, attrs);
    }

    public VoiceUiView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        this(context, attrs, defStyleAttr);
    }

    private void initProperty(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.VoiceUiView);
        try {
            mLineColor = typedArray.getColor(R.styleable.VoiceUiView_line_color, Color.RED);
            mLineColumnNum = typedArray.getInt(R.styleable.VoiceUiView_line_column_num, 20);
        } finally {
            typedArray.recycle();
        }
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setColor(mLineColor);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        if (widthMode != MeasureSpec.EXACTLY) {
            mMeasureWidth = 400;
        } else {
            mMeasureWidth = MeasureSpec.getSize(widthMeasureSpec);
        }
        //包裹类型时高度是画笔宽度的倍数
        mMeasureHeight = (int) (mColumnWidth * 10);
        //设置大小
        setMeasuredDimension(mMeasureWidth, mMeasureHeight);
        //计算每列的宽度,因为中间有间隙,这里让间隙和列等宽
        mColumnWidth = mMeasureWidth / (mLineColumnNum * 2.0F);
        //设置画笔宽度
        mPaint.setStrokeWidth(mColumnWidth);
        if (mLineHeightList == null) {
            mLineHeightList = new LinkedList<>();
            for (int i = 0; i < mLineColumnNum; i++) {
                mLineHeightList.add(0.0F);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.translate(0, (mMeasureHeight / 2.0F));
        /*for (int i = 0; i < mColumn * 2; i++) {
            if (i % 2 != 0) {
                float x = i * mColumnWidth + mColumnWidth / 2;
                canvas.drawPoint(x, 0, mPaint);
            }
        }*/
        int count = 0;
        for (int i = 0; i < mLineColumnNum * 2; i++) {
            if (i % 2 != 0) {
                if (mLineHeightList.size() > count) {
                    float x = i * mColumnWidth + mColumnWidth / 2;
                    Float lineHeight = mLineHeightList.get(count);
                    canvas.drawLine(x, -lineHeight, x,
                            lineHeight, mPaint);
                }
                count++;
            }
        }
    }

    public void setLineData(float percentage) {
        float maxHeight = mMeasureHeight / 2.0F - mColumnWidth / 2.0F;
        float value = maxHeight * percentage;
        if (mColumnWidth / 2.0F > value) {
            value = 0F;
        }
        if (value > maxHeight) {
            value = maxHeight;
        }
        mLineHeightList.addFirst(value);
        mLineHeightList.removeLast();
        invalidate();
    }

    /**
     * 回到初始状态
     */
    public void resetLineHeight() {
        isAutoRefresh = false;
        final ArrayList<Float> cacheList = new ArrayList<>(mLineHeightList);
        ValueAnimator valueAnimator =
                ValueAnimator.ofFloat(1, 0).setDuration(200);
        valueAnimator.setInterpolator(new AccelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                for (int i = 0; i < cacheList.size(); i++) {
                    Float oldValue = cacheList.get(i);
                    float nowValue = oldValue * animatedValue;
                    mLineHeightList.set(i, nowValue);
                }
                invalidate();
            }
        });
        valueAnimator.start();
    }

    /**
     * 自动刷新数据
     */
    public void autoRefreshData() {
        isAutoRefresh = true;
        final Random random = new Random();
        postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isAutoRefresh) {
                    float percentage = (random.nextInt(100) + 1) / 100F;
                    setLineData(percentage);
                    postDelayed(this, 100);
                } else {
                    removeCallbacks(this);
                }
            }
        }, 100);
    }
}

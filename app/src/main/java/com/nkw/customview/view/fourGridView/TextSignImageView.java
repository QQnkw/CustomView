package com.nkw.customview.view.fourGridView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;

import com.nkw.customview.utils.SizeUtils;


public class TextSignImageView extends android.support.v7.widget.AppCompatImageView {

    private Paint  mPaint;
    private String mTextNum;
    private int    mCircleRadius;
    private int    mCircleCenterX;
    private int    mCircleCenterY;
    private Rect    mRect     = new Rect();
    private boolean mShowText = false;

    public TextSignImageView(Context context) {
        this(context, null);
    }

    public TextSignImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextSignImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mCircleRadius = SizeUtils.dp2px(12);
        initPaint(context);
    }

    public void setTextNum(int num,boolean showText) {
        if (num > 9) {
            num = 9;
        }
        mTextNum = "+" + num;
        mShowText = showText;
    }

    private void initPaint(Context context) {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mCircleCenterX = w - SizeUtils.dp2px(18);
        mCircleCenterY = h - SizeUtils.dp2px(18);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mShowText) {
            //背景
            mPaint.setColor(Color.parseColor("#252444"));
            canvas.drawCircle(mCircleCenterX, mCircleCenterY, mCircleRadius, mPaint);
            //文字
            mPaint.setColor(Color.WHITE);
            mPaint.setTextSize(SizeUtils.sp2px(12));
            mPaint.setTextAlign(Paint.Align.CENTER);
            mPaint.getTextBounds(mTextNum, 0, mTextNum.length(), mRect);
            canvas.drawText(mTextNum, mCircleCenterX, mCircleCenterY + mRect.height() / 2, mPaint);
        }
    }
}

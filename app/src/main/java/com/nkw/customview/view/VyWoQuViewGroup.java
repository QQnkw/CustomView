package com.nkw.customview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class VyWoQuViewGroup extends FrameLayout {
    private int mHeight;
    private int mWidth;
    private int mDesignWidth  = 375;
    private int mDesignHeight = 667;

    private Point[] mFirstLinePointArr   = {new Point(107, 203), new Point(127, 203),
            new Point(117, 209), new Point(117, 278),
            new Point(162, 310)};
    private Point[] mSecondLinePointArr  = {new Point(193, 95), new Point(193, 147),
            new Point(181, 155), new Point(181, 187),
            new Point(135, 213), new Point(135, 274),
            new Point(159, 289)};
    private Point[] mThirdLinePointArr   = {new Point(216, 124), new Point(216, 172),
            new Point(252, 192), new Point(252, 223),
            new Point(234, 234), new Point(234, 284),
            new Point(218, 293)};
    private Point[] mFourthLinePointArr  = {new Point(278, 174), new Point(298, 174),
            new Point(288, 180), new Point(288, 219),
            new Point(248, 242), new Point(248, 292),
            new Point(215, 312)};
    private Point[] mFifthLinePointArr   = {new Point(155, 344), new Point(85, 384),
            new Point(85, 476), new Point(75, 482),
            new Point(95, 482)};
    private Point[] mSixthLinePointArr   = {new Point(150, 367), new Point(108, 392),
            new Point(108, 473), new Point(177, 512),
            new Point(177, 568)};
    private Point[] mSeventhLinePointArr = {new Point(210, 379), new Point(233, 392),
            new Point(233, 442), new Point(243, 448),
            new Point(243, 480), new Point(206, 503),
            new Point(206, 547)};
    private Point[] mEighthLinePointArr  = {new Point(212, 359), new Point(277, 396),
            new Point(277, 469), new Point(267, 475),
            new Point(287, 475)};
    private Path  mLinePath;
    private Paint mLinePaint;
    private Paint mCirclePaint;

    public VyWoQuViewGroup(@NonNull Context context) {
        this(context, null);
    }

    public VyWoQuViewGroup(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VyWoQuViewGroup(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);

    }


    public VyWoQuViewGroup(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        this(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs) {
        setWillNotDraw(false);
        initPaint();
        mLinePath = new Path();
    }

    private void initPaint() {
        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setColor(Color.parseColor("#D8D8D8"));
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(2);
        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        initPath();
    }

    private void initPath() {
        mLinePath.reset();
        //绘制第一条线
        mLinePath.moveTo(getRealyX(mFirstLinePointArr[0].x), getRealyY(mFirstLinePointArr[0].y));
        mLinePath.lineTo(getRealyX(mFirstLinePointArr[2].x),getRealyY(mFirstLinePointArr[2].y));
        mLinePath.lineTo(getRealyX(mFirstLinePointArr[3].x),getRealyY(mFirstLinePointArr[3].y));
        mLinePath.lineTo(getRealyX(mFirstLinePointArr[4].x),getRealyY(mFirstLinePointArr[4].y));
        mLinePath.moveTo(getRealyX(mFirstLinePointArr[1].x),getRealyY(mFirstLinePointArr[1].y));
        mLinePath.lineTo(getRealyX(mFirstLinePointArr[2].x),getRealyY(mFirstLinePointArr[2].y));
        //绘制第二条线
        mLinePath.moveTo(getRealyX(mSecondLinePointArr[0].x),getRealyY(mSecondLinePointArr[0].y));
        mLinePath.lineTo(getRealyX(mSecondLinePointArr[1].x),getRealyY(mSecondLinePointArr[1].y));
        mLinePath.lineTo(getRealyX(mSecondLinePointArr[2].x),getRealyY(mSecondLinePointArr[2].y));
        mLinePath.lineTo(getRealyX(mSecondLinePointArr[3].x),getRealyY(mSecondLinePointArr[3].y));
        mLinePath.lineTo(getRealyX(mSecondLinePointArr[4].x),getRealyY(mSecondLinePointArr[4].y));
        mLinePath.lineTo(getRealyX(mSecondLinePointArr[5].x),getRealyY(mSecondLinePointArr[5].y));
        mLinePath.lineTo(getRealyX(mSecondLinePointArr[6].x),getRealyY(mSecondLinePointArr[6].y));
        //绘制第三条
        mLinePath.moveTo(getRealyX(mThirdLinePointArr[0].x),getRealyY(mThirdLinePointArr[0].y));
        mLinePath.lineTo(getRealyX(mThirdLinePointArr[1].x),getRealyY(mThirdLinePointArr[1].y));
        mLinePath.lineTo(getRealyX(mThirdLinePointArr[2].x),getRealyY(mThirdLinePointArr[2].y));
        mLinePath.lineTo(getRealyX(mThirdLinePointArr[3].x),getRealyY(mThirdLinePointArr[3].y));
        mLinePath.lineTo(getRealyX(mThirdLinePointArr[4].x),getRealyY(mThirdLinePointArr[4].y));
        mLinePath.lineTo(getRealyX(mThirdLinePointArr[5].x),getRealyY(mThirdLinePointArr[5].y));
        mLinePath.lineTo(getRealyX(mThirdLinePointArr[6].x),getRealyY(mThirdLinePointArr[6].y));
        //绘制第四条
        mLinePath.moveTo(getRealyX(mFourthLinePointArr[0].x),getRealyY(mFourthLinePointArr[0].y));
        mLinePath.lineTo(getRealyX(mFourthLinePointArr[2].x),getRealyY(mFourthLinePointArr[2].y));
        mLinePath.lineTo(getRealyX(mFourthLinePointArr[3].x),getRealyY(mFourthLinePointArr[3].y));
        mLinePath.lineTo(getRealyX(mFourthLinePointArr[4].x),getRealyY(mFourthLinePointArr[4].y));
        mLinePath.lineTo(getRealyX(mFourthLinePointArr[5].x),getRealyY(mFourthLinePointArr[5].y));
        mLinePath.lineTo(getRealyX(mFourthLinePointArr[6].x),getRealyY(mFourthLinePointArr[6].y));
        mLinePath.moveTo(getRealyX(mFourthLinePointArr[1].x),getRealyY(mFourthLinePointArr[1].y));
        mLinePath.lineTo(getRealyX(mFourthLinePointArr[2].x),getRealyY(mFourthLinePointArr[2].y));
        //绘制第五条
        mLinePath.moveTo(getRealyX(mFifthLinePointArr[0].x),getRealyY(mFifthLinePointArr[0].y));
        mLinePath.lineTo(getRealyX(mFifthLinePointArr[1].x),getRealyY(mFifthLinePointArr[1].y));
        mLinePath.lineTo(getRealyX(mFifthLinePointArr[2].x),getRealyY(mFifthLinePointArr[2].y));
        mLinePath.lineTo(getRealyX(mFifthLinePointArr[3].x),getRealyY(mFifthLinePointArr[3].y));
        mLinePath.moveTo(getRealyX(mFifthLinePointArr[2].x),getRealyY(mFifthLinePointArr[2].y));
        mLinePath.lineTo(getRealyX(mFifthLinePointArr[4].x),getRealyY(mFifthLinePointArr[4].y));
        //绘制第六条
        mLinePath.moveTo(getRealyX(mSixthLinePointArr[0].x),getRealyY(mSixthLinePointArr[0].y));
        mLinePath.lineTo(getRealyX(mSixthLinePointArr[1].x),getRealyY(mSixthLinePointArr[1].y));
        mLinePath.lineTo(getRealyX(mSixthLinePointArr[2].x),getRealyY(mSixthLinePointArr[2].y));
        mLinePath.lineTo(getRealyX(mSixthLinePointArr[3].x),getRealyY(mSixthLinePointArr[3].y));
        mLinePath.lineTo(getRealyX(mSixthLinePointArr[4].x),getRealyY(mSixthLinePointArr[4].y));
        //绘制第七条
        mLinePath.moveTo(getRealyX(mSeventhLinePointArr[0].x),getRealyY(mSeventhLinePointArr[0].y));
        mLinePath.lineTo(getRealyX(mSeventhLinePointArr[1].x),getRealyY(mSeventhLinePointArr[1].y));
        mLinePath.lineTo(getRealyX(mSeventhLinePointArr[2].x),getRealyY(mSeventhLinePointArr[2].y));
        mLinePath.lineTo(getRealyX(mSeventhLinePointArr[3].x),getRealyY(mSeventhLinePointArr[3].y));
        mLinePath.lineTo(getRealyX(mSeventhLinePointArr[4].x),getRealyY(mSeventhLinePointArr[4].y));
        mLinePath.lineTo(getRealyX(mSeventhLinePointArr[5].x),getRealyY(mSeventhLinePointArr[5].y));
        mLinePath.lineTo(getRealyX(mSeventhLinePointArr[6].x),getRealyY(mSeventhLinePointArr[6].y));
        //绘制第八条
        mLinePath.moveTo(getRealyX(mEighthLinePointArr[0].x),getRealyY(mEighthLinePointArr[0].y));
        mLinePath.lineTo(getRealyX(mEighthLinePointArr[1].x),getRealyY(mEighthLinePointArr[1].y));
        mLinePath.lineTo(getRealyX(mEighthLinePointArr[2].x),getRealyY(mEighthLinePointArr[2].y));
        mLinePath.lineTo(getRealyX(mEighthLinePointArr[3].x),getRealyY(mEighthLinePointArr[3].y));
        mLinePath.moveTo(getRealyX(mEighthLinePointArr[2].x),getRealyY(mEighthLinePointArr[2].y));
        mLinePath.lineTo(getRealyX(mEighthLinePointArr[4].x),getRealyY(mEighthLinePointArr[4].y));
        //绘制中心圆
        mLinePath.addCircle(mWidth/2,mHeight/2,34f*mWidth/mDesignWidth, Path.Direction.CW);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(mLinePath, mLinePaint);
        drawCircle(canvas);

    }

    private void drawCircle(Canvas canvas) {
        //绘制灰色圆形
        mCirclePaint.setStyle(Paint.Style.FILL);
        mCirclePaint.setColor(Color.parseColor("#c8c8c8"));
        canvas.drawCircle(getRealyX(mFirstLinePointArr[2].x),getRealyY(mFirstLinePointArr[2].y),2.5f*mWidth/mDesignWidth, mCirclePaint);
        canvas.drawCircle(getRealyX(mFourthLinePointArr[6].x),getRealyY(mFourthLinePointArr[6].y),4.5f*mWidth/mDesignWidth, mCirclePaint);
        canvas.drawCircle(getRealyX(mFifthLinePointArr[0].x),getRealyY(mFifthLinePointArr[0].y),4.5f*mWidth/mDesignWidth, mCirclePaint);
        canvas.drawCircle(getRealyX(mEighthLinePointArr[2].x),getRealyY(mEighthLinePointArr[2].y),2.5f*mWidth/mDesignWidth, mCirclePaint);
        //绘制红色圆
        mCirclePaint.setColor(Color.parseColor("#E13133"));
        canvas.drawCircle(getRealyX(mSecondLinePointArr[6].x),getRealyY(mSecondLinePointArr[6].y),5.5f*mWidth/mDesignWidth, mCirclePaint);
        canvas.drawCircle(getRealyX(mThirdLinePointArr[0].x),getRealyY(mThirdLinePointArr[0].y),7.5f*mWidth/mDesignWidth, mCirclePaint);
        canvas.drawCircle(getRealyX(mFourthLinePointArr[2].x),getRealyY(mFourthLinePointArr[2].y),2.5f*mWidth/mDesignWidth, mCirclePaint);
        canvas.drawCircle(getRealyX(mFifthLinePointArr[2].x),getRealyY(mFifthLinePointArr[2].y),2.5f*mWidth/mDesignWidth, mCirclePaint);
        canvas.drawCircle(getRealyX(mSixthLinePointArr[4].x),getRealyY(mSixthLinePointArr[4].y),8.5f*mWidth/mDesignWidth, mCirclePaint);
        canvas.drawCircle(getRealyX(mSeventhLinePointArr[0].x),getRealyY(mSeventhLinePointArr[0].y),6.5f*mWidth/mDesignWidth, mCirclePaint);
        //绘制白色圆
        mCirclePaint.setColor(Color.parseColor("#FFFFFF"));
        canvas.drawCircle(getRealyX(mFirstLinePointArr[4].x),getRealyY(mFirstLinePointArr[4].y),4.5f*mWidth/mDesignWidth, mCirclePaint);
        canvas.drawCircle(getRealyX(mSecondLinePointArr[0].x),getRealyY(mSecondLinePointArr[0].y),6.5f*mWidth/mDesignWidth, mCirclePaint);
        canvas.drawCircle(getRealyX(mThirdLinePointArr[6].x),getRealyY(mThirdLinePointArr[6].y),5.5f*mWidth/mDesignWidth, mCirclePaint);
        canvas.drawCircle(getRealyX(mSixthLinePointArr[0].x),getRealyY(mSixthLinePointArr[0].y),6.5f*mWidth/mDesignWidth, mCirclePaint);
        canvas.drawCircle(getRealyX(mSeventhLinePointArr[6].x),getRealyY(mSeventhLinePointArr[6].y),7.5f*mWidth/mDesignWidth, mCirclePaint);
        canvas.drawCircle(getRealyX(mEighthLinePointArr[0].x),getRealyY(mEighthLinePointArr[0].y),3.5f*mWidth/mDesignWidth, mCirclePaint);
        //绘制白色圆外部边框
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setColor(Color.parseColor("#D8D8D8"));
        mCirclePaint.setStrokeWidth(2);
        canvas.drawCircle(getRealyX(mFirstLinePointArr[4].x),getRealyY(mFirstLinePointArr[4].y),4f*mWidth/mDesignWidth, mCirclePaint);
        canvas.drawCircle(getRealyX(mSecondLinePointArr[0].x),getRealyY(mSecondLinePointArr[0].y),6f*mWidth/mDesignWidth, mCirclePaint);
        canvas.drawCircle(getRealyX(mThirdLinePointArr[6].x),getRealyY(mThirdLinePointArr[6].y),5f*mWidth/mDesignWidth, mCirclePaint);
        canvas.drawCircle(getRealyX(mSixthLinePointArr[0].x),getRealyY(mSixthLinePointArr[0].y),6f*mWidth/mDesignWidth, mCirclePaint);
        canvas.drawCircle(getRealyX(mSeventhLinePointArr[6].x),getRealyY(mSeventhLinePointArr[6].y),7f*mWidth/mDesignWidth, mCirclePaint);
        canvas.drawCircle(getRealyX(mEighthLinePointArr[0].x),getRealyY(mEighthLinePointArr[0].y),3f*mWidth/mDesignWidth, mCirclePaint);
    }

    private float getRealyX(int width) {
        return width * mWidth / mDesignWidth;
    }

    private float getRealyY(int height) {
        return height * mHeight / mDesignHeight;
    }
}

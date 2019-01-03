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

import java.util.ArrayList;
import java.util.Random;

public class VyWoQuViewGroup extends FrameLayout {
    private int mHeight;
    private int mWidth;
    private int mDesignWidth  = 375;
    private int mDesignHeight = 551;

    private Point[]                       mFirstLinePointArr   = {new Point(107, 135), new Point(127, 135),
            new Point(117, 141), new Point(117, 210),
            new Point(164, 240)};
    private Point[]                       mSecondLinePointArr  = {new Point(193, 27), new Point(193, 79),
            new Point(181, 87), new Point(181, 119),
            new Point(135, 145), new Point(135, 206),
            new Point(159, 221)};
    private Point[]                       mThirdLinePointArr   = {new Point(216, 56), new Point(216, 104),
            new Point(252, 124), new Point(252, 155),
            new Point(234, 166), new Point(234, 216),
            new Point(218, 225)};
    private Point[]                       mFourthLinePointArr  = {new Point(278, 106), new Point(298, 106),
            new Point(288, 112), new Point(288, 151),
            new Point(248, 174), new Point(248, 224),
            new Point(214, 245)};
    private Point[]                       mFifthLinePointArr   = {new Point(155, 275), new Point(85, 316),
            new Point(85, 408), new Point(75, 414),
            new Point(95, 414)};
    private Point[]                       mSixthLinePointArr   = {new Point(150, 299), new Point(108, 324),
            new Point(108, 405), new Point(177, 444),
            new Point(177, 500)};
    private Point[]                       mSeventhLinePointArr = {new Point(210, 311), new Point(233, 324),
            new Point(233, 374), new Point(243, 380),
            new Point(243, 412), new Point(206, 435),
            new Point(206, 479)};
    private Point[]                       mEighthLinePointArr  = {new Point(210, 290), new Point(277, 328),
            new Point(277, 401), new Point(267, 407),
            new Point(287, 407)};
    private Path                          mLinePath;
    private Paint                         mLinePaint;
    private Paint                         mCirclePaint;
    private ArrayList<VyHexagonImageView> mImageViewList       = new ArrayList();
    private LocationInfo[][]              mSumArr              = {
            {new LocationInfo(60, 21, 115), new LocationInfo(237, 5, 101),
                    new LocationInfo(14, 126, 87), new LocationInfo(144, 132, 87),
                    new LocationInfo(44, 219, 73), new LocationInfo(241, 201, 129),
                    new LocationInfo(124, 319, 101), new LocationInfo(282, 336, 73),
                    new LocationInfo(21, 415, 129), new LocationInfo(220, 409, 115)},
            {new LocationInfo(67, 35, 101), new LocationInfo(245, 19, 87),
                    new LocationInfo(11, 108, 73), new LocationInfo(148, 140, 87),
                    new LocationInfo(2, 189, 129), new LocationInfo(249, 194, 101),
                    new LocationInfo(113, 311, 115), new LocationInfo(290, 318, 73),
                    new LocationInfo(28, 415, 115), new LocationInfo(213, 409, 129)},
            {new LocationInfo(53, 7, 129), new LocationInfo(245, 20, 87),
                    new LocationInfo(12, 106, 73), new LocationInfo(151, 140, 73),
                    new LocationInfo(289, 123, 87), new LocationInfo(19, 215, 101),
                    new LocationInfo(235, 204, 129), new LocationInfo(113, 308, 115),
                    new LocationInfo(28, 414, 115), new LocationInfo(227, 408, 101)},
            {new LocationInfo(60, 20, 115), new LocationInfo(245, 18, 87),
                    new LocationInfo(148, 140, 87), new LocationInfo(24, 202, 101),
                    new LocationInfo(249, 163, 129), new LocationInfo(4, 297, 73),
                    new LocationInfo(117, 308, 101), new LocationInfo(289, 342, 73),
                    new LocationInfo(28, 415, 115), new LocationInfo(213, 408, 129)},
            {new LocationInfo(53, 6, 129), new LocationInfo(245, 18, 87),
                    new LocationInfo(135, 125, 101), new LocationInfo(293, 125, 73),
                    new LocationInfo(9, 195, 115), new LocationInfo(237, 204, 129),
                    new LocationInfo(6, 345, 73), new LocationInfo(126, 315, 87),
                    new LocationInfo(35, 415, 101), new LocationInfo(220, 408, 115)}};
    private OnLoadPicListener             mListener;
    private Random mRandom;
    private int mLayoutIndex;

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
        for (int i = 0; i < 10; i++) {
            VyHexagonImageView vyHexagonImageView = new VyHexagonImageView(context);
            mImageViewList.add(vyHexagonImageView);
            addView(vyHexagonImageView);
        }
        mRandom = new Random();
        mLayoutIndex = mRandom.nextInt(5);
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
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(size, size * mDesignHeight / mDesignWidth);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        initPath();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        //        super.onLayout(changed, left, top, right, bottom);
        for (int i = 0; i < mImageViewList.size(); i++) {
            VyHexagonImageView vyHexagonImageView = mImageViewList.get(i);
            int realySize = getRealySize(mSumArr[mLayoutIndex][i].size);
            float realyX = getRealyX(mSumArr[mLayoutIndex][i].x);
            float realyY = getRealyY(mSumArr[mLayoutIndex][i].y);
            vyHexagonImageView.setLayoutParams(new FrameLayout.LayoutParams(realySize, realySize));
            vyHexagonImageView.layout((int) realyX, (int) realyY, (int) realyX + realySize, (int) realyY + realySize);
        }
    }

    private void initPath() {
        mLinePath.reset();
        //绘制第一条线
        mLinePath.moveTo(getRealyX(mFirstLinePointArr[0].x), getRealyY(mFirstLinePointArr[0].y));
        mLinePath.lineTo(getRealyX(mFirstLinePointArr[2].x), getRealyY(mFirstLinePointArr[2].y));
        mLinePath.lineTo(getRealyX(mFirstLinePointArr[3].x), getRealyY(mFirstLinePointArr[3].y));
        mLinePath.lineTo(getRealyX(mFirstLinePointArr[4].x), getRealyY(mFirstLinePointArr[4].y));
        mLinePath.moveTo(getRealyX(mFirstLinePointArr[1].x), getRealyY(mFirstLinePointArr[1].y));
        mLinePath.lineTo(getRealyX(mFirstLinePointArr[2].x), getRealyY(mFirstLinePointArr[2].y));
        //绘制第二条线
        mLinePath.moveTo(getRealyX(mSecondLinePointArr[0].x), getRealyY(mSecondLinePointArr[0].y));
        mLinePath.lineTo(getRealyX(mSecondLinePointArr[1].x), getRealyY(mSecondLinePointArr[1].y));
        mLinePath.lineTo(getRealyX(mSecondLinePointArr[2].x), getRealyY(mSecondLinePointArr[2].y));
        mLinePath.lineTo(getRealyX(mSecondLinePointArr[3].x), getRealyY(mSecondLinePointArr[3].y));
        mLinePath.lineTo(getRealyX(mSecondLinePointArr[4].x), getRealyY(mSecondLinePointArr[4].y));
        mLinePath.lineTo(getRealyX(mSecondLinePointArr[5].x), getRealyY(mSecondLinePointArr[5].y));
        mLinePath.lineTo(getRealyX(mSecondLinePointArr[6].x), getRealyY(mSecondLinePointArr[6].y));
        //绘制第三条
        mLinePath.moveTo(getRealyX(mThirdLinePointArr[0].x), getRealyY(mThirdLinePointArr[0].y));
        mLinePath.lineTo(getRealyX(mThirdLinePointArr[1].x), getRealyY(mThirdLinePointArr[1].y));
        mLinePath.lineTo(getRealyX(mThirdLinePointArr[2].x), getRealyY(mThirdLinePointArr[2].y));
        mLinePath.lineTo(getRealyX(mThirdLinePointArr[3].x), getRealyY(mThirdLinePointArr[3].y));
        mLinePath.lineTo(getRealyX(mThirdLinePointArr[4].x), getRealyY(mThirdLinePointArr[4].y));
        mLinePath.lineTo(getRealyX(mThirdLinePointArr[5].x), getRealyY(mThirdLinePointArr[5].y));
        mLinePath.lineTo(getRealyX(mThirdLinePointArr[6].x), getRealyY(mThirdLinePointArr[6].y));
        //绘制第四条
        mLinePath.moveTo(getRealyX(mFourthLinePointArr[0].x), getRealyY(mFourthLinePointArr[0].y));
        mLinePath.lineTo(getRealyX(mFourthLinePointArr[2].x), getRealyY(mFourthLinePointArr[2].y));
        mLinePath.lineTo(getRealyX(mFourthLinePointArr[3].x), getRealyY(mFourthLinePointArr[3].y));
        mLinePath.lineTo(getRealyX(mFourthLinePointArr[4].x), getRealyY(mFourthLinePointArr[4].y));
        mLinePath.lineTo(getRealyX(mFourthLinePointArr[5].x), getRealyY(mFourthLinePointArr[5].y));
        mLinePath.lineTo(getRealyX(mFourthLinePointArr[6].x), getRealyY(mFourthLinePointArr[6].y));
        mLinePath.moveTo(getRealyX(mFourthLinePointArr[1].x), getRealyY(mFourthLinePointArr[1].y));
        mLinePath.lineTo(getRealyX(mFourthLinePointArr[2].x), getRealyY(mFourthLinePointArr[2].y));
        //绘制第五条
        mLinePath.moveTo(getRealyX(mFifthLinePointArr[0].x), getRealyY(mFifthLinePointArr[0].y));
        mLinePath.lineTo(getRealyX(mFifthLinePointArr[1].x), getRealyY(mFifthLinePointArr[1].y));
        mLinePath.lineTo(getRealyX(mFifthLinePointArr[2].x), getRealyY(mFifthLinePointArr[2].y));
        mLinePath.lineTo(getRealyX(mFifthLinePointArr[3].x), getRealyY(mFifthLinePointArr[3].y));
        mLinePath.moveTo(getRealyX(mFifthLinePointArr[2].x), getRealyY(mFifthLinePointArr[2].y));
        mLinePath.lineTo(getRealyX(mFifthLinePointArr[4].x), getRealyY(mFifthLinePointArr[4].y));
        //绘制第六条
        mLinePath.moveTo(getRealyX(mSixthLinePointArr[0].x), getRealyY(mSixthLinePointArr[0].y));
        mLinePath.lineTo(getRealyX(mSixthLinePointArr[1].x), getRealyY(mSixthLinePointArr[1].y));
        mLinePath.lineTo(getRealyX(mSixthLinePointArr[2].x), getRealyY(mSixthLinePointArr[2].y));
        mLinePath.lineTo(getRealyX(mSixthLinePointArr[3].x), getRealyY(mSixthLinePointArr[3].y));
        mLinePath.lineTo(getRealyX(mSixthLinePointArr[4].x), getRealyY(mSixthLinePointArr[4].y));
        //绘制第七条
        mLinePath.moveTo(getRealyX(mSeventhLinePointArr[0].x), getRealyY(mSeventhLinePointArr[0].y));
        mLinePath.lineTo(getRealyX(mSeventhLinePointArr[1].x), getRealyY(mSeventhLinePointArr[1].y));
        mLinePath.lineTo(getRealyX(mSeventhLinePointArr[2].x), getRealyY(mSeventhLinePointArr[2].y));
        mLinePath.lineTo(getRealyX(mSeventhLinePointArr[3].x), getRealyY(mSeventhLinePointArr[3].y));
        mLinePath.lineTo(getRealyX(mSeventhLinePointArr[4].x), getRealyY(mSeventhLinePointArr[4].y));
        mLinePath.lineTo(getRealyX(mSeventhLinePointArr[5].x), getRealyY(mSeventhLinePointArr[5].y));
        mLinePath.lineTo(getRealyX(mSeventhLinePointArr[6].x), getRealyY(mSeventhLinePointArr[6].y));
        //绘制第八条
        mLinePath.moveTo(getRealyX(mEighthLinePointArr[0].x), getRealyY(mEighthLinePointArr[0].y));
        mLinePath.lineTo(getRealyX(mEighthLinePointArr[1].x), getRealyY(mEighthLinePointArr[1].y));
        mLinePath.lineTo(getRealyX(mEighthLinePointArr[2].x), getRealyY(mEighthLinePointArr[2].y));
        mLinePath.lineTo(getRealyX(mEighthLinePointArr[3].x), getRealyY(mEighthLinePointArr[3].y));
        mLinePath.moveTo(getRealyX(mEighthLinePointArr[2].x), getRealyY(mEighthLinePointArr[2].y));
        mLinePath.lineTo(getRealyX(mEighthLinePointArr[4].x), getRealyY(mEighthLinePointArr[4].y));
        //绘制中心圆
        mLinePath.addCircle(mWidth / 2, getRealyY(265), 34f * mWidth / mDesignWidth, Path.Direction.CW);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(mLinePath, mLinePaint);
        drawCircle(canvas);
        if (mListener != null) {
            for (int i = 0; i < 10; i++) {
                VyHexagonImageView vyHexagonImageView = mImageViewList.get(i);
                mListener.loadPic(vyHexagonImageView,i);
            }
        }
    }

    private void drawCircle(Canvas canvas) {
        //绘制灰色圆形
        mCirclePaint.setStyle(Paint.Style.FILL);
        mCirclePaint.setColor(Color.parseColor("#c8c8c8"));
        canvas.drawCircle(getRealyX(mFirstLinePointArr[2].x), getRealyY(mFirstLinePointArr[2].y), 2.5f * mWidth / mDesignWidth, mCirclePaint);
        canvas.drawCircle(getRealyX(mFourthLinePointArr[6].x), getRealyY(mFourthLinePointArr[6].y), 4.5f * mWidth / mDesignWidth, mCirclePaint);
        canvas.drawCircle(getRealyX(mFifthLinePointArr[0].x), getRealyY(mFifthLinePointArr[0].y), 4.5f * mWidth / mDesignWidth, mCirclePaint);
        canvas.drawCircle(getRealyX(mEighthLinePointArr[2].x), getRealyY(mEighthLinePointArr[2].y), 2.5f * mWidth / mDesignWidth, mCirclePaint);
        //绘制红色圆
        mCirclePaint.setColor(Color.parseColor("#E13133"));
        canvas.drawCircle(getRealyX(mSecondLinePointArr[6].x), getRealyY(mSecondLinePointArr[6].y), 5.5f * mWidth / mDesignWidth, mCirclePaint);
        canvas.drawCircle(getRealyX(mThirdLinePointArr[0].x), getRealyY(mThirdLinePointArr[0].y), 7.5f * mWidth / mDesignWidth, mCirclePaint);
        canvas.drawCircle(getRealyX(mFourthLinePointArr[2].x), getRealyY(mFourthLinePointArr[2].y), 2.5f * mWidth / mDesignWidth, mCirclePaint);
        canvas.drawCircle(getRealyX(mFifthLinePointArr[2].x), getRealyY(mFifthLinePointArr[2].y), 2.5f * mWidth / mDesignWidth, mCirclePaint);
        canvas.drawCircle(getRealyX(mSixthLinePointArr[4].x), getRealyY(mSixthLinePointArr[4].y), 8.5f * mWidth / mDesignWidth, mCirclePaint);
        canvas.drawCircle(getRealyX(mSeventhLinePointArr[0].x), getRealyY(mSeventhLinePointArr[0].y), 6.5f * mWidth / mDesignWidth, mCirclePaint);
        //绘制白色圆
        mCirclePaint.setColor(Color.parseColor("#FFFFFF"));
        canvas.drawCircle(getRealyX(mFirstLinePointArr[4].x), getRealyY(mFirstLinePointArr[4].y), 4.5f * mWidth / mDesignWidth, mCirclePaint);
        canvas.drawCircle(getRealyX(mSecondLinePointArr[0].x), getRealyY(mSecondLinePointArr[0].y), 6.5f * mWidth / mDesignWidth, mCirclePaint);
        canvas.drawCircle(getRealyX(mThirdLinePointArr[6].x), getRealyY(mThirdLinePointArr[6].y), 5.5f * mWidth / mDesignWidth, mCirclePaint);
        canvas.drawCircle(getRealyX(mSixthLinePointArr[0].x), getRealyY(mSixthLinePointArr[0].y), 6.5f * mWidth / mDesignWidth, mCirclePaint);
        canvas.drawCircle(getRealyX(mSeventhLinePointArr[6].x), getRealyY(mSeventhLinePointArr[6].y), 7.5f * mWidth / mDesignWidth, mCirclePaint);
        canvas.drawCircle(getRealyX(mEighthLinePointArr[0].x), getRealyY(mEighthLinePointArr[0].y), 3.5f * mWidth / mDesignWidth, mCirclePaint);
        //绘制白色圆外部边框
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setColor(Color.parseColor("#D8D8D8"));
        mCirclePaint.setStrokeWidth(2);
        canvas.drawCircle(getRealyX(mFirstLinePointArr[4].x), getRealyY(mFirstLinePointArr[4].y), 4f * mWidth / mDesignWidth, mCirclePaint);
        canvas.drawCircle(getRealyX(mSecondLinePointArr[0].x), getRealyY(mSecondLinePointArr[0].y), 6f * mWidth / mDesignWidth, mCirclePaint);
        canvas.drawCircle(getRealyX(mThirdLinePointArr[6].x), getRealyY(mThirdLinePointArr[6].y), 5f * mWidth / mDesignWidth, mCirclePaint);
        canvas.drawCircle(getRealyX(mSixthLinePointArr[0].x), getRealyY(mSixthLinePointArr[0].y), 6f * mWidth / mDesignWidth, mCirclePaint);
        canvas.drawCircle(getRealyX(mSeventhLinePointArr[6].x), getRealyY(mSeventhLinePointArr[6].y), 7f * mWidth / mDesignWidth, mCirclePaint);
        canvas.drawCircle(getRealyX(mEighthLinePointArr[0].x), getRealyY(mEighthLinePointArr[0].y), 3f * mWidth / mDesignWidth, mCirclePaint);
    }

    private float getRealyX(int width) {
        return width * mWidth / mDesignWidth;
    }

    private float getRealyY(int height) {
        return height * mHeight / mDesignHeight;
    }

    private int getRealySize(int size) {
        return size * mWidth / mDesignWidth;
    }

    private static class LocationInfo {
        int x;
        int y;
        int size;

        public LocationInfo(int x, int y, int size) {
            this.x = x;
            this.y = y;
            this.size = size;
        }
    }

    public interface OnLoadPicListener {
        void loadPic(VyHexagonImageView view, int index);
    }

    public void setOnLoadPicListener(OnLoadPicListener listener) {
        mListener = listener;
    }
}

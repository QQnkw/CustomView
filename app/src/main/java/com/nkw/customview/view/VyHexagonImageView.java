package com.nkw.customview.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class VyHexagonImageView extends AppCompatImageView {
    private              String                 tag                     = "HexagonImageView";
    private static final Bitmap.Config          BITMAP_CONFIG           = Bitmap.Config.ARGB_8888;
    private static final int                    COLORDRAWABLE_DIMENSION = 1;
    private static final Matrix                 mShaderMatrix           = new Matrix();
    private static final Paint                  mBitmapPaint            = new Paint();
    private              BitmapShader           mBitmapShader;
    private              int                    mBitmapWidth;
    private              int                    mBitmapHeight;
    private              int                    mWidth                  = 0;
    private              Bitmap                 mBitmap;
    private              Paint                  mBorderPaint;
    private              int                    mDefaultStrokeWidth     = 10;
    private              String                 mInnerBorderColor       = "#FFFFFF";
    private              String                 mOuterVBorderColor      = "#E13133";
    private              Path                   mInnerBorderPath;
    private              Path                   mOuterBorderPath;
    private              OnClickHexagonListener mListener;

    public VyHexagonImageView(Context context) {
        this(context, null);
    }

    public VyHexagonImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VyHexagonImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mBorderPaint = new Paint();
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setStrokeWidth(mDefaultStrokeWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();
        if (drawable == null || getWidth() * getHeight() == 0) {
            return;
        }
        mBitmap = getBitmapFromDrawable(drawable);
        if (mBitmap == null) {
            return;
        }

        setup();
        canvas.drawPath(getHexagonPath(), mBitmapPaint);
        mBorderPaint.setColor(Color.parseColor(mInnerBorderColor));
        //        canvas.drawPath(mPath,mBorderPaint);
        canvas.drawPath(getInnerBorderPath(), mBorderPaint);
        mBorderPaint.setColor(Color.parseColor(mOuterVBorderColor));
        canvas.drawPath(getOuterBorderPath(), mBorderPaint);
    }

    private Path getOuterBorderPath() {
        if (mOuterBorderPath == null) {
            mOuterBorderPath = new Path();
        }
        float outWidth = mWidth - mDefaultStrokeWidth;
        mOuterBorderPath = createPath(outWidth, mOuterBorderPath);
        mOuterBorderPath.offset(mDefaultStrokeWidth * 0.5f, mDefaultStrokeWidth * 0.5f);
        return mOuterBorderPath;
    }

    private Path getInnerBorderPath() {
        if (mInnerBorderPath == null) {
            mInnerBorderPath = new Path();
        }
        float innerWidth = mWidth - mDefaultStrokeWidth * 3f;
        mInnerBorderPath = createPath(innerWidth, mInnerBorderPath);
        mInnerBorderPath.offset(mDefaultStrokeWidth * 1.5f, mDefaultStrokeWidth * 1.5f);
        return mInnerBorderPath;
    }

    private Path mPath = null;

    public Path getHexagonPath() {
        if (mPath == null) {
            mPath = new Path();
        }
        mPath = createPath(mWidth, mPath);

        return mPath;
    }

    private Path createPath(float width, Path path) {
        path.reset();
        float d = (float) ((float) width / 4 * (2 - Math.sqrt(3)));//六边形到边到内切圆的距离
        float r = width / 4;
        //竖六边形
        path.moveTo(width / 2, 0);
        path.lineTo(width - d, r);
        path.lineTo(width - d, r * 3);
        path.lineTo(width / 2, width);
        path.lineTo(d, r * 3);
        path.lineTo(d, r);
        path.close();
        //横六边形
        /*float p0x = r;
        float p0y = d;

        float p1x = r*3;
        float p1y = d;

        float p2x = mWidth;
        float p2y = mWidth/2;

        float p3x = p1x;
        float p3y = mWidth-d;

        float p4x = p0x;
        float p4y = p3y;

        float p5x = 0;
        float p5y = p2y;

        mPath.reset();
        mPath.moveTo(p0x,p0y);
        mPath.lineTo(p1x,p1y);
        mPath.lineTo(p2x,p2y);
        mPath.lineTo(p3x,p3y);
        mPath.lineTo(p4x,p4y);
        mPath.lineTo(p5x,p5y);
        mPath.lineTo(p0x,p0y);*/
        return path;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);//限制为正方形
        mWidth = Math.min(getMeasuredWidth(), getMeasuredHeight());
        setMeasuredDimension(mWidth, mWidth);
    }

    public int getmWidth() {
        return mWidth;
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        invalidate();
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        invalidate();
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        invalidate();
    }

    private Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (drawable == null) {
            return null;
        }
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        try {
            Bitmap bitmap;
            if (drawable instanceof ColorDrawable) {
                bitmap = Bitmap.createBitmap(COLORDRAWABLE_DIMENSION,
                        COLORDRAWABLE_DIMENSION, BITMAP_CONFIG);
            } else {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight(), BITMAP_CONFIG);
            }

            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, mWidth, mWidth);
            drawable.draw(canvas);
            return bitmap;
        } catch (OutOfMemoryError e) {
            return null;
        }
    }

    private void setup() {
        if (mBitmap != null) {
            mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            mBitmapPaint.setAntiAlias(true);
            mBitmapHeight = mBitmap.getHeight();
            mBitmapWidth = mBitmap.getWidth();
            updateShaderMatrix();
            mBitmapPaint.setShader(mBitmapShader);
        }
    }


    private void updateShaderMatrix() {
        float scale;
        mShaderMatrix.set(null);
        if (mBitmapWidth != mBitmapHeight) {
            scale = Math.max((float) mWidth / mBitmapWidth, (float) mWidth / mBitmapHeight);
        } else {
            scale = (float) mWidth / mBitmapWidth;
        }
        mShaderMatrix.setScale(scale, scale);//放大铺满
        float dx = mWidth - mBitmapWidth * scale;
        float dy = mWidth - mBitmapHeight * scale;
        mShaderMatrix.postTranslate(dx / 2, dy / 2);//平移居中
        mBitmapShader.setLocalMatrix(mShaderMatrix);
    }

    public void setBorderColor(int strokeWidth, String innerBorderColor, String outerVBorderColor) {
        if (strokeWidth > 0) {
            mDefaultStrokeWidth = strokeWidth;
        }
        mBorderPaint.setStrokeWidth(mDefaultStrokeWidth);
        if (!TextUtils.isEmpty(innerBorderColor)) {
            mInnerBorderColor = innerBorderColor;
        }
        if (!TextUtils.isEmpty(outerVBorderColor)) {
            mOuterVBorderColor = outerVBorderColor;
        }
    }

    private boolean isDown = false;
    private boolean isUp   = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                Point downPoint = new Point((int) event.getX(), (int) event.getY());
                if (isInHexagon(mPath, downPoint)) {
                    isDown = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                Point upPoint = new Point((int) event.getX(), (int) event.getY());
                if (isInHexagon(mPath, upPoint)) {
                    isUp = true;
                }
                if (isDown && isUp) {
                    if (mListener != null) {
                        mListener.clickHexagon(this);
                    }
                    isDown = false;
                    isUp = false;
                }
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 判断点是否在边界内
     *
     * @param path
     * @param point
     * @return
     */
    private boolean isInHexagon(Path path, Point point) {
        RectF bounds = new RectF();
        path.computeBounds(bounds, true);
        Region region = new Region();
        region.setPath(path, new Region((int) bounds.left, (int) bounds.top,
                (int) bounds.right, (int) bounds.bottom));
        return region.contains(point.x, point.y);
    }

    public interface OnClickHexagonListener {
        void clickHexagon(View view);
    }

    public void setOnClickHexagonListener(OnClickHexagonListener listener) {
        mListener = listener;
    }
}
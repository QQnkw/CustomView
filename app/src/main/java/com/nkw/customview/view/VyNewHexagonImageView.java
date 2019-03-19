package com.nkw.customview.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

public class VyNewHexagonImageView extends android.support.v7.widget.AppCompatImageView {

    private Paint  mBitmapPaint;
    private Path   mPath         = new Path();
    private Paint  mBorderPaint;
    private Matrix mShaderMatrix = new Matrix();
    private int mBorderWidth = 10;

    public VyNewHexagonImageView(Context context) {
        this(context, null);
    }

    public VyNewHexagonImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VyNewHexagonImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint(context);
    }

    private void initPaint(Context context) {
        CornerPathEffect cornerPathEffect = new CornerPathEffect(20);
        mBitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBitmapPaint.setDither(true);
        mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        initBorderWidthAndColor(mBorderWidth,0xFFD8D8D8);
        mBitmapPaint.setPathEffect(cornerPathEffect);
        mBorderPaint.setPathEffect(cornerPathEffect);
    }

    private void initBorderWidthAndColor(int borderWidth, int color) {
        mBorderPaint.setStrokeWidth(borderWidth);
        mBorderPaint.setColor(color);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int size = Math.max(width, height);
        setMeasuredDimension(size, size);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        createPath(w - mBorderWidth);
        Bitmap bitmap = getBitmapFromDrawable();
        if (bitmap == null) {
            return;
        }
        BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        int bitmapHeight = bitmap.getHeight();
        int bitmapWidth = bitmap.getWidth();
        int bitmapSize = Math.min(bitmapHeight, bitmapWidth);
        float scale = w * 1f / bitmapSize;
        mShaderMatrix.reset();
        mShaderMatrix.setScale(scale, scale);//放大铺满
        float dx = w - bitmapWidth * scale;
        float dy = w - bitmapHeight * scale;
        mShaderMatrix.postTranslate(dx / 2, dy / 2);//平移居中
        bitmapShader.setLocalMatrix(mShaderMatrix);
        mBitmapPaint.setShader(bitmapShader);
    }

    private void createPath(int realSize) {
        mPath.reset();
        float d = (float) ((float) realSize / 4 * (2 - Math.sqrt(3)));//六边形到边到内切圆的距离
        float r = realSize / 4;
        //竖六边形
        mPath.moveTo(realSize / 2, 0);
        mPath.lineTo(realSize - d, r);
        mPath.lineTo(realSize - d, r * 3);
        mPath.lineTo(realSize / 2, realSize);
        mPath.lineTo(d, r * 3);
        mPath.lineTo(d, r);
        mPath.close();
        mPath.offset(mBorderWidth/2, mBorderWidth/2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(mPath, mBitmapPaint);
        canvas.drawPath(mPath, mBorderPaint);
    }

    private Bitmap getBitmapFromDrawable() {
        Drawable drawable = getDrawable();
        if (drawable == null || getWidth() * getHeight() == 0) {
            return null;
        }
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        try {
            int w = drawable.getIntrinsicWidth();
            int h = drawable.getIntrinsicHeight();
            Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, w, h);
            drawable.draw(canvas);
            return bitmap;
        } catch (OutOfMemoryError e) {
            return null;
        }
    }

    public void setBorderWidthAndColor(int borderWidth,int borderColor) {
        mBorderWidth = borderWidth;
        initBorderWidthAndColor(mBorderWidth, borderColor);
    }


}

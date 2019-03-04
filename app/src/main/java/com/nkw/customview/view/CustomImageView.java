package com.nkw.customview.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

public class CustomImageView extends android.support.v7.widget.AppCompatImageView {
    private int mWidth;
    private Paint mPaint;
    private Matrix mMatrix;

    public CustomImageView(Context context) {
        this(context,null);
    }

    public CustomImageView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mMatrix = new Matrix();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();
        if (drawable == null || getWidth() * getHeight() == 0) {
            return;
        }
        Bitmap bitmap = getBitmapFromDrawable(drawable);
        if (bitmap == null) {
            return;
        }
        darawBitmap(canvas,bitmap);
    }

    private void darawBitmap(Canvas canvas, Bitmap bitmap) {
        //初始化BitmapShader，传入bitmap对象
        BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        //计算缩放比例
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        float scale = Math.max(mWidth*1f/ height, mWidth*1f/ width);
        mMatrix.reset();
        mMatrix.setScale(scale, scale);

        float dx = mWidth - width * scale;
        float dy = mWidth - height * scale;
        mMatrix.postTranslate(dx / 2, dy / 2);//平移居中
        bitmapShader.setLocalMatrix(mMatrix);
        mPaint.setShader(bitmapShader);
        Path path = new Path();
        path.moveTo(mWidth/2,0);
        path.lineTo(mWidth,mWidth/2);
        path.lineTo(mWidth/2,mWidth);
        path.lineTo(0,mWidth/2);
        path.close();
        canvas.drawPath(path,mPaint);
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
                bitmap = Bitmap.createBitmap(1,
                        1, Bitmap.Config.ARGB_8888);
            } else {
                int intrinsicWidth = drawable.getIntrinsicWidth();
                int intrinsicHeight = drawable.getIntrinsicHeight();
                bitmap = Bitmap.createBitmap(intrinsicWidth,
                        intrinsicHeight, Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, mWidth, mWidth);
            drawable.draw(canvas);
            return bitmap;
        } catch (OutOfMemoryError e) {
            return null;
        }
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);//限制为正方形
        mWidth = Math.min(getMeasuredWidth(), getMeasuredHeight());
        setMeasuredDimension(mWidth, mWidth);
    }
}

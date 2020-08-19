package com.nkw.customview.view.recorder;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.nkw.customview.R;

/**
 * 模仿微信发送语音的布局
 */
public class RecordConstraintLayout extends ConstraintLayout {

    private Path mPath;
    private Paint mPaint;
    private LinearGradient mLinearGradient;
    private int[] mCloseViewLocation;
    private View mCloseView;

    public RecordConstraintLayout(@NonNull Context context) {
        this(context, null);
    }

    public RecordConstraintLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecordConstraintLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mCloseViewLocation = new int[4];
        setBackgroundColor(Color.parseColor("#99000000"));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (mPath == null) {
            mPath = new Path();
        } else {
            mPath.reset();
        }
        mPath.moveTo(0F, h);
        mPath.lineTo(0F, h / 10F * 9F);
        mPath.quadTo(w / 2F, h / 10F * 8F, w, h / 10F * 9F);
        mPath.lineTo(w, h);
        mPath.close();
        if (mLinearGradient == null) {
            mLinearGradient = new LinearGradient(w / 2F, h / 10F * 8F, w / 2F, h, Color.parseColor(
                    "#FFB296"), Color.parseColor("#FF7E4E"), Shader.TileMode.MIRROR);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            if (childAt.getId() == R.id.close_record_view) {
                mCloseView = childAt;
                int[] outLocation = new int[2];
                mCloseView.getLocationOnScreen(outLocation);
                mCloseViewLocation[0] = outLocation[0];
                mCloseViewLocation[1] = outLocation[1];
                mCloseViewLocation[2] = outLocation[0] + mCloseView.getWidth();
                mCloseViewLocation[3] = outLocation[1] + mCloseView.getHeight();
                break;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setStrokeWidth(1);
        mPaint.setShader(mLinearGradient);
        canvas.drawPath(mPath, mPaint);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d("CustomFrameLayout", "ACTION_DOWN");
                setVisibility(View.VISIBLE);
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d("CustomFrameLayout", "ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                Log.d("CustomFrameLayout", "ACTION_UP");
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.d("CustomFrameLayout", "ACTION_CANCEL");
                break;
        }
        float rawY = event.getRawY();
        float rawX = event.getRawX();
        checkTouchIsOnCloseView(rawX, rawY,event.getAction());
        return true;
    }

    private boolean mIsChange = false;

    private void checkTouchIsOnCloseView(float rawX, float rawY,int touchState) {
        Log.d("CustomFrameLayout", "rawY:" + rawY + "---rawX:" + rawX);
        if (rawX > mCloseViewLocation[0] && rawX < mCloseViewLocation[2] && rawY > mCloseViewLocation[1] && rawY < mCloseViewLocation[3]) {
            if (!mIsChange) {
                mCloseView.setBackgroundColor(Color.CYAN);
                mIsChange = true;
            }
            if (touchState== MotionEvent.ACTION_UP) {
                setVisibility(View.GONE);
                closeVoiceUI();
            }
        } else {
            if (mIsChange) {
                mCloseView.setBackgroundColor(Color.BLUE);
                mIsChange = false;
            }
            if (touchState== MotionEvent.ACTION_UP) {
                setVisibility(View.GONE);
                sendVoice();
            }
        }
    }

    private void sendVoice() {
        Toast.makeText(getContext(),"发送语音",Toast.LENGTH_SHORT).show();

    }

    private void closeVoiceUI() {
        Toast.makeText(getContext(),"关闭语音输入",Toast.LENGTH_SHORT).show();
    }
}

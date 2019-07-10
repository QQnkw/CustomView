package com.nkw.customview.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nkw.customview.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class BarrageGroupView extends RelativeLayout {
    private int                        mWidth;
    private int                        mHeight;
    private Context                    mContext;
    private HashMap<Integer, TextView> mRowTextViewMap;
    private ArrayList<String>          mRowList;
    private static final int DEFAULT_ROW_NUM = 3;
    private int          mRowNum;
    private Random       mRandom;
    private Handler      mHandler;
    private int          mHeightMeasure;
    private int          mWidthMeasure;
    private List<String> mList;
    private boolean isRunning         = true;
    private int     MESSAGE_ANIMATION = 123;

    public BarrageGroupView(Context context) {
        this(context, null);
    }

    public BarrageGroupView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BarrageGroupView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mContext = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BarrageGroupView);
        mRowNum = typedArray.getInt(R.styleable.BarrageGroupView_rowNum, DEFAULT_ROW_NUM);
        mRowList = new ArrayList<>();
        mRandom = new Random();
        mWidthMeasure = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        mHeightMeasure = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        mRowTextViewMap = new HashMap<>();
        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == MESSAGE_ANIMATION) {
                    if (isRunning && mList != null && !mList.isEmpty()) {
                        int showRowNum = mRandom.nextInt(100) % mRowNum;
                        while (mRowList.contains(showRowNum + "")) {
                            if (mRowList.size() == mRowNum) {
                                mHandler.sendEmptyMessageDelayed(MESSAGE_ANIMATION, 500);
                                //                            mHandler.post(this);
                                return;
                            } else {
                                showRowNum = mRandom.nextInt(100) % mRowNum;
                            }

                            //                            LogUtils.d("NKW--->弹幕--->"+showRowNum);
                        }
                        TextView textView = mRowTextViewMap.get(showRowNum);
                        if (textView == null) {
                            mHandler.sendEmptyMessageDelayed(MESSAGE_ANIMATION, 500);
                            //                            LogUtils.d("NKW--->弹幕--->控件为空");
                            return;
                        }
                        mRowList.add(showRowNum + "");
                        if (index >= mList.size()) {
                            index %= mList.size();
                        }
                        String text = mList.get(index);
                        textView.setText(text);
                        textView.setTag(showRowNum + "");
                        addView(textView);
                        index++;
                        animatorAlphaAndTranslation(textView);
                        mHandler.sendEmptyMessageDelayed(MESSAGE_ANIMATION, 1000);
                    }
                }
            }
        };
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        createRowTextView();
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        int childCount = this.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View view = getChildAt(i);
            if (view != null) {
                LayoutParams lp = (LayoutParams) view.getLayoutParams();
                if (lp.leftMargin <= 0) {
                    view.layout(mWidth, lp.topMargin, mWidth + view.getMeasuredWidth(),
                            lp.topMargin + view.getMeasuredHeight());
                }
            }
        }
    }

    public void setDataList(List<String> list) {
        mList = list;
    }

    private void createRowTextView() {
        TextView textView = new TextView(mContext);
        textView.setText("作为测量高度使用");
        textView.setGravity(Gravity.CENTER);
        textView.measure(mWidthMeasure, mHeightMeasure);
        int height = textView.getMeasuredHeight();
        int rowHeight = mHeight / mRowNum;
        int difHeight = 0;
        if (rowHeight >= height) {
            difHeight = rowHeight - height;
        } else {
            while (rowHeight < height) {
                mRowNum = mRowNum - 1;
                if (mRowNum == 0) {
                    //当控件高度小于textView高度时
                    return;
                }
                rowHeight = mHeight / mRowNum;
            }
            difHeight = rowHeight - height;
        }
        //多余的空间设置给textView的padding
        textView.setPadding(difHeight / 2, difHeight / 2, difHeight / 2, difHeight / 2);
        //        textView.setBackgroundResource(R.color.app_blue3A75ED);
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.topMargin = (mRowNum - 1) * (height + difHeight);
        textView.setLayoutParams(lp);
        mRowTextViewMap.put(mRowNum - 1, textView);
        for (int i = (mRowNum - 2); i >= 0; i--) {
            TextView tv = new TextView(mContext);
            tv.setGravity(Gravity.CENTER);
            tv.setPadding(difHeight / 2, difHeight / 2, difHeight / 2, difHeight / 2);
            //            tv.setBackgroundResource(R.color.app_blue3A75ED);
            LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            params.topMargin = i * (height + difHeight);
            tv.setLayoutParams(params);
            mRowTextViewMap.put(i, tv);
        }
    }

    private int index = 0;

    public void startBarrage() {
        isRunning = true;
        if (!mHandler.hasMessages(MESSAGE_ANIMATION)) {
            mHandler.sendEmptyMessageDelayed(MESSAGE_ANIMATION, 500);
        }
    }

    public void stopBarrage() {
        isRunning = false;
        mHandler.removeMessages(MESSAGE_ANIMATION);
    }

    private void animatorAlphaAndTranslation(final TextView textView) {
        textView.measure(mWidthMeasure, mHeightMeasure);
        int width = textView.getMeasuredWidth();
        ObjectAnimator alpha = ObjectAnimator.ofFloat(textView, "alpha", 0f, 1f, 0f);
        ObjectAnimator translationX = ObjectAnimator.ofFloat(textView, "translationX", 0, -(mWidth + width + 10));
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                String tag = (String) textView.getTag();
                mRowList.remove(tag);
                BarrageGroupView.this.removeView(textView);

            }
        });
        animatorSet.play(alpha).with(translationX);
        animatorSet.setDuration(5000);
        animatorSet.setInterpolator(new LinearInterpolator());
        animatorSet.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mHandler.removeMessages(MESSAGE_ANIMATION);
    }
}
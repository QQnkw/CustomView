package com.nkw.customview.activity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.widget.ImageView;
import android.widget.TextView;

import com.nkw.customview.R;
import com.nkw.customview.adapter.TigerAdapter;
import com.nkw.customview.view.tagerGame.OnWheelScrollListener;
import com.nkw.customview.view.tagerGame.WheelView;

import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;

public class TigerGameActivity extends BaseActivity {

    @BindView(R.id.thousand_wheel_view)
    WheelView mThousandWheelView;
    @BindView(R.id.hundred_wheel_view)
    WheelView mHundredWheelView;
    @BindView(R.id.ten_wheel_view)
    WheelView mTenWheelView;
    @BindView(R.id.num_wheel_view)
    WheelView mNumWheelView;
    @BindView(R.id.tv_num)
    TextView mTvNum;
    @BindView(R.id.iv_tiger_bg)
    ImageView mIvTigerBg;
    private Random mRandom;
    private OnWheelScrollListener mOnWheelScrollListener = new OnWheelScrollListener() {
        @Override
        public void onScrollingStarted(WheelView wheel) {

        }

        @Override
        public void onScrollingFinished(WheelView wheel) {

        }
    };

    public static void startActivity(BaseActivity activity) {
        activity.startActivity(new Intent(activity, TigerGameActivity.class));
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_tiger_game;
    }

    @Override
    protected void initViewSet() {
        mRandom = new Random();
        initWheel(mThousandWheelView);
        initWheel(mNumWheelView);
        initWheel(mTenWheelView);
        initWheel(mHundredWheelView);

        mIvTigerBg.setImageResource(R.drawable.tiger_game_begin_bg);
        ((AnimationDrawable) mIvTigerBg.getDrawable()).start();
    }

    @OnClick(R.id.iv_tiger_bg)
    public void onViewClicked() {
        mIvTigerBg.setImageResource(R.drawable.tiger_game_after_bg);
        ((AnimationDrawable) mIvTigerBg.getDrawable()).start();
        int newNum = mRandom.nextInt(9999) + 1;
        String numStr = String.valueOf(newNum);
        mTvNum.setText(numStr);
        switch (numStr.length()) {
            case 1:
                mThousandWheelView.scroll(30, 1000);
                mHundredWheelView.scroll(50, 2000);
                mTenWheelView.scroll(70, 3000);
                mNumWheelView.scroll(90 + newNum, 5000);
                break;
            case 2:
                mThousandWheelView.scroll(30, 1000);
                mHundredWheelView.scroll(50, 2000);
                int ten2 = Integer.parseInt(String.valueOf(numStr.charAt(0)));
                mTenWheelView.scroll(70 + ten2, 3000);
                int num2 = Integer.parseInt(String.valueOf(numStr.charAt(1)));
                mNumWheelView.scroll(90 + num2, 5000);
                break;
            case 3:
                mThousandWheelView.scroll(30, 1000);
                int hundred3 = Integer.parseInt(String.valueOf(numStr.charAt(0)));
                mHundredWheelView.scroll(50 + hundred3, 2000);
                int ten3 = Integer.parseInt(String.valueOf(numStr.charAt(1)));
                mTenWheelView.scroll(70 + ten3, 3000);
                int num3 = Integer.parseInt(String.valueOf(numStr.charAt(2)));
                mNumWheelView.scroll(90 + num3, 5000);
                break;
            case 4:
                int thousand4 = Integer.parseInt(String.valueOf(numStr.charAt(0)));
                mThousandWheelView.scroll(30 + thousand4, 1000);
                int hundred4 = Integer.parseInt(String.valueOf(numStr.charAt(1)));
                mHundredWheelView.scroll(50 + hundred4, 2000);
                int ten4 = Integer.parseInt(String.valueOf(numStr.charAt(2)));
                mTenWheelView.scroll(70 + ten4, 3000);
                int num4 = Integer.parseInt(String.valueOf(numStr.charAt(3)));
                mNumWheelView.scroll(90 + num4, 5000);
                break;
        }
    }

    /**
     * 初始化轮子
     */
    private void initWheel(WheelView wheel) {
        wheel.setViewAdapter(new TigerAdapter());
        wheel.setVisibleItems(3);
        wheel.setCyclic(true);
        wheel.setEnabled(false);
        wheel.setDrawShadows(false);
    }

    @Override
    protected void onDestroy() {
        ((AnimationDrawable) mIvTigerBg.getDrawable()).stop();
        super.onDestroy();
    }
}
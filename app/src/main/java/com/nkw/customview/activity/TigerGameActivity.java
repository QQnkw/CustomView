package com.nkw.customview.activity;

import android.widget.Button;
import android.widget.TextView;

import com.nkw.customview.R;
import com.nkw.customview.adapter.TigerAdapter;
import com.nkw.customview.view.tagerGame.OnWheelScrollListener;
import com.nkw.customview.view.tagerGame.WheelView;

import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;

public class TigerGameActivity extends BaseActivity {

    @BindView(R.id.hundred_wheel_view)
    WheelView mHundredWheelView;
    @BindView(R.id.ten_wheel_view)
    WheelView mTenWheelView;
    @BindView(R.id.num_wheel_view)
    WheelView mNumWheelView;
    @BindView(R.id.tv_num)
    TextView mTvNum;
    @BindView(R.id.start_scroll_view)
    Button mStartScrollView;
    private Random mRandom;
    private OnWheelScrollListener mOnWheelScrollListener = new OnWheelScrollListener() {
        @Override
        public void onScrollingStarted(WheelView wheel) {

        }

        @Override
        public void onScrollingFinished(WheelView wheel) {

        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_tiger_game;
    }

    @Override
    protected void initViewSet() {
        mRandom = new Random();
    }

    @OnClick(R.id.start_scroll_view)
    public void onViewClicked() {
        int num = mRandom.nextInt(999) + 1;
        String numStr = String.valueOf(num);
        mTvNum.setText(numStr);
        for (int i = 0; i < numStr.length(); i++) {

        }
    }

    /**
     * 初始化轮子
     */
    private void initWheel(WheelView wheel, int position) {
        wheel.setViewAdapter(new TigerAdapter());
        wheel.setVisibleItems(3);
        if (position == 3) {
            wheel.addScrollingListener(mOnWheelScrollListener);
        }
        wheel.setCyclic(true);
        wheel.setEnabled(false);
        wheel.setDrawShadows(false);
    }
}
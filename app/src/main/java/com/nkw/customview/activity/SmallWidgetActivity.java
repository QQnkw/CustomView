package com.nkw.customview.activity;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.nkw.customview.R;
import com.nkw.customview.comment.AppLocalData;
import com.nkw.customview.view.AngleImageView;
import com.nkw.customview.view.MatchingTimeDrawable;
import com.nkw.customview.view.RefreshLikeIOSView;
import com.nkw.customview.view.TabLayoutVY;
import com.nkw.customview.view.VyLoading;
import com.nkw.customview.view.VyRadar;
import com.nkw.customview.view.movingimage.VyMovingImageView;

import butterknife.BindView;

public class SmallWidgetActivity extends BaseActivity {
    @BindView(R.id.iv_angle)
    AngleImageView mIvAngle;
    @BindView(R.id.moving_image_view)
    VyMovingImageView mMovingImageView;
    @BindView(R.id.tablayoutVy)
    TabLayoutVY mTablayoutVy;
    @BindView(R.id.RefreshLikeIOSView)
    RefreshLikeIOSView mRefreshLikeIOSView;
    @BindView(R.id.vy_loading)
    VyLoading mVyLoading;
    @BindView(R.id.vy_radar)
    VyRadar mVyRadar;
    @BindView(R.id.iv_custom_drawable)
    ImageView mIvCustomDrawable;
    private Handler mHandler = new Handler();
    private int mNum = 0;

    public static void startActivity(BaseActivity activity) {
        activity.startActivity(new Intent(activity, SmallWidgetActivity.class));
    }

    @Override
    protected void initViewSet() {
        tabLayout();
        likeIOSRefreshView();
        VYLoading();
        initRadar();
        customDrawable();
        movingImage();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_small_widget;
    }

    private void tabLayout() {
        mTablayoutVy = findViewById(R.id.tablayoutVy);
        mTablayoutVy.addSelectorListener(new TabLayoutVY.TabClickedListener() {
            @Override
            public void onTabClicked(int clickedResID) {
                Log.d("NKW", clickedResID + "");
            }
        });
    }

    private void likeIOSRefreshView() {
        mRefreshLikeIOSView = findViewById(R.id.RefreshLikeIOSView);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mNum < 101) {
                    mRefreshLikeIOSView.setProgress(mNum);
                    mNum++;
                    mHandler.postDelayed(this, 50);
                }
            }
        }, 100);
    }

    private void VYLoading() {
        mVyLoading = findViewById(R.id.vy_loading);
        mVyLoading.postDelayed(new Runnable() {
            @Override
            public void run() {
                mVyLoading.startLoading();
            }
        }, 1000);
    }

    private void initRadar() {
        mVyRadar = findViewById(R.id.vy_radar);
        mVyRadar.setInterpolator(new LinearInterpolator());
        mVyRadar.start();
    }

    private void customDrawable() {
        mIvCustomDrawable.setBackground(new MatchingTimeDrawable(this));
    }

    private void movingImage() {
        mMovingImageView = findViewById(R.id.moving_image_view);
        Glide.with(this).load(AppLocalData.imgUrlArr[7]).into(mMovingImageView);
        //        Glide.with(this).load(R.mipmap.vertical_long).into(movingImageView);
    }
}

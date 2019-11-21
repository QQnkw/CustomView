package com.nkw.customview.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.LinearInterpolator;

import com.bumptech.glide.Glide;
import com.nkw.customview.R;
import com.nkw.customview.comment.AppLocalData;
import com.nkw.customview.view.MatchingTimeDrawable;
import com.nkw.customview.view.RefreshLikeIOSView;
import com.nkw.customview.view.TabLayoutVY;
import com.nkw.customview.view.VyLoading;
import com.nkw.customview.view.VyRadar;
import com.nkw.customview.view.movingimage.VyMovingImageView;

public class SmallWidgetActivity extends AppCompatActivity {
    private Handler mHandler = new Handler();
    private int     mNum     = 0;
    public static void startActivity(BaseActivity activity) {
        activity.startActivity(new Intent(activity, SmallWidgetActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_small_widget);
        tabLayout();
        likeIOSRefreshView();
        VYLoading();
        initRadar();
        customDrawable();
        movingImage();
    }

    private void tabLayout() {
        TabLayoutVY tabLayoutVY = findViewById(R.id.tablayoutVy);
        tabLayoutVY.addSelectorListener(new TabLayoutVY.TabClickedListener() {
            @Override
            public void onTabClicked(int clickedResID) {
                Log.d("NKW", clickedResID + "");
            }
        });
    }

    private void likeIOSRefreshView() {
        final RefreshLikeIOSView refreshLikeIOSView = findViewById(R.id.RefreshLikeIOSView);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mNum < 101) {
                    refreshLikeIOSView.setProgress(mNum);
                    mNum++;
                    mHandler.postDelayed(this,50);
                }
            }
        }, 100);
    }

    private void VYLoading() {
        final VyLoading vyLoading = findViewById(R.id.vy_loading);
        vyLoading.postDelayed(new Runnable() {
            @Override
            public void run() {
                vyLoading.startLoading();
            }
        },1000);
    }

    private void initRadar() {
        final VyRadar vyRadar = findViewById(R.id.vy_radar);
        vyRadar.setInterpolator(new LinearInterpolator());
        vyRadar.start();
    }
    private void customDrawable() {
        findViewById(R.id.iv_custom_drawable).setBackground(new MatchingTimeDrawable(this));
    }
    private void movingImage() {
        VyMovingImageView movingImageView = findViewById(R.id.moving_image_view);
        Glide.with(this).load(AppLocalData.imgUrlArr[7]).into(movingImageView);
    }
}

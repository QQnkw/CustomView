package com.nkw.customview.activity;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.nkw.customview.R;
import com.nkw.customview.view.MatchingTimeDrawable;
import com.nkw.customview.view.RefreshLikeIOSView;
import com.nkw.customview.view.TabLayoutVY;
import com.nkw.customview.view.VyLoading;

public class MainActivity extends BaseActivity {
    private Handler mHandler = new Handler();
    private int     mNum     = 0;

    @Override
    protected void initViewSet() {
        super.initViewSet();
        tablayout();
        likeIOSRefreshView();
        fourGridView();
        VYLoading();
        goWebView();
        goDragLayout();
        goBarrageGroup();
        goBtnRadar();
        goVyWoQu();
        goLayoutManager();
        goCustomCamera();
        findViewById(R.id.iv_custom_drawable).setBackground(new MatchingTimeDrawable(this));
        //		webView.loadUrl("https://tv.sohu.com/upload/static/share/share_play.html#106903751_9114930_0_9001_0");
        //		webView.loadUrl("https://v.qq.com/txp/iframe/player.html?vid=i0737bacynk");
        //        webView.loadUrl("http://player.youku.com/embed/XMzg1MzkyMzk0MA==");
        //爱奇艺视频播放按钮会重叠
        //        webView.loadUrl("http://open.iqiyi.com/developer/player_js/coopPlayerIndex.html?vid=3675f352a8c555371731c1b09ce8d298&tvId=24357822709&accessToken=2.f22860a2479ad60d8da7697274de9346&appKey=3955c3425820435e86d0f4cdfe56f5e7&appId=1368&height=100%&width=100%");
    }

    private void goCustomCamera() {
        findViewById(R.id.btn_custom_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mActivity,CameraActivity.class));
            }
        });
    }

    private void goLayoutManager() {
        findViewById(R.id.btn_layoutManager).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mActivity,CustomLayoutManagerActivity.class));
            }
        });
    }

    private void goVyWoQu() {
        findViewById(R.id.btn_dian_lu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mActivity,VyWoQuActivity.class));
            }
        });
    }

    private void goBtnRadar() {
        findViewById(R.id.btn_radar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BadarActivity.startActivity(mActivity);
            }
        });
    }

    private void goBarrageGroup() {
        findViewById(R.id.btn_barrage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BarrageGroupViewActivity.startActivity(mActivity);
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    private void goDragLayout() {
        findViewById(R.id.btn_dragLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DragActivity.startActivity(mActivity);
            }
        });
    }

    private void goWebView() {
        findViewById(R.id.btn_webView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebViewActivity.startActivity(mActivity);
            }
        });
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

    private void fourGridView() {
        findViewById(R.id.btn_four_grid_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FourGridViewActivity.startActivity(mActivity);
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

    private void tablayout() {
        TabLayoutVY tabLayoutVY = findViewById(R.id.tablayoutVy);
        tabLayoutVY.addSelectorListener(new TabLayoutVY.TabClickedListener() {
            @Override
            public void onTabClicked(int clickedResID) {
                Log.d("NKW", clickedResID + "");
            }
        });
    }


}

package com.nkw.customview.activity;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.nkw.customview.R;
import com.nkw.customview.video.X5WebViewActivity;
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
        goX5webView();
        goDragLayout();
        goBarrageGroup();
        goBtnRadar();
        goVyWoQu();
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

    private void goX5webView() {
        findViewById(R.id.btn_webView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                X5WebViewActivity.startActivity(mActivity);
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

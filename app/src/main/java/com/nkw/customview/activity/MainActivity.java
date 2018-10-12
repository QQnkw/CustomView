package com.nkw.customview.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.nkw.customview.R;
import com.nkw.customview.video.X5WebViewActivity;
import com.nkw.customview.view.RefreshLikeIOSView;
import com.nkw.customview.view.TabLayoutVY;
import com.nkw.customview.view.VyLoading;

public class MainActivity extends AppCompatActivity {
    private Handler mHandler = new Handler();
    private int     mNum     = 0;
    private MainActivity mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        tablayout();
        likeIOSRefreshView();
        fourGridView();
        VYLoading();
        goX5webView();
    }

    private void goX5webView() {
        findViewById(R.id.btn_webView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                X5WebViewActivity.startActivity(mContext);
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
                FourGridViewActivity.startActivity(mContext);
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

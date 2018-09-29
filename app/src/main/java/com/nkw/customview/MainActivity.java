package com.nkw.customview;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.nkw.customview.view.RefreshLikeIOSView;
import com.nkw.customview.view.TabLayoutVY;

public class MainActivity extends AppCompatActivity {
    private Handler mHandler = new Handler();
    private int     mNum     = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tablayout();
        likeIOSRefreshView();
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

package com.nkw.customview.activity;

import android.support.v7.app.AppCompatDelegate;
import android.view.View;

import com.nkw.customview.R;
import com.nkw.customview.activity.camera.CameraActivity;

public class MainActivity extends BaseActivity implements View.OnClickListener {


    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViewSet() {
        super.initViewSet();

        findViewById(R.id.btn_small_widget).setOnClickListener(this);
        findViewById(R.id.btn_four_grid_view).setOnClickListener(this);
        findViewById(R.id.btn_webView).setOnClickListener(this);
        findViewById(R.id.btn_dragLayout).setOnClickListener(this);
        findViewById(R.id.btn_barrage).setOnClickListener(this);
        findViewById(R.id.btn_layoutManager).setOnClickListener(this);
        findViewById(R.id.btn_night).setOnClickListener(this);
        findViewById(R.id.btn_custom_camera).setOnClickListener(this);
        findViewById(R.id.btn_custom_camera).setOnClickListener(this);
        findViewById(R.id.btn_voice_ui).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_small_widget:
                SmallWidgetActivity.startActivity(mActivity);
                break;
            case R.id.btn_four_grid_view:
                FourGridViewActivity.startActivity(mActivity);
                break;
            case R.id.btn_webView:
                WebViewActivity.startActivity(mActivity);
                break;
            case R.id.btn_dragLayout:
                DragActivity.startActivity(mActivity);
                break;
            case R.id.btn_barrage:
                BarrageGroupViewActivity.startActivity(mActivity);
                break;
            case R.id.btn_layoutManager:
                CustomLayoutManagerActivity.startActivity(mActivity);
                break;
            case R.id.btn_night:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                recreate();
                break;
            case R.id.btn_custom_camera:
                CameraActivity.startActivity(mActivity);
                break;
            case R.id.btn_voice_ui:
                VoiceUIAndRecorderActivity.startActivity(mActivity);
//                LikeWXRecorderActivity.startActivity(mActivity);
                break;
        }
    }
}

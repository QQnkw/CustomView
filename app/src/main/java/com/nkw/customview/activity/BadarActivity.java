package com.nkw.customview.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.LinearInterpolator;

import com.nkw.customview.R;
import com.nkw.customview.view.VyRadar;

public class BadarActivity extends BaseActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_badar;
    }

    public static void startActivity(BaseActivity activity) {
        activity.startActivity(new Intent(activity,BadarActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final VyRadar vyRadar = findViewById(R.id.vy_radar);
        vyRadar.setInterpolator(new LinearInterpolator());
        vyRadar.start();
    }
}

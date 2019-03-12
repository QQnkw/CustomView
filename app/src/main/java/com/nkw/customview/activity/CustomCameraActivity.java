package com.nkw.customview.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.nkw.customview.R;

public class CustomCameraActivity extends AppCompatActivity {

    private CustomCameraActivity mActivity;
    private int                  CAMERA_ONE = 111;
    private int                  CAMERA_TWO = 222;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_camera);
        mActivity = this;
        findViewById(R.id.btn_camera1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(mActivity, Camera1Activity.class), CAMERA_ONE);
            }
        });
        findViewById(R.id.btn_camera2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(mActivity, Camera2Activity.class), CAMERA_TWO);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_ONE) {

        } else if (requestCode == CAMERA_TWO) {

        }
    }
}

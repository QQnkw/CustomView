package com.nkw.customview.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.nkw.customview.R;
import com.nkw.customview.camera.camera1.Camera1Preview;

public class Camera1Activity extends AppCompatActivity implements Camera.PictureCallback{

    private Camera1Preview mCamera1Preview;
    private Button         mBtnTakePhoto;
    private Button         mBtnReverseCamera;
    private Button         mBtnFlashLight;
    private ImageView      mIvThumbnail;
    private Button         mBtnRecordVideo;
    private ImageView      mIvPreview;
    private Button mBtnExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View decorView6 = getWindow().getDecorView();
        int uiOptions6 = View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView6.setSystemUiVisibility(uiOptions6);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_camera1);
        mCamera1Preview = findViewById(R.id.camera1_preview);
        mCamera1Preview.bindActivity(this);
        mIvThumbnail = findViewById(R.id.iv_thumbnail);
        mBtnTakePhoto = findViewById(R.id.btn_take_photo);
        mBtnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCamera1Preview.takePicture(Camera1Activity.this);
            }
        });
        mBtnRecordVideo = findViewById(R.id.btn_record_video);
        mBtnReverseCamera = findViewById(R.id.btn_reverse_camera);
        mBtnFlashLight = findViewById(R.id.btn_flash_light);
        mIvPreview = findViewById(R.id.iv_preview);
        mBtnExit = findViewById(R.id.btn_exit);
        mBtnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction()==MotionEvent.ACTION_DOWN) {
            //设置聚焦
            Point point = new Point((int) event.getX(), (int) event.getY());
            mCamera1Preview.onFocus(point, null);
        }
        return true;
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        mIvPreview.setVisibility(View.VISIBLE);
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        Bitmap bitmap1 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
        mIvPreview.setImageBitmap(bitmap1);
    }
}

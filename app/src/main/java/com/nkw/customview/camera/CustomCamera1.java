package com.nkw.customview.camera;


import android.hardware.Camera;

public class CustomCamera1 extends BaseCamera{

    private final Camera mCamera;

    public CustomCamera1(){
        mCamera = Camera.open();
    }
    @Override
    protected void takePhoto(String filePath, CameraListener listener) {

    }

    @Override
    protected void closeCamera() {

    }
}

package com.nkw.customview.camera;

public abstract class  BaseCamera {
    protected abstract void takePhoto(String filePath, CameraListener listener);
    protected abstract void closeCamera();
}

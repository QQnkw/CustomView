package com.nkw.customview.camera.camera1;

import android.app.Activity;
import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

public class Camera1Preview extends SurfaceView implements SurfaceHolder.Callback {
    private static final String         TAG = Camera1Preview.class.getSimpleName();
    private final        Context        mContext;
    private              Camera         mCamera1;
    private              Camera1Manager mCamera1Manager;
    private              int            mFacing;
    private Activity mActivity;

    public Camera1Preview(Context context) {
        this(context, null);
    }

    public Camera1Preview(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Camera1Preview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mCamera1Manager = Camera1Manager.getInstance(context);
        mFacing = Camera.CameraInfo.CAMERA_FACING_BACK;
        setFocusable(true);
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG,"surfaceCreated");
        if (mCamera1 == null) {
           setUpCamera(mFacing,false);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        try {
            releaseCamera();

            //释放资源
            if (holder != null) {
                if (Build.VERSION.SDK_INT >= 14) {
                    holder.getSurface().release();
                }
            }
        } catch (Exception e) {
            //相机已经关了
            e.printStackTrace();
        }
    }

    private void releaseCamera() {
        mCamera1Manager.releaseCamera(mCamera1);
        mCamera1 = null;
    }


    /**
     * 设置当前的Camera 并进行参数设置
     *
     * @param facing 前置摄像头还是后置摄像头{@Link CAMERA_FRONT#CAMERA_BACKG}
     */
    public void setUpCamera(int facing, boolean isSwitchFromFront) {
        try {
            // android 6.0以下 部分手机如果拒绝权限时，无法获取拒绝权限的监听，此时mCamera可能为null
            mCamera1 = mCamera1Manager.openCamera(facing);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mCamera1 != null) {
            try {
                mCamera1.setPreviewDisplay(getHolder());
                initCamera();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            //            toast("切换失败，请重试！", Toast.LENGTH_LONG);
        }
    }

    private void initCamera() {
        //获取相机参数
        Camera.Parameters camera1Parameters = mCamera1.getParameters();
        //设置照片格式
        camera1Parameters.setPictureFormat(ImageFormat.JPEG);
        //设置预览的参数
        mCamera1Manager.setFitPreSize(mCamera1,camera1Parameters);
        // 设置图片的宽高比预览的一样
        Camera.Size preSize = mCamera1.getParameters().getPreviewSize();
        mCamera1Manager.setFitPicSize(mCamera1, camera1Parameters,preSize);
        //调整控件的布局  防止预览被拉伸
        adjustView(preSize);
        //调整画面的角度
        determineDisplayOrientation();
        //开启预览
        mCamera1.startPreview();
    }

    /**
     * Determine the current display orientation and rotate the camera preview
     * accordingly
     */
    private void determineDisplayOrientation() {

        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        Camera.getCameraInfo(mFacing, cameraInfo);


        int rotation = mActivity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;

        switch (rotation) {
            case Surface.ROTATION_0: {
                degrees = 0;
                break;
            }
            case Surface.ROTATION_90: {
                degrees = 90;
                break;
            }
            case Surface.ROTATION_180: {
                degrees = 180;
                break;
            }
            case Surface.ROTATION_270: {
                degrees = 270;
                break;
            }
        }

        int displayOrientation;

        // Camera direction
        if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            // Orientation is angle of rotation when facing the camera for
            // the camera image to match the natural orientation of the device
            displayOrientation = (cameraInfo.orientation + degrees) % 360;
            displayOrientation = (360 - displayOrientation) % 360;
        } else {
            displayOrientation = (cameraInfo.orientation - degrees + 360) % 360;
        }

//        mDisplayOrientation = (cameraInfo.orientation - degrees + 360) % 360;
//        mLayoutOrientation = degrees;

        mCamera1.setDisplayOrientation(displayOrientation);

        Log.d(TAG, "displayOrientation:" + displayOrientation);
    }

    private void adjustView(Camera.Size preSize) {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) getLayoutParams();
        params.width = mCamera1Manager.mScreenHeight*preSize.height/preSize.width;
        params.height = mCamera1Manager.mScreenHeight;
        setLayoutParams(params);
    }
    public void bindActivity(Activity activity) {
        mActivity = activity;
    }

    /**
     * 手动聚焦
     *
     * @param point 触屏坐标
     */
    public boolean onFocus(Point point, Camera.AutoFocusCallback callback) {
        if (mCamera1 == null) {
            return false;
        }

        Camera.Parameters parameters = null;
        try {
            parameters = mCamera1.getParameters();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        //不支持设置自定义聚焦，则使用自动聚焦，返回

        if (Build.VERSION.SDK_INT >= 14) {

            if (parameters.getMaxNumFocusAreas() <= 0) {
                return focus(callback);
            }

            Log.d(TAG, "onCameraFocus:" + point.x + "," + point.y);

            List<Camera.Area> areas = new ArrayList<Camera.Area>();
            int left = point.x - 300;
            int top = point.y - 300;
            int right = point.x + 300;
            int bottom = point.y + 300;
            left = left < -1000 ? -1000 : left;
            top = top < -1000 ? -1000 : top;
            right = right > 1000 ? 1000 : right;
            bottom = bottom > 1000 ? 1000 : bottom;
            areas.add(new Camera.Area(new Rect(left, top, right, bottom), 100));
            // important
            parameters.setFocusAreas(areas);
            try {
                // 使用的小米手机在设置聚焦区域的时候经常会出异常，看日志发现是框架层的字符串转int的时候出错了，
                // 目测是小米修改了框架层代码导致，在此try掉，对实际聚焦效果没影响
                mCamera1.setParameters(parameters);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }


        return focus(callback);
    }

    private boolean focus(Camera.AutoFocusCallback callback) {
        try {
            if (mCamera1 != null) {
                mCamera1.autoFocus(callback);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 拍照
     */
    public boolean takePicture(Camera.PictureCallback callback) {
        try {
            mCamera1.takePicture(null, null, callback);


        } catch (Throwable t) {
            t.printStackTrace();
            Log.d(TAG, "photo fail after Photo Clicked");

            return false;
        }
        try {
            // fixme
            // mCamera.startPreview();
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return true;
    }
}

package com.nkw.customview.camera.camera1;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.hardware.Camera;
import android.os.Build;
import android.util.Log;
import android.view.WindowManager;

import com.nkw.customview.utils.Utils;

import java.util.List;

public class Camera1Manager {
    private static String         TAG = Camera1Manager.class.getSimpleName();
    private static Camera1Manager sCameraManager;
    private final  Context        mContext;
    public         int            mScreenWidth, mScreenHeight;

    private Camera1Manager(Context context) {
        mContext = context;
        getScreenWidthAndHeight(context);
    }

    private void getScreenWidthAndHeight(Context context) {
        WindowManager wm = (WindowManager) Utils.getApp().getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) {
            mScreenWidth = context.getResources().getDisplayMetrics().widthPixels;
            mScreenHeight = context.getResources().getDisplayMetrics().heightPixels;
        }
        Point point = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            wm.getDefaultDisplay().getRealSize(point);
        } else {
            wm.getDefaultDisplay().getSize(point);
        }
        mScreenWidth = point.x;
        mScreenHeight = point.y;
    }

    public static Camera1Manager getInstance(Context context) {
        if (sCameraManager == null) {
            synchronized (Camera1Manager.class) {
                if (sCameraManager == null) {
                    sCameraManager = new Camera1Manager(context);
                }
            }
        }
        return sCameraManager;
    }

    public Camera openCamera(int facing) {
        Camera camera1 = null;
        if (checkCameraHardware(mContext)) {
            camera1 = Camera.open(getCameraId(facing));
        }
        return camera1;
    }

    private int getCameraId(int facing) {
        //有多少摄像头
        int numberOfCameras = Camera.getNumberOfCameras();
        Camera.CameraInfo info = new Camera.CameraInfo();
        for (int id = 0; id < numberOfCameras; id++) {
            //获取摄像头的信息
            Camera.getCameraInfo(id, info);
            if (info.facing == facing) {
                return id;
            }
        }
        return -1;
    }

    /**
     * 判断相机是否支持
     *
     * @param context
     * @return
     */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 设置合适的预览尺寸
     *
     * @param camera1
     */
    void setFitPreSize(Camera camera1, Camera.Parameters camera1Parameters) {
        try {
            Camera.Size adapterSize = findFitPreResolution(camera1Parameters);
            camera1Parameters.setPreviewSize(adapterSize.width, adapterSize.height);
            camera1.setParameters(camera1Parameters);

            Log.d(TAG, "setFitPreSize:" + adapterSize.width + "*" + adapterSize.height);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 返回合适的预览尺寸参数
     *
     * @param
     * @return
     */
    private Camera.Size findFitPreResolution(Camera.Parameters camera1Parameters) {
        List<Camera.Size> supportedPicResolutions = camera1Parameters.getSupportedPreviewSizes();

        Camera.Size resultSize = null;
        for (Camera.Size size : supportedPicResolutions) {
            Log.d(TAG, "pre--->width:" + size.width + "----height:" + size.height);
            if (size.height * 1f / size.width == mScreenWidth * 1f / mScreenHeight) {
                if (resultSize == null) {
                    resultSize = size;
                } else {
                    if (size.width > resultSize.width && size.height > resultSize.height) {
                        resultSize = size;
                    }
                }
            }
        }
        if (resultSize == null) {
            return supportedPicResolutions.get(0);
        }
        return resultSize;
    }

    public void setFitPicSize(Camera camera1, Camera.Parameters camera1Parameters, Camera.Size preSize) {
        try {
            Camera.Size adapterSize = findFitPicResolution(camera1Parameters, preSize);
            camera1Parameters.setPictureSize(adapterSize.width, adapterSize.height);
            camera1.setParameters(camera1Parameters);

            Log.d(TAG, "setFitPicSize:" + adapterSize.width + "*" + adapterSize.height);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 返回合适的预览尺寸参数
     *
     * @param
     * @param preSize
     * @return
     */
    private Camera.Size findFitPicResolution(Camera.Parameters camera1Parameters, Camera.Size preSize) {
        List<Camera.Size> supportedPictureSizes = camera1Parameters.getSupportedPictureSizes();
        Camera.Size resultSize = null;
        for (Camera.Size size : supportedPictureSizes) {
            Log.d(TAG, "pic--->width:" + size.width + "----height:" + size.height);
            if ((size.width * 1f / size.height == preSize.width * 1f / preSize.height)) {
                if (resultSize == null) {
                    resultSize = size;
                } else {
                    if (size.width > resultSize.width && size.height > resultSize.height) {
                        resultSize = size;
                    }
                }
            }
        }
        if (resultSize == null) {
            return supportedPictureSizes.get(0);
        }
        return resultSize;
    }

    public void releaseCamera(Camera camera1) {
        if (camera1 != null) {
            try {
                camera1.setPreviewCallback(null);
                camera1.setPreviewCallbackWithBuffer(null);
                camera1.stopPreview();
                camera1.release();
                camera1 = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

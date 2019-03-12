package com.nkw.customview.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.nkw.customview.R;
import com.nkw.customview.utils.PhotoBitmapUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import io.reactivex.functions.Consumer;

public class CameraActivity extends AppCompatActivity {

    private ImageView       mImageView;
    private File          mFile;
    private FileInputStream mFis;
    private CameraActivity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        mActivity = this;
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {

                        } else {

                        }
                    }
                });
        systemCamera();
        customCamera();
    }

    private void customCamera() {
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mActivity,CustomCameraActivity.class));
            }
        });
    }

    private void systemCamera() {
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri;
                if (Build.VERSION.SDK_INT >= 24) {
                    uri = FileProvider.getUriForFile(mActivity, "com.nkw.customview.fileprovider", mFile);
                } else {
                    uri = Uri.fromFile(mFile);
                }
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(intent, 1);
            }
        });
        mImageView = findViewById(R.id.imageView);
        File externalFilesDir = mActivity.getExternalFilesDir(Environment.DIRECTORY_DCIM);
        mFile = new File(externalFilesDir, "/images/test.jpg");
        if (!mFile.getParentFile().exists()) {
            mFile.getParentFile().mkdirs();
        }
        showBitmap();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            showBitmap();
        }
    }

    private void showBitmap() {
        try {
            mFis = new FileInputStream(mFile);
            Bitmap bitmap = BitmapFactory.decodeStream(mFis);
            int degree = PhotoBitmapUtils.readPictureDegree(mFile.getAbsolutePath());
            Bitmap rotaingImageView = PhotoBitmapUtils.rotaingImageView(degree, bitmap);
            mImageView.setImageBitmap(rotaingImageView);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (mFis != null)
                    mFis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

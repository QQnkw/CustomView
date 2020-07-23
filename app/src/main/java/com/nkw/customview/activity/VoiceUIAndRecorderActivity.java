package com.nkw.customview.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.nkw.customview.R;
import com.nkw.customview.manager.VoiceRecorderManager;
import com.nkw.customview.view.VoiceUiView;


public class VoiceUIAndRecorderActivity extends BaseActivity implements View.OnClickListener {

    private VoiceRecorderManager mVoiceRecorderManager;

    public static void startActivity(BaseActivity activity) {
        activity.startActivity(new Intent(activity, VoiceUIAndRecorderActivity.class));
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_voice_ui_and_recorder;
    }

    @Override
    protected void initViewSet() {
        checkPermission();
    }

    private void initRecorder() {
        final VoiceUiView voiceUI = findViewById(R.id.voice_ui);
        Button btnStart = findViewById(R.id.btn_start);
        Button btnCancel = findViewById(R.id.btn_cancel);
        Button btnStop = findViewById(R.id.btn_stop);
        mVoiceRecorderManager = VoiceRecorderManager.getInstance();
        mVoiceRecorderManager.setVoiceFileDirPath(getExternalFilesDir(Environment.DIRECTORY_MUSIC).getAbsolutePath());
        mVoiceRecorderManager.setRecorderSateListener(new VoiceRecorderManager.RecorderSateListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onStop() {
                voiceUI.resetLineHeight();
            }

            @Override
            public void onCancel() {
                voiceUI.resetLineHeight();
            }

            @Override
            public void decibelPercentageListener(float value) {
                voiceUI.setLineData(value);
            }

            @Override
            public void recordTime(long countTime) {
                Toast.makeText(VoiceUIAndRecorderActivity.this, "耗时" + countTime, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onVoiceTimeTooShort() {
                Toast.makeText(VoiceUIAndRecorderActivity.this, "录音时间太短", Toast.LENGTH_SHORT).show();
            }
        });

        btnStart.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnStop.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
                mVoiceRecorderManager.startRecord();
                break;
            case R.id.btn_stop:
                mVoiceRecorderManager.stopRecorder();
                break;
            case R.id.btn_cancel:
                mVoiceRecorderManager.cancelRecorder();
                break;
        }
    }

    /**
     * 权限申请
     */
    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] permissions = new String[]{Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, permissions, 200);
                    return;
                }
            }
        }
        initRecorder();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && requestCode == 200) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivityForResult(intent, 200);
                    return;
                }
            }
            initRecorder();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 200) {
            checkPermission();
        }
    }
}
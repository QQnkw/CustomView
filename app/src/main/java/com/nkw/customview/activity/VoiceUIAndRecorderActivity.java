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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nkw.customview.R;
import com.nkw.customview.manager.VoicePlayerManager;
import com.nkw.customview.manager.VoiceRecorderManager;
import com.nkw.customview.view.VoiceUiView;

import java.util.ArrayList;


public class VoiceUIAndRecorderActivity extends BaseActivity implements View.OnClickListener, View.OnLongClickListener {

    public static final String TAG = "VoiceUIAndRecorder";
    private VoiceRecorderManager mVoiceRecorderManager;
    private VoiceUiView mVoiceUI;
    private VoicePlayerManager mVoicePlayerManager;

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

    private void initUI() {
        mVoiceUI = findViewById(R.id.voice_ui);
        recorderUI();
        playVoice();
    }

    private void playVoice() {
        Button btnStartMusic = findViewById(R.id.btn_start_music);
        Button btnPauseMusic = findViewById(R.id.btn_pause_music);
        Button btnStopMusic = findViewById(R.id.btn_stop_music);
        Button btnResetMusic = findViewById(R.id.btn_reset_music);
        Button btnNextMusic = findViewById(R.id.btn_next_music);
        Button btnPreMusic = findViewById(R.id.btn_pre_music);
        final TextView tvSongName = findViewById(R.id.tv_song_name);
        final TextView tvSongTime = findViewById(R.id.tv_song_time);
        SeekBar seekBar = findViewById(R.id.seekBar);
        final ProgressBar playProgress = findViewById(R.id.play_progress);
        final ProgressBar prepareProgress = findViewById(R.id.prepare_progress);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d(TAG, "onProgressChanged-->progress:" + progress + "fromUser:" + fromUser);
                mVoicePlayerManager.seekToPlay(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.d(TAG, "onStartTrackingTouch");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d(TAG, "onStopTrackingTouch");
            }
        });
        btnStartMusic.setOnClickListener(this);
        btnPauseMusic.setOnClickListener(this);
        btnStopMusic.setOnClickListener(this);
        btnResetMusic.setOnClickListener(this);
        btnNextMusic.setOnClickListener(this);
        btnPreMusic.setOnClickListener(this);
        mVoicePlayerManager = VoicePlayerManager.getInstance();
        mVoicePlayerManager.setVoicePlayerStatusListener(new VoicePlayerManager.VoicePlayerStatusListener() {
            @Override
            public void onStatusChange(int status) {
                String text = "";
                switch (status) {
                    case -1:
                        text = "初始状态";
                        break;
                    case 0:
                        text = "切换语音";
                        break;
                    case 1:
                        text = "正在播放";
                        break;
                    case 2:
                        text = "暂停播放";
                        break;
                    case 3:
                        text = "播放完成";
                        break;
                    case 4:
                        text = "重置";
                        break;
                    case 5:
                        text = "错误";
                        break;
                    case 6:
                        text = "准备完成";
                        break;
                }
                Log.d(TAG, "onStatusChange:" + text);
            }

            @Override
            public void onPlayPositionChanged(int position) {
                playProgress.setProgress(position);

            }

            @Override
            public void onPlayTotalDuration(int duration) {
                tvSongTime.setText("总时间:" + duration);
            }

            @Override
            public void onPlayName(String name) {
                tvSongName.setText("名称:" + name);
            }

            @Override
            public void onPreparedPositionChange(int position) {
                prepareProgress.setProgress(position);

            }
        });
        ArrayList<String> musicList = new ArrayList<>();
        musicList.add("http://ting6.yymp3.net:82/new27/dazhuan/1.mp3");
        musicList.add("http://ting6.yymp3.net:82/new27/gdys/1.mp3");
        musicList.add("http://ting6.yymp3.net:82/new27/mljyyj/1.mp3");
        //测试错误语音
        musicList.add("http://ting6.yymp3.net:82");
        mVoicePlayerManager.setPlayNetVoiceSource(musicList);
    }

    private void recorderUI() {
        Button btnStartRecorder = findViewById(R.id.btn_start_recorder);
        Button btnCancelRecorder = findViewById(R.id.btn_cancel_recorder);
        Button btnStopRecorder = findViewById(R.id.btn_stop_recorder);
        CustomButton btnVoice = findViewById(R.id.btn_voice);
        mVoiceRecorderManager = VoiceRecorderManager.getInstance();
        mVoiceRecorderManager.setVoiceFileDirPath(getExternalFilesDir(Environment.DIRECTORY_MUSIC).getAbsolutePath());
        mVoiceRecorderManager.setRecorderSateListener(new VoiceRecorderManager.RecorderSateListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onStop() {
                mVoiceUI.resetLineHeight();
            }

            @Override
            public void onCancel() {
                mVoiceUI.resetLineHeight();
            }

            @Override
            public void decibelPercentageListener(float value) {
                mVoiceUI.setLineData(value);
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
        btnStartRecorder.setOnClickListener(this);
        btnCancelRecorder.setOnClickListener(this);
        btnStopRecorder.setOnClickListener(this);
        btnVoice.setOnLongClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start_recorder:
                mVoiceRecorderManager.startRecord();
                break;
            case R.id.btn_stop_recorder:
                mVoiceRecorderManager.stopRecorder();
                break;
            case R.id.btn_cancel_recorder:
                mVoiceRecorderManager.cancelRecorder();
                break;
            case R.id.btn_start_music:
                mVoicePlayerManager.startPlay();
                break;
            case R.id.btn_pause_music:
                mVoicePlayerManager.pausePlay();
                break;
            case R.id.btn_stop_music:
                mVoicePlayerManager.releasePlayer();
                break;
            case R.id.btn_reset_music:
                mVoicePlayerManager.stopPlay();
                break;
            case R.id.btn_next_music:
                mVoicePlayerManager.nextPlay();
                break;
            case R.id.btn_pre_music:
                mVoicePlayerManager.prePlay();
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
        initUI();
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
            initUI();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 200) {
            checkPermission();
        }
    }

    @Override
    public boolean onLongClick(View v) {
        new VoiceDialog(this).show();
        return false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        mVoiceRecorderManager.stopRecorder();
        mVoicePlayerManager.releasePlayer();
    }
}
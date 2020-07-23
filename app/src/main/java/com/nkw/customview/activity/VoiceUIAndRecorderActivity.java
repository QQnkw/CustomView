package com.nkw.customview.activity;

import android.content.Intent;
import android.os.Handler;

import com.nkw.customview.R;
import com.nkw.customview.view.VoiceUiView;

import java.util.Random;


public class VoiceUIAndRecorderActivity extends BaseActivity {

    public static void startActivity(BaseActivity activity) {
        activity.startActivity(new Intent(activity, VoiceUIAndRecorderActivity.class));
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_voice_ui_and_recorder;
    }

    @Override
    protected void initViewSet() {
        final VoiceUiView voiceUI = findViewById(R.id.voice_ui);
        final Random random = new Random();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int i = random.nextInt(300);
                voiceUI.setLineData(i);
                handler.postDelayed(this, 100);
            }
        }, 2000);
    }
}
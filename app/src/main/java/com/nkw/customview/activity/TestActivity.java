package com.nkw.customview.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;

import com.nkw.customview.R;

public class TestActivity extends AppCompatActivity {

    private RecordConstraintLayout mGroupView;
    private VoiceButton mBtn;

    public static void startActivity(BaseActivity activity) {
        activity.startActivity(new Intent(activity, TestActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        mGroupView = findViewById(R.id.group_view);
        mBtn = findViewById(R.id.btn);
        mBtn.setRelationView(mGroupView);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d("TestActivity", "ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d("TestActivity", "ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                Log.d("TestActivity", "ACTION_UP");
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.d("TestActivity", "ACTION_CANCEL");
                break;
        }
        return super.onTouchEvent(event);
    }
}
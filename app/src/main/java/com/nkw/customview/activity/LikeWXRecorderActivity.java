package com.nkw.customview.activity;

import android.content.Intent;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;

import com.nkw.customview.R;
import com.nkw.customview.view.recorder.RecordConstraintLayout;
import com.nkw.customview.view.recorder.VoiceButton;

import butterknife.BindView;

public class LikeWXRecorderActivity extends BaseActivity {

    @BindView(R.id.btn)
    VoiceButton mBtn;
    @BindView(R.id.touch_up_send_tips_view)
    TextView mTouchUpSendTipsView;
    @BindView(R.id.close_record_view)
    ImageView mCloseRecordView;
    @BindView(R.id.group_view)
    RecordConstraintLayout mGroupView;

    public static void startActivity(BaseActivity activity) {
        activity.startActivity(new Intent(activity, LikeWXRecorderActivity.class));
    }

    @Override
    protected void initViewSet() {
        mBtn.setRelationView(mGroupView);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_like_wx_recorder;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d("LikeWXRecorderActivity", "ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d("LikeWXRecorderActivity", "ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                Log.d("LikeWXRecorderActivity", "ACTION_UP");
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.d("LikeWXRecorderActivity", "ACTION_CANCEL");
                break;
        }
        return super.onTouchEvent(event);
    }
}
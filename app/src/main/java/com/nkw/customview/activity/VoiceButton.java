package com.nkw.customview.activity;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;

public class VoiceButton extends AppCompatTextView {
    private ViewGroup mViewGroup;

    public VoiceButton(Context context) {
        this(context, null);
    }

    public VoiceButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VoiceButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mViewGroup == null) {
            return false;
        }
        /*switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
//                Log.d("CustomButton", "ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
//                Log.d("CustomButton", "ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
//                Log.d("CustomButton", "ACTION_UP");
                break;
            case MotionEvent.ACTION_CANCEL:
//                Log.d("CustomButton", "ACTION_CANCEL");
                break;
        }*/
        mViewGroup.onTouchEvent(event);
        return true;
    }

    public void setRelationView(ViewGroup viewGroup) {
        mViewGroup = viewGroup;
    }
}

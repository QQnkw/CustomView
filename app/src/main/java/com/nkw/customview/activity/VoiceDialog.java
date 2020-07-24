package com.nkw.customview.activity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.nkw.customview.R;
import com.nkw.customview.utils.ScreenUtils;

public class VoiceDialog extends Dialog {

    public static final String TAG = "VoiceDialog";
    private View mView;

    public VoiceDialog(@NonNull Context context) {
        super(context, R.style.dialog);
        mView = LayoutInflater.from(context).inflate(R.layout.dialog_voice_recorder, null, false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(mView);//这行一定要写在前面
        setCancelable(true);//点击外部不可dismiss
        setCanceledOnTouchOutside(true);
    }

    @Override
    public void show() {
        super.show();
        // 设置宽度全屏，要设置在show的后面
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.width = ScreenUtils.getScreenWidth(); //设置宽度
        layoutParams.height = ScreenUtils.getScreenHeight(); //设置高度
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        getWindow().setAttributes(layoutParams);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        Log.d(TAG,"onTouchEvent");
        return super.onTouchEvent(event);
    }
}

package com.nkw.customview.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.WindowManager;

import com.nkw.customview.R;
import com.nkw.customview.utils.ScreenUtils;

public class BaseDialog extends AlertDialog {
    private boolean mBottomDialogAnimation;
    private boolean mWidthIsMatchParent;
    private boolean mHeightIsWrapContent;
    protected     int     mGravity;
    protected     boolean mIscancelable;//控制点击dialog外部是否dismiss
    protected     boolean mIsBackCancelable;//控制返回键是否dismiss
    protected     View    mView;

    public BaseDialog(Context context, View view, boolean isCancelable, boolean isBackCancelable, int gravity, boolean
            widthIsMatchParent, boolean heightIsWrapContent, boolean
                              bottomDialogAnimation) {
        super(context, R.style.dialog);
        mView = view;
        mIscancelable = isCancelable;
        mIsBackCancelable = isBackCancelable;
        mGravity = gravity;
        mWidthIsMatchParent = widthIsMatchParent;
        mHeightIsWrapContent = heightIsWrapContent;
        mBottomDialogAnimation = bottomDialogAnimation;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(mView);//这行一定要写在前面
        setCancelable(mIscancelable);//点击外部不可dismiss
        setCanceledOnTouchOutside(mIsBackCancelable);
    }

    @Override
    public void show() {
        super.show();
        // 设置宽度全屏，要设置在show的后面
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity = mGravity;
        if (mWidthIsMatchParent) {
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        } else {
            layoutParams.width = (int) (ScreenUtils.getScreenWidth() * 0.8); //设置宽度
        }
        if (mHeightIsWrapContent) {
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        } else {
            layoutParams.height = (int) (ScreenUtils.getScreenHeight() * 0.3); //设置高度
        }
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        getWindow().setAttributes(layoutParams);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (mBottomDialogAnimation) {
            getWindow().setWindowAnimations(R.style.dialog_animation_bottom);
        }
    }
}


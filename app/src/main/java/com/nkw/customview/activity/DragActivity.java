package com.nkw.customview.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.nkw.customview.R;

import butterknife.BindView;

public class DragActivity extends BaseActivity {

    @BindView(R.id.iv)
    private ImageView mImageView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_drag;
    }

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, DragActivity.class));
    }

    @Override
    protected void initViewSet() {
        findViewById(R.id.btn_show_hide).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mImageView.getVisibility() == View.GONE) {
                    mImageView.setVisibility(View.VISIBLE);
                } else {
                    mImageView.setVisibility(View.GONE);
                }
            }
        });
    }
}

package com.nkw.customview.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.nkw.customview.R;
import com.nkw.customview.view.DragFrameLayout;

import butterknife.BindView;

public class DragActivity extends BaseActivity {

    @BindView(R.id.dragLayout)
    DragFrameLayout mDragLayout;
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
        super.initViewSet();
        mDragLayout.setDragFrameClickListener(new DragFrameLayout.DragFrameClickListener() {
            @Override
            public void onDragFrameClick() {
                Toast.makeText(mActivity, "點擊了", Toast.LENGTH_SHORT).show();
            }
        });
        mDragLayout.setDragMarginParent(30, 50, 100, 200);
        mImageView = findViewById(R.id.iv);
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

package com.nkw.customview.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.nkw.customview.R;
import com.nkw.customview.view.BarrageGroupView;

import java.util.ArrayList;

public class BarrageGroupViewActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final BarrageGroupView bgv = findViewById(R.id.bgv);
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add("第" + i + "个弹幕");
        }
        bgv.setDataList(list);
        bgv.startBarrage();
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.bg_meet_success);
//        findViewById(R.id.ll).setBackground(new BitmapDrawable(getResources(),bitmap));
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_barrage_group_view;
    }

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, BarrageGroupViewActivity.class));
    }
}

package com.nkw.customview.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ClipDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import com.nkw.customview.R;
import com.nkw.customview.view.BarrageGroupView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class BarrageGroupViewActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BarrageGroupView bgv = findViewById(R.id.bgv);
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add("第" + i + "个弹幕");
        }
        bgv.setDataList(list);
        bgv.startBarrage();
        ImageView imageview = (ImageView) findViewById(R.id.iv);
        //获取图片所显示的ClipDrawble对象
        final ClipDrawable drawable = (ClipDrawable) imageview.getDrawable();
        final Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == 0x1233) {
                    //修改ClipDrawable的level值
                    drawable.setLevel(drawable.getLevel() + 200);
                }
            }
        };
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                Message msg = new Message();
                msg.what = 0x1233;
                //发送消息,通知应用修改ClipDrawable对象的level值
                handler.sendMessage(msg);
                //取消定时器
                if (drawable.getLevel() >= 10000) {
                    timer.cancel();
                }
            }
        }, 0, 50);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_barrage_group_view;
    }

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, BarrageGroupViewActivity.class));
    }
}

package com.nkw.customview.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.nkw.customview.R;
import com.nkw.customview.view.MatchingTimeDrawable;
import com.nkw.customview.view.RefreshLikeIOSView;
import com.nkw.customview.view.TabLayoutVY;
import com.nkw.customview.view.VyLoading;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class MainActivity extends BaseActivity {
    private Handler mHandler = new Handler();
    private int     mNum     = 0;
    private MatchingTimeDrawable mMatchingTimeDrawable;

    @Override
    protected void initViewSet() {
        super.initViewSet();
        tablayout();
        likeIOSRefreshView();
        fourGridView();
        VYLoading();
        goWebView();
        goDragLayout();
        goBarrageGroup();
        goBtnRadar();
        goVyWoQu();
        goLayoutManager();
        goCustomCamera();
        customDrawable();
        //读取APK中的写入的信息,配合TestMain中的方法一起使用
        String unfinishedURL = getUnfinishedURL(this);
        Toast.makeText(this,unfinishedURL,Toast.LENGTH_LONG).show();
    }

    private void customDrawable() {
        mMatchingTimeDrawable = new MatchingTimeDrawable(this);
        findViewById(R.id.iv_custom_drawable).setBackground(mMatchingTimeDrawable);
    }

    private void goCustomCamera() {
        findViewById(R.id.btn_custom_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mActivity,CameraActivity.class));
            }
        });
    }

    private void goLayoutManager() {
        findViewById(R.id.btn_layoutManager).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mActivity,CustomLayoutManagerActivity.class));
            }
        });
    }

    private void goVyWoQu() {
        findViewById(R.id.btn_dian_lu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mActivity,VyWoQuActivity.class));
            }
        });
    }

    private void goBtnRadar() {
        findViewById(R.id.btn_radar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BadarActivity.startActivity(mActivity);
            }
        });
    }

    private void goBarrageGroup() {
        findViewById(R.id.btn_barrage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BarrageGroupViewActivity.startActivity(mActivity);
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    private void goDragLayout() {
        findViewById(R.id.btn_dragLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DragActivity.startActivity(mActivity);
            }
        });
    }

    private void goWebView() {
        findViewById(R.id.btn_webView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebViewActivity.startActivity(mActivity);
            }
        });
    }

    private void VYLoading() {
        final VyLoading vyLoading = findViewById(R.id.vy_loading);
        vyLoading.postDelayed(new Runnable() {
            @Override
            public void run() {
                vyLoading.startLoading();
            }
        },1000);
    }

    private void fourGridView() {
        findViewById(R.id.btn_four_grid_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FourGridViewActivity.startActivity(mActivity);
            }
        });
    }

    private void likeIOSRefreshView() {
        final RefreshLikeIOSView refreshLikeIOSView = findViewById(R.id.RefreshLikeIOSView);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mNum < 101) {
                    refreshLikeIOSView.setProgress(mNum);
                    mNum++;
                    mHandler.postDelayed(this,50);
                }
            }
        }, 100);
    }

    private void tablayout() {
        TabLayoutVY tabLayoutVY = findViewById(R.id.tablayoutVy);
        tabLayoutVY.addSelectorListener(new TabLayoutVY.TabClickedListener() {
            @Override
            public void onTabClicked(int clickedResID) {
                Log.d("NKW", clickedResID + "");
            }
        });
    }
    public static String getUnfinishedURL(Context context) {
        //获取缓存的 APK 文件
        File file = new File(context.getPackageCodePath());
        byte[] bytes;
        RandomAccessFile accessFile = null;
        // 从指定的位置找到 WriteAPK.java 写入的信息
        try {
            accessFile = new RandomAccessFile(file, "r");
            long index = accessFile.length();
            bytes = new byte[2];
            index = index - bytes.length;
            accessFile.seek(index);
            accessFile.readFully(bytes);
            int contentLength = stream2Short(bytes, 0);
            bytes = new byte[contentLength];
            index = index - bytes.length;
            accessFile.seek(index);
            accessFile.readFully(bytes);
            return new String(bytes, "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (accessFile != null) {
                try {
                    accessFile.close();
                } catch (IOException ignored) {
                    ignored.printStackTrace();
                }
            }
        }
        return null;
    }
    public static short stream2Short(byte[] stream, int offset) {
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put(stream[offset]);
        buffer.put(stream[offset + 1]);
        return buffer.getShort(0);
    }
}

package com.nkw.customview.video;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.nkw.customview.R;
import com.nkw.customview.video.utils.X5WebView;
import com.tencent.smtt.sdk.TbsVideo;


public class X5WebViewActivity extends AppCompatActivity {

    /**
     * 用于演示X5webview实现视频的全屏播放功能 其中注意 X5的默认全屏方式 与 android 系统的全屏方式
     */

    X5WebView webView;
    private X5WebViewActivity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_x5_web_view);
        mActivity = this;
        initWebView();
        initBtn();
    }

    private void initBtn() {
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TbsVideo.canUseTbsPlayer(mActivity)) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("screenMode", 102);
                    TbsVideo.openVideo(mActivity,"http://jzvd.nathen.cn/342a5f7ef6124a4a8faf00e738b8bee4/cf6d9db0bd4d41f59d09ea0a81e918fd-5287d2089db37e62345123a1be272f8b.mp4"
                    ,bundle);
                } else {
                    Toast.makeText(mActivity,"TBS播放器不可用",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initWebView() {
        webView = (X5WebView) findViewById(R.id.web_filechooser);
        //		webView.loadUrl("https://tv.sohu.com/upload/static/share/share_play.html#106903751_9114930_0_9001_0");
        //		webView.loadUrl("https://v.qq.com/txp/iframe/player.html?vid=i0737bacynk");
        webView.loadUrl("http://player.youku.com/embed/XMzg1MzkyMzk0MA==");
        //爱奇艺视频播放按钮会重叠
//        webView.loadUrl("http://open.iqiyi.com/developer/player_js/coopPlayerIndex.html?vid=3675f352a8c555371731c1b09ce8d298&tvId=24357822709&accessToken=2.f22860a2479ad60d8da7697274de9346&appKey=3955c3425820435e86d0f4cdfe56f5e7&appId=1368&height=100%&width=100%");

        getWindow().setFormat(PixelFormat.TRANSLUCENT);

        webView.getView().setOverScrollMode(View.OVER_SCROLL_ALWAYS);
    }

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, X5WebViewActivity.class));
    }
}

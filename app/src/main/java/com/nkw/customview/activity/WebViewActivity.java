package com.nkw.customview.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

import com.nkw.customview.R;
import com.nkw.customview.adapter.RvAdapter;
import com.nkw.customview.bean.InfoBean;
import com.nkw.customview.view.nestedscroll.NestedScrollingDetailContainer;
import com.nkw.customview.view.nestedscroll.NestedScrollingWebView;

import java.util.ArrayList;
import java.util.List;


public class WebViewActivity extends AppCompatActivity {

    /**
     * 用于演示X5webview实现视频的全屏播放功能 其中注意 X5的默认全屏方式 与 android 系统的全屏方式
     */

    NestedScrollingWebView webContainer;
    private WebViewActivity mActivity;
    private RecyclerView rvList;
    private NestedScrollingDetailContainer mNestedContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        mActivity = this;
        mNestedContainer = findViewById(R.id.nested_container);
        initWebView();
        initRecyclerView();
        initToolBarView();
    }

    private void initToolBarView() {
        findViewById(R.id.v_tool_bar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNestedContainer.scrollToTarget(rvList);
            }
        });
    }

    private void initWebView() {
        webContainer = findViewById(R.id.web_container);
        webContainer.loadUrl("https://github.com/wangzhengyi/Android-NestedDetail");
        if (false) {
            // 测试JS通知内容高度回调
            webContainer.post(new Runnable() {
                @Override
                public void run() {
                    int contentHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, getResources().getDisplayMetrics());
                    webContainer.setJsCallWebViewContentHeight(contentHeight);
                }
            });
        }
    }


    private void initRecyclerView() {
        rvList = findViewById(R.id.rv_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvList.setLayoutManager(layoutManager);
        List<InfoBean> data = getCommentData();
        RvAdapter rvAdapter = new RvAdapter(this, data);
        rvList.setAdapter(rvAdapter);
    }

    private List<InfoBean> getCommentData() {
        List<InfoBean> commentList = new ArrayList<>();
        InfoBean titleBean = new InfoBean();
        titleBean.type = InfoBean.TYPE_TITLE;
        titleBean.title = "评论列表";
        commentList.add(titleBean);
        for (int i = 0; i < 40; i++) {
            InfoBean contentBean = new InfoBean();
            contentBean.type = InfoBean.TYPE_ITEM;
            contentBean.title = "评论标题" + i;
            contentBean.content = "评论内容" + i;
            commentList.add(contentBean);
        }
        return commentList;
    }

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, WebViewActivity.class));
    }
}

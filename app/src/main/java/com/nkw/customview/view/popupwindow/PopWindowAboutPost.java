package com.nkw.customview.view.popupwindow;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nkw.customview.R;
import com.nkw.customview.utils.ScreenUtils;
import com.nkw.customview.utils.SizeUtils;
import com.nkw.customview.view.popupwindow.utils.FitPopupWindow;

public class PopWindowAboutPost implements View.OnClickListener {

    private TextView mTvMide;
    private View     contentView;

    private Activity context;

    private TextView mTvTop;
    private TextView mTvDown;

    private FitPopupWindow mPopupWindow;

    private OnTextViewClickListener listener;
    private View                    mLineView;

    public PopWindowAboutPost(Activity context) {

        this.context = context;

        LayoutInflater inflater = LayoutInflater.from(context);
        contentView = inflater.inflate(R.layout.popwindow_about_post, null);
        mTvTop = (TextView) contentView.findViewById(R.id.tv_top);
        mTvMide = (TextView) contentView.findViewById(R.id.tv_mide);
        mTvDown = (TextView) contentView.findViewById(R.id.tv_down);
        mLineView = contentView.findViewById(R.id.line_down);

        mTvTop.setOnClickListener(this);
        mTvMide.setOnClickListener(this);
        mTvDown.setOnClickListener(this);
    }

    public void setOnClickListener(OnTextViewClickListener listener) {
        this.listener = listener;
    }

    /**
     * 弹出自适应位置的popupwindow
     *
     * @param anchorView 目标view
     */
    public View showPopup(View anchorView) {
        if (mPopupWindow == null) {
            mPopupWindow = new FitPopupWindow(context,
                    ScreenUtils.getScreenWidth() - SizeUtils.dp2px(20),
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            /*mPopupWindow = new FitPopupWindow(context,
                    ScreenUtils.getScreenWidth(),
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );*/
        }

        mPopupWindow.setView(contentView, anchorView);
        mPopupWindow.show();
        return contentView;
    }


    @Override
    public void onClick(View v) {
        if (listener != null) {
            switch (v.getId()) {
                case R.id.tv_top:
                    listener.onTopClick(v);
                    break;
                case R.id.tv_down:
                    listener.onDownClick(v);
                    break;
                case R.id.tv_mide:
                    listener.onMideClick(v);
                    break;
            }
        }
        mPopupWindow.dismiss();
    }

    public interface OnTextViewClickListener {
        void onTopClick(View view);

        void onDownClick(View view);

        void onMideClick(View view);
    }

    public void updataTextViewData(String topText, String mideText, String downText) {
        mTvTop.setText(topText);
        if (!TextUtils.isEmpty(mideText)) {
            mTvMide.setVisibility(View.VISIBLE);
            mLineView.setVisibility(View.VISIBLE);
            mTvMide.setText(mideText);
        }
        mTvDown.setText(downText);
    }
}
package com.nkw.customview.view.popupwindow.utils;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.nkw.customview.R;
import com.nkw.customview.utils.ResourceUtils;
import com.nkw.customview.utils.ScreenUtils;

/**
 * 现在背景变暗是添加了一个全屏的背景View,原来的背景变暗是给window是设置透明度，原始设置会在宿主Activity设置透明主题时产生BUG；
 */

public class FitPopupWindow extends PopupWindow implements PopupWindow.OnDismissListener {

    private View     anchorView;
    private Activity context;

    private int mWindowWidth;

    private static final int PADDING = 0;
    //x轴坐标
    private int mXCoordinate;

    private int                        mHorizontal;
    private int                        mVertical;
    private int[]                      windowPos;
    private FitPopupWindowLayout       mFitPopupWindowLayout;
    private boolean                    mIsNeedShowUp;
    private boolean                    mIsNeedShowLeft;
    private View                       mBgView;
    private WindowManager              mWindowManager;
    private WindowManager.LayoutParams mWindowMannagerParams;

    public FitPopupWindow(Activity context) {
        init(context, ViewGroup.LayoutParams.WRAP_CONTENT
                , ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public FitPopupWindow(Activity context, int width, int height) {
        mWindowWidth = width;
        init(context, width, height);

        //背景的view
        mBgView = new View(context);
        mBgView.setBackgroundColor(ResourceUtils.getColor(R.color.app_30transparent));
        mWindowMannagerParams = new WindowManager.LayoutParams();
        mWindowMannagerParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        mWindowMannagerParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        mWindowMannagerParams.format = PixelFormat.TRANSLUCENT;
        mWindowMannagerParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_PANEL;
        //        p.token = getWindowToken();
        //            p.windowAnimations = android.R.style.Animation_Toast;
        mWindowManager = context.getWindow().getWindowManager();
    }


    private void init(Activity context, int width, int height) {
        this.context = context;
        //popupwindow会默认忽略最外层的大小,所以应该再嵌套一层
        setWidth(width);
        setHeight(height);
        setBackgroundDrawable(new ColorDrawable(0x00000000));
        setOutsideTouchable(true);
        setFocusable(true);
        setOnDismissListener(this);

        //        setAnimationStyle(R.style.popp_anim);
    }

    private RelativeLayout.LayoutParams layoutParams;
    public void setView(View contentView, View anchorView) {
        this.anchorView = anchorView;
        FitPopupWindowLayout.SHARP_WIDTH = anchorView.getWidth();
        windowPos = calculatePopWindowPos(anchorView, contentView);
/*--------nkw------*/
        if (mFitPopupWindowLayout == null) {
            mFitPopupWindowLayout = new FitPopupWindowLayout(context);
        }
        if (layoutParams==null) {
            layoutParams = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, getHeight() - FitPopupWindowLayout.SHARP_HEIGHT);
            layoutParams.bottomMargin = FitPopupWindowLayout.SHARP_HEIGHT;
        contentView.setLayoutParams(layoutParams);
        }
        mFitPopupWindowLayout.setOrientation(getHorizontal(), getVertical()
                , getXCoordinate());
        mFitPopupWindowLayout.addView(contentView);

        setContentView(mFitPopupWindowLayout);
        /*--------nkw------*/
       /* mFitPopupWindowLayout = new FitPopupWindowLayout(context);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, getHeight() - FitPopupWindowLayout.SHARP_HEIGHT);
        layoutParams.bottomMargin = FitPopupWindowLayout.SHARP_HEIGHT;

        contentView.setLayoutParams(layoutParams);
        mFitPopupWindowLayout.setOrientation(getHorizontal(), getVertical()
                , getXCoordinate());
        mFitPopupWindowLayout.addView(contentView);

        setContentView(mFitPopupWindowLayout);*/

    }


    /*public void show() {
        showAtLocation(anchorView, Gravity.TOP | Gravity.END
                , windowPos[0], windowPos[1]);
        update();
        Window window = context.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = 0.7f;
        window.setAttributes(lp);
        startAnimation(true);

    }*/

    public void show() {
        mWindowManager.addView(mBgView, mWindowMannagerParams);
        showAtLocation(anchorView, Gravity.TOP | Gravity.END
                , windowPos[0], windowPos[1]);
        update();
        startAnimation();
    }

    /**
     * 移除添加的view
     */
    private void removeBgView() {
        if (mBgView != null) {
            mWindowManager.removeViewImmediate(mBgView);
        }
    }

    /**
     * @param anchorView  弹出window的view
     * @param contentView window的内容布局
     * @return window显示的左上角的xOff, yOff坐标
     */
    protected int[] calculatePopWindowPos(final View anchorView, final View contentView) {
        final int windowPos[] = new int[2];
        final int anchorLoc[] = new int[2];
        // 获取锚点View在屏幕上的左上角坐标位置
        anchorView.getLocationOnScreen(anchorLoc);
        final int anchorHeight = anchorView.getHeight();
        final int anchorWidth = anchorView.getWidth();
        mXCoordinate = anchorLoc[0];
        // 获取屏幕的高宽

        final int screenHeight = ScreenUtils.getScreenHeight();
        final int screenWidth = ScreenUtils.getScreenWidth();
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        // 计算contentView的高宽
        int windowHeight = contentView.getMeasuredHeight();
//        LogUtils.d("contentView-->MeasuredHeight" + windowHeight);
        mWindowWidth = mWindowWidth > 0 ? mWindowWidth : contentView.getMeasuredWidth();

        // 判断需要向上弹出还是向下弹出,如果要改变弹出策略,改变此处即可
        // 目前是根据屏幕的一半进行判断
        mIsNeedShowUp = (screenHeight - anchorLoc[1] - anchorHeight < screenHeight / 2);

        // 判断需要向左弹出还是向右弹出
        mIsNeedShowLeft = (anchorLoc[0] < mWindowWidth / 2);

        setHorizontal(mIsNeedShowLeft ? FitPopupWindowLayout.LEFT : FitPopupWindowLayout.RIGHT);
        setVertical(mIsNeedShowUp ? FitPopupWindowLayout.UP : FitPopupWindowLayout.DOWN);

        //        windowPos[0] = isNeedShowLeft ?
        //                anchorLoc[0] - windowWidth : anchorLoc[0] + anchorWidth;
        //保存的pop距屏幕的间隙值
        windowPos[0] = (screenWidth - mWindowWidth) / 2;
        //保存的pop弹出的高度位置
        windowPos[1] = mIsNeedShowUp ?
                anchorLoc[1] - windowHeight - PADDING - FitPopupWindowLayout.SHARP_HEIGHT / 2
                : anchorLoc[1] + anchorHeight + PADDING - FitPopupWindowLayout.SHARP_HEIGHT / 2;

        return windowPos;
    }

    @Override
    public void dismiss() {
        mFitPopupWindowLayout.removeAllViews();
        removeBgView();
        super.dismiss();
    }

    @Override
    public void onDismiss() {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = 1f;
        context.getWindow().setAttributes(lp);

    }

    public int getXCoordinate() {
        if (mXCoordinate > mWindowWidth / 2) {
            //            mXCoordinate = mWindowWidth - mXCoordinate - anchorView.getWidth() + 50;
            int space = (ScreenUtils.getScreenWidth() - mWindowWidth) / 2;
            mXCoordinate = mWindowWidth - mXCoordinate - anchorView.getWidth() + space;
        }
        return mXCoordinate;
    }

    private int getHorizontal() {
        return mHorizontal;
    }

    /**
     * @param mHorizontal 设置水平方向
     */
    private void setHorizontal(int mHorizontal) {
        this.mHorizontal = mHorizontal;
    }

    private int getVertical() {
        return mVertical;
    }

    /**
     * @param mVertical 设置竖直方向
     */
    private void setVertical(int mVertical) {
        this.mVertical = mVertical;
    }


    private void startAnimation() {
        AnimationSet animationSet = new AnimationSet(true);

        ScaleAnimation sa;
        /*计算从点击处弹出动画*/
        float sharpWidth = FitPopupWindowLayout.SHARP_WIDTH;
        //计算点击点在布局的比例位置
        float i = (mXCoordinate + sharpWidth * 1.0f / 2) / mWindowWidth;
        float pivotXValue = 0.5f;
        float pivotYValue = 0.5f;
        if (mIsNeedShowUp) {
            pivotXValue = 1f - i;
            pivotYValue = 1f;
        } else {
            pivotXValue = 1f - i;
            pivotYValue = 0f;
        }
        sa = new ScaleAnimation(0, 1f, 0, 1f,
                Animation.RELATIVE_TO_SELF, pivotXValue, Animation.RELATIVE_TO_SELF, pivotYValue);
        sa.setDuration(200);
        //        sa.setInterpolator(new BounceInterpolator());//单独设置无效
        sa.setFillAfter(true);

        AlphaAnimation aa = new AlphaAnimation(0f, 1f);
        aa.setDuration(200);
        aa.setFillAfter(true);

        animationSet.addAnimation(sa);
        animationSet.addAnimation(aa);
        //        animationSet.setInterpolator(new OvershootInterpolator());
        animationSet.setInterpolator(new AccelerateInterpolator());
        mFitPopupWindowLayout.startAnimation(animationSet);

    }
}

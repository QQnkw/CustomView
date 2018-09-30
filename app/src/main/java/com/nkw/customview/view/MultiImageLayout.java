package com.nkw.customview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.nkw.customview.R;

import java.util.List;

/**
 * Created by fen.zeng on 2018/8/13.根据不同的displayCnt值返回不同的布局样式
 */
public class MultiImageLayout extends FrameLayout {
    private ImageView[] ivs;
    private Rect[]      rects;
    private Adapter     adapter;
    private int         dividerSize;
    private int         displayCnt;
    private int         displayMode;
    public static final int MODE_GRID   = 1;
    public static final int MODE_NORMAL = 0;
    private float          whRatio;
    private SelectListener lsn;
    private List<String>   urls;
    private View           vTag;

    public MultiImageLayout(Context context) {
        this(context, null);
    }

    public MultiImageLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiImageLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setPadding(0, 0, 0, 0);//无视padding
        this.dividerSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MultiImageLayout);
        this.displayMode = a.getInt(R.styleable.MultiImageLayout_layout_mode, 0);
        this.whRatio = a.getFloat(R.styleable.MultiImageLayout_wh_ratio, 1);
        a.recycle();
    }


    /*
     * in pixel
     * */
    public void setDividerSize(int dividerSize) {
        if (this.dividerSize != dividerSize) {
            this.dividerSize = dividerSize;
            requestLayout();
        }
    }

    public void setAdapter(Adapter adapter) {
        this.adapter = adapter;
        rects = new Rect[]{
                new Rect(), new Rect(), new Rect(),
                new Rect(), new Rect(), new Rect(),
                new Rect(), new Rect(), new Rect()};
    }

    public void setDisplayCount(int count) {
        if (this.ivs == null && count >= 0 && count <= 9) {
            this.displayCnt = count;
            this.ivs = new ImageView[count];
            if (displayMode == MODE_NORMAL && displayCnt == 1) {
                ivs[0] = new ImageView(getContext());
                LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                addView(ivs[0], lp);
                vTag = LayoutInflater.from(getContext()).inflate(R.layout.layout_long_img, this, false);
                addView(vTag);
                vTag.setVisibility(GONE);
            } else {
                for (int i = 0; i < count; i++) {
                    ivs[i] = new ImageView(getContext());
                    addView(ivs[i]);
                }
            }
        }
    }

    public void setOnSelectListener(final SelectListener lsn) {
        this.lsn = lsn;
        int i = 0;
        for (ImageView iv : ivs) {
            final int finalI = i;
            iv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (lsn != null)
                        lsn.onImageSelected(finalI, urls);
                }
            });
            i++;
        }
    }

    public void setImages(@NonNull List<String> urls) {
        this.urls = urls;
        final int size = Math.max(0, Math.min(displayCnt, urls.size()));
        for (int i = 0; i < size; i++) {
            if (adapter != null)
                adapter.loadImage(i, displayCnt, urls.get(i), ivs[i]);
        }
    }

    public void showLongImgTag(int displayWidth, int displayHeight) {
        if (vTag != null) {
            vTag.setVisibility(VISIBLE);
            vTag.setTranslationX(displayWidth - (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 26, getResources().getDisplayMetrics()));
            vTag.setTranslationY(displayHeight - (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 18, getResources().getDisplayMetrics()));
        }
    }

    public void hideLongImgTag() {
        if (vTag != null)
            vTag.setVisibility(GONE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getDefaultSize(defaultWidth(), widthMeasureSpec);
        int height = 0;
        if (displayMode == MODE_GRID) {
            if (displayCnt > 0) {
                int pos = 0;
                for (int i = 0; i < 3; i++) {
                    if (i * 3 >= displayCnt)
                        break;
                    height = averageRect(height, width, whRatio, rects[pos++], rects[pos++], rects[pos++]);
                    height += dividerSize;
                }
                height -= dividerSize;
            }
        } else {
            switch (displayCnt) {
                case 0:
                    break;
                case 1:
                    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                    return;
                case 2:
                    height = averageRect(height, width, 1, rects[0], rects[1]);
                    break;
                case 3:
                    height = averageRect(height, width, 1, rects[0], rects[1], rects[2]);
                    break;
                case 4:
                    height = averageRect(height, width, 1, rects[0], rects[1]);
                    height += dividerSize;
                    height = averageRect(height, width, 1, rects[2], rects[3]);
                    break;
                case 5:
                    height = averageRect(height, width, 1, rects[0], rects[1]);
                    height += dividerSize;
                    height = averageRect(height, width, 1, rects[2], rects[3], rects[4]);
                    break;
                case 6: {
                    int pos = 0;
                    for (int i = 0; i < 2; i++) {
                        if (i * 3 >= displayCnt)
                            break;
                        height = averageRect(height, width, 1, rects[pos++], rects[pos++], rects[pos++]);
                        height += dividerSize;
                    }
                    height -= dividerSize;
                    break;
                }
                case 7:
                    height = averageRect(height, width, 2.02f, rects[0]);
                    height += dividerSize;
                    height = averageRect(height, width, 1, rects[1], rects[2], rects[3]);
                    height += dividerSize;
                    height = averageRect(height, width, 1, rects[4], rects[5], rects[6]);
                    break;
                case 8:
                    height = averageRect(height, width, 1, rects[0], rects[1]);
                    height += dividerSize;
                    height = averageRect(height, width, 1, rects[2], rects[3], rects[4]);
                    height += dividerSize;
                    height = averageRect(height, width, 1, rects[5], rects[6], rects[7]);
                    break;
                case 9:
                    int pos = 0;
                    for (int i = 0; i < 3; i++) {
                        height = averageRect(height, width, 1, rects[pos++], rects[pos++], rects[pos++]);
                        height += dividerSize;
                    }
                    height -= dividerSize;
                    break;
            }
        }
        for (int i = 0; i < displayCnt; i++) {
            measureChild(ivs[i], MeasureSpec.makeMeasureSpec(rects[i].width(), MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(rects[i].height(), MeasureSpec.EXACTLY));
        }
        setMeasuredDimension(width, height);
    }

    /**
     * 均分给到宽度得到单位宽度,通过宽高比得到单位高度,通过顶部坐标点确定布局位置
     *
     * @return 底部y坐标
     */
    private int averageRect(int top, int widthGiven, float whRatio, Rect... rects) {
        final int cnt = rects.length;
        int w = (widthGiven - dividerSize * (cnt - 1)) / cnt;
        int h = (int) (w / whRatio);
        int left = 0;
        for (Rect rect : rects) {
            rect.left = left;
            rect.right = left + w;
            rect.top = top;
            rect.bottom = h + top;
            left = rect.right + dividerSize;
        }
        return h + top;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (displayMode == MODE_NORMAL && displayCnt == 1) {
            super.onLayout(changed, l, t, r, b);
        } else {
            for (int i = 0; i < displayCnt; i++) {
                ivs[i].layout(rects[i].left, rects[i].top, rects[i].right, rects[i].bottom);
            }
        }
    }

    private int defaultWidth() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return (int) (outMetrics.widthPixels * 0.9);
    }

    public interface Adapter {
        void loadImage(int position, int total, String path, ImageView iv);
    }

    public interface SelectListener {
        void onImageSelected(int position, List<String> urls);
    }
}

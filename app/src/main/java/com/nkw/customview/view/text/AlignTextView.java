package com.nkw.customview.view.text;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

import com.nkw.customview.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 两端对齐的text view，可以设置最后一行靠左，靠右，居中对齐,最后一行可能有空白
 *
 * @author YD
 */
public class AlignTextView extends TextView {
    private List<String> lines = new ArrayList<String>(); // 分割后的行
    private List<Integer> tailLines = new ArrayList<Integer>(); // 尾行
    private Align align = Align.ALIGN_LEFT; // 默认最后一行左对齐

    private float lineSpacingMultiplier = 1.0f;
    private float lineSpacingAdd = 0.0f;

    String oldText;

    // 尾行对齐方式
    public enum Align {
        ALIGN_LEFT, ALIGN_CENTER, ALIGN_RIGHT  // 居中，居左，居右,针对段落最后一行
    }

    public AlignTextView(Context context) {
        super(context);
        setTextIsSelectable(false);
    }

    public AlignTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTextIsSelectable(false);

        lineSpacingMultiplier = attrs.getAttributeFloatValue("http://schemas.android" + "" +
                ".com/apk/res/android", "lineSpacingMultiplier", 1.0f);

        int[] attributes = new int[]{android.R.attr.lineSpacingExtra};

        TypedArray arr = context.obtainStyledAttributes(attrs, attributes);

        lineSpacingAdd = arr.getDimensionPixelSize(0, 0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);   // 计算宽度
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (heightMode == MeasureSpec.EXACTLY) {
            // 确定高度，直接返回
            setMeasuredDimension(getMeasuredWidth(), heightSize);
        } else {
            recalc();

            int height = lines.size() * getLineHeight();

            if (heightMode ==  MeasureSpec.AT_MOST) {
                // 最小高度
                setMeasuredDimension(getMeasuredWidth(), Math.min(heightSize, height));
            } else {
                setMeasuredDimension(getMeasuredWidth(), height);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        TextPaint paint = getPaint();
        paint.setColor(getCurrentTextColor());
        paint.drawableState = getDrawableState();

        Paint.FontMetrics fm = paint.getFontMetrics();
        float firstHeight = getTextSize() - (fm.bottom - fm.descent + fm.ascent - fm.top);

        int gravity = getGravity();
        if ((gravity & 0x1000) == 0) { // 是否垂直居中
            firstHeight = firstHeight + (getTextSize() - firstHeight) / 2;
        }

        int paddingTop = getPaddingTop();
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int width = getMeasuredWidth() - paddingLeft - paddingRight;

        for (int i = 0; i < lines.size(); i++) {
            float drawY = i * getLineHeight() + firstHeight;
            String line = lines.get(i);
            // 绘画起始x坐标
            float drawSpacingX = paddingLeft;
            float gap = (width - paint.measureText(line));
            float interval = gap / (line.length() - 1);

            // 绘制最后一行
            if (tailLines.contains(i)) {
                interval = 0;
                if (align == Align.ALIGN_CENTER) {
                    drawSpacingX += gap / 2;
                } else if (align == Align.ALIGN_RIGHT) {
                    drawSpacingX += gap;
                }
            }

            for (int j = 0; j < line.length(); j++) {
                float drawX = paint.measureText(line.substring(0, j)) + interval * j;
                canvas.drawText(line.substring(j, j + 1), drawX + drawSpacingX, drawY + paddingTop, paint);
            }
        }
    }

    /**
     * 设置尾行对齐方式
     *
     * @param align 对齐方式
     */
    public void setAlign(Align align) {
        this.align = align;
        invalidate();
    }

    private void recalc() {
        String text = getText().toString();
        if (text.equals(oldText))
            return;
        oldText = text;

        TextPaint paint = getPaint();
        lines.clear();
        tailLines.clear();

        // 文本含有换行符时，分割单独处理
        String[] items = text.split("\\n");
        for (String item : items) {
            calc(paint, item);
        }
    }

    /**
     * 计算每行应显示的文本数
     *
     * @param text 要计算的文本
     */
    private void calc(Paint paint, String text) {
        if (text.length() == 0) {
            lines.add("\n");
            return;
        }
        int width = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        int startPosition = 0; // 起始位置
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < text.length(); ++i) {
            if(paint.measureText(text.substring(startPosition, i + 1)) > (float) width) {
                startPosition = i;
                this.lines.add(sb.toString());
                sb = new StringBuilder();
                i--;
            } else {
                sb.append(text.charAt(i));
            }
        }

        if(sb.length() > 0) {
            lines.add(sb.toString());
        }

        tailLines.add(Integer.valueOf(this.lines.size() - 1));
    }
    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
        //防止recyclerView列表条目内容复用问题
        requestLayout();
    }
}

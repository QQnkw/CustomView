package com.nkw.customview.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.AppCompatTextView;
import android.text.DynamicLayout;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;

import com.nkw.customview.R;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class VyTextView extends AppCompatTextView {
    private static final int       DEFAULT_SHOW_LINES  = 3;
    private static final String    DEFAULT_SUFFIX_TEXT = "全文";
    private              int       mMaxShowLines;
    private              String    mSuffixText;
    private              int       mSuffixColor;
    private              TextPaint mTextPaint;
    private              int       mWidth;
    private              int       mRetryTime;
    public static final  Pattern   WEN_ZI              = Pattern.compile("\\[[a-zA-Z0-9\\u4e00-\\u9fa5]+\\]");
    public static final  Pattern   CUSTOM              = Pattern.compile("\\[M\\]");
    ;
    private int           mTextHeight;
    private StringBuilder mContentSb;
    private DynamicLayout mDynamicLayout;
    private boolean       mClickSpanEnable;
    private String        mContent;
    public static final  String DEFAULT_CONTENT = "                                                                                                                                                                                                                                                                                                                           ";
    private ClickableSpan mClickableSpan = new ClickableSpan() {
        @Override
        public void onClick(View widget) {
            if (mClickTypeTo == EXPAND) {
                /*文本中的表情格式化
                if (mContent != null) {
                    Spannable spannableEmoticonFilter = VyEmoticonUtils.getSpannableEmoticonFilter(VyTextView.this, mContent);
                    setText(spannableEmoticonFilter);
                }*/
            } else {
                if (mOnClickSpanListener!=null) {
                    mOnClickSpanListener.clickSpan();
                }
            }
        }

        @Override
        public void updateDrawState(TextPaint ds) {
        }
    };

    private static final int EXPAND = 0;//点击span去展开
    private              int mClickTypeTo;
    private OnClickSpanListener mOnClickSpanListener;

    public VyTextView(Context context) {
        this(context, null);
    }

    public VyTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.VyTextView);
        mMaxShowLines = typedArray.getInt(R.styleable.VyTextView_maxShowLines, DEFAULT_SHOW_LINES);
        mSuffixText = typedArray.getString(R.styleable.VyTextView_suffixText);
        mSuffixColor = typedArray.getColor(R.styleable.VyTextView_suffixColor, Color.BLUE);
        mClickSpanEnable = typedArray.getBoolean(R.styleable.VyTextView_clickSpanEnable, false);
        mClickTypeTo = typedArray.getInt(R.styleable.VyTextView_clickSpanTo, EXPAND);
        if (TextUtils.isEmpty(mSuffixText)) {
            mSuffixText = DEFAULT_SUFFIX_TEXT;
        }
        typedArray.recycle();
        mTextPaint = getPaint();
        mTextPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    /**
     * 设置内容
     *
     * @param content
     */
    public void setContent(String content) {
        if (content == null) {
            return;
        }
        setContent(content, mMaxShowLines);
    }

    public void setContent(final String content, int maxLine) {
        if (content == null) {
            return;
        }
        mContent = content;
        if (maxLine == 0) {
            maxLine = DEFAULT_SHOW_LINES;
        }
        mMaxShowLines = maxLine;
        if (mWidth <= 0) {
            if (getWidth() > 0)
                mWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        }

        if (mWidth <= 0) {
            if (mRetryTime > 10) {
                setText(DEFAULT_CONTENT);
            }
            this.post(new Runnable() {
                @Override
                public void run() {
                    mRetryTime++;
                    setContent(content);
                }
            });
        } else {
            setRealContent(content);
        }
    }

    private ArrayList<Integer> mEmoticonResIdList = new ArrayList<>();

    private void setRealContent(String content) {
        if (mTextHeight == 0) {
            Paint.FontMetrics fm = mTextPaint.getFontMetrics();
            mTextHeight = (int) Math.ceil(fm.bottom - fm.top);
        }
        if (mContentSb == null) {
            mContentSb = new StringBuilder();
        } else {
            mContentSb.delete(0, mContentSb.length());
        }
        Matcher emoticonMatcher = WEN_ZI.matcher(content);
        int startIndex = 0;
        mEmoticonResIdList.clear();
       /*表情相关处理
       while (emoticonMatcher.find()) {
            String key = emoticonMatcher.group();
            Integer integer = VyEmoticons.sVyEmoticonHashMap.get(key);
            if (integer == null) {
                continue;
            }
            mEmoticonResIdList.add(integer);
            int start = emoticonMatcher.start();
            mContentSb.append(content.toString().substring(startIndex, start)).append("[M]");
            startIndex = emoticonMatcher.end();
            //                Log.d("NKW--->", "start" + start + "||end" + startIndex);
        }*/
        mContentSb.append(content.toString().substring(startIndex, content.length()));
        //用来计算内容的大小
        mDynamicLayout = new DynamicLayout(mContentSb, mTextPaint, mWidth, Layout.Alignment.ALIGN_NORMAL, 1.2f, 0.0f,
                true);
        //获取行数
        int linesCount = mDynamicLayout.getLineCount();
        String substring;
        SpannableString spannable = null;
        if (mMaxShowLines < linesCount) {
            int currentLine = mMaxShowLines - 1;
            int endPosition = mDynamicLayout.getLineEnd(currentLine);
            int startPosition = mDynamicLayout.getLineStart(currentLine);
            float lineWidth = mDynamicLayout.getLineWidth(currentLine);
            int fitPosition = getFitPosition("... " + mSuffixText, endPosition, startPosition, lineWidth, mTextPaint.measureText("...  " + mSuffixText), 0, mContentSb);
            substring = mContentSb.substring(0, fitPosition);
            if (substring.endsWith("\n")) {
                substring = substring.substring(0, substring.length() - "\n".length());
            }
            if ("[".equals(substring.substring(substring.length() - 1)) || "[M".equals(substring.substring(substring.length() - 2))) {
                int lastIndexOf = substring.lastIndexOf("[");
                substring = substring.substring(0, lastIndexOf);
            }
            mContentSb.delete(0, mContentSb.length());
            mContentSb.append(substring);
            mContentSb.append("... ").append(mSuffixText);
            spannable = SpannableString.valueOf(mContentSb);
            spannable.setSpan(new ForegroundColorSpan(mSuffixColor), mContentSb.length() - mSuffixText.length(),
                    mContentSb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            if (mClickSpanEnable) {
                setMovementMethod(MyLinkedMovementMethod.getInstance());
                spannable.setSpan(mClickableSpan, mContentSb.length() - mSuffixText.length(), mContentSb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        if (spannable == null) {
            spannable = SpannableString.valueOf(mContentSb);
        }
        Matcher mm = CUSTOM.matcher(mContentSb);
        int findIndex = 0;
        if (mm != null) {
            while (mm.find()) {
                if (mEmoticonResIdList.size() > findIndex) {
                    Integer integer = mEmoticonResIdList.get(findIndex);
                    emoticonDisplay(getContext(), spannable, integer, mTextHeight, +mm.start(), mm.end());
                    findIndex++;
                } else {
                    break;
                }
            }
        }
        setText(spannable);
    }

    private void emoticonDisplay(Context context, Spannable spannable, int emoticon, int fontSize, int start, int end) {
        /*表情想关大小处理
        Drawable drawable = getDrawable(context, emoticon);
        drawable.setBounds(0, 0, fontSize, fontSize);
        EmoticonSpan imageSpan = new EmoticonSpan(drawable);
        spannable.setSpan(imageSpan, start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);*/
    }

    private Drawable getDrawable(Context context, int emoticon) {
        return Build.VERSION.SDK_INT >= 21 ? context.getResources().getDrawable(emoticon, (Resources.Theme) null) : context.getResources().getDrawable(emoticon);
    }

    /**
     * 计算原内容被裁剪的长度
     *
     * @param endString
     * @param endPosition   指定行最后文字的位置
     * @param startPosition 指定行文字开始的位置
     * @param lineWidth     指定行文字的宽度
     * @param endStringWith 最后添加的文字的宽度
     * @param offset        偏移量
     * @param sb
     * @return
     */
    private int getFitPosition(String endString, int endPosition, int startPosition, float lineWidth,
                               float endStringWith, float offset, StringBuilder sb) {
        //最后一行需要添加的文字的字数
        int position = (int) ((lineWidth - (endStringWith + offset)) * (endPosition - startPosition)
                / lineWidth);

        if (position <= endString.length())
            return endPosition;

        //计算最后一行需要显示的正文的长度
        float measureText = mTextPaint.measureText(
                (sb.substring(startPosition, startPosition + position)));

        //如果最后一行需要显示的正文的长度比最后一行的长减去“展开”文字的长度要短就可以了  否则加个空格继续算
        if (measureText <= lineWidth - endStringWith) {
            return startPosition + position;
        } else {
            return getFitPosition(endString, endPosition, startPosition, lineWidth, endStringWith, offset + mTextPaint.measureText(" "), sb);
        }
    }


    public void setOnClickSpanListener(OnClickSpanListener listener){
        mOnClickSpanListener = listener;
    }
    public interface OnClickSpanListener{
        void clickSpan();
    }

    private static class MyLinkedMovementMethod extends LinkMovementMethod {
        private static MyLinkedMovementMethod sInstance;
        public static MyLinkedMovementMethod getInstance() {
            if (sInstance == null)
                sInstance = new MyLinkedMovementMethod();
            return sInstance;
        }

        @Override
        public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
            // 因为TextView没有点击事件，所以点击TextView的非富文本时，super.onTouchEvent()返回false；
            // 此时可以让TextView的父容器执行点击事件；
            boolean isConsume =  super.onTouchEvent(widget, buffer, event);
            if (!isConsume && event.getAction() == MotionEvent.ACTION_UP) {
                ViewParent parent = widget.getParent();
                if (parent instanceof ViewGroup) {
                    // 获取被点击控件的父容器，让父容器执行点击；
                    ((ViewGroup) parent).performClick();
                }
            }
            return isConsume;
        }
    }
}

package com.nkw.customview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

public class VyNineGridImageLayout extends ViewGroup {

    public VyNineGridImageLayout(Context context) {
        super(context);
        init(context,null);
    }

    public VyNineGridImageLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public VyNineGridImageLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }
}

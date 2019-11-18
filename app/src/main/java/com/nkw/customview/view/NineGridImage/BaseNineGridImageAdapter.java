package com.nkw.customview.view.NineGridImage;

import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.widget.ImageView;

public abstract class BaseNineGridImageAdapter {

    // 被观察者，用来注册观察者
    private final DataSetObservable mDataSetObservable = new DataSetObservable();

    /**
     * 获取 item 的数量
     */
    protected abstract int getImageCount();

    /**
     * 获取第一张图片宽比高的比例
     */
    protected abstract float getFirstImagePer();

    /**
     * 获取图片的类型
     * @param position
     */
    protected String getImageType(int position){
        getImageUrl(position);
        return "jpg";
    }

    /**
     * 获取图片链接
     * @param position
     * @return
     */
    protected abstract String getImageUrl(int position);

    /**
     * 删除已经注册过的观察者
     *
     * @param observer
     */
    public void unregisterDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.unregisterObserver(observer);
    }

    /**
     * 注册观察者
     *
     * @param observer
     */
    public void registerDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.registerObserver(observer);
    }
    /**
     * 通知刷新
     */
    public void notifyDataSetChanged() {
        mDataSetObservable.notifyChanged();
    }

    protected abstract void bindData(int position, ImageView imageView, NineGridImageLayout nineGridImageLayout);
}

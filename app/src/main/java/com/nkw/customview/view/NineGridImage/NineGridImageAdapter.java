package com.nkw.customview.view.NineGridImage;

import java.util.List;

public class NineGridImageAdapter {
    private List<String> mDataList;

    public NineGridImageAdapter(List<String> dataList) {
        mDataList = dataList;
    }

    public void setNewData(List<String> dataList) {
        mDataList = dataList;
        notifyDataSetChanged();
    }

    public void notifyDataSetChanged() {

    }
}

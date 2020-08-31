package com.nkw.customview.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nkw.customview.R;
import com.nkw.customview.view.tagerGame.AbstractWheelAdapter;

/**
 * @author NKW
 * @createTime 2020/8/19 20:26
 * @description 该类
 */
public class TigerAdapter extends AbstractWheelAdapter {
    @Override
    public int getItemsCount() {
        return 10;
    }

    @Override
    public View getItem(int index, View convertView, ViewGroup parent) {
        View view;
        if (convertView != null) {
            view = convertView;
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tiger_img,parent,false);
        }
        ImageView img = (ImageView) view.findViewById(R.id.iv_num_img);
        switch (index) {
            case 0:
                img.setImageResource(R.mipmap.img_png0);
                break;

            case 1:
                img.setImageResource(R.mipmap.img_png1);
                break;

            case 2:
                img.setImageResource(R.mipmap.img_png2);
                break;

            case 3:
                img.setImageResource(R.mipmap.img_png3);
                break;

            case 4:
                img.setImageResource(R.mipmap.img_png4);
                break;

            case 5:
                img.setImageResource(R.mipmap.img_png5);
                break;

            case 6:
                img.setImageResource(R.mipmap.img_png6);
                break;

            case 7:
                img.setImageResource(R.mipmap.img_png7);
                break;

            case 8:
                img.setImageResource(R.mipmap.img_png8);
                break;

            case 9:
                img.setImageResource(R.mipmap.img_png9);
                break;

        }

        return view;
    }
}

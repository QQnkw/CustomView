package com.nkw.customview.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.nkw.customview.R;
import com.nkw.customview.comment.AppLocalData;
import com.nkw.customview.utils.GlideUtils;
import com.nkw.customview.view.NineGridImage.BaseNineGridImageAdapter;
import com.nkw.customview.view.NineGridImage.NineGridImageLayout;
import com.nkw.customview.view.VyTextView;
import com.nkw.customview.view.fourGridView.ImageViewGroup;
import com.nkw.customview.view.popupwindow.PopWindowAboutPost;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import butterknife.BindView;

public class FourGridViewActivity extends BaseActivity {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Override
    protected void initViewSet() {
        super.initViewSet();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        FourGridViewAdapter adapter = new FourGridViewAdapter(this);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_four_grid_view;
    }

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, FourGridViewActivity.class));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("NKW","FourGridViewActivity:onSaveInstanceState");
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState!=null) {
            String string = savedInstanceState.getString("NKW");
            Toast.makeText(this,"FourGridViewActivity:onRestoreInstanceState--->"+string,Toast.LENGTH_LONG).show();
        }
    }


    private static class FourGridViewAdapter extends RecyclerView.Adapter<FourGridViewHolder> {

        private final Random             mRandom;
        private final Context            mContext;
        private final PopWindowAboutPost mPopWindowAboutPost;
        private final String[]           mPoems;

        public FourGridViewAdapter(Context context) {
            mRandom = new Random();
            mContext = context;
            mPopWindowAboutPost = new PopWindowAboutPost((FourGridViewActivity) context);
            mPoems = context.getResources().getStringArray(R.array.poems);
        }

        @NonNull
        @Override
        public FourGridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_four_img, parent, false);
            return new FourGridViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final FourGridViewHolder holder, int position) {
            int num = mRandom.nextInt(10) + 1;
            String[] strings = Arrays.copyOf(AppLocalData.imgUrlArr, num);
            final ArrayList<String> list = new ArrayList<>();
            Collections.addAll(list, strings);
            holder.mCustomGridView.loadImageLayout(list, new ImageViewGroup.LoadImageLayoutSuccessListener() {
                @Override
                public void loadPicUrl(final ImageView imageView, String url) {
                    if (!list.isEmpty()) {
                        if (list.size() == 1) {
                            Glide.with(mContext).asBitmap().load(url).into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                    float v = resource.getWidth() * 1f / resource.getHeight();
                                    ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
                                    layoutParams.width = holder.mCustomGridView.getWidth();
                                    layoutParams.height = (int) (holder.mCustomGridView.getWidth()/v);
                                    imageView.setLayoutParams(layoutParams);
                                    imageView.setImageBitmap(resource);
                                }
                            });
                        } else {
                            GlideUtils.loadImage(mContext, url, imageView);
                        }
                    }
                }

                @Override
                public void onClickImage(ImageView imageView, int position, ArrayList<String> picUrlList) {

                }
            });
            holder.mBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPopWindowAboutPost.showPopup(v);
                }
            });
            holder.mTvContent.setText(mPoems[position]);
            holder.mTvExpand.setContent(mPoems[position]);
            holder.mNineGrid.setAdapter(new BaseNineGridImageAdapter() {
                @Override
                protected int getImageCount() {
                    return list.size();
                }

                @Override
                protected float getFirstImagePer() {
                    return 1.5f;
                }

                @Override
                protected String getImageUrl(int position) {
                    return list.get(position);
                }

                @Override
                protected void bindData(int position, ImageView imageView, NineGridImageLayout nineGridImageLayout) {
                    GlideUtils.loadImage(mContext, list.get(position), imageView);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mPoems.length;
        }

        //半角转全角，解决中英文排版问题，效果不好
        private String ToDBC(String input) {
            char[] c = input.toCharArray();
            for (int i = 0; i < c.length; i++) {
                if (c[i] == 12288) {
                    c[i] = (char) 32;
                    continue;
                }
                if (c[i] > 65280 && c[i] < 65375)
                    c[i] = (char) (c[i] - 65248);
            }
            return new String(c);
        }


    }

    private static class FourGridViewHolder extends RecyclerView.ViewHolder {

        private final TextView       mTvContent;
        private final VyTextView     mTvExpand;
        private       ImageViewGroup mCustomGridView;
        private final Button         mBtn;
        private final NineGridImageLayout mNineGrid;

        public FourGridViewHolder(View itemView) {
            super(itemView);
            mTvExpand = itemView.findViewById(R.id.tv_expand);
            mTvContent = itemView.findViewById(R.id.tv_content);
            mCustomGridView = itemView.findViewById(R.id.custom_grid_view);
            mBtn = itemView.findViewById(R.id.btn);
            mNineGrid = itemView.findViewById(R.id.nine_grid);
        }
    }

}

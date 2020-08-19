package com.nkw.customview.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nkw.customview.R;
import com.nkw.customview.comment.AppLocalData;
import com.nkw.customview.utils.GlideUtils;
import com.nkw.customview.view.CustomImageView;
import com.nkw.customview.view.DiamondLayoutManager;

import java.util.Random;

import butterknife.BindView;

public class CustomLayoutManagerActivity extends BaseActivity {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private Random mRandom;

    public static void startActivity(BaseActivity activity) {
        activity.startActivity(new Intent(activity, CustomLayoutManagerActivity.class));
    }

    @Override
    protected void initViewSet() {
        mRecyclerView.setLayoutManager(new DiamondLayoutManager());
        mRandom = new Random();
        mRecyclerView.setAdapter(new RecyclerView.Adapter<VhImageHolder>() {
            @NonNull
            @Override
            public VhImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                Log.d("NKW--->", "onCreateViewHolder");
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_custom_layout_manager, parent, false);
                int width = mRecyclerView.getWidth() / 2;
                return new VhImageHolder(view, width);
            }

            @Override
            public void onBindViewHolder(@NonNull VhImageHolder holder, int position) {
                Log.d("NKW--->", "onBindViewHolder");
                holder.mTv.setText(position + "");
                int i = mRandom.nextInt(10);
                holder.mIv.setImageResource(R.mipmap.pic);
                GlideUtils.loadImage(CustomLayoutManagerActivity.this, AppLocalData.imgUrlArr[i], holder.mIv);
            }

            @Override
            public int getItemCount() {
                return 100;
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_custom_layout_manager;
    }

    private static class VhImageHolder extends RecyclerView.ViewHolder {

        TextView mTv;
        CustomImageView mIv;

        public VhImageHolder(View itemView, int width) {
            super(itemView);
            mIv = itemView.findViewById(R.id.iv);
            mTv = itemView.findViewById(R.id.tv);
            ViewGroup.LayoutParams layoutParams = mIv.getLayoutParams();
            layoutParams.width = width;
            layoutParams.height = width;
        }
    }
}

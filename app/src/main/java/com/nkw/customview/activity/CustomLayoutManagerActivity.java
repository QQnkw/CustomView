package com.nkw.customview.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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

public class CustomLayoutManagerActivity extends AppCompatActivity {

    private Random mRandom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_layout_manager);
        final RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new DiamondLayoutManager());
        mRandom = new Random();
        recyclerView.setAdapter(new RecyclerView.Adapter<VhImageHolder>() {
            @NonNull
            @Override
            public VhImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                Log.d("NKW--->","onCreateViewHolder");
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_custom_layout_manager, parent, false);
                int width = recyclerView.getWidth() / 2;
                return new VhImageHolder(view,width);
            }

            @Override
            public void onBindViewHolder(@NonNull VhImageHolder holder, int position) {
                Log.d("NKW--->","onBindViewHolder");
                holder.mTv.setText(position+"");
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

    private static class VhImageHolder extends RecyclerView.ViewHolder{

        TextView        mTv;
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

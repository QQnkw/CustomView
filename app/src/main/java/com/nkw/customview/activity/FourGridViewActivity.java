package com.nkw.customview.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nkw.customview.R;
import com.nkw.customview.comment.AppLocalData;
import com.nkw.customview.utils.GlideUtils;
import com.nkw.customview.view.fourGridView.VyFourGridView;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FourGridViewActivity extends AppCompatActivity {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_four_grid_view);
        ButterKnife.bind(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        FourGridViewAdapter adapter = new FourGridViewAdapter(this);
        mRecyclerView.setAdapter(adapter);
    }

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, FourGridViewActivity.class));
    }

    private static class FourGridViewAdapter extends RecyclerView.Adapter<FourGridViewHolder> {

        private final Random  mRandom;
        private final Context mContext;

        public FourGridViewAdapter(Context context) {
            mRandom = new Random();
            mContext = context;
        }

        @NonNull
        @Override
        public FourGridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_four_img, parent, false);
            return new FourGridViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull FourGridViewHolder holder, int position) {
            int num = mRandom.nextInt(10) + 1;
            String[] strings = Arrays.copyOf(AppLocalData.imgUrlArr, num);
            List<String> imgList = Arrays.asList(strings);
            holder.mFourGridView.setVyFourGridViewData(imgList, new VyFourGridView.ImageDisplayUtil() {
                @Override
                public void displayImage(ImageView imageView, String imgUrl) {
                    GlideUtils.loadImage(mContext, imgUrl, imageView);
                    //                    GlideUtils.loadRoundCircleImage(mContext, imgUrl, imageView, SizeUtils.dp2px(5));
                }
            });
            holder.mTv.setText(imgList.size() + "");
           /* holder.mMultiImageLayout.setDisplayCount(imgList.size());
            holder.mMultiImageLayout.setAdapter(new MultiImageLayout.Adapter() {
                @Override
                public void loadImage(int position, int total, String path, ImageView iv) {
                    GlideUtils.loadImage(mContext, path, iv);
                }
            });
            holder.mMultiImageLayout.setImages(imgList);*/
        }

        @Override
        public int getItemCount() {
            return 50;
        }
    }

    private static class FourGridViewHolder extends RecyclerView.ViewHolder {

        private TextView         mTv;
        private VyFourGridView   mFourGridView;
//        private MultiImageLayout mMultiImageLayout;

        public FourGridViewHolder(View itemView) {
            super(itemView);
            mTv = itemView.findViewById(R.id.tv);
            mFourGridView = itemView.findViewById(R.id.four_grid_view);
//            mMultiImageLayout = itemView.findViewById(R.id.multiImageLayout);
        }
    }
}

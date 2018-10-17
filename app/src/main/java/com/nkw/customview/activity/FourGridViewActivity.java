package com.nkw.customview.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nkw.customview.R;
import com.nkw.customview.comment.AppLocalData;
import com.nkw.customview.utils.GlideUtils;
import com.nkw.customview.view.ExpandableTextView;
import com.nkw.customview.view.fourGridView.VyFourGridView;
import com.nkw.customview.view.popupwindow.PopWindowAboutPost;

import java.util.Arrays;
import java.util.List;
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

    private static class FourGridViewAdapter extends RecyclerView.Adapter<FourGridViewHolder> {

        private final Random             mRandom;
        private final Context            mContext;
        private final PopWindowAboutPost mPopWindowAboutPost;
        private final String[]           mPoems;
        private       int                mEtvWidth;

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
            holder.mBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPopWindowAboutPost.showPopup(v);
                }
            });
            if (mEtvWidth == 0) {
                holder.mEtv.post(new Runnable() {
                    @Override
                    public void run() {
                        mEtvWidth = holder.mEtv.getWidth();
                    }
                });
            }
            holder.mEtv.updateForRecyclerView(ToDBC(mPoems[position]), mEtvWidth, ExpandableTextView.STATE_SHRINK);
            holder.mTvContent.setText(ToDBC(mPoems[position]));
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

        private final ExpandableTextView mEtv;
        private final TextView           mTvContent;
        private       TextView           mTv;
        private       VyFourGridView     mFourGridView;
        private final Button             mBtn;
        //        private MultiImageLayout mMultiImageLayout;

        public FourGridViewHolder(View itemView) {
            super(itemView);
            mTv = itemView.findViewById(R.id.tv);
            mEtv = itemView.findViewById(R.id.etv);
            mTvContent = itemView.findViewById(R.id.tv_content);
            mFourGridView = itemView.findViewById(R.id.four_grid_view);
            //            mMultiImageLayout = itemView.findViewById(R.id.multiImageLayout);
            mBtn = itemView.findViewById(R.id.btn);
        }
    }

}

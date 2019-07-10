package com.nkw.customview.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.RawRes;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.nkw.customview.R;

import java.io.File;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.GrayscaleTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * 描述：Glide工具类（glide 4.x）
 * 功能包括加载图片，圆形图片，圆角图片，指定圆角图片，模糊图片，灰度图片等等。
 * 目前我只加了这几个常用功能，其他请参考glide-transformations这个开源库。
 * https://github.com/wasabeef/glide-transformations
 */
public class GlideUtils {
    //    public static final int placeholderSoWhite = R.mipmap.bg_common;
    public static final int placeholderSoWhite = R.color.app_white_fff;
    //    public static final int errorSoWhite = R.mipmap.bg_common;
    public static final int errorSoWhite = R.color.app_white_fff;
    // public static final int soWhite = R.color.white;

    /*
     *加载图片(默认)
     */
    public static void loadImage(Context context, String url, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(placeholderSoWhite) //占位图
                .error(errorSoWhite)       //错误图
                /*.format(DecodeFormat.PREFER_RGB_565)*/;
        Glide.with(context).load(url).apply(options).transition(DrawableTransitionOptions.withCrossFade()).into(imageView);

    }

    /*
     *加载Drawable图片
     */
    public static void loadImage(Context context, @RawRes @DrawableRes @Nullable Integer resourceId, ImageView
            imageView) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(placeholderSoWhite) //占位图
                .error(R.color.app_white_fff)       //错误图
                /*.format(DecodeFormat.PREFER_RGB_565)*/;
        Glide.with(context).load(resourceId).apply(options).transition(DrawableTransitionOptions.withCrossFade()).into(imageView);

    }

    /**
     * 指定图片大小;使用override()方法指定了一个图片的尺寸。
     * Glide现在只会将图片加载成width*height像素的尺寸，而不会管你的ImageView的大小是多少了。
     * 如果你想加载一张图片的原始尺寸的话，可以使用Target.SIZE_ORIGINAL关键字----override(Target.SIZE_ORIGINAL)
     */
    public static void loadImageSize(Context context, String url, ImageView imageView, int width, int height) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(placeholderSoWhite) //占位图
                .error(errorSoWhite)       //错误图
                .override(width, height);
        Glide.with(context).load(url).apply(options).into(imageView);

    }


    /**
     * 禁用内存缓存功能
     * diskCacheStrategy()方法基本上就是Glide硬盘缓存功能的一切，它可以接收五种参数：
     * <p>
     * DiskCacheStrategy.NONE： 表示不缓存任何内容。
     * DiskCacheStrategy.DATA： 表示只缓存原始图片。
     * DiskCacheStrategy.RESOURCE： 表示只缓存转换过后的图片。
     * DiskCacheStrategy.ALL ： 表示既缓存原始图片，也缓存转换过后的图片。
     * DiskCacheStrategy.AUTOMATIC： 表示让Glide根据图片资源智能地选择使用哪一种缓存策略（默认选项）。
     */

    public static void loadImageSizekipMemoryCache(Context context, String url, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .placeholder(placeholderSoWhite) //占位图
                .error(errorSoWhite)       //错误图S
                .skipMemoryCache(true)//禁用掉Glide的内存缓存功能
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(context).load(url).apply(options).into(imageView);

    }


    /**
     * 加载圆形图片
     */
    public static void loadCircleImage(Context context, String url, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .circleCrop()//设置圆形
                .placeholder(placeholderSoWhite)
                .error(errorSoWhite);
        Glide.with(context).load(url).apply(options).into(imageView);
    }

    /**
     * 预先加载图片
     * 在使用图片之前，预先把图片加载到缓存，调用了预加载之后，我们以后想再去加载这张图片就会非常快了，
     * 因为Glide会直接从缓存当中去读取图片并显示出来
     */
    public static void preloadImage(Context context, String url) {
        Glide.with(context)
                .load(url)
                .preload();
    }

    /**
     * 加载圆角图片
     */
    public static void loadRoundCircleImage(Context context, String url, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .bitmapTransform(new MultiTransformation(new CenterCrop(), new RoundedCornersTransformation(15, 0,
                        RoundedCornersTransformation.CornerType.ALL)))
                .placeholder(placeholderSoWhite)
                .error(errorSoWhite);
        Glide.with(context).load(url).apply(options).into(imageView);

    }

    /**
     * 加载圆角图片,指定圆角尺寸
     */
    public static void loadRoundCircleImage(Context context, String url, ImageView imageView, int radius) {
        RequestOptions options =
                new RequestOptions().bitmapTransform(new MultiTransformation<>(new CenterCrop(), new
                        RoundedCornersTransformation(radius, 0, RoundedCornersTransformation.CornerType.ALL)))
                        .placeholder(placeholderSoWhite)
                        .error(errorSoWhite)
                        /*.format(DecodeFormat.PREFER_RGB_565)*/;
        Glide.with(context)
                .load(url)
                .apply(options)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);
    }

    /**
     * 加载圆角图片,指定圆角尺寸
     */
    public static void loadRoundCircleImageWithThumbnail(Context context, String url, String thumbnailUrl, ImageView
            imageView, int radius) {
        RequestOptions options =
                new RequestOptions().bitmapTransform(new MultiTransformation<>(new CenterCrop(), new
                        RoundedCornersTransformation(radius, 0, RoundedCornersTransformation.CornerType.ALL)))
                        .placeholder(placeholderSoWhite)
                        .error(errorSoWhite)
                        /*.disallowHardwareConfig()*/;//关闭硬件位图
        Glide.with(context)
                .load(url)
                .apply(options)
                .into(imageView);
        //                .thumbnail(Glide.with(context).load(thumbnailUrl))
    }


    /**
     * 加载圆角图片-指定任意部分圆角（图片上、下、左、右四个角度任意定义）
     *
     * @param context
     * @param url
     * @param imageView
     * @param type
     */
    public static void loadCustRoundCircleImage(Context context, String url, ImageView imageView,
                                                RoundedCornersTransformation.CornerType type) {
        RequestOptions options = new RequestOptions()
                .bitmapTransform(new MultiTransformation(new CenterCrop(), new RoundedCornersTransformation(15, 0,
                        type)))
                .placeholder(placeholderSoWhite)
                .error(errorSoWhite)
                /*.format(DecodeFormat.PREFER_RGB_565)*/;

        Glide.with(context).load(url).apply(options).transition(DrawableTransitionOptions.withCrossFade()).into(imageView);
    }


    /**
     * 加载模糊图片（自定义透明度）
     *
     * @param context
     * @param url
     * @param imageView
     * @param blur      模糊度，一般1-100够了，越大越模糊
     */
    public static void loadBlurImage(Context context, String url, ImageView imageView, int blur) {
        RequestOptions options = new RequestOptions()
                .bitmapTransform(new MultiTransformation(new CenterCrop(), new BlurTransformation(blur)))
                .placeholder(placeholderSoWhite)
                .error(errorSoWhite);
        Glide.with(context).load(url).apply(options).into(imageView);
    }

    /*
     *加载灰度(黑白)图片（自定义透明度）
     */
    public static void loadBlackImage(Context context, String url, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .bitmapTransform(new MultiTransformation(new CenterCrop(), new GrayscaleTransformation()))
                .placeholder(placeholderSoWhite)
                .error(errorSoWhite);
        Glide.with(context).load(url).apply(options).into(imageView);
    }

    /**
     * 加载Gif图片
     * 如果加载的图片不是gif，则asGif()会报错， 当然，asGif()不写也是可以正常加载的。
     * 加入了一个asBitmap()方法，这个方法的意思就是说这里只允许加载静态图片，不需要Glide去帮我们自动进行图片格式的判断了。
     * 如果你传入的还是一张GIF图的话，Glide会展示这张GIF图的第一帧，而不会去播放它。
     *
     * @param url 例如：https://image.niwoxuexi.com/blog/content/5c0d4b1972-loading.gif
     */
    private void loadGif(Context context, String url, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .placeholder(placeholderSoWhite)
                .error(errorSoWhite);
        Glide.with(context)
                .load(url)
                .apply(options)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target,
                                                boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target,
                                                   DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(imageView);

    }

    /**
     * 先下载图片到本地
     */
    public void downloadImage(final Context context, final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //String url = "http://www.guolin.tech/book.png";
                    FutureTarget<File> target = Glide.with(context)
                            .asFile()
                            .load(url)
                            .submit();
                    final File imageFile = target.get();
                    Log.d("logcat", "下载好的图片文件路径=" + imageFile.getPath());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 加载网络头像
     */
    public static void loadAvatar(Context context, String url, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .circleCrop()//设置圆形
                .placeholder(R.color.app_white_fff)
                .error(R.color.app_white_fff);
        Glide.with(context)
                .load(url)
                .apply(options)
                .into(imageView);
    }
}

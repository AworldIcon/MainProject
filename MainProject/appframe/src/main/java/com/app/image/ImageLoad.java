package com.app.image;

/**
 * Created by MaShiZhao on 2017/1/9
 */

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by MaShiZhao on 2016/12/8.
 * 图片加载工具 正使用的是 glide
 */
public class ImageLoad
{

    /**
     *
     *  圆角或着其它特效参考:
     *  https://github.com/wasabeef/glide-transformations
     *
     *  crossFade()    淡入淡出 crossFade(intanimationId, int duration) 时间和样式 crossfade(intduration)
     *  dontAnimate()  无动画
     *  centerCrop() fitCenter()  // 设置圆角时不能用 大图片可能会超出图片区域
     *
     */

    /**
     * 加载网路图片
     *
     * @param context       context
     * @param url           加载地址
     * @param beforResource 加载前的图片
     * @param errorResource 加载失败的图片
     * @param radius        图片加载的圆角
     * @param cornerType    圆角位置 多种样式
     * @param imageView     加载控件
     */

    public static void loadImage(Context context, String url, int width, int height, int beforResource, int errorResource, int radius, RoundedCornersTransformation.CornerType cornerType, ImageView imageView)
    {
        Glide.with(context)                      //可以是context activity fragment FragmentActivity
                .load(url)                       //加载网路地址
                .override(width, height)         //重置图片的大小尺寸
                .crossFade()                     //淡入淡出
                .error(errorResource)            //加载失败的图片
                .placeholder(beforResource)      //加载中的图片
                .bitmapTransform(new RoundedCornersTransformation(context, radius, 0, cornerType)) // radius 圆角大小 radius CornerType有多种形式
                .diskCacheStrategy(DiskCacheStrategy.RESULT)       //缓存多种尺寸
                .skipMemoryCache(true)
                .into(imageView);               //需要加载的控件
    }

    /**
     *
     */
    public static void loadImage(Context context, String url, int beforResource, int errorResource, int radius, RoundedCornersTransformation.CornerType cornerType, ImageView imageView)
    {
        Glide.with(context)                      //可以是context activity fragment FragmentActivity
                .load(url)                       //加载网路地址
                .crossFade()                     //淡入淡出
                .error(errorResource)            //加载失败的图片
                .placeholder(beforResource)      //加载中的图片
                .bitmapTransform(new RoundedCornersTransformation(context, radius, 0, cornerType)) // radius 圆角大小 radius CornerType有多种形式
                .diskCacheStrategy(DiskCacheStrategy.RESULT)       //缓存多种尺寸
                .skipMemoryCache(true)
                .into(imageView);               //需要加载的控件
    }

    /**
     * 不设置图片大小 保持原图
     * 圆形 圆形 圆形 图片
     */
    public static void loadCirCleImage(Context context, String url, int errorResource, ImageView imageView)
    {
        Glide.with(context)                      //可以是context activity fragment FragmentActivity
                .load(url)                       //加载网路地址
                .crossFade()                     //淡入淡出
                .error(errorResource)            //加载失败的图片
                .placeholder(errorResource)      //加载中的图片
                .bitmapTransform(new CropCircleTransformation(context)) // radius 圆角大小 radius CornerType有多种形式
                .diskCacheStrategy(DiskCacheStrategy.RESULT)       //缓存多种尺寸
                .skipMemoryCache(true)
                .into(imageView);               //需要加载的控件
    }

    /**
     * 不设置图片大小 保持原图
     * 没有圆角
     */
    public static void loadImage(Context context, String url, int beforResource, int errorResource, ImageView imageView)
    {
        Glide.with(context)                      //可以是context activity fragment FragmentActivity
                .load(url)                       //加载网路地址
                .crossFade()                     //淡入淡出
                .error(errorResource)            //加载失败的图片
                .placeholder(beforResource)      //加载中的图片
                .diskCacheStrategy(DiskCacheStrategy.RESULT)       //缓存多种尺寸
                .skipMemoryCache(true)
                .into(imageView);               //需要加载的控件
    }

    /**
     * 加载失败和加载中的图片一致
     */
    public static void loadImage(Context context, String url, int errorResource, int radius, RoundedCornersTransformation.CornerType cornerType, ImageView imageView)
    {
        loadImage(context, url, errorResource, errorResource, radius, cornerType, imageView);
    }

    /**
     * 加载失败和加载中的图片一致
     * 四边都有圆角
     */
    public static void loadRoundImage(Context context, String url, int errorResource, int radius, ImageView imageView)
    {
        loadImage(context, url, errorResource, radius, RoundedCornersTransformation.CornerType.ALL, imageView);
    }

    /**
     * 加载失败和加载中的图片一致
     * 四边无圆角
     */
    public static void loadImage(Context context, String url, int errorResource, ImageView imageView)
    {
        loadImage(context, url, errorResource, errorResource, imageView);
    }


}


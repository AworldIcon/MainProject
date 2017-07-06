package com.coder.kzxt.utils;

import android.content.Context;
import android.widget.ImageView;

import com.app.utils.ImageUtils;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.coder.kzxt.activity.R;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by MaShiZhao on 2016/12/8
 */
public class GlideUtils
{

    /**
     * 注意 圆角时不能设置scaleType centerCrop   center  fitxy   可以设置centerInside
     */

    private static final int ROUND_RADIUS_SMALL = 6;
    private static final int ROUND_RADIUS_DEFAULT = 30;
    private static final int ROUND_RADIUS_LARGE = 60;


    /**
     * 加载通用的圆角头像
     */
    public static void loadCircleHeaderOfCommon(Context context, String url, ImageView image)
    {
        ImageUtils.loadCirCleImage(context, url, R.drawable.default_common_header, image);
    }

    public static void loadHeaderOfCommon(Context context, String url, ImageView image)
    {
        ImageUtils.loadImage(context, url, R.color.transparent, image);
    }

    /**
     * 圆角图片 老师的
     */
    public static void loadHeaderOfTeacher(Context context, String url, ImageView image)
    {
        //当前版本没有区分老师 学生  。下个版本区分要添加说明
        ImageUtils.loadCirCleImage(context, url, R.drawable.default_common_header, image);
//        ImageUtils.loadRoundImage(context, url, R.drawable.default_teacher_header, ROUND_RADIUS_SMALL, image);
    }

    /**
     * 圆角图片学生的
     */
    public static void loadHeaderOfStudnet(Context context, String url, ImageView image)
    {
        ImageUtils.loadRoundImage(context, url, R.drawable.default_student_header, ROUND_RADIUS_SMALL, image);
    }

    /**
     * 加载ic图片
     *
     * @param context
     * @param url
     * @param image
     */
    public static void loadGoods(Context context, String url, ImageView image)
    {
        ImageUtils.loadImage(context, url, R.drawable.iv_category_course, image);
    }

    /**
     * 加载我的页面图片
     *
     * @param context
     * @param url
     * @param image
     */
    public static void loadMyImage(Context context, String url, ImageView image)
    {
        ImageUtils.loadImage(context, url, R.drawable.spread_template1, image);
    }

    /**
     * 加载海报图片
     */

    public static void loadPorstersImg(Context context, String url, ImageView image)
    {
        ImageUtils.loadImage(context, url, R.drawable.poster_template1, R.drawable.poster_template1, 0.5f, DiskCacheStrategy.NONE, image);
    }

    /**
     * 加载海报分类图片
     */

    public static void loadPorstersType(Context context, String url, ImageView image)
    {
        ImageUtils.loadImage(context, url, R.drawable.poster_cata_def, image);
    }


    public static void loadPoseters(Context context, String url, int drawableId, ImageView image)
    {
        ImageUtils.loadImage(context, url, drawableId, drawableId, 0.5f, DiskCacheStrategy.NONE, image);
    }

    /**
     * 加载广告图片
     */
    public static void loadSreadImg(Context context, String url, ImageView image)
    {
        ImageUtils.loadImage(context, url, R.drawable.spread_template1, image);
    }

    /**
     * 加载课程图片
     *
     * @param context
     * @param url
     * @param image
     */
    public static void loadCourseImg(Context context, String url, ImageView image)
    {
        ImageUtils.loadImage(context, url, R.drawable.my_course_def, image);
    }


    /**
     * 加载zip图片
     *
     * @param context
     * @param url
     * @param image
     */
    public static void loadZipImg(Context context, String url, ImageView image)
    {
        ImageUtils.loadImage(context, url, R.drawable.default_zip, image);
    }

    /**
     * 加载问答的暂无图片4:3比例
     *
     * @param context
     * @param url
     * @param image
     */
    public static void loadQuestionsImg(Context context, String url, ImageView image)
    {
        ImageUtils.loadImage(context, url, R.drawable.my_question_def, R.drawable.my_question_def, 0.5f, DiskCacheStrategy.NONE, image);
    }

    /**
     * 加载班级的图片
     *
     * @param context
     * @param url
     * @param image
     */
    public static void loadClassImg(Context context, String url, ImageView image)
    {
        ImageUtils.loadCirCleImage(context, url, R.drawable.course_default, image);
    }

    /**
     * 加载证件照片
     *
     * @param context
     * @param url
     * @param image
     */
    public static void loadCredentialsImg(Context context, String url, ImageView image)
    {
        ImageUtils.loadImage(context, url, R.drawable.credentials_template, image);
    }


    /**
     * 加载发现页的图片
     *
     * @param context
     * @param url
     * @param image
     */
    public static void loadDiscoverImage(Context context, String url, ImageView image)
    {
        ImageUtils.loadImage(context, url, R.drawable.discovery_default, image);
    }

    /**
     * 加载班级的圆角头像
     */
    public static void loadHeaderOfClass(Context context, String url, ImageView image)
    {
        ImageUtils.loadCirCleImage(context, url, R.drawable.class_cirlce_comment_icon, image);
    }

    /**
     * 加载班级话题icon
     * @param context
     * @param url
     * @param image
     */
    public static void loadClassTopicImg(Context context, String url, ImageView image) {
        ImageUtils.loadImage(context, url, R.drawable.my_question_def, R.drawable.my_question_def, 0.5f, DiskCacheStrategy.NONE, image);
    }

    /**
     * 加载服务里面的课程图片 上面圆角
     * @param context
     * @param url
     * @param image
     */
    public static void loadTopRoundService(Context context, String url, ImageView image) {
        ImageUtils.loadImage(context, url, R.drawable.my_course_def, ROUND_RADIUS_SMALL, RoundedCornersTransformation.CornerType.TOP, image);
    }

}
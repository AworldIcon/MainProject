package com.coder.kzxt.course.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.image.ImageLoad;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.course.beans.GoodCourseResult;
import com.coder.kzxt.recyclerview.adapter.BaseViewHolder;
import com.coder.kzxt.recyclerview.adapter.PullRefreshDelegate;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.video.activity.VideoViewActivity;

import java.util.List;

/**
 * Created by pc on 2017/3/11.
 */

public class GoodCourseDelegate extends PullRefreshDelegate<GoodCourseResult.DataBean.CourseBean> {
    private Context context;
    private int type;
    public GoodCourseDelegate(Context context,int type){
        super(R.layout.item_commonlesson_one_item);
        this.context=context;
        this.type=type;
    }

    @Override
    public void initCustomView(BaseViewHolder holder, List<GoodCourseResult.DataBean.CourseBean> data, final int position) {
        ImageView boutique_course_img = (ImageView) holder.findViewById(R.id.boutique_course_img);
        ImageView free_or_vip = (ImageView) holder.findViewById(R.id.free_or_vip);
        TextView boutique_course_name = (TextView) holder.findViewById(R.id.boutique_course_name);
        LinearLayout public_course_ly = (LinearLayout) holder.findViewById(R.id.public_course_ly);
        TextView boutique_school_name = (TextView) holder.findViewById(R.id.boutique_school_name);
        TextView common_lesson_price = (TextView) holder.findViewById(R.id.common_lesson_price);
        RelativeLayout boutique_course_ly = (RelativeLayout) holder.findViewById(R.id.boutique_course_ly);
        TextView boutique_course_hour_number = (TextView) holder.findViewById(R.id.boutique_course_hour_number);
        TextView boutique_lesson_price = (TextView) holder.findViewById(R.id.boutique_lesson_price);

        final String pic = data.get(position).getPic();
        final String title = data.get(position).getTitle();
        String price = data.get(position).getPrice();
        final String courseId = data.get(position).getCourseId();
       // imageLoader.displayImage(pic, holder.boutique_course_img, ImageLoaderOptions.courseOptions);
        ImageLoad.loadImage(context,pic,R.drawable.my_course_def,R.drawable.my_course_def,boutique_course_img);
        boutique_course_name.setText(title);

        //如果是精品课程
        if(type==0){
            boutique_lesson_price.setVisibility(View.GONE);
            String lessonNum = data.get(position).getLessonNum();
            boutique_course_ly.setVisibility(View.VISIBLE);
            public_course_ly.setVisibility(View.GONE);

            if (TextUtils.isEmpty(price) || price.equals("0") || price.equals("0.0") || price.equals("0.00")) {
                free_or_vip.setBackgroundResource(R.drawable.live_free_iv);
//				holder.boutique_lesson_price.setText(R.string.free);
//				holder.boutique_lesson_price.setTextColor(context.getResources().getColor(R.color.font_green));
            } else {
                free_or_vip.setBackgroundResource(R.drawable.live_vip_iv);
                //holder.boutique_lesson_price.setText("¥ " + price);
//				holder.boutique_lesson_price.setVisibility(View.GONE);
                //holder.boutique_lesson_price.setTextColor(context.getResources().getColor(R.color.font_red));
            }
            boutique_course_hour_number.setText(lessonNum + context.getResources().getString(R.string.course_hours));

            //如果是资源库课程
        }else {
            common_lesson_price.setVisibility(View.GONE);
            public_course_ly.setVisibility(View.VISIBLE);
            boutique_course_ly.setVisibility(View.GONE);
            String schoolName = data.get(position).getSubtitle();
            boutique_school_name.setText(schoolName);
            if (TextUtils.isEmpty(price) || price.equals("0") || price.equals("0.0") || price.equals("0.00")) {
                free_or_vip.setBackgroundResource(R.drawable.live_free_iv);
//				holder.common_lesson_price.setText(R.string.free);
//				holder.common_lesson_price.setTextColor(context.getResources().getColor(R.color.font_green));
            } else {
                free_or_vip.setBackgroundResource(R.drawable.live_vip_iv);
                //holder.common_lesson_price.setText("¥ " + price);
//				holder.common_lesson_price.setVisibility(View.INVISIBLE);
                //holder.common_lesson_price.setTextColor(context.getResources().getColor(R.color.font_red));
            }
        }
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VideoViewActivity.class);
                intent.putExtra("flag", Constants.ONLINE);
                intent.putExtra("treeid", courseId);
                intent.putExtra("tree_name", title);
                intent.putExtra("pic", pic);
                intent.putExtra(Constants.IS_CENTER, "0");
                context.startActivity(intent);
            }
        });
    }
}

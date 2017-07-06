package com.coder.kzxt.main.delegate;

import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.coder.kzxt.activity.R;
import com.coder.kzxt.course.beans.CourseBean;
import com.coder.kzxt.recyclerview.adapter.BaseViewHolder;
import com.coder.kzxt.recyclerview.adapter.PullRefreshDelegate;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.GlideUtils;
import com.coder.kzxt.video.activity.VideoViewActivity;

import java.util.List;

import static com.coder.kzxt.utils.PublicUtils.IsEmpty;

/**
 * Created by tangcy on 2017/5/2.
 */

public class CourseDelegate  extends PullRefreshDelegate<com.coder.kzxt.course.beans.CourseBean.Course> {
    private Context context;
    private String  keyword;
    public CourseDelegate(Context context,String keyword) {
        super(R.layout.item_course);
        this.context = context;
        this.keyword = keyword;
    }


    @Override
    public void initCustomView(BaseViewHolder holder, final List<CourseBean.Course> data, final int position) {

        TextView courseNamet = (TextView) holder.findViewById(R.id.course_name_text);
        ImageView courseImagei = (ImageView) holder.findViewById(R.id.course_img);
        ImageView freeOrVipImagei= (ImageView) holder.findViewById(R.id.free_or_vip);
        TextView studyTimet = (TextView) holder.findViewById(R.id.study_time);
        final String courseName =data.get(position).getTitle();
        final String courseImage = data.get(position).getMiddle_pic();
        String studyTime = data.get(position).getLesson_num();
        String price =data.get(position).getPrice();

        if (IsEmpty(price)) {
            freeOrVipImagei.setBackgroundResource(R.drawable.live_free_iv);
        } else {
            freeOrVipImagei.setBackgroundResource(R.drawable.live_vip_iv);
        }
        //搜索关键字变色
        int index1;
        ForegroundColorSpan span1 = new ForegroundColorSpan(context.getResources().getColor(R.color.font_blue));//要显示的颜色
        SpannableStringBuilder builder1 = new SpannableStringBuilder(courseName);
        index1 = courseName.indexOf(keyword);//从第几个匹配上
        if(index1!=-1){//有这个关键字用builder显示
            builder1.setSpan(span1, index1, index1+keyword.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            courseNamet.setText(builder1);
        }else{//没有则直接显示
            courseNamet.setText(courseName);
        }
        studyTimet.setText(studyTime+ context.getResources().getString(R.string.course_hours));
        GlideUtils.loadCourseImg(context, courseImage, courseImagei);

        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VideoViewActivity.class);
                intent.putExtra("flag", Constants.ONLINE);
                intent.putExtra("treeid", data.get(position).getId());
                intent.putExtra("tree_name", data.get(position).getTitle());
                intent.putExtra("pic",  data.get(position).getMiddle_pic());
                intent.putExtra(Constants.IS_CENTER, "0");
                context.startActivity(intent);
            }
        });

        super.initCustomView(holder, data, position);
    }


    public void setKeyword(String keyword){
        this.keyword =keyword;
    }
}

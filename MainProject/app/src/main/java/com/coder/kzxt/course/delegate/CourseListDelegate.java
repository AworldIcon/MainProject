package com.coder.kzxt.course.delegate;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coder.kzxt.activity.R;
import com.coder.kzxt.course.beans.CourseBean;
import com.coder.kzxt.recyclerview.adapter.BaseViewHolder;
import com.coder.kzxt.recyclerview.adapter.PullRefreshDelegate;
import com.coder.kzxt.utils.DateUtil;
import com.coder.kzxt.utils.GlideUtils;

import java.util.List;

/**
 * 课程列表的delegate
 * Created by wangtingshun on 2017/4/6.
 */
public class CourseListDelegate extends PullRefreshDelegate<CourseBean.Course> {

    private Context mContext;
    private String categoryId;

    public CourseListDelegate(Context context, String id) {
        super(R.layout.item_course);
        this.mContext = context;
        this.categoryId = id;
    }


    @Override
    public void initCustomView(BaseViewHolder itemView, List<CourseBean.Course> data, int position) {
        TextView courseName = (TextView) itemView.findViewById(R.id.course_name_text);
        ImageView courseImage = (ImageView) itemView.findViewById(R.id.course_img);
        ImageView freeOrVipImage = (ImageView) itemView.findViewById(R.id.free_or_vip);
        TextView studyTime = (TextView) itemView.findViewById(R.id.study_time);
        LinearLayout course_ly = (LinearLayout) itemView.findViewById(R.id.course_ly);
        LinearLayout studyLayout = itemView.findViewById(R.id.ll_study_time);
        TextView tv_date = itemView.findViewById(R.id.tv_study_date);
        if (!TextUtils.isEmpty(categoryId)) {
            studyLayout.setVisibility(View.GONE);
            studyTime.setVisibility(View.VISIBLE);
        } else {
            studyLayout.setVisibility(View.VISIBLE);
            studyTime.setVisibility(View.GONE);
        }
        final CourseBean.Course course = data.get(position);
        if (IsEmpty(course.getPrice())) {
            freeOrVipImage.setBackgroundResource(R.drawable.live_free_iv);
        } else {
            freeOrVipImage.setBackgroundResource(R.drawable.live_vip_iv);
        }
        courseName.setText(course.getTitle());
        GlideUtils.loadCourseImg(mContext, course.getMiddle_pic(), courseImage);
        String update_time = course.getUpdate_time();
        studyTime.setText(course.getLesson_num()+"课时");
//        String last_learn = course.getLast_learn();
//        if(!TextUtils.isEmpty(last_learn)){
//            tv_date.setText(DateUtil.getYearMonthAndDay(Long.parseLong(last_learn)));
//        } else {
//        }
        tv_date.setText(DateUtil.getYearMonthAndDay(Long.parseLong(update_time)));
    }

    public boolean IsEmpty(String string) {
        if (TextUtils.isEmpty(string) || string.equals("0") || string.equals("0.0") || string.equals("0.00")) {
            return true;
        }
        return false;
    }

}

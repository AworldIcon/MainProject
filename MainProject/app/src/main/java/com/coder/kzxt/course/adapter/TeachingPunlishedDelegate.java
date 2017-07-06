package com.coder.kzxt.course.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.beans.CourseBean;
import com.coder.kzxt.recyclerview.adapter.BaseViewHolder;
import com.coder.kzxt.recyclerview.adapter.PullRefreshDelegate;
import com.coder.kzxt.utils.GlideUtils;
import java.util.List;


public class TeachingPunlishedDelegate extends PullRefreshDelegate<CourseBean> {
    private Context context;
    public TeachingPunlishedDelegate(Context context) {
        super(R.layout.item_course);
        this.context = context;
    }

    @Override
    public void initCustomView(BaseViewHolder holder, List<CourseBean> data, int position) {
        ImageView course_img = (ImageView) holder.findViewById(R.id.course_img);
        TextView course_name_text = (TextView) holder.findViewById(R.id.course_name_text);
        TextView teacher_name = (TextView) holder.findViewById(R.id.teacher_name);
        TextView study_time = (TextView) holder.findViewById(R.id.study_time);
        CourseBean courseBean = data.get(position);
        final String title = courseBean.getTitle();
        final String publicCourse = courseBean.getSource();
        final String courseId = courseBean.getId();
        final String pic = courseBean.getMiddle_pic();
        GlideUtils.loadCourseImg(context,pic,course_img);
        course_name_text.setText(title);

        super.initCustomView(holder, data, position);
    }
}

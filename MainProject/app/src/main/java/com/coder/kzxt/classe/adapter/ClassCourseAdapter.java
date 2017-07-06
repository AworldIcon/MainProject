package com.coder.kzxt.classe.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.classe.beans.ClassCourseResult;
import com.coder.kzxt.utils.GlideUtils;
import java.util.ArrayList;

/**
 * 班级课程适配器
 * Created by wangtingshun on 2017/3/16.
 */

public class ClassCourseAdapter extends RecyclerView.Adapter<ClassCourseAdapter.MyViewHolder> {

    private Context mContext;
    private  ArrayList<ClassCourseResult.ClassCourseBean> courseList;

    public ClassCourseAdapter(Context context,ArrayList<ClassCourseResult.ClassCourseBean> list) {
        this.mContext = context;
        this.courseList = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.class_course_item, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final ClassCourseResult.ClassCourseBean classCourseBean = courseList.get(position);
        holder.courseName.setText(classCourseBean.getTitle());
        holder.studyTime.setText(classCourseBean.getTimeLength());
        holder.teacher.setText(mContext.getResources().getString(R.string.lecturer)+classCourseBean.getTeacher());
        GlideUtils.loadCourseImg(mContext,classCourseBean.getPic(),holder.courseImage);
        if(onItemClickListener != null){
            holder.ll_course.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClick(classCourseBean);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        //加载图
        private ImageView courseImage;
        //课程名
        private TextView courseName;
        //讲师
        private TextView teacher;
        //学习时长
        private TextView studyTime;
        private LinearLayout ll_course;

        public MyViewHolder(View itemView) {
            super(itemView);
            ll_course = (LinearLayout) itemView.findViewById(R.id.ll_course);
            courseName = (TextView) itemView.findViewById(R.id.course_name_text);
            courseImage = (ImageView) itemView.findViewById(R.id.course_img);
            teacher = (TextView) itemView.findViewById(R.id.teacher);
            studyTime = (TextView) itemView.findViewById(R.id.study_time);
        }
    }

    private OnItemClickInterface onItemClickListener;

    public interface OnItemClickInterface{
        void onItemClick(ClassCourseResult.ClassCourseBean courseBean);
    }

    public void setOnItemClickListener(OnItemClickInterface listener){
        this.onItemClickListener = listener;
    }

}

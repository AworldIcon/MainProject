package com.coder.kzxt.order.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.coder.kzxt.activity.R;
import com.coder.kzxt.order.beans.CourseClass;
import com.coder.kzxt.order.beans.MyOrderBean;
import com.coder.kzxt.utils.GlideUtils;
import com.coder.kzxt.utils.Utils;

import java.util.ArrayList;

/**
 * 订单详情适配器
 * Created by wangtingshun on 2017/4/12.
 */

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<MyOrderBean.OrderBean> courseList;
    private ArrayList<CourseClass.ClassBean> classList;
    private CourseClass.ClassBean classBean;
    private String classId;//班级id
    private boolean isDefault = false;

    public OrderDetailAdapter(Context context, ArrayList<MyOrderBean.OrderBean> course,String classId) {
        this.mContext = context;
        this.courseList = course;
        this.classId = classId;
    }

    public void setOrderDetailData(ArrayList<CourseClass.ClassBean> bean,String classId) {
        this.classList = bean;
        this.classId = classId;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_order_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (courseList != null && courseList.size() > 0) {
            MyOrderBean.OrderBean course = courseList.get(position);
            holder.courseName.setText(course.getCourse().getTitle());
            holder.courseTime.setText(course.getCourse().getLesson_num());   //课时待定
            Utils.setPrice(holder.coursePrice, course.getCourse_class().getPrice());
            GlideUtils.loadCourseImg(mContext, course.getCourse().getMiddle_pic(), holder.courseImg);
        }

        if (classList != null && classList.size() > 0) {
            holder.classLayout.setVisibility(View.VISIBLE);
            holder.arrowImg.setVisibility(View.GONE);
            for (int i = 0; i < classList.size(); i++) {
                classBean = classList.get(i);
                if (!TextUtils.isEmpty(classId)) {
                    if (classBean.getId().equals(classId)) {
                        holder.defaultClass.setText(classBean.getClass_name());
                    }
                } else {
                    if (classBean.getIs_default().equals("1")) {
                        isDefault = true;
                        holder.defaultClass.setText(classBean.getClass_name());
                    }
                    if (!isDefault) {
                        holder.defaultClass.setText(classBean.getClass_name());
                    }
                }
            }
        } else {
            holder.classLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView courseImg;
        private TextView courseName;
        private TextView courseTime;
        private TextView coursePrice;
        private TextView defaultClass;
        private RelativeLayout classLayout;
        private ImageView arrowImg;

        public MyViewHolder(View itemView) {
            super(itemView);
            courseImg = (ImageView) itemView.findViewById(R.id.iv_course);
            courseName = (TextView) itemView.findViewById(R.id.tv_course_name);
            courseTime = (TextView) itemView.findViewById(R.id.tv_course_time);
            coursePrice = (TextView) itemView.findViewById(R.id.tv_course_price);
            defaultClass = (TextView) itemView.findViewById(R.id.tv_class_name);
            classLayout = (RelativeLayout) itemView.findViewById(R.id.rl_class_layout);
            arrowImg = (ImageView) itemView.findViewById(R.id.iv_arrow);
        }

    }

}

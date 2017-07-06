package com.coder.kzxt.order.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.coder.kzxt.activity.R;
import com.coder.kzxt.order.beans.CourseClass;
import com.coder.kzxt.order.beans.SingleCourse;
import com.coder.kzxt.order.mInterface.OnItemClickInterface;
import com.coder.kzxt.utils.GlideUtils;
import com.coder.kzxt.utils.Utils;

import java.util.ArrayList;


/**
 * 订单（课程）适配器
 * Created by wangtingshun on 2017/4/10.
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<SingleCourse.Course> courseList;
    private ArrayList<CourseClass.ClassBean> classList;
    private CourseClass.ClassBean classBean;
    private boolean isDefault = false;
    private String scanClassId;

    public OrderAdapter(Context context, ArrayList<SingleCourse.Course> course,String classId) {
        this.mContext = context;
        this.courseList = course;
        this.scanClassId = classId;
    }

    public void setOrderData(ArrayList<CourseClass.ClassBean> bean){
        this.classList = bean;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_order_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (courseList != null && courseList.size() > 0) {
            SingleCourse.Course course = courseList.get(position);
            holder.courseName.setText(course.getTitle());
            holder.courseTime.setText(course.getExpiry_day());
//            Utils.setPrice( holder.coursePrice,course.getPrice());
            GlideUtils.loadCourseImg(mContext, course.getMiddle_pic(), holder.courseImg);
        }

        if (classList != null && classList.size() > 0) {
            holder.classLayout.setVisibility(View.VISIBLE);
            if (classList.size() == 1) {
                holder.arrowImg.setVisibility(View.GONE);
            } else if(classList.size() == 0){
                holder.classLayout.setVisibility(View.GONE);
            }

            for (int i = 0; i < classList.size(); i++) {
                classBean = classList.get(i);
                if (!TextUtils.isEmpty(scanClassId)) { //扫码加班
                    if (classBean.getId().equals(scanClassId)) {
                        holder.arrowImg.setVisibility(View.GONE);
                        holder.defaultClass.setText(classBean.getClass_name());
                        holder.classLayout.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View view, MotionEvent motionEvent) {
                                return true;
                            }
                        });
                    }
                } else {  //其他
                    if (classBean.getIs_default().equals("1")) {
                        isDefault = true;
                        holder.defaultClass.setText(classBean.getClass_name());
                        Utils.setPrice(holder.coursePrice, classBean.getPrice()); //课程跟随班级走
                    }
                    if (!isDefault) {  //没有默认班的情况
                        holder.defaultClass.setText(classBean.getClass_name());
                        Utils.setPrice(holder.coursePrice, classBean.getPrice());
                    }
                }
            }
        } else {
            holder.classLayout.setVisibility(View.GONE);
        }

        if (listener != null) {
            holder.classLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClassItemClick(classBean);
                }
            });
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

    private OnItemClickInterface listener;

    public void setOnClassItemClickListener(OnItemClickInterface onItemClickListener){
        this.listener = onItemClickListener;
    }

}

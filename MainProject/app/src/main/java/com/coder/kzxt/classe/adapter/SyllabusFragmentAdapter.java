package com.coder.kzxt.classe.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.classe.beans.CourseTableResult;
import com.coder.kzxt.utils.DateUtil;
import java.util.ArrayList;

/**
 * 课程表fragment适配器
 * Created by Administrator on 2017/3/16.
 */

public class SyllabusFragmentAdapter extends RecyclerView.Adapter<SyllabusFragmentAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<CourseTableResult.DayBean.Day> dayList;

    public SyllabusFragmentAdapter(Context context,CourseTableResult.DayBean bean) {
        this.mContext = context;
        this.dayList =bean.getList();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(mContext).inflate(R.layout.class_syllabus_item,null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        CourseTableResult.DayBean.Day day = dayList.get(position);
        holder.number.setText(String.valueOf(position + 1));
        holder.class_name.setText(day.getCourseName());
        long startTime = Long.parseLong(day.getStartTime());
        long endTime = Long.parseLong(day.getEndTime());
        holder.start_time.setText(DateUtil.getDateTime(startTime));
        holder.end_time.setText(DateUtil.getDateTime(endTime));
        holder.class_location.setText(day.getClassroom());
    }

    @Override
    public int getItemCount() {
        return dayList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        /**课程名*/
        private TextView class_name;
        /**上课时间*/
        private TextView start_time;
        /**下课时间*/
        private TextView end_time;
        /**教室位置*/
        private TextView class_location;
        /**序号*/
        private TextView number;

        public MyViewHolder(View itemView) {
            super(itemView);
            number = (TextView) itemView.findViewById(R.id.tv_number);
            class_name = (TextView) itemView.findViewById(R.id.tv_book_name);
            class_location = (TextView) itemView.findViewById(R.id.tv_class_location);
            start_time = (TextView) itemView.findViewById(R.id.tv_start_time);
            end_time = (TextView) itemView.findViewById(R.id.tv_end_time);
        }
    }
}

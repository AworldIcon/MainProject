package com.coder.kzxt.course.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coder.kzxt.activity.R;
import com.coder.kzxt.course.beans.TeachBean;
import com.coder.kzxt.course.mInterface.OnTeachItemInterface;
import com.coder.kzxt.utils.GlideUtils;

import java.util.ArrayList;

import static com.coder.kzxt.utils.PublicUtils.IsEmpty;

/**
 * Created by Administrator on 2017/5/4.
 */

public class TeachBottomAdapter extends RecyclerView.Adapter<TeachBottomAdapter.MyViewHolder> {


    private Context mContext;
    private ArrayList<TeachBean.TeachCourseItem> courseItems;

    public TeachBottomAdapter(Context context, ArrayList<TeachBean.TeachCourseItem> datas){
            this.mContext = context;
            this.courseItems = datas;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.order_course_top_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final TeachBean.TeachCourseItem item = courseItems.get(position);
        holder.courseName.setText(item.getTitle());
        holder.courseTime.setText(item.getLesson_num());
        holder.ll_member.setVisibility(View.VISIBLE);
        holder.sutdentNum.setText(item.getStudent_num());
        GlideUtils.loadCourseImg(mContext,item.getMiddle_pic(),holder.courseImg);

        if (IsEmpty(item.getPrice())) {
            holder.freeOrVipImage.setBackgroundResource(R.drawable.live_free_iv);
        } else {
            holder.freeOrVipImage.setBackgroundResource(R.drawable.live_vip_iv);
        }
        holder.coursePrice.setVisibility(View.GONE);
        if(listener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.OnTeachItemClick(item);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return courseItems.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView courseName;//课程名称
        private TextView courseTime; //课程时间
        private TextView sutdentNum; //学生数量
        private ImageView courseImg; //图片
        private ImageView freeOrVipImage; //免费图片
        private TextView coursePrice; //课程价格
        private LinearLayout ll_member; //成员

       public MyViewHolder(View itemView) {
           super(itemView);
           courseName = (TextView) itemView.findViewById(R.id.tv_course_name);
           courseTime = (TextView) itemView.findViewById(R.id.tv_course_time);
           sutdentNum = (TextView) itemView.findViewById(R.id.tv_num);
           ll_member = (LinearLayout) itemView.findViewById(R.id.ll_member);
           courseImg = (ImageView) itemView.findViewById(R.id.iv_course);
           freeOrVipImage = (ImageView) itemView.findViewById(R.id.free_or_vip);
           coursePrice = (TextView) itemView.findViewById(R.id.tv_course_price);
       }
   }

    public OnTeachItemInterface listener;

    public void setOnTeachItemClickListener(OnTeachItemInterface mListener){
        this.listener = mListener;
    }

}

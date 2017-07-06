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
import com.coder.kzxt.classe.beans.ClassListBean;
import com.coder.kzxt.utils.GlideUtils;

import java.util.ArrayList;

/**
 * 班级列表适配器
 * Created by wangtingshun on 2017/3/11.
 */

public class ClassListAdapter extends RecyclerView.Adapter<ClassListAdapter.MyViewHolder> {

    private Context mContext;
    //班级列表数据
    private ArrayList<ClassListBean.ClassBean> classList;


    public ClassListAdapter(Context mContext,ArrayList<ClassListBean.ClassBean> list) {
        this.mContext = mContext;
        this.classList = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_class_teacher_center, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
       final ClassListBean.ClassBean classBean = classList.get(position);
        holder.class_name.setText(classBean.getClassName());
        holder.class_status.setVisibility(View.GONE);
        holder.class_teacher.setText(mContext.getResources().getString(R.string.create_user)+" " + classBean.getCreateName());
        holder.class_learner_number.setText(classBean.getMemberNum()+"人");
        GlideUtils.loadClassImg(mContext,classBean.getLogo(),holder.class_img);
        if(onItemClickListener != null){
            holder.ll_class_detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClick(classBean);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return classList.size();
    }


    static class MyViewHolder extends RecyclerView.ViewHolder{

        private ImageView class_img;
        private TextView class_name;
        private TextView class_status;
        private TextView class_teacher;
        private TextView class_learner_number;
        private LinearLayout ll_class_detail;

        public MyViewHolder(View itemView) {
            super(itemView);
            class_img = (ImageView) itemView.findViewById(R.id.class_img);
            class_name = (TextView) itemView.findViewById(R.id.class_name);
            class_status = (TextView) itemView.findViewById(R.id.class_status);
            class_teacher = (TextView) itemView.findViewById(R.id.class_teacher);
            ll_class_detail = (LinearLayout) itemView.findViewById(R.id.ll_class_detail);
            class_learner_number = (TextView) itemView.findViewById(R.id.class_learner_number);
        }

    }

    private OnItemClickInterface onItemClickListener;

    public interface OnItemClickInterface{
        void onItemClick(ClassListBean.ClassBean classBean);
    }

    public void setOnItemClickListener(OnItemClickInterface listener){
        this.onItemClickListener = listener;
    }

}

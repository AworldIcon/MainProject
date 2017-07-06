package com.coder.kzxt.order.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.order.beans.CourseClass;
import com.coder.kzxt.order.mInterface.OnItemClickInterface;

import java.util.ArrayList;

/**
 * 对话框内的适配器
 * Created by wangtingshun on 2017/4/12.
 */
public class CustomDialogAdapter extends RecyclerView.Adapter<CustomDialogAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<CourseClass.ClassBean> classList;

    public CustomDialogAdapter(Context mContext, ArrayList<CourseClass.ClassBean> classList) {
        this.mContext = mContext;
        this.classList = classList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.order_dialog_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final CourseClass.ClassBean classBean = classList.get(position);
        holder.className.setText(classBean.getClass_name());
        if(classBean.getIs_default().equals("1")){
            holder.selectImg.setVisibility(View.VISIBLE);
        } else {
            holder.selectImg.setVisibility(View.GONE);
        }

        holder.classLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null){
                    listener.onInnerClassItemClick(classBean);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return classList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView className; //班级名称
        private ImageView selectImg;  //勾选
        private RelativeLayout classLayout; //选择班级layout

        public MyViewHolder(View itemView) {
            super(itemView);
            className = (TextView) itemView.findViewById(R.id.class_name);
            selectImg = (ImageView) itemView.findViewById(R.id.iv_select);
            classLayout = (RelativeLayout) itemView.findViewById(R.id.rl_class_layout);
        }
    }

    private OnItemClickInterface listener;

    public void setOnClassItemClickListener(OnItemClickInterface onItemClickListener){
        this.listener = onItemClickListener;
    }

}

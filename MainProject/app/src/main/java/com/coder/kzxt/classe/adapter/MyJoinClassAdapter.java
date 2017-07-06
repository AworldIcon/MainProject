package com.coder.kzxt.classe.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.classe.beans.MyCreateClass;
import com.coder.kzxt.classe.mInterface.OnClassItemInterface;
import com.coder.kzxt.utils.GlideUtils;
import java.util.ArrayList;

public class MyJoinClassAdapter extends RecyclerView.Adapter<MyJoinClassAdapter.MyViewHolder> {


    private Context mContext;
    private ArrayList<MyCreateClass.CreateClassBean> classItems;

    public MyJoinClassAdapter(Context context, ArrayList<MyCreateClass.CreateClassBean> datas) {
        this.mContext = context;
        this.classItems = datas;
    }

    public void setJoinClassAdapter(ArrayList<MyCreateClass.CreateClassBean> datas){
        this.classItems = datas;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.my_class_item_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
       final MyCreateClass.CreateClassBean classBean = classItems.get(position);
        holder.className.setText(classBean.getName());
//        holder.classPerson.setVisibility(View.GONE);
        GlideUtils.loadHeaderOfClass(mContext, classBean.getLogo(), holder.classImg);
        if (listener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClassItemClick(classBean);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return classItems.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView className;//班级名称

        private ImageView classImg; //图片

//        private TextView classPerson; //班级人

        public MyViewHolder(View itemView) {
            super(itemView);
            classImg = (ImageView) itemView.findViewById(R.id.class_icon);
            className = (TextView) itemView.findViewById(R.id.tv_class_name);
//            classPerson = (TextView) itemView.findViewById(R.id.tv_class_person);
        }
    }

    public OnClassItemInterface listener;

    public void setOnClassItemClickListener(OnClassItemInterface mListener) {
        this.listener = mListener;
    }
}
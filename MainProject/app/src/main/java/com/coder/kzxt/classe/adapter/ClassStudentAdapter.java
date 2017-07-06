package com.coder.kzxt.classe.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.coder.kzxt.activity.R;

/**
 * Created by wangtingshun on 2017/6/6.
 *
 */

public class ClassStudentAdapter extends RecyclerView.Adapter<ClassStudentAdapter.MyViewHolder> {


    private Context mContext;


    public ClassStudentAdapter(Context context){
        this.mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView className;//班级名称

        private ImageView classImg; //图片


        public MyViewHolder(View itemView) {
            super(itemView);
            classImg = (ImageView) itemView.findViewById(R.id.class_icon);
            className = (TextView) itemView.findViewById(R.id.tv_class_name);
        }
    }
}

package com.coder.kzxt.main.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.main.beans.MyBean;
import com.coder.kzxt.main.mInterface.OnMyItemInterface;

import java.util.ArrayList;

/**
 * 我的页面上部适配器
 * Created by wangtingshun on 2017/3/30.
 */
public class MyTopAdapter extends RecyclerView.Adapter<MyTopAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<MyBean.ChildItemBean> topData;

    public MyTopAdapter(Context context, ArrayList<MyBean.ChildItemBean> data) {
        this.mContext = context;
        this.topData = data;
    }

    public void setTopItemData(ArrayList<MyBean.ChildItemBean> data){
        this.topData = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.my_top_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final MyBean.ChildItemBean childItem = topData.get(position);
        holder.tv_top_view.setText(childItem.getTitle());
//        GlideUtils.loadMyImage(mContext,childItem.getUrl(),holder.iv_top_view);

        Glide.with(mContext).load(childItem.getUrl()).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                if (resource != null) {
                    holder.iv_top_view.setImageBitmap(resource);
                  } else {

                  }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null){
                    onItemClickListener.onMyTopItemClick(childItem);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return topData == null ? 0 : topData.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        private ImageView iv_top_view;
        private TextView tv_top_view;

        public MyViewHolder(View itemView) {
            super(itemView);
            iv_top_view = (ImageView) itemView.findViewById(R.id.iv_top);
            tv_top_view = (TextView) itemView.findViewById(R.id.tv_top);
        }
    }

    public OnMyItemInterface onItemClickListener;

    public void setTopItemClickListener(OnMyItemInterface listener){
        this.onItemClickListener = listener;
    }
}

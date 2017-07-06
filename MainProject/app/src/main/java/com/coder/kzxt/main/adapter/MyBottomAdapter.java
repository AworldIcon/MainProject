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
 * 我的页面底部适配器
 * Created by wangtingshun on 2017/3/30.
 */
public class MyBottomAdapter extends RecyclerView.Adapter<MyBottomAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<MyBean.ChildItemBean> bottomData;

    public MyBottomAdapter(Context context, ArrayList<MyBean.ChildItemBean> data) {
        this.mContext = context;
        this.bottomData = data;
    }

    public void setBottomItemData(ArrayList<MyBean.ChildItemBean> data){
        if(bottomData != null){
            bottomData.clear();
        }
        this.bottomData = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.my_bottom_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final MyBean.ChildItemBean childItem = bottomData.get(position);
        holder.tv_bottom_item.setText(childItem.getTitle());
//        GlideUtils.loadMyImage(mContext,childItem.getUrl(),holder.iv_bottom_item);

        Glide.with(mContext).load(childItem.getUrl()).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                if (resource != null) {
                    holder.iv_bottom_item.setImageBitmap(resource);
                } else {
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onItemClickListener != null){
                    onItemClickListener.onMyBottomItemClick(childItem);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return bottomData == null ? 0 : bottomData.size();
    }


    static class MyViewHolder extends RecyclerView.ViewHolder{

        private ImageView iv_bottom_item;
        private TextView tv_bottom_item;

        public MyViewHolder(View itemView) {
            super(itemView);
            iv_bottom_item = (ImageView) itemView.findViewById(R.id.iv_bottom_item);
            tv_bottom_item = (TextView) itemView.findViewById(R.id.tv_bottom_item);
        }
    }

    public OnMyItemInterface onItemClickListener;

    public void setBottomItemClickListener(OnMyItemInterface listener){
        this.onItemClickListener = listener;
    }

}

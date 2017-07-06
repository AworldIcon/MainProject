package com.coder.kzxt.poster.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.coder.kzxt.activity.R;
import com.coder.kzxt.poster.activity.PublishImagePosterActivity;
import com.coder.kzxt.poster.activity.PublishTextPosterActivity;
import com.coder.kzxt.poster.beans.PosterCategory;
import com.coder.kzxt.recyclerview.MyRecyclerView;
import com.coder.kzxt.recyclerview.adapter.BaseRecyclerAdapter;
import com.coder.kzxt.recyclerview.adapter.BaseViewHolder;
import com.coder.kzxt.views.RecyclingPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class PosterClassifyPageAdapter extends RecyclingPagerAdapter {

    private Context context;
    private ArrayList<ArrayList<PosterCategory.ItemsBean>> classifyList;
    private int size;
    private boolean isInfiniteLoop;

    public PosterClassifyPageAdapter(Context context, ArrayList<ArrayList<PosterCategory.ItemsBean>> classifyList) {
        this.context = context;
        this.classifyList = classifyList;
        this.size = classifyList.size();
        isInfiniteLoop = false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup container) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_poster_pulish, null);
            holder.child_gridview = (MyRecyclerView) convertView.findViewById(R.id.child_gridview);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        BaseRecyclerAdapter childAdapter = new BaseRecyclerAdapter(context, classifyList.get(position), new ChildDalegate(context));
        holder.child_gridview.setAdapter(childAdapter);

        return convertView;
    }


    private  class ChildDalegate extends com.coder.kzxt.recyclerview.adapter.BaseDelegate<PosterCategory.ItemsBean> {
        private Context context;

        public ChildDalegate(Context context) {
            super(R.layout.item_poster_create_gridview);
            this.context = context;
        }

        @Override
        public void initCustomView(BaseViewHolder convertView, final List<PosterCategory.ItemsBean> arrayList, final int position) {

            TextView classify_tx = (TextView) convertView.findViewById(R.id.classify_tx);
            final PosterCategory.ItemsBean itemsBean = arrayList.get(position);
            final String id = itemsBean.getId() + "";
            String name = itemsBean.getName();
            int isSelectd = itemsBean.getIsSelectd();
            classify_tx.setText(name);

            GradientDrawable myGrad = (GradientDrawable) classify_tx.getBackground();

            if (isSelectd == 1) {
                myGrad.setStroke(1, context.getResources().getColor(R.color.first_theme));// 设置边框宽度与颜色
                myGrad.setColor(context.getResources().getColor(R.color.first_theme));// 设置内部颜色
                classify_tx.setTextColor(context.getResources().getColor(R.color.white));// 设置字体颜色
            } else {
                myGrad.setStroke(1, context.getResources().getColor(R.color.font_gray));// 设置边框宽度与颜色
                myGrad.setColor(context.getResources().getColor(R.color.transparent));// 设置内部颜色
                classify_tx.setTextColor(context.getResources().getColor(R.color.font_gray));// 设置字体颜色
            }

            classify_tx.setOnClickListener(new OnClickListener() {

                public void onClick(View v) {
                    for (int i = 0; i <classifyList.size() ; i++) {
                        ArrayList<PosterCategory.ItemsBean> list = classifyList.get(i);
                        for (int j = 0; j <list.size() ; j++) {
                            list.get(j).setIsSelectd(0);
                        }
                    }
                    itemsBean.setIsSelectd(1);
                    if (context instanceof PublishImagePosterActivity) {
                        ((PublishImagePosterActivity) context).setCid(id);

                    } else {
                        ((PublishTextPosterActivity) context).setCid(id);
                    }
                    notifyDataSetChanged();
                }
            });

        }
    }


    @Override
    public int getCount() {
        return isInfiniteLoop ? Integer.MAX_VALUE : classifyList.size();
    }

    /**
     * get really position
     *
     * @param position
     * @return
     */
    private int getPosition(int position) {
        return isInfiniteLoop ? position % size : position;
    }

    private static class ViewHolder {
        MyRecyclerView child_gridview;
    }

    /**
     * @return the isInfiniteLoop
     */
    public boolean isInfiniteLoop() {
        return isInfiniteLoop;
    }

    /**
     * @param isInfiniteLoop the isInfiniteLoop to set
     */
    public PosterClassifyPageAdapter setInfiniteLoop(boolean isInfiniteLoop) {
        this.isInfiniteLoop = isInfiniteLoop;
        return this;
    }

}

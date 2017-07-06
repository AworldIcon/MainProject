package com.coder.kzxt.video.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.coder.kzxt.activity.R;
import com.coder.kzxt.video.beans.CatalogueBean;

import java.util.List;

/**
 * Created by tcy on 2017/3/17.
 */

public class SelectsAdapter extends BaseAdapter{

    private Context context;
    private List<CatalogueBean.ItemsBean.VideoBean> list;
    private int isJoin;

    public SelectsAdapter(Context context, List<CatalogueBean.ItemsBean.VideoBean> list,int isJoin) {
        this.context = context;
        this.list = list;
        this.isJoin = isJoin;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.select_video_item, null);
        }
        TextView select_text = (TextView) convertView.findViewById(R.id.select_text);
        ImageView select_img = (ImageView) convertView.findViewById(R.id.select_img);
        ImageView inevitable_iv = (ImageView) convertView.findViewById(R.id.inevitable_iv);
        TextView isPublic = (TextView) convertView.findViewById(R.id.isPublic);
        CatalogueBean.ItemsBean.VideoBean videoBean= list.get(position);

        String type = videoBean.getType();
        String title = videoBean.getTitle();
        String free = videoBean.getFree();
        String is_see = videoBean.getIs_see();
        String last_location =  videoBean.getLast_location();
        int showLock = videoBean.getIsShowLock();

        if(free!=null){
            if (free.equals("1") || isJoin != 0) {
                isPublic.setVisibility(View.GONE);
            } else {
                isPublic.setVisibility(View.VISIBLE);
            }
        }

        if (isJoin != 0){
            if (isJoin != 2 && isJoin != 3) {
                if(showLock==1){
                    inevitable_iv.setVisibility(View.VISIBLE);
                }else {
                    inevitable_iv.setVisibility(View.GONE);
                }
            }else {
                //               "is_see": "是否必看： 0否 1是"
                if (is_see.equals("1")) {
                    inevitable_iv.setVisibility(View.VISIBLE);
                } else {
                    inevitable_iv.setVisibility(View.GONE);
                }
            }

        }

        if(type!=null){

        if (type.equals("1")) {// 视频
            if (last_location.equals("1")) {
                select_text.setTextColor(context.getResources().getColor(R.color.first_theme));
                select_img.setBackgroundResource(R.drawable.study_finsh_img_checked);

                isPublic.setTextColor(context.getResources().getColor(R.color.first_theme));
                GradientDrawable myGrad = (GradientDrawable) isPublic.getBackground();
                myGrad.setStroke(1, context.getResources().getColor(R.color.first_theme));// 设置边框宽度与颜色
                isPublic.setPadding(10, 4, 10, 4);

            } else {
                select_text.setTextColor(context.getResources().getColor(R.color.font_white));
                select_img.setBackgroundResource(R.drawable.study_finsh_img);

                isPublic.setTextColor(context.getResources().getColor(R.color.font_gray));
                GradientDrawable myGrad = (GradientDrawable)isPublic.getBackground();
                myGrad.setStroke(1, context.getResources().getColor(R.color.font_gray));// 设置边框宽度与颜色
                isPublic.setPadding(10, 4, 10, 4);
            }

        } else if (type.equals("3")) {// 图文
            select_img.setBackgroundResource(R.drawable.video_document);
        } else if (type.equals("2")) {// pdf
            select_img.setBackgroundResource(R.drawable.video_document);
        } else if (type.equals("practice")) {// 练习
            select_img.setBackgroundResource(R.drawable.video_task);
        } else if (type.equals("unity3d")) {//unity3d
            select_img.setBackgroundResource(R.drawable.video_unity);
        } else if (type.equals("4")) {//url
            select_img.setBackgroundResource(R.drawable.video_url);
        }

    }
        select_text.setText(title);



        return convertView;
    }
}

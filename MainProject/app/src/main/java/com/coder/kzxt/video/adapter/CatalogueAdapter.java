package com.coder.kzxt.video.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.coder.kzxt.activity.R;
import com.coder.kzxt.utils.DateUtil;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.coder.kzxt.video.beans.CatalogueBean;

import java.util.List;


/**
 * Created by Administrator on 2017/3/13.
 */

public class CatalogueAdapter extends BaseExpandableListAdapter {

    private Context context;
    private SharedPreferencesUtil spu;
    private List<CatalogueBean.ItemsBean> Headlist;
    private int isJoin;

    public CatalogueAdapter(Context context, List<CatalogueBean.ItemsBean> headlist, int isJoin) {
        this.context = context;
        Headlist = headlist;
        this.isJoin = isJoin;
        spu = new SharedPreferencesUtil(context);
    }

    @Override
    public int getGroupCount() {
        if (Headlist == null) {
            return 0;
        }
        return Headlist.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return Headlist.get(groupPosition).getVideo().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return Headlist.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return Headlist.get(groupPosition).getVideo().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        ViewHeadHolder holder;
        if (convertView == null) {
            holder = new ViewHeadHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.catalogue_header, null);
            holder.course_list_group_text = (TextView) convertView.findViewById(R.id.course_list_group_text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHeadHolder) convertView.getTag();
        }
        holder.course_list_group_text.setText(Headlist.get(groupPosition).getTitle());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        ViewItemHolder holder;
        if (convertView == null) {
            holder = new ViewItemHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.catalogue_item, null);
            holder.catalogue_content_title = (TextView) convertView.findViewById(R.id.catalogue_content_title);
            holder.video_all_time = (TextView) convertView.findViewById(R.id.video_all_time);
            holder.catalogue_content_layout = (RelativeLayout) convertView.findViewById(R.id.catalogue_content_layout);
            holder.video_time_layout = (RelativeLayout) convertView.findViewById(R.id.video_time_layout);
            holder.exercise_tag = (TextView) convertView.findViewById(R.id.exercise_tag);
            holder.isPublic = (TextView) convertView.findViewById(R.id.isPublic);
            holder.isCoinExchange = (TextView) convertView.findViewById(R.id.isCoinExchange);
            holder.catalogue_content_img = (ImageView) convertView.findViewById(R.id.catalogue_content_img);
            holder.inevitable_iv = (ImageView) convertView.findViewById(R.id.inevitable_iv);
            convertView.setTag(holder);
        } else {
            holder = (ViewItemHolder) convertView.getTag();
        }

        CatalogueBean.ItemsBean.VideoBean videoBean = Headlist.get(groupPosition).getVideo().get(childPosition);
//        String id = videoBean.getId();//视频id
//        String tid =  Headlist.get(groupPosition).getId();//章节id
//        String randid =  Headlist.get(groupPosition).getSequence();
//        String sort = videoBean.getSequence();
//        String taskname = Headlist.get(groupPosition).getTitle();//章节名称
        String name = videoBean.getTitle();//视频名称
//        String url_content = videoBean.getResource_url();//视频地址
        String free = videoBean.getFree();//是否免费
//        String isGoldBuy = videoBean.getIsGoldBuy();// 课时的兑换(0为未兑换,1为已兑换)
//        String supportGoldBuy = videoBean.getSupportGoldBuy();// 是否支持够购买
//        String needGoldNum = videoBean.getGold_num();// 购买所需金币
        String factDuration = videoBean.getTime_length();// 每个视频的总时间
        String type = videoBean.getType();//类型
        String last_location = videoBean.getLast_location();//起始位置
        String is_see = videoBean.getIs_see();//是否必看
        int showLock = videoBean.getIsShowLock();//是否显示锁

        holder.catalogue_content_title.setText(name);

        if (free.equals("1") || isJoin != 0) {
            holder.isPublic.setVisibility(View.GONE);
        } else {
            holder.isPublic.setVisibility(View.VISIBLE);
        }

        if (isJoin != 0) {

            if (isJoin != 2 && isJoin != 3) {
                if (showLock == 0) {
                    holder.inevitable_iv.setVisibility(View.GONE);
                } else {
                    holder.inevitable_iv.setVisibility(View.VISIBLE);
                }
            } else {
//               "is_see": "是否必看： 0否 1是"
                if (is_see.equals("1")) {
                    holder.inevitable_iv.setVisibility(View.VISIBLE);
                } else {
                    holder.inevitable_iv.setVisibility(View.GONE);
                }

            }

        }

//        if(!TextUtils.isEmpty(spu.getIsLogin())){
//            if(is_see.equals("1")&&isJoin){
//                holder.inevitable_iv.setVisibility(View.VISIBLE);
//            }else {
//                holder.inevitable_iv.setVisibility(View.GONE);
//            }
//        }

        if (type != null) {

            if (type.equals("1")) {// 视频
                if (last_location.equals("1")) {
                    holder.catalogue_content_img.setImageResource(R.drawable.study_finsh_img_checked);
                } else {
                    holder.catalogue_content_img.setImageResource(R.drawable.study_finsh_img);
                }

                holder.video_time_layout.setVisibility(View.VISIBLE);
                holder.exercise_tag.setVisibility(View.GONE);
                holder.video_all_time.setVisibility(View.VISIBLE);
                holder.video_all_time.setText(DateUtil.formatSecond(Integer.parseInt(factDuration)));

            } else if (type.equals("3")) {// 图文
                if (last_location.equals("1")) {
                    holder.catalogue_content_img.setImageResource(R.drawable.video_document_checked);
                } else {
                    holder.catalogue_content_img.setImageResource(R.drawable.video_document);
                }

                holder.video_time_layout.setVisibility(View.GONE);
                holder.exercise_tag.setVisibility(View.GONE);

            } else if (type.equals("2")) {// pdf
                if (last_location.equals("1")) {
                    holder.catalogue_content_img.setImageResource(R.drawable.video_pdf_checked);
                } else {
                    holder.catalogue_content_img.setImageResource(R.drawable.video_pdf);
                }

                holder.video_time_layout.setVisibility(View.GONE);
                holder.exercise_tag.setVisibility(View.GONE);

            } else if (type.equals("practice")) { // 练习
                if (last_location.equals("1")) {
                    holder.catalogue_content_img.setImageResource(R.drawable.video_task_checked);
                } else {
                    holder.catalogue_content_img.setImageResource(R.drawable.video_task);
                }

                holder.video_time_layout.setVisibility(View.GONE);
                holder.exercise_tag.setVisibility(View.VISIBLE);

            } else if (type.equals("unity3d")) {
                if (last_location.equals("1")) {
                    holder.catalogue_content_img.setImageResource(R.drawable.video_unity_checked);
                } else {
                    holder.catalogue_content_img.setImageResource(R.drawable.video_unity);
                }

                holder.video_time_layout.setVisibility(View.GONE);
                holder.exercise_tag.setVisibility(View.GONE);

            } else if (type.equals("4")) {
                if (last_location.equals("1")) {
                    holder.catalogue_content_img.setImageResource(R.drawable.video_url_checked);
                } else {
                    holder.catalogue_content_img.setImageResource(R.drawable.video_url);
                }

                holder.video_time_layout.setVisibility(View.GONE);
                holder.exercise_tag.setVisibility(View.GONE);
            } else if (type.equals("6")) {
                if (last_location.equals("1")) {
                    holder.catalogue_content_img.setImageResource(R.drawable.vr_img_checked);
                } else {
                    holder.catalogue_content_img.setImageResource(R.drawable.vr_img);
                }
                holder.video_time_layout.setVisibility(View.VISIBLE);
                holder.exercise_tag.setVisibility(View.GONE);
                holder.video_all_time.setVisibility(View.VISIBLE);
                holder.video_all_time.setText(DateUtil.formatSecond(Integer.parseInt(factDuration)));

            }
        }
        if (last_location.equals("1")) {
            holder.catalogue_content_title.setTextColor(context.getResources().getColor(R.color.first_theme));
            holder.video_all_time.setTextColor(context.getResources().getColor(R.color.first_theme));
            holder.isPublic.setTextColor(context.getResources().getColor(R.color.first_theme));
            GradientDrawable myGrad = (GradientDrawable) holder.isPublic.getBackground();
            myGrad.setStroke(1, context.getResources().getColor(R.color.first_theme));// 设置边框宽度与颜色
            holder.isPublic.setPadding(10, 4, 10, 4);
        } else {
            holder.catalogue_content_title.setTextColor(context.getResources().getColor(R.color.font_black));
            holder.video_all_time.setTextColor(context.getResources().getColor(R.color.font_gray));
            holder.isPublic.setTextColor(context.getResources().getColor(R.color.font_gray));
            GradientDrawable myGrad = (GradientDrawable) holder.isPublic.getBackground();
            myGrad.setStroke(1, context.getResources().getColor(R.color.font_gray));// 设置边框宽度与颜色
            holder.isPublic.setPadding(10, 4, 10, 4);

        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public class ViewHeadHolder {
        TextView course_list_group_text;

    }

    public class ViewItemHolder {
        TextView catalogue_content_title;
        RelativeLayout catalogue_content_layout;
        RelativeLayout video_time_layout;
        TextView exercise_tag;
        TextView isPublic;
        TextView isCoinExchange;
        TextView video_all_time;
        ImageView catalogue_content_img;
        ImageView inevitable_iv;
    }
}

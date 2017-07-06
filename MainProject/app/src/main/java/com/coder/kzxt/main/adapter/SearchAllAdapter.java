package com.coder.kzxt.main.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.beans.LiveBean;
import com.coder.kzxt.course.activity.LiveSynopsisActivity;
import com.coder.kzxt.course.activity.SearchLiveListActivity;
import com.coder.kzxt.main.activity.SearchAllCourseActivity;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.DateUtil;
import com.coder.kzxt.utils.GlideUtils;
import com.coder.kzxt.video.activity.VideoViewActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.coder.kzxt.activity.R.id.liveProgress;
import static com.coder.kzxt.activity.R.id.liveTitle;
import static com.coder.kzxt.activity.R.id.live_sta_tv;
import static com.coder.kzxt.utils.PublicUtils.IsEmpty;

/**
 * Created by tangcy on 2017/4/28.
 */

public class SearchAllAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<HashMap<String,String>> groupall;
    private ArrayList<List<HashMap<String,String>>> arrayList_all;
    final int TYPE1 = 1;//课程
    final int TYPE2 = 2;//直播课
    private LayoutInflater inflater;
    private String keywords;

    public SearchAllAdapter(Context context,List<HashMap<String,String>> groupall,ArrayList<List<HashMap<String,String>>> arrayList_all,String keywords) {
        this.context = context;
        this.groupall = groupall;
        this.arrayList_all = arrayList_all;
        this.keywords = keywords;
    }
    @Override
    public int getGroupCount() {
         return groupall.size();
    }
    @Override
    public int getChildrenCount(int groupPosition) {
        if(arrayList_all.get(groupPosition).size()>3){
            return 3;
        }else {
            return arrayList_all.get(groupPosition).size();
        }

    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupall.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return arrayList_all.get(groupPosition).get(childPosition);
    }

    @Override
    public int getChildType(int groupPosition, int childPosition) {
        String item = arrayList_all.get(groupPosition).get(childPosition).get("childtype");
        if(item.equals("1")){
            return TYPE1;
        }else {
            return TYPE2;
        }

    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public int getChildTypeCount() {
        return 20;
    }
    @Override
    public int getGroupTypeCount() {
        return 20;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.tab_search_live_group_item, null);
        }
        final TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView number = (TextView) convertView.findViewById(R.id.number);
        RelativeLayout relativeLayout = (RelativeLayout) convertView.findViewById(R.id.relativeLayout);
        title.setText(groupall.get(groupPosition).get("name"));
        number.setText("( "+groupall.get(groupPosition).get("num")+"个 )");

        if(arrayList_all.get(groupPosition).size()==0){
            relativeLayout.setVisibility(View.GONE);
        }else {
            relativeLayout.setVisibility(View.VISIBLE);
        }


        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolder1 holder1 = null;
        ViewHolder2 holder2 = null;
        HashMap<String,String> hashMap = arrayList_all.get(groupPosition).get(childPosition);
        int item = getChildType(groupPosition,childPosition);

        if (convertView == null) {
            inflater = LayoutInflater.from(context);
            switch (item){
                case TYPE1:
                    convertView= inflater.inflate(R.layout.item_course, null);
                    holder1 = new ViewHolder1();
                    holder1.courseName = (TextView) convertView.findViewById(R.id.course_name_text);
                    holder1.courseImage = (ImageView) convertView.findViewById(R.id.course_img);
                    holder1.freeOrVipImage = (ImageView) convertView.findViewById(R.id.free_or_vip);
                    holder1.studyTime = (TextView) convertView.findViewById(R.id.study_time);
                    holder1.more = (TextView) convertView.findViewById(R.id.more);
                    convertView.setTag(holder1);
                  break;

                case TYPE2:
                    convertView= inflater.inflate(R.layout.item_live, null);
                    holder2 = new ViewHolder2();
                    holder2.course_img = (ImageView) convertView.findViewById(R.id.course_img);
                    holder2.live_name_tv = (TextView) convertView.findViewById(R.id.live_name_tv);
                    holder2.host_name_tv = (TextView) convertView.findViewById(R.id.host_name_tv);
                    holder2.more = (TextView) convertView.findViewById(R.id.more);
                    holder2.liveProgress = (ProgressBar) convertView.findViewById(liveProgress);
                    holder2.liveTitle = (TextView) convertView.findViewById(liveTitle);
                    holder2.live_sta_tv = (LinearLayout) convertView.findViewById(live_sta_tv);
                    convertView.setTag(holder2);
                    break;

                default:
                    break;
            }

        }else {
            switch (item) {
                case TYPE1:
                    holder1 = (ViewHolder1) convertView.getTag();
                    break;
                case TYPE2:
                    holder2 = (ViewHolder2) convertView.getTag();
                    break;
            }
        }
        switch (item){
            case TYPE1:
                if(Integer.parseInt(groupall.get(groupPosition).get("num"))>3){
                    if(getChildId(groupPosition,childPosition)==0||getChildId(groupPosition,childPosition)==1){
                        holder1.more.setVisibility(View.GONE);
                    }else{
                        holder1.more.setVisibility(View.VISIBLE);
                    }
                }else {
                    holder1.more.setVisibility(View.GONE);
                }
                  final String courseName = hashMap.get("courseName");
                  final String courseImage = hashMap.get("courseImage");
                  String studyTime = hashMap.get("studyTime");
                  String price = hashMap.get("price");
                  final String treeId = hashMap.get("treeId");

                if (IsEmpty(price)) {
                    holder1.freeOrVipImage.setBackgroundResource(R.drawable.live_free_iv);
                } else {
                    holder1.freeOrVipImage.setBackgroundResource(R.drawable.live_vip_iv);
                }

                //搜索关键字变色
                int index1;
                ForegroundColorSpan span1 = new ForegroundColorSpan(context.getResources().getColor(R.color.font_blue));//要显示的颜色
                SpannableStringBuilder builder1 = new SpannableStringBuilder(courseName);
                index1 = courseName.indexOf(keywords);//从第几个匹配上
                if(index1!=-1){//有这个关键字用builder显示
                    builder1.setSpan(span1, index1, index1+keywords.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    holder1.courseName.setText(builder1);
                }else{//没有则直接显示
                    holder1.courseName.setText(courseName);
                }

                holder1.studyTime.setText(studyTime+ context.getResources().getString(R.string.course_hours));
                GlideUtils.loadCourseImg(context, courseImage, holder1.courseImage);

                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, VideoViewActivity.class);
                        intent.putExtra("flag", Constants.ONLINE);
                        intent.putExtra("treeid", treeId);
                        intent.putExtra("tree_name", courseName);
                        intent.putExtra("pic", courseImage);
                        intent.putExtra(Constants.IS_CENTER, "0");
                        context.startActivity(intent);
                    }
                });

                holder1.more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context , SearchAllCourseActivity.class);
                        intent.putExtra("keyword",keywords);
                        context.startActivity(intent);

                    }
                });


              break;

            case TYPE2:
                if(Integer.parseInt(groupall.get(groupPosition).get("num"))>3){
                    if(getChildId(groupPosition,childPosition)==0||getChildId(groupPosition,childPosition)==1){
                        holder2.more.setVisibility(View.GONE);
                    }else{
                        holder2.more.setVisibility(View.VISIBLE);
                    }
                }else {
                    holder2.more.setVisibility(View.GONE);
                }


                final String course_img = hashMap.get("course_img");
                final String live_name_tv = hashMap.get("live_name_tv");
                final String host_name_tv = hashMap.get("host_name_tv");
                final String start_time = hashMap.get("start_time");
                final String end_time = hashMap.get("end_time");
                final String status = hashMap.get("status");
                final String live_id = hashMap.get("live_id");

                GlideUtils.loadCourseImg(this.context, course_img, holder2.course_img);

                //搜索关键字变色
                int index2;
                ForegroundColorSpan span2 = new ForegroundColorSpan(context.getResources().getColor(R.color.font_blue));//要显示的颜色
                SpannableStringBuilder builder2 = new SpannableStringBuilder(live_name_tv);
                index2 = live_name_tv.indexOf(keywords);//从第几个匹配上
                if(index2!=-1){//有这个关键字用builder显示
                    builder2.setSpan(span2, index2, index2+keywords.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    holder2.live_name_tv.setText(builder2);
                }else{//没有则直接显示
                    holder2.live_name_tv.setText(live_name_tv);
                }

                holder2.host_name_tv.setText(context.getString(R.string.lecturer) + host_name_tv);

                long  start_time2 = Long.parseLong(start_time);
                long end_time2 = Long.parseLong(end_time);

                if(status.equals("0")){
                    holder2.liveTitle.setText(DateUtil.getHourMin(start_time2)+"--"+DateUtil.getHourMin(end_time2));
                    holder2.live_sta_tv.setVisibility(View.VISIBLE);
                    holder2.liveProgress.setVisibility(View.INVISIBLE);

                }else if(status.equals("1")){

                    holder2.live_sta_tv.setVisibility(View.VISIBLE);
                    holder2.liveProgress.setVisibility(View.VISIBLE);
                    if (android.os.Build.VERSION.SDK_INT > 22) {//android 6.0替换clip的加载动画
                        final Drawable drawable =  context.getResources().getDrawable(R.drawable.liveing_anim_60);
                        holder2.liveProgress.setIndeterminateDrawable(drawable);
                    }
                    holder2.liveTitle.setText("正在直播："+ DateUtil.getHourMin(start_time2)+"--"+DateUtil.getHourMin(end_time2));


                }else {
                    holder2.live_sta_tv.setVisibility(View.GONE);
                }

                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LiveBean liveBean = new LiveBean();
                        liveBean.setId(live_id);
                        LiveSynopsisActivity.gotoActivity(context,liveBean);
                    }
                });

                holder2.more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SearchLiveListActivity.gotoActivity(context, "all", keywords);
                    }
                });

                break;

            default:
                break;
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }


    public class ViewHolder1{
        TextView courseName;
        ImageView courseImage;
        ImageView freeOrVipImage;
        TextView studyTime;
        TextView more;
    }
    public class ViewHolder2{
        ImageView course_img;
        TextView live_name_tv;
        TextView host_name_tv;
        ProgressBar liveProgress;
        TextView liveTitle;
        LinearLayout live_sta_tv;
        TextView more;
    }
}

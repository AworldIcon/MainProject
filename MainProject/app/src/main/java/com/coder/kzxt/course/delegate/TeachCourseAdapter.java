package com.coder.kzxt.course.delegate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coder.kzxt.activity.R;
import com.coder.kzxt.course.beans.TeachBean;
import com.coder.kzxt.course.beans.TeachCourseBean;
import com.coder.kzxt.utils.GlideUtils;

import java.util.List;

import static com.coder.kzxt.utils.PublicUtils.IsEmpty;

/**
 * Created by tangcy on 2017/6/9.
 */

public class TeachCourseAdapter extends BaseExpandableListAdapter {

    private List<TeachCourseBean> mData;
    private Context context;

    public TeachCourseAdapter(Context context,List<TeachCourseBean> mData) {
        this.context = context;
        this.mData = mData;
    }

    @Override
    public int getGroupCount() {
        return mData.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return  mData.get(groupPosition).getOpenCourses().size();

    }

    @Override
    public Object getGroup(int groupPosition) {
        return mData.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mData.get(groupPosition).getOpenCourses().get(childPosition);
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
        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.teach_course_group_item, null);
        }
        final TextView type_tx = (TextView) convertView.findViewById(R.id.type_tx);
        String type =  mData.get(groupPosition).getType();
        if(type.equals("openCourse")){
            type_tx.setText("已开课");
        }else {
            type_tx.setText("待开课");
        }

        return convertView;

    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.order_course_top_layout, null);
        }
        TextView courseName = (TextView) convertView.findViewById(R.id.tv_course_name);
        TextView courseTime = (TextView) convertView.findViewById(R.id.tv_course_time);
        TextView sutdentNum = (TextView) convertView.findViewById(R.id.tv_num);
        ImageView courseImg = (ImageView) convertView.findViewById(R.id.iv_course);
        ImageView freeOrVipImage = (ImageView) convertView.findViewById(R.id.free_or_vip);
        LinearLayout ll_member = (LinearLayout) convertView.findViewById(R.id.ll_member);
        TeachBean.TeachCourseItem  teachCourseItem =  mData.get(groupPosition).getOpenCourses().get(childPosition);

        courseName.setText(teachCourseItem.getTitle());
        courseTime.setText(teachCourseItem.getLesson_num());
        ll_member.setVisibility(View.VISIBLE);
        sutdentNum.setText(teachCourseItem.getStudent_num());
        GlideUtils.loadCourseImg(context,teachCourseItem.getMiddle_pic(),courseImg);

        if (IsEmpty(teachCourseItem.getPrice())) {
            freeOrVipImage.setBackgroundResource(R.drawable.live_free_iv);
        } else {
            freeOrVipImage.setBackgroundResource(R.drawable.live_vip_iv);
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

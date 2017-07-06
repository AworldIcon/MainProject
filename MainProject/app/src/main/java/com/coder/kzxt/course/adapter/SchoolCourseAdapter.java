package com.coder.kzxt.course.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.course.beans.LearnBean;
import com.coder.kzxt.utils.GlideUtils;
import com.coder.kzxt.utils.PublicUtils;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2017/3/27.
 */

public class SchoolCourseAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<String> groups;
    private ArrayList<List<LearnBean.ItemsBean>> childs;

    public SchoolCourseAdapter(Context context, ArrayList<String> groups, ArrayList<List<LearnBean.ItemsBean>> childs) {
        this.context = context;
        this.groups = groups;
        this.childs = childs;
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childs.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childs.get(groupPosition).get(childPosition);
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
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.course_catalogue_group_item, null);
        }
        TextView catalogue_title_text = (TextView) convertView.findViewById(R.id.catalogue_title_text);
        catalogue_title_text.setText(groups.get(groupPosition));
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_course, null);
        }

        TextView course_name_text = (TextView) convertView.findViewById(R.id.course_name_text);
        ImageView course_img = (ImageView) convertView.findViewById(R.id.course_img);
        ImageView free_or_vip = (ImageView) convertView.findViewById(R.id.free_or_vip);
        TextView study_time = (TextView) convertView.findViewById(R.id.study_time);

        LearnBean.ItemsBean courseBean =  childs.get(groupPosition).get(childPosition);

        final String middlePicture = courseBean.getMiddle_pic();
//        final String lessonNum = courseBean.getLesson_num();
        final String name = courseBean.getTitle();
        final String treeid =courseBean.getId();
        final String price = courseBean.getPrice();

        course_name_text.setText(name);
        if (PublicUtils.IsEmpty(price)) {
            free_or_vip.setBackgroundResource(R.drawable.live_free_iv);
        } else {
            free_or_vip.setBackgroundResource(R.drawable.live_vip_iv);
        }
//        study_time.setText(lessonNum + context.getResources().getString(R.string.course_hours));
        GlideUtils.loadCourseImg(context,middlePicture,course_img);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

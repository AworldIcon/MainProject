package com.coder.kzxt.course.delegate;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.course.beans.TeachBean;
import com.coder.kzxt.course.beans.TeachResult;
import com.coder.kzxt.recyclerview.viewholder.TreeItem;
import com.coder.kzxt.recyclerview.viewholder.TreeParentItem;
import com.coder.kzxt.recyclerview.viewholder.ViewHolder;
import java.util.ArrayList;
import java.util.List;

/**
 * 发布第一级
 * Created by wangtingshun on 2017/4/26.
 */

public class PublicFirstLevel extends TreeParentItem<TeachResult> implements TreeItem.onClickChangeListener {

    private ArrayList<TeachBean.TeachCourseItem> courseList;

    public PublicFirstLevel(TeachResult data) {
        super(data);
    }

    @Override
    protected List<TreeItem> initChildsList(TeachResult data) {
        ArrayList<TreeItem> treeItems = new ArrayList<>();
        courseList = data.getCourseList();
        if (null == childs) {
            return null;
        }
        for (int i = 0; i < courseList.size(); i++) {
            PublicSecondLevel twoItems = new PublicSecondLevel(courseList.get(i),this);
            treeItems.add(twoItems);
        }
        return treeItems;
    }

    @Override
    protected int initLayoutId() {
        return  R.layout.activity_online_course;
    }

    @Override
    public void onBindViewHolder(Context context, ViewHolder holder) {
        String title = data.getTitle();
        TextView tv_title = (TextView) holder.getConvertView().findViewById(R.id.tv_course);
        ImageView iv_arrow = (ImageView) holder.getConvertView().findViewById(R.id.iv_arrow);
        if(!TextUtils.isEmpty(title)){
            tv_title.setVisibility(View.VISIBLE);
//            iv_arrow.setVisibility(View.VISIBLE);
            holder.setText(R.id.tv_course, title);
        } else {
            tv_title.setVisibility(View.GONE);
        }
        iv_arrow.setVisibility(View.GONE);

//        if (isExpand()) {
//            if (courseList.size() > 0) {
//                holder.setImageResource(R.id.iv_arrow, R.drawable.department_down);
//            } else {
//                holder.setImageResource(R.id.iv_arrow, R.drawable.department_go);
//            }
//        } else {
//            holder.setImageResource(R.id.iv_arrow, R.drawable.department_down);
//        }

    }

    @Override
    public void onClickChange(TreeItem treeItem) {
        super.onClickChange(treeItem);
    }
}

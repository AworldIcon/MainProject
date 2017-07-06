package com.coder.kzxt.main.delegate;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.coder.kzxt.activity.R;
import com.coder.kzxt.course.activity.CourseListActivity;
import com.coder.kzxt.main.beans.DepartmentBean;
import com.coder.kzxt.recyclerview.viewholder.TreeItem;
import com.coder.kzxt.recyclerview.viewholder.TreeParentItem;
import com.coder.kzxt.recyclerview.viewholder.ViewHolder;
import com.coder.kzxt.views.LinearLineWrapLayout;

import java.util.ArrayList;

/**
 * 院系三级item
 * Created by wangtingshun on 2017/2/21.
 */

public class ThreeLevelItem extends TreeItem<DepartmentBean.Department.DepartmentChild.ThreeLevelChild> {

    private Context mContext;
    private LinearLineWrapLayout lineWrapLayout;
    private ArrayList<DepartmentBean.Department.DepartmentChild.ThreeLevelChild> threeChilds;

    public ThreeLevelItem(DepartmentBean.Department.DepartmentChild.ThreeLevelChild data,
                          ArrayList<DepartmentBean.Department.DepartmentChild.ThreeLevelChild> childs, TreeParentItem parentItem) {
        super(data, parentItem);
        this.threeChilds = childs;
    }

    @Override
    protected int initLayoutId() {
        return R.layout.all_course_three_item;
    }

    @Override
    public void onBindViewHolder(final Context context, ViewHolder holder) {
        this.mContext = context;
        View convertView = holder.getConvertView();
        if (convertView != null) {
            lineWrapLayout = (LinearLineWrapLayout) convertView.findViewById(R.id.ll_wrap);
            if (lineWrapLayout != null) {
                lineWrapLayout.removeAllViews();
            }
            for (int i = 0; i < threeChilds.size(); i++) {
                TextView childItem = new TextView(context);
                childItem.setBackgroundResource(R.drawable.round_hui_banyuan);
                final DepartmentBean.Department.DepartmentChild.ThreeLevelChild threeChild = threeChilds.get(i);
                holder.setText(childItem, threeChild.getName());
                lineWrapLayout.addView(childItem);
                childItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mContext != null) {
                            Intent intent = new Intent(mContext, CourseListActivity.class);
                            intent.putExtra("category_id", threeChild.getId());
                            intent.putExtra("category_name", threeChild.getName());
                            mContext.startActivity(intent);
                        }
                    }
                });
            }
        }
    }

}

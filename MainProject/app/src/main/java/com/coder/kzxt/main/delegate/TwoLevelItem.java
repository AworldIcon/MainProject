package com.coder.kzxt.main.delegate;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.course.activity.CourseListActivity;
import com.coder.kzxt.main.beans.DepartmentBean;
import com.coder.kzxt.recyclerview.viewholder.TreeItem;
import com.coder.kzxt.recyclerview.viewholder.TreeParentItem;
import com.coder.kzxt.recyclerview.viewholder.ViewHolder;
import java.util.ArrayList;
import java.util.List;

/**
 * 院系二级item
 * Created by wangtingshun on 2017/2/21.
 */

public class TwoLevelItem extends TreeParentItem<DepartmentBean.Department.DepartmentChild> implements TreeItem.onClickChangeListener {


    private Context mContext;

    public TwoLevelItem(DepartmentBean.Department.DepartmentChild data, TreeParentItem parentItem) {
        super(data, parentItem);
    }

    @Override
    protected List<TreeItem> initChildsList(DepartmentBean.Department.DepartmentChild data) {
        ArrayList<TreeItem> treeItems = new ArrayList<>();
        ArrayList<DepartmentBean.Department.DepartmentChild.ThreeLevelChild> threeChilds = data.getChild();
        if (null == threeChilds) {
            return null;
        }
        if (threeChilds.size() > 0) {
            data.setHasChild(true);
        } else {
            data.setHasChild(false);
        }
        int size = threeChilds.size();
        for (int i = 0; i < size; i++) {
            ThreeLevelItem threeItem = new ThreeLevelItem(threeChilds.get(i), threeChilds,this);
            treeItems.add(threeItem);
        }
        return treeItems;
    }

    @Override
    protected int initLayoutId() {
        return R.layout.all_course_child_item;
    }

    @Override
    public void onBindViewHolder(final Context context,ViewHolder holder) {
        mContext = context;
        holder.setText(R.id.child_name, data.getName());
        ImageView child_img = (ImageView) holder.getConvertView().findViewById(R.id.child_img);
        if (data.isHasChild()) {
            child_img.setVisibility(View.VISIBLE);
        } else {
            child_img.setVisibility(View.GONE);
        }
        if (isExpand()) {
            holder.setImageResource(R.id.child_img, R.drawable.department_child_down);
        } else {
            holder.setImageResource(R.id.child_img, R.drawable.department_child_go);
        }
    }

    @Override
    public void onClickChange(TreeItem treeItem) {
        super.onClickChange(treeItem);
        TwoLevelItem twoLevelItem = (TwoLevelItem) treeItem;
        DepartmentBean.Department.DepartmentChild twoChild = twoLevelItem.getData();
        if(mContext != null && !data.isHasChild()){
            Intent intent = new Intent(mContext, CourseListActivity.class);
            intent.putExtra("category_id", twoChild.getId());
            intent.putExtra("category_name", twoChild.getName());
            mContext.startActivity(intent);
        }
    }
}

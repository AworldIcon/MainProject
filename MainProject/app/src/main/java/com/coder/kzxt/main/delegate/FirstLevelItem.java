package com.coder.kzxt.main.delegate;

import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.course.activity.CourseListActivity;
import com.coder.kzxt.main.beans.DepartmentBean;
import com.coder.kzxt.recyclerview.viewholder.TreeItem;
import com.coder.kzxt.recyclerview.viewholder.TreeParentItem;
import com.coder.kzxt.recyclerview.viewholder.ViewHolder;
import com.coder.kzxt.utils.GlideUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * 院系一级item
 * Created by wangtingshun on 2017/2/21.
 */

public class FirstLevelItem extends TreeParentItem<DepartmentBean.Department> implements TreeItem.onClickChangeListener {

    private Context mContext;

    public FirstLevelItem(DepartmentBean.Department data) {
        super(data);
    }

    @Override
    protected List<TreeItem> initChildsList(DepartmentBean.Department data) {
        ArrayList<TreeItem> treeItems = new ArrayList<>();
        ArrayList<DepartmentBean.Department.DepartmentChild> childs = data.getChild();
        if (null == childs) {
            return null;
        }
        if (childs.size() > 0) {
            data.setHasChild(true);
        } else {
            data.setHasChild(false);
        }
        for (int i = 0; i < childs.size(); i++) {
            TwoLevelItem twoItems = new TwoLevelItem(childs.get(i), this);
            treeItems.add(twoItems);
        }
        return treeItems;
    }

    @Override
    protected int initLayoutId() {
        return R.layout.all_course_group_item;
    }

    @Override
    public void onBindViewHolder(final Context context, ViewHolder holder) {
        this.mContext = context;
        holder.setText(R.id.course_name, data.getName());
        ImageView iv_icon = (ImageView) holder.getConvertView().findViewById(R.id.iv_icon);
        GlideUtils.loadGoods(context, data.getIconUrl(), iv_icon);
        if (isExpand()) {
            if (childs.size() > 0) {
                holder.setImageResource(R.id.iv_arrow, R.drawable.department_down);
            } else {
                holder.setImageResource(R.id.iv_arrow, R.drawable.department_go);
            }
        } else {
            holder.setImageResource(R.id.iv_arrow, R.drawable.department_go);
        }
    }

    @Override
    public void onClickChange(TreeItem treeItem) {
        super.onClickChange(treeItem);
        FirstLevelItem firstItem = (FirstLevelItem) treeItem;
        DepartmentBean.Department firstChild = firstItem.getData();
        if (mContext != null && childs.size() <= 0) {
            Intent intent = new Intent(mContext, CourseListActivity.class);
            intent.putExtra("category_id", firstChild.getId());
            intent.putExtra("category_name", firstChild.getName());
            mContext.startActivity(intent);
        }
    }
}

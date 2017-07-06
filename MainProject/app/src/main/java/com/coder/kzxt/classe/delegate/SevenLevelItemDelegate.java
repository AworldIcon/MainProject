package com.coder.kzxt.classe.delegate;

import android.content.Context;

import com.coder.kzxt.activity.R;
import com.coder.kzxt.classe.activity.OrganizationActivity;
import com.coder.kzxt.classe.beans.OrganizationBean;
import com.coder.kzxt.classe.manager.ObserverManager;
import com.coder.kzxt.recyclerview.viewholder.TreeItem;
import com.coder.kzxt.recyclerview.viewholder.TreeParentItem;
import com.coder.kzxt.recyclerview.viewholder.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 王廷顺 on 2017/6/19.
 * 七级item
 */

public class SevenLevelItemDelegate  extends TreeParentItem<OrganizationBean.SevenLevelBean> implements TreeItem.onClickChangeListener {

    private OrganizationActivity mContext;

    public SevenLevelItemDelegate(OrganizationBean.SevenLevelBean data, TreeParentItem parentItem) {
        super(data, parentItem);
    }

    @Override
    protected List<TreeItem> initChildsList(OrganizationBean.SevenLevelBean data) {
        ArrayList<TreeItem> treeItems = new ArrayList<>();
        ArrayList<OrganizationBean.EightLevelBean> childrens = data.getChildren();
        if (null == childrens) {
            return null;
        }
        if (childrens.size() > 0) {
            data.setHasChild(true);
        } else {
            data.setHasChild(false);
        }
        int size = childrens.size();
        for (int i = 0; i < size; i++) {
            EightLevelItemDelegate eightItem = new EightLevelItemDelegate(childrens.get(i),this);
            treeItems.add(eightItem);
        }
        return treeItems;
    }

    @Override
    protected int initLayoutId() {
        return R.layout.organization_child_item;
    }

    @Override
    public void onBindViewHolder(Context context, ViewHolder holder) {
        this.mContext = (OrganizationActivity)context;
        holder.setText(R.id.child_name,data.getName());
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
        if(!data.isHasChild()){
            String name = data.getName();
            String categoryId = data.getId();
            ObserverManager.getInstance().add(mContext);
            ObserverManager.getInstance().notifyObserver(categoryId,name);
        }
    }
}

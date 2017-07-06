package com.coder.kzxt.recyclerview.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.coder.kzxt.course.delegate.PublicFirstLevel;
import com.coder.kzxt.main.delegate.FirstLevelItem;
import com.coder.kzxt.main.delegate.ThreeLevelItem;
import com.coder.kzxt.recyclerview.viewholder.TreeItem;
import com.coder.kzxt.recyclerview.viewholder.TreeItemManager;
import com.coder.kzxt.recyclerview.viewholder.TreeParentItem;
import com.coder.kzxt.recyclerview.viewholder.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangtingshun on 2017/2/21.
 */

public class TreeRecyclerViewAdapter<T extends TreeItem> extends RecyclerView.Adapter<ViewHolder>
        implements TreeItemManager {
    protected TreeRecyclerViewType type;
    /**
     * 上下文
     */
    protected Context mContext;
    /**
     * 存储原始的items;
     */
    private List<T> mDatas;//处理后的展示数据
    /**
     * 存储可见的items
     */
    protected List<T> mShowDatas;//处理后的展示数据

    private  TreeItem treeItem;
    /**
     * @param context 上下文
     * @param datas   条目数据
     */
    public TreeRecyclerViewAdapter(Context context, List<T> datas) {
        this(context, datas, TreeRecyclerViewType.SHOW_DEFUTAL);
    }

    /**
     * @param context 上下文
     * @param datas   条目数据
     */
    public TreeRecyclerViewAdapter(Context context, List<T> datas, TreeRecyclerViewType type) {
        this.type = type;
        mContext = context;
        mDatas = datas;
        datas = datas == null ? new ArrayList<T>() : datas;
        if (type == TreeRecyclerViewType.SHOW_ALL) {
            mShowDatas = new ArrayList<>();
            for (int i = 0; i < datas.size(); i++) {
                T t = datas.get(i);
                mShowDatas.add(t);
                if (t instanceof TreeParentItem) {
                    List allChilds = ((TreeParentItem) t).getChilds(type);
                    mShowDatas.addAll(allChilds);
                }
            }
        } else {
            mShowDatas = datas;
        }
    }

    /**
     * 相应ListView的点击事件 展开或关闭某节点
     *
     * @param position 触发的条目
     */
    private void expandOrCollapse(int position) {
        treeItem = mShowDatas.get(position);
        if (treeItem instanceof TreeParentItem && ((TreeParentItem) treeItem).isCanChangeExpand()) {
            TreeParentItem treeParentItem = (TreeParentItem) treeItem;
            boolean expand = treeParentItem.isExpand();
            List allChilds = treeParentItem.getChilds(type);
            if (expand) {
                mShowDatas.removeAll(allChilds);
                treeParentItem.onCollapse();
                treeParentItem.setExpand(false);
                //收回列表直接刷新全部
                notifyDataSetChanged();
            } else {
                if (treeItem instanceof FirstLevelItem) {
                    mShowDatas.addAll(position + 1, allChilds);
                    for (int i = 0; i < allChilds.size(); i++) {
                        notifyItemInserted((position + 1) + i);
                    }
                } else if (treeItem instanceof PublicFirstLevel) {
                    mShowDatas.addAll(position + 1, allChilds);
                } else {     //第三级数据结构发生改变，可以横向显示多个条目
                    List<ThreeLevelItem> childs = new ArrayList<>();
                    if (allChilds.size() > 0) {
                        Object o = allChilds.get(0);
                        if (o instanceof ThreeLevelItem) {
                            ThreeLevelItem levelItem = (ThreeLevelItem) allChilds.get(0);
                            childs.add(levelItem);
                            mShowDatas.addAll(position + 1, (List) childs);
                            //recyclerview 角标从position + 1位置开始陆续增1
                            for (int i = 0; i < childs.size(); i++) {
                                notifyItemInserted((position + 1) + i);
                            }
                        } else {
                            mShowDatas.addAll(position + 1, allChilds);
                        }
                    }
                }
                treeParentItem.onExpand();
                treeParentItem.setExpand(true);
                //从position位置开始到最后位置刷新角标变化
                notifyItemRangeChanged(position, mShowDatas.size());
            }
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    TreeItem treeItem = mShowDatas.get(position);
                    if (treeItem.getSpanSize() == 0) {
                        return gridLayoutManager.getSpanCount();
                    }
                    return treeItem.getSpanSize();
                }
            });
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return ViewHolder.createViewHolder(mContext, parent, viewType);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final TreeItem treeItem = mShowDatas.get(position);
        treeItem.setTreeItemManager(this);
        treeItem.onBindViewHolder(mContext,holder);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type != TreeRecyclerViewType.SHOW_ALL) {
                    expandOrCollapse(holder.getLayoutPosition());
                }

                if(!(treeItem instanceof ThreeLevelItem)){//屏蔽掉第三级点击事件
                    treeItem.onClickChange(treeItem);
                }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return mShowDatas.get(position).getLayoutId();
    }

    @Override
    public int getItemCount() {
        return mShowDatas == null ? 0 : mShowDatas.size();
    }

    public List<T> getDatas() {
        return mDatas;
    }

    public void setDatas(List<T> datas) {
        mDatas = datas;
    }

}
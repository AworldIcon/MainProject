package com.coder.kzxt.recyclerview.adapter;


import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.ViewGroup;

/**
 * Created by MaShiZhao on 2016/12/29
 * 对BaseDelegate进行封装 主要是footer时进行判断如果是StaggeredGridLayoutManager加载更多在一行显示
 */

public class PullRefreshDelegate<T> extends BaseDelegate<T>
{


    public PullRefreshDelegate(int customLayoutId)
    {
        super(customLayoutId);
    }

    public PullRefreshDelegate(int headerLayoutId, int customLayoutId)
    {
        super(headerLayoutId, customLayoutId);
    }

    @Override
    public void initFooterView(BaseViewHolder holder, int position)
    {
        super.initFooterView(holder, position);

        //StaggeredGridLayoutManager 有footer时判断 如果是瀑布流时需要 设置布局是充满整个屏幕
        //GridLayout 参考 MyPullRecyclerView   resetItem()
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        if (layoutParams instanceof StaggeredGridLayoutManager.LayoutParams)
        {
            ((StaggeredGridLayoutManager.LayoutParams) layoutParams).setFullSpan(true);
        }
    }
}

package com.coder.kzxt.recyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;

import com.coder.kzxt.recyclerview.adapter.PullRefreshAdapter;

/**
 * Created by MaShiZhao on 2016/12/30
 */
public class MyPullRecyclerView extends MyRecyclerView
{

    // 加载更多的回调
    private OnAddMoreListener addMoreListener;
    private boolean isInTheBottom = false;


    private PullRefreshAdapter myAdapter;


    public MyPullRecyclerView(Context context)
    {
        super(context);
    }

    public MyPullRecyclerView(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
    }

    public MyPullRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }


    @Override
    public void setAdapter(Adapter adapter)
    {
        super.setAdapter(adapter);
        if (adapter instanceof PullRefreshAdapter)
        {
            this.myAdapter = (PullRefreshAdapter) adapter;
        }
        resetItem();
    }

    //GridLayoutManager 时, 设置底部或者头部有布局时 充满整个屏幕
    public void resetItem()
    {
        if (myAdapter == null)
        {
            return;
        }
        LayoutManager layoutManager = getLayoutManager();
        if (layoutManager == null)
        { //it maybe unnecessary
            throw new RuntimeException("LayoutManager is null,Please check it!");
        }

        if (layoutManager instanceof GridLayoutManager)
        {
            final GridLayoutManager lm = (GridLayoutManager) layoutManager;
            lm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup()
            {
                @Override
                public int getSpanSize(int position)
                {
                    return myAdapter.isHeaderItem(position) ? lm.getSpanCount() : (myAdapter.isFooterItem(position) ? lm.getSpanCount() : 1);
                }
            });
        }
    }


    /**
     * reachBottomRow = 1;(default)
     * mean : when the lastVisibleRow is lastRow , call the onReachBottom();
     * reachBottomRow = 2;
     * mean : when the lastVisibleRow is Penultimate Row , call the onReachBottom();
     * And so on
     */
    private int reachBottomRow = 2;


    @Override
    public void onScrolled(int dx, int dy)
    {
        super.onScrolled(dx, dy);
        if (addMoreListener != null)
        {
            LayoutManager layoutManager = getLayoutManager();
            if (layoutManager == null)
            { //it maybe unnecessary
                throw new RuntimeException("LayoutManager is null,Please check it!");
            }
            Adapter adapter = getAdapter();
            if (adapter == null)
            { //it maybe unnecessary
                throw new RuntimeException("Adapter is null,Please check it!");
            }
            boolean isReachBottom = false;
            //is GridLayoutManager
            if (layoutManager instanceof GridLayoutManager)
            {
                GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
                int rowCount = adapter.getItemCount() / gridLayoutManager.getSpanCount();
                int lastVisibleRowPosition = gridLayoutManager.findLastVisibleItemPosition() / gridLayoutManager.getSpanCount();
                isReachBottom = (lastVisibleRowPosition >= rowCount - reachBottomRow);
            }
            //is LinearLayoutManager
            else if (layoutManager instanceof LinearLayoutManager)
            {
                int lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                int rowCount = adapter.getItemCount();
                if (reachBottomRow > rowCount)
                    reachBottomRow = 1;
                isReachBottom = (lastVisibleItemPosition >= rowCount - reachBottomRow);
            }
            //is StaggeredGridLayoutManager
            else if (layoutManager instanceof StaggeredGridLayoutManager)
            {
                StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
                int spanCount = staggeredGridLayoutManager.getSpanCount();
                int[] into = new int[spanCount];
                int[] eachSpanListVisibleItemPosition = staggeredGridLayoutManager.findLastVisibleItemPositions(into);
                for (int i = 0; i < spanCount; i++)
                {
                    if (eachSpanListVisibleItemPosition[i] > adapter.getItemCount() - reachBottomRow * spanCount)
                    {
                        isReachBottom = true;
                        break;
                    }
                }
            }

            if (!isReachBottom)
            {
                isInTheBottom = false;
            } else if (!isInTheBottom)
            {
                isInTheBottom = true;
                // 正在加载或者刷新的 不执行
                if (myAdapter != null && myAdapter.isBeginLoading())
                {
                    addMoreListener.addMoreListener();
                    myAdapter.setLoading(true);
                    myAdapter.getSwipeRefresh().setEnabled(false);
                }
            }
        }
    }

    // get set 方法
    public void setOnAddMoreListener(OnAddMoreListener addMoreListener)
    {
        this.addMoreListener = addMoreListener;
    }


    public interface OnAddMoreListener
    {
        void addMoreListener();
    }


}

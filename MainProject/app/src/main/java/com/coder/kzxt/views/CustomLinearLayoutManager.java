package com.coder.kzxt.views;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

/**
 * 禁止recyclerView滑动
 * Created by wangtingshun on 2017/5/9.
 */

public class CustomLinearLayoutManager extends LinearLayoutManager {

    private boolean isScrollEnabled = true;

    public CustomLinearLayoutManager(Context context) {
        super(context);
    }

    public CustomLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public void setScrollEnabled(boolean isScroll){
        this.isScrollEnabled = isScroll;
    }

    @Override
    public boolean canScrollVertically() {
        return isScrollEnabled && super.canScrollVertically();
    }
}

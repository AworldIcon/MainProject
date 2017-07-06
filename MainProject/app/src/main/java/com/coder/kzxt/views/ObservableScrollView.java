package com.coder.kzxt.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

import com.coder.kzxt.main.mInterface.ScrollViewInterface;

/**
 * 解决ScrollView.setOnScrollChangeListener() API23以上可用问题
 * Created by wangtingshun on 2017/3/27.
 */

public class ObservableScrollView extends ScrollView {

    private ScrollViewInterface mScrollViewlistener;

    public ObservableScrollView(Context context) {
        super(context);
    }

    public ObservableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ObservableScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setOnScrollChangeListener(ScrollViewInterface listener) {
        this.mScrollViewlistener = listener;
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        if (mScrollViewlistener != null) {
            mScrollViewlistener.onScrollChanged(this, x, y, oldx, oldy);
        }
    }
}

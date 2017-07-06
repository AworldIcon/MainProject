package com.coder.kzxt.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by MaShiZhao on 2017/3/27
 */

public class MyAddViewOnLinearLayout extends LinearLayout
{
    private Context mContext;

    public MyAddViewOnLinearLayout(Context context)
    {
        super(context);
        this.mContext = context;
    }


    public MyAddViewOnLinearLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        this.mContext = context;
    }

    public void mAddViewInlayout(View child, int index, ViewGroup.LayoutParams params)
    {
        addViewInLayout(child, index, params);
    }

    public void mAddViewInlayout(View child)
    {
        int index = this.getChildCount();
        mAddViewInlayout(child, index);
    }

    public void mAddViewInlayout(View child, int index)
    {
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addViewInLayout(child, index, layoutParams);
    }
}

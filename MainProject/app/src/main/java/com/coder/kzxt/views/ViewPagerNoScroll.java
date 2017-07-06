package com.coder.kzxt.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 禁止触摸左右滑动，但不影响点击按钮的滑动切换，也不影响pager中的列表上下滚动
 * 需要在点击外部切换按钮的时候让istouch字段为false;
 * 切记不要影响事件的下发，否则会影响里面列表的滚动
 */

public class ViewPagerNoScroll extends ViewPager {

    public  boolean istouch=false;
    public ViewPagerNoScroll(Context context) {
        super(context);
    }

    public ViewPagerNoScroll(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(ev.getAction()==MotionEvent.ACTION_DOWN){
            istouch=true;
        }
         return super.dispatchTouchEvent(ev);

    }

    @Override
    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
        if(istouch){
            return false;
        }
        return super.canScroll(v, checkV, dx, x, y);
    }

    @Override
    public void scrollTo(int x, int y) {
        if(!istouch){
            super.scrollTo(x, y);
        }
    }
}


package com.coder.kzxt.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

public class SelfLayout extends RelativeLayout {
    public SelfLayout(Context context) {
        super(context);
    }

    public SelfLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SelfLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    public void setOnLongClickListener(OnLongClickListener l) {

        super.setOnLongClickListener(l);
    }

    long preTime;
    long preTime2;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            preTime = System.currentTimeMillis();
        }
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            preTime2 = System.currentTimeMillis();
        }
        if (preTime2 - preTime > 100) {
            preTime = preTime2;
            return true;
        }
        onClickEventListener.OnClickEvent();
        return super.onInterceptTouchEvent(ev);
    }

    private OnClickEventListener onClickEventListener;

    public void setOnClickEventListener(OnClickEventListener eventListener){
        this.onClickEventListener = eventListener;
    }

    public interface OnClickEventListener{
       void OnClickEvent();
    }

}
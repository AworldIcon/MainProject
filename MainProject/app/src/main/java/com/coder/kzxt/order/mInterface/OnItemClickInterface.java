package com.coder.kzxt.order.mInterface;

import com.coder.kzxt.order.beans.CourseClass;

public interface OnItemClickInterface {

    /**
     * 点击授课班条目
     * @param classBean
     */
    void onClassItemClick(CourseClass.ClassBean classBean);

    /**
     * 点击授课班内部条目
     * @param classBean
     */
    void onInnerClassItemClick(CourseClass.ClassBean classBean);
}
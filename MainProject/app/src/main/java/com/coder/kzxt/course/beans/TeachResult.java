package com.coder.kzxt.course.beans;

import java.util.ArrayList;

/**
 * 组合在教课程数据
 * Created by wangtingshun on 2017/4/24.
 */

public class TeachResult {

    private String title; //标题

    private ArrayList<TeachBean.TeachCourseItem> courseList;

    public ArrayList<TeachBean.TeachCourseItem> getCourseList() {
        return courseList;
    }

    public void setCourseList(ArrayList<TeachBean.TeachCourseItem> courseList) {
        this.courseList = courseList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "TeachResult{" +
                "title='" + title + '\'' +
                ", courseList=" + courseList +
                '}';
    }
}

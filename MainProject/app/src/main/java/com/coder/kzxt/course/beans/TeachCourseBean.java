package com.coder.kzxt.course.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tangcy on 2017/6/9.
 */

public class TeachCourseBean {

    private String type;
    private ArrayList<TeachBean.TeachCourseItem> openCourses;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<TeachBean.TeachCourseItem> getOpenCourses() {
        return openCourses;
    }

    public void setOpenCourses(ArrayList<TeachBean.TeachCourseItem> openCourses) {
        this.openCourses = openCourses;
    }

}

package com.coder.kzxt.course.beans;

import com.coder.kzxt.base.beans.BaseBean;
import com.coder.kzxt.base.beans.CourseBean;

/**
 * Created by Administrator on 2017/3/25.
 */

public class CourseSynopsosBean extends BaseBean
{
    private CourseBean item;

    public CourseBean getItem() {
        return item;
    }
    public void setItem(CourseBean item) {
        this.item = item;
    }
}

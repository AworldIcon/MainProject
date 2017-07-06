package com.coder.kzxt.course.beans;

import com.coder.kzxt.base.beans.*;
import com.coder.kzxt.base.beans.CourseBean;

import java.util.List;

/**
 * Created by MaShiZhao on 2017/3/21
 */

public class LocalCourseResultBean extends BaseBean
{

    private List<com.coder.kzxt.base.beans.CourseBean> items;

    public List<CourseBean> getItems()
    {
        return items;
    }

    public void setItems(List<CourseBean> items)
    {
        this.items = items;
    }

}

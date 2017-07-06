package com.coder.kzxt.service.beans;

import com.coder.kzxt.base.beans.BaseBean;
import com.coder.kzxt.base.beans.CourseBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MaShiZhao on 2017/6/12
 */

public class ServiceCourseResult extends BaseBean
{
    private List<CourseBean> items;

    public List<CourseBean> getItems()
    {
        if (items == null)
        {
            return new ArrayList<>();
        }
        return items;
    }

    public void setItems(List<CourseBean> items)
    {
        this.items = items;
    }
}

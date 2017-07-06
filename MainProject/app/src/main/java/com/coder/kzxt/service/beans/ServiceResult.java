package com.coder.kzxt.service.beans;

import com.coder.kzxt.base.beans.BaseBean;

import java.util.List;

/**
 * Created by MaShiZhao on 2017/6/9
 */

public class ServiceResult extends BaseBean
{
    private List<ServiceBean> items;

    public List<ServiceBean> getItems()
    {
        return items;
    }

    public void setItems(List<ServiceBean> items)
    {
        this.items = items;
    }
}

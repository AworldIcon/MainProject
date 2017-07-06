package com.coder.kzxt.service.beans;

import com.coder.kzxt.base.beans.BaseBean;

/**
 * Created by MaShiZhao on 2017/6/9
 */

public class ServiceDetailResult extends BaseBean
{
    private ServiceBean item;

    public ServiceBean getItem()
    {
        return item;
    }

    public void setItem(ServiceBean item)
    {
        this.item = item;
    }
}

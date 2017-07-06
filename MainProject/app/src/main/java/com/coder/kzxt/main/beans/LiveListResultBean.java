package com.coder.kzxt.main.beans;

import com.coder.kzxt.base.beans.BaseBean;
import com.coder.kzxt.base.beans.LiveBean;

import java.util.List;

/**
 * Created by MaShiZhao on 2017/4/13
 */

public class LiveListResultBean extends BaseBean
{
    private List<LiveBean> items;

    public List<LiveBean> getItems()
    {
        return items;
    }

    public void setItems(List<LiveBean> items)
    {
        this.items = items;
    }
}

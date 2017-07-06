package com.coder.kzxt.sign.beans;

import com.coder.kzxt.base.beans.BaseBean;

import java.util.List;

/**
 * Created by MaShiZhao on 2017/5/24
 */

public class SignListResult extends BaseBean
{
    private List<SignBean> items;

    public List<SignBean> getItems()
    {
        return items;
    }

    public void setItems(List<SignBean> items)
    {
        this.items = items;
    }
}

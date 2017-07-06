package com.coder.kzxt.sign.beans;

import com.coder.kzxt.base.beans.BaseBean;

import java.util.List;

/**
 * Created by MaShiZhao on 2017/5/24
 */

public class SignTagResult extends BaseBean
{
    private List<SignTagBean> items;

    public List<SignTagBean> getItems()
    {
        return items;
    }

    public void setItems(List<SignTagBean> items)
    {
        this.items = items;
    }
}

package com.coder.kzxt.sign.beans;

import com.coder.kzxt.base.beans.BaseBean;

/**
 * Created by MaShiZhao on 2017/5/23
 */

public class SignResult extends BaseBean
{
    private SignBean item;

    public SignBean getItem()
    {
        if (item == null)
        {
            return new SignBean();
        }
        return item;
    }

    public void setItem(SignBean item)
    {
        this.item = item;
    }
}

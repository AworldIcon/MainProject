package com.coder.kzxt.login.beans;

import com.coder.kzxt.base.beans.BaseBean;
import com.coder.kzxt.base.beans.ImInfoBean;

/**
 * Created by MaShiZhao on 2017/3/28
 */

public class ImInfoResult extends BaseBean
{
    private ImInfoBean item;

    public ImInfoBean getItem()
    {
        return item;
    }

    public void setItem(ImInfoBean item)
    {
        this.item = item;
    }
}

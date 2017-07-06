package com.coder.kzxt.login.beans;

import com.coder.kzxt.base.beans.BaseBean;
import com.coder.kzxt.base.beans.UserInfoBean;

/**
 * Created by MaShiZhao on 2017/3/20
 */

public class UserInfoResult extends BaseBean
{

    private UserInfoBean item;

    public UserInfoBean getItem()
    {
        return item;
    }

    public void setItem(UserInfoBean item)
    {
        this.item = item;
    }
}

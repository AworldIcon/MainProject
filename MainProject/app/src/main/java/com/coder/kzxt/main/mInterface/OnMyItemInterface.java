package com.coder.kzxt.main.mInterface;

import com.coder.kzxt.main.beans.MyBean;

/**
 * 我的页面item回调接口
 * Created by wangtingshun on 2017/3/30.
 */

public interface OnMyItemInterface {

    /**
     * 上部Item
     * @param item
     */
    void onMyTopItemClick(MyBean.ChildItemBean item);

    /**
     * 底部Item
     * @param item
     */
    void onMyBottomItemClick(MyBean.ChildItemBean item);
}

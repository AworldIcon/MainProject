package com.coder.kzxt.recyclerview.viewholder;

import java.util.List;

/**
 * Created by wangtingshun on 2017/2/16.
 */

public interface ParentItem {

    /**
     * 是否展开
     *
     * @return
     */
    boolean isExpand();

    /**
     * 展开后的回调
     */
    void onExpand();

    /**
     * 折叠后的回调
     */
    void onCollapse();

    /**
     * 获取孩子
     *
     * @return
     */
    List<TreeItem> getChilds();
}

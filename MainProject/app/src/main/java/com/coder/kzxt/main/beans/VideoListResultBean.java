package com.coder.kzxt.main.beans;

import com.coder.kzxt.base.beans.BaseBean;
import com.coder.kzxt.base.beans.VideoBean;

import java.util.List;

/**
 * Created by MaShiZhao on 2017/4/13
 */

public class VideoListResultBean extends BaseBean
{
    private List<VideoBean> items;

    public List<VideoBean> getItems()
    {
        return items;
    }

    public void setItems(List<VideoBean> items)
    {
        this.items = items;
    }
}

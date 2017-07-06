package com.coder.kzxt.video.beans;

import com.coder.kzxt.base.beans.BaseBean;
import com.coder.kzxt.base.beans.ChatRoomBean;

/**
 * Created by MaShiZhao on 2017/4/21
 */

public class ChatRoomResult extends BaseBean
{
    private ChatRoomBean item;

    public ChatRoomBean getItem()
    {
        return item;
    }

    public void setItem(ChatRoomBean item)
    {
        this.item = item;
    }
}

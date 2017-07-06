package com.coder.kzxt.base.beans;

import java.io.Serializable;

/**
 * Created by zw on 2017/5/19.
 */

public  class TextImageBean implements Serializable {
    /**
     * 0 是没有返回
     * 1 text 文本
     * 2 img 图片
     */
    private String type;
    private String content;

    public int getType()
    {
        if (type == null)
        {
            return 0;
        }
        // 现阶段只区分 图文
        if (type.equals("img"))
        {
            return 2;
        } else
        {
            return 1;
        }
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }
}
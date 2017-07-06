package com.coder.kzxt.sign.beans;

import java.io.Serializable;

/**
 * Created by MaShiZhao on 2017/5/24
 */

public class SignTagBean implements Serializable
{
    private String id;
    private String name;
    private Boolean isSelect;

    public Boolean getSelect()
    {
        return isSelect;
    }

    public void setSelect(Boolean select)
    {
        isSelect = select;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}

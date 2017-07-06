package com.coder.kzxt.dialog.util;

/**
 * Created by MaShiZhao on 2017/1/10
 */
public class BaseString
{

    private String msg;
    private Boolean isSelect;

    public BaseString(String string)
    {
        msg = string;
    }

    public Boolean getIsSelect()
    {
        if (isSelect == null)
        {
            return false;
        }
        return isSelect;
    }

    public void setIsSelect(Boolean isSelect)
    {
        this.isSelect = isSelect;
    }

    public String getMsg()
    {
        if (msg == null)
        {
            return "";
        } else
        {
            return msg;
        }
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }

}

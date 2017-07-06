package com.coder.kzxt.main.beans;

import com.coder.kzxt.base.beans.BaseBean;

/**
 * Created by MaShiZhao on 2017/4/13
 */

public class SystemTimeResultBean extends BaseBean
{
    private SystemTimeBean item;

    public SystemTimeBean getItem()
    {
        return item;
    }

    public void setItem(SystemTimeBean item)
    {
        this.item = item;
    }

    public class SystemTimeBean
    {
        private long time;

        public long getTime()
        {
            return time;
        }

        public void setTime(long time)
        {
            this.time = time;
        }
    }
}

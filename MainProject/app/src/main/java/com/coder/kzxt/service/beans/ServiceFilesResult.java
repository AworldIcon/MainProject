package com.coder.kzxt.service.beans;

import com.coder.kzxt.base.beans.BaseBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MaShiZhao on 2017/6/12
 */

public class ServiceFilesResult extends BaseBean
{
    private List<Item> items;

    public List<Item> getItems()
    {
        if (items == null)
        {
            return new ArrayList<>();
        }
        return items;
    }

    public void setItems(List<Item> items)
    {
        this.items = items;
    }


    public class Item
    {
        private String id;
        private String title;
        private String size;
        private String ext;

        public String getId()
        {
            return id;
        }

        public void setId(String id)
        {
            this.id = id;
        }

        public String getTitle()
        {
            return title;
        }

        public void setTitle(String title)
        {
            this.title = title;
        }

        public String getSize()
        {
            return size;
        }

        public void setSize(String size)
        {
            this.size = size;
        }

        public String getExt()
        {
            return ext;
        }

        public void setExt(String ext)
        {
            this.ext = ext;
        }
    }
}

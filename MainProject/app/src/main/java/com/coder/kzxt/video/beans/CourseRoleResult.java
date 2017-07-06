package com.coder.kzxt.video.beans;

import com.coder.kzxt.base.beans.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by MaShiZhao on 2017/4/21
 */

public class CourseRoleResult extends BaseBean implements Serializable
{
    private Item item;

    public Item getItem()
    {
        return item;
    }

    public void setItem(Item item)
    {
        this.item = item;
    }

    public class Item implements Serializable
    {
        private int role;
        private List<ListBean> list;

        public int getRole()
        {
            return role;
        }

        public void setRole(int role)
        {
            this.role = role;
        }

        public List<ListBean> getList()
        {
            return list;
        }

        public void setList(List<ListBean> list)
        {
            this.list = list;
        }

        public class ListBean implements Serializable
        {
            private String id;
            private String class_name;

            public String getId()
            {
                return id;
            }

            public void setId(String id)
            {
                this.id = id;
            }

            public String getClass_name()
            {
                return class_name;
            }

            public void setClass_name(String class_name)
            {
                this.class_name = class_name;
            }
        }
    }
}

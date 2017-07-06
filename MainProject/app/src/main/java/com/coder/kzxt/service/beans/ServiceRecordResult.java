package com.coder.kzxt.service.beans;

import com.coder.kzxt.base.beans.BaseBean;

/**
 * Created by MaShiZhao on 2017/6/16
 */

public class ServiceRecordResult extends BaseBean
{

    private  ItemsBean item;

    public ItemsBean getItem()
    {
        return item;
    }

    public void setItem(ItemsBean item)
    {
        this.item = item;
    }

    public static class ItemsBean
    {
        /**
         * id : ID
         * service_member_id : 学习id
         * desc : 记录
         * type : 类型
         * state : 状态
         * create_time : 创建时间
         * update_time : 修改时间
         */

        private String id;
        private String service_member_id;
        private String desc;
        private String type;
        private String state;
        private String remark;
        private long create_time;
        private long update_time;

        public String getRemark()
        {
            return remark;
        }

        public void setRemark(String remark)
        {
            this.remark = remark;
        }

        public String getId()
        {
            return id;
        }

        public void setId(String id)
        {
            this.id = id;
        }

        public String getService_member_id()
        {
            return service_member_id;
        }

        public void setService_member_id(String service_member_id)
        {
            this.service_member_id = service_member_id;
        }

        public String getDesc()
        {
            return desc;
        }

        public void setDesc(String desc)
        {
            this.desc = desc;
        }

        public String getType()
        {
            return type;
        }

        public void setType(String type)
        {
            this.type = type;
        }

        public String getState()
        {
            return state;
        }

        public void setState(String state)
        {
            this.state = state;
        }

        public long getCreate_time()
        {
            return create_time;
        }

        public void setCreate_time(long create_time)
        {
            this.create_time = create_time;
        }

        public long getUpdate_time()
        {
            return update_time;
        }

        public void setUpdate_time(long update_time)
        {
            this.update_time = update_time;
        }
    }
}

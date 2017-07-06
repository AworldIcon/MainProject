package com.coder.kzxt.service.beans;

import com.coder.kzxt.base.beans.BaseBean;

import java.util.List;

/**
 * Created by MaShiZhao on 2017/6/16
 */

public class ServiceExamResult extends BaseBean
{

    private List<ItemsBean> items;

    public List<ItemsBean> getItem()
    {
        return items;
    }

    public void setItem(List<ItemsBean> item)
    {
        this.items = item;
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
        private String title;
        private String place;
        private String remark;
        private long create_time;
        private long exam_time;

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

        public String getTitle()
        {
            return title;
        }

        public void setTitle(String title)
        {
            this.title = title;
        }

        public String getPlace()
        {
            return place;
        }

        public void setPlace(String place)
        {
            this.place = place;
        }

        public String getRemark()
        {
            return remark;
        }

        public void setRemark(String remark)
        {
            this.remark = remark;
        }

        public long getCreate_time()
        {
            return create_time;
        }

        public void setCreate_time(long create_time)
        {
            this.create_time = create_time;
        }

        public long getExam_time()
        {
            return exam_time;
        }

        public void setExam_time(long exam_time)
        {
            this.exam_time = exam_time;
        }
    }
}

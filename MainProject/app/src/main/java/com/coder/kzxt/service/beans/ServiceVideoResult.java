package com.coder.kzxt.service.beans;


import com.coder.kzxt.base.beans.BaseBean;

import java.util.List;

/**
 * Created by MaShiZhao on 2017/6/21
 */

public class ServiceVideoResult extends BaseBean
{

    /**
     * code : 200
     * items : [{"id":23,"user_id":832695,"service_id":64,"sequence":17,"status":2,"title":"呜呜呜与","vid":"daee6f6ed746c158c2297575a7557299_d","time_length":60,"create_time":1497859388,"update_time":1497859391,"status_text":"已发布","format_lenght":"01:00","media_uri":"http://hls.videocc.net/daee6f6ed7/9/daee6f6ed746c158c2297575a7557299.m3u8"},{"id":24,"user_id":832695,"service_id":64,"sequence":18,"status":2,"title":"呜呜呜111","vid":"daee6f6ed74bd807dfd4f1ee8a809125_d","time_length":305,"create_time":1497859501,"update_time":1497859504,"status_text":"已发布","format_lenght":"05:05","media_uri":"http://hls.videocc.net/daee6f6ed7/5/daee6f6ed74bd807dfd4f1ee8a809125.m3u8"}]
     */

     private List<ItemsBean> items;

    public List<ItemsBean> getItems()
    {
        return items;
    }

    public void setItems(List<ItemsBean> items)
    {
        this.items = items;
    }

    public static class ItemsBean
    {
        /**
         * id : 23
         * user_id : 832695
         * service_id : 64
         * sequence : 17
         * status : 2
         * title : 呜呜呜与
         * vid : daee6f6ed746c158c2297575a7557299_d
         * time_length : 60
         * create_time : 1497859388
         * update_time : 1497859391
         * status_text : 已发布
         * format_lenght : 01:00
         * media_uri : http://hls.videocc.net/daee6f6ed7/9/daee6f6ed746c158c2297575a7557299.m3u8
         */

        private String id;
        private String user_id;
        private String service_id;
        private String sequence;
        private String status;
        private String title;
        private String vid;
        private String time_length;
        private String create_time;
        private String update_time;
        private String status_text;
        private String format_lenght;
        private String media_uri;

        public String getId()
        {
            return id;
        }

        public void setId(String id)
        {
            this.id = id;
        }

        public String getUser_id()
        {
            return user_id;
        }

        public void setUser_id(String user_id)
        {
            this.user_id = user_id;
        }

        public String getService_id()
        {
            return service_id;
        }

        public void setService_id(String service_id)
        {
            this.service_id = service_id;
        }

        public String getSequence()
        {
            return sequence;
        }

        public void setSequence(String sequence)
        {
            this.sequence = sequence;
        }

        public String getStatus()
        {
            return status;
        }

        public void setStatus(String status)
        {
            this.status = status;
        }

        public String getTitle()
        {
            return title;
        }

        public void setTitle(String title)
        {
            this.title = title;
        }

        public String getVid()
        {
            return vid;
        }

        public void setVid(String vid)
        {
            this.vid = vid;
        }

        public String getTime_length()
        {
            return time_length;
        }

        public void setTime_length(String time_length)
        {
            this.time_length = time_length;
        }

        public String getCreate_time()
        {
            return create_time;
        }

        public void setCreate_time(String create_time)
        {
            this.create_time = create_time;
        }

        public String getUpdate_time()
        {
            return update_time;
        }

        public void setUpdate_time(String update_time)
        {
            this.update_time = update_time;
        }

        public String getStatus_text()
        {
            return status_text;
        }

        public void setStatus_text(String status_text)
        {
            this.status_text = status_text;
        }

        public String getFormat_lenght()
        {
            return format_lenght;
        }

        public void setFormat_lenght(String format_lenght)
        {
            this.format_lenght = format_lenght;
        }

        public String getMedia_uri()
        {
            return media_uri;
        }

        public void setMedia_uri(String media_uri)
        {
            this.media_uri = media_uri;
        }
    }
}

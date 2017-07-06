package com.coder.kzxt.service.beans;

import com.coder.kzxt.base.beans.BaseBean;

import java.util.List;

/**
 * Created by MaShiZhao on 2017/6/9
 */

public class ServiceTeacherResult extends BaseBean
{
    private List<ItemsBean> items;

    public List<ItemsBean> getItems()
    {
        return items;
    }

    public void setItems(List<ItemsBean> items)
    {
        this.items = items;
    }

    public String getTeacherName()
    {
        if (items == null) return "";
        StringBuffer stringBuff = new StringBuffer();
        for (ItemsBean item : items)
        {
            stringBuff.append(item.getProfile().getNickname());
        }
        return stringBuff.toString();
    }

    public static class ItemsBean
    {
        /**
         * id : 189
         * service_id : 43
         * service_class_id : 43
         * user_id : 830740
         * type : 0
         * create_time : 1496293835
         * update_time : 1496293835
         * delete_time : null
         * profile : {"id":830740,"gender":1,"nickname":"姜浩然_学生","avatar":"http://html.gxy.com/www/theme/common/default/images/avatar/boy.png","birthday":"1970-01-01","desc":""}
         * user : {"id":830740,"email":"","mobile":"13671189310"}
         */

        private int id;
        private int service_id;
        private int service_class_id;
        private ProfileBean profile;

        public int getId()
        {
            return id;
        }

        public void setId(int id)
        {
            this.id = id;
        }

        public int getService_id()
        {
            return service_id;
        }

        public void setService_id(int service_id)
        {
            this.service_id = service_id;
        }

        public int getService_class_id()
        {
            return service_class_id;
        }

        public void setService_class_id(int service_class_id)
        {
            this.service_class_id = service_class_id;
        }

        public ProfileBean getProfile()
        {
            return profile;
        }

        public void setProfile(ProfileBean profile)
        {
            this.profile = profile;
        }

        public static class ProfileBean
        {
            /**
             * id : 830740
             * gender : 1
             * nickname : 姜浩然_学生
             * avatar : http://html.gxy.com/www/theme/common/default/images/avatar/boy.png
             * birthday : 1970-01-01
             * desc :
             */

            private int id;
            private String nickname;

            public int getId()
            {
                return id;
            }

            public void setId(int id)
            {
                this.id = id;
            }

            public String getNickname()
            {
                return nickname;
            }

            public void setNickname(String nickname)
            {
                this.nickname = nickname;
            }
        }
    }
}

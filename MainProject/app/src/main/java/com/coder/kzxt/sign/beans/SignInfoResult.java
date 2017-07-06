package com.coder.kzxt.sign.beans;

import com.coder.kzxt.base.beans.BaseBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MaShiZhao on 2017/5/24
 */

public class SignInfoResult extends BaseBean
{

    private List<SignInfoBean> items;

    public List<SignInfoBean> getItems()
    {
        return items;
    }

    public void setItems(List<SignInfoBean> items)
    {
        this.items = items;
    }


    public class SignInfoBean implements Serializable
    {


        /**
         * sign : {"id":907005,"gender":1,"nickname":"李寻欢","avatar":"http://html.gxy.com/www/theme/common/default/images/avatar/girl.png","big_avatar":"http://html.gxy.com/www/theme/common/default/images/avatar/big_boy.png","birthday":null,"desc":"123456","remark":"123456","view_count":16,"update_time":1494554188}
         * record : {}
         */

        private SignBean sign;
        private UserBean user;
        private RecordBean record;

        private Boolean isRecord;
        private int status;


        public Boolean getIsRecord()
        {
            return record.getUser_id() != null && record.getStatus() != sign.getStatus();
        }

        public int getStatus()
        {
            return record.getUser_id() == null ? sign.getStatus() : record.getStatus();
        }

        public long getTime()
        {
            return record.getUser_id() != null? record.getCreate_time() : sign.getSign_time();
        }

        public String getTagsIds()
        {
            StringBuilder stringBuffer = new StringBuilder();
            for (SignTagBean signTagBean : record.getTags())
            {
                stringBuffer.append(signTagBean.getId()).append(",");
            }
            return stringBuffer.toString();
        }

        public UserBean getUser()
        {
            return user;
        }

        public void setUser(UserBean user)
        {
            this.user = user;
        }

        public SignBean getSign()
        {
            return sign;
        }

        public void setSign(SignBean sign)
        {
            this.sign = sign;
        }

        public RecordBean getRecord()
        {
            return record;
        }

        public void setRecord(RecordBean record)
        {
            this.record = record;
        }


    }

    public static class SignBean implements Serializable
    {

        private int status;
        private int is_offline;
        private int is_range;
        private long sign_time;
        private String course_id;
        private String class_id;

        public String getCourse_id()
        {
            return course_id;
        }

        public void setCourse_id(String course_id)
        {
            this.course_id = course_id;
        }

        public String getClass_id()
        {
            return class_id;
        }

        public void setClass_id(String class_id)
        {
            this.class_id = class_id;
        }

        public int getIs_range()
        {
            return is_range;
        }

        public void setIs_range(int is_range)
        {
            this.is_range = is_range;
        }

        public int getIs_offline()
        {
            return is_offline;
        }

        public void setIs_offline(int is_offline)
        {
            this.is_offline = is_offline;
        }


        public int getStatus()
        {
            return status;
        }

        public void setStatus(int status)
        {
            this.status = status;
        }

        public long getSign_time()
        {
            return sign_time;
        }

        public void setSign_time(long sign_time)
        {
            this.sign_time = sign_time;
        }
    }


    public static class UserBean implements Serializable
    {

        private int id;
        private String nickname;
        private String avatar;

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

        public String getAvatar()
        {
            return avatar;
        }

        public void setAvatar(String avatar)
        {
            this.avatar = avatar;
        }
    }

    public static class RecordBean implements Serializable
    {
        private int status;
        private String user_id;
        private List<SignTagBean> tags;
        private long create_time;

        public long getCreate_time()
        {
            return create_time;
        }

        public void setCreate_time(long create_time)
        {
            this.create_time = create_time;
        }

        public String getUser_id()
        {
            return user_id;
        }

        public void setUser_id(String user_id)
        {
            this.user_id = user_id;
        }

        public List<SignTagBean> getTags()
        {
            if (tags == null)
            {
                return new ArrayList<>();
            }
            return tags;
        }

        public void setTags(List<SignTagBean> tags)
        {
            this.tags = tags;
        }

        public int getStatus()
        {
            return status;
        }

        public void setStatus(int status)
        {
            this.status = status;
        }
    }


}

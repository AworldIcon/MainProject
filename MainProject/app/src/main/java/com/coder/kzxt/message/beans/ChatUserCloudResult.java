package com.coder.kzxt.message.beans;

import com.coder.kzxt.base.beans.BaseBean;

/**
 * Created by MaShiZhao on 2017/4/26
 */

public class ChatUserCloudResult extends BaseBean
{

    private ChatUserCloud item;

    public ChatUserCloud getItem()
    {
        return item;
    }

    public void setItem(ChatUserCloud item)
    {
        this.item = item;
    }

    public class ChatUserCloud
    {

        /**
         * id : ID
         * email : 邮箱
         * mobile : 手机
         * profile : {"id":"用户id","gender":"性别:1.男 2.女","nickname":"用户名","avatar":"头像","big_avatar":"大头像","birthday":"生日","desc":"个人简介","remark":"备注"}
         */

        private String id;
        private String email;
        private String mobile;
        /**
         * id : 用户id
         * gender : 性别:1.男 2.女
         * nickname : 用户名
         * avatar : 头像
         * big_avatar : 大头像
         * birthday : 生日
         * desc : 个人简介
         * remark : 备注
         */

        private ProfileBean profile;

        public String getId()
        {
            return id;
        }

        public void setId(String id)
        {
            this.id = id;
        }

        public String getEmail()
        {
            return email;
        }

        public void setEmail(String email)
        {
            this.email = email;
        }

        public String getMobile()
        {
            return mobile;
        }

        public void setMobile(String mobile)
        {
            this.mobile = mobile;
        }

        public ProfileBean getProfile()
        {
            return profile;
        }

        public void setProfile(ProfileBean profile)
        {
            this.profile = profile;
        }

        public  class ProfileBean
        {
            private String id;
            private String gender;
            private String nickname;
            private String avatar;
            private String big_avatar;
            private String birthday;
            private String desc;
            private String remark;

            public String getId()
            {
                return id;
            }

            public void setId(String id)
            {
                this.id = id;
            }

            public String getGender()
            {
                return gender;
            }

            public void setGender(String gender)
            {
                this.gender = gender;
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

            public String getBig_avatar()
            {
                return big_avatar;
            }

            public void setBig_avatar(String big_avatar)
            {
                this.big_avatar = big_avatar;
            }

            public String getBirthday()
            {
                return birthday;
            }

            public void setBirthday(String birthday)
            {
                this.birthday = birthday;
            }

            public String getDesc()
            {
                return desc;
            }

            public void setDesc(String desc)
            {
                this.desc = desc;
            }

            public String getRemark()
            {
                return remark;
            }

            public void setRemark(String remark)
            {
                this.remark = remark;
            }
        }
    }
}

package com.coder.kzxt.base.beans;

/**
 * 用户信息
 * Created by MaShiZhao on 2017/3/20
 */

public class UserInfoBean
{


    /**
     * id : 用户id
     * mobile : 手机号
     * email : 邮箱
     * register_type : 注册方式:1.手机 2.邮箱
     * create_time : 注册时间
     * profile : {"id":"用户id","gender":"性别:1.男 2.女","nickname":"用户名","avatar":"头像","big_avatar":"大头像","birthday":"生日","desc":"个人简介","remark":"备注"}
     */

    private String id;
    private String mobile;
    private String email;
    private String register_type;
    private String create_time;
    private String is_teacher;
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

    public String getMobile()
    {
        return mobile;
    }

    public void setMobile(String mobile)
    {
        this.mobile = mobile;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getRegister_type()
    {
        return register_type;
    }

    public void setRegister_type(String register_type)
    {
        this.register_type = register_type;
    }

    public String getCreate_time()
    {
        return create_time;
    }

    public void setCreate_time(String create_time)
    {
        this.create_time = create_time;
    }

    public String getIs_teacher() {
        return is_teacher;
    }

    public void setIs_teacher(String is_teacher) {
        this.is_teacher = is_teacher;
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

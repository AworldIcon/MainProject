package com.coder.kzxt.service.beans;

import com.coder.kzxt.base.beans.BaseBean;

import java.io.Serializable;

/**
 * Created by MaShiZhao on 2017/6/20
 */

public class ServiceFormResult extends BaseBean
{


    /**
     * code : 200
     * item : {"id":201,"name":"etu","gender":1,"id_card":"130682199010106","qq":"153123336","email":"eyiooo@qq.com","date_birth":631123200,"mobile":"13069856558","education":"本科","major":"tyyyu","school":"guj","graduation_time":649436400,"experience":"","city":"安徽省/安庆市/枞阳县","user_id":907011,"create_time":1498463020,"update_time":1498463020}
     */

    private ItemBean item;



    public ItemBean getItem()
    {
        return item;
    }

    public void setItem(ItemBean item)
    {
        this.item = item;
    }


    public static class ItemBean  implements Serializable
    {
        /**
         * id : 201
         * name : etu
         * gender : 1
         * id_card : 130682199010106
         * qq : 153123336
         * email : eyiooo@qq.com
         * date_birth : 631123200
         * mobile : 13069856558
         * education : 本科
         * major : tyyyu
         * school : guj
         * graduation_time : 649436400
         * experience :
         * city : 安徽省/安庆市/枞阳县
         * user_id : 907011
         * create_time : 1498463020
         * update_time : 1498463020
         */

        private String id;
        private String item_id;
        private String name;
        private String gender;
        private String id_card;
        private String qq;
        private String email;
        private long date_birth;
        private String mobile;
        private String education;
        private String major;
        private String school;
        private long graduation_time;
        private String experience;
        private String date_birthStr;
        private String city;
        private String user_id;

        public ItemBean(String s)
        {
            this.item_id = s;
        }

        public String getGender()
        {
            if (gender.equals("1"))return "男";
            return "女";
        }
        public String getGenderString()
        {
            if (gender.equals("男"))return "1";
            return "2";
        }
        public String getGenderValue()
        {
             return gender;
        }

        public String getItem_id()
        {
            return item_id;
        }

        public void setItem_id(String item_id)
        {
            this.item_id = item_id;
        }

        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }


        public String getId_card()
        {
            return id_card;
        }

        public void setId_card(String id_card)
        {
            this.id_card = id_card;
        }

        public String getQq()
        {
            return qq;
        }

        public void setQq(String qq)
        {
            this.qq = qq;
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

        public String getEducation()
        {
            return education;
        }

        public void setEducation(String education)
        {
            this.education = education;
        }

        public String getMajor()
        {
            return major;
        }

        public void setMajor(String major)
        {
            this.major = major;
        }

        public String getSchool()
        {
            return school;
        }

        public void setSchool(String school)
        {
            this.school = school;
        }


        public String getExperience()
        {
            return experience;
        }

        public void setExperience(String experience)
        {
            this.experience = experience;
        }

        public String getCity()
        {
            return city;
        }

        public void setCity(String city)
        {
            this.city = city;
        }

        public String getId()
        {
            return id;
        }

        public void setId(String id)
        {
            this.id = id;
        }

        public void setGender(String gender)
        {
            this.gender = gender;
        }

        public long getDate_birth()
        {
            return date_birth;
        }

        public void setDate_birth(long date_birth)
        {
            this.date_birth = date_birth;
        }

        public long getGraduation_time()
        {
            return graduation_time;
        }

        public void setGraduation_time(long graduation_time)
        {
            this.graduation_time = graduation_time;
        }

        public String getUser_id()
        {
            return user_id;
        }

        public void setUser_id(String user_id)
        {
            this.user_id = user_id;
        }

        public void setDate_birthStr(String date_birthStr)
        {
            this.date_birthStr = date_birthStr;
        }

        public String getDate_birthStr()
        {
            return date_birthStr;
        }
    }
}

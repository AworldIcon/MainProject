package com.coder.kzxt.service.beans;

import com.coder.kzxt.base.beans.TextImageBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by MaShiZhao on 2017/6/9
 */

public class ServiceBean implements Serializable
{

           /* "id": "ID",
            "create_time": "创建时间",
            "title": "服务名",
            "desc": "简介",
            "price": "价格",
            "expiry_day": "过期天数",
            "is_alone": "是否可单独购买",
            "user_count": "加入人数",
            "user_id": "创建者",
            "update_time": "更新时间"*/

    private int id;
    private int service_id;
    private String title;
    private List<TextImageBean> desc;
    private String price;
    private int type;
    private String picture;
    private int expiry_day;
    private int state;
    private int is_alone;
    private int student_num;
    private int user_id;
    private int create_time;
    private int update_time;


    public void setService_id(int service_id)
    {
        this.service_id = service_id;
    }

    public int getService_id()
    {
        return service_id == 0 ? id : service_id;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
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

    public List<TextImageBean> getDesc()
    {
        return desc;
    }

    public void setDesc(List<TextImageBean> desc)
    {
        this.desc = desc;
    }

    public String getPrice()
    {
        return price;
    }

    public void setPrice(String price)
    {
        this.price = price;
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public String getPicture()
    {
        return picture;
    }

    public void setPicture(String picture)
    {
        this.picture = picture;
    }

    public int getExpiry_day()
    {
        return expiry_day;
    }

    public void setExpiry_day(int expiry_day)
    {
        this.expiry_day = expiry_day;
    }

    public int getState()
    {
        return state;
    }

    public void setState(int state)
    {
        this.state = state;
    }

    public int getIs_alone()
    {
        return is_alone;
    }

    public void setIs_alone(int is_alone)
    {
        this.is_alone = is_alone;
    }

    public int getStudent_num()
    {
        return student_num;
    }

    public void setStudent_num(int student_num)
    {
        this.student_num = student_num;
    }

    public int getUser_id()
    {
        return user_id;
    }

    public void setUser_id(int user_id)
    {
        this.user_id = user_id;
    }

    public int getCreate_time()
    {
        return create_time;
    }

    public void setCreate_time(int create_time)
    {
        this.create_time = create_time;
    }

    public int getUpdate_time()
    {
        return update_time;
    }

    public void setUpdate_time(int update_time)
    {
        this.update_time = update_time;
    }
}

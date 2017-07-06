package com.coder.kzxt.sign.beans;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MaShiZhao on 2017/5/23
 */

public class SignBean implements Serializable
{


    /**
     * id : 12
     * course_id : 20875
     * class_id : 22735
     * time : 1000
     * range : 12
     * begin_time : 1495432075
     * end_time : 1495504824
     * user_id : 907004
     * create_time : 1495432074
     * update_time : 1495504824
     * delete_time : 1495504824
     */

    // id 和 sign_id 可能都是签到 根据具体解析类分析
    private String id;
    private String sign_id;
    private String course_id;
    private String course_name;
    private String class_id;
    private long time;
    private int range;
    private long begin_time;
    private long end_time;
    private String user_id;
    private String sign_num;
    private String unsign_num;
    private String num;
    private long create_time;
    private long update_time;
    private String delete_time;
    private String address;
    private int status;
    private long odd;
    private int is_offline;
    private int is_range;
    private String latitude;
    private String longitude;
    private long sign_time;
    private List<SignTagBean> tags;


    public int getSignProgress()
    {
        if (TextUtils.isEmpty(num) || num.equals("0")) return 100;
        if (TextUtils.isEmpty(sign_num) || sign_num.equals("0")) return 0;
        return (int) Math.round(((Double.valueOf(sign_num) / Double.valueOf(num)) * 100));
    }

    public int getUnsignProgress()
    {
        if (TextUtils.isEmpty(num) || num.equals("0")) return 0;
        if (TextUtils.isEmpty(unsign_num) || unsign_num.equals("0")) return 0;
        return (int) Math.round(((Double.valueOf(unsign_num) / Double.valueOf(num)) * 100));
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

    public String getSign_id()
    {
        return sign_id;
    }

    public void setSign_id(String sign_id)
    {
        this.sign_id = sign_id;
    }

    public int getIs_range()
    {
        return is_range;
    }

    public void setIs_range(int is_range)
    {
        this.is_range = is_range;
    }

    public String getCourse_name()
    {
        return course_name;
    }

    public void setCourse_name(String course_name)
    {
        this.course_name = course_name;
    }

    public long getSign_time()
    {
        return sign_time;
    }

    public void setSign_time(long sign_time)
    {
        this.sign_time = sign_time;
    }

    public String getLongitude()
    {
        return longitude;
    }

    public void setLongitude(String longitude)
    {
        this.longitude = longitude;
    }

    public String getLatitude()
    {
        return latitude;
    }

    public void setLatitude(String latitude)
    {
        this.latitude = latitude;
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

    public String getNum()
    {
        if (num == null)
        {
            return "";
        }
        return num;
    }

    public void setNum(String num)
    {
        this.num = num;
    }

    public long getOdd()
    {
        return odd;
    }

    public void setOdd(long odd)
    {
        this.odd = odd;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getSign_num()
    {
        if (TextUtils.isEmpty(sign_num))
        {
            return "0";
        }
        return sign_num;
    }

    public void setSign_num(String sign_num)
    {
        this.sign_num = sign_num;
    }

    public String getUnsign_num()
    {
        return unsign_num;
    }

    public void setUnsign_num(String unsign_num)
    {
        this.unsign_num = unsign_num;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

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

    public long getTime()
    {
        return time;
    }

    public void setTime(long time)
    {
        this.time = time;
    }

    public int getRange()
    {
        return range;
    }

    public void setRange(int range)
    {
        this.range = range;
    }

    public long getBegin_time()
    {
        return begin_time;
    }

    public void setBegin_time(long begin_time)
    {
        this.begin_time = begin_time;
    }

    public long getEnd_time()
    {
        return end_time;
    }

    public void setEnd_time(long end_time)
    {
        this.end_time = end_time;
    }

    public String getUser_id()
    {
        return user_id;
    }

    public void setUser_id(String user_id)
    {
        this.user_id = user_id;
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

    public Object getDelete_time()
    {
        return delete_time;
    }

    public void setDelete_time(String delete_time)
    {
        this.delete_time = delete_time;
    }
}

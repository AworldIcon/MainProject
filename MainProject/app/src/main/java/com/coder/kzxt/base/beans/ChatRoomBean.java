package com.coder.kzxt.base.beans;

/**
 * Created by MaShiZhao on 2017/4/21
 * 聊天室信息
 */

public class ChatRoomBean
{

    private String course_class_id;
    private String group_id;
    // 0 所有人 1 需要加入课程
    private String access_permission;
    private String status;

    public String getCourse_class_id()
    {
        return course_class_id;
    }

    public void setCourse_class_id(String course_class_id)
    {
        this.course_class_id = course_class_id;
    }

    public String getGroup_id()
    {
        return group_id;
    }

    public void setGroup_id(String group_id)
    {
        this.group_id = group_id;
    }

    public String getAccess_permission()
    {
        return access_permission;
    }

    public void setAccess_permission(String access_permission)
    {
        this.access_permission = access_permission;
    }

    public String getStatus()
    {
        if (status == null)
        {
            return "1";
        }
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }
}

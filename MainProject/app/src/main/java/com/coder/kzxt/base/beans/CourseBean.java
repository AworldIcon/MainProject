package com.coder.kzxt.base.beans;

/**
 * 课程对象
 * Created by MaShiZhao on 2017/3/21
 */

public class CourseBean
{

    /**
     * id : ID
     * title : 课程标题
     * price : 课程价格
     * small_pic : 课程小图
     * middle_pic : 课程中图
     * large_pic : 课程大图
     * desc : 课程简介
     * source : 课程来源
     * custom_teacher_title : 自定义课程教师团队选项卡标题
     * custom_desc_title : 自定义课程简介选项卡标题
     * create_time : 课程创建时间
     * lesson_num : 课时
     * is_free :是否免费
     */

    private String id;
    private String title;
    private String price;
    private String small_pic;
    private String middle_pic;
    private String large_pic;
    private String desc;
    private String source;
    private String custom_teacher_title;
    private String custom_desc_title;
    private String create_time;
    private String lesson_num;
    private String is_free;
    private String student_num;
    private float grade;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title+"";
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getPrice()
    {
        return price;
    }

    public void setPrice(String price)
    {
        this.price = price;
    }

    public String getSmall_pic()
    {
        return small_pic;
    }

    public void setSmall_pic(String small_pic)
    {
        this.small_pic = small_pic;
    }

    public String getMiddle_pic()
    {
        return middle_pic;
    }

    public void setMiddle_pic(String middle_pic)
    {
        this.middle_pic = middle_pic;
    }

    public String getLarge_pic()
    {
        return large_pic;
    }

    public void setLarge_pic(String large_pic)
    {
        this.large_pic = large_pic;
    }

    public String getDesc()
    {
        return desc;
    }

    public void setDesc(String desc)
    {
        this.desc = desc;
    }

    public String getSource()
    {
        return source;
    }

    public void setSource(String source)
    {
        this.source = source;
    }

    public String getCustom_teacher_title()
    {
        return custom_teacher_title;
    }

    public void setCustom_teacher_title(String custom_teacher_title)
    {
        this.custom_teacher_title = custom_teacher_title;
    }

    public String getCustom_desc_title()
    {
        return custom_desc_title;
    }

    public void setCustom_desc_title(String custom_desc_title)
    {
        this.custom_desc_title = custom_desc_title;
    }

    public String getCreate_time()
    {
        return create_time;
    }

    public void setCreate_time(String create_time)
    {
        this.create_time = create_time;
    }

    public String getLesson_num()
    {
        return lesson_num;
    }

    public void setLesson_num(String lesson_num)
    {
        this.lesson_num = lesson_num;
    }

    public String getIs_free() {
        return is_free;
    }

    public void setIs_free(String is_free) {
        this.is_free = is_free;
    }

    public float getGrade() {
        return grade;
    }

    public String getStudent_num() {
        return student_num;
    }

    public void setStudent_num(String student_num) {
        this.student_num = student_num;
    }

    public void setGrade(float grade) {
        this.grade = grade;
    }
}

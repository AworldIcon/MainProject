package com.coder.kzxt.message.beans;

import com.coder.kzxt.base.beans.BaseBean;

import java.util.List;

/**
 * Created by MaShiZhao on 2017/4/8
 */

public class ChatGroupBean extends BaseBean
{

    /**
     * course_class_id : 课程班级id
     * group_id : group_id
     * course_class : {"id":"课程班级id","class_name":"课程班级名称","course":{"id":"课程id","title":"课程标题"}}
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
        private String course_class_id;
        private String group_id;
        private String status;

        public Boolean getStatus()
        {
            if (status != null && status.equals("0"))
                return true;
            return false;
        }

        public void setStatus(String status)
        {
            this.status = status;
        }

        /**
         * id : 课程班级id
         * class_name : 课程班级名称
         * course : {"id":"课程id","title":"课程标题"}
         * status 0 关闭 1开启
         */


        private CourseClassBean course_class;

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

        public CourseClassBean getCourse_class()
        {
            return course_class;
        }

        public void setCourse_class(CourseClassBean course_class)
        {
            this.course_class = course_class;
        }

        public static class CourseClassBean
        {
            private String id;
            private String class_name;
            /**
             * id : 课程id
             * title : 课程标题
             */

            private CourseBean course;

            public String getId()
            {
                return id;
            }

            public void setId(String id)
            {
                this.id = id;
            }

            public String getClass_name()
            {
                return class_name;
            }

            public void setClass_name(String class_name)
            {
                this.class_name = class_name;
            }

            public CourseBean getCourse()
            {
                return course;
            }

            public void setCourse(CourseBean course)
            {
                this.course = course;
            }

            public static class CourseBean
            {
                private String id;
                private String title;

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
                    return title;
                }

                public void setTitle(String title)
                {
                    this.title = title;
                }
            }
        }
    }
}

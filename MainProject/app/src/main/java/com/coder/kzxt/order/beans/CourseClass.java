package com.coder.kzxt.order.beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 课程下关联的班级
 * Created by wangtingshun on 2017/4/11.
 */

public class CourseClass {

    private String code;
    private String message;
    private ArrayList<ClassBean> items;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<ClassBean> getItems() {
        return items;
    }

    public void setItems(ArrayList<ClassBean> items) {
        this.items = items;
    }


    public class ClassBean implements Serializable{

        private String id;
        private String class_name;
        private String user_id;
        private String course_id;
        private String start_time;
        private String end_time;
        private String apply_end_time;
        private String desc;
        private String price;
        private String status;
        private String is_default;
        private String sort;
        private String hidden;
        private String user_count;
        private String user_limit;
        private String apply_status;
        private String create_time;
        private String update_time;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getClass_name() {
            return class_name;
        }

        public void setClass_name(String class_name) {
            this.class_name = class_name;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getCourse_id() {
            return course_id;
        }

        public void setCourse_id(String course_id) {
            this.course_id = course_id;
        }

        public String getStart_time() {
            return start_time;
        }

        public void setStart_time(String start_time) {
            this.start_time = start_time;
        }

        public String getEnd_time() {
            return end_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }

        public String getApply_end_time() {
            return apply_end_time;
        }

        public void setApply_end_time(String apply_end_time) {
            this.apply_end_time = apply_end_time;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getIs_default() {
            return is_default;
        }

        public void setIs_default(String is_default) {
            this.is_default = is_default;
        }

        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }

        public String getHidden() {
            return hidden;
        }

        public void setHidden(String hidden) {
            this.hidden = hidden;
        }

        public String getUser_count() {
            return user_count;
        }

        public void setUser_count(String user_count) {
            this.user_count = user_count;
        }

        public String getUser_limit() {
            return user_limit;
        }

        public void setUser_limit(String user_limit) {
            this.user_limit = user_limit;
        }

        public String getApply_status() {
            return apply_status;
        }

        public void setApply_status(String apply_status) {
            this.apply_status = apply_status;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(String update_time) {
            this.update_time = update_time;
        }

        @Override
        public String toString() {
            return "ClassBean{" +
                    "id='" + id + '\'' +
                    ", class_name='" + class_name + '\'' +
                    ", user_id='" + user_id + '\'' +
                    ", course_id='" + course_id + '\'' +
                    ", start_time='" + start_time + '\'' +
                    ", end_time='" + end_time + '\'' +
                    ", apply_end_time='" + apply_end_time + '\'' +
                    ", desc='" + desc + '\'' +
                    ", price='" + price + '\'' +
                    ", status='" + status + '\'' +
                    ", is_default='" + is_default + '\'' +
                    ", sort='" + sort + '\'' +
                    ", hidden='" + hidden + '\'' +
                    ", user_count='" + user_count + '\'' +
                    ", user_limit='" + user_limit + '\'' +
                    ", apply_status='" + apply_status + '\'' +
                    ", create_time='" + create_time + '\'' +
                    ", update_time='" + update_time + '\'' +
                    '}';
        }
    }

}

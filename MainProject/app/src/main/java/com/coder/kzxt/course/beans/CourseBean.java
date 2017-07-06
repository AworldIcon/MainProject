package com.coder.kzxt.course.beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 课程bean
 * Created by Administrator on 2017/3/10.
 */

public class CourseBean {

    private int code;
    private String message;

    private ArrayList<Course> items;

    private Paginate paginate;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public ArrayList<Course> getItems() {
        return items;
    }

    public void setItems(ArrayList<Course> items) {
        this.items = items;
    }

    public Paginate getPaginate() {
        return paginate;
    }

    public void setPaginate(Paginate paginate) {
        this.paginate = paginate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public class Course implements Serializable {

        private String id;
        private String title;
        private String price;
        private String status;
        private String is_free;
        private String expiry_day;
        private String small_pic;
        private String middle_pic;
        private String large_pic;
        private String desc;
        private String source;
        private String custom_teacher_title;
        private String custom_desc_title;
        private String create_time;
        private String lesson_num;
        private String update_time;


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getIs_free() {
            return is_free;
        }

        public void setIs_free(String is_free) {
            this.is_free = is_free;
        }

        public String getExpiry_day() {
            return expiry_day;
        }

        public void setExpiry_day(String expiry_day) {
            this.expiry_day = expiry_day;
        }

        public String getSmall_pic() {
            return small_pic;
        }

        public void setSmall_pic(String small_pic) {
            this.small_pic = small_pic;
        }

        public String getMiddle_pic() {
            return middle_pic;
        }

        public void setMiddle_pic(String middle_pic) {
            this.middle_pic = middle_pic;
        }

        public String getLarge_pic() {
            return large_pic;
        }

        public void setLarge_pic(String large_pic) {
            this.large_pic = large_pic;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getCustom_teacher_title() {
            return custom_teacher_title;
        }

        public void setCustom_teacher_title(String custom_teacher_title) {
            this.custom_teacher_title = custom_teacher_title;
        }

        public String getCustom_desc_title() {
            return custom_desc_title;
        }

        public void setCustom_desc_title(String custom_desc_title) {
            this.custom_desc_title = custom_desc_title;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getLesson_num() {
            return lesson_num;
        }

        public void setLesson_num(String lesson_num) {
            this.lesson_num = lesson_num;
        }

        public String getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(String update_time) {
            this.update_time = update_time;
        }



        @Override
        public String toString() {
            return "Course{" +
                    "id='" + id + '\'' +
                    ", title='" + title + '\'' +
                    ", price='" + price + '\'' +
                    ", status='" + status + '\'' +
                    ", is_free='" + is_free + '\'' +
                    ", expiry_day='" + expiry_day + '\'' +
                    ", small_pic='" + small_pic + '\'' +
                    ", middle_pic='" + middle_pic + '\'' +
                    ", large_pic='" + large_pic + '\'' +
                    ", desc='" + desc + '\'' +
                    ", source='" + source + '\'' +
                    ", custom_teacher_title='" + custom_teacher_title + '\'' +
                    ", custom_desc_title='" + custom_desc_title + '\'' +
                    ", create_time='" + create_time + '\'' +
                    ", lesson_num='" + lesson_num + '\'' +
                    ", update_time='" + update_time + '\'' +
                    '}';
        }
    }

    public class Paginate {
        //当前页数
        private int currentPage;
        //每页记录数
        private int pageSize;
        //总页数
        private int pageNum;
        //总记录数
        private int totalNum;

        public int getCurrentPage() {
            return currentPage;
        }

        public void setCurrentPage(int currentPage) {
            this.currentPage = currentPage;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getPageNum() {
            return pageNum;
        }

        public void setPageNum(int pageNum) {
            this.pageNum = pageNum;
        }

        public int getTotalNum() {
            return totalNum;
        }

        public void setTotalNum(int totalNum) {
            this.totalNum = totalNum;
        }
    }

}

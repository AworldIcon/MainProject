package com.coder.kzxt.question.beans;

import com.coder.kzxt.base.beans.TextImageBean;

import java.util.List;

/**
 * Created by pc on 2017/4/19.
 */

public class QuestionsDetailBean {

    /**
     * code : 200
     * message : ok
     * item : {"id":"问答id","course_id":"课程id","title":"问答标题","content":"问答内容","user_id":"用户ID","device":"提问使用的设备  0: pc 1:android 2: iphone","role":"身份，1老师，2学生","create_time":"创建时间"}
     */

    private String code;
    private String message;
    private ItemBean item;

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

    public ItemBean getItem() {
        return item;
    }

    public void setItem(ItemBean item) {
        this.item = item;
    }

    public static class ItemBean {
        /**
         * id : 问答id
         * course_id : 课程id
         * title : 问答标题
         * content : 问答内容
         * user_id : 用户ID
         * device : 提问使用的设备  0: pc 1:android 2: iphone
         * role : 身份，1老师，2学生
         * create_time : 创建时间
         */

        private String id;
        private String course_id;
        private String title;
        private List<TextImageBean> content;
        private String user_id;
        private String device;
        private String role;
        private int is_teachers;
        private long create_time;
        private int is_praise;//点赞
        private int is_follow;//收藏
        private int is_elite;//精华问答
        private int best_answer_id;//问题是否解决，0未解决
        private int post_num;

        public int getPost_num() {
            return post_num;
        }

        public void setPost_num(int post_num) {
            this.post_num = post_num;
        }

        public int getBest_answer_id() {
            return best_answer_id;
        }

        public void setBest_answer_id(int best_answer_id) {
            this.best_answer_id = best_answer_id;
        }

        public int getIs_elite() {
            return is_elite;
        }

        public void setIs_elite(int is_elite) {
            this.is_elite = is_elite;
        }

        public int getIs_praise() {
            return is_praise;
        }

        public void setIs_praise(int is_praise) {
            this.is_praise = is_praise;
        }

        public int getIs_follow() {
            return is_follow;
        }

        public void setIs_follow(int is_follow) {
            this.is_follow = is_follow;
        }

        public int getIs_teachers() {
            return is_teachers;
        }

        public void setIs_teachers(int is_teachers) {
            this.is_teachers = is_teachers;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCourse_id() {
            return course_id;
        }

        public void setCourse_id(String course_id) {
            this.course_id = course_id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<TextImageBean> getContent() {
            return content;
        }

        public void setContent(List<TextImageBean> content) {
            this.content = content;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getDevice() {
            return device;
        }

        public void setDevice(String device) {
            this.device = device;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public long getCreate_time() {
            return create_time;
        }

        public void setCreate_time(long create_time) {
            this.create_time = create_time;
        }

    }
}

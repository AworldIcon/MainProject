package com.coder.kzxt.buildwork.entity;

import java.util.List;

/**
 * Created by pc on 2017/3/27.
 */

public class WorkOrderBean {
    /**
     * code : 200
     * message : ok
     * item : {"id":219,"course_id":4,"title":"待批阅已过期","type":1,"question_count":1,"question_type_sort":[1,2,3,4,5],"score":100,"score_type":2,"mode":1,"status":3,"user_id":916412,"create_time":1490348521,"trash":0,"published_to_class":[1]}
     */

    private int code;
    private String message;
    private ItemBean item;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
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
         * id : 219
         * course_id : 4
         * title : 待批阅已过期
         * type : 1
         * question_count : 1
         * question_type_sort : [1,2,3,4,5]
         * score : 100
         * score_type : 2
         * mode : 1
         * status : 3
         * user_id : 916412
         * create_time : 1490348521
         * trash : 0
         * published_to_class : [1]
         */

        private int id;
        private int course_id;
        private String title;
        private int type;
        private int question_count;
        private String score;
        private int score_type;
        private int mode;
        private int status;
        private int user_id;
        private Long create_time;
        private int trash;
        private List<Integer> question_type_sort;
        private List<Integer> published_to_class;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getCourse_id() {
            return course_id;
        }

        public void setCourse_id(int course_id) {
            this.course_id = course_id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getQuestion_count() {
            return question_count;
        }

        public void setQuestion_count(int question_count) {
            this.question_count = question_count;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public int getScore_type() {
            return score_type;
        }

        public void setScore_type(int score_type) {
            this.score_type = score_type;
        }

        public int getMode() {
            return mode;
        }

        public void setMode(int mode) {
            this.mode = mode;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public Long getCreate_time() {
            return create_time;
        }

        public void setCreate_time(Long create_time) {
            this.create_time = create_time;
        }

        public int getTrash() {
            return trash;
        }

        public void setTrash(int trash) {
            this.trash = trash;
        }

        public List<Integer> getQuestion_type_sort() {
            return question_type_sort;
        }

        public void setQuestion_type_sort(List<Integer> question_type_sort) {
            this.question_type_sort = question_type_sort;
        }

        public List<Integer> getPublished_to_class() {
            return published_to_class;
        }

        public void setPublished_to_class(List<Integer> published_to_class) {
            this.published_to_class = published_to_class;
        }
    }
}

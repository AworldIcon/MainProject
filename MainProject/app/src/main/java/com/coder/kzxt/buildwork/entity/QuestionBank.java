package com.coder.kzxt.buildwork.entity;

import java.util.List;

/**
 * Created by pc on 2017/2/16.
 */

public class QuestionBank {

    /**
     * code : 200
     * message : ok
     * paginate : {"currentPage":1,"pageSize":20,"pageNum":10,"totalNum":199}
     * items : [{"id":344,"type":3,"title":"我是填空题","difficulty":2,"user_id":916412,"create_time":1491458682,"trash":0,"creator":{"nickname":"Ethan","id":916412},"difficulty_text":"一般","type_text":"填空"},{"id":343,"type":2,"title":"Jhgfds","difficulty":3,"user_id":916412,"create_time":1491454913,"trash":0,"creator":{"nickname":"Ethan","id":916412},"difficulty_text":"困难","type_text":"多选"},{"id":342,"type":5,"title":"Hgfd","difficulty":1,"user_id":916412,"create_time":1491454892,"trash":0,"creator":{"nickname":"Ethan","id":916412},"difficulty_text":"简单","type_text":"简答"},{"id":340,"type":2,"title":"多选题目","difficulty":1,"user_id":916412,"create_time":1491454636,"trash":0,"creator":{"nickname":"Ethan","id":916412},"difficulty_text":"简单","type_text":"多选"},{"id":339,"type":5,"title":"Eee","difficulty":1,"user_id":916412,"create_time":1491454614,"trash":0,"creator":{"nickname":"Ethan","id":916412},"difficulty_text":"简单","type_text":"简答"},{"id":338,"type":1,"title":"单选题目","difficulty":1,"user_id":916412,"create_time":1491454542,"trash":0,"creator":{"nickname":"Ethan","id":916412},"difficulty_text":"简单","type_text":"单选"},{"id":337,"type":4,"title":"判断","difficulty":2,"user_id":916412,"create_time":1491454409,"trash":0,"creator":{"nickname":"Ethan","id":916412},"difficulty_text":"一般","type_text":"判断"},{"id":336,"type":5,"title":"Eeeeee","difficulty":1,"user_id":916412,"create_time":1491454045,"trash":0,"creator":{"nickname":"Ethan","id":916412},"difficulty_text":"简单","type_text":"简答"},{"id":335,"type":5,"title":"Ffff","difficulty":3,"user_id":916412,"create_time":1491453911,"trash":0,"creator":{"nickname":"Ethan","id":916412},"difficulty_text":"困难","type_text":"简答"},{"id":334,"type":1,"title":"Rrrrr","difficulty":1,"user_id":916412,"create_time":1491453896,"trash":0,"creator":{"nickname":"Ethan","id":916412},"difficulty_text":"简单","type_text":"单选"},{"id":333,"type":1,"title":"Qqqqqq","difficulty":1,"user_id":916412,"create_time":1491453881,"trash":0,"creator":{"nickname":"Ethan","id":916412},"difficulty_text":"简单","type_text":"单选"},{"id":324,"type":5,"title":"U76y5t4re3w2q","difficulty":2,"user_id":916412,"create_time":1491446128,"trash":0,"creator":{"nickname":"Ethan","id":916412},"difficulty_text":"一般","type_text":"简答"},{"id":323,"type":3,"title":"Jyhtrew()jhgfde()w","difficulty":3,"user_id":916412,"create_time":1491446115,"trash":0,"creator":{"nickname":"Ethan","id":916412},"difficulty_text":"困难","type_text":"填空"},{"id":322,"type":4,"title":"Iuytriuytreewq?","difficulty":2,"user_id":916412,"create_time":1491446058,"trash":0,"creator":{"nickname":"Ethan","id":916412},"difficulty_text":"一般","type_text":"判断"},{"id":321,"type":2,"title":"2345","difficulty":1,"user_id":916412,"create_time":1491446041,"trash":0,"creator":{"nickname":"Ethan","id":916412},"difficulty_text":"简单","type_text":"多选"},{"id":320,"type":1,"title":"我是单选题","difficulty":3,"user_id":916412,"create_time":1491446008,"trash":0,"creator":{"nickname":"Ethan","id":916412},"difficulty_text":"困难","type_text":"单选"},{"id":319,"type":3,"title":"aa","difficulty":1,"user_id":916412,"create_time":1491444219,"trash":0,"creator":{"nickname":"Ethan","id":916412},"difficulty_text":"简单","type_text":"填空"},{"id":318,"type":2,"title":"bbb","difficulty":1,"user_id":916412,"create_time":1491444207,"trash":0,"creator":{"nickname":"Ethan","id":916412},"difficulty_text":"简单","type_text":"多选"},{"id":317,"type":1,"title":"321","difficulty":1,"user_id":916412,"create_time":1491444161,"trash":0,"creator":{"nickname":"Ethan","id":916412},"difficulty_text":"简单","type_text":"单选"},{"id":316,"type":1,"title":"bbb","difficulty":1,"user_id":916412,"create_time":1491444026,"trash":0,"creator":{"nickname":"Ethan","id":916412},"difficulty_text":"简单","type_text":"单选"}]
     */

    private int code;
    private String message;
    private PaginateBean paginate;
    private List<ItemsBean> items;

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

    public PaginateBean getPaginate() {
        return paginate;
    }

    public void setPaginate(PaginateBean paginate) {
        this.paginate = paginate;
    }

    public List<ItemsBean> getItems() {
        return items;
    }

    public void setItems(List<ItemsBean> items) {
        this.items = items;
    }

    public static class PaginateBean {
        /**
         * currentPage : 1
         * pageSize : 20
         * pageNum : 10
         * totalNum : 199
         */

        private int currentPage;
        private int pageSize;
        private int pageNum;
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

    public static class ItemsBean {
        /**
         * id : 344
         * type : 3
         * title : 我是填空题
         * difficulty : 2
         * user_id : 916412
         * create_time : 1491458682
         * trash : 0
         * creator : {"nickname":"Ethan","id":916412}
         * difficulty_text : 一般
         * type_text : 填空
         */

        private int id;
        private int type;
        private String title;
        private int difficulty;
        private int user_id;
        private Long create_time;
        private int trash;
        private CreatorBean creator;
        private String difficulty_text;
        private String type_text;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getDifficulty() {
            return difficulty;
        }

        public void setDifficulty(int difficulty) {
            this.difficulty = difficulty;
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

        public CreatorBean getCreator() {
            return creator;
        }

        public void setCreator(CreatorBean creator) {
            this.creator = creator;
        }

        public String getDifficulty_text() {
            return difficulty_text;
        }

        public void setDifficulty_text(String difficulty_text) {
            this.difficulty_text = difficulty_text;
        }

        public String getType_text() {
            return type_text;
        }

        public void setType_text(String type_text) {
            this.type_text = type_text;
        }

        public static class CreatorBean {
            /**
             * nickname : Ethan
             * id : 916412
             */

            private String nickname;
            private int id;

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }
        }
    }
}

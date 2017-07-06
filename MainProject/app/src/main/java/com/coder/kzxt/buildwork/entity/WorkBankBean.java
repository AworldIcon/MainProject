package com.coder.kzxt.buildwork.entity;

import java.util.List;

/**
 * Created by pc on 2017/3/21.
 */

public class WorkBankBean {
    /**
     * code : 200
     * message : ok
     * items : [{"id":"试卷id","title":"试卷标题","question_count":"题目数量","score":"总分","score_type":"总分算法:1.自定义 2.百分制","mode":"组卷规则:1.手动 2.随机","status":"状态:1.创建中 2.未发布 3.已发布","user_id":"创建人id","creator":{"nickname":"创建人昵称"},"create_time":"创建时间","trash":"1.已回收 0. 正常"}]
     * paginate : {"currentPage":"当前页数","pageSize":"每页记录数","pageNum":"总页数","totalNum":"总记录数"}
     */

    private String code;
    private String message;
    private PaginateBean paginate;
    private List<ItemsBean> items;

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
         * currentPage : 当前页数
         * pageSize : 每页记录数
         * pageNum : 总页数
         * totalNum : 总记录数
         */

        private String currentPage;
        private String pageSize;
        private String pageNum;
        private String totalNum;

        public String getCurrentPage() {
            return currentPage;
        }

        public void setCurrentPage(String currentPage) {
            this.currentPage = currentPage;
        }

        public String getPageSize() {
            return pageSize;
        }

        public void setPageSize(String pageSize) {
            this.pageSize = pageSize;
        }

        public String getPageNum() {
            return pageNum;
        }

        public void setPageNum(String pageNum) {
            this.pageNum = pageNum;
        }

        public String getTotalNum() {
            return totalNum;
        }

        public void setTotalNum(String totalNum) {
            this.totalNum = totalNum;
        }
    }

    public static class ItemsBean {
        /**
         * id : 试卷id
         * title : 试卷标题
         * question_count : 题目数量
         * score : 总分
         * score_type : 总分算法:1.自定义 2.百分制
         * mode : 组卷规则:1.手动 2.随机
         * status : 状态:1.创建中 2.未发布 3.已发布
         * user_id : 创建人id
         * creator : {"nickname":"创建人昵称"}
         * create_time : 创建时间
         * trash : 1.已回收 0. 正常
         */

        private String id;
        private String title;
        private String question_count;
        private String score;
        private String score_type;
        private String mode;
        private String status;
        private String user_id;
        private CreatorBean creator;
        private String create_time;
        private String trash;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getQuestion_count() {
            return question_count;
        }

        public void setQuestion_count(String question_count) {
            this.question_count = question_count;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public String getScore_type() {
            return score_type;
        }

        public void setScore_type(String score_type) {
            this.score_type = score_type;
        }

        public String getMode() {
            return mode;
        }

        public void setMode(String mode) {
            this.mode = mode;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public CreatorBean getCreator() {
            return creator;
        }

        public void setCreator(CreatorBean creator) {
            this.creator = creator;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getTrash() {
            return trash;
        }

        public void setTrash(String trash) {
            this.trash = trash;
        }

        public static class CreatorBean {
            /**
             * nickname : 创建人昵称
             */

            private String nickname;

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }
        }
    }
}

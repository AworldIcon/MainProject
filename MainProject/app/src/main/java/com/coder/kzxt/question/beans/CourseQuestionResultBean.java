package com.coder.kzxt.question.beans;

import com.coder.kzxt.base.beans.TextImageBean;

import java.util.List;

/**
 * Created by MaShiZhao on 2017/4/10
 */

public class CourseQuestionResultBean
{


    /**
     * code : 200
     * message : ok
     * items : [{"id":"id","course_id":"课程ID","is_elite":"是否精华","title":"话题标题","content":"话题内容","user_id":"用户ID","post_num":"回答数","hit_num":"查看数","follow_num":"关注数","best_answer_id":"最佳答案(回答id)","praise_num":"点赞数","role":"创建者身份，1老师，2学生","create_time":"创建时间","user":{"id":"用户id","avatar":"用户头像","nickname":"用户名称"}}]
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
        private int pageNum;
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

        public int getPageNum() {
            return pageNum;
        }

        public void setPageNum(int pageNum) {
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
         * id : id
         * course_id : 课程ID
         * is_elite : 是否精华
         * title : 话题标题
         * content : 话题内容
         * user_id : 用户ID
         * post_num : 回答数
         * hit_num : 查看数
         * follow_num : 关注数
         * best_answer_id : 最佳答案(回答id)
         * praise_num : 点赞数
         * role : 创建者身份，1老师，2学生
         * create_time : 创建时间
         * user : {"id":"用户id","avatar":"用户头像","nickname":"用户名称"}
         */

        private int id;
        private String course_id;
        private String is_elite;
        private String title;
        private List<TextImageBean> content;
        private String user_id;
        private String post_num;
        private String hit_num;
        private String follow_num;
        private String best_answer_id;
        private String praise_num;
        private String role;
        private Long create_time;
        private UserBean user;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCourse_id() {
            return course_id;
        }

        public void setCourse_id(String course_id) {
            this.course_id = course_id;
        }

        public String getIs_elite() {
            return is_elite;
        }

        public void setIs_elite(String is_elite) {
            this.is_elite = is_elite;
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

        public String getPost_num() {
            return post_num;
        }

        public void setPost_num(String post_num) {
            this.post_num = post_num;
        }

        public String getHit_num() {
            return hit_num;
        }

        public void setHit_num(String hit_num) {
            this.hit_num = hit_num;
        }

        public String getFollow_num() {
            return follow_num;
        }

        public void setFollow_num(String follow_num) {
            this.follow_num = follow_num;
        }

        public String getBest_answer_id() {
            return best_answer_id;
        }

        public void setBest_answer_id(String best_answer_id) {
            this.best_answer_id = best_answer_id;
        }

        public String getPraise_num() {
            return praise_num;
        }

        public void setPraise_num(String praise_num) {
            this.praise_num = praise_num;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public Long getCreate_time() {
            return create_time;
        }

        public void setCreate_time(Long create_time) {
            this.create_time = create_time;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public static class UserBean {
            /**
             * id : 用户id
             * avatar : 用户头像
             * nickname : 用户名称
             */

            private String id;
            private String avatar;
            private String nickname;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }
        }

    }

}

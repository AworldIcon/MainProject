package com.coder.kzxt.question.beans;

import com.coder.kzxt.base.beans.TextImageBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by pc on 2017/4/19.
 */

public class QuestionsReplyListBean implements Serializable{

    /**
     * code : 200
     * message : ok
     * items : [{"id":"id","pid":"回答id（回复父id）","course_id":"课程ID","qa_id":"话题（问答）id","content":"回复内容","user_id":"用户ID","is_best_answer":"是否是最佳答案1是，0不是","reply_uid":"被回复用户id","role":"创建者身份，1老师，2学生","create_time":"创建时间"}]
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

    public static class PaginateBean implements Serializable{
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

    public static class ItemsBean implements Serializable{
        /**
         * id : id
         * pid : 回答id（回复父id）
         * course_id : 课程ID
         * qa_id : 话题（问答）id
         * content : 回复内容
         * user_id : 用户ID
         * is_best_answer : 是否是最佳答案1是，0不是
         * reply_uid : 被回复用户id
         * role : 创建者身份，1老师，2学生
         * create_time : 创建时间
         * count: 每个一级回复下的二级回复数量
         */

        private String id;
        private String pid;
        private String course_id;
        private String qa_id;
        private List<TextImageBean> content;
        private String user_id;
        private String is_best_answer;
        private String reply_uid;
        private String role;
        private long create_time;
        private UserBean user;
        private int count;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getCourse_id() {
            return course_id;
        }

        public void setCourse_id(String course_id) {
            this.course_id = course_id;
        }

        public String getQa_id() {
            return qa_id;
        }

        public void setQa_id(String qa_id) {
            this.qa_id = qa_id;
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

        public String getIs_best_answer() {
            return is_best_answer;
        }

        public void setIs_best_answer(String is_best_answer) {
            this.is_best_answer = is_best_answer;
        }

        public String getReply_uid() {
            return reply_uid;
        }

        public void setReply_uid(String reply_uid) {
            this.reply_uid = reply_uid;
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
        public static class UserBean implements Serializable{
            /**
             * id : 回复用户id
             * avatar :回复 用户头像
             * nickname : 回复用户名称
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

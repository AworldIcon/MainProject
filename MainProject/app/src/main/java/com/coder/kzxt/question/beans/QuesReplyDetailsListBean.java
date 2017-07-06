package com.coder.kzxt.question.beans;

import com.coder.kzxt.base.beans.TextImageBean;

import java.util.List;

/**
 * Created by zw on 2017/4/26.
 */

public class QuesReplyDetailsListBean {

    /**
     * code : 200
     * message : ok
     * paginate : {"currentPage":1,"pageSize":20,"pageNum":1,"totalNum":1}
     * items : [{"id":160776,"pid":160774,"course_id":20643,"lesson_id":0,"qa_id":44612,"user_id":906994,"is_elite":0,"content":"回复内容","is_best_answer":0,"reply_uid":906994,"role":1,"is_course_member":1,"device":"","create_time":1493174365,"update_time":1493174365,"delete_time":null,"user":{"nickname":"123","avatar":"http://html.gxy.com/www/theme/common/default/images/avatar/girl.png","id":906994},"replyid":{"nickname":"123","id":906994}}]
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
         * pageNum : 1
         * totalNum : 1
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
         * id : 160776
         * pid : 160774
         * course_id : 20643
         * lesson_id : 0
         * qa_id : 44612
         * user_id : 906994
         * is_elite : 0
         * content : 回复内容
         * is_best_answer : 0
         * reply_uid : 906994
         * role : 1
         * is_course_member : 1
         * device :
         * create_time : 1493174365
         * update_time : 1493174365
         * delete_time : null
         * user : {"nickname":"123","avatar":"http://html.gxy.com/www/theme/common/default/images/avatar/girl.png","id":906994}
         * replyid : {"nickname":"123","id":906994}
         */

        private int id;
        private int pid;
        private int course_id;
        private int lesson_id;
        private int qa_id;
        private int user_id;
        private int is_elite;
        private List<TextImageBean> content;
        private int is_best_answer;
        private int reply_uid;
        private int role;
        private int is_course_member;
        private String device;
        private Long create_time;
        private Long update_time;
        private Object delete_time;
        private UserBean user;
        private ReplyidBean replyid;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getPid() {
            return pid;
        }

        public void setPid(int pid) {
            this.pid = pid;
        }

        public int getCourse_id() {
            return course_id;
        }

        public void setCourse_id(int course_id) {
            this.course_id = course_id;
        }

        public int getLesson_id() {
            return lesson_id;
        }

        public void setLesson_id(int lesson_id) {
            this.lesson_id = lesson_id;
        }

        public int getQa_id() {
            return qa_id;
        }

        public void setQa_id(int qa_id) {
            this.qa_id = qa_id;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public int getIs_elite() {
            return is_elite;
        }

        public void setIs_elite(int is_elite) {
            this.is_elite = is_elite;
        }

        public List<TextImageBean> getContent() {
            return content;
        }

        public void setContent(List<TextImageBean> content) {
            this.content = content;
        }

        public int getIs_best_answer() {
            return is_best_answer;
        }

        public void setIs_best_answer(int is_best_answer) {
            this.is_best_answer = is_best_answer;
        }

        public int getReply_uid() {
            return reply_uid;
        }

        public void setReply_uid(int reply_uid) {
            this.reply_uid = reply_uid;
        }

        public int getRole() {
            return role;
        }

        public void setRole(int role) {
            this.role = role;
        }

        public int getIs_course_member() {
            return is_course_member;
        }

        public void setIs_course_member(int is_course_member) {
            this.is_course_member = is_course_member;
        }

        public String getDevice() {
            return device;
        }

        public void setDevice(String device) {
            this.device = device;
        }

        public Long getCreate_time() {
            return create_time;
        }

        public void setCreate_time(Long create_time) {
            this.create_time = create_time;
        }

        public Long getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(Long update_time) {
            this.update_time = update_time;
        }

        public Object getDelete_time() {
            return delete_time;
        }

        public void setDelete_time(Object delete_time) {
            this.delete_time = delete_time;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public ReplyidBean getReplyid() {
            return replyid;
        }

        public void setReplyid(ReplyidBean replyid) {
            this.replyid = replyid;
        }

        public static class UserBean {
            /**
             * nickname : 123
             * avatar : http://html.gxy.com/www/theme/common/default/images/avatar/girl.png
             * id : 906994
             */

            private String nickname;
            private String avatar;
            private int id;

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }
        }

        public static class ReplyidBean {
            /**
             * nickname : 123
             * id : 906994
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

package com.coder.kzxt.buildwork.entity;

import java.util.List;

/**
 * Created by pc on 2017/3/21.
 */

public class FinishWorkNum {
    //获取（考试/作业）结果列表
    /**
     * code : 200
     * message : ok
     * items : [{"id":"ID","user":{"name":"用户注册账号"},"profile":{"nickname":"用户昵称"},"status":"状态:1.做题中 2.待批阅 3.已完成","status_text":"状态文本","finished_time":"提交时间","user_id":"提交用户id","ip":"提交ip","score":"成绩","checker":{"nickname":"批阅人昵称"}}]
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
        private int totalNum;

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

        public int getTotalNum() {
            return totalNum;
        }

        public void setTotalNum(int totalNum) {
            this.totalNum = totalNum;
        }
    }

    public static class ItemsBean {
        /**
         * id : ID
         * user : {"name":"用户注册账号"}
         * profile : {"nickname":"用户昵称"}
         * status : 状态:1.做题中 2.待批阅 3.已完成
         * status_text : 状态文本
         * finished_time : 提交时间
         * user_id : 提交用户id
         * ip : 提交ip
         * score : 成绩
         * checker : {"nickname":"批阅人昵称"}
         */

        private String id;
        private UserBean user;
        private ProfileBean profile;
        private String status;
        private String status_text;
        private String finished_time;
        private String user_id;
        private String ip;
        private String score;
        private CheckerBean checker;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public ProfileBean getProfile() {
            return profile;
        }

        public void setProfile(ProfileBean profile) {
            this.profile = profile;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getStatus_text() {
            return status_text;
        }

        public void setStatus_text(String status_text) {
            this.status_text = status_text;
        }

        public String getFinished_time() {
            return finished_time;
        }

        public void setFinished_time(String finished_time) {
            this.finished_time = finished_time;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public CheckerBean getChecker() {
            return checker;
        }

        public void setChecker(CheckerBean checker) {
            this.checker = checker;
        }

        public static class UserBean {
            /**
             * name : 用户注册账号
             */

            private String name;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }

        public static class ProfileBean {
            /**
             * nickname : 用户昵称
             */

            private String nickname;
            private String avatar;

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

        public static class CheckerBean {
            /**
             * nickname : 批阅人昵称
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

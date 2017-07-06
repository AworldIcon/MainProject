package com.coder.kzxt.course.beans;

import java.util.List;

/**
 * Created by Administrator on 2017/4/8.
 */

public class CommentBean {


    /**
     * code : 200
     * message : ok
     * paginate : {"currentPage":1,"pageSize":20,"pageNum":1,"totalNum":6}
     * items : [{"id":29,"course_id":16681,"user_id":916518,"content":"fdafda","score":3,"is_anon":1,"create_time":1491617738,"update_time":1491617738,"delete_time":null,"profile":{"id":916518,"gender":2,"nickname":"匿名","avatar":"http://html.gxy.com/www/theme/common/default/images/avatar/girl.png","birthday":null}},{"id":28,"course_id":16681,"user_id":916518,"content":"fdsafdsa","score":4,"is_anon":0,"create_time":1491617393,"update_time":1491617393,"delete_time":null,"profile":{"id":916518,"gender":2,"nickname":"匿名","avatar":"http://html.gxy.com/www/theme/common/default/images/avatar/girl.png","birthday":null}},{"id":27,"course_id":16681,"user_id":916518,"content":"fdsafds","score":4,"is_anon":0,"create_time":1491617385,"update_time":1491617385,"delete_time":null,"profile":{"id":916518,"gender":2,"nickname":"匿名","avatar":"http://html.gxy.com/www/theme/common/default/images/avatar/girl.png","birthday":null}},{"id":26,"course_id":16681,"user_id":916518,"content":"fdsafd","score":3,"is_anon":1,"create_time":1491617377,"update_time":1491617377,"delete_time":null,"profile":{"id":916518,"gender":2,"nickname":"匿名","avatar":"http://html.gxy.com/www/theme/common/default/images/avatar/girl.png","birthday":null}},{"id":25,"course_id":16681,"user_id":916518,"content":"fjdlksa","score":4,"is_anon":1,"create_time":1491616578,"update_time":1491616578,"delete_time":null,"profile":{"id":916518,"gender":2,"nickname":"匿名","avatar":"http://html.gxy.com/www/theme/common/default/images/avatar/girl.png","birthday":null}},{"id":24,"course_id":16681,"user_id":916518,"content":"fdsafds","score":2,"is_anon":1,"create_time":1491616195,"update_time":1491616195,"delete_time":null,"profile":{"id":916518,"gender":2,"nickname":"匿名","avatar":"http://html.gxy.com/www/theme/common/default/images/avatar/girl.png","birthday":null}}]
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
         * totalNum : 6
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
         * id : 29
         * course_id : 16681
         * user_id : 916518
         * content : fdafda
         * score : 3
         * is_anon : 1
         * create_time : 1491617738
         * update_time : 1491617738
         * delete_time : null
         * profile : {"id":916518,"gender":2,"nickname":"匿名","avatar":"http://html.gxy.com/www/theme/common/default/images/avatar/girl.png","birthday":null}
         */

        private int id;
        private int course_id;
        private int user_id;
        private String content;
        private int score;
        private int is_anon;
        private int create_time;
        private int update_time;
        private Object delete_time;
        private ProfileBean profile;

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

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public int getIs_anon() {
            return is_anon;
        }

        public void setIs_anon(int is_anon) {
            this.is_anon = is_anon;
        }

        public int getCreate_time() {
            return create_time;
        }

        public void setCreate_time(int create_time) {
            this.create_time = create_time;
        }

        public int getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(int update_time) {
            this.update_time = update_time;
        }

        public Object getDelete_time() {
            return delete_time;
        }

        public void setDelete_time(Object delete_time) {
            this.delete_time = delete_time;
        }

        public ProfileBean getProfile() {
            return profile;
        }

        public void setProfile(ProfileBean profile) {
            this.profile = profile;
        }

        public static class ProfileBean {
            /**
             * id : 916518
             * gender : 2
             * nickname : 匿名
             * avatar : http://html.gxy.com/www/theme/common/default/images/avatar/girl.png
             * birthday : null
             */

            private int id;
            private int gender;
            private String nickname;
            private String avatar;
            private Object birthday;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getGender() {
                return gender;
            }

            public void setGender(int gender) {
                this.gender = gender;
            }

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

            public Object getBirthday() {
                return birthday;
            }

            public void setBirthday(Object birthday) {
                this.birthday = birthday;
            }
        }
    }
}

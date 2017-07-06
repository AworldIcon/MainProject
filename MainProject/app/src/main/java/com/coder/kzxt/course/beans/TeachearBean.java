package com.coder.kzxt.course.beans;

import java.util.List;

/**
 * Created by Administrator on 2017/3/25.
 */

public class TeachearBean {

    /**
     * code : 200
     * message : ok
     * paginate : {"currentPage":1,"pageSize":20,"pageNum":1,"totalNum":1}
     * items : [{"id":1203,"course_id":16589,"course_class_id":305,"user_id":695364,"type":1,"create_time":1490405799,"update_time":1490405799,"delete_time":null,"profile":{"id":695364,"gender":2,"nickname":"1111111","avatar":"http://html.gxy.com/uploads/avatar/20170325/5e26cd8441519b7fe06e2a744b5d99c2.png","birthday":null,"desc":"desc"},"user":{"id":695364,"email":"1248836281@qq.com","mobile":0}}]
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
         * id : 1203
         * course_id : 16589
         * course_class_id : 305
         * user_id : 695364
         * type : 1
         * create_time : 1490405799
         * update_time : 1490405799
         * delete_time : null
         * profile : {"id":695364,"gender":2,"nickname":"1111111","avatar":"http://html.gxy.com/uploads/avatar/20170325/5e26cd8441519b7fe06e2a744b5d99c2.png","birthday":null,"desc":"desc"}
         * user : {"id":695364,"email":"1248836281@qq.com","mobile":0}
         */

        private int id;
        private int course_id;
        private int course_class_id;
        private int user_id;
        private int type;
        private int create_time;
        private int update_time;
//        private Object delete_time;
        private ProfileBean profile;
//        private UserBean user;

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

        public int getCourse_class_id() {
            return course_class_id;
        }

        public void setCourse_class_id(int course_class_id) {
            this.course_class_id = course_class_id;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
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

//        public Object getDelete_time() {
//            return delete_time;
//        }
//
//        public void setDelete_time(Object delete_time) {
//            this.delete_time = delete_time;
//        }

        public ProfileBean getProfile() {
            return profile;
        }

        public void setProfile(ProfileBean profile) {
            this.profile = profile;
        }

//        public UserBean getUser() {
//            return user;
//        }
//
//        public void setUser(UserBean user) {
//            this.user = user;
//        }

        public static class ProfileBean {
            /**
             * id : 695364
             * gender : 2
             * nickname : 1111111
             * avatar : http://html.gxy.com/uploads/avatar/20170325/5e26cd8441519b7fe06e2a744b5d99c2.png
             * birthday : null
             * desc : desc
             */

            private int id;
            private int gender;
            private String nickname;
            private String avatar;
            private Object birthday;
            private String desc;

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

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }
        }

//        public static class UserBean {
//            /**
//             * id : 695364
//             * email : 1248836281@qq.com
//             * mobile : 0
//             */
//
//            private int id;
//            private String email;
//            private int mobile;
//
//            public int getId() {
//                return id;
//            }
//
//            public void setId(int id) {
//                this.id = id;
//            }
//
//            public String getEmail() {
//                return email;
//            }
//
//            public void setEmail(String email) {
//                this.email = email;
//            }
//
//            public int getMobile() {
//                return mobile;
//            }
//
//            public void setMobile(int mobile) {
//                this.mobile = mobile;
//            }
//        }
    }
}

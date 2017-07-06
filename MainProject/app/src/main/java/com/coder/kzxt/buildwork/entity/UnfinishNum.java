package com.coder.kzxt.buildwork.entity;

import java.util.List;

/**
 * Created by pc on 2016/10/18.
 */
public class UnfinishNum {

    /**
     * code : 200
     * message : ok
     * paginate : {"currentPage":1,"pageSize":20,"pageNum":2,"totalNum":23}
     * items : [{"user_id":916502,"user":{"id":916502,"email":null,"mobile":13621264044,"name":"13621264044"},"profile":{"id":916502,"gender":2,"nickname":"袁玉龙123","avatar":"http://html.gxy.com/www/theme/common/default/images/avatar/girl.png","birthday":null}},{"user_id":916452,"user":{"id":916452,"email":null,"mobile":18610373739,"name":"18610373739"},"profile":{"id":916452,"gender":1,"nickname":"猜猜看","avatar":"http://html.gxy.com/www/theme/common/default/images/avatar/boy.png","birthday":null}},{"user_id":916452,"user":{"id":916452,"email":null,"mobile":18610373739,"name":"18610373739"},"profile":{"id":916452,"gender":1,"nickname":"猜猜看","avatar":"http://html.gxy.com/www/theme/common/default/images/avatar/boy.png","birthday":null}},{"user_id":916500,"user":{"id":916500,"email":null,"mobile":13439077579,"name":"13439077579"},"profile":{"id":916500,"gender":1,"nickname":"背吧","avatar":"http://html.gxy.com/www/theme/common/default/images/avatar/boy.png","birthday":"1970-01-01"}},{"user_id":785069,"user":{"id":785069,"email":"...595995401@qq.com","mobile":0,"name":"...595995401@qq.com"},"profile":{"id":null,"gender":2,"nickname":"","avatar":"http://html.gxy.com/www/theme/common/default/images/avatar/girl.png","birthday":null}},{"user_id":778039,"user":{"id":778039,"email":"---kunkun198585@126.comkunkun198585@126.com","mobile":0,"name":"---kunkun198585@126.comkunkun198585@126.com"},"profile":{"id":null,"gender":2,"nickname":"","avatar":"http://html.gxy.com/www/theme/common/default/images/avatar/girl.png","birthday":null}},{"user_id":770358,"user":{"id":770358,"email":"\"qcf_vip@163.com","mobile":0,"name":"\"qcf_vip@163.com"},"profile":{"id":null,"gender":2,"nickname":"","avatar":"http://html.gxy.com/www/theme/common/default/images/avatar/girl.png","birthday":null}},{"user_id":769820,"user":{"id":769820,"email":"\"Lrf594@126.com","mobile":0,"name":"\"Lrf594@126.com"},"profile":{"id":null,"gender":2,"nickname":"","avatar":"http://html.gxy.com/www/theme/common/default/images/avatar/girl.png","birthday":null}},{"user_id":766568,"user":{"id":766568,"email":"-----------w332127044@sina.com","mobile":0,"name":"-----------w332127044@sina.com"},"profile":{"id":null,"gender":2,"nickname":"","avatar":"http://html.gxy.com/www/theme/common/default/images/avatar/girl.png","birthday":null}},{"user_id":766056,"user":{"id":766056,"email":"--yerik.gao@gmail.comyerik.gao@gmail.com","mobile":0,"name":"--yerik.gao@gmail.comyerik.gao@gmail.com"},"profile":{"id":null,"gender":2,"nickname":"","avatar":"http://html.gxy.com/www/theme/common/default/images/avatar/girl.png","birthday":null}},{"user_id":764480,"user":{"id":764480,"email":"--717526937@qq.com","mobile":0,"name":"--717526937@qq.com"},"profile":{"id":null,"gender":2,"nickname":"","avatar":"http://html.gxy.com/www/theme/common/default/images/avatar/girl.png","birthday":null}},{"user_id":764476,"user":{"id":764476,"email":"---731021822@qq.com","mobile":0,"name":"---731021822@qq.com"},"profile":{"id":null,"gender":2,"nickname":"","avatar":"http://html.gxy.com/www/theme/common/default/images/avatar/girl.png","birthday":null}},{"user_id":720447,"user":{"id":720447,"email":"-LIHUA-1215@163.COM","mobile":0,"name":"-LIHUA-1215@163.COM"},"profile":{"id":null,"gender":2,"nickname":"","avatar":"http://html.gxy.com/www/theme/common/default/images/avatar/girl.png","birthday":null}},{"user_id":707820,"user":{"id":707820,"email":".1330313953@qq.com","mobile":0,"name":".1330313953@qq.com"},"profile":{"id":null,"gender":2,"nickname":"","avatar":"http://html.gxy.com/www/theme/common/default/images/avatar/girl.png","birthday":null}},{"user_id":695364,"user":{"id":695364,"email":"1248836281@qq.com","mobile":0,"name":"1248836281@qq.com"},"profile":{"id":695364,"gender":2,"nickname":"1111111","avatar":"http://html.gxy.com/uploads/avatar/20170325/5e26cd8441519b7fe06e2a744b5d99c2.png","birthday":null}},{"user_id":682189,"user":{"id":682189,"email":"0-4444@163.com","mobile":0,"name":"0-4444@163.com"},"profile":{"id":null,"gender":2,"nickname":"","avatar":"http://html.gxy.com/www/theme/common/default/images/avatar/girl.png","birthday":null}},{"user_id":682114,"user":{"id":682114,"email":"0.003q@163.com","mobile":0,"name":"0.003q@163.com"},"profile":{"id":null,"gender":2,"nickname":"","avatar":"http://html.gxy.com/www/theme/common/default/images/avatar/girl.png","birthday":null}},{"user_id":628522,"user":{"id":628522,"email":".942980758@qq.com","mobile":0,"name":".942980758@qq.com"},"profile":{"id":null,"gender":2,"nickname":"","avatar":"http://html.gxy.com/www/theme/common/default/images/avatar/girl.png","birthday":null}},{"user_id":628044,"user":{"id":628044,"email":".13785132658@163.com","mobile":0,"name":".13785132658@163.com"},"profile":{"id":null,"gender":2,"nickname":"","avatar":"http://html.gxy.com/www/theme/common/default/images/avatar/girl.png","birthday":null}},{"user_id":584765,"user":{"id":584765,"email":"0.123456@qq.com","mobile":0,"name":"0.123456@qq.com"},"profile":{"id":null,"gender":2,"nickname":"","avatar":"http://html.gxy.com/www/theme/common/default/images/avatar/girl.png","birthday":null}}]
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
         * pageNum : 2
         * totalNum : 23
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
         * user_id : 916502
         * user : {"id":916502,"email":null,"mobile":13621264044,"name":"13621264044"}
         * profile : {"id":916502,"gender":2,"nickname":"袁玉龙123","avatar":"http://html.gxy.com/www/theme/common/default/images/avatar/girl.png","birthday":null}
         */

        private int user_id;
        private UserBean user;
        private ProfileBean profile;

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
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

        public static class UserBean {
            /**
             * id : 916502
             * email : null
             * mobile : 13621264044
             * name : 13621264044
             */

            private int id;
            private Object email;
            private long mobile;
            private String name;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public Object getEmail() {
                return email;
            }

            public void setEmail(Object email) {
                this.email = email;
            }

            public long getMobile() {
                return mobile;
            }

            public void setMobile(long mobile) {
                this.mobile = mobile;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }

        public static class ProfileBean {
            /**
             * id : 916502
             * gender : 2
             * nickname : 袁玉龙123
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

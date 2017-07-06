package com.coder.kzxt.poster.beans;

import java.util.List;

/**
 * Created by Administrator on 2017/3/2.
 */

public class PostersCommentBean {


    /**
     * code : 200
     * message : ok
     * paginate : {"currentPage":1,"pageSize":20,"pageNum":1,"totalNum":4}
     * items : [{"id":289,"user_id":907179,"content":"as long as you love me ","type":"poster","pid":0,"posterId":19925,"create_time":1494294695,"update_time":1494294695,"delete_time":null,"reply_uid":0,"user":{"nickname":"有谁知道1","avatar":"http://html.gxy.com/www/theme/common/default/images/avatar/girl.png","id":907179},"reply":null},{"id":310,"user_id":906998,"content":"y65y65","type":"poster","pid":289,"posterId":19925,"create_time":1495589760,"update_time":1495589760,"delete_time":null,"reply_uid":907179,"user":{"nickname":"测试","avatar":"http://html.gxy.com/www/theme/common/default/images/avatar/girl.png","id":906998},"reply":{"nickname":"有谁知道1","avatar":"http://html.gxy.com/www/theme/common/default/images/avatar/girl.png","id":907179}},{"id":311,"user_id":906998,"content":"y65y65","type":"poster","pid":289,"posterId":19925,"create_time":1495589764,"update_time":1495589764,"delete_time":null,"reply_uid":906998,"user":{"nickname":"测试","avatar":"http://html.gxy.com/www/theme/common/default/images/avatar/girl.png","id":906998},"reply":{"nickname":"测试","avatar":"http://html.gxy.com/www/theme/common/default/images/avatar/girl.png","id":906998}},{"id":312,"user_id":906998,"content":"dewdewd","type":"poster","pid":289,"posterId":19925,"create_time":1495619963,"update_time":1495619963,"delete_time":null,"reply_uid":906998,"user":{"nickname":"测试","avatar":"http://html.gxy.com/www/theme/common/default/images/avatar/girl.png","id":906998},"reply":{"nickname":"测试","avatar":"http://html.gxy.com/www/theme/common/default/images/avatar/girl.png","id":906998}}]
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
         * totalNum : 4
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
         * id : 289
         * user_id : 907179
         * content : as long as you love me
         * type : poster
         * pid : 0
         * posterId : 19925
         * create_time : 1494294695
         * update_time : 1494294695
         * delete_time : null
         * reply_uid : 0
         * user : {"nickname":"有谁知道1","avatar":"http://html.gxy.com/www/theme/common/default/images/avatar/girl.png","id":907179}
         * reply : null
         */

        private String id;
        private String user_id;
        private String content;
        private String type;
        private String pid;
        private String posterId;
        private String create_time;
//        private int update_time;
//        private Object delete_time;
        private String reply_uid;
        private UserBean user;
        private ReplyBean reply;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getPosterId() {
            return posterId;
        }

        public void setPosterId(String posterId) {
            this.posterId = posterId;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

//        public int getUpdate_time() {
//            return update_time;
//        }
//
//        public void setUpdate_time(int update_time) {
//            this.update_time = update_time;
//        }
//
//        public Object getDelete_time() {
//            return delete_time;
//        }
//
//        public void setDelete_time(Object delete_time) {
//            this.delete_time = delete_time;
//        }

        public String getReply_uid() {
            return reply_uid;
        }

        public void setReply_uid(String reply_uid) {
            this.reply_uid = reply_uid;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public ReplyBean getReply() {
            return reply;
        }

        public void setReply(ReplyBean reply) {
            this.reply = reply;
        }

        public static  class ReplyBean{
            private String nickname;
            private String avatar;
            private String id;

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

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }
        }

        public static class UserBean {
            /**
             * nickname : 有谁知道1
             * avatar : http://html.gxy.com/www/theme/common/default/images/avatar/girl.png
             * id : 907179
             */

            private String nickname;
            private String avatar;
            private String id;

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

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }
        }
    }
}

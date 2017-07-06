package com.coder.kzxt.poster.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by tangcy on 2017/5/22.
 */

public class PosterBeans implements Serializable {

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
         * totalNum : 38
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

    public static class ItemsBean implements Serializable{
        /**
         * id : 21014
         * poster_category_id : 38
         * type : 2
         * desc : dfsdsssssssssfffffffffffffffffffffffffffffffffffffffffffffffffff东方时尚所所所所所所所所所所所所所所所所所所所所所所所所所所所所所所所所所所所所所所所所所所所




         所所所所所所所所所所所所所所所所所东方时尚所所所所所所所所所所所所所所所所所所所所所                                                            所所所所所所所所所所所所所所所所所所所所所所所所所所所所所所所所所所所所饭是顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶大大大多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多
         * pic :
         * top : 0
         * user_id : 832695
         * pic_height : 0
         * pic_width : 0
         * love : 0
         * pv : 0
         * status : 开启
         * recommend : 0
         * bg_color : #0c8fb2
         * create_time : 1495422236
         * update_time : 1495422236
         * delete_time : null
         * topTime : 1495422236
         * poster_type : {"name":"通知","id":38,"status":"开启","type":"默认"}
         * poster_comment : 0
         * posterlike : 0
         * user : {"nickname":"he","avatar":"http://192.168.3.86/group1/M00/00/0D/wKgDV1kiXWiAOhBoAAI5hjtXnoU795_80x80.png","id":832695}
         */

        private String id;
        private int poster_category_id;
        private int type;
        private String desc;
        private String pic;
//        private int top;
        private int user_id;
        private String pic_height;
        private String pic_width;
//        private int love;
        private String pv;
//        private String status;
//        private int recommend;
        private String bg_color;
        private String create_time;
//        private int update_time;
//        private Object delete_time;
//        private int topTime;
//        private PosterTypeBean poster_type;
//        private String poster_comment;
//        private String posterlike;

        private String comment_count;
        private String poster_like;

        private UserBean user;
        private String is_like;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getPoster_category_id() {
            return poster_category_id;
        }

        public void setPoster_category_id(int poster_category_id) {
            this.poster_category_id = poster_category_id;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

//        public int getTop() {
//            return top;
//        }
//
//        public void setTop(int top) {
//            this.top = top;
//        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public String getPic_height() {
            return pic_height;
        }

        public void setPic_height(String pic_height) {
            this.pic_height = pic_height;
        }

        public String getPic_width() {
            return pic_width;
        }

        public void setPic_width(String pic_width) {
            this.pic_width = pic_width;
        }

//        public int getLove() {
//            return love;
//        }
//
//        public void setLove(int love) {
//            this.love = love;
//        }

        public String  getPv() {
            return pv;
        }

        public void setPv(String pv) {
            this.pv = pv;
        }

//        public String getStatus() {
//            return status;
//        }
//
//        public void setStatus(String status) {
//            this.status = status;
//        }
//
//        public int getRecommend() {
//            return recommend;
//        }
//
//        public void setRecommend(int recommend) {
//            this.recommend = recommend;
//        }

        public String getBg_color() {
            return bg_color;
        }

        public void setBg_color(String bg_color) {
            this.bg_color = bg_color;
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

//        public int getTopTime() {
//            return topTime;
//        }
//
//        public void setTopTime(int topTime) {
//            this.topTime = topTime;
//        }

//        public PosterTypeBean getPoster_type() {
//            return poster_type;
//        }
//
//        public void setPoster_type(PosterTypeBean poster_type) {
//            this.poster_type = poster_type;
//        }
//
//        public String getPoster_comment() {
//            return poster_comment;
//        }

//        public void setPoster_comment(String poster_comment) {
//            this.poster_comment = poster_comment;
//        }

//        public String getPosterlike() {
//            return posterlike;
//        }


        public String getComment_count() {
            return comment_count;
        }

        public String getPoster_like() {
            return poster_like;
        }

        public void setComment_count(String comment_count) {
            this.comment_count = comment_count;
        }

        public void setPoster_like(String poster_like) {
            this.poster_like = poster_like;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public String getIs_like() {
            return is_like;
        }

        public void setIs_like(String is_like) {
            this.is_like = is_like;
        }

        //        public static class PosterTypeBean {
//            /**
//             * name : 通知
//             * id : 38
//             * status : 开启
//             * type : 默认
//             */
//
//            private String name;
//            private int id;
//            private String status;
//            private String type;
//
//            public String getName() {
//                return name;
//            }
//
//            public void setName(String name) {
//                this.name = name;
//            }
//
//            public int getId() {
//                return id;
//            }
//
//            public void setId(int id) {
//                this.id = id;
//            }
//
//            public String getStatus() {
//                return status;
//            }
//
//            public void setStatus(String status) {
//                this.status = status;
//            }
//
//            public String getType() {
//                return type;
//            }
//
//            public void setType(String type) {
//                this.type = type;
//            }
//        }

        public static class UserBean implements Serializable {
            /**
             * nickname : he
             * avatar : http://192.168.3.86/group1/M00/00/0D/wKgDV1kiXWiAOhBoAAI5hjtXnoU795_80x80.png
             * id : 832695
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
                if(avatar==null){
                    avatar = "";
                }
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

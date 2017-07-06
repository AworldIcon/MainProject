package com.coder.kzxt.classe.beans;

import java.util.ArrayList;

/**
 * Created by wangtingshun on 2017/6/12.
 * 话题bean
 */

public class ClassTopicBean {

    private String code;
    private String message;
    private Paginate paginate;
    private ArrayList<ClassTopic> items;

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

    public ArrayList<ClassTopic> getItems() {
        return items;
    }

    public void setItems(ArrayList<ClassTopic> items) {
        this.items = items;
    }

    public Paginate getPaginate() {
        return paginate;
    }

    public void setPaginate(Paginate paginate) {
        this.paginate = paginate;
    }

    public class ClassTopic{

        private String id;
        private String user_id;
        private String title;
        private String is_stick;
        private String is_top;
        private String reply_count;
        private String collect_count;
        private String view_count;
        private String like_count;
        private String create_time;
        private Creator creator;
        private Content content;

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

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getIs_stick() {
            return is_stick;
        }

        public void setIs_stick(String is_stick) {
            this.is_stick = is_stick;
        }

        public String getIs_top() {
            return is_top;
        }

        public void setIs_top(String is_top) {
            this.is_top = is_top;
        }

        public String getReply_count() {
            return reply_count;
        }

        public void setReply_count(String reply_count) {
            this.reply_count = reply_count;
        }

        public String getCollect_count() {
            return collect_count;
        }

        public void setCollect_count(String collect_count) {
            this.collect_count = collect_count;
        }

        public String getView_count() {
            return view_count;
        }

        public void setView_count(String view_count) {
            this.view_count = view_count;
        }

        public String getLike_count() {
            return like_count;
        }

        public void setLike_count(String like_count) {
            this.like_count = like_count;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public Creator getCreator() {
            return creator;
        }

        public void setCreator(Creator creator) {
            this.creator = creator;
        }

        public Content getContent() {
            return content;
        }

        public void setContent(Content content) {
            this.content = content;
        }

        public class Creator {
            private String id;
            private String nickname;
            private String avatar;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
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
        }

        public class Content {
            private String text;
            private ArrayList<String> images;

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }

            public ArrayList<String> getImages() {
                return images;
            }

            public void setImages(ArrayList<String> images) {
                this.images = images;
            }
        }
    }

    public class Paginate {
        //当前页数
        private int currentPage;
        //每页记录数
        private int pageSize;
        //总页数
        private int pageNum;
        //总记录数
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
}

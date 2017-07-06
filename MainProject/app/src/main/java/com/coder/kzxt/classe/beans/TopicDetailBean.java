package com.coder.kzxt.classe.beans;

import java.util.ArrayList;

/**
 * Created by wangtingshun on 2017/6/14.
 * 班级详情的bean
 */

public class TopicDetailBean {

    private String code;
    private String message;
    private TopicDetail item;
    private Paginate paginate;

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

    public TopicDetail getItem() {
        return item;
    }

    public void setItem(TopicDetail item) {
        this.item = item;
    }

    public Paginate getPaginate() {
        return paginate;
    }

    public void setPaginate(Paginate paginate) {
        this.paginate = paginate;
    }

    public class TopicDetail{

        private String id;
        private String user_id;
        private String title;
        private ArrayList<DetailContent> content;
        private String is_stick;
        private String is_top;
        private String reply_count;
        private String collect_count;
        private String view_count;
        private String like_count;
        private String create_time;
        private String has_like;
        private String has_collect;
        private Creator creator;

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

        public String getHas_like() {
            return has_like;
        }

        public void setHas_like(String has_like) {
            this.has_like = has_like;
        }

        public Creator getCreator() {
            return creator;
        }

        public void setCreator(Creator creator) {
            this.creator = creator;
        }

        public String getHas_collect() {
            return has_collect;
        }

        public void setHas_collect(String has_collect) {
            this.has_collect = has_collect;
        }

        public ArrayList<DetailContent> getContent() {
            return content;
        }

        public void setContent(ArrayList<DetailContent> content) {
            this.content = content;
        }

        @Override
        public String toString() {
            return "TopicDetail{" +
                    "id='" + id + '\'' +
                    ", user_id='" + user_id + '\'' +
                    ", title='" + title + '\'' +
                    ", content='" + content + '\'' +
                    ", is_stick='" + is_stick + '\'' +
                    ", is_top='" + is_top + '\'' +
                    ", reply_count='" + reply_count + '\'' +
                    ", collect_count='" + collect_count + '\'' +
                    ", view_count='" + view_count + '\'' +
                    ", like_count='" + like_count + '\'' +
                    ", create_time='" + create_time + '\'' +
                    ", has_like='" + has_like + '\'' +
                    ", has_collect='" + has_collect + '\'' +
                    ", creator=" + creator +
                    '}';
        }
    }
    public static class DetailContent{

        private String type;
        private String content;

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

        @Override
        public String toString() {
            return "DetailContent{" +
                    "type='" + type + '\'' +
                    ", content='" + content + '\'' +
                    '}';
        }
    }

    public static class Creator{

        private String id;
        private String nickname;
        private String avatar;

        public String getId() {
            if (id == null)
            {
                return "";
            }
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

        @Override
        public String toString() {
            return "Creator{" +
                    "id='" + id + '\'' +
                    ", nickname='" + nickname + '\'' +
                    ", avatar='" + avatar + '\'' +
                    '}';
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

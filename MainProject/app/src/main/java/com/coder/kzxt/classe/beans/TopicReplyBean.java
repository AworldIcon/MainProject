package com.coder.kzxt.classe.beans;

import java.util.ArrayList;

/**
 * Created by wangtingshun on 2017/6/15.
 * 话题详情回复的bean
 */

public class TopicReplyBean {

    private String code;
    private String message;
    private Paginate paginate;
    public ArrayList<TopicReply> items;


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

    public Paginate getPaginate() {
        return paginate;
    }

    public void setPaginate(Paginate paginate) {
        this.paginate = paginate;
    }

    public ArrayList<TopicReply> getItems() {
        return items;
    }

    public void setItems(ArrayList<TopicReply> items) {
        this.items = items;
    }

    /**
     * 话题回复
     */
    public static class TopicReply{

        private String id;
        private ArrayList<CreateContent> content; //回复内容
        private String user_id;  //用户id
        private String group_topic_id;  //话题id
        private String create_time;   //创建时间
        private CreateProfile user_profile; //创建者
        private ArrayList<Conmment> comments;     //回复列表

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public ArrayList<CreateContent> getContent() {
            return content;
        }

        public void setContent(ArrayList<CreateContent> content) {
            this.content = content;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getGroup_topic_id() {
            return group_topic_id;
        }

        public void setGroup_topic_id(String group_topic_id) {
            this.group_topic_id = group_topic_id;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public CreateProfile getUser_profile() {
            return user_profile;
        }

        public void setUser_profile(CreateProfile user_profile) {
            this.user_profile = user_profile;
        }

        public ArrayList<Conmment> getComments() {
            return comments;
        }

        public void setComments(ArrayList<Conmment> comments) {
            this.comments = comments;
        }

        @Override
        public String toString() {
            return "TopicReply{" +
                    "id='" + id + '\'' +
                    ", content=" + content +
                    ", user_id='" + user_id + '\'' +
                    ", group_topic_id='" + group_topic_id + '\'' +
                    ", create_time='" + create_time + '\'' +
                    ", user_profile=" + user_profile +
                    ", comments=" + comments +
                    '}';
        }
    }

    public class CreateContent{
        private String type; //内容类型
        private String content;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        @Override
        public String toString() {
            return "CreateContent{" +
                    "type='" + type + '\'' +
                    ", content='" + content + '\'' +
                    '}';
        }
    }
    /**
     * 创建者
     */
    public class CreateProfile{

        private String id; //用户id
        private String nickname; //用户名
        private String avatar; //头像

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

        @Override
        public String toString() {
            return "CreateProfile{" +
                    "id='" + id + '\'' +
                    ", nickname='" + nickname + '\'' +
                    ", avatar='" + avatar + '\'' +
                    '}';
        }
    }

    /**
     * 回复内容列表
     */
    public class Conmment{

        private String id;
        private ArrayList<ReplyContent> content;  //回复内容
        private String user_id;  //用户id
        private String reply_id;  //回复的id
        private String to_user_id;  //被回复人的id
        private String create_time;  //创建时间
        private ReplyProfile user_profile;  //回复人
        private ByProfile to_user_profile;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public ArrayList<ReplyContent> getContent() {
            return content;
        }

        public void setContent(ArrayList<ReplyContent> content) {
            this.content = content;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getReply_id() {
            return reply_id;
        }

        public void setReply_id(String reply_id) {
            this.reply_id = reply_id;
        }

        public String getTo_user_id() {
            return to_user_id;
        }

        public void setTo_user_id(String to_user_id) {
            this.to_user_id = to_user_id;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public ReplyProfile getUser_profile() {
            return user_profile;
        }

        public void setUser_profile(ReplyProfile user_profile) {
            this.user_profile = user_profile;
        }

        public ByProfile getTo_user_profile() {
            return to_user_profile;
        }

        public void setTo_user_profile(ByProfile to_user_profile) {
            this.to_user_profile = to_user_profile;
        }

        @Override
        public String toString() {
            return "Conmment{" +
                    "id='" + id + '\'' +
                    ", content=" + content +
                    ", user_id='" + user_id + '\'' +
                    ", reply_id='" + reply_id + '\'' +
                    ", to_user_id='" + to_user_id + '\'' +
                    ", create_time='" + create_time + '\'' +
                    ", user_profile=" + user_profile +
                    ", to_user_profile=" + to_user_profile +
                    '}';
        }
    }

    public class ReplyContent{
        private String type; //内容类型
        private String content;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        @Override
        public String toString() {
            return "ReplyContent{" +
                    "type='" + type + '\'' +
                    ", content='" + content + '\'' +
                    '}';
        }
    }
    /**
     * 回复人
     */
    public class ReplyProfile{
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

        @Override
        public String toString() {
            return "ReplyProfile{" +
                    "id='" + id + '\'' +
                    ", nickname='" + nickname + '\'' +
                    ", avatar='" + avatar + '\'' +
                    '}';
        }
    }

    /**
     * 被回复人
     */
    public class ByProfile{

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

        @Override
        public String toString() {
            return "ByProfile{" +
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

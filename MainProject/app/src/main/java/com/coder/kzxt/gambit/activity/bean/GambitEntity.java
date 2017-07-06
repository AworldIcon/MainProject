package com.coder.kzxt.gambit.activity.bean;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class GambitEntity implements Serializable {

    private String id;//话题id
    private String userName;//用户名
    private String userFace;//用户头像
    private String hitNum;
    private String postNum;
    private String className;
    private String classId;
    private String userId;
    private String userGender;//用户性别
    private String title;//话题标题
    private String contentText; //话题内容
    private String isLiked;//是否赞过 0否1是
    private String likeNum;//赞过的数量
    private String createdTime;//创建时间
    private String audioUrl;//语音地址
    private String audioDuration;//语音播放时间
    private ArrayList<String> imgs;// 图片地址
    private ArrayList<HashMap<String, String>> list_msg; // 话题内容

    private String isTop;//是否置顶,0.否,1.是
    private String isElite;//是否设为精华0.否,1.是
    private String hasAccess; //是有否删除权限 0否，1是


    public void setId(String id) {
        this.id = id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserFace(String userFace) {
        this.userFace = userFace;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setIsTop(String isTop) {
        this.isTop = isTop;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public void setAudioDuration(String audioDuration) {
        this.audioDuration = audioDuration;
    }

    public void setImgs(ArrayList<String> imgs) {
        this.imgs = imgs;
    }

    public String getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserFace() {
        return userFace;
    }

    public String getTitle() {
        return title;
    }

    public String getIsTop() {
        return isTop;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public String getAudioDuration() {
        return audioDuration;
    }

    public ArrayList<String> getImgs() {
        return imgs;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public ArrayList<HashMap<String, String>> getList_msg() {
        return list_msg;
    }

    public void setList_msg(ArrayList<HashMap<String, String>> list_msg) {
        this.list_msg = list_msg;
    }


    public String getHitNum() {
        return hitNum;
    }

    public String getPostNum() {
        return postNum;
    }

    public String getClassName() {
        return className;
    }

    public String getClassId() {
        return classId;
    }

    public String getUserId() {
        return userId;
    }


    public void setHitNum(String hitNum) {
        this.hitNum = hitNum;
    }

    public void setPostNum(String postNum) {
        this.postNum = postNum;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContentText() {
        return contentText;
    }

    public void setContentText(String contentText) {
        this.contentText = contentText;
    }

    public String getIsLiked() {
        return isLiked;
    }

    public String getLikeNum() {
        return likeNum;
    }

    public void setIsLiked(String isLiked) {
        this.isLiked = isLiked;
    }

    public void setLikeNum(String likeNum) {
        this.likeNum = likeNum;
    }

    public String getIsElite() {
        return isElite;
    }

    public String getHasAccess() {
        return hasAccess;
    }

    public void setIsElite(String isElite) {
        this.isElite = isElite;
    }

    public void setHasAccess(String hasAccess) {
        this.hasAccess = hasAccess;
    }

}

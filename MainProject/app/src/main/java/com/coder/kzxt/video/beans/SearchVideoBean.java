package com.coder.kzxt.video.beans;

import java.util.ArrayList;

/**
 * 搜索直播bean
 * Created by wangtingshun on 2017/3/13.
 */

public class SearchVideoBean {

    private String total;  //总计

    private ArrayList<SearchVideo> list;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public ArrayList<SearchVideo> getList() {
        return list;
    }

    public void setList(ArrayList<SearchVideo> list) {
        this.list = list;
    }

    public class SearchVideo {

        private String liveTitle;
        private String channelId;
        private String hls;
        private String rtmp;
        private String flv;
        private String courseId;
        private String publicCourse;
        private String teacher;
        private String createUid;
        private String editUid;
        private String readyStartTime;
        private String readyEndTime;
        private String realStartTime;
        private String realEndTime;
        private String createTime;
        private String editTime;
        private String viewRange;
        private String liveStatus;
        private String playback;
        private String isDel;
        private String isFree;
        private String isClose;
        private String isOpenChatRoom;
        private String chatRoomStatus;
        private String chatRoomId;
        private String roomId;
        private String liveVideoStatus;
        private String isVideo;
        private String webCode;
        private String owebPriv;
        private String isPush;
        private String pushWay;
        private String pushTime;
        private String pushRange;
        private String isReoeat;
        private String isSuccessPush;
        private String className;
        private String courseTitle;

        private String middlePicture;
        private String selectPictureIdx;
        private String liveUrl;
        private String coverImage;
        private String courseClassId;
        private String viewUserNum;
        private String userId;
        private String identifier;
        private String aboutBegin;

        public String getLiveTitle() {
            return liveTitle;
        }

        public void setLiveTitle(String liveTitle) {
            this.liveTitle = liveTitle;
        }

        public String getChannelId() {
            return channelId;
        }

        public void setChannelId(String channelId) {
            this.channelId = channelId;
        }

        public String getHls() {
            return hls;
        }

        public void setHls(String hls) {
            this.hls = hls;
        }

        public String getRtmp() {
            return rtmp;
        }

        public void setRtmp(String rtmp) {
            this.rtmp = rtmp;
        }

        public String getFlv() {
            return flv;
        }

        public void setFlv(String flv) {
            this.flv = flv;
        }

        public String getCourseId() {
            return courseId;
        }

        public void setCourseId(String courseId) {
            this.courseId = courseId;
        }

        public String getPublicCourse() {
            return publicCourse;
        }

        public void setPublicCourse(String publicCourse) {
            this.publicCourse = publicCourse;
        }

        public String getTeacher() {
            return teacher;
        }

        public void setTeacher(String teacher) {
            this.teacher = teacher;
        }

        public String getCreateUid() {
            return createUid;
        }

        public void setCreateUid(String createUid) {
            this.createUid = createUid;
        }

        public String getEditUid() {
            return editUid;
        }

        public void setEditUid(String editUid) {
            this.editUid = editUid;
        }

        public String getReadyStartTime() {
            return readyStartTime;
        }

        public void setReadyStartTime(String readyStartTime) {
            this.readyStartTime = readyStartTime;
        }

        public String getReadyEndTime() {
            return readyEndTime;
        }

        public void setReadyEndTime(String readyEndTime) {
            this.readyEndTime = readyEndTime;
        }

        public String getRealStartTime() {
            return realStartTime;
        }

        public void setRealStartTime(String realStartTime) {
            this.realStartTime = realStartTime;
        }

        public String getRealEndTime() {
            return realEndTime;
        }

        public void setRealEndTime(String realEndTime) {
            this.realEndTime = realEndTime;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getEditTime() {
            return editTime;
        }

        public void setEditTime(String editTime) {
            this.editTime = editTime;
        }

        public String getViewRange() {
            return viewRange;
        }

        public void setViewRange(String viewRange) {
            this.viewRange = viewRange;
        }

        public String getLiveStatus() {
            return liveStatus;
        }

        public void setLiveStatus(String liveStatus) {
            this.liveStatus = liveStatus;
        }

        public String getPlayback() {
            return playback;
        }

        public void setPlayback(String playback) {
            this.playback = playback;
        }

        public String getIsDel() {
            return isDel;
        }

        public void setIsDel(String isDel) {
            this.isDel = isDel;
        }

        public String getIsFree() {
            return isFree;
        }

        public void setIsFree(String isFree) {
            this.isFree = isFree;
        }

        public String getIsClose() {
            return isClose;
        }

        public void setIsClose(String isClose) {
            this.isClose = isClose;
        }

        public String getIsOpenChatRoom() {
            return isOpenChatRoom;
        }

        public void setIsOpenChatRoom(String isOpenChatRoom) {
            this.isOpenChatRoom = isOpenChatRoom;
        }

        public String getChatRoomStatus() {
            return chatRoomStatus;
        }

        public void setChatRoomStatus(String chatRoomStatus) {
            this.chatRoomStatus = chatRoomStatus;
        }

        public String getChatRoomId() {
            return chatRoomId;
        }

        public void setChatRoomId(String chatRoomId) {
            this.chatRoomId = chatRoomId;
        }

        public String getRoomId() {
            return roomId;
        }

        public void setRoomId(String roomId) {
            this.roomId = roomId;
        }

        public String getLiveVideoStatus() {
            return liveVideoStatus;
        }

        public void setLiveVideoStatus(String liveVideoStatus) {
            this.liveVideoStatus = liveVideoStatus;
        }

        public String getIsVideo() {
            return isVideo;
        }

        public void setIsVideo(String isVideo) {
            this.isVideo = isVideo;
        }

        public String getWebCode() {
            return webCode;
        }

        public void setWebCode(String webCode) {
            this.webCode = webCode;
        }

        public String getOwebPriv() {
            return owebPriv;
        }

        public void setOwebPriv(String owebPriv) {
            this.owebPriv = owebPriv;
        }

        public String getIsPush() {
            return isPush;
        }

        public void setIsPush(String isPush) {
            this.isPush = isPush;
        }

        public String getPushWay() {
            return pushWay;
        }

        public void setPushWay(String pushWay) {
            this.pushWay = pushWay;
        }

        public String getPushTime() {
            return pushTime;
        }

        public void setPushTime(String pushTime) {
            this.pushTime = pushTime;
        }

        public String getPushRange() {
            return pushRange;
        }

        public void setPushRange(String pushRange) {
            this.pushRange = pushRange;
        }

        public String getIsReoeat() {
            return isReoeat;
        }

        public void setIsReoeat(String isReoeat) {
            this.isReoeat = isReoeat;
        }

        public String getIsSuccessPush() {
            return isSuccessPush;
        }

        public void setIsSuccessPush(String isSuccessPush) {
            this.isSuccessPush = isSuccessPush;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public String getCourseTitle() {
            return courseTitle;
        }

        public void setCourseTitle(String courseTitle) {
            this.courseTitle = courseTitle;
        }

        public String getMiddlePicture() {
            return middlePicture;
        }

        public void setMiddlePicture(String middlePicture) {
            this.middlePicture = middlePicture;
        }

        public String getSelectPictureIdx() {
            return selectPictureIdx;
        }

        public void setSelectPictureIdx(String selectPictureIdx) {
            this.selectPictureIdx = selectPictureIdx;
        }

        public String getLiveUrl() {
            return liveUrl;
        }

        public void setLiveUrl(String liveUrl) {
            this.liveUrl = liveUrl;
        }

        public String getCoverImage() {
            return coverImage;
        }

        public void setCoverImage(String coverImage) {
            this.coverImage = coverImage;
        }

        public String getCourseClassId() {
            return courseClassId;
        }

        public void setCourseClassId(String courseClassId) {
            this.courseClassId = courseClassId;
        }

        public String getViewUserNum() {
            return viewUserNum;
        }

        public void setViewUserNum(String viewUserNum) {
            this.viewUserNum = viewUserNum;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getIdentifier() {
            return identifier;
        }

        public void setIdentifier(String identifier) {
            this.identifier = identifier;
        }

        public String getAboutBegin() {
            return aboutBegin;
        }

        public void setAboutBegin(String aboutBegin) {
            this.aboutBegin = aboutBegin;
        }
    }


}

package com.coder.kzxt.classe.beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 上传照片bean
 * Created by wangtingshun on 2017/3/17.
 */

public class PhotoBeanResult {

    private int code;
    private String msg;
    private String xhprof;
    private String runTm;
    private String mem;
    private String server;
    private String serverTime;
    private int page;
    private ArrayList<PhotoBean> data;
    private int totalPages;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getXhprof() {
        return xhprof;
    }

    public void setXhprof(String xhprof) {
        this.xhprof = xhprof;
    }

    public String getRunTm() {
        return runTm;
    }

    public void setRunTm(String runTm) {
        this.runTm = runTm;
    }

    public String getMem() {
        return mem;
    }

    public void setMem(String mem) {
        this.mem = mem;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getServerTime() {
        return serverTime;
    }

    public void setServerTime(String serverTime) {
        this.serverTime = serverTime;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public ArrayList<PhotoBean> getData() {
        return data;
    }

    public void setData(ArrayList<PhotoBean> data) {
        this.data = data;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public class PhotoBean implements Serializable{
        private String goodNum;
        private String isGood;
        private String photoId;
        private String picHeight;
        private String picWidth;
        private String pictureUrl;
        private String uploadAvatar;//上传者头像
        private String uploadName;//上传者姓名
        private String senderId;//上传者id

        public String getSenderId() {
            return senderId;
        }

        public void setSenderId(String senderId) {
            this.senderId = senderId;
        }

        public String getUploadName() {
            return uploadName;
        }

        public void setUploadName(String uploadName) {
            this.uploadName = uploadName;
        }

        public String getGoodNum() {
            return goodNum;
        }

        public void setGoodNum(String goodNum) {
            this.goodNum = goodNum;
        }

        public String getIsGood() {
            return isGood;
        }

        public void setIsGood(String isGood) {
            this.isGood = isGood;
        }

        public String getPhotoId() {
            return photoId;
        }

        public void setPhotoId(String photoId) {
            this.photoId = photoId;
        }

        public String getPicHeight() {
            return picHeight;
        }

        public void setPicHeight(String picHeight) {
            this.picHeight = picHeight;
        }

        public String getPicWidth() {
            return picWidth;
        }

        public void setPicWidth(String picWidth) {
            this.picWidth = picWidth;
        }

        public String getPictureUrl() {
            return pictureUrl;
        }

        public void setPictureUrl(String pictureUrl) {
            this.pictureUrl = pictureUrl;
        }

        public String getUploadAvatar() {
            return uploadAvatar;
        }

        public void setUploadAvatar(String uploadAvatar) {
            this.uploadAvatar = uploadAvatar;
        }

    }
}

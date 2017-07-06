package com.coder.kzxt.classe.beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 学生申请bean
 * Created by wangtingshun on 2017/3/18.
 */

public class ClassApplyResult {

    private String code;
    private String msg;
    private String xhprof;
    private String runTm;
    private String mem;
    private String server;
    private String serverTime;
    private int totalPages;

    private ArrayList<ClassApplyBean> data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
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

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public ArrayList<ClassApplyBean> getData() {
        return data;
    }

    public void setData(ArrayList<ClassApplyBean> data) {
        this.data = data;
    }

    public class ClassApplyBean implements Serializable{

     private String id;
     private String applyTime;
     private String opTime;
     private String opName;
     private String email;
     private String mobile;
     private String userFace;
     private String userGender;
     private String userName;
     private String status;
     private String isTeacher;
     private String studNum;
     private String idPhotoUrl;
        //是否同意
        private boolean isAgree = false;

        public boolean isAgree() {
            return isAgree;
        }

        public void setAgree(boolean agree) {
            isAgree = agree;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getApplyTime() {
            return applyTime;
        }

        public void setApplyTime(String applyTime) {
            this.applyTime = applyTime;
        }

        public String getOpTime() {
            return opTime;
        }

        public void setOpTime(String opTime) {
            this.opTime = opTime;
        }

        public String getOpName() {
            return opName;
        }

        public void setOpName(String opName) {
            this.opName = opName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getUserFace() {
            return userFace;
        }

        public void setUserFace(String userFace) {
            this.userFace = userFace;
        }

        public String getUserGender() {
            return userGender;
        }

        public void setUserGender(String userGender) {
            this.userGender = userGender;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getIsTeacher() {
            return isTeacher;
        }

        public void setIsTeacher(String isTeacher) {
            this.isTeacher = isTeacher;
        }

        public String getStudNum() {
            return studNum;
        }

        public void setStudNum(String studNum) {
            this.studNum = studNum;
        }

        public String getIdPhotoUrl() {
            return idPhotoUrl;
        }

        public void setIdPhotoUrl(String idPhotoUrl) {
            this.idPhotoUrl = idPhotoUrl;
        }
    }

}

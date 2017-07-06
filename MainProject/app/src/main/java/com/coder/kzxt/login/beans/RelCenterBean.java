package com.coder.kzxt.login.beans;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/6.
 */

public class RelCenterBean {

    private int code;
    private String msg;
    private String xhprof;
    private String runTm;
    private String mem;
    private String server;
    private String serverTime;
    private ArrayList<RelateCenterBean> data;

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

    public ArrayList<RelateCenterBean> getData() {
        return data;
    }

    public void setData(ArrayList<RelateCenterBean> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "RelCenterBean{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", xhprof='" + xhprof + '\'' +
                ", runTm='" + runTm + '\'' +
                ", mem='" + mem + '\'' +
                ", server='" + server + '\'' +
                ", serverTime='" + serverTime + '\'' +
                ", data=" + data +
                '}';
    }

    public class RelateCenterBean{

        private String uid;
        private String email;
        private String mobile;
        private String userface;
        private BindLocalBean bindLocalUser;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
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

        public String getUserface() {
            return userface;
        }

        public void setUserface(String userface) {
            this.userface = userface;
        }

        public BindLocalBean getBindLocalUser() {
            return bindLocalUser;
        }

        public void setBindLocalUser(BindLocalBean bindLocalUser) {
            this.bindLocalUser = bindLocalUser;
        }

        @Override
        public String toString() {
            return "RelateCenterBean{" +
                    "uid='" + uid + '\'' +
                    ", email='" + email + '\'' +
                    ", mobile='" + mobile + '\'' +
                    ", userface='" + userface + '\'' +
                    ", bindLocalUser=" + bindLocalUser +
                    '}';
        }
    }

    public class BindLocalBean{
        private String uid;
        private String email;
        private String mobile;
        private String userface;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
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

        public String getUserface() {
            return userface;
        }

        public void setUserface(String userface) {
            this.userface = userface;
        }

        @Override
        public String toString() {
            return "BindLocalBean{" +
                    "uid='" + uid + '\'' +
                    ", email='" + email + '\'' +
                    ", mobile='" + mobile + '\'' +
                    ", userface='" + userface + '\'' +
                    '}';
        }
    }

}

package com.coder.kzxt.setting.beans;

import java.io.Serializable;

/**
 * 通知设置
 * Created by Administrator on 2017/3/9.
 */

public class NotifySettingBean implements Serializable {
    private Data data;
    private int code;
    private String msg;
    private String xhprof;
    private String runTm;
    private String mem;
    private String server;
    private String serverTime;

    public void setData(Data data) {
        this.data = data;
    }

    public Data getData() {
        return this.data;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setXhprof(String xhprof) {
        this.xhprof = xhprof;
    }

    public String getXhprof() {
        return this.xhprof;
    }

    public void setRunTm(String runTm) {
        this.runTm = runTm;
    }

    public String getRunTm() {
        return this.runTm;
    }

    public void setMem(String mem) {
        this.mem = mem;
    }

    public String getMem() {
        return this.mem;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getServer() {
        return this.server;
    }

    public void setServerTime(String serverTime) {
        this.serverTime = serverTime;
    }

    public String getServerTime() {
        return this.serverTime;
    }

    public class Data {
        private String noticeclass;
        private String poster;
        private String course;
        private String identity;
        private String sign;
        private String chat;
        private String night;
        private String custom;
        private String readBeginTime;

        public void setNoticeclass(String noticeclass) {
            this.noticeclass = noticeclass;
        }

        public String getNoticeclass() {
            return this.noticeclass;
        }

        public void setPoster(String poster) {
            this.poster = poster;
        }

        public String getPoster() {
            return this.poster;
        }

        public void setCourse(String course) {
            this.course = course;
        }

        public String getCourse() {
            return this.course;
        }

        public void setIdentity(String identity) {
            this.identity = identity;
        }

        public String getIdentity() {
            return this.identity;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getSign() {
            return this.sign;
        }

        public void setChat(String chat) {
            this.chat = chat;
        }

        public String getChat() {
            return this.chat;
        }

        public void setNight(String night) {
            this.night = night;
        }

        public String getNight() {
            return this.night;
        }

        public void setCustom(String custom) {
            this.custom = custom;
        }

        public String getCustom() {
            return this.custom;
        }

        public void setReadBeginTime(String readBeginTime) {
            this.readBeginTime = readBeginTime;
        }

        public String getReadBeginTime() {
            return this.readBeginTime;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "noticeclass='" + noticeclass + '\'' +
                    ", poster='" + poster + '\'' +
                    ", course='" + course + '\'' +
                    ", identity='" + identity + '\'' +
                    ", sign='" + sign + '\'' +
                    ", chat='" + chat + '\'' +
                    ", night='" + night + '\'' +
                    ", custom='" + custom + '\'' +
                    ", readBeginTime='" + readBeginTime + '\'' +
                    '}';
        }
    }
}

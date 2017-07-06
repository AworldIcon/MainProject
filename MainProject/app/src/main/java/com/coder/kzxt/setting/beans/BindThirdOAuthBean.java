package com.coder.kzxt.setting.beans;

/**
 * 三方登陆绑定本地账号
 * Created by Administrator on 2017/3/7.
 */

public class BindThirdOAuthBean {

    private int code;
    private String msg;
    private String xhprof;
    private String runTm;
    private String mem;
    private String server;
    private String serverTime;
    private BindThirdBean data;

    public String getRunTm() {
        return runTm;
    }

    public void setRunTm(String runTm) {
        this.runTm = runTm;
    }

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

    public BindThirdBean getData() {
        return data;
    }

    public void setData(BindThirdBean data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "BindThirdOAuthBean{" +
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

    public class BindThirdBean{

        private String uid;
        private String openId;
        private String oauth_token;
        private String oauth_token_secret;
        private String nickname;
        private String mobile;
        private String email;
        private String sex;
        private String cash;
        private String gold;
        private String userface;
        private String signature;
        private String studNum;
        private String isTeacher;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getOpenId() {
            return openId;
        }

        public void setOpenId(String openId) {
            this.openId = openId;
        }

        public String getOauth_token() {
            return oauth_token;
        }

        public void setOauth_token(String oauth_token) {
            this.oauth_token = oauth_token;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getOauth_token_secret() {
            return oauth_token_secret;
        }

        public void setOauth_token_secret(String oauth_token_secret) {
            this.oauth_token_secret = oauth_token_secret;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getCash() {
            return cash;
        }

        public void setCash(String cash) {
            this.cash = cash;
        }

        public String getGold() {
            return gold;
        }

        public void setGold(String gold) {
            this.gold = gold;
        }

        public String getUserface() {
            return userface;
        }

        public void setUserface(String userface) {
            this.userface = userface;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public String getStudNum() {
            return studNum;
        }

        public void setStudNum(String studNum) {
            this.studNum = studNum;
        }

        public String getIsTeacher() {
            return isTeacher;
        }

        public void setIsTeacher(String isTeacher) {
            this.isTeacher = isTeacher;
        }

        @Override
        public String toString() {
            return "BindThirdBean{" +
                    "uid='" + uid + '\'' +
                    ", openId='" + openId + '\'' +
                    ", oauth_token='" + oauth_token + '\'' +
                    ", oauth_token_secret='" + oauth_token_secret + '\'' +
                    ", nickname='" + nickname + '\'' +
                    ", mobile='" + mobile + '\'' +
                    ", email='" + email + '\'' +
                    ", sex='" + sex + '\'' +
                    ", cash='" + cash + '\'' +
                    ", gold='" + gold + '\'' +
                    ", userface='" + userface + '\'' +
                    ", signature='" + signature + '\'' +
                    ", studNum='" + studNum + '\'' +
                    ", isTeacher='" + isTeacher + '\'' +
                    '}';
        }
    }

}

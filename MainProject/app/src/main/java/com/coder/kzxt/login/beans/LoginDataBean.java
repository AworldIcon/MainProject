package com.coder.kzxt.login.beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by wangtingshun on 2017/3/1.
 */

public class LoginDataBean {

    private int code;
    private String msg;
    private String xhprof;
    private String runTm;
    private String mem;
    private String server;
    private String serverTime;

    private LoginBean data;

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

    public LoginBean getData() {
        return data;
    }

    public void setData(LoginBean data) {
        this.data = data;
    }

    public static class LoginBean implements Serializable {

        private String uid;
        private String email;
        private String mobile;
        private String gold;
        private String signature;
        private String sex;
        private String nickname;
        private String studNum;
        private String userface;
        private String deviceId;
        private String oauth_token;
        private String oauth_token_secret;
        private String isTeacher;
        private String bannerurl;
        private String qqName;
        private String wxName;
        private String openId;
        private String cash;
        private String iosCash;
        private String account;
        private String loginUseIdentity;
        private String idPhoto;
        private String isCloudLogin;

        private CloudCenterUser cloudLogin_centerUser;
        private CloundCenterBindUser cloudLogin_bindCenterUser;
        private ArrayList<CloundCenterUnbindUser> cloudLogin_unbindLocalUsers;

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

        public String getGold() {
            return gold;
        }

        public void setGold(String gold) {
            this.gold = gold;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getStudNum() {
            return studNum;
        }

        public void setStudNum(String studNum) {
            this.studNum = studNum;
        }

        public String getUserface() {
            return userface;
        }

        public void setUserface(String userface) {
            this.userface = userface;
        }

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public String getOauth_token() {
            return oauth_token;
        }

        public void setOauth_token(String oauth_token) {
            this.oauth_token = oauth_token;
        }

        public String getOauth_token_secret() {
            return oauth_token_secret;
        }

        public void setOauth_token_secret(String oauth_token_secret) {
            this.oauth_token_secret = oauth_token_secret;
        }

        public String getUserType() {
            return isTeacher;
        }

        public void setUserType(String isTeacher) {
            this.isTeacher = isTeacher;
        }

        public String getBannerurl() {
            return bannerurl;
        }

        public void setBannerurl(String bannerurl) {
            this.bannerurl = bannerurl;
        }

        public String getQqName() {
            return qqName;
        }

        public void setQqName(String qqName) {
            this.qqName = qqName;
        }

        public String getWxName() {
            return wxName;
        }

        public void setWxName(String wxName) {
            this.wxName = wxName;
        }

        public String getOpenId() {
            return openId;
        }

        public void setOpenId(String openId) {
            this.openId = openId;
        }

        public String getCash() {
            return cash;
        }

        public void setCash(String cash) {
            this.cash = cash;
        }

        public String getIosCash() {
            return iosCash;
        }

        public void setIosCash(String iosCash) {
            this.iosCash = iosCash;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getLoginUseIdentity() {
            return loginUseIdentity;
        }

        public void setLoginUseIdentity(String loginUseIdentity) {
            this.loginUseIdentity = loginUseIdentity;
        }

        public String getIdPhoto() {
            return idPhoto;
        }

        public void setIdPhoto(String idPhoto) {
            this.idPhoto = idPhoto;
        }

        public String getIsCloudLogin() {
            return isCloudLogin;
        }

        public void setIsCloudLogin(String isCloudLogin) {
            this.isCloudLogin = isCloudLogin;
        }

        public CloudCenterUser getCloudLogin_centerUser() {
            return cloudLogin_centerUser;
        }

        public void setCloudLogin_centerUser(CloudCenterUser cloudLogin_centerUser) {
            this.cloudLogin_centerUser = cloudLogin_centerUser;
        }

        public CloundCenterBindUser getCloudLogin_bindCenterUser() {
            return cloudLogin_bindCenterUser;
        }

        public void setCloudLogin_bindCenterUser(CloundCenterBindUser cloudLogin_bindCenterUser) {
            this.cloudLogin_bindCenterUser = cloudLogin_bindCenterUser;
        }

        public ArrayList<CloundCenterUnbindUser> getCloudLogin_unbindLocalUsers() {
            return cloudLogin_unbindLocalUsers;
        }

        public void setCloudLogin_unbindLocalUsers(ArrayList<CloundCenterUnbindUser> cloudLogin_unbindLocalUsers) {
            this.cloudLogin_unbindLocalUsers = cloudLogin_unbindLocalUsers;
        }

        @Override
        public String toString() {
            return "Bean{" +
                    "uid='" + uid + '\'' +
                    ", email='" + email + '\'' +
                    ", mobile='" + mobile + '\'' +
                    ", gold='" + gold + '\'' +
                    ", signature='" + signature + '\'' +
                    ", sex='" + sex + '\'' +
                    ", nickname='" + nickname + '\'' +
                    ", studNum='" + studNum + '\'' +
                    ", userface='" + userface + '\'' +
                    ", deviceId='" + deviceId + '\'' +
                    ", oauth_token='" + oauth_token + '\'' +
                    ", oauth_token_secret='" + oauth_token_secret + '\'' +
                    ", isTeacher='" + isTeacher + '\'' +
                    ", bannerurl='" + bannerurl + '\'' +
                    ", qqName='" + qqName + '\'' +
                    ", wxName='" + wxName + '\'' +
                    ", openId='" + openId + '\'' +
                    ", cash='" + cash + '\'' +
                    ", iosCash='" + iosCash + '\'' +
                    ", account='" + account + '\'' +
                    ", loginUseIdentity='" + loginUseIdentity + '\'' +
                    ", idPhoto='" + idPhoto + '\'' +
                    ", isCloudLogin='" + isCloudLogin + '\'' +
                    ", cloudLogin_centerUser=" + cloudLogin_centerUser +
                    ", cloudLogin_bindCenterUser=" + cloudLogin_bindCenterUser +
                    ", cloudLogin_unbindLocalUsers=" + cloudLogin_unbindLocalUsers +
                    '}';
        }
    }

    public class CloudCenterUser implements Serializable{

        private String uid;
        private String account;
        private String userface;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getUserface() {
            return userface;
        }

        public void setUserface(String userface) {
            this.userface = userface;
        }

        @Override
        public String toString() {
            return "CloudCenterUser{" +
                    "uid='" + uid + '\'' +
                    ", account='" + account + '\'' +
                    ", userface='" + userface + '\'' +
                    '}';
        }
    }


    public class CloundCenterBindUser implements Serializable{
        private String uid;
        private String account;
        private String userface;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getUserface() {
            return userface;
        }

        public void setUserface(String userface) {
            this.userface = userface;
        }

        @Override
        public String toString() {
            return "CloundCenterBindUser{" +
                    "uid='" + uid + '\'' +
                    ", account='" + account + '\'' +
                    ", userface='" + userface + '\'' +
                    '}';
        }
    }


    public class CloundCenterUnbindUser implements Serializable{
        private String uid;
        private String account;
        private String userface;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getUserface() {
            return userface;
        }

        public void setUserface(String userface) {
            this.userface = userface;
        }

        @Override
        public String toString() {
            return "CloundCenterUnbindUser{" +
                    "uid='" + uid + '\'' +
                    ", account='" + account + '\'' +
                    ", userface='" + userface + '\'' +
                    '}';
        }
    }

}

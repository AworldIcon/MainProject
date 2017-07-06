package com.coder.kzxt.setting.beans;

/**
 * 账户信息bean
 * Created by Administrator on 2017/3/7.
 */

public class AccountInfoBean {

    private int code;
    private String msg;
    private String xhprof;
    private String runTm;
    private String mem;
    private String server;
    private String serverTime;
    private AccountInfo data;

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

    public AccountInfo getData() {
        return data;
    }

    public void setData(AccountInfo data) {
        this.data = data;
    }

    public class AccountInfo {

        private String email;
        private String mobile;
        private String thirdName;
        private String qqName;
        private String wxName;
        private String qqToken;
        private String wxToken;
        private String studNum;
        private String id;
        private String nickname;
        private String sex;
        private String signature;
        private String userface;
        private String gold;
        private String isTeacher;
        private String idPhoto;
        private String check_local_take_delivery_enabled;
        private String check_cloud_take_delivery_enabled;

        private DefaultLocalAddress defaultLocalAddress;
        private DefaultCloudAddress defaultCloudAddress;

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

        public String getThirdName() {
            return thirdName;
        }

        public void setThirdName(String thirdName) {
            this.thirdName = thirdName;
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

        public String getQqToken() {
            return qqToken;
        }

        public void setQqToken(String qqToken) {
            this.qqToken = qqToken;
        }

        public String getWxToken() {
            return wxToken;
        }

        public void setWxToken(String wxToken) {
            this.wxToken = wxToken;
        }

        public String getStudNum() {
            return studNum;
        }

        public void setStudNum(String studNum) {
            this.studNum = studNum;
        }

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

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public String getUserface() {
            return userface;
        }

        public void setUserface(String userface) {
            this.userface = userface;
        }

        public String getGold() {
            return gold;
        }

        public void setGold(String gold) {
            this.gold = gold;
        }

        public String getIsTeacher() {
            return isTeacher;
        }

        public void setIsTeacher(String isTeacher) {
            this.isTeacher = isTeacher;
        }

        public String getIdPhoto() {
            return idPhoto;
        }

        public void setIdPhoto(String idPhoto) {
            this.idPhoto = idPhoto;
        }

        public String getCheck_local_take_delivery_enabled() {
            return check_local_take_delivery_enabled;
        }

        public void setCheck_local_take_delivery_enabled(String check_local_take_delivery_enabled) {
            this.check_local_take_delivery_enabled = check_local_take_delivery_enabled;
        }

        public String getCheck_cloud_take_delivery_enabled() {
            return check_cloud_take_delivery_enabled;
        }

        public void setCheck_cloud_take_delivery_enabled(String check_cloud_take_delivery_enabled) {
            this.check_cloud_take_delivery_enabled = check_cloud_take_delivery_enabled;
        }

        public DefaultLocalAddress getDefaultLocalAddress() {
            return defaultLocalAddress;
        }

        public void setDefaultLocalAddress(DefaultLocalAddress defaultLocalAddress) {
            this.defaultLocalAddress = defaultLocalAddress;
        }

        public DefaultCloudAddress getDefaultCloudAddress() {
            return defaultCloudAddress;
        }

        public void setDefaultCloudAddress(DefaultCloudAddress defaultCloudAddress) {
            this.defaultCloudAddress = defaultCloudAddress;
        }

        @Override
        public String toString() {
            return "AccountInfo{" +
                    "email='" + email + '\'' +
                    ", mobile='" + mobile + '\'' +
                    ", thirdName='" + thirdName + '\'' +
                    ", qqName='" + qqName + '\'' +
                    ", wxName='" + wxName + '\'' +
                    ", qqToken='" + qqToken + '\'' +
                    ", wxToken='" + wxToken + '\'' +
                    ", studNum='" + studNum + '\'' +
                    ", id='" + id + '\'' +
                    ", nickname='" + nickname + '\'' +
                    ", sex='" + sex + '\'' +
                    ", signature='" + signature + '\'' +
                    ", userface='" + userface + '\'' +
                    ", gold='" + gold + '\'' +
                    ", isTeacher='" + isTeacher + '\'' +
                    ", idPhoto='" + idPhoto + '\'' +
                    ", check_local_take_delivery_enabled='" + check_local_take_delivery_enabled + '\'' +
                    ", check_cloud_take_delivery_enabled='" + check_cloud_take_delivery_enabled + '\'' +
                    ", defaultLocalAddress=" + defaultLocalAddress +
                    ", defaultCloudAddress=" + defaultCloudAddress +
                    '}';
        }
    }

    public class DefaultLocalAddress{

        private String id;
        private String userId;
        private String receiver;
        private String province;
        private String city;
        private String district;
        private String addressDetail;
        private String mobile;
        private String email;
        private String qq;
        private String createTime;
        private String updateTime;
        private String webCode;
        private String isDefault;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getReceiver() {
            return receiver;
        }

        public void setReceiver(String receiver) {
            this.receiver = receiver;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public String getAddressDetail() {
            return addressDetail;
        }

        public void setAddressDetail(String addressDetail) {
            this.addressDetail = addressDetail;
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

        public String getQq() {
            return qq;
        }

        public void setQq(String qq) {
            this.qq = qq;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public String getWebCode() {
            return webCode;
        }

        public void setWebCode(String webCode) {
            this.webCode = webCode;
        }

        public String getIsDefault() {
            return isDefault;
        }

        public void setIsDefault(String isDefault) {
            this.isDefault = isDefault;
        }

        @Override
        public String toString() {
            return "DefaultLocalAddress{" +
                    "id='" + id + '\'' +
                    ", userId='" + userId + '\'' +
                    ", receiver='" + receiver + '\'' +
                    ", province='" + province + '\'' +
                    ", city='" + city + '\'' +
                    ", district='" + district + '\'' +
                    ", addressDetail='" + addressDetail + '\'' +
                    ", mobile='" + mobile + '\'' +
                    ", email='" + email + '\'' +
                    ", qq='" + qq + '\'' +
                    ", createTime='" + createTime + '\'' +
                    ", updateTime='" + updateTime + '\'' +
                    ", webCode='" + webCode + '\'' +
                    ", isDefault='" + isDefault + '\'' +
                    '}';
        }
    }

    public class DefaultCloudAddress{

    }

}

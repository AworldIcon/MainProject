package com.coder.kzxt.setting.beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/11/22.
 */
public class MyAddressResult {
    private int code;
    private String msg;
    private String xhprof;
    private String runTm;
    private String mem;
    private String server;
    private ArrayList<MyAddressBean> data;

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

    public ArrayList<MyAddressBean> getData() {
        return data;
    }

    public void setData(ArrayList<MyAddressBean> data) {
        this.data = data;
    }

    public class MyAddressBean implements Serializable {

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
        private String address;
        private int createTime;
        private int updateTime;
        private int isDefault;// 0=否 1=是
        private String webCode;

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

        public int getCreateTime() {
            return createTime;
        }

        public void setCreateTime(int createTime) {
            this.createTime = createTime;
        }

        public int getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(int updateTime) {
            this.updateTime = updateTime;
        }

        public int getIsDefault() {
            return isDefault;
        }

        public void setIsDefault(int isDefault) {
            this.isDefault = isDefault;
        }

        public String getWebCode() {
            return webCode;
        }

        public void setWebCode(String webCode) {
            this.webCode = webCode;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        @Override
        public String toString() {
            return "MyAddressBean{" +
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
                    ", address='" + address + '\'' +
                    ", createTime=" + createTime +
                    ", updateTime=" + updateTime +
                    ", isDefault=" + isDefault +
                    ", webCode='" + webCode + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "MyAddressResult{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", xhprof='" + xhprof + '\'' +
                ", runTm='" + runTm + '\'' +
                ", mem='" + mem + '\'' +
                ", server='" + server + '\'' +
                ", data=" + data +
                '}';
    }
}

package com.coder.kzxt.order.beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 用户地址
 * Created by wangtingshun on 2017/4/11.
 */

public class AddressBean implements Serializable{

    private String code;
    private String message;

    private ArrayList<Address> items;

    private Paginate paginate;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<Address> getItems() {
        return items;
    }

    public void setItems(ArrayList<Address> items) {
        this.items = items;
    }

    public Paginate getPaginate() {
        return paginate;
    }

    public void setPaginate(Paginate paginate) {
        this.paginate = paginate;
    }

    public class Paginate{

        private int currentPage;
        private int pageSize;
        private int pageNum;
        private int totalNum;

        public int getCurrentPage() {
            return currentPage;
        }

        public void setCurrentPage(int currentPage) {
            this.currentPage = currentPage;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getPageNum() {
            return pageNum;
        }

        public void setPageNum(int pageNum) {
            this.pageNum = pageNum;
        }

        public int getTotalNum() {
            return totalNum;
        }

        public void setTotalNum(int totalNum) {
            this.totalNum = totalNum;
        }
    }


    public class Address implements Serializable{

        private String id;
        private String user_id;
        private String receiver;
        private String province;
        private String city;
        private String district;
        private String address_detail;
        private String mobile;
        private String email;
        private String is_default;
        private String create_time;
        private String update_time;
        private String delete_time;

        public String getId() {
            if(id == null){
                return "";
            }
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
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

        public String getAddress_detail() {
            return address_detail;
        }

        public void setAddress_detail(String address_detail) {
            this.address_detail = address_detail;
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

        public String getIs_default() {
            return is_default;
        }

        public void setIs_default(String is_default) {
            this.is_default = is_default;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(String update_time) {
            this.update_time = update_time;
        }

        public String getDelete_time() {
            return delete_time;
        }

        public void setDelete_time(String delete_time) {
            this.delete_time = delete_time;
        }

        @Override
        public String toString() {
            return "Address{" +
                    "id='" + id + '\'' +
                    ", user_id='" + user_id + '\'' +
                    ", receiver='" + receiver + '\'' +
                    ", province='" + province + '\'' +
                    ", city='" + city + '\'' +
                    ", district='" + district + '\'' +
                    ", address_detail='" + address_detail + '\'' +
                    ", mobile='" + mobile + '\'' +
                    ", email='" + email + '\'' +
                    ", is_default='" + is_default + '\'' +
                    ", create_time='" + create_time + '\'' +
                    ", update_time='" + update_time + '\'' +
                    ", delete_time='" + delete_time + '\'' +
                    '}';
        }
    }
}

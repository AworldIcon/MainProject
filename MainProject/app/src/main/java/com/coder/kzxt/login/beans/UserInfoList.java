package com.coder.kzxt.login.beans;

import java.util.ArrayList;

/**
 * Created by wangtingshun on 2017/6/19.
 * 用户信息列表
 */

public class UserInfoList {

    private String code;
    private String message;
    private Paginate paginate;
    private ArrayList<UserBean> items;

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

    public Paginate getPaginate() {
        return paginate;
    }

    public void setPaginate(Paginate paginate) {
        this.paginate = paginate;
    }

    public ArrayList<UserBean> getItems() {
        return items;
    }

    public void setItems(ArrayList<UserBean> items) {
        this.items = items;
    }

    public class UserBean{

        private String id;
        private String email;
        private String mobile;
        private Profile profile;
        /**
         * 成员是否被添加
         * true 已添加， flse 未添加
         */
        private boolean isAdd = false;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public Profile getProfile() {
            return profile;
        }

        public void setProfile(Profile profile) {
            this.profile = profile;
        }

        public boolean isAdd() {
            return isAdd;
        }

        public void setAdd(boolean add) {
            isAdd = add;
        }

        @Override
        public String toString() {
            return "UserBean{" +
                    "id='" + id + '\'' +
                    ", email='" + email + '\'' +
                    ", mobile='" + mobile + '\'' +
                    ", profile=" + profile +
                    ", isAdd=" + isAdd +
                    '}';
        }
    }
    public class Profile{

        private String id;
        private String gender;
        private String nickname;
        private String avatar;
        private String big_avatar;
        private String birthday;
        private String desc;
        private String remark;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getBig_avatar() {
            return big_avatar;
        }

        public void setBig_avatar(String big_avatar) {
            this.big_avatar = big_avatar;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        @Override
        public String toString() {
            return "Profile{" +
                    "id='" + id + '\'' +
                    ", gender='" + gender + '\'' +
                    ", nickname='" + nickname + '\'' +
                    ", avatar='" + avatar + '\'' +
                    ", big_avatar='" + big_avatar + '\'' +
                    ", birthday='" + birthday + '\'' +
                    ", desc='" + desc + '\'' +
                    ", remark='" + remark + '\'' +
                    '}';
        }
    }

    public class Paginate {
        //当前页数
        private int currentPage;
        //每页记录数
        private int pageSize;
        //总页数
        private int pageNum;
        //总记录数
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

}

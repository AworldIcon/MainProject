package com.coder.kzxt.poster.beans;

import java.util.List;

/**
 * Created by tangcy on 2017/3/8.
 */

public class LikePraiseBean {

    /**
     * code : 200
     * message : ok
     * items : [{"id":832695,"gender":2,"nickname":"he","avatar":"http://192.168.3.86/group1/M00/00/0D/wKgDV1kiXWiAOhBoAAI5hjtXnoU795_80x80.png","big_avatar":"http://192.168.3.86/group1/M00/00/0D/wKgDV1kiXWiAOhBoAAI5hjtXnoU795_180x180.png","birthday":"1970-01-01","desc":"测试个人简介''  ''''''","remark":"","view_count":131,"update_time":1495767026}]
     */

    private int code;
    private String message;
    private List<ItemsBean> items;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ItemsBean> getItems() {
        return items;
    }

    public void setItems(List<ItemsBean> items) {
        this.items = items;
    }

    public static class ItemsBean {
        /**
         * id : 832695
         * gender : 2
         * nickname : he
         * avatar : http://192.168.3.86/group1/M00/00/0D/wKgDV1kiXWiAOhBoAAI5hjtXnoU795_80x80.png
         * big_avatar : http://192.168.3.86/group1/M00/00/0D/wKgDV1kiXWiAOhBoAAI5hjtXnoU795_180x180.png
         * birthday : 1970-01-01
         * desc : 测试个人简介''  ''''''
         * remark :
         * view_count : 131
         * update_time : 1495767026
         */

//        private int id;
//        private int gender;
        private String nickname;
        private String avatar;
//        private String big_avatar;
//        private String birthday;
//        private String desc;
//        private String remark;
//        private int view_count;
//        private int update_time;

//        public int getId() {
//            return id;
//        }
//
//        public void setId(int id) {
//            this.id = id;
//        }
//
//        public int getGender() {
//            return gender;
//        }
//
//        public void setGender(int gender) {
//            this.gender = gender;
//        }

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

//        public String getBig_avatar() {
//            return big_avatar;
//        }
//
//        public void setBig_avatar(String big_avatar) {
//            this.big_avatar = big_avatar;
//        }
//
//        public String getBirthday() {
//            return birthday;
//        }
//
//        public void setBirthday(String birthday) {
//            this.birthday = birthday;
//        }
//
//        public String getDesc() {
//            return desc;
//        }
//
//        public void setDesc(String desc) {
//            this.desc = desc;
//        }
//
//        public String getRemark() {
//            return remark;
//        }
//
//        public void setRemark(String remark) {
//            this.remark = remark;
//        }
//
//        public int getView_count() {
//            return view_count;
//        }
//
//        public void setView_count(int view_count) {
//            this.view_count = view_count;
//        }
//
//        public int getUpdate_time() {
//            return update_time;
//        }
//
//        public void setUpdate_time(int update_time) {
//            this.update_time = update_time;
//        }
    }
}

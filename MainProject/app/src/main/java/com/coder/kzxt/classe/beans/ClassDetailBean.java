package com.coder.kzxt.classe.beans;

import java.io.Serializable;

/**
 * Created by wangtingshun on 2017/6/9.
 * 班级详情的bean
 */

public class ClassDetailBean {

    private String code;
    private String message;
    private ClassDetail item;

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

    public ClassDetail getItem() {
        return item;
    }

    public void setItem(ClassDetail item) {
        this.item = item;
    }

    public class ClassDetail implements Serializable{

        private static final long serialVersionUID = 4688537441319121280L;
        private String id;
        private String name;
        private String year;
        private String member_count;
        private String logo;
        private String join_status;
        private String announcement;
        private String self_role;
        private String has_apply;
        private String category_id;
        private String category_name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getMember_count() {
            return member_count;
        }

        public void setMember_count(String member_count) {
            this.member_count = member_count;
        }

        public String getJoin_status() {
            return join_status;
        }

        public void setJoin_status(String join_status) {
            this.join_status = join_status;
        }

        public String getAnnouncement() {
            return announcement;
        }

        public void setAnnouncement(String announcement) {
            this.announcement = announcement;
        }

        public String getSelf_role() {
            return self_role;
        }

        public void setSelf_role(String self_role) {
            this.self_role = self_role;
        }

        public String getHas_apply() {
            return has_apply;
        }

        public void setHas_apply(String has_apply) {
            this.has_apply = has_apply;
        }

        public String getCategory_id() {
            return category_id;
        }

        public void setCategory_id(String category_id) {
            this.category_id = category_id;
        }

        public String getCategory_name() {
            return category_name;
        }

        public void setCategory_name(String category_name) {
            this.category_name = category_name;
        }

        @Override
        public String toString() {
            return "ClassDetail{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", year='" + year + '\'' +
                    ", member_count='" + member_count + '\'' +
                    ", logo='" + logo + '\'' +
                    ", join_status='" + join_status + '\'' +
                    ", announcement='" + announcement + '\'' +
                    ", self_role='" + self_role + '\'' +
                    ", has_apply='" + has_apply + '\'' +
                    ", category_id='" + category_id + '\'' +
                    ", category_name='" + category_name + '\'' +
                    '}';
        }
    }

}

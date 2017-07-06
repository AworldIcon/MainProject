package com.coder.kzxt.classe.beans;

import java.util.ArrayList;

/**
 * 搜索班级的bean
 * Created by wangtingshun on 2017/3/13.
 */

public class SearchClassBean {

    private String total;

    private ArrayList<SearchClass> list;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public ArrayList<SearchClass> getList() {
        return list;
    }

    public void setList(ArrayList<SearchClass> list) {
        this.list = list;
    }

    public class SearchClass {

        private String id;
        private String title;
        private String about;
        private String logo;
        private String backgroundLogo;
        private String status;
        private String memberNum;
        private String threadNum;
        private String postNum;
        private String ownerId;
        private String createdTime;
        private String announcement;
        private String categoryId;
        private String permissionOfContent;
        private String joinStatus;
        private String year;
        private String webCode;
        private String owebPriv;
        private String categoryName;
        private String classId;
        private String className;
        private String createName;
        private String createUid;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAbout() {
            return about;
        }

        public void setAbout(String about) {
            this.about = about;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getBackgroundLogo() {
            return backgroundLogo;
        }

        public void setBackgroundLogo(String backgroundLogo) {
            this.backgroundLogo = backgroundLogo;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getMemberNum() {
            return memberNum;
        }

        public void setMemberNum(String memberNum) {
            this.memberNum = memberNum;
        }

        public String getThreadNum() {
            return threadNum;
        }

        public void setThreadNum(String threadNum) {
            this.threadNum = threadNum;
        }

        public String getPostNum() {
            return postNum;
        }

        public void setPostNum(String postNum) {
            this.postNum = postNum;
        }

        public String getOwnerId() {
            return ownerId;
        }

        public void setOwnerId(String ownerId) {
            this.ownerId = ownerId;
        }

        public String getCreatedTime() {
            return createdTime;
        }

        public void setCreatedTime(String createdTime) {
            this.createdTime = createdTime;
        }

        public String getAnnouncement() {
            return announcement;
        }

        public void setAnnouncement(String announcement) {
            this.announcement = announcement;
        }

        public String getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }

        public String getPermissionOfContent() {
            return permissionOfContent;
        }

        public void setPermissionOfContent(String permissionOfContent) {
            this.permissionOfContent = permissionOfContent;
        }

        public String getJoinStatus() {
            return joinStatus;
        }

        public void setJoinStatus(String joinStatus) {
            this.joinStatus = joinStatus;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public String getWebCode() {
            return webCode;
        }

        public void setWebCode(String webCode) {
            this.webCode = webCode;
        }

        public String getOwebPriv() {
            return owebPriv;
        }

        public void setOwebPriv(String owebPriv) {
            this.owebPriv = owebPriv;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public String getClassId() {
            return classId;
        }

        public void setClassId(String classId) {
            this.classId = classId;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public String getCreateName() {
            return createName;
        }

        public void setCreateName(String createName) {
            this.createName = createName;
        }

        public String getCreateUid() {
            return createUid;
        }

        public void setCreateUid(String createUid) {
            this.createUid = createUid;
        }
    }
}

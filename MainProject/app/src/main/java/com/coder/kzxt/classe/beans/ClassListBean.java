package com.coder.kzxt.classe.beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 班级列表数据
 * Created by Administrator on 2017/3/11.
 */

public class ClassListBean {

    private String code;
    private String msg;
    private String xhprof;
    private String runTm;
    private String mem;
    private String server;
    private String serverTime;
    private String page;
    private String totalPage;
    private ArrayList<ClassBean> data;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
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

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(String totalPage) {
        this.totalPage = totalPage;
    }

    public ArrayList<ClassBean> getData() {
        return data;
    }

    public void setData(ArrayList<ClassBean> data) {
        this.data = data;
    }

    /**
     * 班级列表bean
     */
    public class ClassBean implements Serializable{
        private String classId;
        private String className;
        private String logo;
        private String categoryId;
        private String memberNum;
        private String createUid;
        private String status;
        private String createName;
        private String categoryName;

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

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }

        public String getMemberNum() {
            return memberNum;
        }

        public void setMemberNum(String memberNum) {
            this.memberNum = memberNum;
        }

        public String getCreateUid() {
            return createUid;
        }

        public void setCreateUid(String createUid) {
            this.createUid = createUid;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCreateName() {
            return createName;
        }

        public void setCreateName(String createName) {
            this.createName = createName;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        @Override
        public String toString() {
            return "ClassBean{" +
                    "classId='" + classId + '\'' +
                    ", className='" + className + '\'' +
                    ", logo='" + logo + '\'' +
                    ", categoryId='" + categoryId + '\'' +
                    ", memberNum='" + memberNum + '\'' +
                    ", createUid='" + createUid + '\'' +
                    ", status='" + status + '\'' +
                    ", createName='" + createName + '\'' +
                    ", categoryName='" + categoryName + '\'' +
                    '}';
        }
    }

}

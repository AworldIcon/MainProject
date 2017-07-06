package com.coder.kzxt.classe.beans;

import java.util.ArrayList;

/**
 * 班级课程
 * Created by wangtingshun on 2017/3/16.
 */

public class ClassCourseResult {

    private String code;
    private String msg;
    private String xhprof;
    private String runTm;
    private String mem;
    private String server;
    private String serverTime;
    private String page;
    private String totalPage;

   private ArrayList<ClassCourseBean> data;

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

    public ArrayList<ClassCourseBean> getData() {
        return data;
    }

    public void setData(ArrayList<ClassCourseBean> data) {
        this.data = data;
    }

    public class ClassCourseBean {

        private String lessonNum;
        private String subtitle;
        private String title;
        private String treeid;
        private String courseId;
        private String publicCourse;
        private String pic;
        private String shareurl;
        private String studyNum;
        private String timeLength;
        private String price;
        private String teacher;
        private String creater;
        private String createdTime;

        public String getLessonNum() {
            return lessonNum;
        }

        public void setLessonNum(String lessonNum) {
            this.lessonNum = lessonNum;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public void setSubtitle(String subtitle) {
            this.subtitle = subtitle;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTreeid() {
            return treeid;
        }

        public void setTreeid(String treeid) {
            this.treeid = treeid;
        }

        public String getCourseId() {
            return courseId;
        }

        public void setCourseId(String courseId) {
            this.courseId = courseId;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getPublicCourse() {
            return publicCourse;
        }

        public void setPublicCourse(String publicCourse) {
            this.publicCourse = publicCourse;
        }

        public String getShareurl() {
            return shareurl;
        }

        public void setShareurl(String shareurl) {
            this.shareurl = shareurl;
        }

        public String getStudyNum() {
            return studyNum;
        }

        public void setStudyNum(String studyNum) {
            this.studyNum = studyNum;
        }

        public String getTimeLength() {
            return timeLength;
        }

        public void setTimeLength(String timeLength) {
            this.timeLength = timeLength;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getTeacher() {
            return teacher;
        }

        public void setTeacher(String teacher) {
            this.teacher = teacher;
        }

        public String getCreater() {
            return creater;
        }

        public void setCreater(String creater) {
            this.creater = creater;
        }

        public String getCreatedTime() {
            return createdTime;
        }

        public void setCreatedTime(String createdTime) {
            this.createdTime = createdTime;
        }

        @Override
        public String toString() {
            return "ClassCourseBean{" +
                    "lessonNum='" + lessonNum + '\'' +
                    ", subtitle='" + subtitle + '\'' +
                    ", title='" + title + '\'' +
                    ", treeid='" + treeid + '\'' +
                    ", courseId='" + courseId + '\'' +
                    ", publicCourse='" + publicCourse + '\'' +
                    ", pic='" + pic + '\'' +
                    ", shareurl='" + shareurl + '\'' +
                    ", studyNum='" + studyNum + '\'' +
                    ", timeLength='" + timeLength + '\'' +
                    ", price='" + price + '\'' +
                    ", teacher='" + teacher + '\'' +
                    ", creater='" + creater + '\'' +
                    ", createdTime='" + createdTime + '\'' +
                    '}';
        }
    }

}

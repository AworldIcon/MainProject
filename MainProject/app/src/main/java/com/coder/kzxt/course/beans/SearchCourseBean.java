package com.coder.kzxt.course.beans;

import java.util.ArrayList;

/**
 * 搜索课程的bean
 * Created by wangtingshun on 2017/3/13.
 */

public class SearchCourseBean {

    private String total;
    private ArrayList<SearchCourse> list;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public ArrayList<SearchCourse> getList() {
        return list;
    }

    public void setList(ArrayList<SearchCourse> list) {
        this.list = list;
    }

    public class SearchCourse{

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

        public String getPublicCourse() {
            return publicCourse;
        }

        public void setPublicCourse(String publicCourse) {
            this.publicCourse = publicCourse;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
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
    }

}

package com.coder.kzxt.course.beans;

import java.util.List;

/**
 * Created by pc on 2017/3/11.
 */

public class GoodCourseResult {

    /**
     * data : {"course":[{"lessonNum":"0","subtitle":"","title":"PHP","treeid":"4649","courseId":"4649","publicCourse":0,"pic":"http://192.168.3.6:88/Public/assets/img/default/largecoursePicture?5.1.4","shareurl":"http://192.168.3.6:88/Course/Course/showAction/id/4649","studyNum":"0","timeLength":0,"price":100,"teacher":"Messiah","creater":"Messiah","createdTime":"1487835800"},{"lessonNum":"0","subtitle":"","title":"测试12.22","treeid":"4508","courseId":"4508","publicCourse":0,"pic":"http://192.168.3.6:88/Public/assets/img/default/largecoursePicture?5.1.4","shareurl":"http://192.168.3.6:88/Course/Course/showAction/id/4508","studyNum":"10","timeLength":0,"price":10,"teacher":"","creater":"旺旺","createdTime":"1482397628"},{"lessonNum":"1","subtitle":"","title":"HTML入门基础","treeid":"4475","courseId":"4475","publicCourse":0,"pic":"http://192.168.3.6:88/Public/files/course/2016/12-13/103511f123e0122940.jpg.w300.jpg","shareurl":"http://192.168.3.6:88/Course/Course/showAction/id/4475","studyNum":"18","timeLength":5,"price":1000,"teacher":"","creater":"老刘","createdTime":"1481596162"}],"total":3,"page":1,"totalPage":1,"pageNum":20}
     * code : 1000
     * msg : 成功
     * xhprof : no setup
     * runTm : s:1489214442.230 e:1489214442.632 tms=402ms
     * mem : 23.92 MB
     * server : wt1.nazhengshu.com
     * serverTime : 1489214442
     */

    private DataBean data;
    private int code;
    private String msg;
    private String xhprof;
    private String runTm;
    private String mem;
    private String server;
    private String serverTime;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

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

    public static class DataBean {
        /**
         * course : [{"lessonNum":"0","subtitle":"","title":"PHP","treeid":"4649","courseId":"4649","publicCourse":0,"pic":"http://192.168.3.6:88/Public/assets/img/default/largecoursePicture?5.1.4","shareurl":"http://192.168.3.6:88/Course/Course/showAction/id/4649","studyNum":"0","timeLength":0,"price":100,"teacher":"Messiah","creater":"Messiah","createdTime":"1487835800"},{"lessonNum":"0","subtitle":"","title":"测试12.22","treeid":"4508","courseId":"4508","publicCourse":0,"pic":"http://192.168.3.6:88/Public/assets/img/default/largecoursePicture?5.1.4","shareurl":"http://192.168.3.6:88/Course/Course/showAction/id/4508","studyNum":"10","timeLength":0,"price":10,"teacher":"","creater":"旺旺","createdTime":"1482397628"},{"lessonNum":"1","subtitle":"","title":"HTML入门基础","treeid":"4475","courseId":"4475","publicCourse":0,"pic":"http://192.168.3.6:88/Public/files/course/2016/12-13/103511f123e0122940.jpg.w300.jpg","shareurl":"http://192.168.3.6:88/Course/Course/showAction/id/4475","studyNum":"18","timeLength":5,"price":1000,"teacher":"","creater":"老刘","createdTime":"1481596162"}]
         * total : 3
         * page : 1
         * totalPage : 1
         * pageNum : 20
         */

        private int total;
        private int page;
        private int totalPage;
        private int pageNum;
        private List<CourseBean> course;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public int getTotalPage() {
            return totalPage;
        }

        public void setTotalPage(int totalPage) {
            this.totalPage = totalPage;
        }

        public int getPageNum() {
            return pageNum;
        }

        public void setPageNum(int pageNum) {
            this.pageNum = pageNum;
        }

        public List<CourseBean> getCourse() {
            return course;
        }

        public void setCourse(List<CourseBean> course) {
            this.course = course;
        }

        public static class CourseBean {
            /**
             * lessonNum : 0
             * subtitle :
             * title : PHP
             * treeid : 4649
             * courseId : 4649
             * publicCourse : 0
             * pic : http://192.168.3.6:88/Public/assets/img/default/largecoursePicture?5.1.4
             * shareurl : http://192.168.3.6:88/Course/Course/showAction/id/4649
             * studyNum : 0
             * timeLength : 0
             * price : 100
             * teacher : Messiah
             * creater : Messiah
             * createdTime : 1487835800
             */

            private String lessonNum;
            private String subtitle;
            private String title;
            private String treeid;
            private String courseId;
            private String publicCourse;
            private String pic;
            private String shareurl;
            private String studyNum;
            private int timeLength;
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

            public int getTimeLength() {
                return timeLength;
            }

            public void setTimeLength(int timeLength) {
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
}

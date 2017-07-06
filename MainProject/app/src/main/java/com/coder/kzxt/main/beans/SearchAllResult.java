package com.coder.kzxt.main.beans;

import com.coder.kzxt.classe.beans.SearchClassBean;
import com.coder.kzxt.course.beans.SearchCourseBean;
import com.coder.kzxt.video.beans.SearchVideoBean;

/**
 * 搜索全部（课程，班级，直播）
 * Created by wangtingshun on 2017/3/13.
 */

public class SearchAllResult {

    private int code;
    private String msg;
    private String xhprof;
    private String runTm;
    private String mem;
    private String server;
    private String serverTime;
    private SearchAllBean data;

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

    public String getRunTm() {
        return runTm;
    }

    public void setRunTm(String runTm) {
        this.runTm = runTm;
    }

    public String getXhprof() {
        return xhprof;
    }

    public void setXhprof(String xhprof) {
        this.xhprof = xhprof;
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

    public SearchAllBean getData() {
        return data;
    }

    public void setData(SearchAllBean data) {
        this.data = data;
    }

    public class SearchAllBean{
        //搜索直播
        private SearchVideoBean videoList;
        //搜索课程
        private SearchCourseBean courseList;
        //搜索班级
        private SearchClassBean classList;


        public SearchVideoBean getVideoList() {
            return videoList;
        }

        public void setVideoList(SearchVideoBean videoList) {
            this.videoList = videoList;
        }

        public SearchCourseBean getCourseList() {
            return courseList;
        }

        public void setCourseList(SearchCourseBean courseList) {
            this.courseList = courseList;
        }

        public SearchClassBean getClassList() {
            return classList;
        }

        public void setClassList(SearchClassBean classList) {
            this.classList = classList;
        }
    }

}

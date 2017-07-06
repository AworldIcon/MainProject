package com.coder.kzxt.classe.beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 班级课程表
 * Created by wangtingshun on 2017/3/16.
 */

public class CourseTableResult {

    private String code;
    private String msg;
    private String xhprof;
    private String runTm;
    private String mem;
    private String server;
    private String serverTime;
    private CourseTable data;

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

    public String getServerTime() {
        return serverTime;
    }

    public void setServerTime(String serverTime) {
        this.serverTime = serverTime;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public CourseTable getData() {
        return data;
    }

    public void setData(CourseTable data) {
        this.data = data;
    }

    public static class CourseTable{
        private DayBean day_1;
        private DayBean day_2;
        private DayBean day_3;
        private DayBean day_4;
        private DayBean day_5;
        private DayBean day_6;
        private DayBean day_7;

        public DayBean getDay_1() {
            return day_1;
        }

        public void setDay_1(DayBean day_1) {
            this.day_1 = day_1;
        }

        public DayBean getDay_2() {
            return day_2;
        }

        public void setDay_2(DayBean day_2) {
            this.day_2 = day_2;
        }

        public DayBean getDay_3() {
            return day_3;
        }

        public void setDay_3(DayBean day_3) {
            this.day_3 = day_3;
        }

        public DayBean getDay_4() {
            return day_4;
        }

        public void setDay_4(DayBean day_4) {
            this.day_4 = day_4;
        }

        public DayBean getDay_5() {
            return day_5;
        }

        public void setDay_5(DayBean day_5) {
            this.day_5 = day_5;
        }

        public DayBean getDay_6() {
            return day_6;
        }

        public void setDay_6(DayBean day_6) {
            this.day_6 = day_6;
        }

        public DayBean getDay_7() {
            return day_7;
        }

        public void setDay_7(DayBean day_7) {
            this.day_7 = day_7;
        }
    }

    public class DayBean implements Serializable{

        private String tip;
        private ArrayList<Day> list;

        public String getTip() {
            return tip;
        }

        public void setTip(String tip) {
            this.tip = tip;
        }

        public ArrayList<Day> getList() {
            return list;
        }

        public void setList(ArrayList<Day> list) {
            this.list = list;
        }

        public class Day {
            private String courseName;
            private String classroom;
            private String startTime;
            private String endTime;

            public String getCourseName() {
                return courseName;
            }

            public void setCourseName(String courseName) {
                this.courseName = courseName;
            }

            public String getClassroom() {
                return classroom;
            }

            public void setClassroom(String classroom) {
                this.classroom = classroom;
            }

            public String getStartTime() {
                return startTime;
            }

            public void setStartTime(String startTime) {
                this.startTime = startTime;
            }

            public String getEndTime() {
                return endTime;
            }

            public void setEndTime(String endTime) {
                this.endTime = endTime;
            }
        }
    }

}

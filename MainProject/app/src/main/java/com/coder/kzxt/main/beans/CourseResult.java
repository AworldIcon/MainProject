package com.coder.kzxt.main.beans;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MaShiZhao on 16/9/20.
 */
public class CourseResult {

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
        private int totalPage;
        /**
         * userId : 234
         * userName : 花粉管
         * userFace : http://sdfsdfsd.png
         */

        private List<ListBean> list;
        private List<ListBean> waiting;
        private List<ListBean> running;

        public int getTotalPage() {
            return totalPage;
        }

        public void setTotalPage(int totalPage) {
            this.totalPage = totalPage;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public List<ListBean> getWaiting() {
            if(waiting == null) return new ArrayList<>();
            return waiting;
        }

        public void setWaiting(List<ListBean> waiting) {
            this.waiting = waiting;
        }

        public List<ListBean> getRunning() {
            if(running == null) return new ArrayList<>();
            return running;
        }

        public void setRunning(List<ListBean> running) {
            this.running = running;
        }

        public static class ListBean {
            private String aboutBegin;
            private String chatRoomId;
            private String className;
            private String classId;
            private String courseId;
            private String courseTitle;
            private String coverImage;
            private String isFree;
            private String isOpenChatRoom;
            private String liveId;
            private String liveStatus;
            private String liveTitle;
            private String publicCourse;
            private String teacher;
            private String viewUserNum;
            private String schoolName;
            private String lessonNum;
            private String studentNum;
            private String classNum;
            private String lessonLength;
            private String price;
            private String createdTime;
            private List<classListBean> classList;

            public String getCreatedTime() {
                return createdTime;
            }

            public void setCreatedTime(String createdTime) {
                this.createdTime = createdTime;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getLessonLength() {
                return lessonLength;
            }

            public void setLessonLength(String lessonLength) {
                this.lessonLength = lessonLength;
            }

            public String getStudentNum() {
                return studentNum;
            }

           public void setStudentNum(String studentNumber) {
                this.studentNum = studentNumber;
            }

            public String getLessonNum() {
                return lessonNum;
            }

            public void setLessonNum(String studyTime) {
                this.lessonNum = studyTime;
            }

            public String getSchoolName() {
                if (schoolName == null) return "";
                return schoolName;
            }

            public void setSchoolName(String schoolName) {
                this.schoolName = schoolName;
            }


            public String getChatRoomId() {
                return chatRoomId;
            }

            public void setChatRoomId(String chatRoomId) {
                this.chatRoomId = chatRoomId;
            }

            public String getClassName() {
                return className;
            }

            public void setClassName(String className) {
                this.className = className;
            }

            public String getClassId() {
                return classId;
            }

            public void setClassId(String courseClassId) {
                this.classId = courseClassId;
            }

            public String getCourseId() {
                return courseId;
            }

            public void setCourseId(String courseId) {
                this.courseId = courseId;
            }

            public String getCourseTitle() {
                return courseTitle;
            }

            public void setCourseTitle(String courseTitle) {
                this.courseTitle = courseTitle;
            }

            public String getCoverImage() {
                return coverImage;
            }

            public void setCoverImage(String coverImage) {
                this.coverImage = coverImage;
            }

            public String getIsFree() {
                return isFree;
            }

            public void setIsFree(String isFree) {
                this.isFree = isFree;
            }

            public String getIsOpenChatRoom() {
                return isOpenChatRoom;
            }

            public void setIsOpenChatRoom(String isOpenChatRoom) {
                this.isOpenChatRoom = isOpenChatRoom;
            }

            public String getLiveId() {
                return liveId;
            }

            public void setLiveId(String liveId) {
                this.liveId = liveId;
            }

            public String getLiveStatus() {
                return liveStatus;
            }

            public void setLiveStatus(String liveStatus) {
                this.liveStatus = liveStatus;
            }

            public String getLiveTitle() {
                return liveTitle;
            }

            public void setLiveTitle(String liveTitle) {
                this.liveTitle = liveTitle;
            }

            public String getPublicCourse() {
                return publicCourse;
            }

            public void setPublicCourse(String publicCourse) {
                this.publicCourse = publicCourse;
            }

            public String getTeacher() {
                return teacher;
            }

            public void setTeacher(String teacher) {
                this.teacher = teacher;
            }

            public String getViewUserNum() {
                return viewUserNum;
            }

            public void setViewUserNum(String viewUserNum) {
                this.viewUserNum = viewUserNum;
            }

            public String getAboutBegin() {

                return aboutBegin;
            }

            public void setAboutBegin(String aboutBegin) {
                this.aboutBegin = aboutBegin;
            }

            public String getClassNum() {
                if(TextUtils.isEmpty(classNum)){
                    return getClassList().size()+"";
                }
                return classNum;
            }

            public void setClassNum(String classNum) {
                this.classNum = classNum;
            }

            public List<classListBean> getClassList() {
                if(classList == null){
                 return  new ArrayList<>();
                }
                return classList;
            }

            public void setClassList(List<classListBean> classList) {
                this.classList = classList;
            }


            public static class classListBean {
                private String classId;
                private String className;
                private String image;
                private String price;
                private String signInNumber;
                private String courseId;
                private String courseName;

                public String getCourseId() {
                    return courseId;
                }

                public void setCourseId(String courseId) {
                    this.courseId = courseId;
                }

                public String getCourseName() {
                    return courseName;
                }

                public void setCourseName(String courseName) {
                    this.courseName = courseName;
                }

                private List<TeacherBean> teachers;

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

                public String getImage() {
                    return image;
                }

                public void setImage(String image) {
                    this.image = image;
                }

                public String getPrice() {
                    return price;
                }

                public void setPrice(String price) {
                    this.price = price;
                }

                public String getSignInNumber() {
                    return signInNumber;
                }

                public void setSignInNumber(String signInNumber) {
                    this.signInNumber = signInNumber;
                }

                public List<TeacherBean> getTeachers() {
                    return teachers;
                }

                public void setTeachers(List<TeacherBean> teachers) {
                    this.teachers = teachers;
                }
            }
        }
    }


}

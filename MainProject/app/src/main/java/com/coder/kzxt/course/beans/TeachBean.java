package com.coder.kzxt.course.beans;

import java.util.ArrayList;

public class TeachBean {

//    private String code;
//    private String message;
//
//    public Paginate paginate;

    private ArrayList<TeachCourseItem> items;


//    public String getCode() {
//        return code;
//    }
//
//    public void setCode(String code) {
//        this.code = code;
//    }
//
//    public String getMessage() {
//        return message;
//    }
//
//    public void setMessage(String message) {
//        this.message = message;
//    }
//
//    public Paginate getPaginate() {
//        return paginate;
//    }
//
//    public void setPaginate(Paginate paginate) {
//        this.paginate = paginate;
//    }

    public ArrayList<TeachCourseItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<TeachCourseItem> items) {
        this.items = items;
    }

    public static class TeachCourseItem {

        private String id;
        private String title;
        private String student_num;
        private String lesson_num;
        private String price;
        private String is_free;
        private String middle_pic;
        private String status;
//        private String expiry_day;
//        private String small_pic;
//        private String large_pic;
//        private String user_id;
//        private String desc;
//        private String source;
//        private String custom_teacher_title;
//        private String custom_desc_title;
//        private String grade;
//        private String create_time;
//        private String update_time;
//        private String delete_time;
//        private String status_text;
//        private boolean hasChild;   //是否有子数据
//        private ArrayList<CourseTeacher> courseteacher;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

//        public String getExpiry_day() {
//            return expiry_day;
//        }
//
//        public void setExpiry_day(String expiry_day) {
//            this.expiry_day = expiry_day;
//        }

        public String getIs_free() {
            return is_free;
        }

        public void setIs_free(String is_free) {
            this.is_free = is_free;
        }

//        public String getSmall_pic() {
//            return small_pic;
//        }
//
//        public void setSmall_pic(String small_pic) {
//            this.small_pic = small_pic;
//        }

        public String getMiddle_pic() {
            return middle_pic;
        }

        public void setMiddle_pic(String middle_pic) {
            this.middle_pic = middle_pic;
        }

//        public String getLarge_pic() {
//            return large_pic;
//        }
//
//        public void setLarge_pic(String large_pic) {
//            this.large_pic = large_pic;
//        }

//        public String getUser_id() {
//            return user_id;
//        }
//
//        public void setUser_id(String user_id) {
//            this.user_id = user_id;
//        }
//
//        public String getDesc() {
//            return desc;
//        }
//
//        public void setDesc(String desc) {
//            this.desc = desc;
//        }

//        public String getSource() {
//            return source;
//        }
//
//        public void setSource(String source) {
//            this.source = source;
//        }

//        public boolean isHasChild() {
//            return hasChild;
//        }
//
//        public void setHasChild(boolean hasChild) {
//            this.hasChild = hasChild;
//        }
//
//        public String getCustom_teacher_title() {
//            return custom_teacher_title;
//        }
//
//        public void setCustom_teacher_title(String custom_teacher_title) {
//            this.custom_teacher_title = custom_teacher_title;
//        }
//
//        public String getCustom_desc_title() {
//            return custom_desc_title;
//        }
//
//        public void setCustom_desc_title(String custom_desc_title) {
//            this.custom_desc_title = custom_desc_title;
//        }
//
//        public String getGrade() {
//            return grade;
//        }
//
//        public void setGrade(String grade) {
//            this.grade = grade;
//        }

        public String getStudent_num() {
            return student_num;
        }

        public void setStudent_num(String student_num) {
            this.student_num = student_num;
        }

        public String getLesson_num() {
            return lesson_num;
        }

        public void setLesson_num(String lesson_num) {
            this.lesson_num = lesson_num;
        }

//        public String getCreate_time() {
//            return create_time;
//        }
//
//        public void setCreate_time(String create_time) {
//            this.create_time = create_time;
//        }
//
//        public String getUpdate_time() {
//            return update_time;
//        }
//
//        public void setUpdate_time(String update_time) {
//            this.update_time = update_time;
//        }
//
//        public String getDelete_time() {
//            return delete_time;
//        }
//
//        public void setDelete_time(String delete_time) {
//            this.delete_time = delete_time;
//        }
//
//        public String getStatus_text() {
//            return status_text;
//        }
//
//        public void setStatus_text(String status_text) {
//            this.status_text = status_text;
//        }
//
//        public ArrayList<CourseTeacher> getCourseteacher() {
//            return courseteacher;
//        }
//
//        public void setCourseteacher(ArrayList<CourseTeacher> courseteacher) {
//            this.courseteacher = courseteacher;
//        }

//        public  static class CourseTeacher {
//
//            private String id;
//            private String user_id;
//            private Profile profile;
//
//            public String getId() {
//                return id;
//            }
//
//            public void setId(String id) {
//                this.id = id;
//            }
//
//            public String getUser_id() {
//                return user_id;
//            }
//
//            public void setUser_id(String user_id) {
//                this.user_id = user_id;
//            }
//
//            public Profile getProfile() {
//                return profile;
//            }
//
//            public void setProfile(Profile profile) {
//                this.profile = profile;
//            }
//
//
//            public class Profile {
//                private String id;
//                private String gender;
//                private String nickname;
//                private String avatar;
//                private String birthday;
//                private String desc;
//
//                public String getId() {
//                    return id;
//                }
//
//                public void setId(String id) {
//                    this.id = id;
//                }
//
//                public String getGender() {
//                    return gender;
//                }
//
//                public void setGender(String gender) {
//                    this.gender = gender;
//                }
//
//                public String getNickname() {
//                    return nickname;
//                }
//
//                public void setNickname(String nickname) {
//                    this.nickname = nickname;
//                }
//
//                public String getAvatar() {
//                    return avatar;
//                }
//
//                public void setAvatar(String avatar) {
//                    this.avatar = avatar;
//                }
//
//                public String getBirthday() {
//                    return birthday;
//                }
//
//                public void setBirthday(String birthday) {
//                    this.birthday = birthday;
//                }
//
//                public String getDesc() {
//                    return desc;
//                }
//
//                public void setDesc(String desc) {
//                    this.desc = desc;
//                }
//            }
//
//        }
    }

//    public class Paginate {
//        private int currentPage;
//        private int pageSize;
//        private int pageNum;
//        private int totalNum;
//
//        public int getCurrentPage() {
//            return currentPage;
//        }
//
//        public void setCurrentPage(int currentPage) {
//            this.currentPage = currentPage;
//        }
//
//        public int getPageSize() {
//            return pageSize;
//        }
//
//        public void setPageSize(int pageSize) {
//            this.pageSize = pageSize;
//        }
//
//        public int getPageNum() {
//            return pageNum;
//        }
//
//        public void setPageNum(int pageNum) {
//            this.pageNum = pageNum;
//        }
//
//        public int getTotalNum() {
//            return totalNum;
//        }
//
//        public void setTotalNum(int totalNum) {
//            this.totalNum = totalNum;
//        }
//    }

}

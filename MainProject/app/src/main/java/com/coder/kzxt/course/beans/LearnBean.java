package com.coder.kzxt.course.beans;

import java.util.List;

/**
 * Created by Administrator on 2017/3/27.
 */

public class LearnBean {


    /**
     * code : 200
     * message : ok
     * paginate : {"currentPage":1,"pageSize":20,"pageNum":1,"totalNum":2}
     * items : [{"id":4,"title":"网站编辑","status":2,"price":0,"expiry_day":0,"is_free":0,"small_pic":"http://wyzc.com/Public/files/course/2016/06-18/0300579b8df0345209.jpg","middle_pic":"http://wyzc.com/Public/files/course/2016/06-18/0300579b8df0345209.jpg","large_pic":"http://wyzc.com/Public/files/course/2016/06-18/0300579b8df0345209.jpg","user_id":916500,"desc":"","source":"","custom_teacher_title":"教师团队","custom_desc_title":"课程概述","lesson_num":0,"create_time":0,"update_time":1490318843,"delete_time":null,"course_class_id":1,"join_type":2,"join_time":1490434688,"course_id":4,"teacher":[{"id":1301,"course_id":4,"course_class_id":0,"user_id":916503,"type":1,"create_time":1490766734,"update_time":1490766734,"delete_time":null},{"id":1202,"course_id":4,"course_class_id":0,"user_id":916453,"type":1,"create_time":1490333774,"update_time":1490333774,"delete_time":null},{"id":1296,"course_id":4,"course_class_id":213,"user_id":916502,"type":1,"create_time":1489390985,"update_time":1489395856,"delete_time":null}],"learn_num":0},{"id":16633,"title":"修的的课程","status":2,"price":0,"expiry_day":0,"is_free":1,"small_pic":"","middle_pic":"http://html.gxy.net:8080/www/theme/courseedit/default/images/default-img2.jpg","large_pic":"","user_id":916500,"desc":null,"source":"","custom_teacher_title":"","custom_desc_title":"","lesson_num":0,"create_time":1491026104,"update_time":1491026138,"delete_time":null,"course_class_id":375,"join_type":2,"join_time":1491033013,"course_id":16633,"teacher":[],"learn_num":0}]
     */

    private int code;
    private String message;
    private PaginateBean paginate;
    private List<ItemsBean> items;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public PaginateBean getPaginate() {
        return paginate;
    }

    public void setPaginate(PaginateBean paginate) {
        this.paginate = paginate;
    }

    public List<ItemsBean> getItems() {
        return items;
    }

    public void setItems(List<ItemsBean> items) {
        this.items = items;
    }

    public static class PaginateBean {
        /**
         * currentPage : 1
         * pageSize : 20
         * pageNum : 1
         * totalNum : 2
         */

        private int currentPage;
        private int pageSize;
        private int pageNum;
        private int totalNum;

        public int getCurrentPage() {
            return currentPage;
        }

        public void setCurrentPage(int currentPage) {
            this.currentPage = currentPage;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getPageNum() {
            return pageNum;
        }

        public void setPageNum(int pageNum) {
            this.pageNum = pageNum;
        }

        public int getTotalNum() {
            return totalNum;
        }

        public void setTotalNum(int totalNum) {
            this.totalNum = totalNum;
        }
    }

    public static class ItemsBean {
        /**
         * id : 4
         * title : 网站编辑
         * status : 2
         * price : 0
         * expiry_day : 0
         * is_free : 0
         * small_pic : http://wyzc.com/Public/files/course/2016/06-18/0300579b8df0345209.jpg
         * middle_pic : http://wyzc.com/Public/files/course/2016/06-18/0300579b8df0345209.jpg
         * large_pic : http://wyzc.com/Public/files/course/2016/06-18/0300579b8df0345209.jpg
         * user_id : 916500
         * desc :
         * source :
         * custom_teacher_title : 教师团队
         * custom_desc_title : 课程概述
         * lesson_num : 0
         * create_time : 0
         * update_time : 1490318843
         * delete_time : null
         * course_class_id : 1
         * join_type : 2
         * join_time : 1490434688
         * course_id : 4
         * teacher : [{"id":1301,"course_id":4,"course_class_id":0,"user_id":916503,"type":1,"create_time":1490766734,"update_time":1490766734,"delete_time":null},{"id":1202,"course_id":4,"course_class_id":0,"user_id":916453,"type":1,"create_time":1490333774,"update_time":1490333774,"delete_time":null},{"id":1296,"course_id":4,"course_class_id":213,"user_id":916502,"type":1,"create_time":1489390985,"update_time":1489395856,"delete_time":null}]
         * learn_num : 0
         */

        private String id;
        private String title;
        private int status;
        private String price;
        private int expiry_day;
        private int is_free;
        private String small_pic;
        private String middle_pic;
        private String large_pic;
        private int user_id;
        private String desc;
        private String source;
        private String custom_teacher_title;
        private String custom_desc_title;
        private int lesson_num;
        private int create_time;
        private int update_time;
        private Object delete_time;
        private int course_class_id;
        private int join_type;
        private int join_time;
        private int course_id;
        private int learn_num;
        private List<TeacherBean> teacher;
        private LearnCourseBean last_learn;

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

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public int getExpiry_day() {
            return expiry_day;
        }

        public void setExpiry_day(int expiry_day) {
            this.expiry_day = expiry_day;
        }

        public int getIs_free() {
            return is_free;
        }

        public void setIs_free(int is_free) {
            this.is_free = is_free;
        }

        public String getSmall_pic() {
            return small_pic;
        }

        public void setSmall_pic(String small_pic) {
            this.small_pic = small_pic;
        }

        public String getMiddle_pic() {
            return middle_pic;
        }

        public void setMiddle_pic(String middle_pic) {
            this.middle_pic = middle_pic;
        }

        public String getLarge_pic() {
            return large_pic;
        }

        public void setLarge_pic(String large_pic) {
            this.large_pic = large_pic;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getCustom_teacher_title() {
            return custom_teacher_title;
        }

        public void setCustom_teacher_title(String custom_teacher_title) {
            this.custom_teacher_title = custom_teacher_title;
        }

        public String getCustom_desc_title() {
            return custom_desc_title;
        }

        public void setCustom_desc_title(String custom_desc_title) {
            this.custom_desc_title = custom_desc_title;
        }

        public int getLesson_num() {
            return lesson_num;
        }

        public void setLesson_num(int lesson_num) {
            this.lesson_num = lesson_num;
        }

        public int getCreate_time() {
            return create_time;
        }

        public void setCreate_time(int create_time) {
            this.create_time = create_time;
        }

        public int getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(int update_time) {
            this.update_time = update_time;
        }

        public Object getDelete_time() {
            return delete_time;
        }

        public void setDelete_time(Object delete_time) {
            this.delete_time = delete_time;
        }

        public int getCourse_class_id() {
            return course_class_id;
        }

        public void setCourse_class_id(int course_class_id) {
            this.course_class_id = course_class_id;
        }

        public int getJoin_type() {
            return join_type;
        }

        public void setJoin_type(int join_type) {
            this.join_type = join_type;
        }

        public int getJoin_time() {
            return join_time;
        }

        public void setJoin_time(int join_time) {
            this.join_time = join_time;
        }

        public int getCourse_id() {
            return course_id;
        }

        public void setCourse_id(int course_id) {
            this.course_id = course_id;
        }

        public int getLearn_num() {
            return learn_num;
        }

        public void setLearn_num(int learn_num) {
            this.learn_num = learn_num;
        }

        public List<TeacherBean> getTeacher() {
            return teacher;
        }

        public void setTeacher(List<TeacherBean> teacher) {
            this.teacher = teacher;
        }

        public LearnCourseBean getLast_learn() {
            return last_learn;
        }

        public void setLast_learn(LearnCourseBean last_learn) {
            this.last_learn = last_learn;
        }

        public static class TeacherBean {
            /**
             * id : 1301
             * course_id : 4
             * course_class_id : 0
             * user_id : 916503
             * type : 1
             * create_time : 1490766734
             * update_time : 1490766734
             * delete_time : null
             */

            private int id;
            private int course_id;
            private int course_class_id;
            private int user_id;
            private int type;
            private int create_time;
            private int update_time;
            private Object delete_time;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getCourse_id() {
                return course_id;
            }

            public void setCourse_id(int course_id) {
                this.course_id = course_id;
            }

            public int getCourse_class_id() {
                return course_class_id;
            }

            public void setCourse_class_id(int course_class_id) {
                this.course_class_id = course_class_id;
            }

            public int getUser_id() {
                return user_id;
            }

            public void setUser_id(int user_id) {
                this.user_id = user_id;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public int getCreate_time() {
                return create_time;
            }

            public void setCreate_time(int create_time) {
                this.create_time = create_time;
            }

            public int getUpdate_time() {
                return update_time;
            }

            public void setUpdate_time(int update_time) {
                this.update_time = update_time;
            }

            public Object getDelete_time() {
                return delete_time;
            }

            public void setDelete_time(Object delete_time) {
                this.delete_time = delete_time;
            }
        }

        public class LearnCourseBean{
            private String id;
            private String gold_num;
            private String user_id;
            private String course_id;
            private String course_lesson_id;
            private String status;
            private String start_time;
            private String finished_time;
            private String learn_time;
            private String watch_time;
            private String video_status;
            private String total_learn_num;
            private String total_learn_time;
            private String create_time;
            private String update_time;
            private String delete_time;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getGold_num() {
                return gold_num;
            }

            public void setGold_num(String gold_num) {
                this.gold_num = gold_num;
            }

            public String getUser_id() {
                return user_id;
            }

            public void setUser_id(String user_id) {
                this.user_id = user_id;
            }

            public String getCourse_id() {
                return course_id;
            }

            public void setCourse_id(String course_id) {
                this.course_id = course_id;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getCourse_lesson_id() {
                return course_lesson_id;
            }

            public void setCourse_lesson_id(String course_lesson_id) {
                this.course_lesson_id = course_lesson_id;
            }

            public String getStart_time() {
                return start_time;
            }

            public void setStart_time(String start_time) {
                this.start_time = start_time;
            }

            public String getFinished_time() {
                return finished_time;
            }

            public void setFinished_time(String finished_time) {
                this.finished_time = finished_time;
            }

            public String getLearn_time() {
                return learn_time;
            }

            public void setLearn_time(String learn_time) {
                this.learn_time = learn_time;
            }

            public String getWatch_time() {
                return watch_time;
            }

            public void setWatch_time(String watch_time) {
                this.watch_time = watch_time;
            }

            public String getVideo_status() {
                return video_status;
            }

            public void setVideo_status(String video_status) {
                this.video_status = video_status;
            }

            public String getTotal_learn_num() {
                return total_learn_num;
            }

            public void setTotal_learn_num(String total_learn_num) {
                this.total_learn_num = total_learn_num;
            }

            public String getTotal_learn_time() {
                return total_learn_time;
            }

            public void setTotal_learn_time(String total_learn_time) {
                this.total_learn_time = total_learn_time;
            }

            public String getCreate_time() {
                return create_time;
            }

            public void setCreate_time(String create_time) {
                this.create_time = create_time;
            }

            public String getUpdate_time() {
                return update_time;
            }

            public void setUpdate_time(String update_time) {
                this.update_time = update_time;
            }

            public String getDelete_time() {
                return delete_time;
            }

            public void setDelete_time(String delete_time) {
                this.delete_time = delete_time;
            }

            @Override
            public String toString() {
                return "LearnCourseBean{" +
                        "id='" + id + '\'' +
                        ", gold_num='" + gold_num + '\'' +
                        ", user_id='" + user_id + '\'' +
                        ", course_id='" + course_id + '\'' +
                        ", course_lesson_id='" + course_lesson_id + '\'' +
                        ", status='" + status + '\'' +
                        ", start_time='" + start_time + '\'' +
                        ", finished_time='" + finished_time + '\'' +
                        ", learn_time='" + learn_time + '\'' +
                        ", watch_time='" + watch_time + '\'' +
                        ", video_status='" + video_status + '\'' +
                        ", total_learn_num='" + total_learn_num + '\'' +
                        ", total_learn_time='" + total_learn_time + '\'' +
                        ", create_time='" + create_time + '\'' +
                        ", update_time='" + update_time + '\'' +
                        ", delete_time='" + delete_time + '\'' +
                        '}';
            }
        }
    }
}

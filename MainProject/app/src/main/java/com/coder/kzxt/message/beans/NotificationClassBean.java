package com.coder.kzxt.message.beans;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.List;

/**
 * Created by zw on 2017/6/16.
 */

public class NotificationClassBean {
    public static HashMap<String,Integer> childList=new HashMap();
    public static HashMap<String,Integer> childSelectList=new HashMap();
    public static String totaldata="";
    public static String classNames="";
    /**
     * code : 200
     * message : ok
     * items : [{"id":20858,"title":"临时课程测试","status":2,"price":"0.00","expiry_day":1000,"is_free":0,"small_pic":"","middle_pic":"http://html.gxy.com/www/theme/courseedit/default/images/default-img7.jpg","large_pic":"","user_id":906994,"desc":"","source":"","custom_teacher_title":"","custom_desc_title":"","grade":0,"student_num":7,"is_center":0,"lesson_num":0,"create_time":1494473069,"update_time":1495704967,"delete_time":"","courseclasshasmany":[{"id":22712,"class_name":"默认授课班","user_id":906994,"course_id":20858,"start_time":1494473069,"end_time":1526009069,"apply_end_time":0,"desc":"","price":"0.00","status":1,"is_default":1,"sort":0,"hidden":0,"user_count":5,"user_limit":1000,"apply_status":1,"create_time":1494473069,"update_time":1495704967,"delete_time":null,"qrcode":0},{"id":22715,"class_name":"123","user_id":906994,"course_id":20858,"start_time":1494432000,"end_time":1495555200,"apply_end_time":0,"desc":"","price":"0.00","status":1,"is_default":0,"sort":0,"hidden":0,"user_count":1,"user_limit":1000,"apply_status":0,"create_time":1494484666,"update_time":1494498502,"delete_time":null,"qrcode":0},{"id":22716,"class_name":"1564651654867894616","user_id":906994,"course_id":20858,"start_time":1494432000,"end_time":1495555200,"apply_end_time":0,"desc":"","price":"0.00","status":1,"is_default":0,"sort":0,"hidden":0,"user_count":1,"user_limit":1000,"apply_status":0,"create_time":1494484686,"update_time":1494502402,"delete_time":null,"qrcode":0}],"status_text":""}]
     */

    private int code;
    private String message;
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

    public List<ItemsBean> getItems() {
        return items;
    }

    public void setItems(List<ItemsBean> items) {
        this.items = items;
    }

    public static class ItemsBean {
        /**
         * id : 20858
         * title : 临时课程测试
         * status : 2
         * price : 0.00
         * expiry_day : 1000
         * is_free : 0
         * small_pic :
         * middle_pic : http://html.gxy.com/www/theme/courseedit/default/images/default-img7.jpg
         * large_pic :
         * user_id : 906994
         * desc :
         * source :
         * custom_teacher_title :
         * custom_desc_title :
         * grade : 0
         * student_num : 7
         * is_center : 0
         * lesson_num : 0
         * create_time : 1494473069
         * update_time : 1495704967
         * delete_time :
         * courseclasshasmany : [{"id":22712,"class_name":"默认授课班","user_id":906994,"course_id":20858,"start_time":1494473069,"end_time":1526009069,"apply_end_time":0,"desc":"","price":"0.00","status":1,"is_default":1,"sort":0,"hidden":0,"user_count":5,"user_limit":1000,"apply_status":1,"create_time":1494473069,"update_time":1495704967,"delete_time":null,"qrcode":0},{"id":22715,"class_name":"123","user_id":906994,"course_id":20858,"start_time":1494432000,"end_time":1495555200,"apply_end_time":0,"desc":"","price":"0.00","status":1,"is_default":0,"sort":0,"hidden":0,"user_count":1,"user_limit":1000,"apply_status":0,"create_time":1494484666,"update_time":1494498502,"delete_time":null,"qrcode":0},{"id":22716,"class_name":"1564651654867894616","user_id":906994,"course_id":20858,"start_time":1494432000,"end_time":1495555200,"apply_end_time":0,"desc":"","price":"0.00","status":1,"is_default":0,"sort":0,"hidden":0,"user_count":1,"user_limit":1000,"apply_status":0,"create_time":1494484686,"update_time":1494502402,"delete_time":null,"qrcode":0}]
         * status_text :
         */

        private int id;
        private String title;
        private int status;
        private int user_id;
        private int student_num;
        private int lesson_num;
        private long create_time;
        private long update_time;
        private String delete_time;
        private String status_text;
        @SerializedName("courseclasshasmany")
        private List<CourseclasshasmanyBean> classX;
        private boolean expand;

        public boolean isExpand() {
            return expand;
        }

        public void setExpand(boolean expand) {
            this.expand = expand;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
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

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public int getStudent_num() {
            return student_num;
        }

        public void setStudent_num(int student_num) {
            this.student_num = student_num;
        }

        public int getLesson_num() {
            return lesson_num;
        }

        public void setLesson_num(int lesson_num) {
            this.lesson_num = lesson_num;
        }

        public long getCreate_time() {
            return create_time;
        }

        public void setCreate_time(long create_time) {
            this.create_time = create_time;
        }

        public long getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(long update_time) {
            this.update_time = update_time;
        }

        public String getDelete_time() {
            return delete_time;
        }

        public void setDelete_time(String delete_time) {
            this.delete_time = delete_time;
        }

        public String getStatus_text() {
            return status_text;
        }

        public void setStatus_text(String status_text) {
            this.status_text = status_text;
        }

        public List<CourseclasshasmanyBean> getClassX() {
            return classX;
        }

        public void setClassX(List<CourseclasshasmanyBean> classX) {
            this.classX = classX;
        }

        public static class CourseclasshasmanyBean {
            /**
             * id : 22712
             * class_name : 默认授课班
             * user_id : 906994
             * course_id : 20858
             * start_time : 1494473069
             * end_time : 1526009069
             * apply_end_time : 0
             * desc :
             * price : 0.00
             * status : 1
             * is_default : 1
             * sort : 0
             * hidden : 0
             * user_count : 5
             * user_limit : 1000
             * apply_status : 1
             * create_time : 1494473069
             * update_time : 1495704967
             * delete_time : null
             * qrcode : 0
             */

            private int id;
            private String class_name;
            private int user_id;
            private int course_id;
            private boolean expand;
            private  int user_count;

            public int getUser_count() {
                return user_count;
            }

            public void setUser_count(int user_count) {
                this.user_count = user_count;
            }

            public boolean isExpand() {
                return expand;
            }

            public void setExpand(boolean expand) {
                this.expand = expand;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getClass_name() {
                return class_name;
            }

            public void setClass_name(String class_name) {
                this.class_name = class_name;
            }

            public int getUser_id() {
                return user_id;
            }

            public void setUser_id(int user_id) {
                this.user_id = user_id;
            }

            public int getCourse_id() {
                return course_id;
            }

            public void setCourse_id(int course_id) {
                this.course_id = course_id;
            }
        }
    }
}

package com.coder.kzxt.message.beans;

/**
 * Created by zw on 2017/6/24.
 */

public class NotificationDetailBean {

    /**
     * code : 200
     * message : ok
     * item : {"id":12,"plat":2,"title":"范德萨发","content":"发发呆撒","is_cron":null,"is_app":1,"receive_type":1,"receive_role":2,"course_class":"20915,22802,|20914,22801,|","category_ids":"","notify_type":1,"notify_time":0,"notify_user":"832695,907004","custom_time":"","status":1,"user_id":906991,"create_time":1497595391,"update_time":1497597010,"delete_time":null,"profile":{"id":906991,"gender":2,"nickname":"4458","avatar":"http://192.168.3.86/group1/M00/00/09/wKgDV1kSqtKAeCvpAAXp1xBNr3A50_50x50.jpeg","birthday":"1970-01-01","desc":"","remark":"","big_avatar":"http://192.168.3.86/group1/M00/00/09/wKgDV1kSqtKAeCvpAAXp1xBNr3A50_180x180.jpeg","view_count":123,"create_time":1493023465,"update_time":1496893440},"platform_text":"PC","status_text":"成功","role_text":"老师","obj":"课程名称-默认授课班,霍贞光签到测试2222-默认授课班,","read_num":2}
     */

    private int code;
    private String message;
    private ItemBean item;

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

    public ItemBean getItem() {
        return item;
    }

    public void setItem(ItemBean item) {
        this.item = item;
    }

    public static class ItemBean {
        /**
         * id : 12
         * plat : 2
         * title : 范德萨发
         * content : 发发呆撒
         * is_cron : null
         * is_app : 1
         * receive_type : 1
         * receive_role : 2
         * course_class : 20915,22802,|20914,22801,|
         * category_ids :
         * notify_type : 1
         * notify_time : 0
         * notify_user : 832695,907004
         * custom_time :
         * status : 1
         * user_id : 906991
         * create_time : 1497595391
         * update_time : 1497597010
         * delete_time : null
         * profile : {"id":906991,"gender":2,"nickname":"4458","avatar":"http://192.168.3.86/group1/M00/00/09/wKgDV1kSqtKAeCvpAAXp1xBNr3A50_50x50.jpeg","birthday":"1970-01-01","desc":"","remark":"","big_avatar":"http://192.168.3.86/group1/M00/00/09/wKgDV1kSqtKAeCvpAAXp1xBNr3A50_180x180.jpeg","view_count":123,"create_time":1493023465,"update_time":1496893440}
         * platform_text : PC
         * status_text : 成功
         * role_text : 老师
         * obj : 课程名称-默认授课班,霍贞光签到测试2222-默认授课班,
         * read_num : 2
         */

        private int id;
        private int plat;
        private String title;
        private String content;
        private Object is_cron;
        private int is_app;
        private int receive_type;
        private int receive_role;
        private String course_class;
        private String category_ids;
        private int notify_type;
        private int notify_time;
        private String notify_user;
        private String custom_time;
        private int status;
        private int user_id;
        private long create_time;
        private long update_time;
        private Object delete_time;
        private ProfileBean profile;
        private String platform_text;
        private String status_text;
        private String role_text;
        private String obj;
        private int read_num;
        private int number;

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getPlat() {
            return plat;
        }

        public void setPlat(int plat) {
            this.plat = plat;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public Object getIs_cron() {
            return is_cron;
        }

        public void setIs_cron(Object is_cron) {
            this.is_cron = is_cron;
        }

        public int getIs_app() {
            return is_app;
        }

        public void setIs_app(int is_app) {
            this.is_app = is_app;
        }

        public int getReceive_type() {
            return receive_type;
        }

        public void setReceive_type(int receive_type) {
            this.receive_type = receive_type;
        }

        public int getReceive_role() {
            return receive_role;
        }

        public void setReceive_role(int receive_role) {
            this.receive_role = receive_role;
        }

        public String getCourse_class() {
            return course_class;
        }

        public void setCourse_class(String course_class) {
            this.course_class = course_class;
        }

        public String getCategory_ids() {
            return category_ids;
        }

        public void setCategory_ids(String category_ids) {
            this.category_ids = category_ids;
        }

        public int getNotify_type() {
            return notify_type;
        }

        public void setNotify_type(int notify_type) {
            this.notify_type = notify_type;
        }

        public int getNotify_time() {
            return notify_time;
        }

        public void setNotify_time(int notify_time) {
            this.notify_time = notify_time;
        }

        public String getNotify_user() {
            return notify_user;
        }

        public void setNotify_user(String notify_user) {
            this.notify_user = notify_user;
        }

        public String getCustom_time() {
            return custom_time;
        }

        public void setCustom_time(String custom_time) {
            this.custom_time = custom_time;
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

        public Object getDelete_time() {
            return delete_time;
        }

        public void setDelete_time(Object delete_time) {
            this.delete_time = delete_time;
        }

        public ProfileBean getProfile() {
            return profile;
        }

        public void setProfile(ProfileBean profile) {
            this.profile = profile;
        }

        public String getPlatform_text() {
            return platform_text;
        }

        public void setPlatform_text(String platform_text) {
            this.platform_text = platform_text;
        }

        public String getStatus_text() {
            return status_text;
        }

        public void setStatus_text(String status_text) {
            this.status_text = status_text;
        }

        public String getRole_text() {
            return role_text;
        }

        public void setRole_text(String role_text) {
            this.role_text = role_text;
        }

        public String getObj() {
            return obj;
        }

        public void setObj(String obj) {
            this.obj = obj;
        }

        public int getRead_num() {
            return read_num;
        }

        public void setRead_num(int read_num) {
            this.read_num = read_num;
        }

        public static class ProfileBean {
            /**
             * id : 906991
             * gender : 2
             * nickname : 4458
             * avatar : http://192.168.3.86/group1/M00/00/09/wKgDV1kSqtKAeCvpAAXp1xBNr3A50_50x50.jpeg
             * birthday : 1970-01-01
             * desc :
             * remark :
             * big_avatar : http://192.168.3.86/group1/M00/00/09/wKgDV1kSqtKAeCvpAAXp1xBNr3A50_180x180.jpeg
             * view_count : 123
             * create_time : 1493023465
             * update_time : 1496893440
             */

            private int id;
            private int gender;
            private String nickname;
            private String avatar;
            private String birthday;
            private String desc;
            private String remark;
            private String big_avatar;
            private int view_count;
            private long create_time;
            private long update_time;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getGender() {
                return gender;
            }

            public void setGender(int gender) {
                this.gender = gender;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getBirthday() {
                return birthday;
            }

            public void setBirthday(String birthday) {
                this.birthday = birthday;
            }

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public String getBig_avatar() {
                return big_avatar;
            }

            public void setBig_avatar(String big_avatar) {
                this.big_avatar = big_avatar;
            }

            public int getView_count() {
                return view_count;
            }

            public void setView_count(int view_count) {
                this.view_count = view_count;
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
        }
    }
}

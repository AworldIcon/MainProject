package com.coder.kzxt.message.beans;

import java.util.List;

/**
 * Created by zw on 2017/6/26.
 */

public class NotificationListBean {

    /**
     * code : 200
     * message : ok
     * items : [{"id":177,"plat":1,"title":"做最去整容","content":"哦破orz","is_cron":0,"is_app":0,"receive_type":1,"receive_role":1,"course_class":"20858,22716,|","category_ids":"","notify_type":4,"notify_time":0,"notify_user":"907001,906994","custom_time":"","status":1,"user_id":0,"create_time":1498448418,"update_time":1498448419,"delete_time":null,"profile":{"id":null,"gender":2,"nickname":"","avatar":"http://html.gxy.com/www/theme/common/default/images/avatar/girl.png","birthday":null,"desc":null,"remark":null,"big_avatar":"http://html.gxy.com/www/theme/common/default/images/avatar/big_girl.png","view_count":null,"create_time":null,"update_time":null},"platform_text":"全部","status_text":"成功","role_text":"全部","read_num":0,"number":2},{"id":146,"plat":1,"title":"考虑考虑","content":"好咯魔图","is_cron":0,"is_app":0,"receive_type":1,"receive_role":1,"course_class":"20858,22716,22715,|","category_ids":"","notify_type":4,"notify_time":0,"notify_user":"907001,832695,906994","custom_time":"","status":1,"user_id":0,"create_time":1498286405,"update_time":1498286406,"delete_time":null,"profile":{"id":null,"gender":2,"nickname":"","avatar":"http://html.gxy.com/www/theme/common/default/images/avatar/girl.png","birthday":null,"desc":null,"remark":null,"big_avatar":"http://html.gxy.com/www/theme/common/default/images/avatar/big_girl.png","view_count":null,"create_time":null,"update_time":null},"platform_text":"全部","status_text":"成功","role_text":"全部","read_num":1,"number":3},{"id":141,"plat":1,"title":"11555","content":"啦咯啦咯啦咯","is_cron":0,"is_app":0,"receive_type":1,"receive_role":1,"course_class":"20858,22716,|","category_ids":"","notify_type":4,"notify_time":0,"notify_user":"907001,906994","custom_time":"","status":1,"user_id":0,"create_time":1498284644,"update_time":1498284647,"delete_time":null,"profile":{"id":null,"gender":2,"nickname":"","avatar":"http://html.gxy.com/www/theme/common/default/images/avatar/girl.png","birthday":null,"desc":null,"remark":null,"big_avatar":"http://html.gxy.com/www/theme/common/default/images/avatar/big_girl.png","view_count":null,"create_time":null,"update_time":null},"platform_text":"全部","status_text":"成功","role_text":"全部","read_num":0,"number":2},{"id":133,"plat":1,"title":"测试","content":"123","is_cron":0,"is_app":0,"receive_type":1,"receive_role":1,"course_class":"20858,22715,|","category_ids":"","notify_type":4,"notify_time":0,"notify_user":"832695,906994","custom_time":"","status":1,"user_id":0,"create_time":1498283306,"update_time":1498283308,"delete_time":null,"profile":{"id":null,"gender":2,"nickname":"","avatar":"http://html.gxy.com/www/theme/common/default/images/avatar/girl.png","birthday":null,"desc":null,"remark":null,"big_avatar":"http://html.gxy.com/www/theme/common/default/images/avatar/big_girl.png","view_count":null,"create_time":null,"update_time":null},"platform_text":"全部","status_text":"成功","role_text":"全部","read_num":1,"number":2},{"id":132,"plat":1,"title":"测试","content":"123","is_cron":0,"is_app":0,"receive_type":1,"receive_role":1,"course_class":"20858,22715,|","category_ids":"","notify_type":4,"notify_time":0,"notify_user":"832695,906994","custom_time":"","status":1,"user_id":0,"create_time":1498282797,"update_time":1498282799,"delete_time":null,"profile":{"id":null,"gender":2,"nickname":"","avatar":"http://html.gxy.com/www/theme/common/default/images/avatar/girl.png","birthday":null,"desc":null,"remark":null,"big_avatar":"http://html.gxy.com/www/theme/common/default/images/avatar/big_girl.png","view_count":null,"create_time":null,"update_time":null},"platform_text":"全部","status_text":"成功","role_text":"全部","read_num":1,"number":2}]
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
         * id : 177
         * plat : 1
         * title : 做最去整容
         * content : 哦破orz
         * is_cron : 0
         * is_app : 0
         * receive_type : 1
         * receive_role : 1
         * course_class : 20858,22716,|
         * category_ids :
         * notify_type : 4
         * notify_time : 0
         * notify_user : 907001,906994
         * custom_time :
         * status : 1
         * user_id : 0
         * create_time : 1498448418
         * update_time : 1498448419
         * delete_time : null
         * profile : {"id":null,"gender":2,"nickname":"","avatar":"http://html.gxy.com/www/theme/common/default/images/avatar/girl.png","birthday":null,"desc":null,"remark":null,"big_avatar":"http://html.gxy.com/www/theme/common/default/images/avatar/big_girl.png","view_count":null,"create_time":null,"update_time":null}
         * platform_text : 全部
         * status_text : 成功
         * role_text : 全部
         * read_num : 0
         * number : 2
         */

        private int id;
        private int plat;
        private String title;
        private String content;
        private int is_cron;
        private int is_app;
        private int notify_type;
        private int status;
        private int user_id;
        private long create_time;
        private long update_time;
        private ProfileBean profile;
        private int number;
        private int read_num;
        private int is_read;

        public int getIs_read() {
            return is_read;
        }

        public void setIs_read(int is_read) {
            this.is_read = is_read;
        }

        public int getRead_num() {
            return read_num;
        }

        public void setRead_num(int read_num) {
            this.read_num = read_num;
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

        public int getIs_cron() {
            return is_cron;
        }

        public void setIs_cron(int is_cron) {
            this.is_cron = is_cron;
        }

        public int getIs_app() {
            return is_app;
        }

        public void setIs_app(int is_app) {
            this.is_app = is_app;
        }

        public int getNotify_type() {
            return notify_type;
        }

        public void setNotify_type(int notify_type) {
            this.notify_type = notify_type;
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

        public ProfileBean getProfile() {
            return profile;
        }

        public void setProfile(ProfileBean profile) {
            this.profile = profile;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public static class ProfileBean {
            /**
             * id : null
             * gender : 2
             * nickname :
             * avatar : http://html.gxy.com/www/theme/common/default/images/avatar/girl.png
             * birthday : null
             * desc : null
             * remark : null
             * big_avatar : http://html.gxy.com/www/theme/common/default/images/avatar/big_girl.png
             * view_count : null
             * create_time : null
             * update_time : null
             */

            private Object id;
            private int gender;
            private String nickname;
            private String avatar;

            public Object getId() {
                return id;
            }

            public void setId(Object id) {
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
        }
    }
}

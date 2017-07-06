package com.coder.kzxt.message.beans;

import java.util.List;

/**
 * Created by zw on 2017/6/24.
 */

public class NotificationReadNumBean {

    /**
     * code : 8000
     * message : ok
     * item : {"read":[{"id":907256,"gender":2,"nickname":"管理_老师","avatar":"http://html.gxy.com/www/theme/common/default/images/avatar/girl.png","big_avatar":"http://html.gxy.com/www/theme/common/default/images/avatar/big_girl.png","birthday":null,"desc":"","remark":"","view_count":0,"update_time":1498011388}],"un_read":[{"id":907228,"gender":1,"nickname":"学生_13621264144","avatar":"http://html.gxy.com/www/theme/common/default/images/avatar/boy.png","big_avatar":"http://html.gxy.com/www/theme/common/default/images/avatar/big_boy.png","birthday":null,"desc":"","remark":"学生","view_count":0,"update_time":1498024245},{"id":907226,"gender":2,"nickname":"18901090001","avatar":"http://html.gxy.com/www/theme/common/default/images/avatar/girl.png","big_avatar":"http://html.gxy.com/www/theme/common/default/images/avatar/big_girl.png","birthday":null,"desc":null,"remark":"","view_count":0,"update_time":1496821142}]}
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
        private List<ReadBean> read;
        private List<UnReadBean> un_read;

        public List<ReadBean> getRead() {
            return read;
        }

        public void setRead(List<ReadBean> read) {
            this.read = read;
        }

        public List<UnReadBean> getUn_read() {
            return un_read;
        }

        public void setUn_read(List<UnReadBean> un_read) {
            this.un_read = un_read;
        }

        public static class ReadBean {
            /**
             * id : 907256
             * gender : 2
             * nickname : 管理_老师
             * avatar : http://html.gxy.com/www/theme/common/default/images/avatar/girl.png
             * big_avatar : http://html.gxy.com/www/theme/common/default/images/avatar/big_girl.png
             * birthday : null
             * desc :
             * remark :
             * view_count : 0
             * update_time : 1498011388
             */

            private int id;
            private int gender;
            private String nickname;
            private String avatar;
            private String big_avatar;
            private String course_class;

            public String getCourse_class() {
                return course_class;
            }

            public void setCourse_class(String course_class) {
                this.course_class = course_class;
            }

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

            public String getBig_avatar() {
                return big_avatar;
            }

            public void setBig_avatar(String big_avatar) {
                this.big_avatar = big_avatar;
            }
        }

        public static class UnReadBean {
            /**
             * id : 907228
             * gender : 1
             * nickname : 学生_13621264144
             * avatar : http://html.gxy.com/www/theme/common/default/images/avatar/boy.png
             * big_avatar : http://html.gxy.com/www/theme/common/default/images/avatar/big_boy.png
             * birthday : null
             * desc :
             * remark : 学生
             * view_count : 0
             * update_time : 1498024245
             */

            private int id;
            private int gender;
            private String nickname;
            private String avatar;
            private String big_avatar;
            private String course_class;

            public String getCourse_class() {
                return course_class;
            }

            public void setCourse_class(String course_class) {
                this.course_class = course_class;
            }

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

            public String getBig_avatar() {
                return big_avatar;
            }

            public void setBig_avatar(String big_avatar) {
                this.big_avatar = big_avatar;
            }
        }
    }
}

package com.coder.kzxt.classe.beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by wangtingshun on 2017/6/9.
 * 班级成员bean
 */

public class ClassMemberBean {

    private static final long serialVersionUID = -1305961807523688161L;
    private String code;
    private String message;
    private Paginate paginate;
    private ArrayList<ClassMember> items;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<ClassMember> getItems() {
        return items;
    }

    public void setItems(ArrayList<ClassMember> items) {
        this.items = items;
    }

    public Paginate getPaginate() {
        return paginate;
    }

    public void setPaginate(Paginate paginate) {
        this.paginate = paginate;
    }

    public class ClassMember implements Serializable{

        private static final long serialVersionUID = 3417550922266752516L;
        private String id;
        private String group_id;
        private String user_id;
        private String role;
        private Profile profile;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getGroup_id() {
            return group_id;
        }

        public void setGroup_id(String group_id) {
            this.group_id = group_id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public Profile getProfile() {
            return profile;
        }

        public void setProfile(Profile profile) {
            this.profile = profile;
        }

        public class Profile implements Serializable{

            private static final long serialVersionUID = 9156589645550331518L;
            private String id;
            private String gender;
            private String nickname;
            private String avatar;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getGender() {
                return gender;
            }

            public void setGender(String gender) {
                this.gender = gender;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getAvatar() {
                if(avatar==null){
                    return avatar="";
                }
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }
        }
    }

    public class Paginate {
        //当前页数
        private int currentPage;
        //每页记录数
        private int pageSize;
        //总页数
        private int pageNum;
        //总记录数
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

    @Override
    public String toString() {
        return "ClassMemberBean{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", paginate=" + paginate +
                ", items=" + items +
                '}';
    }
}

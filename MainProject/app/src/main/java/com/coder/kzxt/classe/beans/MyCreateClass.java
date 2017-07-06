package com.coder.kzxt.classe.beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by wangtingshun on 2017/6/6.
 * 我创建的班级bean
 */

public class MyCreateClass {

    private String code;
    private String message;
    private Paginate paginate;

    private ArrayList<CreateClassBean> items;

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

    public Paginate getPaginate() {
        return paginate;
    }

    public void setPaginate(Paginate paginate) {
        this.paginate = paginate;
    }

    public ArrayList<CreateClassBean> getItems() {
        return items;
    }

    public void setItems(ArrayList<CreateClassBean> items) {
        this.items = items;
    }

    public static class CreateClassBean implements Serializable{

        private static final long serialVersionUID = 1835133758892004143L;
        private String id;
        private String name;
        private String year;
        private String member_count;
        private String logo;
        private String category_id;
        private String self_role;
        private String join_status;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public String getMember_count() {
            return member_count;
        }

        public void setMember_count(String member_count) {
            this.member_count = member_count;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getCategory_id() {
            return category_id;
        }

        public void setCategory_id(String category_id) {
            this.category_id = category_id;
        }

        public String getSelf_role() {
            return self_role;
        }

        public void setSelf_role(String self_role) {
            this.self_role = self_role;
        }

        public String getJoin_status() {
            return join_status;
        }

        public void setJoin_status(String join_status) {
            this.join_status = join_status;
        }

        @Override
        public String toString() {
            return "CreateClassBean{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", year='" + year + '\'' +
                    ", member_count='" + member_count + '\'' +
                    ", logo='" + logo + '\'' +
                    ", category_id='" + category_id + '\'' +
                    ", self_role='" + self_role + '\'' +
                    ", join_status='" + join_status + '\'' +
                    '}';
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

}

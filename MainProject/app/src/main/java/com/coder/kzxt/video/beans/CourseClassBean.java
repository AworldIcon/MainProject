package com.coder.kzxt.video.beans;

import java.util.List;

/**
 * Created by Administrator on 2017/4/8.
 *
 */

public class CourseClassBean {


    /**
     * code : 200
     * message : ok
     * items : [{"id":"ID","course_id":"课程id","course_class_id":"授课班id","user_id":"学员id","join_type":"加入方式：1、购买加入 2、后台导入","create_time":"创建时间","update_time":"最后更新时间"}]
     * paginate : {"currentPage":"当前页数","pageSize":"每页记录数","pageNum":"总页数","totalNum":"总记录数"}
     */

    private String code;
    private String message;
    private PaginateBean paginate;
    private List<ItemsBean> items;

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
         * currentPage : 当前页数
         * pageSize : 每页记录数
         * pageNum : 总页数
         * totalNum : 总记录数
         */

        private String currentPage;
        private String pageSize;
        private String pageNum;
        private String totalNum;

        public String getCurrentPage() {
            return currentPage;
        }

        public void setCurrentPage(String currentPage) {
            this.currentPage = currentPage;
        }

        public String getPageSize() {
            return pageSize;
        }

        public void setPageSize(String pageSize) {
            this.pageSize = pageSize;
        }

        public String getPageNum() {
            return pageNum;
        }

        public void setPageNum(String pageNum) {
            this.pageNum = pageNum;
        }

        public String getTotalNum() {
            return totalNum;
        }

        public void setTotalNum(String totalNum) {
            this.totalNum = totalNum;
        }
    }

    public static class ItemsBean {
        /**
         * id : ID
         * course_id : 课程id
         * course_class_id : 授课班id
         * user_id : 学员id
         * join_type : 加入方式：1、购买加入 2、后台导入
         * create_time : 创建时间
         * update_time : 最后更新时间
         */

        private String id;
        private String course_id;
        private String course_class_id;
        private String user_id;
        private String join_type;
        private String create_time;
        private String update_time;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCourse_id() {
            return course_id;
        }

        public void setCourse_id(String course_id) {
            this.course_id = course_id;
        }

        public String getCourse_class_id() {
            return course_class_id;
        }

        public void setCourse_class_id(String course_class_id) {
            this.course_class_id = course_class_id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getJoin_type() {
            return join_type;
        }

        public void setJoin_type(String join_type) {
            this.join_type = join_type;
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
    }
}

package com.coder.kzxt.buildwork.entity;

import java.util.List;

/**
 * Created by pc on 2017/3/27.
 */

public class CourseAllClassBean {

    /**
     * code : 200
     * message : ok
     * paginate : {"currentPage":1,"pageSize":10,"pageNum":1,"totalNum":2}
     * items : [{"id":2,"class_name":"测试授课班","user_id":6666,"course_id":4,"start_time":0,"end_time":0,"apply_end_time":0,"desc":"","price":"11.00","status":1,"is_default":0,"sort":0,"hidden":0,"user_count":0,"user_limit":0,"apply_status":1,"create_time":1484717027,"update_time":1484717027,"delete_time":null,"course":{"id":4,"title":"网站编辑","status":2,"source":"","middle_pic":"http://wyzc.com/Public/files/course/2016/06-18/0300579b8df0345209.jpg"}},{"id":1,"class_name":"默认授课班","user_id":6666,"course_id":4,"start_time":0,"end_time":0,"apply_end_time":0,"desc":"","price":"11.00","status":1,"is_default":1,"sort":0,"hidden":0,"user_count":1,"user_limit":0,"apply_status":1,"create_time":1484716888,"update_time":1490434763,"delete_time":null,"course":{"id":4,"title":"网站编辑","status":2,"source":"","middle_pic":"http://wyzc.com/Public/files/course/2016/06-18/0300579b8df0345209.jpg"}}]
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
         * pageSize : 10
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
         * id : 2
         * class_name : 测试授课班
         * user_id : 6666
         * course_id : 4
         * start_time : 0
         * end_time : 0
         * apply_end_time : 0
         * desc :
         * price : 11.00
         * status : 1
         * is_default : 0
         * sort : 0
         * hidden : 0
         * user_count : 0
         * user_limit : 0
         * apply_status : 1
         * create_time : 1484717027
         * update_time : 1484717027
         * delete_time : null
         * course : {"id":4,"title":"网站编辑","status":2,"source":"","middle_pic":"http://wyzc.com/Public/files/course/2016/06-18/0300579b8df0345209.jpg"}
         */

        private int id;
        private String class_name;
        private int user_id;
        private int course_id;
        private Long start_time;
        private Long end_time;
        private Long apply_end_time;
        private String desc;
        private String price;
        private int status;
        private int is_default;
        private int sort;
        private int hidden;
        private int user_count;
        private int user_limit;
        private int apply_status;
        private Long create_time;
        private Long update_time;
        private Object delete_time;
        private CourseBean course;

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

        public Long getStart_time() {
            return start_time;
        }

        public void setStart_time(Long start_time) {
            this.start_time = start_time;
        }

        public Long getEnd_time() {
            return end_time;
        }

        public void setEnd_time(Long end_time) {
            this.end_time = end_time;
        }

        public Long getApply_end_time() {
            return apply_end_time;
        }

        public void setApply_end_time(Long apply_end_time) {
            this.apply_end_time = apply_end_time;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getIs_default() {
            return is_default;
        }

        public void setIs_default(int is_default) {
            this.is_default = is_default;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public int getHidden() {
            return hidden;
        }

        public void setHidden(int hidden) {
            this.hidden = hidden;
        }

        public int getUser_count() {
            return user_count;
        }

        public void setUser_count(int user_count) {
            this.user_count = user_count;
        }

        public int getUser_limit() {
            return user_limit;
        }

        public void setUser_limit(int user_limit) {
            this.user_limit = user_limit;
        }

        public int getApply_status() {
            return apply_status;
        }

        public void setApply_status(int apply_status) {
            this.apply_status = apply_status;
        }

        public Long getCreate_time() {
            return create_time;
        }

        public void setCreate_time(Long create_time) {
            this.create_time = create_time;
        }

        public Long getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(Long update_time) {
            this.update_time = update_time;
        }

        public Object getDelete_time() {
            return delete_time;
        }

        public void setDelete_time(Object delete_time) {
            this.delete_time = delete_time;
        }

        public CourseBean getCourse() {
            return course;
        }

        public void setCourse(CourseBean course) {
            this.course = course;
        }

        public static class CourseBean {
            /**
             * id : 4
             * title : 网站编辑
             * status : 2
             * source :
             * middle_pic : http://wyzc.com/Public/files/course/2016/06-18/0300579b8df0345209.jpg
             */

            private int id;
            private String title;
            private int status;
            private String source;
            private String middle_pic;

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

            public String getSource() {
                return source;
            }

            public void setSource(String source) {
                this.source = source;
            }

            public String getMiddle_pic() {
                return middle_pic;
            }

            public void setMiddle_pic(String middle_pic) {
                this.middle_pic = middle_pic;
            }
        }
    }
}

package com.coder.kzxt.course.beans;

import java.util.ArrayList;

/**
 * 分类课程
 * Created by wangtingshun on 2017/4/7.
 */

public class CategoryCourse {

    private String code;
    private String message;

    private Paginate paginate;

    private ArrayList<CategoryBean> items;


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

    public ArrayList<CategoryBean> getItems() {
        return items;
    }

    public void setItems(ArrayList<CategoryBean> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "CategoryCourse{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", paginate=" + paginate +
                ", items=" + items +
                '}';
    }

    public class CategoryBean {

        private String id;
        private String course_id;
        private String category_id;
        private String create_time;
        private String update_time;
        private String delete_time;
        private CategoryItem category;

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

        public String getCategory_id() {
            return category_id;
        }

        public void setCategory_id(String category_id) {
            this.category_id = category_id;
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

        public CategoryItem getCategory() {
            return category;
        }

        public void setCategory(CategoryItem category) {
            this.category = category;
        }

        @Override
        public String toString() {
            return "CategoryBean{" +
                    "id='" + id + '\'' +
                    ", course_id='" + course_id + '\'' +
                    ", category_id='" + category_id + '\'' +
                    ", create_time='" + create_time + '\'' +
                    ", update_time='" + update_time + '\'' +
                    ", delete_time='" + delete_time + '\'' +
                    ", category=" + category +
                    '}';
        }

        public class CategoryItem {

            private String id;
            private String name;
            private String is_recommend;
            private String level;

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

            public String getIs_recommend() {
                return is_recommend;
            }

            public void setIs_recommend(String is_recommend) {
                this.is_recommend = is_recommend;
            }

            public String getLevel() {
                return level;
            }

            public void setLevel(String level) {
                this.level = level;
            }

            @Override
            public String toString() {
                return "CategoryItem{" +
                        "id='" + id + '\'' +
                        ", name='" + name + '\'' +
                        ", is_recommend='" + is_recommend + '\'' +
                        ", level='" + level + '\'' +
                        '}';
            }
        }

    }


    public class Paginate {
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

}

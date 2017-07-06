package com.coder.kzxt.main.beans;

import java.util.List;

/**
 * Created by pc on 2017/3/28.
 */

public class CourseMoreBean {

    /**
     * code : 200
     * message : ok
     * paginate : {"currentPage":1,"pageSize":20,"pageNum":36,"totalNum":706}
     * items : [{"id":16607,"title":"1231","price":"0.00","middle_pic":"http://html.gxy.com/www/theme/courseedit/default/images/default-img2.jpg","source":"","lesson_num":0,"student_num":0},{"id":16606,"title":"1231","price":"111.00","middle_pic":"http://html.gxy.com/www/theme/courseedit/default/images/default-img7.jpg","source":"","lesson_num":0,"student_num":0},{"id":16605,"title":"1231","price":"123.00","middle_pic":"","source":"","lesson_num":0,"student_num":0},{"id":16604,"title":"111","price":"1.00","middle_pic":"","source":"","lesson_num":0,"student_num":0},{"id":16603,"title":"统计二期3","price":"1.00","middle_pic":"","source":"","lesson_num":0,"student_num":0},{"id":16602,"title":"统计二期3","price":"1.00","middle_pic":"","source":"","lesson_num":0,"student_num":0},{"id":16601,"title":"123","price":"0.00","middle_pic":"http://html.gxy.com/uploads/course_cover/05ed1e857df44710d3bdd3e3cca2782d.jpeg","source":"","lesson_num":0,"student_num":0},{"id":16600,"title":"统计二期3","price":"0.00","middle_pic":"","source":"","lesson_num":0,"student_num":0},{"id":16599,"title":"dfa","price":"0.00","middle_pic":"","source":"","lesson_num":0,"student_num":0},{"id":16598,"title":"dssaddsa11111","price":"1.00","middle_pic":"","source":"","lesson_num":0,"student_num":0},{"id":16597,"title":"dfasdf","price":"12312.00","middle_pic":"","source":"","lesson_num":0,"student_num":0},{"id":16596,"title":"百度","price":"0.00","middle_pic":"","source":"","lesson_num":0,"student_num":0},{"id":16595,"title":"2134123","price":"0.00","middle_pic":"http://html.gxy.com/uploads/course_cover/f84af2394265492ac1357867cecf17d5.png","source":"","lesson_num":0,"student_num":0},{"id":16594,"title":"123123","price":"123.00","middle_pic":"http://html.gxy.com/www/theme/courseedit/default/images/default-img2.jpg","source":"","lesson_num":0,"student_num":0},{"id":16593,"title":"测试课程1","price":"0.01","middle_pic":"http://html.gxy.com/www/theme/courseedit/default/images/default-img7.jpg","source":"","lesson_num":0,"student_num":0},{"id":16592,"title":"tytry","price":"0.01","middle_pic":"http://html.gxy.com/www/theme/courseedit/default/images/default-img7.jpg","source":"","lesson_num":0,"student_num":0},{"id":16591,"title":"yan_test004","price":"0.00","middle_pic":"http://html.gxy.com/uploads/course_cover/98ae2d9eb807648ce319baaa8392ccde.jpeg","source":"","lesson_num":0,"student_num":0},{"id":16590,"title":"yan_test003","price":"0.00","middle_pic":"http://html.gxy.com/uploads/course_cover/6c2b4bc05d3b8f3d0c3255f4b04b3a76.jpeg","source":"","lesson_num":0,"student_num":0},{"id":16589,"title":"yan_002","price":"0.00","middle_pic":"http://html.gxy.com/www/theme/courseedit/default/images/default-img2.jpg","source":"","lesson_num":0,"student_num":0},{"id":16588,"title":"111","price":"1.00","middle_pic":"http://user.gxy.net:8080/uploads/course_cover/75526e41e116a66a9b945222f2d3ce16.jpeg","source":"","lesson_num":0,"student_num":0}]
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
         * pageNum : 36
         * totalNum : 706
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
         * id : 16607
         * title : 1231
         * price : 0.00
         * middle_pic : http://html.gxy.com/www/theme/courseedit/default/images/default-img2.jpg
         * source :
         * lesson_num : 0
         * student_num : 0
         */

        private int id;
        private String title;
        private String price;
        private String middle_pic;
        private String source;
        private int lesson_num;
        private int student_num;
        private String name;
        private String home_pic;
        private String user_face;
        private String brief;

        public String getUser_face() {
            return user_face;
        }

        public void setUser_face(String user_face) {
            this.user_face = user_face;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getHome_pic() {
            return home_pic;
        }

        public void setHome_pic(String home_pic) {
            this.home_pic = home_pic;
        }

        public String getBrief() {
            return brief;
        }

        public void setBrief(String brief) {
            this.brief = brief;
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

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getMiddle_pic() {
            return middle_pic;
        }

        public void setMiddle_pic(String middle_pic) {
            this.middle_pic = middle_pic;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public int getLesson_num() {
            return lesson_num;
        }

        public void setLesson_num(int lesson_num) {
            this.lesson_num = lesson_num;
        }

        public int getStudent_num() {
            return student_num;
        }

        public void setStudent_num(int student_num) {
            this.student_num = student_num;
        }
    }
}

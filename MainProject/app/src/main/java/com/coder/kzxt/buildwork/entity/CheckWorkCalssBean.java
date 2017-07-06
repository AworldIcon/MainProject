package com.coder.kzxt.buildwork.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by pc on 2017/3/25.
 */

public class CheckWorkCalssBean {
    /**
     * code : 200
     * message : ok
     * paginate : {"currentPage":1,"pageSize":8,"pageNum":1,"totalNum":2}
     * items : [{"id":72,"title":"待批阅已过期试卷测试","test_paper_id":218,"start_time":1490348400,"end_time":1490348700,"finished_number":0,"class_id":2,"course_class":{"class_name":"测试授课班","id":2},"total_number":0,"to_check_number":0,"status":3,"status_text":"已过期"},{"id":73,"title":"待批阅已过期试卷测试","test_paper_id":218,"start_time":1490348400,"end_time":1490348700,"finished_number":1,"class_id":1,"course_class":{"class_name":"默认授课班","id":1},"total_number":1,"to_check_number":0,"status":3,"status_text":"已过期"}]
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
         * pageSize : 8
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

    public static class ItemsBean implements Serializable{
        /**
         * id : 72
         * title : 待批阅已过期试卷测试
         * test_paper_id : 218
         * start_time : 1490348400
         * end_time : 1490348700
         * finished_number : 0
         * class_id : 2
         * course_class : {"class_name":"测试授课班","id":2}
         * total_number : 0
         * to_check_number : 0
         * status : 3
         * status_text : 已过期
         */

        private int id;
        private String title;
        private int test_paper_id;
        private Long start_time;
        private Long end_time;
        private int finished_number;
        private int class_id;
        private CourseClassBean course_class;
        private int total_number;
        private int to_check_number;
        private int status;
        private String status_text;
        private TestPaperBean test_paper;


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

        public int getTest_paper_id() {
            return test_paper_id;
        }

        public void setTest_paper_id(int test_paper_id) {
            this.test_paper_id = test_paper_id;
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

        public int getFinished_number() {
            return finished_number;
        }

        public void setFinished_number(int finished_number) {
            this.finished_number = finished_number;
        }

        public int getClass_id() {
            return class_id;
        }

        public void setClass_id(int class_id) {
            this.class_id = class_id;
        }

        public CourseClassBean getCourse_class() {
            return course_class;
        }

        public void setCourse_class(CourseClassBean course_class) {
            this.course_class = course_class;
        }

        public int getTotal_number() {
            return total_number;
        }

        public void setTotal_number(int total_number) {
            this.total_number = total_number;
        }

        public int getTo_check_number() {
            return to_check_number;
        }

        public void setTo_check_number(int to_check_number) {
            this.to_check_number = to_check_number;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getStatus_text() {
            return status_text;
        }

        public void setStatus_text(String status_text) {
            this.status_text = status_text;
        }

        public TestPaperBean getTest_paper()
        {
            return test_paper;
        }

        public void setTest_paper(TestPaperBean test_paper)
        {
            this.test_paper = test_paper;
        }

        public static class CourseClassBean {
            /**
             * class_name : 测试授课班
             * id : 2
             */

            private String class_name;
            private int id;

            public String getClass_name() {
                return class_name;
            }

            public void setClass_name(String class_name) {
                this.class_name = class_name;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }
        }
        public static class TestPaperBean {


            private int mode;
            private int id;
            private String score;
            private String question_count;

            public int getMode()
            {
                return mode;
            }

            public void setMode(int mode)
            {
                this.mode = mode;
            }

            public int getId()
            {
                return id;
            }

            public void setId(int id)
            {
                this.id = id;
            }

            public String getScore()
            {
                return score;
            }

            public void setScore(String score)
            {
                this.score = score;
            }

            public String getQuestion_count()
            {
                if (question_count == null)
                {
                    return "";
                }
                return question_count;
            }

            public void setQuestion_count(String question_count)
            {
                this.question_count = question_count;
            }
        }
    }
}

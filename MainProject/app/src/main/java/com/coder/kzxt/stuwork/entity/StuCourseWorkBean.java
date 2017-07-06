package com.coder.kzxt.stuwork.entity;

import java.util.List;

/**
 * Created by pc on 2017/4/1.
 */

public class StuCourseWorkBean {

    /**
     * code : 200
     * message : ok
     * paginate : {"currentPage":1,"pageSize":8,"pageNum":7,"totalNum":55}
     * items : [{"id":174,"title":"测试1","test_paper_id":489,"start_time":1491541320,"end_time":1491627720,"allow_see_answer":1,"allow_see_score":1,"limit_time":60,"test_paper":{"mode":1,"id":489,"score":37,"question_count":8},"test_result":{"id":206,"status":2,"score":0,"start_time":1491542176,"status_text":"待批阅"},"status":2,"status_text":"进行中"},{"id":172,"title":"新建试卷20173795416","test_paper_id":485,"start_time":1491534693,"end_time":1493521860,"allow_see_answer":1,"allow_see_score":1,"limit_time":11940,"test_paper":{"mode":1,"id":485,"score":100,"question_count":10},"test_result":{"id":205,"status":1,"score":0,"start_time":1491534724,"status_text":"待做"},"status":2,"status_text":"进行中"},{"id":170,"title":"44","test_paper_id":483,"start_time":1491494400,"end_time":1491580800,"allow_see_answer":0,"allow_see_score":0,"limit_time":4620,"test_paper":{"mode":1,"id":483,"score":100,"question_count":3},"test_result":{"id":204,"status":2,"score":33.3,"start_time":1491529644,"status_text":"待批阅"},"status":2,"status_text":"进行中"},{"id":169,"title":"Eee","test_paper_id":480,"start_time":1491528548,"end_time":1491787740,"allow_see_answer":1,"allow_see_score":1,"limit_time":660,"test_paper":{"mode":1,"id":480,"score":15,"question_count":5},"test_result":{"id":203,"status":3,"score":0,"start_time":1491528736,"status_text":"已完成"},"status":2,"status_text":"进行中"},{"id":167,"title":"040601试卷","test_paper_id":472,"start_time":1491479400,"end_time":1491483000,"allow_see_answer":1,"allow_see_score":1,"limit_time":600,"test_paper":{"mode":1,"id":472,"score":100,"question_count":6},"test_result":{"id":0,"status":1,"score":0,"status_text":"待做","start_time":0},"status":3,"status_text":"已过期"},{"id":165,"title":"新建试卷201736134315","test_paper_id":461,"start_time":1491478620,"end_time":1491565020,"allow_see_answer":1,"allow_see_score":1,"limit_time":60,"test_paper":{"mode":1,"id":461,"score":100,"question_count":8},"test_result":{"id":195,"status":3,"score":0.1,"start_time":1491478982,"status_text":"已完成"},"status":2,"status_text":"进行中"},{"id":163,"title":"测试判断","test_paper_id":463,"start_time":1491408000,"end_time":1491494400,"allow_see_answer":0,"allow_see_score":0,"limit_time":1980,"test_paper":{"mode":1,"id":463,"score":100,"question_count":15},"test_result":{"id":187,"status":3,"score":15,"start_time":1491463101,"status_text":"已完成"},"status":3,"status_text":"已过期"},{"id":155,"title":"Test","test_paper_id":454,"start_time":1491446150,"end_time":1491705300,"allow_see_answer":1,"allow_see_score":1,"limit_time":120,"test_paper":{"mode":1,"id":454,"score":45,"question_count":9},"test_result":{"id":183,"status":3,"score":0.6,"start_time":1491459099,"status_text":"已完成"},"status":2,"status_text":"进行中"}]
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
         * pageNum : 7
         * totalNum : 55
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
         * id : 174
         * title : 测试1
         * test_paper_id : 489
         * start_time : 1491541320
         * end_time : 1491627720
         * allow_see_answer : 1
         * allow_see_score : 1
         * limit_time : 60
         * test_paper : {"mode":1,"id":489,"score":37,"question_count":8}
         * test_result : {"id":206,"status":2,"score":0,"start_time":1491542176,"status_text":"待批阅"}
         * status : 2
         * status_text : 进行中
         */

        private int id;
        private String title;
        private int test_paper_id;
        private Long start_time;
        private Long end_time;
        private int allow_see_answer;
        private int allow_see_score;
        private int limit_time;
        private TestPaperBean test_paper;
        private TestResultBean test_result;
        private int status;
        private String status_text;

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

        public int getAllow_see_answer() {
            return allow_see_answer;
        }

        public void setAllow_see_answer(int allow_see_answer) {
            this.allow_see_answer = allow_see_answer;
        }

        public int getAllow_see_score() {
            return allow_see_score;
        }

        public void setAllow_see_score(int allow_see_score) {
            this.allow_see_score = allow_see_score;
        }

        public int getLimit_time() {
            return limit_time;
        }

        public void setLimit_time(int limit_time) {
            this.limit_time = limit_time;
        }

        public TestPaperBean getTest_paper() {
            return test_paper;
        }

        public void setTest_paper(TestPaperBean test_paper) {
            this.test_paper = test_paper;
        }

        public TestResultBean getTest_result() {
            return test_result;
        }

        public void setTest_result(TestResultBean test_result) {
            this.test_result = test_result;
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

        public static class TestPaperBean {
            /**
             * mode : 1
             * id : 489
             * score : 37
             * question_count : 8
             */

            private int mode;
            private int id;
            private String score;
            private int question_count;

            public int getMode() {
                return mode;
            }

            public void setMode(int mode) {
                this.mode = mode;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getScore() {
                return score;
            }

            public void setScore(String score) {
                this.score = score;
            }

            public int getQuestion_count() {
                return question_count;
            }

            public void setQuestion_count(int question_count) {
                this.question_count = question_count;
            }
        }

        public static class TestResultBean {
            /**
             * id : 206
             * status : 2
             * score : 0
             * start_time : 1491542176
             * status_text : 待批阅
             */

            private int id;
            private int status;
            private String score;
            private int start_time;
            private String status_text;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public String getScore() {
                return score;
            }

            public void setScore(String score) {
                this.score = score;
            }

            public int getStart_time() {
                return start_time;
            }

            public void setStart_time(int start_time) {
                this.start_time = start_time;
            }

            public String getStatus_text() {
                return status_text;
            }

            public void setStatus_text(String status_text) {
                this.status_text = status_text;
            }
        }
    }
}

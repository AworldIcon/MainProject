package com.coder.kzxt.buildwork.entity;

import java.util.List;

/**
 * Created by pc on 2017/3/20.
 */

public class AlreadyPublishWork {

    /**
     * code : 200
     * message : ok
     * items : [{"id":"ID","title":"标题","test_paper_id":"试卷id","start_time":"开始时间","end_time":"结束时间","status":"状态:1.未开始 2.进行中 3.已过期","status_text":"状态文本","test_result":{"id":"id","status":"状态:1.待做 2.待批阅 3.已完成","score":"总分","status_text":"状态文本"}}]
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
         * title : 标题
         * test_paper_id : 试卷id
         * start_time : 开始时间
         * end_time : 结束时间
         * status : 状态:1.未开始 2.进行中 3.已过期
         * status_text : 状态文本
         * test_result : {"id":"id","status":"状态:1.待做 2.待批阅 3.已完成","score":"总分","status_text":"状态文本"}
         */

        private String id;
        private String title;
        private String test_paper_id;
        private String start_time;
        private String end_time;
        private String status;
        private String status_text;
        private TestResultBean test_result;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTest_paper_id() {
            return test_paper_id;
        }

        public void setTest_paper_id(String test_paper_id) {
            this.test_paper_id = test_paper_id;
        }

        public String getStart_time() {
            return start_time;
        }

        public void setStart_time(String start_time) {
            this.start_time = start_time;
        }

        public String getEnd_time() {
            return end_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getStatus_text() {
            return status_text;
        }

        public void setStatus_text(String status_text) {
            this.status_text = status_text;
        }

        public TestResultBean getTest_result() {
            return test_result;
        }

        public void setTest_result(TestResultBean test_result) {
            this.test_result = test_result;
        }

        public static class TestResultBean {
            /**
             * id : id
             * status : 状态:1.待做 2.待批阅 3.已完成
             * score : 总分
             * status_text : 状态文本
             */

            private String id;
            private String status;
            private String score;
            private String status_text;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getScore() {
                return score;
            }

            public void setScore(String score) {
                this.score = score;
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

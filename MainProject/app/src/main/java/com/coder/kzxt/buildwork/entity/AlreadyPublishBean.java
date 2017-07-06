package com.coder.kzxt.buildwork.entity;

import java.util.List;

/**
 * Created by pc on 2017/3/24.
 */

public class AlreadyPublishBean {

    /**
     * code : 200
     * message : ok
     * paginate : {"currentPage":1,"pageSize":20,"pageNum":1,"totalNum":15}
     * items : [{"id":219,"title":"待批阅已过期","question_count":1,"score":100,"score_type":2,"mode":1,"status":3,"user_id":916412,"create_time":1490348521,"trash":0,"creator":{"nickname":"Ethan","id":916412},"test":[{"id":77,"test_paper_id":219,"class_id":1,"start_time":1490348700,"end_time":1490349000,"class_name":"默认授课班","status":3,"status_text":"已过期"}]},{"id":218,"title":"待批阅已过期试卷测试","question_count":1,"score":100,"score_type":2,"mode":1,"status":3,"user_id":916412,"create_time":1490348291,"trash":0,"creator":{"nickname":"Ethan","id":916412},"test":[{"id":72,"test_paper_id":218,"class_id":2,"start_time":1490348400,"end_time":1490348700,"class_name":"测试授课班","status":3,"status_text":"已过期"},{"id":73,"test_paper_id":218,"class_id":1,"start_time":1490348400,"end_time":1490348700,"class_name":"默认授课班","status":3,"status_text":"已过期"}]},{"id":217,"title":"测试查看我的考试","question_count":5,"score":100,"score_type":2,"mode":1,"status":3,"user_id":916412,"create_time":1490347949,"trash":0,"creator":{"nickname":"Ethan","id":916412},"test":[{"id":74,"test_paper_id":217,"class_id":2,"start_time":1490284800,"end_time":1490371200,"class_name":"测试授课班","status":2,"status_text":"进行中"},{"id":75,"test_paper_id":217,"class_id":1,"start_time":1490284800,"end_time":1490371200,"class_name":"默认授课班","status":2,"status_text":"进行中"}]},{"id":216,"title":"测试过期","question_count":5,"score":100,"score_type":2,"mode":1,"status":3,"user_id":916412,"create_time":1490345564,"trash":0,"creator":{"nickname":"Ethan","id":916412},"test":[{"id":70,"test_paper_id":216,"class_id":2,"start_time":1490284800,"end_time":1490371200,"class_name":"测试授课班","status":2,"status_text":"进行中"},{"id":71,"test_paper_id":216,"class_id":1,"start_time":1490284800,"end_time":1490346014,"class_name":"默认授课班","status":3,"status_text":"已过期"}]},{"id":214,"title":"有效时间段试卷测试","question_count":1,"score":100,"score_type":2,"mode":1,"status":3,"user_id":916412,"create_time":1490343778,"trash":0,"creator":{"nickname":"Ethan","id":916412},"test":[{"id":66,"test_paper_id":214,"class_id":2,"start_time":1490344200,"end_time":1490351400,"class_name":"测试授课班","status":3,"status_text":"已过期"},{"id":67,"test_paper_id":214,"class_id":1,"start_time":1490344200,"end_time":1490351400,"class_name":"默认授课班","status":3,"status_text":"已过期"}]},{"id":213,"title":"44","question_count":1,"score":100,"score_type":2,"mode":1,"status":3,"user_id":916412,"create_time":1490342841,"trash":0,"creator":{"nickname":"Ethan","id":916412},"test":[{"id":64,"test_paper_id":213,"class_id":2,"start_time":1490284800,"end_time":1490371200,"class_name":"测试授课班","status":2,"status_text":"进行中"},{"id":65,"test_paper_id":213,"class_id":1,"start_time":1490284800,"end_time":1490371200,"class_name":"默认授课班","status":2,"status_text":"进行中"}]},{"id":212,"title":"考试时限试卷","question_count":1,"score":100,"score_type":2,"mode":1,"status":3,"user_id":916412,"create_time":1490342728,"trash":0,"creator":{"nickname":"Ethan","id":916412},"test":[{"id":62,"test_paper_id":212,"class_id":2,"start_time":1490342400,"end_time":1490353200,"class_name":"测试授课班","status":3,"status_text":"已过期"},{"id":63,"test_paper_id":212,"class_id":1,"start_time":1490342400,"end_time":1490353200,"class_name":"默认授课班","status":3,"status_text":"已过期"}]},{"id":211,"title":"考试时限测试","question_count":1,"score":100,"score_type":2,"mode":1,"status":3,"user_id":916412,"create_time":1490342055,"trash":0,"creator":{"nickname":"Ethan","id":916412},"test":[{"id":60,"test_paper_id":211,"class_id":2,"start_time":1490342100,"end_time":1490349300,"class_name":"测试授课班","status":3,"status_text":"已过期"},{"id":61,"test_paper_id":211,"class_id":1,"start_time":1490342100,"end_time":1490355300,"class_name":"默认授课班","status":2,"status_text":"进行中"}]},{"id":207,"title":"试卷032401","question_count":5,"score":25,"score_type":1,"mode":1,"status":3,"user_id":916412,"create_time":1490340166,"trash":0,"creator":{"nickname":"Ethan","id":916412},"test":[{"id":58,"test_paper_id":207,"class_id":2,"start_time":1490341500,"end_time":1490345100,"class_name":"测试授课班","status":3,"status_text":"已过期"},{"id":59,"test_paper_id":207,"class_id":1,"start_time":1490341500,"end_time":1490345100,"class_name":"默认授课班","status":3,"status_text":"已过期"}]},{"id":190,"title":"3","question_count":1,"score":100,"score_type":2,"mode":1,"status":3,"user_id":916412,"create_time":1490323592,"trash":0,"creator":{"nickname":"Ethan","id":916412},"test":[{"id":56,"test_paper_id":190,"class_id":2,"start_time":1490284800,"end_time":1490371200,"class_name":"测试授课班","status":2,"status_text":"进行中"},{"id":57,"test_paper_id":190,"class_id":1,"start_time":1490284800,"end_time":1490371200,"class_name":"默认授课班","status":2,"status_text":"进行中"}]},{"id":189,"title":"测试粘贴","question_count":1,"score":100,"score_type":2,"mode":1,"status":3,"user_id":916412,"create_time":1490320480,"trash":0,"creator":{"nickname":"Ethan","id":916412},"test":[{"id":54,"test_paper_id":189,"class_id":2,"start_time":1490284800,"end_time":1490457600,"class_name":"测试授课班","status":2,"status_text":"进行中"},{"id":55,"test_paper_id":189,"class_id":1,"start_time":1490284800,"end_time":1490457600,"class_name":"默认授课班","status":2,"status_text":"进行中"}]},{"id":188,"title":"测交卷","question_count":1,"score":100,"score_type":2,"mode":1,"status":3,"user_id":916412,"create_time":1490319812,"trash":0,"creator":{"nickname":"Ethan","id":916412},"test":[{"id":52,"test_paper_id":188,"class_id":2,"start_time":1490284800,"end_time":1490371200,"class_name":"测试授课班","status":2,"status_text":"进行中"},{"id":53,"test_paper_id":188,"class_id":1,"start_time":1490284800,"end_time":1490371200,"class_name":"默认授课班","status":2,"status_text":"进行中"}]},{"id":183,"title":"ABCD","question_count":5,"score":100,"score_type":2,"mode":1,"status":3,"user_id":916412,"create_time":1490183175,"trash":0,"creator":{"nickname":"Ethan","id":916412},"test":[{"id":50,"test_paper_id":183,"class_id":2,"start_time":1490198400,"end_time":1490284800,"class_name":"测试授课班","status":3,"status_text":"已过期"},{"id":51,"test_paper_id":183,"class_id":1,"start_time":1490198400,"end_time":1490284800,"class_name":"默认授课班","status":3,"status_text":"已过期"}]},{"id":178,"title":"考试时间测试","question_count":1,"score":100,"score_type":2,"mode":1,"status":3,"user_id":916412,"create_time":1490175039,"trash":0,"creator":{"nickname":"Ethan","id":916412},"test":[{"id":48,"test_paper_id":178,"class_id":2,"start_time":1490254284,"end_time":1490355300,"class_name":"测试授课班","status":2,"status_text":"进行中"},{"id":49,"test_paper_id":178,"class_id":1,"start_time":1490175300,"end_time":1490178900,"class_name":"默认授课班","status":3,"status_text":"已过期"}]},{"id":177,"title":"032201试卷","question_count":5,"score":100,"score_type":2,"mode":1,"status":3,"user_id":916412,"create_time":1490171803,"trash":0,"creator":{"nickname":"Ethan","id":916412},"test":[{"id":46,"test_paper_id":177,"class_id":2,"start_time":1490172600,"end_time":1490179800,"class_name":"测试授课班","status":3,"status_text":"已过期"},{"id":47,"test_paper_id":177,"class_id":1,"start_time":1490172600,"end_time":1490179800,"class_name":"默认授课班","status":3,"status_text":"已过期"}]}]
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
         * pageNum : 1
         * totalNum : 15
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
         * id : 219
         * title : 待批阅已过期
         * question_count : 1
         * score : 100
         * score_type : 2
         * mode : 1
         * status : 3
         * user_id : 916412
         * create_time : 1490348521
         * trash : 0
         * creator : {"nickname":"Ethan","id":916412}
         * test : [{"id":77,"test_paper_id":219,"class_id":1,"start_time":1490348700,"end_time":1490349000,"class_name":"默认授课班","status":3,"status_text":"已过期"}]
         */

        private int id;
        private String title;
        private int question_count;
        private String score;
        private int score_type;
        private int mode;
        private int status;
        private int user_id;
        private Long create_time;
        private int trash;
        private CreatorBean creator;
        private List<TestBean> test;

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

        public int getQuestion_count() {
            return question_count;
        }

        public void setQuestion_count(int question_count) {
            this.question_count = question_count;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public int getScore_type() {
            return score_type;
        }

        public void setScore_type(int score_type) {
            this.score_type = score_type;
        }

        public int getMode() {
            return mode;
        }

        public void setMode(int mode) {
            this.mode = mode;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public Long getCreate_time() {
            return create_time;
        }

        public void setCreate_time(Long create_time) {
            this.create_time = create_time;
        }

        public int getTrash() {
            return trash;
        }

        public void setTrash(int trash) {
            this.trash = trash;
        }

        public CreatorBean getCreator() {
            return creator;
        }

        public void setCreator(CreatorBean creator) {
            this.creator = creator;
        }

        public List<TestBean> getTest() {
            return test;
        }

        public void setTest(List<TestBean> test) {
            this.test = test;
        }

        public static class CreatorBean {
            /**
             * nickname : Ethan
             * id : 916412
             */

            private String nickname;
            private int id;

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }
        }

        public static class TestBean {
            /**
             * id : 77
             * test_paper_id : 219
             * class_id : 1
             * start_time : 1490348700
             * end_time : 1490349000
             * class_name : 默认授课班
             * status : 3
             * status_text : 已过期
             */

            private int id;
            private int test_paper_id;
            private int class_id;
            private Long start_time;
            private Long end_time;
            private String class_name;
            private int status;
            private String status_text;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getTest_paper_id() {
                return test_paper_id;
            }

            public void setTest_paper_id(int test_paper_id) {
                this.test_paper_id = test_paper_id;
            }

            public int getClass_id() {
                return class_id;
            }

            public void setClass_id(int class_id) {
                this.class_id = class_id;
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

            public String getClass_name() {
                return class_name;
            }

            public void setClass_name(String class_name) {
                this.class_name = class_name;
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
        }
    }
}

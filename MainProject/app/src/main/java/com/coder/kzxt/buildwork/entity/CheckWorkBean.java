package com.coder.kzxt.buildwork.entity;

import java.util.List;

/**
 * Created by pc on 2017/3/22.
 */

public class CheckWorkBean {

    /**
     * code : 200
     * message : ok
     * item : {"id":41,"user_id":916412,"status":3,"start_time":1489714334,"limit_time":0,"score":13.6,"test_id":17,"test":{"id":17,"test_paper_id":30,"start_time":1489420800,"end_time":1490889600,"status":2,"status_text":"进行中"},"detail":[{"id":16,"test_result_id":41,"question_id":21,"score":6.6,"status":1,"answer":["1"],"teacher_say":null},{"id":17,"test_result_id":41,"question_id":26,"score":0,"status":2,"answer":["2"],"teacher_say":null},{"id":18,"test_result_id":41,"question_id":31,"score":0,"status":2,"answer":["0"],"teacher_say":"\u201c\u201d"},{"id":19,"test_result_id":41,"question_id":17,"score":0,"status":2,"answer":["B","C"],"teacher_say":"\u201c\u201d"},{"id":20,"test_result_id":41,"question_id":18,"score":0,"status":2,"answer":["             填空2","             填空2","             填空2"],"teacher_say":null},{"id":21,"test_result_id":41,"question_id":19,"score":0,"status":2,"answer":["1"],"teacher_say":null},{"id":22,"test_result_id":41,"question_id":20,"score":3,"status":1,"answer":["   978"],"teacher_say":"2"},{"id":23,"test_result_id":41,"question_id":22,"score":0,"status":2,"answer":["1","3"],"teacher_say":null},{"id":24,"test_result_id":41,"question_id":23,"score":0,"status":2,"answer":["             填空1","             填空1","             填空1"],"teacher_say":null},{"id":25,"test_result_id":41,"question_id":24,"score":0,"status":2,"answer":["0"],"teacher_say":null},{"id":26,"test_result_id":41,"question_id":25,"score":2,"status":1,"answer":["   876"],"teacher_say":"2"},{"id":27,"test_result_id":41,"question_id":27,"score":0,"status":2,"answer":["3","4"],"teacher_say":null},{"id":28,"test_result_id":41,"question_id":28,"score":0,"status":2,"answer":["             填空3","             填空3","             填空3"],"teacher_say":null},{"id":29,"test_result_id":41,"question_id":29,"score":0,"status":2,"answer":["1"],"teacher_say":null},{"id":30,"test_result_id":41,"question_id":30,"score":2,"status":1,"answer":["   654"],"teacher_say":"2"}],"status_text":"已完成"}
     */

    private int code;
    private String message;
    private ItemBean item;

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

    public ItemBean getItem() {
        return item;
    }

    public void setItem(ItemBean item) {
        this.item = item;
    }

    public static class ItemBean {
        /**
         * id : 41
         * user_id : 916412
         * status : 3
         * start_time : 1489714334
         * limit_time : 0
         * score : 13.6
         * test_id : 17
         * test : {"id":17,"test_paper_id":30,"start_time":1489420800,"end_time":1490889600,"status":2,"status_text":"进行中"}
         * detail : [{"id":16,"test_result_id":41,"question_id":21,"score":6.6,"status":1,"answer":["1"],"teacher_say":null},{"id":17,"test_result_id":41,"question_id":26,"score":0,"status":2,"answer":["2"],"teacher_say":null},{"id":18,"test_result_id":41,"question_id":31,"score":0,"status":2,"answer":["0"],"teacher_say":"\u201c\u201d"},{"id":19,"test_result_id":41,"question_id":17,"score":0,"status":2,"answer":["B","C"],"teacher_say":"\u201c\u201d"},{"id":20,"test_result_id":41,"question_id":18,"score":0,"status":2,"answer":["             填空2","             填空2","             填空2"],"teacher_say":null},{"id":21,"test_result_id":41,"question_id":19,"score":0,"status":2,"answer":["1"],"teacher_say":null},{"id":22,"test_result_id":41,"question_id":20,"score":3,"status":1,"answer":["   978"],"teacher_say":"2"},{"id":23,"test_result_id":41,"question_id":22,"score":0,"status":2,"answer":["1","3"],"teacher_say":null},{"id":24,"test_result_id":41,"question_id":23,"score":0,"status":2,"answer":["             填空1","             填空1","             填空1"],"teacher_say":null},{"id":25,"test_result_id":41,"question_id":24,"score":0,"status":2,"answer":["0"],"teacher_say":null},{"id":26,"test_result_id":41,"question_id":25,"score":2,"status":1,"answer":["   876"],"teacher_say":"2"},{"id":27,"test_result_id":41,"question_id":27,"score":0,"status":2,"answer":["3","4"],"teacher_say":null},{"id":28,"test_result_id":41,"question_id":28,"score":0,"status":2,"answer":["             填空3","             填空3","             填空3"],"teacher_say":null},{"id":29,"test_result_id":41,"question_id":29,"score":0,"status":2,"answer":["1"],"teacher_say":null},{"id":30,"test_result_id":41,"question_id":30,"score":2,"status":1,"answer":["   654"],"teacher_say":"2"}]
         * status_text : 已完成
         */

        private int id;
        private int user_id;
        private int status;
        private int start_time;
        private int limit_time;
        private double score;
        private int test_id;
        private TestBean test;
        private String status_text;
        private List<DetailBean> detail;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getStart_time() {
            return start_time;
        }

        public void setStart_time(int start_time) {
            this.start_time = start_time;
        }

        public int getLimit_time() {
            return limit_time;
        }

        public void setLimit_time(int limit_time) {
            this.limit_time = limit_time;
        }

        public double getScore() {
            return score;
        }

        public void setScore(double score) {
            this.score = score;
        }

        public int getTest_id() {
            return test_id;
        }

        public void setTest_id(int test_id) {
            this.test_id = test_id;
        }

        public TestBean getTest() {
            return test;
        }

        public void setTest(TestBean test) {
            this.test = test;
        }

        public String getStatus_text() {
            return status_text;
        }

        public void setStatus_text(String status_text) {
            this.status_text = status_text;
        }

        public List<DetailBean> getDetail() {
            return detail;
        }

        public void setDetail(List<DetailBean> detail) {
            this.detail = detail;
        }

        public static class TestBean {
            /**
             * id : 17
             * test_paper_id : 30
             * start_time : 1489420800
             * end_time : 1490889600
             * status : 2
             * status_text : 进行中
             */

            private int id;
            private int test_paper_id;
            private int start_time;
            private int end_time;
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

            public int getStart_time() {
                return start_time;
            }

            public void setStart_time(int start_time) {
                this.start_time = start_time;
            }

            public int getEnd_time() {
                return end_time;
            }

            public void setEnd_time(int end_time) {
                this.end_time = end_time;
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

        public static class DetailBean {
            /**
             * id : 16
             * test_result_id : 41
             * question_id : 21
             * score : 6.6
             * status : 1
             * answer : ["1"]
             * teacher_say : null
             */

            private String id;
            private int test_result_id;
            private String question_id;
            private float score;
            private int status;
            private Object teacher_say;
            private List<String> answer;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public int getTest_result_id() {
                return test_result_id;
            }

            public void setTest_result_id(int test_result_id) {
                this.test_result_id = test_result_id;
            }

            public String getQuestion_id() {
                return question_id;
            }

            public void setQuestion_id(String question_id) {
                this.question_id = question_id;
            }

            public float getScore() {
                return score;
            }

            public void setScore(float score) {
                this.score = score;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public Object getTeacher_say() {
                return teacher_say;
            }

            public void setTeacher_say(Object teacher_say) {
                this.teacher_say = teacher_say;
            }

            public List<String> getAnswer() {
                return answer;
            }

            public void setAnswer(List<String> answer) {
                this.answer = answer;
            }
        }
    }
}

package com.coder.kzxt.service.beans;

import com.coder.kzxt.base.beans.BaseBean;

import java.util.List;

/**
 * Created by MaShiZhao on 2017/6/16
 */

public class ServiceMyTestResult extends BaseBean
{


    private List<ItemsBean> items;

    public List<ItemsBean> getItems()
    {
        return items;
    }

    public void setItems(List<ItemsBean> items)
    {
        this.items = items;
    }


    public static class ItemsBean
    {
        /**
         * id : ID
         * title : 标题
         * test_paper_id : 试卷id
         * start_time : 开始时间
         * end_time : 结束时间
         * status : 状态:1.未开始 2.进行中 3.已过期
         * status_text : 状态文本
         * test_paper : {"mode":"组卷模式","id":"试卷id","score":"试卷总分","question_count":"试卷题量"}
         * test_result : {"id":"id","status":"状态:1.待做 2.待批阅 3.已完成","score":"总分","start_time":"开始做题时间","status_text":"状态文本","result":"审核结果/状态 0未完成 1等待 2通过 3未通过","history":"是否历史记录 1为历史记录"}
         * prev_result : {"id":"id 这里的数据作为查看历史使用","status":"状态:1.待做 2.待批阅 3.已完成","score":"总分","start_time":"开始做题时间","status_text":"状态文本","result":"审核结果/状态 0未完成 1等待 2通过 3未通过","history":"是否历史记录 1为历史记录"}
         */

        private String id;
        private String title;
        private String test_paper_id;
        private String start_time;
        private long end_time;
        private int limit_time;
        private int status;
        private String status_text;
        private TestResultBean test_result;

        public int getLimit_time()
        {
            return limit_time;
        }

        public void setLimit_time(int limit_time)
        {
            this.limit_time = limit_time;
        }

        public String getId()
        {
            return id;
        }

        public void setId(String id)
        {
            this.id = id;
        }

        public String getTitle()
        {
            return title;
        }

        public void setTitle(String title)
        {
            this.title = title;
        }

        public String getTest_paper_id()
        {
            return test_paper_id;
        }

        public void setTest_paper_id(String test_paper_id)
        {
            this.test_paper_id = test_paper_id;
        }

        public String getStart_time()
        {
            return start_time;
        }

        public void setStart_time(String start_time)
        {
            this.start_time = start_time;
        }

        public long getEnd_time()
        {
            return end_time;
        }

        public void setEnd_time(long end_time)
        {
            this.end_time = end_time;
        }

        public int getStatus()
        {
            return status;
        }

        public void setStatus(int status)
        {
            this.status = status;
        }

        public String getStatus_text()
        {
            return status_text;
        }

        public void setStatus_text(String status_text)
        {
            this.status_text = status_text;
        }


        public TestResultBean getTest_result()
        {
            return test_result;
        }

        public void setTest_result(TestResultBean test_result)
        {
            this.test_result = test_result;
        }


        public static class TestResultBean
        {
            /**
             * id : id
             * status : 状态:1.待做 2.待批阅 3.已完成
             * score : 总分
             * start_time : 开始做题时间
             * status_text : 状态文本
             * result : 审核结果/状态 0未完成 1等待 2通过 3未通过
             * history : 是否历史记录 1为历史记录
             */

            private String id;
            private int status;
            private String score;
            private String start_time;
            private String status_text;
            private int result;

            public String getId()
            {
                return id;
            }

            public void setId(String id)
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

            public String getStart_time()
            {
                return start_time;
            }

            public void setStart_time(String start_time)
            {
                this.start_time = start_time;
            }

            public String getStatus_text()
            {
                return status_text;
            }

            public void setStatus_text(String status_text)
            {
                this.status_text = status_text;
            }

            public int getStatus()
            {
                return status;
            }

            public void setStatus(int status)
            {
                this.status = status;
            }

            public int getResult()
            {
                return result;
            }

            public void setResult(int result)
            {
                this.result = result;
            }
        }

    }
}

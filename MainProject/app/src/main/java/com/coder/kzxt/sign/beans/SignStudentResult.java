package com.coder.kzxt.sign.beans;

import com.coder.kzxt.base.beans.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by MaShiZhao on 2017/5/31
 */

public class SignStudentResult extends BaseBean
{


    /**
     * code : 200
     * items : [{"info":{"id":39,"sign_id":132,"student_id":907004,"user_id":907004,"status":1,"tag_id":"1,2,","create_time":1495877244,
     * "update_time":1495877244,"delete_time":null,"nickname":"咚咚四","tags":[{"id":1,"name":"迟到"},{"id":2,"name":"早退"}]},"record":[],
     * "sign":{"id":132,"course_id":20875,"class_id":22735,"time":300,"range":100,"begin_time":1495876974,"end_time":1495877274,
     * "user_id":907004,"latitude":"39.963492","longitude":"116.32354","address":"北京市海淀区篱笆房路靠近中国工商银行(紫竹院支行)",
     * "create_time":1495876974,"update_time":1495877274,"delete_time":null}}          ]
     */

    private List<ItemsBean> items;


    public List<ItemsBean> getItems()
    {
        return items;
    }

    public void setItems(List<ItemsBean> items)
    {
        this.items = items;
    }

    public static class ItemsBean implements Serializable
    {
        /**
         * info 是学生自己签的到 record是老师修改的信息 sign 是签到的详情
         * <p>
         * info : {"id":39,"sign_id":132,"student_id":907004,"user_id":907004,"status":1,"tag_id":"1,2,","create_time":1495877244,"update_time":1495877244,"delete_time":null,"nickname":"咚咚四","tags":[{"id":1,"name":"迟到"},{"id":2,"name":"早退"}]}
         * record : []
         * sign : {"id":132,"course_id":20875,"class_id":22735,"time":300,"range":100,"begin_time":1495876974,"end_time":1495877274,"user_id":907004,"latitude":"39.963492","longitude":"116.32354","address":"北京市海淀区篱笆房路靠近中国工商银行(紫竹院支行)","create_time":1495876974,"update_time":1495877274,"delete_time":null}
         */

        private SignBean info;
        private SignBean sign;
        private RecordBean record;

        public Boolean getIsRecord()
        {
            return record.getNickname() != null && record.getStatus() != info.getStatus();
        }

        public int getStatus()
        {
            return getIsRecord() ? record.getStatus() : info.getStatus();
        }


        public int getIs_rang()
        {
            return getIsRecord() ? 1 : info.getIs_range();
        }


        public SignBean getInfo()
        {
            return info;
        }

        public void setInfo(SignBean info)
        {
            this.info = info;
        }

        public SignBean getSign()
        {
            return sign;
        }

        public void setSign(SignBean sign)
        {
            this.sign = sign;
        }

        public RecordBean getRecord()
        {
            return record;
        }

        public void setRecord(RecordBean record)
        {
            this.record = record;
        }

        public static class RecordBean extends SignBean implements Serializable
        {
            private String nickname;

            public String getNickname()
            {
                return nickname;
            }

            public void setNickname(String nickname)
            {
                this.nickname = nickname;
            }
        }

    }
}

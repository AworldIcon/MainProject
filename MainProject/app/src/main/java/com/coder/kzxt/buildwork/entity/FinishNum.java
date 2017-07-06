package com.coder.kzxt.buildwork.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by pc on 2016/10/18.
 */
public class FinishNum implements Serializable{


    /**
     * data : {"doing":[{"id":"52","nickname":"生_螣","smallAvatar":"http://192.168.3.6:88/Public/assets/img/default/middlestudentAvatarMale.png"},{"id":"53","nickname":"师_螣","smallAvatar":"http://192.168.3.6:88/Public/assets/img/default/middleteacherAvatarMale.png"},{"id":"74","nickname":"生_锡","smallAvatar":"http://192.168.3.6:88/Public/assets/img/default/middlestudentAvatarMale.png"},{"id":"2146","nickname":"生_垦","smallAvatar":"http://192.168.3.6:88/Public/assets/img/default/middlestudentAvatarMale.png"},{"id":"2399","nickname":"lwl_002","smallAvatar":"http://192.168.3.6:88/Public/assets/img/default/middlestudentAvatarMale.png"},{"id":"6504","nickname":"马世钊老师","smallAvatar":"http://192.168.3.6:88/Public/files/user/2017/02-16/153010244c84003540.jpg?5.1.4"},{"id":"7552","nickname":"student","smallAvatar":"http://192.168.3.6:88/Public/files/user/2016/11-15/112621d745ba221740.png?5.1.4"},{"id":"1081917","nickname":"我是中国人人啊","smallAvatar":"http://192.168.3.6:88/Public/files/user/2016/10-18/1845382b177e602965.jpg?5.1.4"},{"id":"1099145","nickname":"内网测试_老师","smallAvatar":"http://192.168.3.6:88/Public/files/user/2016/11-30/1703026c72a2367566.jpg?5.1.4"},{"id":"1099210","nickname":"Messiah","smallAvatar":"http://192.168.3.6:88/Public/assets/img/default/middleteacherAvatarMale.png"},{"id":"1100016","nickname":"王健林","smallAvatar":"http://192.168.3.6:88/Public/assets/img/default/middlestudentAvatarMale.png"},{"id":"1100233","nickname":"签到五","smallAvatar":"http://192.168.3.6:88/Public/files/user/2016/11-09/174026ad2e85418173.jpg?5.1.4"},{"id":"1100254","nickname":"waiwai","smallAvatar":"http://192.168.3.6:88/Public/assets/img/default/middleteacherAvatarMale.png"},{"id":"1100255","nickname":"waiwai3","smallAvatar":"http://192.168.3.6:88/Public/assets/img/default/middlestudentAvatarMale.png"}],"finshed":[{"id":"2343","nickname":"马云","smallAvatar":"http://192.168.3.6:88/Public/assets/img/default/middleteacherAvatarMale.png","finishTime":"1484211758","status":"finished","score":"1.0","userId":"76"}],"test":{"name":"作业考试1作业20170112","finishedNum":1,"unfinishedNum":14,"itemCount":"3"}}
     * code : 1000
     * msg : 成功
     * xhprof : no setup
     * runTm : s:1487303371.362 e:1487303371.887 tms=525ms
     * mem : 22.02 MB
     * server : wyzc
     * serverTime : 1487303371
     */

    private DataBean data;
    private int code;
    private String msg;
    private String xhprof;
    private String runTm;
    private String mem;
    private String server;
    private String serverTime;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getXhprof() {
        return xhprof;
    }

    public void setXhprof(String xhprof) {
        this.xhprof = xhprof;
    }

    public String getRunTm() {
        return runTm;
    }

    public void setRunTm(String runTm) {
        this.runTm = runTm;
    }

    public String getMem() {
        return mem;
    }

    public void setMem(String mem) {
        this.mem = mem;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getServerTime() {
        return serverTime;
    }

    public void setServerTime(String serverTime) {
        this.serverTime = serverTime;
    }

    public static class DataBean implements Serializable{
        /**
         * doing : [{"id":"52","nickname":"生_螣","smallAvatar":"http://192.168.3.6:88/Public/assets/img/default/middlestudentAvatarMale.png"},{"id":"53","nickname":"师_螣","smallAvatar":"http://192.168.3.6:88/Public/assets/img/default/middleteacherAvatarMale.png"},{"id":"74","nickname":"生_锡","smallAvatar":"http://192.168.3.6:88/Public/assets/img/default/middlestudentAvatarMale.png"},{"id":"2146","nickname":"生_垦","smallAvatar":"http://192.168.3.6:88/Public/assets/img/default/middlestudentAvatarMale.png"},{"id":"2399","nickname":"lwl_002","smallAvatar":"http://192.168.3.6:88/Public/assets/img/default/middlestudentAvatarMale.png"},{"id":"6504","nickname":"马世钊老师","smallAvatar":"http://192.168.3.6:88/Public/files/user/2017/02-16/153010244c84003540.jpg?5.1.4"},{"id":"7552","nickname":"student","smallAvatar":"http://192.168.3.6:88/Public/files/user/2016/11-15/112621d745ba221740.png?5.1.4"},{"id":"1081917","nickname":"我是中国人人啊","smallAvatar":"http://192.168.3.6:88/Public/files/user/2016/10-18/1845382b177e602965.jpg?5.1.4"},{"id":"1099145","nickname":"内网测试_老师","smallAvatar":"http://192.168.3.6:88/Public/files/user/2016/11-30/1703026c72a2367566.jpg?5.1.4"},{"id":"1099210","nickname":"Messiah","smallAvatar":"http://192.168.3.6:88/Public/assets/img/default/middleteacherAvatarMale.png"},{"id":"1100016","nickname":"王健林","smallAvatar":"http://192.168.3.6:88/Public/assets/img/default/middlestudentAvatarMale.png"},{"id":"1100233","nickname":"签到五","smallAvatar":"http://192.168.3.6:88/Public/files/user/2016/11-09/174026ad2e85418173.jpg?5.1.4"},{"id":"1100254","nickname":"waiwai","smallAvatar":"http://192.168.3.6:88/Public/assets/img/default/middleteacherAvatarMale.png"},{"id":"1100255","nickname":"waiwai3","smallAvatar":"http://192.168.3.6:88/Public/assets/img/default/middlestudentAvatarMale.png"}]
         * finshed : [{"id":"2343","nickname":"马云","smallAvatar":"http://192.168.3.6:88/Public/assets/img/default/middleteacherAvatarMale.png","finishTime":"1484211758","status":"finished","score":"1.0","userId":"76"}]
         * test : {"name":"作业考试1作业20170112","finishedNum":1,"unfinishedNum":14,"itemCount":"3"}
         */

        private TestBean test;
        private List<DoingBean> doing;
        private List<FinshedBean> finshed;

        public TestBean getTest() {
            return test;
        }

        public void setTest(TestBean test) {
            this.test = test;
        }

        public List<DoingBean> getDoing() {
            return doing;
        }

        public void setDoing(List<DoingBean> doing) {
            this.doing = doing;
        }

        public List<FinshedBean> getFinshed() {
            return finshed;
        }

        public void setFinshed(List<FinshedBean> finshed) {
            this.finshed = finshed;
        }

        public static class TestBean implements Serializable{
            /**
             * name : 作业考试1作业20170112
             * finishedNum : 1
             * unfinishedNum : 14
             * itemCount : 3
             */

            private String name;
            private int finishedNum;
            private int unfinishedNum;
            private String itemCount;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getFinishedNum() {
                return finishedNum;
            }

            public void setFinishedNum(int finishedNum) {
                this.finishedNum = finishedNum;
            }

            public int getUnfinishedNum() {
                return unfinishedNum;
            }

            public void setUnfinishedNum(int unfinishedNum) {
                this.unfinishedNum = unfinishedNum;
            }

            public String getItemCount() {
                return itemCount;
            }

            public void setItemCount(String itemCount) {
                this.itemCount = itemCount;
            }
        }

        public static class DoingBean implements Serializable{
            /**
             * id : 52
             * nickname : 生_螣
             * smallAvatar : http://192.168.3.6:88/Public/assets/img/default/middlestudentAvatarMale.png
             */

            private String id;
            private String nickname;
            private String smallAvatar;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getSmallAvatar() {
                return smallAvatar;
            }

            public void setSmallAvatar(String smallAvatar) {
                this.smallAvatar = smallAvatar;
            }
        }

        public static class FinshedBean implements Serializable{
            /**
             * id : 2343
             * nickname : 马云
             * smallAvatar : http://192.168.3.6:88/Public/assets/img/default/middleteacherAvatarMale.png
             * finishTime : 1484211758
             * status : finished
             * score : 1.0
             * userId : 76
             */

            private String id;
            private String nickname;
            private String smallAvatar;
            private String finishTime;
            private String status;
            private String score;
            private String userId;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getSmallAvatar() {
                return smallAvatar;
            }

            public void setSmallAvatar(String smallAvatar) {
                this.smallAvatar = smallAvatar;
            }

            public String getFinishTime() {
                return finishTime;
            }

            public void setFinishTime(String finishTime) {
                this.finishTime = finishTime;
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

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }
        }
    }
}

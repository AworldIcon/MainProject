package com.coder.kzxt.question.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by MaShiZhao on 2017/3/13
 */

public class QuestionBean
{

    private DataBean data;
    /**
     * data : {"questionList":[{"audioDuration":0,"audioId":"0","audioUrl":"","bestAnswerId":"0","content":"小卡弟弟简简单单拿得起","courseId":"4489","courseName":"H5收费课程","courseTitle":"","ctime":"1484559168","gender":"male","goodNum":"0","hitNum":"0","imgs":[],"isDelete":"0","isFine":"0","isGood":"0","isGrab":"0","lockedUid":"0","postNum":"1","questionId":"37491","questionStatus":"0","role":"student","score":"0","title":"小卡弟弟简简单单拿得起","uid":"1100233","userface":"http://192.168.3.6:88/Public/files/user/2016/11-09/174026ad2e85418173.jpg?5.1.4","username":"签到五"},{"audioDuration":0,"audioId":"0","audioUrl":"","bestAnswerId":"127344","content":"东东要东阿你","courseId":"4438","courseName":"LC作业考试","courseTitle":"","ctime":"1484214367","gender":"male","goodNum":"0","hitNum":"0","imgs":[],"isDelete":"0","isFine":"0","isGood":"0","isGrab":"0","lockedUid":"0","postNum":"2","questionId":"37487","questionStatus":"1","role":"student","score":"0","title":"东东要东阿你","uid":"1100233","userface":"http://192.168.3.6:88/Public/files/user/2016/11-09/174026ad2e85418173.jpg?5.1.4","username":"签到五"},{"audioDuration":0,"audioId":"0","audioUrl":"","bestAnswerId":"127334","content":"1233456778990","courseId":"4438","courseName":"LC作业考试","courseTitle":"","ctime":"1484129619","gender":"male","goodNum":"0","hitNum":"0","imgs":[],"isDelete":"0","isFine":"0","isGood":"0","isGrab":"0","lockedUid":"0","postNum":"1","questionId":"37482","questionStatus":"1","role":"student","score":"0","title":"1233456778990","uid":"1100233","userface":"http://192.168.3.6:88/Public/files/user/2016/11-09/174026ad2e85418173.jpg?5.1.4","username":"签到五"},{"audioDuration":0,"audioId":"0","audioUrl":"","bestAnswerId":"0","content":"晃荡？！？","courseId":"4438","courseName":"LC作业考试","courseTitle":"","ctime":"1484120825","gender":"male","goodNum":"0","hitNum":"0","imgs":[],"isDelete":"0","isFine":"0","isGood":"0","isGrab":"0","lockedUid":"0","postNum":"2","questionId":"37475","questionStatus":"0","role":"student","score":"0","title":"晃荡？！？","uid":"1100233","userface":"http://192.168.3.6:88/Public/files/user/2016/11-09/174026ad2e85418173.jpg?5.1.4","username":"签到五"},{"audioDuration":0,"audioId":"0","audioUrl":"","bestAnswerId":"0","content":"的官方修复","courseId":"4438","courseName":"LC作业考试","courseTitle":"","ctime":"1484100205","gender":"male","goodNum":"0","hitNum":"0","imgs":[],"isDelete":"0","isFine":"0","isGood":"0","isGrab":"0","lockedUid":"0","postNum":"1","questionId":"37472","questionStatus":"0","role":"student","score":"0","title":"的官方修复","uid":"1100233","userface":"http://192.168.3.6:88/Public/files/user/2016/11-09/174026ad2e85418173.jpg?5.1.4","username":"签到五"},{"audioDuration":0,"audioId":"0","audioUrl":"","bestAnswerId":"0","content":"出111111111","courseId":"4379","courseName":"测试直播1234","courseTitle":"","ctime":"1479369885","gender":"male","goodNum":"0","hitNum":"0","imgs":[],"isDelete":"0","isFine":"0","isGood":"0","isGrab":"0","lockedUid":"0","postNum":"1","questionId":"37299","questionStatus":"0","role":"student","score":"0","title":"出111111111","uid":"1100233","userface":"http://192.168.3.6:88/Public/files/user/2016/11-09/174026ad2e85418173.jpg?5.1.4","username":"签到五"}],"typeList":[{"type":"studentNewReply","name":"新答复"},{"type":"studentMyQuestion","name":"提问的"},{"type":"studentMyAnswer","name":"回答的"}]}
     * page : 1
     * totalPages : 1
     * code : 1000
     * msg : 成功
     * xhprof : no setup
     * runTm : s:1489392301.190 e:1489392301.566 tms=376ms
     * mem : 22.86 MB
     * server : wt1.nazhengshu.com
     * serverTime : 1489392301
     */

    private int page;
    private int totalPages;
    private int code;
    private String msg;
    private String xhprof;
    private String runTm;
    private String mem;
    private String server;
    private String serverTime;

    public DataBean getData()
    {
        return data;
    }

    public void setData(DataBean data)
    {
        this.data = data;
    }

    public int getPage()
    {
        return page;
    }

    public void setPage(int page)
    {
        this.page = page;
    }

    public int getTotalPages()
    {
        return totalPages;
    }

    public void setTotalPages(int totalPages)
    {
        this.totalPages = totalPages;
    }

    public int getCode()
    {
        return code;
    }

    public void setCode(int code)
    {
        this.code = code;
    }

    public String getMsg()
    {
        return msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }

    public String getXhprof()
    {
        return xhprof;
    }

    public void setXhprof(String xhprof)
    {
        this.xhprof = xhprof;
    }

    public String getRunTm()
    {
        return runTm;
    }

    public void setRunTm(String runTm)
    {
        this.runTm = runTm;
    }

    public String getMem()
    {
        return mem;
    }

    public void setMem(String mem)
    {
        this.mem = mem;
    }

    public String getServer()
    {
        return server;
    }

    public void setServer(String server)
    {
        this.server = server;
    }

    public String getServerTime()
    {
        return serverTime;
    }

    public void setServerTime(String serverTime)
    {
        this.serverTime = serverTime;
    }

    public static class DataBean
    {
        /**
         * audioDuration : 0
         * audioId : 0
         * audioUrl :
         * bestAnswerId : 0
         * content : 小卡弟弟简简单单拿得起
         * courseId : 4489
         * courseName : H5收费课程
         * courseTitle :
         * ctime : 1484559168
         * gender : male
         * goodNum : 0
         * hitNum : 0
         * imgs : []
         * isDelete : 0
         * isFine : 0
         * isGood : 0
         * isGrab : 0
         * lockedUid : 0
         * postNum : 1
         * questionId : 37491
         * questionStatus : 0
         * role : student
         * score : 0
         * title : 小卡弟弟简简单单拿得起
         * uid : 1100233
         * userface : http://192.168.3.6:88/Public/files/user/2016/11-09/174026ad2e85418173.jpg?5.1.4
         * username : 签到五
         */

        private List<QuestionListBean> questionList;
        /**
         * type : studentNewReply
         * name : 新答复
         */

        private List<TypeListBean> typeList;

        public List<QuestionListBean> getQuestionList()
        {
            return questionList;
        }

        public void setQuestionList(List<QuestionListBean> questionList)
        {
            this.questionList = questionList;
        }

        public List<TypeListBean> getTypeList()
        {
            return typeList;
        }

        public void setTypeList(List<TypeListBean> typeList)
        {
            this.typeList = typeList;
        }

        public static class QuestionListBean implements Serializable
        {
            private String audioDuration;
            private String audioId;
            private String audioUrl;
            private String bestAnswerId;
            private String content;
            private String courseId;
            private String courseName;
            private String courseTitle;
            private String ctime;
            private String gender;
            private String goodNum;
            private String hitNum;
            private String isDelete;
            private String isFine;
            private String isGood;
            private String isGrab;
            private String lockedUid;
            private String postNum;
            private String questionId;
            private String questionStatus;
            private String role;
            private String score;
            private String title;
            private String uid;
            private String userface;
            private String username;
            private List<String> imgs;

            public String getAudioDuration()
            {
                return audioDuration;
            }

            public void setAudioDuration(String audioDuration)
            {
                this.audioDuration = audioDuration;
            }

            public String getAudioId()
            {
                return audioId;
            }

            public void setAudioId(String audioId)
            {
                this.audioId = audioId;
            }

            public String getAudioUrl()
            {
                return audioUrl;
            }

            public void setAudioUrl(String audioUrl)
            {
                this.audioUrl = audioUrl;
            }

            public String getBestAnswerId()
            {
                return bestAnswerId;
            }

            public void setBestAnswerId(String bestAnswerId)
            {
                this.bestAnswerId = bestAnswerId;
            }

            public String getContent()
            {
                return content;
            }

            public void setContent(String content)
            {
                this.content = content;
            }

            public String getCourseId()
            {
                return courseId;
            }

            public void setCourseId(String courseId)
            {
                this.courseId = courseId;
            }

            public String getCourseName()
            {
                return courseName;
            }

            public void setCourseName(String courseName)
            {
                this.courseName = courseName;
            }

            public String getCourseTitle()
            {
                return courseTitle;
            }

            public void setCourseTitle(String courseTitle)
            {
                this.courseTitle = courseTitle;
            }

            public String getCtime()
            {
                return ctime;
            }

            public void setCtime(String ctime)
            {
                this.ctime = ctime;
            }

            public String getGender()
            {
                return gender;
            }

            public void setGender(String gender)
            {
                this.gender = gender;
            }

            public String getGoodNum()
            {
                return goodNum;
            }

            public void setGoodNum(String goodNum)
            {
                this.goodNum = goodNum;
            }

            public String getHitNum()
            {
                return hitNum;
            }

            public void setHitNum(String hitNum)
            {
                this.hitNum = hitNum;
            }

            public String getIsDelete()
            {
                return isDelete;
            }

            public void setIsDelete(String isDelete)
            {
                this.isDelete = isDelete;
            }

            public String getIsFine()
            {
                return isFine;
            }

            public void setIsFine(String isFine)
            {
                this.isFine = isFine;
            }

            public String getIsGood()
            {
                return isGood;
            }

            public void setIsGood(String isGood)
            {
                this.isGood = isGood;
            }

            public String getIsGrab()
            {
                return isGrab;
            }

            public void setIsGrab(String isGrab)
            {
                this.isGrab = isGrab;
            }

            public String getLockedUid()
            {
                return lockedUid;
            }

            public void setLockedUid(String lockedUid)
            {
                this.lockedUid = lockedUid;
            }

            public String getPostNum()
            {
                return postNum;
            }

            public void setPostNum(String postNum)
            {
                this.postNum = postNum;
            }

            public String getQuestionId()
            {
                return questionId;
            }

            public void setQuestionId(String questionId)
            {
                this.questionId = questionId;
            }

            public String getQuestionStatus()
            {
                return questionStatus;
            }

            public void setQuestionStatus(String questionStatus)
            {
                this.questionStatus = questionStatus;
            }

            public String getRole()
            {
                return role;
            }

            public void setRole(String role)
            {
                this.role = role;
            }

            public String getScore()
            {
                return score;
            }

            public void setScore(String score)
            {
                this.score = score;
            }

            public String getTitle()
            {
                return title;
            }

            public void setTitle(String title)
            {
                this.title = title;
            }

            public String getUid()
            {
                return uid;
            }

            public void setUid(String uid)
            {
                this.uid = uid;
            }

            public String getUserface()
            {
                return userface;
            }

            public void setUserface(String userface)
            {
                this.userface = userface;
            }

            public String getUsername()
            {
                return username;
            }

            public void setUsername(String username)
            {
                this.username = username;
            }

            public List<String> getImgs()
            {
                return imgs;
            }

            public void setImgs(List<String> imgs)
            {
                this.imgs = imgs;
            }
        }

        public static class TypeListBean
        {
            private String type;
            private String name;

            public String getType()
            {
                return type;
            }

            public void setType(String type)
            {
                this.type = type;
            }

            public String getName()
            {
                return name;
            }

            public void setName(String name)
            {
                this.name = name;
            }
        }
    }
}

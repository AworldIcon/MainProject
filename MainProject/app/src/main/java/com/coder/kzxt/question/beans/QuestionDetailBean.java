package com.coder.kzxt.question.beans;

import java.util.List;

/**
 * Created by MaShiZhao on 2017/3/15
 */

public class QuestionDetailBean
{

    /**
     * question : {"audioDuration":0,"audioId":"0","audioUrl":"","bestAnswerId":"0","canGrab":"1","content":"小卡弟弟简简单单拿得起","courseId":"4489","courseName":"H5收费课程","courseTitle":"","ctime":"1484559168","gender":"male","goodNum":"0","hitNum":"0","imgs":[],"isCollect":"0","isDelete":"0","isFine":"0","isGood":"0","isGrab":"0","lockedUid":"0","postNum":"1","questionId":"37491","questionStatus":"0","role":"student","score":"0","shareUrl":"http://192.168.3.6:88/Course/CourseThread/showAction/courseId/4489/id/37491","title":"小卡弟弟简简单单拿得起","uid":"1100233","userface":"http://192.168.3.6:88/Public/files/user/2016/11-09/174026ad2e85418173.jpgString5.1.4","username":"签到五"}
     * answerList : [{"answerId":"127349","audioDuration":0,"audioId":"0","audioUrl":"","bestAnswerId":"0","content":"","ctime":"1484559210","gender":"male","goodNum":"0","imgs":[],"isBest":"0","isGood":"0","postNum":"13","questionId":"37491","questionStatus":"0","role":"teacher","score":"0","uid":"2175","userface":"http://192.168.3.6:88/Public/files/user/2016/11-30/11573931c37a851844.jpgString5.1.4","username":"咚咚幺"}]
     */

    private DataBean data;
    /**
     * data : {"question":{"audioDuration":0,"audioId":"0","audioUrl":"","bestAnswerId":"0","canGrab":"1","content":"小卡弟弟简简单单拿得起","courseId":"4489","courseName":"H5收费课程","courseTitle":"","ctime":"1484559168","gender":"male","goodNum":"0","hitNum":"0","imgs":[],"isCollect":"0","isDelete":"0","isFine":"0","isGood":"0","isGrab":"0","lockedUid":"0","postNum":"1","questionId":"37491","questionStatus":"0","role":"student","score":"0","shareUrl":"http://192.168.3.6:88/Course/CourseThread/showAction/courseId/4489/id/37491","title":"小卡弟弟简简单单拿得起","uid":"1100233","userface":"http://192.168.3.6:88/Public/files/user/2016/11-09/174026ad2e85418173.jpgString5.1.4","username":"签到五"},"answerList":[{"answerId":"127349","audioDuration":0,"audioId":"0","audioUrl":"","bestAnswerId":"0","content":"","ctime":"1484559210","gender":"male","goodNum":"0","imgs":[],"isBest":"0","isGood":"0","postNum":"13","questionId":"37491","questionStatus":"0","role":"teacher","score":"0","uid":"2175","userface":"http://192.168.3.6:88/Public/files/user/2016/11-30/11573931c37a851844.jpgString5.1.4","username":"咚咚幺"}]}
     * page : 1
     * totalPages : 1
     * code : 1000
     * msg : 成功
     * xhprof : no setup
     * runTm : s:1489542179.227 e:1489542179.642 tms=415ms
     * mem : 21.52 MB
     * server : wt1.nazhengshu.com
     * serverTime : 1489542179
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
         * canGrab : 1
         * content : 小卡弟弟简简单单拿得起
         * courseId : 4489
         * courseName : H5收费课程
         * courseTitle :
         * ctime : 1484559168
         * gender : male
         * goodNum : 0
         * hitNum : 0
         * imgs : []
         * isCollect : 0
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
         * shareUrl : http://192.168.3.6:88/Course/CourseThread/showAction/courseId/4489/id/37491
         * title : 小卡弟弟简简单单拿得起
         * uid : 1100233
         * userface : http://192.168.3.6:88/Public/files/user/2016/11-09/174026ad2e85418173.jpgString5.1.4
         * username : 签到五
         */

        private QuestionBean question;
        /**
         * answerId : 127349
         * audioDuration : 0
         * audioId : 0
         * audioUrl :
         * bestAnswerId : 0
         * content :
         * ctime : 1484559210
         * gender : male
         * goodNum : 0
         * imgs : []
         * isBest : 0
         * isGood : 0
         * postNum : 13
         * questionId : 37491
         * questionStatus : 0
         * role : teacher
         * score : 0
         * uid : 2175
         * userface : http://192.168.3.6:88/Public/files/user/2016/11-30/11573931c37a851844.jpgString5.1.4
         * username : 咚咚幺
         */

        private List<AnswerListBean> answerList;

        public QuestionBean getQuestion()
        {
            return question;
        }

        public void setQuestion(QuestionBean question)
        {
            this.question = question;
        }

        public List<AnswerListBean> getAnswerList()
        {
            return answerList;
        }

        public void setAnswerList(List<AnswerListBean> answerList)
        {
            this.answerList = answerList;
        }

        public static class QuestionBean
        {
            private int audioDuration;
            private String audioId;
            private String audioUrl;
            private String bestAnswerId;
            private String canGrab;
            private String content;
            private String courseId;
            private String courseName;
            private String courseTitle;
            private String ctime;
            private String gender;
            private String goodNum;
            private String hitNum;
            private String isCollect;
            private String isDelete;
            private String isFine;
            private String isGood;
            private String isGrab;
            private String lockedUid;
            private String postNum;
            private String questionId;
            private String questionStatus; // 解决状态 0待解决 1已经解决
            private String role;
            private String score;
            private String shareUrl;
            private String title;
            private String uid;
            private String userface;
            private String username;
            private String publicCourse;
            private String myId;
            private List<String> imgs;

            public int getAudioDuration()
            {
                return audioDuration;
            }

            public void setAudioDuration(int audioDuration)
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

            public String getCanGrab()
            {
                return canGrab;
            }

            public void setCanGrab(String canGrab)
            {
                this.canGrab = canGrab;
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

            public String getIsCollect()
            {
                return isCollect;
            }

            public void setIsCollect(String isCollect)
            {
                this.isCollect = isCollect;
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

            public String getShareUrl()
            {
                return shareUrl;
            }

            public void setShareUrl(String shareUrl)
            {
                this.shareUrl = shareUrl;
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

            public String getPublicCourse()
            {
                return publicCourse;
            }

            public void setPublicCourse(String publicCourse)
            {
                this.publicCourse = publicCourse;
            }


            public String getMyId()
            {
                return myId;
            }

            public void setMyId(String myId)
            {
                this.myId = myId;
            }
        }

        public static class AnswerListBean
        {
            private String answerId;
            private int audioDuration;
            private String audioId;
            private String audioUrl;
            private String bestAnswerId;
            private String content;
            private String ctime;
            private String gender;
            private String goodNum;
            private String isBest;
            private String isGood;
            private String postNum;
            private String questionId;
            private String questionStatus;
            private String role;
            private String score;
            private String uid;
            private String userface;
            private String username;
            private List<String> imgs;

            public String getAnswerId()
            {
                return answerId;
            }

            public void setAnswerId(String answerId)
            {
                this.answerId = answerId;
            }

            public int getAudioDuration()
            {
                return audioDuration;
            }

            public void setAudioDuration(int audioDuration)
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

            public String getIsBest()
            {
                return isBest;
            }

            public void setIsBest(String isBest)
            {
                this.isBest = isBest;
            }

            public String getIsGood()
            {
                return isGood;
            }

            public void setIsGood(String isGood)
            {
                this.isGood = isGood;
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
    }
}

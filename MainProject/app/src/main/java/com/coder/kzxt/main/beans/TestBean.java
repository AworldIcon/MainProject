package com.coder.kzxt.main.beans;

import java.util.List;

/**
 * Created by MaShiZhao on 2017/2/7
 */
public class TestBean
{

    /**
     * data : [{"id":"546","classId":"1287","courseId":"750","show":"0","title":"2017","createdTime":"1481015542","testId":"640","type":"1","userId":"1747","startTime":"1486173840","endTime":"1491271380","release":"1","testCount":"0","itemCount":"4","limitedTime":"0","score":"8.0","className":"默认授课班","publish":"1","perfect":0,"finish_num":"1","classMemberNum":"4","publicCourse":"0"},{"id":"544","classId":"1287","courseId":"750","show":"0","title":"默认授课班作业2016126","createdTime":"1481015245","testId":"638","type":"1","userId":"1747","startTime":"0","endTime":"0","release":"0","testCount":"0","itemCount":"1","limitedTime":"0","score":"2.0","className":"默认授课班","publish":"0","perfect":1,"finish_num":"0","classMemberNum":"4","publicCourse":"0"},{"id":"516","classId":"1287","courseId":"750","show":"0","title":"听一下","createdTime":"1480924222","testId":"609","type":"1","userId":"1556","startTime":"1480924200","endTime":"1480924680","release":"1","testCount":"0","itemCount":"4","limitedTime":"0","score":"8.0","className":"默认授课班","publish":"1","perfect":0,"finish_num":"0","classMemberNum":"4","publicCourse":"0"},{"id":"508","classId":"1287","courseId":"750","show":"0","title":"剧透了尽量","createdTime":"1480920573","testId":"601","type":"1","userId":"1556","startTime":"1480920600","endTime":"1480921080","release":"1","testCount":"0","itemCount":"7","limitedTime":"0","score":"14.0","className":"默认授课班","publish":"1","perfect":0,"finish_num":"1","classMemberNum":"4","publicCourse":"0"}]
     * code : 1000
     * msg : 成功
     * xhprof : no setup
     * runTm : s:1486451213.995 e:1486451214.203 tms=207ms
     * mem : 23.86 MB
     * server : online-test
     * serverTime : 1486451214
     */

    private int code;
    private String msg;
    private String xhprof;
    private String runTm;
    private String mem;
    private String server;
    private String serverTime;
    /**
     * id : 546
     * classId : 1287
     * courseId : 750
     * show : 0
     * title : 2017
     * createdTime : 1481015542
     * testId : 640
     * type : 1
     * userId : 1747
     * startTime : 1486173840
     * endTime : 1491271380
     * release : 1
     * testCount : 0
     * itemCount : 4
     * limitedTime : 0
     * score : 8.0
     * className : 默认授课班
     * publish : 1
     * perfect : 0
     * finish_num : 1
     * classMemberNum : 4
     * publicCourse : 0
     */

    private List<DataBean> data;

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

    public List<DataBean> getData()
    {
        return data;
    }

    public void setData(List<DataBean> data)
    {
        this.data = data;
    }

    public static class DataBean
    {
        private String id;
        private String classId;
        private String courseId;
        private String show;
        private String title;
        private String createdTime;
        private String testId;
        private String type;
        private String userId;
        private String startTime;
        private String endTime;
        private String release;
        private String testCount;
        private String itemCount;
        private String limitedTime;
        private String score;
        private String className;
        private String publish;
        private int perfect;
        private String finish_num;
        private String classMemberNum;
        private String publicCourse;

        public String getId()
        {
            return id;
        }

        public void setId(String id)
        {
            this.id = id;
        }

        public String getClassId()
        {
            return classId;
        }

        public void setClassId(String classId)
        {
            this.classId = classId;
        }

        public String getCourseId()
        {
            return courseId;
        }

        public void setCourseId(String courseId)
        {
            this.courseId = courseId;
        }

        public String getShow()
        {
            return show;
        }

        public void setShow(String show)
        {
            this.show = show;
        }

        public String getTitle()
        {
            return title;
        }

        public void setTitle(String title)
        {
            this.title = title;
        }

        public String getCreatedTime()
        {
            return createdTime;
        }

        public void setCreatedTime(String createdTime)
        {
            this.createdTime = createdTime;
        }

        public String getTestId()
        {
            return testId;
        }

        public void setTestId(String testId)
        {
            this.testId = testId;
        }

        public String getType()
        {
            return type;
        }

        public void setType(String type)
        {
            this.type = type;
        }

        public String getUserId()
        {
            return userId;
        }

        public void setUserId(String userId)
        {
            this.userId = userId;
        }

        public String getStartTime()
        {
            return startTime;
        }

        public void setStartTime(String startTime)
        {
            this.startTime = startTime;
        }

        public String getEndTime()
        {
            return endTime;
        }

        public void setEndTime(String endTime)
        {
            this.endTime = endTime;
        }

        public String getRelease()
        {
            return release;
        }

        public void setRelease(String release)
        {
            this.release = release;
        }

        public String getTestCount()
        {
            return testCount;
        }

        public void setTestCount(String testCount)
        {
            this.testCount = testCount;
        }

        public String getItemCount()
        {
            return itemCount;
        }

        public void setItemCount(String itemCount)
        {
            this.itemCount = itemCount;
        }

        public String getLimitedTime()
        {
            return limitedTime;
        }

        public void setLimitedTime(String limitedTime)
        {
            this.limitedTime = limitedTime;
        }

        public String getScore()
        {
            return score;
        }

        public void setScore(String score)
        {
            this.score = score;
        }

        public String getClassName()
        {
            return className;
        }

        public void setClassName(String className)
        {
            this.className = className;
        }

        public String getPublish()
        {
            return publish;
        }

        public void setPublish(String publish)
        {
            this.publish = publish;
        }

        public int getPerfect()
        {
            return perfect;
        }

        public void setPerfect(int perfect)
        {
            this.perfect = perfect;
        }

        public String getFinish_num()
        {
            return finish_num;
        }

        public void setFinish_num(String finish_num)
        {
            this.finish_num = finish_num;
        }

        public String getClassMemberNum()
        {
            return classMemberNum;
        }

        public void setClassMemberNum(String classMemberNum)
        {
            this.classMemberNum = classMemberNum;
        }

        public String getPublicCourse()
        {
            return publicCourse;
        }

        public void setPublicCourse(String publicCourse)
        {
            this.publicCourse = publicCourse;
        }
    }
}

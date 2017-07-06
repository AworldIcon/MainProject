package com.coder.kzxt.gambit.activity.bean;

import java.util.List;

/**
 * Created by pc on 2017/3/15.
 */

public class ClassGambitsBean {

    /**
     * data : []
     * totalPages : 0
     * myClass : [{"className":"班级名称","classId":"73","status":"0","logo":"http://192.168.3.6:88/Public/files/user/2016/06-18/1636064410.png?5.1.4","memberNum":"2","createUid":"2198","categoryId":"9","defaultLogo":0,"createName":"","categoryName":"人类进化史"},{"className":"影视动画1班","classId":"157","status":"0","logo":"http://192.168.3.6:88/Public/assets/img/default/group.png","memberNum":"1","createUid":"2085","categoryId":"11725","defaultLogo":1,"createName":"老卢","categoryName":"影视动画"},{"className":"图15662","classId":"5","status":"0","logo":"http://192.168.3.6:88/Public/files/user/2016/06-15/16235391551a028502.gif?5.1.4","memberNum":"17","createUid":"2124","categoryId":"9","defaultLogo":0,"createName":"哈哈刘","categoryName":"人类进化史"}]
     * canSubmitTopic : 0
     * typeList : [{"name":"全部话题","type":"allClassTopic"},{"name":"精华","type":"bestTopic"},{"name":"我发布的","type":"myIssueTopic"},{"name":"我回复的","type":"myReplyTopic"}]
     * code : 1000
     * msg : 成功
     * xhprof : no setup
     * runTm : s:1489561104.823 e:1489561105.127 tms=304ms
     * mem : 20.27 MB
     * server : wt1.nazhengshu.com
     * serverTime : 1489561105
     */

    private int totalPages;
    private String canSubmitTopic;
    private int code;
    private String msg;
    private String xhprof;
    private String runTm;
    private String mem;
    private String server;
    private String serverTime;
    private List<MyClassBean> myClass;
    private List<TypeListBean> typeList;

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public String getCanSubmitTopic() {
        return canSubmitTopic;
    }

    public void setCanSubmitTopic(String canSubmitTopic) {
        this.canSubmitTopic = canSubmitTopic;
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

    public List<MyClassBean> getMyClass() {
        return myClass;
    }

    public void setMyClass(List<MyClassBean> myClass) {
        this.myClass = myClass;
    }

    public List<TypeListBean> getTypeList() {
        return typeList;
    }

    public void setTypeList(List<TypeListBean> typeList) {
        this.typeList = typeList;
    }

    public static class MyClassBean {
        /**
         * className : 班级名称
         * classId : 73
         * status : 0
         * logo : http://192.168.3.6:88/Public/files/user/2016/06-18/1636064410.png?5.1.4
         * memberNum : 2
         * createUid : 2198
         * categoryId : 9
         * defaultLogo : 0
         * createName :
         * categoryName : 人类进化史
         */

        private String className;
        private String classId;
        private String status;
        private String logo;
        private String memberNum;
        private String createUid;
        private String categoryId;
        private int defaultLogo;
        private String createName;
        private String categoryName;

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public String getClassId() {
            return classId;
        }

        public void setClassId(String classId) {
            this.classId = classId;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getMemberNum() {
            return memberNum;
        }

        public void setMemberNum(String memberNum) {
            this.memberNum = memberNum;
        }

        public String getCreateUid() {
            return createUid;
        }

        public void setCreateUid(String createUid) {
            this.createUid = createUid;
        }

        public String getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }

        public int getDefaultLogo() {
            return defaultLogo;
        }

        public void setDefaultLogo(int defaultLogo) {
            this.defaultLogo = defaultLogo;
        }

        public String getCreateName() {
            return createName;
        }

        public void setCreateName(String createName) {
            this.createName = createName;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }
    }

    public static class TypeListBean {
        /**
         * name : 全部话题
         * type : allClassTopic
         */

        private String name;
        private String type;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}

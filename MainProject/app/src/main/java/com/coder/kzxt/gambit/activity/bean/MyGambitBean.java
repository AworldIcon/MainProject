package com.coder.kzxt.gambit.activity.bean;

import java.util.List;

/**
 * Created by pc on 2017/3/13.
 */

public class MyGambitBean {

    /**
     * data : [{"id":"116","title":"的歌","audioId":"178","groupId":"189","userId":"1099359","createdTime":"49分钟前","postNum":"0","hitNum":"0","className":"呃呃呃呃呃","userName":"user2","userGender":"female","userFace":"http://192.168.3.6:88/Public/files/user/2016/08-10/140118ee11ce031060.jpg?5.1.4","isTop":"0","img":[],"content":"16464","audioUrl":"http://192.168.3.6:88/Data/private_files/audio/201703/1489398918_GALTjKCytZ.wav","audioDuration":"1","likeNum":"0","isLiked":"0"},{"id":"115","title":"丁敏","audioId":"177","groupId":"189","userId":"1099359","createdTime":"50分钟前","postNum":"0","hitNum":"1","className":"呃呃呃呃呃","userName":"user2","userGender":"female","userFace":"http://192.168.3.6:88/Public/files/user/2016/08-10/140118ee11ce031060.jpg?5.1.4","isTop":"0","img":["http://192.168.3.6:88/Data/private_files/editor/20170313/14893988608513.jpeg"],"content":"13464994","audioUrl":"http://192.168.3.6:88/Data/private_files/audio/201703/1489398860_IbtKBDSqLh.wav","audioDuration":"1","likeNum":"0","isLiked":"0"},{"id":"114","title":"呃呃呃额额","audioId":"0","groupId":"189","userId":"1099359","createdTime":"1小时前","postNum":"0","hitNum":"0","className":"呃呃呃呃呃","userName":"user2","userGender":"female","userFace":"http://192.168.3.6:88/Public/files/user/2016/08-10/140118ee11ce031060.jpg?5.1.4","isTop":"0","img":["http://192.168.3.6:88/Data/private_files/editor/20170313/14893979353465.jpeg","http://192.168.3.6:88/Data/private_files/editor/20170313/14893979358545.jpeg","http://192.168.3.6:88/Data/private_files/editor/20170313/14893979355802.jpeg"],"content":"恶魔哦哦","audioUrl":"","audioDuration":0,"likeNum":"0","isLiked":"0"},{"id":"113","title":"ijh","audioId":"0","groupId":"189","userId":"1099359","createdTime":"1小时前","postNum":"0","hitNum":"1","className":"呃呃呃呃呃","userName":"user2","userGender":"female","userFace":"http://192.168.3.6:88/Public/files/user/2016/08-10/140118ee11ce031060.jpg?5.1.4","isTop":"0","img":["http://192.168.3.6:88/Data/private_files/editor/20170313/14893972552672.jpeg","http://192.168.3.6:88/Data/private_files/editor/20170313/14893972559098.jpeg"],"content":"yhbhh","audioUrl":"","audioDuration":0,"likeNum":"0","isLiked":"0"},{"id":"101","title":"DJ哦哦哦","audioId":"0","groupId":"189","userId":"1099359","createdTime":"12-15","postNum":"0","hitNum":"4","className":"呃呃呃呃呃","userName":"user2","userGender":"female","userFace":"http://192.168.3.6:88/Public/files/user/2016/08-10/140118ee11ce031060.jpg?5.1.4","isTop":"0","img":[],"content":"看看咯","audioUrl":"","audioDuration":0,"likeNum":"0","isLiked":"0"}]
     * totalPages : 1
     * myClass : [{"className":"呃呃呃呃呃","classId":"189","status":"1","logo":"http://192.168.3.6:88/Public/assets/img/default/group.png","memberNum":"4","createUid":"1099359","categoryId":"11746","defaultLogo":1,"createName":"user2","categoryName":"土木工程"}]
     * code : 1000
     * msg : 成功
     * xhprof : no setup
     * runTm : s:1489401837.510 e:1489401837.858 tms=348ms
     * mem : 21.62 MB
     * server : wt1.nazhengshu.com
     * serverTime : 1489401837
     */

    private int totalPages;
    private int code;
    private String msg;
    private String xhprof;
    private String runTm;
    private String mem;
    private String server;
    private String serverTime;
    private List<DataBean> data;
    private List<MyClassBean> myClass;

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public List<MyClassBean> getMyClass() {
        return myClass;
    }

    public void setMyClass(List<MyClassBean> myClass) {
        this.myClass = myClass;
    }

    public static class DataBean {
        /**
         * id : 116
         * title : 的歌
         * audioId : 178
         * groupId : 189
         * userId : 1099359
         * createdTime : 49分钟前
         * postNum : 0
         * hitNum : 0
         * className : 呃呃呃呃呃
         * userName : user2
         * userGender : female
         * userFace : http://192.168.3.6:88/Public/files/user/2016/08-10/140118ee11ce031060.jpg?5.1.4
         * isTop : 0
         * img : []
         * content : 16464
         * audioUrl : http://192.168.3.6:88/Data/private_files/audio/201703/1489398918_GALTjKCytZ.wav
         * audioDuration : 1
         * likeNum : 0
         * isLiked : 0
         */

        private String id;
        private String title;
        private String audioId;
        private String groupId;
        private String userId;
        private String createdTime;
        private String postNum;
        private String hitNum;
        private String className;
        private String userName;
        private String userGender;
        private String userFace;
        private String isTop;
        private String content;
        private String audioUrl;
        private String audioDuration;
        private String likeNum;
        private String isLiked;
        private List<String> img;

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

        public String getAudioId() {
            return audioId;
        }

        public void setAudioId(String audioId) {
            this.audioId = audioId;
        }

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getCreatedTime() {
            return createdTime;
        }

        public void setCreatedTime(String createdTime) {
            this.createdTime = createdTime;
        }

        public String getPostNum() {
            return postNum;
        }

        public void setPostNum(String postNum) {
            this.postNum = postNum;
        }

        public String getHitNum() {
            return hitNum;
        }

        public void setHitNum(String hitNum) {
            this.hitNum = hitNum;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserGender() {
            return userGender;
        }

        public void setUserGender(String userGender) {
            this.userGender = userGender;
        }

        public String getUserFace() {
            return userFace;
        }

        public void setUserFace(String userFace) {
            this.userFace = userFace;
        }

        public String getIsTop() {
            return isTop;
        }

        public void setIsTop(String isTop) {
            this.isTop = isTop;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getAudioUrl() {
            return audioUrl;
        }

        public void setAudioUrl(String audioUrl) {
            this.audioUrl = audioUrl;
        }

        public String getAudioDuration() {
            return audioDuration;
        }

        public void setAudioDuration(String audioDuration) {
            this.audioDuration = audioDuration;
        }

        public String getLikeNum() {
            return likeNum;
        }

        public void setLikeNum(String likeNum) {
            this.likeNum = likeNum;
        }

        public String getIsLiked() {
            return isLiked;
        }

        public void setIsLiked(String isLiked) {
            this.isLiked = isLiked;
        }

        public List<String> getImg() {
            return img;
        }

        public void setImg(List<String> img) {
            this.img = img;
        }
    }

    public static class MyClassBean {
        /**
         * className : 呃呃呃呃呃
         * classId : 189
         * status : 1
         * logo : http://192.168.3.6:88/Public/assets/img/default/group.png
         * memberNum : 4
         * createUid : 1099359
         * categoryId : 11746
         * defaultLogo : 1
         * createName : user2
         * categoryName : 土木工程
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
}

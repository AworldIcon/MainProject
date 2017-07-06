package com.coder.kzxt.main.beans;

import java.util.List;

/**
 * Created by Administrator on 2017/3/8.
 */

public class CutSpreadBean {


    /**
     * data : {"id":"75","title":"插屏","type":"apply","image":"http://192.168.3.6:88/Data/private_files/Upload/user11000-1000.jpg","startTime":"1488902400","endTime":"1490975940","periodStart":1488902400,"periodEnd":1488988740,"remark":"","editTime":"0","addTime":"1488942312","joinUrl":"","position":"cut","userId":"2190","editUid":"0","isClosed":"0","webCode":"user1","owebPriv":"-","channelType":"1","channel":["market_yd_youxi","market_yd_360","market_yd_yingyongbao","market_yd_meizu","market_yd_xiaomi","market_yd_baidu","market_yd_wangdoujia","market_yd_huawei","market_yd_ditui"]}
     * code : 1000
     * msg : 成功
     * xhprof : no setup
     * runTm : s:1488942552.183 e:1488942552.596 tms=413ms
     * mem : 18.92 MB
     * server : wt1.nazhengshu.com
     * serverTime : 1488942552
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

    public static class DataBean {
        /**
         * id : 75
         * title : 插屏
         * type : apply
         * image : http://192.168.3.6:88/Data/private_files/Upload/user11000-1000.jpg
         * startTime : 1488902400
         * endTime : 1490975940
         * periodStart : 1488902400
         * periodEnd : 1488988740
         * remark :
         * editTime : 0
         * addTime : 1488942312
         * joinUrl :
         * position : cut
         * userId : 2190
         * editUid : 0
         * isClosed : 0
         * webCode : user1
         * owebPriv : -
         * channelType : 1
         * channel : ["market_yd_youxi","market_yd_360","market_yd_yingyongbao","market_yd_meizu","market_yd_xiaomi","market_yd_baidu","market_yd_wangdoujia","market_yd_huawei","market_yd_ditui"]
         */

        private String id;
        private String title;
        private String type;
        private String image;
        private String startTime;
        private String endTime;
        private int periodStart;
        private int periodEnd;
        private String remark;
        private String editTime;
        private String addTime;
        private String joinUrl;
        private String position;
        private String userId;
        private String editUid;
        private String isClosed;
        private String webCode;
        private String owebPriv;
        private String channelType;
        private List<String> channel;

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

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public int getPeriodStart() {
            return periodStart;
        }

        public void setPeriodStart(int periodStart) {
            this.periodStart = periodStart;
        }

        public int getPeriodEnd() {
            return periodEnd;
        }

        public void setPeriodEnd(int periodEnd) {
            this.periodEnd = periodEnd;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getEditTime() {
            return editTime;
        }

        public void setEditTime(String editTime) {
            this.editTime = editTime;
        }

        public String getAddTime() {
            return addTime;
        }

        public void setAddTime(String addTime) {
            this.addTime = addTime;
        }

        public String getJoinUrl() {
            return joinUrl;
        }

        public void setJoinUrl(String joinUrl) {
            this.joinUrl = joinUrl;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getEditUid() {
            return editUid;
        }

        public void setEditUid(String editUid) {
            this.editUid = editUid;
        }

        public String getIsClosed() {
            return isClosed;
        }

        public void setIsClosed(String isClosed) {
            this.isClosed = isClosed;
        }

        public String getWebCode() {
            return webCode;
        }

        public void setWebCode(String webCode) {
            this.webCode = webCode;
        }

        public String getOwebPriv() {
            return owebPriv;
        }

        public void setOwebPriv(String owebPriv) {
            this.owebPriv = owebPriv;
        }

        public String getChannelType() {
            return channelType;
        }

        public void setChannelType(String channelType) {
            this.channelType = channelType;
        }

        public List<String> getChannel() {
            return channel;
        }

        public void setChannel(List<String> channel) {
            this.channel = channel;
        }
    }
}

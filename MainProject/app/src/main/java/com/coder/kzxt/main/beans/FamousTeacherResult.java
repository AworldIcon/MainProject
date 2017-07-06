package com.coder.kzxt.main.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by MaShiZhao on 16/9/19.
 */
public class FamousTeacherResult {

    /**
     * list : [{"userId":"234","userName":"花粉管","userFace":"http://sdfsdfsd.png"},{"userId":"234","userName":"花粉管","userFace":"http://sdfsdfsd.png"}]
     * totalPage : 0
     */

    private DataBean data;
    /**
     * data : {"list":[{"userId":"234","userName":"花粉管","userFace":"http://sdfsdfsd.png"},{"userId":"234","userName":"花粉管","userFace":"http://sdfsdfsd.png"}],"totalPage":0}
     * code : 1000
     * msg : 成功
     * xhprof : no setup
     * runTm : s:1474281115.100 e:1474281115.147 tms=46ms
     * mem : 3.94 MB
     * server : wt1.nazhengshu.com
     * serverTime : 1474281115
     */

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
        private int totalPage;
        /**
         * userId : 234
         * userName : 花粉管
         * userFace : http://sdfsdfsd.png
         */

        private List<ListBean> list;

        public int getTotalPage() {
            return totalPage;
        }

        public void setTotalPage(int totalPage) {
            this.totalPage = totalPage;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean implements Serializable{
            private String userId;
            private String userName;
            private String brief;
            private String userFace;
            private String bannerUrl;

            public String getBrief() {
                return brief;
            }

            public void setBrief(String brief) {
                this.brief = brief;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public String getUserName() {
                return userName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }

            public String getUserFace() {
                return userFace;
            }

            public void setUserFace(String userFace) {
                this.userFace = userFace;
            }

            public String getBannerUrl() {
                return bannerUrl;
            }

            public void setBannerUrl(String bannerUrl) {
                this.bannerUrl = bannerUrl;
            }
        }
    }
}

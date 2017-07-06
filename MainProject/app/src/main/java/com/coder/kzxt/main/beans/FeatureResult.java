package com.coder.kzxt.main.beans;

import java.util.List;

/**
 * 功能区和精品课程推荐都要用到
 * 
 */
public class FeatureResult {


    /**
     * list : [{"id":"1385","name":"幻影刺客教学","image":"http://lwl.cloud.com/Public/assets/img/default/largecoursePicture?5.1.4"},{"id":"1385","name":"幻影刺客教学","image":"http://lwl.cloud.com/Public/assets/img/default/largecoursePicture?5.1.4"}]
     * totalPage : 0
     */

    private DataBean data;
    /**
     * data : {"list":[{"id":"1385","name":"幻影刺客教学","image":"http://lwl.cloud.com/Public/assets/img/default/largecoursePicture?5.1.4"},{"id":"1385","name":"幻影刺客教学","image":"http://lwl.cloud.com/Public/assets/img/default/largecoursePicture?5.1.4"}],"totalPage":0}
     * code : 1000
     * msg : 成功
     * xhprof : no setup
     * runTm : s:1474355036.475 e:1474355036.534 tms=58ms
     * mem : 4.23 MB
     * server : wt1.nazhengshu.com
     * serverTime : 1474355036
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
         * id : 1385
         * name : 幻影刺客教学
         * image : http://lwl.cloud.com/Public/assets/img/default/largecoursePicture?5.1.4
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

        public static class ListBean {
            private String id;
            private String name;
            private String icon;

            public String getType() {
                if (type == null) {
                    return "";
                }
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            private String type;//区分跳转类型

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }
        }
    }
}

package com.coder.kzxt.main.beans;

import java.util.List;

/**
 * Created by MaShiZhao on 16/9/18.
 */
public class BannerResult {


    /**
     * newsUrl :
     * banner : [{"img":"http://192.168.3.6:88/Public/files/system/mobile_picture1470818070.jpg.w800.jpg","url":"","title":"未设置标题"},{"img":"http://192.168.3.6:88/Public/files/system/mobile_picture1470818081.jpg.w800.jpg","url":"","title":"未设置标题"},{"img":"http://192.168.3.6:88/Public/files/system/mobile_picture1470818114.jpg.w800.jpg","url":"","title":"未设置标题"},{"img":"http://192.168.3.6:88/Public/files/system/mobile_picture1470821397.jpg.w800.jpg","url":"","title":"未设置标题"}]
     */

    private DataBean data;
    /**
     * data : {"newsUrl":"","banner":[{"img":"http://192.168.3.6:88/Public/files/system/mobile_picture1470818070.jpg.w800.jpg","url":"","title":"未设置标题"},{"img":"http://192.168.3.6:88/Public/files/system/mobile_picture1470818081.jpg.w800.jpg","url":"","title":"未设置标题"},{"img":"http://192.168.3.6:88/Public/files/system/mobile_picture1470818114.jpg.w800.jpg","url":"","title":"未设置标题"},{"img":"http://192.168.3.6:88/Public/files/system/mobile_picture1470821397.jpg.w800.jpg","url":"","title":"未设置标题"}]}
     * code : 1000
     * msg : 成功
     * xhprof : no setup
     * runTm : s:1474191151.558 e:1474191151.755 tms=197ms
     * mem : 19.16 MB
     * server : wt1.nazhengshu.com
     * serverTime : 1474191151
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
        private String newsUrl;
        /**
         * img : http://192.168.3.6:88/Public/files/system/mobile_picture1470818070.jpg.w800.jpg
         * url :
         * title : 未设置标题
         */

        private List<BannerBean> banner;
        private List<BannerBean> list;

        public String getNewsUrl() {
            return newsUrl;
        }

        public void setNewsUrl(String newsUrl) {
            this.newsUrl = newsUrl;
        }

        public List<BannerBean> getBanner() {
            return banner;
        }

        public void setBanner(List<BannerBean> banner) {
            this.banner = banner;
        }

        public List<BannerBean> getList() {
            return list;
        }

        public void setList(List<BannerBean> list) {
            this.list = list;
        }

        public static class BannerBean {
            private String img;
            private String url;
            private String title;

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }
        }
    }
}

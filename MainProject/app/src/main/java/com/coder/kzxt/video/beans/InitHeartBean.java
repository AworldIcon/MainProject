package com.coder.kzxt.video.beans;

/**
 * Created by Administrator on 2017/4/17.
 */

public class InitHeartBean {


    /**
     * code : 200
     * message : ok
     * item : {"deviceId":"加密设备id","live_id":"直播id","reportTime":"心跳时间"}
     */

    private String code;
    private String message;
    private ItemBean item;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ItemBean getItem() {
        return item;
    }

    public void setItem(ItemBean item) {
        this.item = item;
    }

    public static class ItemBean {
        /**
         * deviceId : 加密设备id
         * live_id : 直播id
         * reportTime : 心跳时间
         */

        private String deviceId;
        private String live_id;
        private String reportTime;

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public String getLive_id() {
            return live_id;
        }

        public void setLive_id(String live_id) {
            this.live_id = live_id;
        }

        public String getReportTime() {
            return reportTime;
        }

        public void setReportTime(String reportTime) {
            this.reportTime = reportTime;
        }
    }
}

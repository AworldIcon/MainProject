package com.coder.kzxt.video.beans;

/**
 * Created by Administrator on 2017/4/17.
 */

public class HeadrtBean {


    /**
     * code : 200
     * message : ok
     * item : {"duration":"播放总时长","isVideo":"0=>直播流  1=>录播视频","liveStatus":"直播状态 0=>未开始 1=>直播中 2=>已结束 3=>已关闭","liveUrl":"视频播放地址","liveVideoStatus":"未开始=>''ready''   直播中=>''playing''  切换=>switch  停止=>\u2019stop\u2018","rangeTime":"录播误差时间","videoState":"录播视频状态"}
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
         * duration : 播放总时长
         * isVideo : 0=>直播流  1=>录播视频
         * liveStatus : 直播状态 0=>未开始 1=>直播中 2=>已结束 3=>已关闭
         * liveUrl : 视频播放地址
         * liveVideoStatus : 未开始=>''ready''   直播中=>''playing''  切换=>switch  停止=>’stop‘
         * rangeTime : 录播误差时间
         * videoState : 录播视频状态
         */

        private String duration;
        private int isVideo;
        private int liveStatus;
        private String liveUrl;
        private String liveVideoStatus;
        private int rangeTime;
        private String videoState;
        private int position;
        private String studyNum;

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public int getIsVideo() {
            return isVideo;
        }

        public void setIsVideo(int isVideo) {
            this.isVideo = isVideo;
        }

        public int getLiveStatus() {
            return liveStatus;
        }

        public void setLiveStatus(int liveStatus) {
            this.liveStatus = liveStatus;
        }

        public String getLiveUrl() {
            return liveUrl;
        }

        public void setLiveUrl(String liveUrl) {
            this.liveUrl = liveUrl;
        }

        public String getLiveVideoStatus() {
            return liveVideoStatus;
        }

        public void setLiveVideoStatus(String liveVideoStatus) {
            this.liveVideoStatus = liveVideoStatus;
        }

        public int getRangeTime() {
            return rangeTime;
        }

        public void setRangeTime(int rangeTime) {
            this.rangeTime = rangeTime;
        }

        public String getVideoState() {
            return videoState;
        }

        public void setVideoState(String videoState) {
            this.videoState = videoState;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public String getStudyNum() {
            return studyNum;
        }

        public void setStudyNum(String studyNum) {
            this.studyNum = studyNum;
        }
    }
}

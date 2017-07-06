package com.coder.kzxt.course.beans;

import com.coder.kzxt.base.beans.LiveBean;

import java.util.List;

/**
 * Created by Administrator on 2017/4/24.
 */

public class CourseBelowLiveBean {


    /**
     * code : 200
     * message : ok
     * paginate : {"currentPage":1,"pageSize":1,"pageNum":2,"totalNum":2}
     * items : [{"id":178,"channel_id":"9896587163798189245","live_title":"咣当。","hls":"http://2184.liveplay.myqcloud.com/live/2184_43aa93fb5d7911e791eae435c87f075e.m3u8","rtmp":"rtmp://2184.liveplay.myqcloud.com/live/2184_43aa93fb5d7911e791eae435c87f075e","flv":"http://2184.liveplay.myqcloud.com/live/2184_43aa93fb5d7911e791eae435c87f075e.flv","speaker":"咚咚五","start_time":1498795200,"end_time":1498824000,"real_start_time":1498793397,"real_end_time":1498825802,"description":"<p><span style=\"font-weight: bold; font-style: italic; text-decoration-line: underline; background-color: yellow;\">1111111111<\/span><\/p>","picture":"http://192.168.3.86/group1/M00/00/1F/wKgDV1lVxYWAVKspAAj5KLE8Hxc614.jpg","chatroom_group_id":"a104be3ab55d","live_status":2,"is_closed":0,"is_designated_courses":1,"is_notification":0,"is_playback":1,"user_id":6284297279592128512,"live_video_status":"stop","room_id":"879334997","is_video":0,"create_time":1498793350,"update_time":1498825802,"study_num":0}]
     */

    private int code;
    private String message;
    private PaginateBean paginate;
    private List<LiveBean> items;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public PaginateBean getPaginate() {
        return paginate;
    }

    public void setPaginate(PaginateBean paginate) {
        this.paginate = paginate;
    }

    public List<LiveBean> getItems() {
        return items;
    }

    public void setItems(List<LiveBean> items) {
        this.items = items;
    }

    public static class PaginateBean {
        /**
         * currentPage : 1
         * pageSize : 1
         * pageNum : 2
         * totalNum : 2
         */

        private int currentPage;
        private int pageSize;
        private int pageNum;
        private int totalNum;

        public int getCurrentPage() {
            return currentPage;
        }

        public void setCurrentPage(int currentPage) {
            this.currentPage = currentPage;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getPageNum() {
            return pageNum;
        }

        public void setPageNum(int pageNum) {
            this.pageNum = pageNum;
        }

        public int getTotalNum() {
            return totalNum;
        }

        public void setTotalNum(int totalNum) {
            this.totalNum = totalNum;
        }
    }
}

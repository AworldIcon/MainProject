package com.coder.kzxt.course.beans;

import com.coder.kzxt.base.beans.LiveBean;

/**
 * Created by Administrator on 2017/4/24.
 */

public class LiveSynopspsBean {


    /**
     * code : 200
     * message : ok
     * item : {"id":177,"channel_id":"","live_title":"4325342","hls":"","rtmp":"","flv":"","speaker":"654324","start_time":1492998300,"end_time":1493027100,"real_start_time":0,"real_end_time":0,"description":"<p>2354325432543253454325432<\/p>","picture":"http://html.gxy.com/uploads/live_cover/d39197788ddc296b172b5aa2592df840.jpeg","chatroom_group_id":"ef8db5f3ba5e","live_status":1,"is_designated_courses":1,"is_notification":0,"is_playback":1,"user_id":916510,"live_video_status":"ready","room_id":"299820120","is_video":0,"create_time":1492998201,"update_time":1493003716,"delete_time":null,"user_profile":{"nickname":"姜浩然_老师","avatar":"http://html.gxy.com/www/theme/common/default/images/avatar/boy.png","desc":""},"video_list":[]}
     */

    private int code;
    private String message;
    private ItemBean item;

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

    public ItemBean getItem() {
        return item;
    }

    public void setItem(ItemBean item) {
        this.item = item;
    }

    public static class ItemBean extends LiveBean{
    }
}

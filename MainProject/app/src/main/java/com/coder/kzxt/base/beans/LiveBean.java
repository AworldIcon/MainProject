package com.coder.kzxt.base.beans;

import java.io.Serializable;

/**
 * Created by MaShiZhao on 2017/4/13
 * 直播 对象
 */

public class LiveBean implements Serializable
{


    /**
     * id : 直播ID
     * live_title : 直播主题
     * hls : HLS 直播视频接收流
     * rtmp : HLS RTMP  直播视频接收流
     * flv : HLS FLV 直播视频接收流
     * speaker : 主讲人
     * start_time : 直播开始时间
     * end_time : 直播结束时间
     * description : 直播介绍',
     * picture : 直播封面图片
     * live_status : 直播状态 0=>未开始 1=>直播中 2=>已结束 3=>已关闭
     * is_designated_courses : 发送对象(1=>指定课程,0=>全网开放)
     * is_notification : 是否发送通知(1=>是,0=>否)
     * is_playback : 是否回放(1=>是,0=>否)
     * room_id : 房间号
     * live_video_status : 直播视频状态(未开始=>''ready''   直播中=>''playing''  切换=>switch  停止=>’stop‘)
     * is_video : 是否录播视频（0=>直播流  1=>录播视频）
     * user_id : 创建人id
     * create_time : 创建时间
     * update_time : 更新时间
     * user_profile : 创建用户信息([nickname] => 马化_学生 [avatar] => http://html.gxy.com/www/theme/common/default/images/avatar/girl.png[desc] => )
     */


    private String id;
    private String live_title;
//    private String hls;
//    private String rtmp;
//    private String flv;
//    private String speaker;
    private String start_time;
    private String end_time;
    private String description;
    private String picture;
    private int live_status;
//    private String is_designated_courses;
//    private String is_notification;
//    private String is_playback;
    private String room_id;
//    private String live_video_status;
//    private String is_video;
    private String user_id;
    private String create_time;
//    private String update_time;
    private String chatroom_group_id;
    private String study_num;
//    private String studentNum;

    private UserProfile user_profile;

    public String getStudy_num()
    {
        return study_num;
    }

    public void setStudy_num(String study_num)
    {
        this.study_num = study_num;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getLive_title()
    {
        return live_title;
    }

    public void setLive_title(String live_title)
    {
        this.live_title = live_title;
    }

//    public String getHls()
//    {
//        return hls;
//    }
//
//    public void setHls(String hls)
//    {
//        this.hls = hls;
//    }
//
//    public String getRtmp()
//    {
//        return rtmp;
//    }
//
//    public void setRtmp(String rtmp)
//    {
//        this.rtmp = rtmp;
//    }
//
//    public String getFlv()
//    {
//        return flv;
//    }
//
//    public void setFlv(String flv)
//    {
//        this.flv = flv;
//    }

//    public String getSpeaker()
//    {
//        return speaker;
//    }
//
//    public void setSpeaker(String speaker)
//    {
//        this.speaker = speaker;
//    }

    public String getStart_time()
    {
        return start_time;
    }

    public void setStart_time(String start_time)
    {
        this.start_time = start_time;
    }

    public String getEnd_time()
    {
        return end_time;
    }

    public void setEnd_time(String end_time)
    {
        this.end_time = end_time;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getPicture()
    {
        return picture;
    }

//    public String getStudentNum()
//    {
//        return studentNum;
//    }
//
//    public void setStudentNum(String studentNum)
//    {
//        this.studentNum = studentNum;
//    }

    public void setPicture(String picture)
    {
        this.picture = picture;
    }

    public int getLive_status()
    {
        return live_status;
    }

    public void setLive_status(int live_status)
    {
        this.live_status = live_status;
    }

//    public String getIs_designated_courses()
//    {
//        return is_designated_courses;
//    }
//
//    public void setIs_designated_courses(String is_designated_courses)
//    {
//        this.is_designated_courses = is_designated_courses;
//    }

//    public String getIs_notification()
//    {
//        return is_notification;
//    }
//
//    public void setIs_notification(String is_notification)
//    {
//        this.is_notification = is_notification;
//    }

//    public String getIs_playback()
//    {
//        return is_playback;
//    }
//
//    public void setIs_playback(String is_playback)
//    {
//        this.is_playback = is_playback;
//    }

    public String getRoom_id()
    {
        return room_id;
    }

    public void setRoom_id(String room_id)
    {
        this.room_id = room_id;
    }

//    public String getLive_video_status()
//    {
//        return live_video_status;
//    }
//
//    public void setLive_video_status(String live_video_status)
//    {
//        this.live_video_status = live_video_status;
//    }

//    public String getIs_video()
//    {
//        return is_video;
//    }
//
//    public void setIs_video(String is_video)
//    {
//        this.is_video = is_video;
//    }

    public String getUser_id()
    {
        return user_id;
    }

    public void setUser_id(String user_id)
    {
        this.user_id = user_id;
    }

    public String getCreate_time()
    {
        return create_time;
    }

    public void setCreate_time(String create_time)
    {
        this.create_time = create_time;
    }

//    public String getUpdate_time()
//    {
//        return update_time;
//    }
//
//    public void setUpdate_time(String update_time)
//    {
//        this.update_time = update_time;
//    }

    public UserProfile getUser_profile()
    {
        if (user_profile == null)
        {
            return new UserProfile();
        }
        return user_profile;
    }

    public void setUser_profile(UserProfile user_profile)
    {
        this.user_profile = user_profile;
    }

    public String getChatroom_group_id()
    {
        return chatroom_group_id;
    }

    public void setChatroom_group_id(String chatroom_group_id)
    {
        this.chatroom_group_id = chatroom_group_id;
    }
    
}

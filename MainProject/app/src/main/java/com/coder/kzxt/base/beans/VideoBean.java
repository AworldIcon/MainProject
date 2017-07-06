package com.coder.kzxt.base.beans;

import java.io.Serializable;

/**
 * Created by MaShiZhao on 2017/4/13
 * 视频 对象
 */

public class VideoBean implements Serializable
{

    /**
     * id : ID
     * title : 视频名称
     * video_id : 视频id
     * video_detail : 视频详情
     * video_size : 视频时长
     * type : 视频类型 （1.录播 2.直播回放)
     * user_id : 创建者id
     * create_time : 创建时间
     * update_time : 更新时间
     */

    private String id;
    private String title;
    private String video_id;
    private String live_id;
    private VideoDetailBean video_detail;
    private String video_size;
    private String type;
    private String user_id;
    private String create_time;
    private String update_time;
    private UserProfile user_profile;


    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getVideo_id()
    {
        return video_id;
    }

    public void setVideo_id(String video_id)
    {
        this.video_id = video_id;
    }

    public VideoDetailBean getVideo_detail()
    {
        if (video_detail == null)
        {
            return new VideoDetailBean();
        }
        return video_detail;
    }

    public String getLive_id() {
        return live_id;
    }

    public void setLive_id(String live_id) {
        this.live_id = live_id;
    }

    public void setVideo_detail(VideoDetailBean video_detail)
    {
        this.video_detail = video_detail;
    }

    public String getVideo_size()
    {
        return video_size;
    }

    public void setVideo_size(String video_size)
    {
        this.video_size = video_size;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

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

    public String getUpdate_time()
    {
        return update_time;
    }

    public void setUpdate_time(String update_time)
    {
        this.update_time = update_time;
    }

    public class VideoDetailBean implements Serializable
    {
        private String first_image;

        public String getFirst_image()
        {
            return first_image;
        }

        public void setFirst_image(String first_image)
        {
            this.first_image = first_image;
        }
    }

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
}

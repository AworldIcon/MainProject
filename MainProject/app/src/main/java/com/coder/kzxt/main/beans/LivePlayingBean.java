package com.coder.kzxt.main.beans;

import com.coder.kzxt.base.beans.LiveBean;

import java.util.List;

/**
 * Created by Administrator on 2017/4/11
 */

public class LivePlayingBean
{

    // live notice review
    private String type;
    private List<LiveBean> liveCourseBean;
    private List<LiveBean> videoBean;
    private int allNumbers;

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public List<LiveBean> getLiveCourseBean()
    {
        return liveCourseBean;
    }

    public void setLiveCourseBean(List<LiveBean> liveCourseBean)
    {
        this.liveCourseBean = liveCourseBean;
    }

    public List<LiveBean> getVideoBean()
    {
        return videoBean;
    }

    public void setVideoBean(List<LiveBean> videoBean)
    {
        this.videoBean = videoBean;
    }

    public int getAllNumbers()
    {
        return allNumbers;
    }

    public void setAllNumbers(int allNumbers)
    {
        this.allNumbers = allNumbers;
    }
}

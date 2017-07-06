package com.coder.kzxt.im.beans;

import com.tencent.TIMUserProfile;

/**
 * Created by MaShiZhao on 2017/5/5
 */

public class GroupManagerInfo
{
    private Boolean silenceSeconds;
    private TIMUserProfile tIMUserProfile;

    public Boolean getSilenceSeconds()
    {
        return silenceSeconds;
    }

    public void setSilenceSeconds(Boolean silenceSeconds)
    {
        this.silenceSeconds = silenceSeconds;
    }

    public TIMUserProfile gettIMUserProfile()
    {
        return tIMUserProfile;
    }

    public void settIMUserProfile(TIMUserProfile tIMUserProfile)
    {
        this.tIMUserProfile = tIMUserProfile;
    }
}

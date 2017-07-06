package com.coder.kzxt.base.beans;

import java.io.Serializable;

/**
 * Created by MaShiZhao on 2017/4/14
 * 腾讯im的信息 服务器返回
 */

public class UserProfile implements Serializable
{

    /**
     * nickname : 你叫什么
     * avatar : http://html.gxy.com/www/theme/common/default/images/avatar/girl.png
     * desc : dffffffffx11111111111115466498899468956594345394389138943094384138493094389437943894389138913794686
     */

    private String nickname;
    private String avatar;
    private String desc;
    private String about, realName;//中心站数据 about = desc realName = nickname

    public String getNickname()
    {

        if (nickname == null)
        {
            return realName == null ? "" : realName;
        }
        return nickname;
    }

    public void setNickname(String nickname)
    {
        this.nickname = nickname;
    }

    public String getAvatar()
    {
        return avatar;
    }

    public void setAvatar(String avatar)
    {
        this.avatar = avatar;
    }

    public String getDesc()
    {
        if (desc == null) return about== null ? "" : about;
        return desc;
    }

    public void setDesc(String desc)
    {
        this.desc = desc;
    }

    public void setAbout(String about)
    {
        this.about = about;
    }

    public void setRealName(String realName)
    {
        this.realName = realName;
    }
}

package com.coder.kzxt.message.beans;

import com.tencent.TIMGroupMemberRoleType;

/**
 * Created by MaShiZhao on 2017/5/6
 */

public class ManagerUserProfile
{
    private Boolean isShutUp;
    private TIMGroupMemberRoleType roleType;

    public Boolean getShutUp()
    {
        return isShutUp;
    }

    public void setShutUp(Boolean shutUp)
    {
        isShutUp = shutUp;
    }

    public TIMGroupMemberRoleType getRoleType()
    {
        return roleType;
    }

    public void setRoleType(TIMGroupMemberRoleType roleType)
    {
        this.roleType = roleType;
    }
}

package com.coder.kzxt.base.beans;

import com.tencent.TIMUser;

/**
 * Created by MaShiZhao on 2017/3/28
 * im用到的用户信息
 */

public class ImInfoBean
{


    /**
     * sdk_app_id : sdk_app_id
     * account_type : account_type
     * identifier : identifier
     * user_sig : user_sig
     */

    private String sdk_app_id;
    private String account_type;
    private String identifier;
    private String user_sig;
    private TIMUser timUser;

    public String getSdk_app_id()
    {
        return sdk_app_id;
    }

    public void setSdk_app_id(String sdk_app_id)
    {
        this.sdk_app_id = sdk_app_id;
    }

    public String getAccount_type()
    {
        return account_type;
    }

    public void setAccount_type(String account_type)
    {
        this.account_type = account_type;
    }

    public String getIdentifier()
    {
        return identifier;
    }

    public void setIdentifier(String identifier)
    {
        this.identifier = identifier;
    }

    public String getUser_sig()
    {
        return user_sig;
    }

    public void setUser_sig(String user_sig)
    {
        this.user_sig = user_sig;
    }

    public TIMUser getTimUser()
    {
        if (timUser == null)
        {
            timUser = new TIMUser();
            timUser.setIdentifier(identifier);
            timUser.setAccountType(account_type);
            timUser.setAppIdAt3rd(sdk_app_id);
        }
        return timUser;
    }
}

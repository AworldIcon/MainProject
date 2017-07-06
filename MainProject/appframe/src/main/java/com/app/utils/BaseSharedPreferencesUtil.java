package com.app.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2017/3/6.
 */

public class BaseSharedPreferencesUtil {

    protected Context context;
    protected SharedPreferences userSp;
    protected SharedPreferences.Editor userEditor;
    protected SharedPreferences sp;
    protected SharedPreferences.Editor editor;

    public BaseSharedPreferencesUtil(Context context) {
        this.context = context;
        if(this.context!=null){
            userSp = this.context.getSharedPreferences(BaseConstants.USERS_SP, Context.MODE_PRIVATE);
            userEditor = userSp.edit();
            sp = this.context.getSharedPreferences(BaseConstants.DEVICEDID, Context.MODE_PRIVATE);
            editor = sp.edit();
        }
    }


    /**
     * 获取用户uid
     * @return
     */
    public String getUid() {
        return userSp.getString(BaseConstants.MID, "");
    }

    /**
     * 保存用户uid
     * @param uid
     */
    public void setUid(String uid) {
        userEditor.putString(BaseConstants.MID, uid);
        userEditor.commit();
    }


    /**
     * 获取token
     * @return
     */
    public String getToken() {
        return userSp.getString(BaseConstants.OAUTH_TOKEN, "");
    }

    /**
     * 保存token
     * @param token
     */
    public void setToken(String token) {
        userEditor.putString(BaseConstants.OAUTH_TOKEN, token);
        userEditor.commit();
    }

    /**
     * 登录状态
     * @return
     */
    public String getIsLogin() {
        return userSp.getString(BaseConstants.IS_LOGIN, "");
    }

    /**
     * 保存登录状态
     * @param token
     */
    public void setIsLogin(String token) {
        userEditor.putString(BaseConstants.IS_LOGIN, token);
        userEditor.commit();
    }


    /**
     * 获取token secret
     * @return
     */
    public String getTokenSecret() {
        return userSp.getString(BaseConstants.OAUTH_TOKEN_SECRET, "");
    }

    /**
     * 保存token secret
     * @param tokenSecret
     */
    public void setTokenSecret(String tokenSecret) {
        userEditor.putString(BaseConstants.OAUTH_TOKEN_SECRET, tokenSecret);
        userEditor.commit();
    }

    /**
     * 获取deviceID
     * @return
     */
    public String getDevicedId(){
        return sp.getString(BaseConstants.DEVICEID,"");
    }

    /**
     * 保存devicedId
     * @param devicedId
     */
    public void setDevicedId(String devicedId){
        editor.putString(BaseConstants.DEVICEID,devicedId);
        editor.commit();
    }
/**
     * 获取版本号
     * @return
     */
    public String getVersionName(){
        return sp.getString(BaseConstants.VERSION_NAME,"");
    }

    /**
     * 保存版本号
     * @param devicedId
     */
    public void setVersionName(String devicedId){
        editor.putString(BaseConstants.VERSION_NAME,devicedId);
        editor.commit();
    }

}

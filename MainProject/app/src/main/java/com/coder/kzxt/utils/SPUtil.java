package com.coder.kzxt.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreferences工具类
 */
public class SPUtil {

    private Context context;
    private SharedPreferences sp;
    private SharedPreferences userSp;
    private SharedPreferences.Editor editor;
    private SharedPreferences.Editor userEditor;

    public SPUtil(Context context) {
        this.context = context;
        if(this.context!=null){
            sp = this.context.getSharedPreferences(Constants.CUURRENT_SP, Context.MODE_PRIVATE);
            editor = sp.edit();
            userSp = this.context.getSharedPreferences(Constants.USERS_SP, Context.MODE_PRIVATE);
            userEditor = userSp.edit();
        }
    }
    public String getIsLogin() {
        return userSp.getString(Constants.ISLOGIN, "");
    }

    public void setIsLogin(String flag) {
        userEditor.putString(Constants.ISLOGIN, flag);
        userEditor.commit();
    }

}

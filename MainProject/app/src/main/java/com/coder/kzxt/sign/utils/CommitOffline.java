package com.coder.kzxt.sign.utils;

import android.content.Context;

import com.app.http.HttpCallBack;
import com.app.http.UrlsNew;
import com.app.http.builders.HttpPostBuilder;
import com.coder.kzxt.base.beans.BaseBean;
import com.coder.kzxt.sign.beans.SignBean;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.google.gson.Gson;

/**
 * Created by MaShiZhao on 2017/6/2
 */

public class CommitOffline
{
    private SharedPreferencesUtil sharedPreferencesUtil;

    public CommitOffline(Context context)
    {

        if (!NetworkUtil.isNetworkAvailable(context)) return;

        sharedPreferencesUtil = new SharedPreferencesUtil(context);

        String offline = sharedPreferencesUtil.getSignOffline();
        if (!offline.equals(""))
        {
            SignBean bean = new Gson().fromJson(offline, SignBean.class);

            new HttpPostBuilder(context)
                    .setHttpResult(new HttpCallBack()
                    {
                        @Override
                        public void setOnSuccessCallback(int requestCode, Object resultBean)
                        {
                            sharedPreferencesUtil.setSignOffline("");
                        }

                        @Override
                        public void setOnErrorCallback(int requestCode, int code, String msg)
                        {

                        }
                    })
                    .setUrl(UrlsNew.SIGN_STUDENT)
                    .setClassObj(BaseBean.class)
                    .addBodyParam("course_id", bean.getCourse_id())
                    .addBodyParam("class_id", bean.getClass_id())
                    .addBodyParam("sign_id", bean.getId())
                    .addBodyParam("type", "0")
                    .addBodyParam("latitude", bean.getLatitude())
                    .addBodyParam("longitude", bean.getLatitude())
                    .addBodyParam("sign_time", bean.getSign_time() + "")
                    .addBodyParam("is_offline", "1")
                    .addBodyParam("status", "1")
                    .build();

        }
    }

}

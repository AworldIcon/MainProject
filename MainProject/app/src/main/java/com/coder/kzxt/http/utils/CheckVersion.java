package com.coder.kzxt.http.utils;

import android.content.Context;

import com.app.http.HttpCallBack;
import com.app.http.builders.HttpGetBuilder;
import com.coder.kzxt.setting.beans.CheckVersionBean;
import com.app.http.UrlsNew;

/**
 * 检查版本共用接口
 */

public class CheckVersion{

    private Context context;

    public CheckVersion(Context context, HttpCallBack interfaceHttpResult) {
        this.context = context;
        //new HttpGetOld(context,context,interfaceHttpResult, CheckVersionBean.class, Urls.NEW_VERSION,"4").excute();
        new HttpGetBuilder(context).setClassObj(CheckVersionBean.class)
                .setUrl(UrlsNew.NEW_VERSION)
                .addQueryParams("type","3")
                .setHttpResult(interfaceHttpResult)
                .build();
    }

}
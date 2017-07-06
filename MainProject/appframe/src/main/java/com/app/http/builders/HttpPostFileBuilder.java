package com.app.http.builders;

import android.content.Context;

import com.app.http.HttpUtils;

/**
 * Created by zw on 2017/4/25.
 */

public class HttpPostFileBuilder extends BaseBuilder{
    public HttpPostFileBuilder(Context context)
    {
        super(context);
    }

    public void build()
    {
        // 创建get实例
        HttpUtils httpGet = new HttpUtils(context, requestCode, interfaceHttpResult, classObj, HttpUtils.METHOD.POST, getFileNames(),getUrlString(), getBodyString(), getSignString());
        httpGet.execute();
    }

}

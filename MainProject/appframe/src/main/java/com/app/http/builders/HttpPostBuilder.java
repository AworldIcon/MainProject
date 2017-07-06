package com.app.http.builders;

import android.content.Context;

import com.app.http.HttpUtils;

/**
 * Created by MaShiZhao on 2017/1/6
 * httpPost 的 请求
 */
public class HttpPostBuilder extends BaseBuilder
{


    public HttpPostBuilder(Context context)
    {
        super(context);
    }

    public void build()
    {
        // 创建get实例
        HttpUtils httpGet = new HttpUtils(context, requestCode, interfaceHttpResult, classObj, HttpUtils.METHOD.POST, getUrlString(), getBodyString(), getSignString());
        httpGet.execute();
    }


}

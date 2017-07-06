package com.app.http.builders;

import android.content.Context;

import com.app.http.HttpUtils;

/**
 * Created by MaShiZhao on 2017/1/6
 * httpGet 的 请求
 */
public class HttpGetBuilder extends BaseBuilder
{


    public HttpGetBuilder(Context context)
    {
        super(context);
    }

    public void build()
    {
        // 创建post实例
        HttpUtils httpPost = new HttpUtils(context, requestCode, interfaceHttpResult, classObj, HttpUtils.METHOD.GET, getUrlString(), getBodyString(), getSignString());
        httpPost.execute();
    }


}

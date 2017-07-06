package com.app.http.builders;

import android.content.Context;

import com.app.http.HttpUtils;

/**
 * Created by MaShiZhao on 2017/1/6
 * httpPut 的请求 用于更新数据
 */
public class HttpPutBuilder extends BaseBuilder
{


    public HttpPutBuilder(Context context)
    {
        super(context);
    }

    public void build()
    {
        // 创建put实例
        HttpUtils httpPut = new HttpUtils(context, requestCode, interfaceHttpResult, classObj, HttpUtils.METHOD.PUT, getUrlString(), getBodyString(), getSignString());
        httpPut.execute();
    }


}

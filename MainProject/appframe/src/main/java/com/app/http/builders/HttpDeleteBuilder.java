package com.app.http.builders;

import android.content.Context;

import com.app.http.HttpUtils;

/**
 * Created by MaShiZhao on 2017/1/6
 * http delete 的请求 用于更新数据
 */
public class HttpDeleteBuilder extends BaseBuilder
{


    public HttpDeleteBuilder(Context context)
    {
        super(context);
    }

    public void build()
    {
        // 创建put实例
        HttpUtils httpDelete = new HttpUtils(context, requestCode, interfaceHttpResult, classObj, HttpUtils.METHOD.DELETE, getUrlString(), getBodyString(), getSignString());
        httpDelete.execute();
    }


}

package com.coder.kzxt.http.utils;

import android.content.Context;

import com.app.http.HttpCallBack;
import com.app.http.UrlsNew;
import com.app.http.builders.HttpDeleteBuilder;
import com.app.utils.L;

/**
 * 取消海报点赞
 */
public class CancelPraise {

    public CancelPraise(Context context, String uid, String pid) {
        new HttpDeleteBuilder(context).addBodyParam("uid",uid)
                .addBodyParam("pid",pid)
                .addBodyParam("del","1")
                .setUrl(UrlsNew.DELETE_POSTERLIKE_BATCH)
                .setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
                        L.v("tangcy","取消成功");
                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {
                        L.v("tangcy","取消失败");
                    }
                })
                .build();
       }
}

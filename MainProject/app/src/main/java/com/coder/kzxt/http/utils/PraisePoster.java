package com.coder.kzxt.http.utils;

import android.content.Context;

import com.app.http.HttpCallBack;
import com.app.http.UrlsNew;
import com.app.http.builders.HttpPostBuilder;
import com.app.utils.L;

/**
 * 海报公用点赞接口
 */

public class PraisePoster {

    public PraisePoster(Context context,String uid,String pid) {
        new HttpPostBuilder(context).addBodyParam("uid",uid)
                .addBodyParam("pid",pid)
                .setUrl(UrlsNew.POST_POSETER_POSTERLIKE)
                .setHttpResult(new HttpCallBack() {
            @Override
            public void setOnSuccessCallback(int requestCode, Object resultBean) {
                L.v("tangcy","点赞成功");
            }

            @Override
            public void setOnErrorCallback(int requestCode, int code, String msg) {
                L.v("tangcy","点赞失败");
            }
         })
        .build();
    }
}

package com.coder.kzxt.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;

/**
 * Created by Administrator on 2017/3/8.
 */

public class PublicUtils
{

    public static int getVersionCodetwo(Context context)
    {// 获取版本号(内部识别号)
        try
        {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionCode;
        } catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
            return 0;
        }
    }


    public static boolean IsEmpty(String string) {
        if (TextUtils.isEmpty(string) || string.equals("0") || string.equals("0.0") || string.equals("0.00")) {
            return true;
        }
        return false;
    }

    // 获取AppKey
    public static String getMetaValue(Context context, String metaKey) {
        Bundle metaData = null;
        String apiKey = null;
        if (context == null || metaKey == null) {
            return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                apiKey = metaData.getString(metaKey);
            }
        } catch (PackageManager.NameNotFoundException e) {

        }
        return apiKey;
    }
}

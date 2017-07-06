package com.coder.kzxt.utils;

import android.os.Build;

/**
 * Created by Administrator on 2017/3/6.
 */

public class SdkVerstion {
    //系统版本是否大于5.0
    public static boolean isAndroid5() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }
}

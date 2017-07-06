package com.coder.kzxt.sign.utils;

/**
 * Created by MaShiZhao on 2017/6/7
 */

public class SignUtils
{
    public static String getRangeString(int i)
    {
        if (i == 0) return "不限";
        return i + "米";
    }
}

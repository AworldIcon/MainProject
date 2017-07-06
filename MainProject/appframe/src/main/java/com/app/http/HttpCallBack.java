package com.app.http;

/**
 * Created by MaShiZhao on 2017/3/17
 */

public interface HttpCallBack
{
    /**
     * http 回调
     *
     * @param requestCode http关联的请求码
     * @param resultBean  强转类型的对象
     */
    public void setOnSuccessCallback(int requestCode, Object resultBean);

    /**
     * @param requestCode
     * @param code        失败错误码
     * @param msg         失败信息
     */
//    callBackString
    public void setOnErrorCallback(int requestCode, int code, String msg);
}

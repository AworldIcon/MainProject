package com.app.utils;

/**
 * Created by MaShiZhao on 2017/1/6
 */
public class BaseConstants
{
    //用户token
    public static final String TOKEN = "token";
    public static final String MID = "mid";
    public static final String TOKEN_SECRET = "token_secret";
    //用户信息
    public static final String KEY_USER_INFO = "user_info";
    //设备信息
    public static final String DEVICE_ID = "deviceId";
    //版本号
    public static final String APP_VERSION = "app_version";
    //平台
    public static final String PLATFORM = "2";
    //推送channel
    public static final String PUSH_CHANNELID = "push_channel_id";
    //http解密的key
    public static final String HTTP_KEY = "w2y0z1cx";

    // http 请求成功code
    public static final int HTTP_CODE_SUCCESS = 200;//解析成功
    public static final int HTTP_CODE_2001 = 1008;//请登录
    public static final int HTTP_CODE_2004 = 1020;//被抢登
    public static final int HTTP_CODE_8000 = 8000;//请重新登录
    public static final int HTTP_CODE_8303 = 8303;
    public static final int HTTP_CODE_5000 = 5000;//解析异常
    public static final int HTTP_CODE_4000 = 4000;//请求出错
    public static final int HTTP_CODE_6000 = 6000;//请求的参数出现问题


    //创建存储用户数据SharedPreferences标识
    public static final String USERS_SP = "user_info";
    //创建存储devicedId标识
    public static final String DEVICEDID = "devicedId";
    public static final String Mid = "mid";
    public static final String OAUTH_TOKEN = "oauth_token";
    public static final String IS_LOGIN = "is_login";
    public static final String OAUTH_TOKEN_SECRET = "oauth_token_secret";
    public static final String DEVICEID = "deviceId";
    public static final String VERSION_NAME = "version_name";
}

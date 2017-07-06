package com.coder.kzxt.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.login.activity.LoginActivity;
import com.coder.kzxt.base.fragment.BaseFragment;

/**
 * 检查网络工具类
 */
public class NetworkUtil
{

    /**
     * 判断网络连接是否可用
     *
     * @return
     */
    public static boolean isNetworkAvailable(Context context)
    {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected())
            {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED)
                {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * 是否是wifi网络
     */
    public static boolean isWifiNetwork(Context mContext)
    {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null)
        {
            return false;
        }
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI)
        {
            return true;
        }
        return false;
    }

    /**
     * 请求完接口失败后首先调用的方法
     * context activity
     * view 页面最外层布局
     */
    public static void httpNetErrTip(Context context, View view)
    {
        if (context == null)
        {
            return;
        }
//        if (!isNetworkAvailable(context) || !ping())
        if (!isNetworkAvailable(context))
        {
            //提示网络连接出错
            new SnackbarUtils(context).Long(view, context.getResources().getString(R.string.net_inAvailable))
                    .backColor(context.getResources().getColor(R.color.bg_red)).show();
        }
    }

    /**
     * 请求完接口 返回2001或2004 提示重新登录
     */
    public static void httpRestartLogin(final Activity context, View view)
    {
        if (context == null)
        {
            return;
        }
        //清空登录状态
//        new SharedPreferencesUtil(context).setIsLogin("");
        new SharedPreferencesUtil(context).clearUserInfo();

        new SnackbarUtils(context).Indefinite(view, context.getResources().getString(R.string.repeat_login))
                .setAction(context.getString(R.string.click_login), new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {

                        context.startActivityForResult(new Intent(context, LoginActivity.class), Constants.RESTART_LOGIN);

                        //如果是进入APP弹出登录的话，此处不应该finish，不然在登录页操作会直接退出
//                        if (!context.getClass().equals(MainActivity.class))
//                        {
//                            ((Activity) context).finish();
//                        }
                    }
                }).backColor(context.getResources().getColor(R.color.font_dark_black2)).show();
    }

    /**
     * 请求完接口 返回2001或2004 提示重新登录
     */
    public static void httpRestartLogin(final BaseFragment context, View view)
    {
        if (context == null)
        {
            return;
        }

        //清空登录状态
//        new SharedPreferencesUtil(context.getActivity()).setToken("");
        new SharedPreferencesUtil(context.getActivity()).clearUserInfo();

        new SnackbarUtils(context.getActivity()).Indefinite(view, context.getResources().getString(R.string.repeat_login))
                .setAction(context.getString(R.string.click_login), new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {

                        context.startActivityForResult(new Intent(context.getActivity(), LoginActivity.class), Constants.RESTART_LOGIN);

                        //如果是进入APP弹出登录的话，此处不应该finish，不然c在登录页操作会直接退出
//                        if (!context.getClass().equals(MainActivity.class))
//                        {
//                            ((Activity) context).finish();
//                        }
                    }
                }).backColor(context.getResources().getColor(R.color.font_dark_black2)).show();
    }


}

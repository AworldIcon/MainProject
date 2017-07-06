package com.coder.kzxt.utils;

import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 吐司工具类  升级版
 * 解决5.0以上系统如果禁用系统通知的话，原生的Toast会不显示的问题
 * 如果没禁用，则显示原生的Toast
 * Created by Administrator on 2017/2/7.
 */

public class ToastUtils
{
    private static final String CHECK_OP_NO_THROW = "checkOpNoThrow";
    private static final String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";
    private static int checkNotification = -1;
    private Object mToast;
    private boolean isShow = false;

    private ToastUtils(Context context, String message, int duration)
    {
        try
        {
            if (checkNotification == -1)
            {
                checkNotification = isNotificationEnabled(context) ? 0 : 1;
            }

            if (checkNotification == 1 && context instanceof Activity)
            {
                mToast = EToast.makeText(context, message, duration);
            } else
            {
                mToast = Toast.makeText(context, message, duration);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private ToastUtils(Context context, int resId, int duration)
    {
        if (checkNotification == -1)
        {
            checkNotification = isNotificationEnabled(context) ? 0 : 1;
        }

        if (checkNotification == 1 && context instanceof Activity)
        {
            mToast = EToast.makeText(context, resId, duration);
        } else
        {
            mToast = Toast.makeText(context, resId, duration);
        }
    }


    public static void makeText(Context context, String message)
    {
        new ToastUtils(context, message, Toast.LENGTH_SHORT).show();
    }

    public static ToastUtils makeText(Context context, String message, int duration)
    {
        return new ToastUtils(context, message, duration);
    }

    public static ToastUtils makeText(Context context, int resId, int duration)
    {
        return new ToastUtils(context, resId, duration);
    }

    public void show()
    {
        if (isShow)
        {
            return;
        }
        isShow = true;
        if (mToast instanceof EToast)
        {
            ((EToast) mToast).show();
        } else if (mToast instanceof android.widget.Toast)
        {
            ((Toast) mToast).show();
        }
    }

    public void cancel()
    {
        if (isShow)
        {
            isShow = false;
            if (mToast instanceof EToast)
            {
                ((EToast) mToast).cancel();
            } else if (mToast instanceof android.widget.Toast)
            {
                ((Toast) mToast).cancel();
            }
        }
    }

    public void setText(int resId)
    {
        if (mToast instanceof EToast)
        {
            ((EToast) mToast).setText(resId);
        } else if (mToast instanceof android.widget.Toast)
        {
            ((Toast) mToast).setText(resId);
        }
    }

    public void setText(CharSequence s)
    {
        if (mToast instanceof EToast)
        {
            ((EToast) mToast).setText(s);
        } else if (mToast instanceof android.widget.Toast)
        {
            ((Toast) mToast).setText(s);
        }
    }

    /**
     * 用来判断是否开启通知权限
     */
    private static boolean isNotificationEnabled(Context context)
    {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.KITKAT)
        {
            return true;
        }
        AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        ApplicationInfo appInfo = context.getApplicationInfo();

        String pkg = context.getApplicationContext().getPackageName();

        int uid = appInfo.uid;

        Class appOpsClass = null; /* Context.APP_OPS_MANAGER */

        try
        {

            appOpsClass = Class.forName(AppOpsManager.class.getName());

            Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE, String.class);

            Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);
            int value = (int) opPostNotificationValue.get(Integer.class);
            return ((int) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);

        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return true;
    }
}
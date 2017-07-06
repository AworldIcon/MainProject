package com.coder.kzxt.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.MapView;
import com.app.frame.ErrorHandler;
import com.app.http.BaseActivityLifecycleCallbacks;
import com.app.utils.L;
import com.baidu.mobstat.StatService;
import com.coder.kzxt.activity.R;
import com.com.tencent.qcloud.suixinbo.presenters.InitBusinessHelper;
import com.tencent.TIMManager;
import com.tencent.TIMOfflinePushListener;
import com.tencent.TIMOfflinePushNotification;
import com.tencent.qalsdk.sdk.MsfSdkUtils;
import com.tencent.qcloud.suixinbo.utils.SxbLogImpl;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

//MultiDexApplication
public class MyApplication extends Application
{

    private static MyApplication instance;
    public static MapView mapView;
    public static AMap aMap;
    //    private static ErrorHandler errorHandler;

    //    private static RefWatcher refWatcher;
    public static List<Activity> workList = new ArrayList<>();

    @Override
    public void onCreate()
    {
        super.onCreate();
        //初始化xutils3
        x.Ext.init(this);
//        refWatcher= LeakCanary.install(this);
        instance = this;
        L.isDebug = true;

        //高德地图
        mapView = new MapView(instance);
        aMap = mapView.getMap();

        //注册生命周期
        registerActivityLifecycleCallbacks(new BaseActivityLifecycleCallbacks());
        //初始化腾讯
        if (shouldInit()) {
            SxbLogImpl.init(getApplicationContext());
            //初始化APP
            InitBusinessHelper.initApp(instance);
            inItTencentSdk();
        }

        //初始化渠道包信息
        initChannel();
        //设置error的信息
//        initErrorHandler();

    }

    private void initErrorHandler()
    {
        ErrorHandler  errorHandler = ErrorHandler.getInstance();
        errorHandler.setToErrorHandler();
    }

    public synchronized static MyApplication getInstance()
    {
        if (null == instance)
        {
            instance = new MyApplication();
        }
        return instance;
    }

    private void initChannel()
    {
        //获取本apk的渠道号 默认是guanwang
//        source_channel = ;
//        MyApplication.packageName = instance.getPackageName();
        //如果需要打其他单个的渠道包 直接修改 setAPPChannel 即可,上面两行注释
        StatService.setAppChannel(this, ChannelUtil.getChannel(instance), true);
        StatService.setSessionTimeOut(30);
        StatService.setOn(MyApplication.getInstance(), StatService.EXCEPTION_LOG);
        StatService.setLogSenderDelayed(10);
    }
//    public static RefWatcher getRefWatcher() {
//        return refWatcher;
//    }


    //首次启动开启一个线程来加载classes2.dex，防止阻塞UI线程，非首次启动则同步执行。
    @Override
    protected void attachBaseContext(final Context base)
    {
        super.attachBaseContext(base);
        try
        {
            MultiDex.install(base);
        } catch (RuntimeException multiDexException)
        {
        }
    }

    /**
     * 设置整体工程textView不随系统字体大小而改变
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.fontScale != 1)//非默认值
            getResources();
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        if (res.getConfiguration().fontScale != 1) {//非默认值
            Configuration newConfig = new Configuration();
            newConfig.setToDefaults();//设置默认
            res.updateConfiguration(newConfig, res.getDisplayMetrics());
        }
        return res;
    }


    public void inItTencentSdk()
    {
        //初始化腾讯sdk
//        SxbLogImpl.init(getApplicationContext());
//        InitBusinessHelper.initApp(context);
        //腾讯im
        if (MsfSdkUtils.isMainProcess(this))
        {
//            TIMManager.getInstance().init(getApplicationContext());
//            TIMManager.getInstance().setLogLevel(TIMLogLevel.DEBUG);
            TIMManager.getInstance().setOfflinePushListener(new TIMOfflinePushListener()
            {
                @Override
                public void handleNotification(TIMOfflinePushNotification notification)
                {
                    Log.e("MyApplication", "recv offline push");
                    notification.doNotify(getApplicationContext(), R.mipmap.ic_launcher);
                }
            });
        }

    }


    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = android.os.Process.myPid();

        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 强制升级时调用的退出方法
     */
    public void exit()
    {
        android.os.Process.killProcess(android.os.Process.myPid());
    }


}

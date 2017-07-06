//package com.coder.kzxt.utils;
//
//import android.app.Activity;
//import android.app.Application;
//import android.content.Context;
//import android.os.Bundle;
//import android.os.Handler;
//import android.support.multidex.MultiDex;
//import android.util.Log;
//
//import com.amap.api.maps2d.AMap;
//import com.amap.api.maps2d.MapView;
//import com.app.frame.ErrorHandler;
//import com.app.utils.L;
//import com.baidu.mobstat.StatService;
//import com.coder.kzxt.activity.R;
////import com.squareup.leakcanary.LeakCanary;
////import com.squareup.leakcanary.RefWatcher;
//import com.tencent.TIMManager;
//import com.tencent.TIMOfflinePushListener;
//import com.tencent.TIMOfflinePushNotification;
//import com.tencent.qalsdk.sdk.MsfSdkUtils;
//
//import org.xutils.x;
//
//import java.util.ArrayList;
//import java.util.List;
//
////import com.baidu.mobstat.StatService;
////import com.tencent.qcloud.suixinbo.presenters.InitBusinessHelper;
////import com.tencent.qcloud.suixinbo.utils.SxbLogImpl;
//
////import com.squareup.leakcanary.LeakCanary;
////import com.squareup.leakcanary.RefWatcher;
//
//
//public class UILApplication extends Application
//{
//
//    private static UILApplication instance;
//    public static String source_channel = "";
//    public static String packageName = "";
//    public static MapView mapView;
//    public static AMap aMap;
//    private Handler handler;
//    private static Context context;
//    public static List<Activity> list;
//    private static ErrorHandler errorHandler;
////    private static RefWatcher refWatcher;
//    @Override
//    public void onCreate()
//    {
//        super.onCreate();
//        //初始化xutils3
//        x.Ext.init(this);
////        refWatcher= LeakCanary.install(this);
//        list = new ArrayList<>();
//        instance = this;
//        context = getApplicationContext();
//        L.isDebug = true;
//
//        errorHandler = ErrorHandler.getInstance();
////        UILApplication.errorHandler.setToErrorHandler();
//
//
//        mapView = new MapView(instance);
//        aMap = mapView.getMap();
//
//        inItTencentSdk();
//        //获取本apk的渠道号 默认是guanwang
//        UILApplication.source_channel = ChannelUtil.getChannel(instance);
//        UILApplication.packageName = instance.getPackageName();
//        //如果需要打其他单个的渠道包 直接修改 setAPPChannel 即可,上面两行注释
//        StatService.setAppChannel(this, UILApplication.source_channel, true);
//        StatService.setSessionTimeOut(30);
//        StatService.setOn(UILApplication.getInstance(), StatService.EXCEPTION_LOG);
//        StatService.setLogSenderDelayed(10);
//        //获取当前activity的回调
//        registerAcitvityLifeCycleCallBacks();
//
//        //腾讯im
//        if (MsfSdkUtils.isMainProcess(this))
//        {
//            TIMManager.getInstance().setOfflinePushListener(new TIMOfflinePushListener()
//            {
//                @Override
//                public void handleNotification(TIMOfflinePushNotification notification)
//                {
//                    Log.e("MyApplication", "recv offline push");
//                    notification.doNotify(getApplicationContext(), R.mipmap.ic_launcher);
//                }
//            });
//        }
//
//
//    }
////    public static RefWatcher getRefWatcher() {
////        return refWatcher;
////    }
//
//    //首次启动开启一个线程来加载classes2.dex，防止阻塞UI线程，非首次启动则同步执行。
//    @Override
//    protected void attachBaseContext(final Context base)
//    {
//        super.attachBaseContext(base);
//        try
//        {
//            MultiDex.install(base);
//        } catch (RuntimeException multiDexException)
//        {
//        }
//
//
//    }
//
//    public synchronized static UILApplication getInstance()
//    {
//        if (null == instance)
//        {
//            instance = new UILApplication();
//        }
//        return instance;
//    }
//
//
//    public void inItTencentSdk()
//    {
//        //初始化腾讯sdk
////        SxbLogImpl.init(getApplicationContext());
////        InitBusinessHelper.initApp(context);
//    }
//
//    public Handler getHandler()
//    {
//        return handler;
//    }
//
//    public void setHandler(Handler handler)
//    {
//        this.handler = handler;
//    }
//
//    public static Context getContext()
//    {
//        return context;
//    }
//
//    /**
//     * 强制升级时调用的退出方法
//     */
//    public void exit()
//    {
//        android.os.Process.killProcess(android.os.Process.myPid());
//    }
//
//
//    private void registerAcitvityLifeCycleCallBacks()
//    {
//        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks()
//        {
//            @Override
//            public void onActivityCreated(Activity activity, Bundle savedInstanceState)
//            {
//
//            }
//
//            @Override
//            public void onActivityStarted(Activity activity)
//            {
//
//            }
//
//            @Override
//            public void onActivityResumed(Activity activity)
//            {
//                MyActivityManager.getInstance().setCurrentActivity(activity);
//            }
//
//            @Override
//            public void onActivityPaused(Activity activity)
//            {
//
//            }
//
//            @Override
//            public void onActivityStopped(Activity activity)
//            {
//
//            }
//
//            @Override
//            public void onActivitySaveInstanceState(Activity activity, Bundle outState)
//            {
//
//            }
//
//            @Override
//            public void onActivityDestroyed(Activity activity)
//            {
//
//            }
//        });
//    }
//}

package com.coder.kzxt.download;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import com.coder.kzxt.db.DataBaseDao;
import com.coder.kzxt.utils.Constants;

import org.xutils.common.Callback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DownloadService extends Service {

    private static DownloadManager DOWNLOAD_MANAGER;

    public static DownloadManager getDownloadManager(Context appContext) {
        if (!DownloadService.isServiceRunning(appContext)) {
            Intent downloadSvr = new Intent();
            downloadSvr.setAction(Constants.DOWN_SERVICE_FITER);//你定义的service的action
            downloadSvr.setPackage(appContext.getPackageName());//这里你需要设置你应用的包名  
            appContext.startService(downloadSvr);
        }
        if (DownloadService.DOWNLOAD_MANAGER == null) {
            DownloadService.DOWNLOAD_MANAGER = new DownloadManager(appContext);
        }
        return DOWNLOAD_MANAGER;
    }


    /**
     * 后台自动下载下一个视频?
     *
     * @param courseId 课程id
     */
    public static void backstage_Download_Next_Video(Context context, String courseId, String serviceId) {

        ArrayList<String> query_All_DownStatus = DataBaseDao.getInstance(context).query_All_DownStatus();
        //正在下载的不做处理
        if (query_All_DownStatus.contains("1")) {

        } else if (!query_All_DownStatus.contains("0")) {
            //停止服务
            DownloadService.stopService(context);
        } else {
            //
            if (!TextUtils.isEmpty(courseId)) {
                dowloadCourseVideo(context, courseId);
            } else if (!TextUtils.isEmpty(serviceId))  {
                dowloadServiceVideo(context, serviceId);
            }
        }

    }
    private static void dowloadCourseVideo(Context context, String courseId) {
        Log.v("tangcy", "后台service自动下载等待的视�?");
        //自动下载本课程等待的视频
        ArrayList<HashMap<String, String>> query_Wait_Download = DataBaseDao.getInstance(context).query_Wait_Download(courseId);
        if (query_Wait_Download.size() != 0) {
            HashMap<String, String> hashMap2 = query_Wait_Download.get(0);
            DataBaseDao.getInstance(context).updata_DownStatus(hashMap2.get("id"), 1);

            DOWNLOAD_MANAGER.addCourseVideoDownload(hashMap2.get("url_content"), hashMap2.get("treeid"), hashMap2.get("tid")
                    , hashMap2.get("id"), Integer.parseInt(hashMap2.get("mapKey"))
                    , hashMap2.get("name"), Long.parseLong(hashMap2.get("downloadedSize")),hashMap2.get("video_type"));

            Intent intent_refresh = new Intent();
            intent_refresh.setAction(Constants.MY_DOWNLOAD_REFRESH);
            intent_refresh.putExtra("id", hashMap2.get("id"));
            context.sendBroadcast(intent_refresh);

        } else {
            //自动下载其他课程等待的视�?
            ArrayList<HashMap<String, String>> allwaitList = DataBaseDao.getInstance(context).queryAllWaitCourseVideos();
            if (allwaitList.size() != 0) {
                HashMap<String, String> hashMap2 = allwaitList.get(0);
                DataBaseDao.getInstance(context).updata_DownStatus(hashMap2.get("id"), 1);

                DOWNLOAD_MANAGER.addCourseVideoDownload(hashMap2.get("url_content"), hashMap2.get("treeid"), hashMap2.get("tid")
                        , hashMap2.get("id"), Integer.parseInt(hashMap2.get("mapKey"))
                        , hashMap2.get("name"), Long.parseLong(hashMap2.get("downloadedSize")),hashMap2.get("video_type"));

                Intent intent_refresh = new Intent();
                intent_refresh.setAction(Constants.MY_DOWNLOAD_REFRESH);
                intent_refresh.putExtra("id", hashMap2.get("id"));
                context.sendBroadcast(intent_refresh);

            } else {
                //停止服务
                DownloadService.stopService(context);
            }
        }
    }


    private static void dowloadServiceVideo(Context context, String serviceId) {

        //自动下载其他课程等待的视�?
        ArrayList<HashMap<String, String>> allwaitList = DataBaseDao.getInstance(context).queryAllWaitServiceVideos(serviceId);
        if (allwaitList.size() != 0) {
            HashMap<String, String> hashMap2 = allwaitList.get(0);
            DataBaseDao.getInstance(context).updata_DownStatus(hashMap2.get("id"),serviceId, 1);

            DOWNLOAD_MANAGER.addServiceVideoDownload(hashMap2.get("url_content"), hashMap2.get("service_id"), hashMap2.get("id")
                    , hashMap2.get("id"), Integer.parseInt(hashMap2.get("mapKey"))
                    , hashMap2.get("name"), Long.parseLong(hashMap2.get("downloadedSize")),hashMap2.get("video_type"));

            Intent intent_refresh = new Intent();
            intent_refresh.setAction(Constants.MY_DOWNLOAD_REFRESH);
            intent_refresh.putExtra("id", hashMap2.get("id"));
            context.sendBroadcast(intent_refresh);

        } else {
            //停止服务
            DownloadService.stopService(context);
        }

    }



    public static void stopService(Context appContext) {
        Log.v("tangcy", "停止服务");
//    	boolean serviceRunning = DownloadService.isServiceRunning(appContext);
        if (DownloadService.isServiceRunning(appContext)) {
            Intent downloadSvr = new Intent();
            downloadSvr.setAction(Constants.DOWN_SERVICE_FITER);//你定义的service的action
            downloadSvr.setPackage(appContext.getPackageName());//这里你需要设置你应用的包名
            appContext.stopService(downloadSvr);

        }
    }


    public DownloadService() {
        super();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public void onDestroy() {
        if (DOWNLOAD_MANAGER != null) {
            ArrayList<Callback.Cancelable> cancelables = DOWNLOAD_MANAGER.getAllCacebles();
            if (cancelables.size() != 0) {
                for (int i = 0; i < cancelables.size(); i++) {
                    Callback.Cancelable cancelable = cancelables.get(i);
                    if (cancelable != null && !cancelable.isCancelled()) {
                        cancelable.cancel();
                    }
                }
            }

        }
        super.onDestroy();
    }

    public static boolean isServiceRunning(Context context) {
        boolean isRunning = false;

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(Integer.MAX_VALUE);

        if (serviceList == null || serviceList.size() == 0) {
            return false;
        }

        for (int i = 0; i < serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(DownloadService.class.getName())) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }
}

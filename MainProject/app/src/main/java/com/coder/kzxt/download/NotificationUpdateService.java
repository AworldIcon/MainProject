package com.coder.kzxt.download;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.RemoteViews;

import com.coder.kzxt.activity.R;
import com.coder.kzxt.main.activity.MainActivity;
import com.coder.kzxt.utils.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class NotificationUpdateService extends Service
{

    //状态栏通知管理类
    private NotificationManager mNotificationManager;
    //状态栏通知
    private Notification mNotification;
    //状态栏通知显示的view
    private RemoteViews remoteViews;
    //通知的id
    private final int notificationID = 1;
    private String apkUrl;
    private int progress;
    private int lastRate = 0;
    private boolean canceled;

    private Handler mHandler = new Handler()
    {

        @Override
        public void handleMessage(Message msg)
        {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what)
            {
                case 0:
                    // 下载完毕
                    installApk();
                    stopSelf();// 停掉服务自身

                    break;
                case 1:
                    int rate = msg.arg1;
                    if (rate < 100)
                    {
                        remoteViews.setTextViewText(R.id.tv_already_download, rate + "%");
                        remoteViews.setProgressBar(R.id.ProgressBarDown, 100, rate, false);
                        mNotification.contentView = remoteViews;
                        mNotificationManager.notify(notificationID, mNotification);
                    }

                    break;

                case 2:

                    mNotification.flags = Notification.FLAG_AUTO_CANCEL; // 点击后自动消除
                    remoteViews.setTextViewText(R.id.tv_already_download, "升级失败,点击取消");
                    mNotification.contentView = remoteViews;
                    Intent intent_noti = new Intent(NotificationUpdateService.this, MainActivity.class);
                    intent_noti.putExtra("from", "update");
                    intent_noti.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    PendingIntent contentIntent = PendingIntent.getActivity(NotificationUpdateService.this, 0, intent_noti, PendingIntent.FLAG_UPDATE_CURRENT);
                    // 指定内容意图 e
                    mNotification.contentIntent = contentIntent;
                    mNotificationManager.notify(notificationID, mNotification);

                    break;
            }
        }
    };

    @Override
    public void onCreate()
    {
        super.onCreate();
        mNotificationManager = (NotificationManager) getSystemService(android.content.Context.NOTIFICATION_SERVICE);

    }

    @Override
    public void onStart(Intent intent, int startId)
    {
        // TODO Auto-generated method stub
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        apkUrl = intent.getStringExtra("apkUrl");
        canceled = false;
        Thread downLoadThread = new Thread(mdownApkRunnable);
        downLoadThread.start();
        return START_NOT_STICKY;
    }


    private Runnable mdownApkRunnable = new Runnable()
    {
        @Override
        public void run()
        {
            try
            {
                setUpNotification();
                URL url = new URL(apkUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                int length = conn.getContentLength();
                InputStream is = conn.getInputStream();
                File file = new File(Constants.APK_PATH);
                if (!file.exists())
                {
                    file.mkdirs();
                }
                String apkFile = Constants.SAVE_FILE_NAME;
                File ApkFile = new File(apkFile);
                FileOutputStream fos = new FileOutputStream(ApkFile);
                int count = 0;
                byte buf[] = new byte[1024];

                do
                {
                    int numread = is.read(buf);
                    count += numread;
                    progress = (int) (((float) count / length) * 100);
                    // 更新进度
                    Message msg = mHandler.obtainMessage();
                    msg.what = 1;
                    msg.arg1 = progress;
                    if (progress >= lastRate + 1)
                    {
                        mHandler.sendMessage(msg);
                        lastRate = progress;
                    }
                    if (numread <= 0)
                    {
                        // 下载完成通知安装
                        mHandler.sendEmptyMessage(0);
                        // 下载完了，cancelled也要设置
                        canceled = true;
                        break;
                    }
                    fos.write(buf, 0, numread);
                } while (!canceled);// 点击取消就停止下载.

                fos.close();
                is.close();
            } catch (MalformedURLException e)
            {
                mHandler.sendEmptyMessage(2);
                e.printStackTrace();
            } catch (IOException e)
            {
                mHandler.sendEmptyMessage(2);
                e.printStackTrace();
            }

        }
    };


    public void cancel()
    {
        canceled = true;
    }

    public boolean isCanceled()
    {
        return canceled;
    }


    @Override
    public void onDestroy()
    {
        Log.v("tangcy", "销毁升级服务");
        super.onDestroy();
    }


    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }


    private void installApk()
    {
        // TODO Auto-generated method stub
        /*********下载完成，点击安装***********/
        File file = new File(Constants.SAVE_FILE_NAME);
        if (file.exists())
        {
            mNotificationManager.cancel(notificationID);
            Uri uri = Uri.fromFile(file);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            /**********加这个属性是因为使用Context的startActivity方法的话，就需要开启一个新的task**********/
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
            NotificationUpdateService.this.startActivity(intent);
         }
    }


    /**
     * 创建通知栏
     */
    private void setUpNotification()
    {
        mNotification = new Notification();
        mNotification.icon = android.R.drawable.stat_sys_download;//设置通知消息的图标
        mNotification.tickerText = "版本升级...";//设置通知消息的标题
        mNotification.flags = Notification.FLAG_ONGOING_EVENT;  // 放置在"正在运行"栏目中
        remoteViews = new RemoteViews(getPackageName(), R.layout.download_notify);
        remoteViews.setTextViewText(R.id.iv_notify_name, "版本升级");
        remoteViews.setTextViewText(R.id.tv_already_download, "资源链接中..."); // 显示下载进度百分比
        remoteViews.setProgressBar(R.id.ProgressBarDown, 100, 0, false);//设置进度条最大100 默认0
        // 指定个性化视图
        mNotification.contentView = remoteViews;
        mNotificationManager.notify(notificationID, mNotification);

    }

}

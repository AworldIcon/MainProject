package com.coder.kzxt.download;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.app.utils.L;
import com.coder.kzxt.db.DataBaseDao;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.SdcardUtils;
import com.coder.kzxt.utils.SharedPreferencesUtil;

import org.xutils.common.Callback;
import org.xutils.common.task.PriorityExecutor;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.coder.kzxt.utils.Constants.DOWNLOAD_KEY;
import static com.coder.kzxt.utils.Constants.DOWNLOAD_M3U8;
import static com.coder.kzxt.utils.Constants.DOWNLOAD_URLS;
import static com.coder.kzxt.utils.Constants.DOWNLOAD_VR;
import static com.coder.kzxt.utils.Constants.M3U8;
import static com.coder.kzxt.utils.Constants.M3U8_KEY;
import static com.coder.kzxt.utils.Constants.M3U8_URLS;
import static com.coder.kzxt.utils.Constants.MAIN_FOLDER;
import static com.coder.kzxt.utils.Constants.VIDEOS;
import static com.coder.kzxt.utils.Constants.VR_VIDEO;

public class DownloadManager {

    private Context mContext;
    private ArrayList<String> arrayList;
    //存储下载队列
    private ArrayList<org.xutils.common.Callback.Cancelable> cacelables;
    private SharedPreferencesUtil spu;
    private String path_urls;//m3u8 所有url地址
    private String path_m3u8;//m3u8 总文件地址
    private String path_key; //m3u8 key文件地址
    private String path_vr; //vr视频下载地址

    //服务id
    private String serviceId;
    //课程id
    private String treeid;
    private String tid;
    private String url;
    private String filename;
    private String newM3u8DownUrl;
    private String id;
    private long filesize;//文件总大小
    private long fileCurrentSize;//vr文件当前大小
    private boolean addfilesize;
    private int mapKey;// 下载到第几个视频
    private String video_type; //视频类型

    /**
     * 下载的 平均网速
     */
    private final int beginDownLoad = 1;// 开始下载
    private final int downLoadComplete = 2;// 下载全部完成
    private final int downLoadFailure = 3;// 下载失败
    private final int downLoadVrFailure = 4;// 下载vr失败
    private final int downLoadVrComplete = 5;// 下载vr完成
    private boolean examine_sd = true;// 检查sd卡
    private boolean isOldM3u8 = false;// 是否是新版格式m3u8 true 代表是旧版 flase 代表是新版

    private boolean isRANGE = true;// 是否断点续传

    private int tsErr = 0;
    private boolean isStopDownload=false;
    OkHttpClient client = new OkHttpClient();
    org.xutils.common.Callback.Cancelable cancelable;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            if (msg.what == beginDownLoad) {

                if (arrayList.size() != 0) {

                    checkSatisfyDownload(video_type);

                    // 创建urls存放的文件夹
                    final File path_urls_file = new File(path_urls + tid + id);
                    if (!path_urls_file.exists()) {
                        path_urls_file.mkdirs();
                    }
                    Log.v("tangcy", "当前下载到第" + mapKey + "个");

                    for (int i = 0; i < arrayList.size(); i++) {

                        if (i == mapKey) {
                            RequestParams params = new RequestParams(arrayList.get(i));
                            //自定义保存路径
                            params.setSaveFilePath(path_urls + tid + id + "//" + i);
                            //自动为文件命名
                            params.setAutoRename(false);
                            //断点续传
                            params.setAutoResume(false);
                            params.setExecutor(new PriorityExecutor(1,true));
                            params.setCancelFast(true);

                            cancelable = x.http().get(params, new org.xutils.common.Callback.ProgressCallback<File>() {
                                @Override
                                public void onSuccess(File result) {
                                    mapKey++;
                                    cancelable.cancel();
                                   if (mapKey >= arrayList.size()) {
                                        L.v("tangcy", "下载url全部完成");
                                        addfilesize = true;

                                        if (!isOldM3u8) {
                                            CreateM3u8 cM3u8 = new CreateM3u8(newM3u8DownUrl);
                                            cM3u8.start();
                                        } else {
                                            CreateM3u8 cM3u8 = new CreateM3u8(url);
                                            cM3u8.start();
                                        }

                                    } else {
                                        if(!isStopDownload){
                                            handler.sendEmptyMessage(beginDownLoad);
                                        }
                                        tsErr = 0;
                                        L.v("tangcy", "下载成功");
                                        addfilesize = true;

                                        float size = (float) mapKey * 100 / (float) arrayList.size();// 下载进度
                                        DecimalFormat format = new DecimalFormat("0.00");
                                        String progress = format.format(size);// 下载百分比

                                        // 更新数据库
                                        DataBaseDao.getInstance(mContext).updateProgress(id, progress, size, mapKey, Integer.parseInt(String.valueOf(filesize)));

                                        Intent intent_broad = new Intent();
                                        intent_broad.setAction(Constants.MY_DOWNLOAD_DOWNING);
                                        intent_broad.putExtra("progress", size);
                                        intent_broad.putExtra("baifenbi", progress);
                                        intent_broad.putExtra("id", id);
                                        mContext.sendBroadcast(intent_broad);
                                    }

                                }

                                @Override
                                public void onError(Throwable ex, boolean isOnCallback) {
                                    L.e("tangcy", "下载ts失败");
                                    addfilesize = true;
                                    cancelable.cancel();
                                    tsErr++;
                                    if(tsErr <=2&&!isStopDownload){
                                        handler.sendEmptyMessage(beginDownLoad);
                                    }else {
                                        handler.sendEmptyMessage(downLoadFailure);
                                    }
                                    ex.printStackTrace();
                                }

                                @Override
                                public void onCancelled(CancelledException cex) {

                                }

                                @Override
                                public void onFinished() {

                                }

                                @Override
                                public void onWaiting() {

                                }

                                @Override
                                public void onStarted() {
                                    checkSatisfyDownload(video_type);
                                }

                                @Override
                                public void onLoading(long total, long current, boolean isDownloading) {
                                    if (addfilesize) {
                                        filesize += total;
                                        L.v("tangcy", "视频下载的大小为" + filesize);
                                        addfilesize = false;
                                    }
                                }
                            });
                            cacelables.add(cancelable);
                        }

                    }

                }
            } else if (msg.what == downLoadComplete) {

                DataBaseDao.getInstance(mContext).updata_DownloagIng_Finsh(id, path_m3u8 + "/" + tid + id + ".m3u8", Integer.parseInt(String.valueOf(filesize)));
                Intent intent_finsh = new Intent();
                intent_finsh.setAction(Constants.MY_DOWNLOAD_FINSH);
                intent_finsh.putExtra("id", id);
                intent_finsh.putExtra("url", path_m3u8 + "/" + tid + id + ".m3u8");
                intent_finsh.putExtra("filesize", String.valueOf(filesize));
                mContext.sendBroadcast(intent_finsh);
                DownloadService.backstage_Download_Next_Video(mContext, treeid, serviceId);

            } else if (msg.what == downLoadFailure) {

                downloadFail();

            }else if(msg.what==downLoadVrComplete){

                    DataBaseDao.getInstance(mContext).updata_DownloagIng_Finsh(id, path_vr + "/" + tid + id + ".mp4",
                            Integer.parseInt(String.valueOf(filesize)));
                    Intent intent_finsh = new Intent();
                    intent_finsh.setAction(Constants.MY_DOWNLOAD_FINSH);
                    intent_finsh.putExtra("id", id);
                    intent_finsh.putExtra("url", path_vr + "/" + tid + id + ".mp4");
                    intent_finsh.putExtra("filesize", String.valueOf(filesize));
                    mContext.sendBroadcast(intent_finsh);
                    DownloadService.backstage_Download_Next_Video(mContext, treeid, serviceId);

            }else if(msg.what==downLoadVrFailure){
                float size;
                if(filesize!=0){
                     size =  fileCurrentSize/filesize;//下载进度
                }else {
                     size=0;
                }

                DecimalFormat format = new DecimalFormat("0.00");
                String progress = format.format(size);// 下载百分比
                DataBaseDao.getInstance(mContext).failProgress(id, progress, size, 0, Integer.parseInt(String.valueOf(filesize)));
                downloadFail();

            }

            super.handleMessage(msg);
        }

    };

    DownloadManager(Context appContext) {
        mContext = appContext;
        arrayList = new ArrayList<String>();
        cacelables = new ArrayList<>();
        spu = new SharedPreferencesUtil(mContext);

    }

    // 开始下载

    /**
     * 课程的视频下载调用
     *
     * @param url      下载地址
     * @param courseId 课程id
     * @param tid      章节id
     * @param videoId  视频id
     * @param mapkey
     * @param filename
     * @param filesize
     */
    public void addCourseVideoDownload(String url, String courseId, String tid, String videoId,
                                       int mapkey, String filename, long filesize, String video_type) {
        addNewVideoDownload(url, courseId, "", tid, videoId, mapkey, filename, filesize,video_type);
    }

    /**
     * 服务的视频下载调用
     *
     * @param url       下载地址
     * @param serviceId 服务id
     * @param videoId1  视频id
     * @param mapkey
     * @param filename
     * @param filesize
     */
    public void addServiceVideoDownload(String url, String serviceId, String videoId1, String videoId2,
                                        int mapkey, String filename, long filesize, String video_type) {
        addNewVideoDownload(url, "", serviceId, videoId1, videoId2, mapkey, filename, filesize,video_type);
    }

    /**
     * @param url      下载地址
     * @param courseId 本课程的id 用于查询本视频下载成功后 下载的本课程下的其它视频不是其它课程的视频
     * @param tid      文件夹的第一次字段 章节id （没有则传递视频id）
     * @param id       文件夹的第二个字段
     * @param mapkey
     * @param filename
     * @param filesize
     */
    public void addNewVideoDownload(final String url, String courseId, String serviceId, String tid,
                                    String id, int mapkey, String filename, long filesize, String video_type) {
        examine_sd = true;
        arrayList.clear();
        cacelables.clear();
        addfilesize = true;
        this.url = url;
        this.serviceId = serviceId;
        this.treeid = courseId;
        this.tid = tid;
        this.id = id;
        this.filename = filename;
        this.mapKey = mapkey;
        this.filesize = filesize;
        this.video_type = video_type;
        isStopDownload = false;

        if (spu.getSelectAddress() == 0) {
            path_urls = mContext.getExternalFilesDir(null).getAbsolutePath()+DOWNLOAD_URLS;
            path_m3u8 = mContext.getExternalFilesDir(null).getAbsolutePath()+DOWNLOAD_M3U8;
            path_key = mContext.getExternalFilesDir(null).getAbsolutePath()+DOWNLOAD_KEY;
            path_vr =  mContext.getExternalFilesDir(null).getAbsolutePath()+DOWNLOAD_VR;

        } else if (spu.getSelectAddress() == 1) {

            String secondSdcard = spu.getSecondSdcard();

            if (Constants.API_LEVEL_19) {

                path_urls = secondSdcard + "/Android/data/" + mContext.getPackageName() + MAIN_FOLDER + VIDEOS + M3U8_URLS;
                path_m3u8 = secondSdcard + "/Android/data/" + mContext.getPackageName() + MAIN_FOLDER + VIDEOS + M3U8;
                path_key = secondSdcard + "/Android/data/" + mContext.getPackageName() + MAIN_FOLDER+ VIDEOS + M3U8_KEY;
                path_vr = secondSdcard + "/Android/data/" + mContext.getPackageName() + MAIN_FOLDER + VIDEOS + VR_VIDEO;

            } else {
                path_urls = secondSdcard + MAIN_FOLDER + VIDEOS + M3U8_URLS;
                path_m3u8 = secondSdcard + MAIN_FOLDER + VIDEOS + M3U8;
                path_key = secondSdcard + MAIN_FOLDER + VIDEOS + M3U8_KEY;
                path_vr = secondSdcard + MAIN_FOLDER + VIDEOS + VR_VIDEO;
            }

        }

        //如果video_type是空的或者是video 证明是下载的m3u8文件
        if(TextUtils.isEmpty(this.video_type)|| this.video_type.equals("1")){
//            DownloadThread dt = new DownloadThread(url);
//            dt.start();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    downloadM3u8Video(url);
                }
            }).start();
        }else if(video_type.equals("6")){
            //下载vr文件 mp4
            downloadVrVideo();
        }

    }

    public ArrayList<Callback.Cancelable> getAllCacebles() {
        isStopDownload = true;
        return cacelables;

    }


    /**
     * 下载m3u8总文件
     */
    private void downloadM3u8Video(String url){
        ArrayList<String> m3u8List = new ArrayList<String>();
        try {
            Request request = new Request.Builder().url(url).build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                InputStream is =  response.body().byteStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
                String line;
                String httpStartUrl = null;
                while ((line = br.readLine()) != null) {
                    if (line.contains(".m3u8")) {
                        int lastIndexOf = url.lastIndexOf("/");
                        httpStartUrl = url.substring(0, url.length() - (url.length() - lastIndexOf));
                        m3u8List.add(line);
                        isOldM3u8 = false;
                    } else {
                        isOldM3u8 = true;
                    }
                }
                is.close();
                L.v("tangcy", "m3u8格式" + isOldM3u8);
                if (!isOldM3u8) {
                    newM3u8DownUrl = httpStartUrl + "/" + m3u8List.get(0);
                    L.v("tangcy", "下载url" + newM3u8DownUrl);
                    newM3u8Url();
                }else {
                    Log.v("tangcy", "下载url" + url);
                    oldM3u8Url();
                }
            }else {
                L.v("tangcy","下载m3u8文件失败");
                downloadFailM3u8();
            }
        }catch (Exception e) {
            e.printStackTrace();
            downloadFailM3u8();
        }
    }

    /**
     * 解析新m3u8格式文件
     */
    private void newM3u8Url(){
        try {
            Request request = new Request.Builder().url(newM3u8DownUrl).build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()){
                InputStream isNewM3u8 =  response.body().byteStream();
                BufferedReader brNewM3u8 = new BufferedReader(new InputStreamReader(isNewM3u8, "utf-8"));
                String lineNewM3u8;
                while ((lineNewM3u8 = brNewM3u8.readLine()) != null) {
                    if (lineNewM3u8.startsWith("#")&&lineNewM3u8.contains("URI")) {
                         String keyUrl = lineNewM3u8.substring(lineNewM3u8.lastIndexOf("http:"), lineNewM3u8.lastIndexOf(".key"))+".key";
                         L.v("tangcy","key地址"+keyUrl);

                        if(!TextUtils.isEmpty(keyUrl)){
                            Request requestKey = new Request.Builder().url(keyUrl).build();
                            Response responseKey = client.newCall(requestKey).execute();
                            if(responseKey.isSuccessful()){
                                InputStream is_key =  responseKey.body().byteStream();
                                // 创建key存放的文件夹
                                File path_key_file = new File(path_key);
                                if (!path_key_file.exists()) {
                                    path_key_file.mkdirs();
                                }
                                // 创建key文件
                                File downloadFile_key = new File(path_key + "//" + tid + id + "key");

                                if (downloadFile_key.exists()) {
                                    downloadFile_key.delete();
                                }

                                byte[] bs_key = new byte[1024];
                                // 读取到的数据长度
                                int len_key;
                                // 输出的文件流
                                OutputStream os_key = new FileOutputStream(downloadFile_key);
                                // 开始读取
                                while ((len_key = is_key.read(bs_key)) != -1) {
                                    os_key.write(bs_key, 0, len_key);
                                }
                                os_key.close();
                                is_key.close();
                                L.v("tangcy", "创建key成功");
                            }else {
                                downloadFailM3u8();
                                L.v("tangcy","创建key失败");
                            }

                            }

                    }

                    if (lineNewM3u8.contains("http:") && lineNewM3u8.contains(".ts")) {
                        // 把分段课程的urls存入文件
                        if (lineNewM3u8.length() > 0) {
                            arrayList.add(lineNewM3u8);
                        }
                    }

                }
                isNewM3u8.close();
                brNewM3u8.close();
                handler.sendEmptyMessage(beginDownLoad);
            }else {
                downloadFailM3u8();
                L.v("tangcy","创建新m3u8文件失败");
            }
        }catch (Exception e){
            e.printStackTrace();
            downloadFailM3u8();
        }

    }

    /**
     * 解析旧m3u8格式文件
     */
    private void oldM3u8Url(){
        try {
            Request request = new Request.Builder().url(url).build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()){
                InputStream isOld =  response.body().byteStream();
                BufferedReader brOld = new BufferedReader(new InputStreamReader(isOld, "utf-8"));
                String lineOld;

                while ((lineOld = brOld.readLine()) != null) {
                    if (lineOld.startsWith("#")) {
                        if (lineOld.contains("URI")) {
                            // key的地址
                            String keyUrl = lineOld.substring(lineOld.lastIndexOf("http:"), lineOld.lastIndexOf(".key"))+".key";
                            L.v("tangcy","key地址"+keyUrl);
                            if (!TextUtils.isEmpty(keyUrl)) {
                                Request requestKey = new Request.Builder().url(keyUrl).build();
                                Response responseKey = client.newCall(requestKey).execute();
                                if(responseKey.isSuccessful()){
                                    InputStream is_key =  responseKey.body().byteStream();
                                    // 创建key存放的文件夹
                                    File path_key_file = new File(path_key);
                                    if (!path_key_file.exists()) {
                                        path_key_file.mkdirs();
                                    }

                                    // 创建key文件
                                    File downloadFile_key = new File(path_key + "//" + tid + id + "key");

                                    if (downloadFile_key.exists()) {
                                        downloadFile_key.delete();
                                    }

                                    byte[] bs_key = new byte[1024];
                                    // 读取到的数据长度
                                    int len_key;
                                    // 输出的文件流
                                    OutputStream os_key = new FileOutputStream(downloadFile_key);
                                    // 开始读取
                                    while ((len_key = is_key.read(bs_key)) != -1) {
                                        os_key.write(bs_key, 0, len_key);
                                    }

                                    os_key.close();
                                    is_key.close();
                                    L.v("tangcy", "创建key成功");
                                }else {
                                    downloadFailM3u8();
                                    L.v("tangcy","创建key失败");
                                }

                            }
                        }

                        continue;
                    }

                    // 把分段课程的urls存入文件
                    if (lineOld.length() > 0) {
                        arrayList.add(lineOld);
                    }
                }
                isOld.close();
                handler.sendEmptyMessage(beginDownLoad);

            }else {
                downloadFailM3u8();
                L.v("tangcy","创建旧m3u8文件失败");
            }

        }catch (Exception e){
            e.printStackTrace();
            downloadFailM3u8();
        }

    }

    private void downloadFailM3u8(){
        Intent intent_fail = new Intent();
        intent_fail.putExtra("id", id);
        mContext.sendBroadcast(intent_fail);
        addfilesize = true;
        handler.sendEmptyMessage(downLoadFailure);
    }


    public class CreateM3u8 extends Thread {
        private String url;

        public CreateM3u8(String url) {
            this.url = url;
        }
        @Override
        public void run() {
            createSdcardM3u8File(url);
            super.run();
        }

    }


    /**
     * 创建m3u8格式的本地文件
     */
    private void createSdcardM3u8File(String m3u8Url){
        FileOutputStream fos = null;
        OutputStreamWriter osw = null;
        BufferedWriter bw = null;

        try{
            Request request = new Request.Builder().url(m3u8Url).build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()){
                InputStream isNewM3u8 =  response.body().byteStream();
                BufferedReader brNewM3u8 = new BufferedReader(new InputStreamReader(isNewM3u8, "utf-8"));
                String lineNewM3u8;

                // 创建m3u8存放的文件夹
                File path_urls_file = new File(path_m3u8);
                if (!path_urls_file.exists()) {
                    path_urls_file.mkdirs();
                }
                // 创建m3u8文件
                File downloadFile_key = new File(path_m3u8 + "//" + tid + id + ".m3u8");
                if (downloadFile_key.exists()) {
                    downloadFile_key.delete();
                }

                fos = new FileOutputStream(downloadFile_key);
                osw = new OutputStreamWriter(fos, "UTF-8");
                bw = new BufferedWriter(osw);
                int i = 0;

                while ((lineNewM3u8 = brNewM3u8.readLine()) != null) {

                    if (lineNewM3u8.contains("URI") && lineNewM3u8.contains(".key")) {
                        // #EXT-X-KEY:METHOD=AES-128,URI="http://hls.videocc.net/a443cb96bc/a/a443cb96bcab9d63acef82fc9a64699a_3.key",IV=0x44a47b204381f37750e06bd8662f2603
                        String substring = lineNewM3u8.substring(0, lineNewM3u8.lastIndexOf("http:"));
                        String substring2 = lineNewM3u8.substring(lineNewM3u8.lastIndexOf(".key"), lineNewM3u8.length());
                        String substring3 = substring2.substring(5, substring2.length());

                        // #EXT-X-KEY:METHOD=AES-128,URI="
                        // .key",IV=0x44a47b204381f37750e06bd8662f2603
                        bw.write(substring.substring(0, substring.length() - 1) + path_key + "/" + tid + id + "key" + substring3 + "\n");
                    }

                    if (lineNewM3u8.contains("http:") && lineNewM3u8.contains(".ts")) {
                        bw.write(path_urls + tid + id + "/" + i++ + "\n");
                    } else {
                        if (!lineNewM3u8.contains("URI") && !lineNewM3u8.contains(".key")) {
                            bw.write(lineNewM3u8 + "\n");
                        }
                    }

                }

                isNewM3u8.close();
                brNewM3u8.close();
                L.v("tangcy", "创建m3u8文件完成");
                // 下载完成
                handler.sendEmptyMessage(downLoadComplete);
            }else {
                L.v("tangcy", "创建m3u8文件失败");
                downloadFailM3u8();
            }

        }catch (Exception e){
            e.printStackTrace();
            downloadFailM3u8();
        }finally {
            try {
                if (bw != null) {
                    bw.close();
                }
                if (osw != null) {
                    osw.close();
                }

                if (fos != null) {
                    fos.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 下载vr视频
     */
    private void downloadVrVideo(){
        // 创建vr存放的文件夹
        final File path_vr_f = new File(path_vr);
        if (!path_vr_f.exists()) {
            path_vr_f.mkdirs();
        }
        String downloadpos = DataBaseDao.getInstance(mContext).quaryDownloadpos(tid, id);// 离线视频地址
        //检查本地文件是否拥有并且下载进度为0 这种情况是删除了app但是视频没删除 下载视频不能断点续传
        File file =new File(path_vr + "//"+tid + id + ".mp4");
        if(file.exists()&&downloadpos.equals("0")){
            isRANGE = false;
        }else {
            isRANGE = true;
        }

        RequestParams params = new RequestParams(url);
        //自定义保存路径
        params.setSaveFilePath(path_vr + "//"+tid + id + ".mp4");
        //自动为文件命名
        params.setAutoRename(false);
        //断点续传
        params.setAutoResume(isRANGE);
        params.setExecutor(new PriorityExecutor(1,true));
        params.setCancelFast(true);

        org.xutils.common.Callback.Cancelable cacelable = x.http().get(params, new Callback.ProgressCallback<File>() {

            @Override
            public void onSuccess(File result) {
                handler.sendEmptyMessage(downLoadVrComplete);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                handler.sendEmptyMessage(downLoadVrFailure);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }

            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {
                checkSatisfyDownload(video_type);
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {

                //如果下载的文件大小大于100字节就加密
                    File ecrFile =  new File(path_vr + "//"+tid + id + ".mp4");
//                    if(!spu.getEncr(tid,id)){
//                    if(current>20000){
//                       boolean encry = VideoAesUtil.encrypt(path_vr + "//" + tid + id + ".mp4");
//                        if(encry){
//                            spu.setEncr(tid,id,true);
//                          }
//                        }
//                    }

                filesize = total;
                fileCurrentSize = current;
                float percent = (((float)current / total) * 100);
                DecimalFormat format = new DecimalFormat("0.00");
                String progress = format.format(percent);// 下载百分比
                DataBaseDao.getInstance(mContext).updateProgress(id, progress, percent, 0, Integer.parseInt(String.valueOf(current)));
                //发送广播更新界面
                Intent intent_broad = new Intent();
                intent_broad.setAction(Constants.MY_DOWNLOAD_DOWNING);
                intent_broad.putExtra("progress", percent);
                intent_broad.putExtra("baifenbi", progress);
                intent_broad.putExtra("id", id);
                mContext.sendBroadcast(intent_broad);

            }
        });
        cacelables.add(cacelable);
    }


    /**
     * 下载失败
     */
    private void downloadFail(){
        Intent intent_fail = new Intent();
        intent_fail.setAction(Constants.MY_DOWNLOAD_FAIL);
        intent_fail.putExtra("id", id);
        mContext.sendBroadcast(intent_fail);

        // 取消队列中的全部下载
        ArrayList<Callback.Cancelable> getallhandler = getAllCacebles();
        for (int i = 0; i < getallhandler.size(); i++) {
           Callback.Cancelable cabcelble = getallhandler.get(i);
            if (cabcelble != null && !cabcelble.isCancelled()) {
                cabcelble.cancel();
            }
        }
        DownloadService.stopService(mContext);


    }


    /**
     * 检查是否满足下载条件
     */
    private void checkSatisfyDownload(String video_type){
        // 判断外置sd卡是否有足够空间
        if (spu.getSelectAddress() == 1) {
            if (SdcardUtils.getSDFreeSize(spu.getSecondSdcard()) < 100) {
                if (examine_sd) {
                    if(video_type.equals("6")){
                        handler.sendEmptyMessage(downLoadVrFailure);
                    }else {
                        handler.sendEmptyMessage(downLoadFailure);
                    }

                    Toast.makeText(mContext, "检测到您的SD空间不足。", Toast.LENGTH_LONG).show();
                    examine_sd = false;
                }

            }

        } else {
            // 判断sd卡是否有足够空间下载
            if (SdcardUtils.getSDFreeSize(Environment.getExternalStorageDirectory().getPath()) < 100) {

                if (examine_sd) {
                    if(video_type.equals("6")){
                        handler.sendEmptyMessage(downLoadVrFailure);
                    }else {
                        handler.sendEmptyMessage(downLoadFailure);
                    }

                    Toast.makeText(mContext, "检测到您的SD空间不足。", Toast.LENGTH_LONG).show();
                    examine_sd = false;
                }

            }

        }

        //  判断是否开启3g/4g开关，如果没有开启检查是否连接wifi
        if(spu.getDownloadFlag().equals("0")){
            if(!NetworkUtil.isWifiNetwork(mContext)){
                //发送下载失败消息 停止下载
                if(video_type.equals("6")){
                    handler.sendEmptyMessage(downLoadVrFailure);
                }else {
                    handler.sendEmptyMessage(downLoadFailure);
                }

            }
        }
    }

}

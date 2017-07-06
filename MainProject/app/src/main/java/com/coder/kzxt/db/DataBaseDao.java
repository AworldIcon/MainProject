package com.coder.kzxt.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataBaseDao
{

    private static DataBaseDao dao = null;
    private Context context;

    private DataBaseDao(Context context)
    {
        this.context = context;
    }

    public static DataBaseDao getInstance(Context context)
    {
        if (dao == null)
        {
            dao = new DataBaseDao(context);
        }
        return dao;
    }


    public SQLiteDatabase getConnection()
    {
        SQLiteDatabase sqliteDatabase = null;
        try
        {
            sqliteDatabase = new DBHelper(context).getWritableDatabase();
        } catch (Exception e)
        {
        }
        return sqliteDatabase;
    }


    /**
     * 获取数据库所有id
     */
    public synchronized ArrayList<String> getVideoIds()
    {
        ArrayList<String> arrayList = new ArrayList<String>();
        SQLiteDatabase database = getConnection();
        Cursor cursor = database.query("Download", new String[]{"video_id"}, null, null, null, null, null);
        if (cursor != null)
        {
            while (cursor.moveToNext())
            {
                String id_db = cursor.getString(cursor.getColumnIndex("video_id"));
                arrayList.add(id_db);
            }
        }

        if (cursor != null)
        {
            cursor.close();
        }
        database.close();
        return arrayList;
    }

    /**
     * 下载课程 视频数据
     *
     * @param id 视频id
     *           tid 视频章节id
     *           name 视频名字
     */
    public synchronized void initDownloadData(String serviceId, String serviceName, String id, String tid, String name, String url_content, String pic,
                                              String taskname, int downloadStatus, int downloadSpeed, int treeid, String treename, String treepicture,
                                              int randid, int sort, String video_type, int is_drag)
    {

        SQLiteDatabase database = getConnection();
        ContentValues values = new ContentValues();
        //新增两个字段 服务相关
        values.put("service_id", serviceId);//服务id
        values.put("service_name", serviceName);//服务名字

        values.put("video_id", id);//视频id
        values.put("url", url_content);//视频播放地址
        values.put("filename", name);//视频名字
        values.put("picture", pic);//视频的图片地址
        values.put("urlTotal", tid);///视频章节id
        values.put("foldername", taskname);//视频章节名称

        values.put("downloadStatus", downloadStatus);//视频下载状态 不是按钮显示状态 0 等待下载,  1 下载中, 2暂停,3 是下载完成的
        values.put("isfinish", 0);//是否在下载 0初始化下载 1下载完成
        values.put("downloadedSize", 0);//下载完成视频的大小
        values.put("mapKey", 0);//下载到第几个url
        values.put("downloadpos", 0);//下载进度
        values.put("downloadPercent", 0);//下载百分比
        values.put("downloadSpeed", downloadSpeed);//下载速度，这个字段暂时没用到

        values.put("treeid", treeid);//课程id
        values.put("treename", treename);//课程名字
        values.put("currentposition", 0);//视频当前播放的位置
        values.put("continue_playing_time", 0);//一门课程的持续播放时间
        values.put("recordingtime", 0);//一门课程的持续播放的毫秒值
        values.put("reporteddata", "1");//是否上报观看进度
        values.put("duration", 0);//视频总长度
        values.put("sort", sort);//视频排序
        // 课程 服务共用
        values.put("localurl", treepicture);// 课程图片 或者 服务的图片
        values.put("randid", randid);//章节排序
        values.put("video_type", video_type);//视频类型 6是vr 1是普通视频
        values.put("is_drag", is_drag);//是否支持快进快退 0是支持 1是不支持
        long aa = database.insert("Download", null, values);
        database.close();

    }

    /**
     * 下载服务视频数据
     *
     * @param videoId 视频id
     */
    public synchronized void initDownloadData(String serviceId, String serviceName,
                                              String videoId, String videoName, String videoPic, String video_url, int downloadStatus)

    {

        SQLiteDatabase database = getConnection();
        ContentValues values = new ContentValues();
        //新增两个字段 服务相关
        values.put("service_id", serviceId);//服务id
        values.put("service_name", serviceName);//服务名字

        values.put("video_id", videoId);//视频id
        values.put("url", video_url);//视频播放地址
        values.put("filename", videoName);//视频名字
        values.put("picture", videoPic);//视频的图片地址
        values.put("downloadedSize", 0);//下载完成视频的大小
        values.put("duration", 0);//视频总长度
        values.put("video_type", "1");//视频类型 6是vr 1是普通视频
        values.put("downloadStatus", downloadStatus);//视频下载状态 不是按钮显示状态 0 等待下载,  1 下载中, 2暂停,3 是下载完成的
        values.put("is_drag", 0);//是否支持快进快退 0是支持 1是不支持

        values.put("isfinish", 0);//是否在下载 0初始化下载 1下载完成
        values.put("downloadPercent", 0);//下载百分比


        //服务用不到的参数
        values.put("urlTotal", "");///视频章节id
        values.put("foldername", "");//视频章节名称
        values.put("mapKey", 0);//下载到第几个url
        values.put("treeid", "");//课程id
        values.put("treename", "");//课程名字
        values.put("currentposition", 0);//视频当前播放的位置
        values.put("continue_playing_time", 0);//一门课程的持续播放时间
        values.put("recordingtime", 0);//一门课程的持续播放的毫秒值
        values.put("reporteddata", "1");//是否上报观看进度
        values.put("duration", 0);//视频总长度
        values.put("sort", 0);//视频排序
        // 课程 服务共用
        values.put("localurl", "");// 课程图片 或者 服务的图片
        values.put("randid", 0);//章节排序

        long aa = database.insert("Download", null, values);
        database.close();

    }


    /**
     * 更新视频下载进度到数据库
     */
    public synchronized void updateProgress(String id, String progress, Float size, int downloadIngindex, int downloadedSize)
    {

        SQLiteDatabase database = getConnection();
        ContentValues contentValues = new ContentValues();
        contentValues.put("downloadpos", progress);//进度条
        contentValues.put("downloadPercent", size);//下载百分比
        contentValues.put("mapKey", downloadIngindex); //下载第几个视频
        contentValues.put("downloadedSize", downloadedSize);
        database.update("Download", contentValues, "video_id=?", new String[]{id});
        database.close();

    }

    /**
     * 下载失败更新进度到数据库
     */
    public synchronized void failProgress(String id, String progress, Float size, int downloadIngindex, int downloadedSize)
    {
        SQLiteDatabase database = getConnection();
        ContentValues contentValues = new ContentValues();
        contentValues.put("downloadpos", progress);//进度条
        contentValues.put("downloadPercent", size);//下载百分比
        contentValues.put("mapKey", downloadIngindex); //下载第几个视频
        contentValues.put("downloadedSize", downloadedSize);
        contentValues.put("downloadStatus", 2);
        database.update("Download", contentValues, "video_id=?", new String[]{id});
        database.close();

    }


    /**
     * 根据treeid获得一门课程的所有视频id
     *
     * @param treeid
     * @return
     */
    public synchronized ArrayList<String> query_tree_allvideoids(String treeid)
    {
        ArrayList<String> arrayList = new ArrayList<String>();
        SQLiteDatabase database = getConnection();
        Cursor cursor = database.query("Download", new String[]{"video_id"}, "treeid=?", new String[]{treeid}, null, null, null);
        if (cursor != null)
        {
            while (cursor.moveToNext())
            {
                String id = cursor.getString(cursor.getColumnIndex("video_id"));
                arrayList.add(id);
            }
        }

        if (cursor != null)
        {
            cursor.close();
        }
        database.close();

        return arrayList;
    }

    /**
     * 根据serviceId获得一服务下的所有视频id
     *
     * @return
     */
    public synchronized ArrayList<String> queryVideoidsByServiceId(String serviceId)
    {
        ArrayList<String> arrayList = new ArrayList<String>();
        SQLiteDatabase database = getConnection();
        Cursor cursor = database.query("Download", new String[]{"video_id"}, "service_id=?", new String[]{serviceId}, null, null, null);
        if (cursor != null)
        {
            while (cursor.moveToNext())
            {
                String id = cursor.getString(cursor.getColumnIndex("video_id"));
                arrayList.add(id);
            }
        }

        if (cursor != null)
        {
            cursor.close();
        }
        database.close();

        return arrayList;
    }


    /**
     * 根据课程id获得本课程所有等待下载的视频
     */
    public synchronized ArrayList<HashMap<String, String>> query_Wait_Download(String next_treeid)
    {
        ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String, String>>();
        SQLiteDatabase database = getConnection();
        Cursor cursor = database.query("Download", new String[]{"video_id", "urlTotal", "filename", "url", "picture", "foldername", "downloadStatus"
                , "isfinish", "downloadedSize", "downloadpos", "downloadPercent", "mapKey", "downloadSpeed", "treeid", "currentposition", "treename",
                "localurl", "reporteddata", "duration", "randid", "sort", "video_type"}, "downloadStatus=? and treeid=?", new String[]{"0", next_treeid}, null, null, null);
        if (cursor != null)
        {
            while (cursor.moveToNext())
            {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                int _id = cursor.getInt(cursor.getColumnIndex("video_id"));
                int urlTotal = cursor.getInt(cursor.getColumnIndex("urlTotal"));
                String filename = cursor.getString(cursor.getColumnIndex("filename"));
                String url = cursor.getString(cursor.getColumnIndex("url"));
                String picture = cursor.getString(cursor.getColumnIndex("picture"));
                String foldername = cursor.getString(cursor.getColumnIndex("foldername"));
                int downloadStatus = cursor.getInt(cursor.getColumnIndex("downloadStatus"));
                String downloadedSize = cursor.getString(cursor.getColumnIndex("downloadedSize"));
                int isfinish = cursor.getInt(cursor.getColumnIndex("isfinish"));
                String mapKey = cursor.getString(cursor.getColumnIndex("mapKey"));
                int downloadpos = cursor.getInt(cursor.getColumnIndex("downloadpos"));
                int downloadPercent = cursor.getInt(cursor.getColumnIndex("downloadPercent"));
                int downloadSpeed = cursor.getInt(cursor.getColumnIndex("downloadSpeed"));
                int treeid = cursor.getInt(cursor.getColumnIndex("treeid"));
                String treename = cursor.getString(cursor.getColumnIndex("treename"));
                String localurl = cursor.getString(cursor.getColumnIndex("localurl"));
                long currentposition = cursor.getLong(cursor.getColumnIndex("currentposition"));
                long duration = cursor.getLong(cursor.getColumnIndex("duration"));
                int randid = cursor.getInt(cursor.getColumnIndex("randid"));
                int sort = cursor.getInt(cursor.getColumnIndex("sort"));
                String reporteddata = cursor.getString(cursor.getColumnIndex("reporteddata"));
                String video_type = cursor.getString(cursor.getColumnIndex("video_type"));

                hashMap.put("id", String.valueOf(_id));
                hashMap.put("tid", String.valueOf(urlTotal));
                hashMap.put("name", filename);
                hashMap.put("url_content", url);
                hashMap.put("picture", picture);
                hashMap.put("taskname", foldername);
                hashMap.put("downloadStatus", String.valueOf(downloadStatus));
                hashMap.put("downloadedSize", downloadedSize);
                hashMap.put("isfinish", String.valueOf(isfinish));
                hashMap.put("mapKey", mapKey);
                hashMap.put("downloadpos", String.valueOf(downloadpos));
                hashMap.put("downloadPercent", String.valueOf(downloadPercent));
                hashMap.put("downloadSpeed", String.valueOf(downloadSpeed));
                hashMap.put("treeid", String.valueOf(treeid));
                hashMap.put("treename", treename);
                hashMap.put("treepicture", localurl);
                hashMap.put("reporteddata", reporteddata);
                hashMap.put("currentposition", String.valueOf(currentposition));
                hashMap.put("duration", String.valueOf(duration));
                hashMap.put("randid", String.valueOf(randid));
                hashMap.put("sort", String.valueOf(sort));
                hashMap.put("video_type", video_type);
                arrayList.add(hashMap);
            }

        }

        if (cursor != null)
        {
            cursor.close();
        }
        database.close();
        return arrayList;

    }

    /**
     * 课程的等待的下载的视频
     */
    public synchronized ArrayList<HashMap<String, String>> queryAllWaitCourseVideos()
    {
        return queryAllWaitVideos("");
    }

    public synchronized ArrayList<HashMap<String, String>> queryAllWaitServiceVideos(String serviceId)
    {
        return queryAllWaitVideos(serviceId);
    }

    public synchronized ArrayList<HashMap<String, String>> queryAllWaitVideos(String serviceId)
    {
        ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String, String>>();
        SQLiteDatabase database = getConnection();
        Cursor cursor = database.query("Download", new String[]{"video_id", "urlTotal", "filename", "url", "picture", "foldername", "downloadStatus"
                , "isfinish", "downloadedSize", "downloadpos", "downloadPercent", "mapKey", "downloadSpeed", "treeid", "currentposition", "treename",
                "localurl", "reporteddata", "duration", "randid", "sort", "service_id", "service_name", "video_type"}, "downloadStatus=? and service_id=?", new String[]{"0", serviceId}, null, null, null);
        if (cursor != null)
        {
            while (cursor.moveToNext())
            {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                int _id = cursor.getInt(cursor.getColumnIndex("video_id"));
                int urlTotal = cursor.getInt(cursor.getColumnIndex("urlTotal"));
                String filename = cursor.getString(cursor.getColumnIndex("filename"));
                String url = cursor.getString(cursor.getColumnIndex("url"));
                String picture = cursor.getString(cursor.getColumnIndex("picture"));
                String foldername = cursor.getString(cursor.getColumnIndex("foldername"));
                int downloadStatus = cursor.getInt(cursor.getColumnIndex("downloadStatus"));
                String downloadedSize = cursor.getString(cursor.getColumnIndex("downloadedSize"));
                int isfinish = cursor.getInt(cursor.getColumnIndex("isfinish"));
                String mapKey = cursor.getString(cursor.getColumnIndex("mapKey"));
                int downloadpos = cursor.getInt(cursor.getColumnIndex("downloadpos"));
                int downloadPercent = cursor.getInt(cursor.getColumnIndex("downloadPercent"));
                int downloadSpeed = cursor.getInt(cursor.getColumnIndex("downloadSpeed"));
                int treeid = cursor.getInt(cursor.getColumnIndex("treeid"));
                String treename = cursor.getString(cursor.getColumnIndex("treename"));
                String localurl = cursor.getString(cursor.getColumnIndex("localurl"));
                long currentposition = cursor.getLong(cursor.getColumnIndex("currentposition"));
                long duration = cursor.getLong(cursor.getColumnIndex("duration"));
                int randid = cursor.getInt(cursor.getColumnIndex("randid"));
                int sort = cursor.getInt(cursor.getColumnIndex("sort"));
                String reporteddata = cursor.getString(cursor.getColumnIndex("reporteddata"));
                String service_id = cursor.getString(cursor.getColumnIndex("service_id"));
                String service_name = cursor.getString(cursor.getColumnIndex("service_name"));
                String video_type = cursor.getString(cursor.getColumnIndex("video_type"));

                hashMap.put("id", String.valueOf(_id));
                hashMap.put("tid", String.valueOf(urlTotal));
                hashMap.put("name", filename);
                hashMap.put("url_content", url);
                hashMap.put("picture", picture);
                hashMap.put("taskname", foldername);
                hashMap.put("downloadStatus", String.valueOf(downloadStatus));
                hashMap.put("downloadedSize", downloadedSize);
                hashMap.put("isfinish", String.valueOf(isfinish));
                hashMap.put("mapKey", mapKey);
                hashMap.put("downloadpos", String.valueOf(downloadpos));
                hashMap.put("downloadPercent", String.valueOf(downloadPercent));
                hashMap.put("downloadSpeed", String.valueOf(downloadSpeed));
                hashMap.put("treeid", String.valueOf(treeid));
                hashMap.put("treename", treename);
                hashMap.put("treepicture", localurl);
                hashMap.put("reporteddata", reporteddata);
                hashMap.put("currentposition", String.valueOf(currentposition));
                hashMap.put("duration", String.valueOf(duration));
                hashMap.put("randid", String.valueOf(randid));
                hashMap.put("sort", String.valueOf(sort));
                hashMap.put("service_id", service_id);
                hashMap.put("service_name", service_name);
                hashMap.put("video_type", video_type);
                arrayList.add(hashMap);
            }

        }

        if (cursor != null)
        {
            cursor.close();
        }
        database.close();
        return arrayList;

    }


    /**
     * 获取正在下载的视频
     */
    public synchronized HashMap<String, String> query_Downing_Download()
    {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        SQLiteDatabase database = getConnection();
        Cursor cursor = database.query("Download", new String[]{"video_id", "urlTotal", "filename", "url", "picture", "foldername", "downloadStatus"
                , "isfinish", "downloadedSize", "downloadpos", "downloadPercent", "mapKey", "downloadSpeed", "treeid", "currentposition",
                "treename", "localurl", "reporteddata", "duration", "randid", "sort", "service_id", "service_name", "video_type"}, "downloadStatus=?", new String[]{"1"}, null, null, null);
        if (cursor != null)
        {
            while (cursor.moveToNext())
            {
                int id = cursor.getInt(cursor.getColumnIndex("video_id"));
                int urlTotal = cursor.getInt(cursor.getColumnIndex("urlTotal"));
                String filename = cursor.getString(cursor.getColumnIndex("filename"));
                String url = cursor.getString(cursor.getColumnIndex("url"));
                String picture = cursor.getString(cursor.getColumnIndex("picture"));
                String foldername = cursor.getString(cursor.getColumnIndex("foldername"));
                int downloadStatus = cursor.getInt(cursor.getColumnIndex("downloadStatus"));
                String downloadedSize = cursor.getString(cursor.getColumnIndex("downloadedSize"));
                int isfinish = cursor.getInt(cursor.getColumnIndex("isfinish"));
                String mapKey = cursor.getString(cursor.getColumnIndex("mapKey"));
                int downloadpos = cursor.getInt(cursor.getColumnIndex("downloadpos"));
                int downloadPercent = cursor.getInt(cursor.getColumnIndex("downloadPercent"));
                int downloadSpeed = cursor.getInt(cursor.getColumnIndex("downloadSpeed"));
                int treeid = cursor.getInt(cursor.getColumnIndex("treeid"));
                String treename = cursor.getString(cursor.getColumnIndex("treename"));
                String localurl = cursor.getString(cursor.getColumnIndex("localurl"));
                long currentposition = cursor.getLong(cursor.getColumnIndex("currentposition"));
                long duration = cursor.getLong(cursor.getColumnIndex("duration"));
                int randid = cursor.getInt(cursor.getColumnIndex("randid"));
                int sort = cursor.getInt(cursor.getColumnIndex("sort"));
                String reporteddata = cursor.getString(cursor.getColumnIndex("reporteddata"));
                String service_id = cursor.getString(cursor.getColumnIndex("service_id"));
                String service_name = cursor.getString(cursor.getColumnIndex("service_name"));
                String video_type = cursor.getString(cursor.getColumnIndex("video_type"));

                hashMap.put("id", String.valueOf(id));
                hashMap.put("tid", String.valueOf(urlTotal));
                hashMap.put("name", filename);
                hashMap.put("url_content", url);
                hashMap.put("picture", picture);
                hashMap.put("taskname", foldername);
                hashMap.put("downloadStatus", String.valueOf(downloadStatus));
                hashMap.put("downloadedSize", downloadedSize);
                hashMap.put("isfinish", String.valueOf(isfinish));
                hashMap.put("mapKey", mapKey);
                hashMap.put("downloadpos", String.valueOf(downloadpos));
                hashMap.put("downloadPercent", String.valueOf(downloadPercent));
                hashMap.put("downloadSpeed", String.valueOf(downloadSpeed));
                hashMap.put("treeid", String.valueOf(treeid));
                hashMap.put("treename", treename);
                hashMap.put("treepicture", localurl);
                hashMap.put("reporteddata", reporteddata);
                hashMap.put("currentposition", String.valueOf(currentposition));
                hashMap.put("duration", String.valueOf(duration));
                hashMap.put("randid", String.valueOf(randid));
                hashMap.put("sort", String.valueOf(sort));
                hashMap.put("service_id", service_id);
                hashMap.put("service_name", service_name);
                hashMap.put("video_type", video_type);

            }

        }

        if (cursor != null)
        {
            cursor.close();
        }
        database.close();
        return hashMap;

    }


    /**
     * 根据treeid查询每个课程所下载的章节,并且根据randid升序排列
     */
    public synchronized void query_DownLoad_Tree_Chapters(String treeid, ArrayList<HashMap<String, String>> groupList)
    {
        SQLiteDatabase database = getConnection();
        Cursor cursor = null;
        cursor = database.query("Download", new String[]{"urlTotal", "foldername"}, "treeid=?", new String[]{treeid}, "urlTotal", null, "randid asc");
        if (cursor != null)
        {
            while (cursor.moveToNext())
            {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("foldername", cursor.getString(cursor.getColumnIndex("foldername")));
                map.put("tid", cursor.getString(cursor.getColumnIndex("urlTotal")));
                groupList.add(map);
            }
        }

        if (cursor != null)
        {
            cursor.close();
        }
        database.close();
    }


    /**
     * 根据treeid和tid查询下载章节下的所有视频,并且根据sort升序排序
     */
    public synchronized void query_DownLoad_Tree_Videos(String treeid, String tid, ArrayList<ArrayList<HashMap<String, String>>> childList)
    {
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        SQLiteDatabase database = getConnection();
        Cursor cursor = null;
        cursor = database.query("Download", new String[]{"video_id", "urlTotal", "filename", "foldername", "url", "picture", "downloadStatus"
                , "isfinish", "downloadedSize", "downloadpos", "downloadPercent", "mapKey", "downloadSpeed", "localurl", "currentposition"
                , "reporteddata", "duration", "randid", "sort", "video_type", "is_drag"}, "treeid=? and urlTotal=? and service_id =? ", new String[]{treeid, tid, ""}, null, null, "sort asc");

        if (cursor != null)
        {
            while (cursor.moveToNext())
            {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                int id = cursor.getInt(cursor.getColumnIndex("video_id"));
                int urlTotal = cursor.getInt(cursor.getColumnIndex("urlTotal"));
                String filename = cursor.getString(cursor.getColumnIndex("filename"));
                String url = cursor.getString(cursor.getColumnIndex("url"));
                String picture = cursor.getString(cursor.getColumnIndex("picture"));
                int downloadStatus = cursor.getInt(cursor.getColumnIndex("downloadStatus"));
                String downloadedSize = cursor.getString(cursor.getColumnIndex("downloadedSize"));
                String localurl = cursor.getString(cursor.getColumnIndex("localurl"));
                int isfinish = cursor.getInt(cursor.getColumnIndex("isfinish"));
                String foldername = cursor.getString(cursor.getColumnIndex("foldername"));
                String mapKey = cursor.getString(cursor.getColumnIndex("mapKey"));
                int downloadpos = cursor.getInt(cursor.getColumnIndex("downloadpos"));
                int downloadPercent = cursor.getInt(cursor.getColumnIndex("downloadPercent"));
                int downloadSpeed = cursor.getInt(cursor.getColumnIndex("downloadSpeed"));
                long currentposition = cursor.getLong(cursor.getColumnIndex("currentposition"));
                long duration = cursor.getLong(cursor.getColumnIndex("duration"));
                String reporteddata = cursor.getString(cursor.getColumnIndex("reporteddata"));
                int randid = cursor.getInt(cursor.getColumnIndex("randid"));
                int sort = cursor.getInt(cursor.getColumnIndex("sort"));
                String video_type = cursor.getString(cursor.getColumnIndex("video_type"));
                int is_drag = cursor.getInt(cursor.getColumnIndex("is_drag"));

                hashMap.put("id", String.valueOf(id));
                hashMap.put("tid", String.valueOf(urlTotal));
                hashMap.put("name", filename);
                hashMap.put("taskname", foldername);
                hashMap.put("url_content", url);
                hashMap.put("picture", picture);
                hashMap.put("downloadStatus", String.valueOf(downloadStatus));
                hashMap.put("downloadedSize", downloadedSize);
                hashMap.put("isfinish", String.valueOf(isfinish));
                hashMap.put("mapKey", mapKey);
                hashMap.put("treepicture", localurl);
                hashMap.put("downloadpos", String.valueOf(downloadpos));
                hashMap.put("downloadPercent", String.valueOf(downloadPercent));
                hashMap.put("downloadSpeed", String.valueOf(downloadSpeed));
                hashMap.put("reporteddata", reporteddata);
                hashMap.put("currentposition", String.valueOf(currentposition));
                hashMap.put("duration", String.valueOf(duration));
                hashMap.put("randid", String.valueOf(randid));
                hashMap.put("sort", String.valueOf(sort));
                hashMap.put("showcheckbox", "0");//是否显示Checkbox
                hashMap.put("showtext", "0");//是否显示百分比textview
                hashMap.put("type", video_type);
                hashMap.put("is_drag", String.valueOf(is_drag));

                list.add(hashMap);

            }
            childList.add(list);

        }

        if (cursor != null)
        {
            cursor.close();
        }
        database.close();

    }

    /**
     * Msz 根据sercieid 查询下载特定服务下的所有视频
     */
    public synchronized void query_DownLoad_Service_Videos(String serviceId, ArrayList<HashMap<String, String>> childList)
    {
        SQLiteDatabase database = getConnection();
        Cursor cursor = null;
        cursor = database.query("Download", new String[]{"video_id", "urlTotal", "filename", "foldername", "url", "picture", "downloadStatus"
                , "isfinish", "downloadedSize", "downloadpos", "downloadPercent", "mapKey", "downloadSpeed", "localurl", "currentposition",
                "reporteddata", "duration", "randid", "sort", "service_id", "service_name", "video_type"}, "service_id=? and urlTotal =? ", new String[]{serviceId, ""}, null, null, null);

        if (cursor != null)
        {
            while (cursor.moveToNext())
            {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                int id = cursor.getInt(cursor.getColumnIndex("video_id"));
                int urlTotal = cursor.getInt(cursor.getColumnIndex("urlTotal"));
                String filename = cursor.getString(cursor.getColumnIndex("filename"));
                String url = cursor.getString(cursor.getColumnIndex("url"));
                String picture = cursor.getString(cursor.getColumnIndex("picture"));
                int downloadStatus = cursor.getInt(cursor.getColumnIndex("downloadStatus"));
                String downloadedSize = cursor.getString(cursor.getColumnIndex("downloadedSize"));
                String localurl = cursor.getString(cursor.getColumnIndex("localurl"));
                int isfinish = cursor.getInt(cursor.getColumnIndex("isfinish"));
                String foldername = cursor.getString(cursor.getColumnIndex("foldername"));
                String mapKey = cursor.getString(cursor.getColumnIndex("mapKey"));
                int downloadpos = cursor.getInt(cursor.getColumnIndex("downloadpos"));
                int downloadPercent = cursor.getInt(cursor.getColumnIndex("downloadPercent"));
                int downloadSpeed = cursor.getInt(cursor.getColumnIndex("downloadSpeed"));
                long currentposition = cursor.getLong(cursor.getColumnIndex("currentposition"));
                long duration = cursor.getLong(cursor.getColumnIndex("duration"));
                String reporteddata = cursor.getString(cursor.getColumnIndex("reporteddata"));
                int randid = cursor.getInt(cursor.getColumnIndex("randid"));
                int sort = cursor.getInt(cursor.getColumnIndex("sort"));
                String service_id = cursor.getString(cursor.getColumnIndex("service_id"));
                String service_name = cursor.getString(cursor.getColumnIndex("service_name"));
                String video_type = cursor.getString(cursor.getColumnIndex("video_type"));

                hashMap.put("id", String.valueOf(id));
                hashMap.put("tid", String.valueOf(urlTotal));
                hashMap.put("name", filename);
                hashMap.put("taskname", foldername);
                hashMap.put("url_content", url);
                hashMap.put("picture", picture);
                hashMap.put("downloadStatus", String.valueOf(downloadStatus));
                hashMap.put("downloadedSize", downloadedSize);
                hashMap.put("isfinish", String.valueOf(isfinish));
                hashMap.put("mapKey", mapKey);
                hashMap.put("treepicture", localurl);
                hashMap.put("downloadpos", String.valueOf(downloadpos));
                hashMap.put("downloadPercent", String.valueOf(downloadPercent));
                hashMap.put("downloadSpeed", String.valueOf(downloadSpeed));
                hashMap.put("reporteddata", reporteddata);
                hashMap.put("currentposition", String.valueOf(currentposition));
                hashMap.put("duration", String.valueOf(duration));
                hashMap.put("randid", String.valueOf(randid));
                hashMap.put("sort", String.valueOf(sort));
                hashMap.put("showcheckbox", "0");//是否显示Checkbox
                hashMap.put("showtext", "0");//是否显示百分比textview
                hashMap.put("type", "video");//下载列表的视频 默认全是video类型
                hashMap.put("service_id", service_id);
                hashMap.put("service_name", service_name);
                hashMap.put("video_type", video_type);

                childList.add(hashMap);

            }

        }

        if (cursor != null)
        {
            cursor.close();
        }
        database.close();

    }

    /**
     * 查询所有下载的课程视频,根据treeid分组
     */
    public synchronized ArrayList<HashMap<String, String>> queryAllDownLoadCourse()
    {
        ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String, String>>();
        SQLiteDatabase database = getConnection();
        Cursor cursor = database.query("Download", new String[]{"treeid", "treename", "localurl"}, "service_id =? ", new String[]{""}, "treeid", null, null);

        if (cursor != null)
        {
            while (cursor.moveToNext())
            {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                int treeid = cursor.getInt(cursor.getColumnIndex("treeid"));
                String treename = cursor.getString(cursor.getColumnIndex("treename"));
                String localurl = cursor.getString(cursor.getColumnIndex("localurl"));
                hashMap.put("treeid", String.valueOf(treeid));
                hashMap.put("treename", treename);
                hashMap.put("treepicture", localurl);
                arrayList.add(hashMap);
            }

        }

        if (cursor != null)
        {
            cursor.close();
        }
        database.close();
        return arrayList;
    }

    /**
     * Msz 查询所有下载的服务视频,根据serviceId分组
     */
    public synchronized ArrayList<HashMap<String, String>> queryAllDownLoadService()
    {
        ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String, String>>();
        SQLiteDatabase database = getConnection();
        //urlTotal 章节id
        Cursor cursor = database.query("Download", new String[]{"service_id", "service_name", "localurl"}, "urlTotal=?",
                new String[]{""}, "service_id", null, null);

        if (cursor != null)
        {
            while (cursor.moveToNext())
            {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                String service_id = cursor.getString(cursor.getColumnIndex("service_id"));
                String service_name = cursor.getString(cursor.getColumnIndex("service_name"));
                String localurl = cursor.getString(cursor.getColumnIndex("localurl"));
                hashMap.put("service_id", service_id);
                hashMap.put("service_name", service_name);
                hashMap.put("service_picture", localurl);
                arrayList.add(hashMap);
            }

        }

        if (cursor != null)
        {
            cursor.close();
        }
        database.close();
        return arrayList;
    }


    /**
     * 根据treeid查询所有的视频以及每个视频的大小
     */
    public synchronized ArrayList<HashMap<String, String>> query_All_videoId(String treeid)
    {
        ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String, String>>();
        SQLiteDatabase database = getConnection();
        Cursor cursor = database.query("Download", new String[]{"video_id", "downloadStatus", "downloadedSize"}, "treeid = ?", new String[]{treeid}, null, null, null);

        if (cursor != null)
        {
            while (cursor.moveToNext())
            {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                int video_id = cursor.getInt(cursor.getColumnIndex("video_id"));
                int downloadStatus = cursor.getInt(cursor.getColumnIndex("downloadStatus"));
                String downloadedSize = cursor.getString(cursor.getColumnIndex("downloadedSize"));

                hashMap.put("treeid", String.valueOf(video_id));
                hashMap.put("downloadStatus", String.valueOf(downloadStatus));
                hashMap.put("downloadedSize", downloadedSize);
                arrayList.add(hashMap);
            }

        }

        if (cursor != null)
        {
            cursor.close();
        }
        database.close();
        return arrayList;
    }

    /**
     * 根据serviceId查询所有的视频以及每个视频的大小
     */
    public synchronized ArrayList<HashMap<String, String>> queryServiceVideoSize(String serciceId)
    {
        ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String, String>>();
        SQLiteDatabase database = getConnection();
        Cursor cursor = database.query("Download", new String[]{"video_id", "downloadStatus", "downloadedSize"}, "service_id = ?", new String[]{serciceId}, null, null, null);

        if (cursor != null)
        {
            while (cursor.moveToNext())
            {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                int video_id = cursor.getInt(cursor.getColumnIndex("video_id"));
                int downloadStatus = cursor.getInt(cursor.getColumnIndex("downloadStatus"));
                String downloadedSize = cursor.getString(cursor.getColumnIndex("downloadedSize"));

                hashMap.put("treeid", String.valueOf(video_id));
                hashMap.put("downloadStatus", String.valueOf(downloadStatus));
                hashMap.put("downloadedSize", downloadedSize);
                arrayList.add(hashMap);
            }

        }

        if (cursor != null)
        {
            cursor.close();
        }
        database.close();
        return arrayList;
    }


    /**
     * 根据tid和id查询要继续下载的视频的 mapkey和downloadedSize
     */
    public synchronized HashMap<String, String> queryCourseDownLoadingVideo(String tid, String id)
    {
        return queryDownLoadingVideo(tid, id, "");
    }

    /**
     * 查询正在下载的服务视频 treeId默认传递空
     *
     * @param serviceId
     * @param videoId
     * @return
     */
    public synchronized HashMap<String, String> queryServiceDownLoadingVideo(String serviceId, String videoId)
    {
        return queryDownLoadingVideo("", videoId, serviceId);
    }

    public synchronized HashMap<String, String> queryDownLoadingVideo(String tid, String id, String serviceId)
    {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        SQLiteDatabase database = getConnection();
        Cursor cursor = database.query("Download", new String[]{"mapkey", "downloadedSize", "video_type"}, "urlTotal=? and video_id=? and service_id=?", new String[]{tid, id, serviceId}, null, null, null);
        if (cursor != null)
        {
            while (cursor.moveToNext())
            {
                String mapKey = cursor.getString(cursor.getColumnIndex("mapKey"));
                String downloadedSize = cursor.getString(cursor.getColumnIndex("downloadedSize"));
                String video_type = cursor.getString(cursor.getColumnIndex("video_type"));
                hashMap.put("mapkey", mapKey);
                hashMap.put("downloadedSize", downloadedSize);
                hashMap.put("video_type", video_type);
            }
        }

        if (cursor != null)
        {
            cursor.close();
        }
        database.close();
        return hashMap;
    }

    public synchronized List<String> queryAllDownLoadedVideo(String serviceId)
    {
        List<String> hashMap = new ArrayList<>();
        SQLiteDatabase database = getConnection();
        Cursor cursor = database.query("Download", new String[]{"video_id", "downloadedSize"}, "service_id=? and downloadStatus=?", new String[]{serviceId, "3"}, null, null, null);
        if (cursor != null)
        {
            while (cursor.moveToNext())
            {
                String video_id = cursor.getString(cursor.getColumnIndex("video_id"));
                hashMap.add(video_id);
            }
        }

        if (cursor != null)
        {
            cursor.close();
        }
        database.close();
        return hashMap;
    }


    public synchronized String queryVideoUrl(String videoId)
    {
        String video_id = "";
        SQLiteDatabase database = getConnection();
        Cursor cursor = database.query("Download", new String[]{"url"}, "video_id=?", new String[]{videoId}, null, null, null);
        if (cursor != null)
        {
            while (cursor.moveToNext())
            {
                video_id = cursor.getString(cursor.getColumnIndex("url"));
            }
        }

        if (cursor != null)
        {
            cursor.close();
        }
        database.close();
        return video_id;
    }


    /**
     * 查询需要上报观看进度的视频
     */
//	 public synchronized ArrayList<HashMap<String, String>> query_DownLoad_Reported_Data(){
//		  ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String,String>>();
//		  SQLiteDatabase database = getConnection();
//		 
//		  Cursor cursor = database.query("Download", new String[]{"video_id","urlTotal","filename","url","picture","foldername","downloadStatus"
//			         ,"isfinish","downloadedSize","downloadpos","downloadPercent","mapKey","downloadSpeed","treeid","currentposition","treename","localurl","reporteddata","duration"}, "isfinish=? and reporteddata=?", new String[]{"1","0"}, null, null, "_id desc");
//		 
//		  if(cursor!=null){
//			  while(cursor.moveToNext()){
//					 HashMap<String, String> hashMap = new HashMap<String, String>();
//					 int _id = cursor.getInt(cursor.getColumnIndex("video_id"));
//					 int urlTotal = cursor.getInt(cursor.getColumnIndex("urlTotal"));
//					 String filename = cursor.getString(cursor.getColumnIndex("filename"));
//					 String url = cursor.getString(cursor.getColumnIndex("url"));
//					 String picture = cursor.getString(cursor.getColumnIndex("picture"));
//					 String foldername = cursor.getString(cursor.getColumnIndex("foldername"));
//					 int downloadStatus = cursor.getInt(cursor.getColumnIndex("downloadStatus"));
//					 String downloadedSize = cursor.getString(cursor.getColumnIndex("downloadedSize"));
//					 int isfinish = cursor.getInt(cursor.getColumnIndex("isfinish"));
//					 String mapKey = cursor.getString(cursor.getColumnIndex("mapKey"));
//					 int downloadpos = cursor.getInt(cursor.getColumnIndex("downloadpos"));
//					 int downloadPercent = cursor.getInt(cursor.getColumnIndex("downloadPercent"));
//					 int downloadSpeed = cursor.getInt(cursor.getColumnIndex("downloadSpeed"));
//					 int treeid = cursor.getInt(cursor.getColumnIndex("treeid"));
//					 String treename = cursor.getString(cursor.getColumnIndex("treename"));
//					 String localurl = cursor.getString(cursor.getColumnIndex("localurl"));
//					 int currentposition = cursor.getInt(cursor.getColumnIndex("currentposition"));
//					 int duration = cursor.getInt(cursor.getColumnIndex("duration"));
//					 String reporteddata = cursor.getString(cursor.getColumnIndex("reporteddata"));
//					 
//					 hashMap.put("id", String.valueOf(_id));
//					 hashMap.put("tid", String.valueOf(urlTotal));
//					 hashMap.put("name",filename);
//					 hashMap.put("url_content", url);
//					 hashMap.put("picture", picture);
//					 hashMap.put("taskname",foldername);
//					 hashMap.put("downloadStatus",String.valueOf(downloadStatus));
//					 hashMap.put("downloadedSize",downloadedSize);
//					 hashMap.put("isfinish",String.valueOf(isfinish));
//					 hashMap.put("mapKey",mapKey);
//					 hashMap.put("downloadpos",String.valueOf(downloadpos));
//					 hashMap.put("downloadPercent",String.valueOf(downloadPercent));
//					 hashMap.put("downloadSpeed",String.valueOf(downloadSpeed));
//					 hashMap.put("treeid",String.valueOf(treeid));
//					 hashMap.put("treename",treename);
//					 hashMap.put("treepicture",localurl);
//					 hashMap.put("reporteddata",reporteddata);
//					 hashMap.put("currentposition",String.valueOf(currentposition));
//					 hashMap.put("duration",String.valueOf(duration));
//					 
//					 arrayList.add(hashMap);
//			  }
//			  
//		  }
//		  
//		  if (cursor != null) {
//				cursor.close();
//			}
//			database.close();	
//		   return arrayList;
//		  
//	 }


    /**
     * 下载完成更新数据库信息
     */
    public synchronized void updata_DownloagIng_Finsh(String id, String url, int downloadedSize)
    {
        SQLiteDatabase database = getConnection();
        ContentValues contentValues = new ContentValues();
        contentValues.put("isfinish", 1);
        contentValues.put("url", url);
        contentValues.put("downloadedSize", downloadedSize);
        contentValues.put("downloadStatus", 3);
        database.update("Download", contentValues, "video_id=?", new String[]{id});
        database.close();

    }


    /**
     * 根据videoid更新下载状态为等待状态 默认的是课程章节的视频
     *
     * @param videoId 视频id
     *                serviceId 服务id
     */
    public synchronized void updata_DownStatus(String videoId, int status)
    {
        updata_DownStatus(videoId, "", status);
    }

    public synchronized void updata_DownStatus(String videoId, String serviceId, int status)
    {
        SQLiteDatabase database = getConnection();
        ContentValues contentValues = new ContentValues();
        contentValues.put("downloadStatus", status);
        database.update("Download", contentValues, "video_id=? and service_id=?", new String[]{videoId, serviceId});
        database.close();
    }


    /**
     * 根据课程id更新所有视频下载状态为等待状态
     */

    public synchronized void updata_AllDownStatus(String treeid, int status)
    {
        SQLiteDatabase database = getConnection();
        ContentValues contentValues = new ContentValues();
        contentValues.put("downloadStatus", status);
        database.update("Download", contentValues, "treeid=? and downloadStatus!=?", new String[]{treeid, "3"});
        database.close();
    }

    public synchronized void updata_AllDownStatusOfService(String serviceId, int status)
    {
        SQLiteDatabase database = getConnection();
        ContentValues contentValues = new ContentValues();
        contentValues.put("downloadStatus", status);
        database.update("Download", contentValues, "service_id=? and downloadStatus!=?", new String[]{serviceId, "3"});
        database.close();
    }

    /**
     * 查询所有下载状态
     */
    public synchronized ArrayList<String> query_All_DownStatus()
    {

        SQLiteDatabase database = getConnection();
        Cursor cursor = database.query("Download", new String[]{"downloadStatus"}, null, null, null, null, null);
        ArrayList<String> arrayList = new ArrayList<String>();
        if (cursor != null)
        {
            while (cursor.moveToNext())
            {
                String downloadStatus = cursor.getString(cursor.getColumnIndex("downloadStatus"));
                arrayList.add(downloadStatus);
            }

        }

        if (cursor != null)
        {
            cursor.close();
        }
        database.close();

        return arrayList;
    }


    /**
     * 查询所有下载中视频的百分比
     */
    public synchronized ArrayList<String> query_All_DownPercent()
    {

        SQLiteDatabase database = getConnection();
        Cursor cursor = database.query("Download", new String[]{"downloadPercent"}, "isfinish!=1", null, null, null, null);
        ArrayList<String> arrayList = new ArrayList<String>();
        if (cursor != null)
        {
            while (cursor.moveToNext())
            {
                String downloadPercent = cursor.getString(cursor.getColumnIndex("downloadPercent"));
                arrayList.add(downloadPercent);
            }

        }

        if (cursor != null)
        {
            cursor.close();
        }
        database.close();

        return arrayList;
    }


    /**
     * 根据tid查询对应的所有video_id
     */
//  	 public ArrayList<String> query_tid_all_ids(String tid){
//  		 
//  		   ArrayList<String> arrayList = new ArrayList<String>();
//		   SQLiteDatabase database = getConnection();
//		   Cursor cursor = database.query("Download", new String[]{"video_id"}, "urlTotal=?", new String[]{tid}, null, null, null);
//	
//		 if(cursor!=null){
//			 while(cursor.moveToNext()){
//				 String video_id = cursor.getString(cursor.getColumnIndex("video_id"));
//				 arrayList.add(video_id);
//	    	 }
//		 }
//		 
//		 if(cursor!=null){
//			 cursor.close();
//		 }
//		 database.close();
//		 
//		return arrayList;
//	 }


    /**
     * 根据id删除数据
     */

    public synchronized void delete_item(String id)
    {
        delete_item(id, "");
    }

    public synchronized void delete_item(String id, String serviceId)
    {
        SQLiteDatabase database = getConnection();
        database.delete("Download", "video_id=? and service_id=?", new String[]{id, serviceId});
        database.close();
    }


    /**
     * 根据id查询视频名字
     */
    public synchronized String query_item_filename(String id)
    {
        String filenames = null;
        SQLiteDatabase database = getConnection();
        Cursor cursor = database.query("Download", new String[]{"filename"}, "video_id=?", new String[]{id}, null, null, null);
        if (cursor != null)
        {
            if (cursor.moveToNext())
            {
                filenames = cursor.getString(cursor.getColumnIndex("filename"));
            }

        }

        if (cursor != null)
        {
            cursor.close();
        }
        database.close();


        return filenames;

    }


    /**
     * 更新视频最后观看的位置和视频总时长
     */
//	 public synchronized void updateCurrentPosition(String id,String tid, int currentposition,int duration){
//		//根据视频id更新数据库视频完成度信息
//		 SQLiteDatabase database = getConnection();
//		 ContentValues contentValues = new ContentValues();
//		 contentValues.put("currentposition", currentposition);
//		 contentValues.put("duration", duration);
//		 database.update("Download", contentValues,"video_id=? and urlTotal=?", new String[]{id,tid});
//		 database.close();
//	 }


    /**
     * 查询视频最后的观看位置
     */
//	 public long quaryCurrentPosition(String id,String tid){
//		 long currentposition = 0;
//		 SQLiteDatabase database = getConnection();
//		 Cursor cursor = database.query("Download", new String[]{"currentposition"}, "video_id=? and urlTotal=?", new String[]{id,tid}, null, null, null); 
//		   if(cursor!=null){
//				 if(cursor.moveToNext()){
//					 currentposition = cursor.getLong(cursor.getColumnIndex("currentposition"));
//	    	   }
//				 
//			}
//		 
//		   if(cursor!=null){
//				 cursor.close();
//			 }
//			 database.close();
//			 
//		return currentposition;
//	 }


    /**
     * @param isfinish_lv
     * @param key_id
     * @return
     * 查询下载列表 大于下载完成id的所有视频
     */
//	 public synchronized ArrayList<HashMap<String, String>> query_Next_DownLoad_Ing(String isfinish_lv,String key_id){
//		  ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String,String>>();
//		  SQLiteDatabase database = getConnection();
//		  Cursor cursor = null;
//		  
//		   cursor = database.query("Download", new String[]{"_id","video_id","urlTotal","filename","url","picture","foldername","downloadStatus"
//			         ,"isfinish","downloadedSize","downloadpos","downloadPercent","mapKey","downloadSpeed","treeid"}, "isfinish!=? and _id>?", new String[]{isfinish_lv,key_id}, null, null, null);
//			
//		  if(cursor!=null){
//			  while(cursor.moveToNext()){
//				 HashMap<String, String> hashMap = new HashMap<String, String>();
//				 int _id = cursor.getInt(cursor.getColumnIndex("video_id"));
//				 int urlTotal = cursor.getInt(cursor.getColumnIndex("urlTotal"));
//				 String filename = cursor.getString(cursor.getColumnIndex("filename"));
//				 String url = cursor.getString(cursor.getColumnIndex("url"));
//				 String picture = cursor.getString(cursor.getColumnIndex("picture"));
//				 String foldername = cursor.getString(cursor.getColumnIndex("foldername"));
//				 int downloadStatus = cursor.getInt(cursor.getColumnIndex("downloadStatus"));
//				 String downloadedSize = cursor.getString(cursor.getColumnIndex("downloadedSize"));
//				 int isfinish = cursor.getInt(cursor.getColumnIndex("isfinish"));
//				 String mapKey = cursor.getString(cursor.getColumnIndex("mapKey"));
//				 int downloadpos = cursor.getInt(cursor.getColumnIndex("downloadpos"));
//				 int downloadPercent = cursor.getInt(cursor.getColumnIndex("downloadPercent"));
//				 int downloadSpeed = cursor.getInt(cursor.getColumnIndex("downloadSpeed"));
//				 int treeid = cursor.getInt(cursor.getColumnIndex("treeid"));
//				 
//				 hashMap.put("id", String.valueOf(_id));
//				 hashMap.put("tid", String.valueOf(urlTotal));
//				 hashMap.put("name",filename);
//				 hashMap.put("url_content", url);
//				 hashMap.put("picture", picture);
//				 hashMap.put("taskname",foldername);
//				 hashMap.put("downloadStatus",String.valueOf(downloadStatus));
//				 hashMap.put("downloadedSize",downloadedSize);
//				 hashMap.put("isfinish",String.valueOf(isfinish));
//				 hashMap.put("mapKey",mapKey);
//				 hashMap.put("downloadpos",String.valueOf(downloadpos));
//				 hashMap.put("downloadPercent",String.valueOf(downloadPercent));
//				 hashMap.put("downloadSpeed",String.valueOf(downloadSpeed));
//				 hashMap.put("treeid",String.valueOf(treeid));
//				  
//				 arrayList.add(hashMap);
//			  }
//			  
//		  }
//		  
//		if (cursor != null) {
//			cursor.close();
//		}
//		database.close();	
//	   return arrayList;
//		 
//	 }


    /**
     * 根据课程id更新下载完成一门课程的持续播放毫秒值
     */

    public synchronized void upateTreedCorseMs(String treeid, long recordingtime)
    {
        SQLiteDatabase database = getConnection();
        ContentValues contentValues = new ContentValues();
        contentValues.put("recordingtime", recordingtime);
        database.update("Download", contentValues, "treeid=? and isfinish=?", new String[]{treeid, "1"});
        database.close();
    }


    /**
     * 根据课程id查询下载完成的一门课程持续播放毫秒值，用以持续计时
     */
//	 public long quaryTreedCorseMs(String treeid){
//		 long recordingtime = 0;
//		 SQLiteDatabase database = getConnection();
//		 Cursor cursor = database.query("Download", new String[]{"recordingtime"}, "treeid=? and isfinish=?", new String[]{treeid,"1"}, null, null, null); 
//		   if(cursor!=null){
//				 if(cursor.moveToNext()){
//					 recordingtime = cursor.getLong(cursor.getColumnIndex("recordingtime"));
//	    	   }
//				 
//			}
//		 
//		   if(cursor!=null){
//				 cursor.close();
//			 }
//			 database.close();
//			 
//		return recordingtime;
//	 }


    /**
     * 根据课程id更新下载完成的一门课程持续播放时间
     */

    public synchronized void upateTreedCorseTime(String treeid, int recordingTime_Int)
    {
        SQLiteDatabase database = getConnection();
        ContentValues contentValues = new ContentValues();
        contentValues.put("continue_playing_time", recordingTime_Int);
        database.update("Download", contentValues, "treeid=? and isfinish=?", new String[]{treeid, "1"});
        database.close();
    }


    /**
     * 根据课程id查询下载完成的一门课程持续播放时间
     */
    public synchronized int quaryTreedCorseTime(String treeid)
    {
        int play_time_value = 0;
        SQLiteDatabase database = getConnection();
        Cursor cursor = database.query("Download", new String[]{"continue_playing_time"}, "treeid=? and isfinish=?", new String[]{treeid, "1"}, null, null, null);
        if (cursor != null)
        {
            if (cursor.moveToNext())
            {
                play_time_value = cursor.getInt(cursor.getColumnIndex("continue_playing_time"));
            }

        }

        if (cursor != null)
        {
            cursor.close();
        }
        database.close();

        return play_time_value;
    }

    /**
     * 根据tid和id查询下载视频的url
     */

    public synchronized String quaryOfflineUrl(String tid, String id)
    {
        String offlineUrl = null;
        SQLiteDatabase database = getConnection();
        Cursor cursor = database.query("Download", new String[]{"url"}, "urlTotal=? and video_id=? and downloadStatus=?", new String[]{tid, id, "3"}, null, null, null);

        if (cursor != null)
        {
            if (cursor.moveToNext())
            {
                offlineUrl = cursor.getString(cursor.getColumnIndex("url"));
            }
        }

        if (cursor != null)
        {
            cursor.close();
        }
        database.close();

        return offlineUrl;

    }

    /**
     * @param tid
     * @param id  感觉tid id返回下载进度
     */
    public synchronized String quaryDownloadpos(String tid, String id)
    {
        String downloadpos = null;
        SQLiteDatabase database = getConnection();
        Cursor cursor = database.query("Download", new String[]{"downloadpos"}, "urlTotal=? and video_id=?", new String[]{tid, id}, null, null, null);

        if (cursor != null)
        {
            if (cursor.moveToNext())
            {
                downloadpos = cursor.getString(cursor.getColumnIndex("downloadpos"));
            }
        }

        if (cursor != null)
        {
            cursor.close();
        }
        database.close();

        return downloadpos;

    }

    /**
     * 根据serviceid和id查询下载视频的url
     */

    public synchronized String quaryServiceOfflineUrl(String serviceId, String id)
    {
        String offlineUrl = null;
        SQLiteDatabase database = getConnection();
        Cursor cursor = database.query("Download", new String[]{"url"}, "service_id=? and video_id=? and downloadStatus=?", new String[]{serviceId, id, "3"}, null, null, null);

        if (cursor != null)
        {
            if (cursor.moveToNext())
            {
                offlineUrl = cursor.getString(cursor.getColumnIndex("url"));
            }
        }

        if (cursor != null)
        {
            cursor.close();
        }
        database.close();

        return offlineUrl;

    }


//    /**
//     * im 根据 用户id 获取用户信息
//     */
//    public  UserInfoBean getUserInfo(String id) {
//        L.d("im userinfo id = " + id);
//        UserInfoBean userInfo = null;
//        if (id == null) {
//            return userInfo;
//        }
//        SQLiteDatabase database = getConnection();
//        Cursor cursor = database.query(DBHelper.IMUSER, new String[]{UserInfoBean.IDENTIFIER, UserInfoBean.NICKNAME, UserInfoBean.FACEURL}, UserInfoBean.IDENTIFIER + "=?", new String[]{id}, null, null, null);
//        if (cursor != null) {
//            if (cursor.moveToNext()) {
//                userInfo = new UserInfoBean();
//                userInfo.setIdentifier(cursor.getString(cursor.getColumnIndex(UserInfoBean.IDENTIFIER)));
//                userInfo.setNick(cursor.getString(cursor.getColumnIndex(UserInfoBean.NICKNAME)));
//                userInfo.setUserFace(cursor.getString(cursor.getColumnIndex(UserInfoBean.FACEURL)));
//            }
//            cursor.close();
//        }
//        database.close();
//        return userInfo;
//    }
//
//    // 添加 im 用户
//    public Long insertUserInfo(TIMUserProfile user) {
//        SQLiteDatabase database = getConnection();
//        ContentValues values = new ContentValues();
//        values.put(UserInfoBean.IDENTIFIER, user.getIdentifier());
//        values.put(UserInfoBean.NICKNAME, user.getNickName());
//        values.put(UserInfoBean.FACEURL, user.getFaceUrl());
//        Long result = database.insert(DBHelper.IMUSER, user.getIdentifier(), values);
//        L.d("Im insertUserInfo result = " + result);
//        database.close();
//        return result;
//    }
//
//    // 更新 im users表
//    public int updateUserInfo(TIMUserProfile user) {
//        SQLiteDatabase database = getConnection();
//        ContentValues values = new ContentValues();
//        values.put(UserInfoBean.NICKNAME, user.getNickName());
//        values.put(UserInfoBean.FACEURL, user.getFaceUrl());
//        int id = database.update(DBHelper.IMUSER, values, UserInfoBean.IDENTIFIER + "=?", new String[]{user.getIdentifier()});
//        L.d("Im updateUserInfo id = " + id);
//        database.close();
//        return id;
//    }
//
//
//    /**
//     * im 根据 用户id 获取昵称
//     */
//    public String getNickName(String id) {
//        String nickName = "";
//        SQLiteDatabase database = getConnection();
//        Cursor cursor = database.query(DBHelper.IMUSER, new String[]{UserInfoBean.NICKNAME}, UserInfoBean.IDENTIFIER + " = ?", new String[]{id}, null, null, null);
//
//        if (cursor != null) {
//            if (cursor.moveToNext()) {
//                nickName = cursor.getString(cursor.getColumnIndex(UserInfoBean.NICKNAME));
//            }
//            cursor.close();
//        }
//        L.d("Im getNickName nickName = " + nickName);
//        database.close();
//        return nickName;
//    }
//
//    /**
//     * im 根据 用户id 获取face
//     */
//    public String getFaceUrl(String id) {
//        String faceUrl = "";
//        SQLiteDatabase database = getConnection();
//        Cursor cursor = database.query(DBHelper.IMUSER, new String[]{UserInfoBean.FACEURL}, UserInfoBean.IDENTIFIER + " = ?", new String[]{id}, null, null, null);
//
//        if (cursor != null) {
//            if (cursor.moveToNext()) {
//                faceUrl = cursor.getString(cursor.getColumnIndex(UserInfoBean.FACEURL));
//            }
//            cursor.close();
//        }
//        L.d("Im getNickName faceUrl = " + faceUrl);
//        database.close();
//        return faceUrl;
//    }
//
//
//    /**
//     * 获取免打扰的用户id,
//     *
//     * @return ids 1,1,1,
//     */
//    public String disturbIds() {
//        String disturbIds = "";
//        SQLiteDatabase database = getConnection();
//        Cursor cursor = database.query(DBHelper.NODISTURB, new String[]{UserInfoBean.IDENTIFIER}, null, null, null, null, null);
//        if (cursor != null) {
//            while (cursor.moveToNext()) {
//                String id = cursor.getString(cursor.getColumnIndex(UserInfoBean.IDENTIFIER));
//                disturbIds = disturbIds + id + ",";
//            }
//        }
//
//        if (cursor != null) {
//            cursor.close();
//        }
//
//        L.d("Im  disturbIds = " + disturbIds);
//        database.close();
//        return disturbIds;
//    }
//
//    /**
//     * 获取最近联系人 isRecentContact 1 是最近连续 0不是最近联系
//     *
//     * @return
//     */
//    public List<UserInfoBean> getRecentContacts() {
//        List<UserInfoBean> recentContacts = new ArrayList<>();
//        SQLiteDatabase database = getConnection();
//        Cursor cursor = database.query(DBHelper.IMUSER, new String[]{UserInfoBean.IDENTIFIER, UserInfoBean.NICKNAME, UserInfoBean.FACEURL}, UserInfoBean.ISRECENTCONTACT + "=?", new String[]{"1"}, null, null, null);
//        if (cursor != null) {
//            if (cursor.moveToNext()) {
//                UserInfoBean userInfo = new UserInfoBean();
//                userInfo.setIdentifier(cursor.getString(cursor.getColumnIndex(UserInfoBean.IDENTIFIER)));
//                userInfo.setNick(cursor.getString(cursor.getColumnIndex(UserInfoBean.NICKNAME)));
//                userInfo.setUserFace(cursor.getString(cursor.getColumnIndex(UserInfoBean.FACEURL)));
//                recentContacts.add(userInfo);
//            }
//            cursor.close();
//        }
//        database.close();
//        L.d("recnet contact size = "+recentContacts.size());
//        return recentContacts;
//    }
//
//    /**
//     * 根据关键字搜索联系人
//     *
//     * @param resultString
//     * @return
//     */
//    public List<UserInfoBean> getSearchListOfChat(String resultString) {
//        List<UserInfoBean> recentContacts = new ArrayList<>();
//        SQLiteDatabase database = getConnection();
//        Cursor cursor = database.query(DBHelper.IMUSER, new String[]{UserInfoBean.IDENTIFIER, UserInfoBean.NICKNAME, UserInfoBean.FACEURL}, UserInfoBean.NICKNAME + "=?", new String[]{"%" + resultString + "%"}, null, null, null);
//        if (cursor != null) {
//            if (cursor.moveToNext()) {
//                UserInfoBean userInfo = new UserInfoBean();
//                userInfo.setIdentifier(cursor.getString(cursor.getColumnIndex(UserInfoBean.IDENTIFIER)));
//                userInfo.setNick(cursor.getString(cursor.getColumnIndex(UserInfoBean.NICKNAME)));
//                userInfo.setUserFace(cursor.getString(cursor.getColumnIndex(UserInfoBean.FACEURL)));
//                recentContacts.add(userInfo);
//            }
//            cursor.close();
//        }
//        database.close();
//        return recentContacts;
//    }
//
//    /**
//     * 清除最近联系人
//     * 把recentContact都设为0
//     */
//    public void clearRecentContacts() {
//        SQLiteDatabase database = getConnection();
//        ContentValues values = new ContentValues();
//        values.put(UserInfoBean.ISRECENTCONTACT, "0");
//        int id = database.update(DBHelper.IMUSER, values, UserInfoBean.ISRECENTCONTACT + "=?", new String[]{"1"});
//        database.close();
//        L.d("clear recent contacts result = " + id);
//    }
//
//    /**
//     * 添加最近联系人
//     *
//     * @param user
//     */
//    public void addRecentContact(UserInfoBean user) {
//        SQLiteDatabase database = getConnection();
//        ContentValues values = new ContentValues();
//        values.put(UserInfoBean.ISRECENTCONTACT, "1");
//        int id = database.update(DBHelper.IMUSER, values, UserInfoBean.IDENTIFIER + "=?", new String[]{user.getIdentifier()});
//        database.close();
//        L.d("add recent contacts result = " + id);
//    }
}

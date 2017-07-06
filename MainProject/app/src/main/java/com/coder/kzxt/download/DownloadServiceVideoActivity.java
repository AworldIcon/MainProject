package com.coder.kzxt.download;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.coder.kzxt.activity.R;
import com.coder.kzxt.db.DataBaseDao;
import com.coder.kzxt.dialog.util.MyPublicDialog;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.FileUtil;
import com.coder.kzxt.utils.PlayVideoServer;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.coder.kzxt.video.activity.VideoViewActivity;
import com.coder.kzxt.video.beans.CatalogueBean;

import org.xutils.common.Callback;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import static com.coder.kzxt.utils.Constants.DOWNLOAD_KEY;
import static com.coder.kzxt.utils.Constants.DOWNLOAD_M3U8;
import static com.coder.kzxt.utils.Constants.DOWNLOAD_URLS;
import static com.coder.kzxt.utils.Constants.M3U8;
import static com.coder.kzxt.utils.Constants.M3U8_KEY;
import static com.coder.kzxt.utils.Constants.M3U8_URLS;
import static com.coder.kzxt.utils.Constants.MAIN_FOLDER;
import static com.coder.kzxt.utils.Constants.VIDEOS;

public class DownloadServiceVideoActivity extends BaseActivity
{
    private Toolbar mToolbarView;
    private SharedPreferencesUtil spu;
    //下载类
    private DownloadManager downloadManager;
    private FileUtil fileUtil;
    private String service_id;
    private String service_name;

    private String path_key;
    private String path_urls;
    private String path_m3u8;

    private String path_urls_second;
    private String path_m3u8_second;
    private String path_key_second;

    private RelativeLayout all_operation_ly;
    private LinearLayout all_begin_ly;
    private LinearLayout all_pause_ly;
    private ListView listView;
    private ViewGroup mSelectionMenuView;
    private View bottom_fenge;
    private Button selection_all;
    private Button selection_delete;
    private DownloadServiceListAdapter adapter;

    private int checkNum = 0; // 记录选中的条目数量
    private Dialog dialog;
    private MyReceiver receiver;

    private ArrayList<HashMap<String, String>> childList;
    private ArrayList<String> downloadStatusList;//此课程下所以视频的下载状态
    private ArrayList<CatalogueBean.ItemsBean.VideoBean> selectList;
    private boolean selectAll;
    private boolean showCheckbox;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_down_load_service_video);
        spu = new SharedPreferencesUtil(this);
        downloadManager = DownloadService.getDownloadManager(DownloadServiceVideoActivity.this);
        fileUtil = new FileUtil(this);

        childList = new ArrayList<>();
        downloadStatusList = new ArrayList<>();
        selectList = new ArrayList<>();

        service_id = getIntent().getStringExtra("service_id");
        service_name = getIntent().getStringExtra("service_name");

        mToolbarView = (Toolbar) findViewById(R.id.toolbar);
        mToolbarView.setTitle(service_name);
        setSupportActionBar(mToolbarView);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        path_urls = getExternalFilesDir(null) + DOWNLOAD_URLS;
        path_m3u8 = getExternalFilesDir(null) + DOWNLOAD_M3U8;
        path_key = getExternalFilesDir(null) + DOWNLOAD_KEY;
        String secondSdcard = spu.getSecondSdcard();
        if (!TextUtils.isEmpty(secondSdcard))
        {
            if (Constants.API_LEVEL_19)
            {
                path_urls_second = secondSdcard + "/Android/data/" + getPackageName() + MAIN_FOLDER + VIDEOS + M3U8_URLS;
                path_m3u8_second = secondSdcard + "/Android/data/" + getPackageName() + MAIN_FOLDER + VIDEOS + M3U8;
                path_key_second = secondSdcard + "/Android/data/" + getPackageName() + MAIN_FOLDER + VIDEOS + M3U8_KEY;
            } else
            {
                path_urls_second = secondSdcard + MAIN_FOLDER + VIDEOS + M3U8_URLS;
                path_m3u8_second = secondSdcard + MAIN_FOLDER + VIDEOS + M3U8;
                path_key_second = secondSdcard + MAIN_FOLDER + VIDEOS + M3U8_KEY;
            }
        }
        initView();
        initData();
        initClick();

        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.MY_DOWNLOAD_DOWNING);
        filter.addAction(Constants.MY_DOWNLOAD_FAIL);
        filter.addAction(Constants.MY_DOWNLOAD_FINSH);
        filter.addAction(Constants.MY_DOWNLOAD_REFRESH);
        // 注册广播
        registerReceiver(receiver, filter);
    }

    private void initView()
    {
        all_operation_ly = (RelativeLayout) findViewById(R.id.all_operation_ly);
        all_begin_ly = (LinearLayout) findViewById(R.id.all_begin_ly);
        all_pause_ly = (LinearLayout) findViewById(R.id.all_pause_ly);
        listView = (ListView) findViewById(R.id.list);

        mSelectionMenuView = (ViewGroup) findViewById(R.id.selection_menu);
        bottom_fenge = findViewById(R.id.bottom_fenge);
        selection_all = (Button) findViewById(R.id.selection_all);
        selection_delete = (Button) findViewById(R.id.selection_delete);
    }

    private void initData()
    {
        DataBaseDao.getInstance(this).query_DownLoad_Service_Videos(service_id, childList);
        adapter = new DownloadServiceListAdapter(DownloadServiceVideoActivity.this, childList, service_id);
        listView.setAdapter(adapter);

        //根据id初始化childitem的checkbox
        for (int j = 0; j < childList.size(); j++)
        {
            HashMap<String, String> hashMap = childList.get(j);
            String video_id = hashMap.get("id");
            adapter.getIsSelected().put(video_id, false);
        }
        updateAllOperationStatus();
    }


    private MenuItem itema;

    private void initClick()
    {

        mToolbarView.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick(MenuItem item)
            {
                switch (item.getItemId())
                {
                    case R.id.editor:
                        itema = item;
                        if (!showCheckbox)
                        {
                            mSelectionMenuView.setVisibility(View.VISIBLE);
                            bottom_fenge.setVisibility(View.VISIBLE);
                            mSelectionMenuView.startAnimation(AnimationUtils.loadAnimation(DownloadServiceVideoActivity.this, R.anim.footer_appear));
                            itema.setTitle(getResources().getString(R.string.cancel));
                            showCheckbox = true;
                            for (int i = 0; i < childList.size(); i++)
                            {
                                HashMap<String, String> hashMap = childList.get(i);
                                hashMap.put("showcheckbox", "1");
                                hashMap.put("showtext", "1");
                            }

                            selection_delete.setText(getResources().getString(R.string.delete));
                            adapter.notifyDataSetChanged();

                        } else
                        {
                            mSelectionMenuView.setVisibility(View.GONE);
                            bottom_fenge.setVisibility(View.GONE);
                            mSelectionMenuView.startAnimation(AnimationUtils.loadAnimation(DownloadServiceVideoActivity.this, R.anim.footer_disappear));

                            itema.setTitle(getResources().getString(R.string.edit));
                            showCheckbox = false;

                            for (int i = 0; i < childList.size(); i++)
                            {
                                HashMap<String, String> hashMap = childList.get(i);
                                hashMap.put("showcheckbox", "0");
                                hashMap.put("showtext", "0");
                                String video_id = hashMap.get("id");
                                adapter.getIsSelected().put(video_id, false);
                            }
                            setCheckNum(0);
                            adapter.notifyDataSetChanged();
                        }

                        break;
                }
                return true;
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                HashMap<String, String> hashMap = childList.get(position);
                final String video_id = hashMap.get("id");
                final String isfinish = hashMap.get("isfinish");
                CheckBox video_check = (CheckBox) view.findViewById(R.id.video_check);
                if (showCheckbox)
                {
                    // 改变CheckBox的状态
                    video_check.toggle();
                    // 将CheckBox的选中状况记录下来
                    adapter.getIsSelected().put(video_id, video_check.isChecked());
                    // 调整选定条目
                    if (video_check.isChecked() == true)
                    {
                        setCheckNum(getCheckNum() + 1);
                    } else
                    {
                        setCheckNum(getCheckNum() - 1);
                    }
                    selection_delete.setText(getResources().getString(R.string.delete) + "（" + getCheckNum() + "）");
                } else
                {
                    if (isfinish.equals("1"))
                    {
                        playDownlaodVideo(hashMap);
                    }
                }

            }
        });

        //全部开始
        all_begin_ly.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                stop_Dwonload();
                HashMap<String, String> query_Downing_Download = DataBaseDao.getInstance(DownloadServiceVideoActivity.this).query_Downing_Download();
                String download_id = query_Downing_Download.get("id");
                if (download_id != null)
                {
                    DataBaseDao.getInstance(DownloadServiceVideoActivity.this).updata_DownStatus(download_id, service_id, 0);// 中断下载把在这状态改为等待状态
                    // 停止服务
                    DownloadService.stopService(DownloadServiceVideoActivity.this);
                }
                //把所有视频设为等待状态(不包括下载完成的视频)
                DataBaseDao.getInstance(DownloadServiceVideoActivity.this).updata_AllDownStatusOfService(service_id, 0);
                ArrayList<HashMap<String, String>> query_Wait_Download = DataBaseDao.getInstance(DownloadServiceVideoActivity.this).queryAllWaitServiceVideos(service_id);
                if (query_Wait_Download.size() != 0)
                {
                    HashMap<String, String> hashMap2 = query_Wait_Download.get(0);
                    DataBaseDao.getInstance(DownloadServiceVideoActivity.this).updata_DownStatus(hashMap2.get("id"), service_id, 1);

                    for (int j = 0; j < childList.size(); j++)
                    {
                        HashMap<String, String> hashMap = childList.get(j);
                        String update_id = hashMap.get("id");
                        if (update_id.equals(hashMap2.get("id")))
                        {
                            hashMap.put("downloadStatus", "1");
                        } else
                        {
                            hashMap.put("downloadStatus", "0");
                        }
                    }

                    downloadManager.addServiceVideoDownload(hashMap2.get("url_content"), service_id, hashMap2.get("id")
                            , hashMap2.get("id"), Integer.parseInt(hashMap2.get("mapKey"))
                            , hashMap2.get("name"), Long.parseLong(hashMap2.get("downloadedSize")), hashMap2.get("video_type"));
                }
                updateAllOperationStatus();
                adapter.notifyDataSetChanged();
            }
        });

        //全部暂停
        all_pause_ly.setOnClickListener(new View.OnClickListener()
                                        {

                                            @Override
                                            public void onClick(View v)
                                            {
                                                stop_Dwonload();
                                                HashMap<String, String> query_Downing_Download = DataBaseDao.getInstance(DownloadServiceVideoActivity.this).query_Downing_Download();
                                                String download_id = query_Downing_Download.get("id");
                                                DataBaseDao.getInstance(DownloadServiceVideoActivity.this).updata_DownStatus(download_id, service_id, 0);// 中断下载把在这状态改为等待状态
                                                // 停止服务
                                                DownloadService.stopService(DownloadServiceVideoActivity.this);

                                                for (int j = 0; j < childList.size(); j++)
                                                {
                                                    HashMap<String, String> hashMap = childList.get(j);
                                                    hashMap.put("downloadStatus", "2");
                                                }

                                                updateAllOperationStatus();
                                                adapter.notifyDataSetChanged();
                                            }
                                        }
        );

        //全选
        selection_all.setOnClickListener(new View.OnClickListener()
                                         {

                                             @Override
                                             public void onClick(View v)
                                             {
                                                 if (!selectAll)
                                                 {
                                                     selection_all.setText(getResources().getString(R.string.cancel_select_all));
                                                     int allcheckbox = 0;
                                                     for (int j = 0; j < childList.size(); j++)
                                                     {
                                                         HashMap<String, String> hashMap = childList.get(j);
                                                         String video_id = hashMap.get("id");
                                                         allcheckbox++;
                                                         adapter.getIsSelected().put(video_id, true);
                                                     }
                                                     setCheckNum(allcheckbox);
                                                     selection_delete.setText(getResources().getString(R.string.delete) + "（" + getCheckNum() + "）");
                                                     // 刷新listview和TextView的显示
                                                     adapter.notifyDataSetChanged();
                                                     selectAll = true;
                                                 } else
                                                 {
                                                     selection_all.setText(getResources().getString(R.string.select_all));

                                                     //根据id初始化childitem的checkbox
                                                     for (int j = 0; j < childList.size(); j++)
                                                     {
                                                         HashMap<String, String> hashMap = childList.get(j);
                                                         String video_id = hashMap.get("id");
                                                         adapter.getIsSelected().put(video_id, false);
                                                     }
                                                     setCheckNum(0);
                                                     selection_delete.setText(getResources().getString(R.string.delete));
                                                     adapter.notifyDataSetChanged();
                                                     selectAll = false;
                                                 }
                                             }
                                         }
        );

        //删除
        selection_delete.setOnClickListener(new View.OnClickListener()
                                            {

                                                @Override
                                                public void onClick(View v)
                                                {

                                                    if (Constants.API_LEVEL_11)
                                                    {
                                                        new Delete_AsyncTask().executeOnExecutor(Constants.exec);
                                                    } else
                                                    {
                                                        new Delete_AsyncTask().execute();
                                                    }
                                                }
                                            }
        );

    }

    //删除线程
    private class Delete_AsyncTask extends AsyncTask<String, Integer, Boolean>
    {

        @Override
        protected Boolean doInBackground(String... params)
        {
            boolean flag = false;
            publishProgress(1);

            for (int i = 0; i < childList.size(); i++)
            {
                for (int j = 0; j < childList.size(); j++)
                {
                    HashMap<String, String> hashMap = childList.get(j);
                    Boolean boolean1 = adapter.getIsSelected().get(hashMap.get("id"));
                    if (boolean1)
                    {
                        String video_id = hashMap.get("id");
                        String isdownloadStatus = hashMap.get("downloadStatus");
//				  		 //删除数据库
                        DataBaseDao.getInstance(DownloadServiceVideoActivity.this).delete_item(video_id, service_id);
                        //删除sd卡
                        FileUtil.deleteFolder(path_urls + video_id + video_id);
                        FileUtil.deleteFile(path_m3u8 + "//" + video_id + video_id + ".m3u8");
                        FileUtil.deleteFile(path_key + "//" + video_id + video_id + "key");
                        //删除外置sd卡
                        if (!TextUtils.isEmpty(spu.getSecondSdcard()))
                        {
                            FileUtil.delteallFile(path_urls_second + video_id + video_id);
                            FileUtil.deleteFile(path_m3u8_second + "//" + video_id + video_id + ".m3u8");
                            FileUtil.deleteFile(path_key_second + "//" + video_id + video_id + "key");
                        }
                        //如果删除的视频正在下载中,就停止下载，继续下载其他等待的视频
                        if (isdownloadStatus.equals("1"))
                        {
                            stop_Dwonload();
                        }
                    }
                }
            }
            flag = true;
            return flag;
        }

        @Override
        protected void onProgressUpdate(Integer... values)
        {
            super.onProgressUpdate(values);
            if (values[0] == 1)
            {
                if (!isFinishing())
                {
                    dialog = MyPublicDialog.createLoadingDialog(DownloadServiceVideoActivity.this);
                    dialog.show();
                }
            }
        }

        @Override
        protected void onPostExecute(Boolean result)
        {
            super.onPostExecute(result);
            if (dialog != null && dialog.isShowing())
            {
                dialog.dismiss();
            }
            if (result)
            {
                ArrayList<String> query_tree_allvideoids = DataBaseDao.getInstance(DownloadServiceVideoActivity.this).queryVideoidsByServiceId(service_id);
                if (query_tree_allvideoids.size() == 0)
                {
                    setResult(1);
                    finish();
                } else
                {
//                    rightText.setText(getResources().getString(R.string.edit));
                    showCheckbox = false;
                    mSelectionMenuView.setVisibility(View.GONE);
                    bottom_fenge.setVisibility(View.GONE);
                    childList.clear();
                    DataBaseDao.getInstance(DownloadServiceVideoActivity.this).query_DownLoad_Service_Videos(service_id, childList);
                    listView.setAdapter(adapter);
                    for (int i = 0; i < childList.size(); i++)
                    {
                        HashMap<String, String> arrayList = childList.get(i);
                        String video_id = arrayList.get("id");
                        adapter.getIsSelected().put(video_id, false);
                    }
                    //删除后全部设为不选中状态
                    setCheckNum(0);
                }
                self_Download_Next_Video();
            } else
            {
            }
        }
    }

    /**
     * 自动下载下一个视频
     */
    public void self_Download_Next_Video()
    {
        ArrayList<String> query_All_DownStatus = DataBaseDao.getInstance(DownloadServiceVideoActivity.this).query_All_DownStatus();
        if (query_All_DownStatus.contains("1"))
        {

        } else
        {
            //自动下载其他课程等待的视频?
            ArrayList<HashMap<String, String>> allwaitList = DataBaseDao.getInstance(DownloadServiceVideoActivity.this).queryAllWaitServiceVideos(service_id);
            if (allwaitList.size() != 0)
            {
                HashMap<String, String> hashMap2 = allwaitList.get(0);
                DataBaseDao.getInstance(DownloadServiceVideoActivity.this).updata_DownStatus(hashMap2.get("id"),service_id, 1);
                downloadManager.addServiceVideoDownload(hashMap2.get("url_content"), hashMap2.get("service_id"), hashMap2.get("id")
                        , hashMap2.get("id"), Integer.parseInt(hashMap2.get("mapKey"))
                        , hashMap2.get("name"), Long.parseLong(hashMap2.get("downloadedSize")), hashMap2.get("video_type"));
                Intent intent_refresh = new Intent();
                intent_refresh.setAction(Constants.MY_DOWNLOAD_REFRESH);
                intent_refresh.putExtra("id", hashMap2.get("id"));
                DownloadServiceVideoActivity.this.sendBroadcast(intent_refresh);
            } else
            {
                //停止服务
                DownloadService.stopService(DownloadServiceVideoActivity.this);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.download_editor_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private class MyReceiver extends BroadcastReceiver
    {

        @Override
        public void onReceive(Context context, Intent intent)
        {
            if (intent.getAction().equals(Constants.MY_DOWNLOAD_DOWNING))
            {
                Bundle bundle = intent.getExtras();
                Float progress = bundle.getFloat("progress");//进度
                String baifenbi = bundle.getString("baifenbi");//下载百分比
                String updata_id = bundle.getString("id");//视频id

                for (int j = 0; j < childList.size(); j++)
                {
                    HashMap<String, String> hashMap = childList.get(j);
                    String downloadStatus = hashMap.get("downloadStatus");
                    String video_id = hashMap.get("id");
                    if (video_id.equals(updata_id) && downloadStatus.equals("1"))
                    {
                        hashMap.put("downloadpos", String.valueOf(progress));
                        hashMap.put("downloadPercent", baifenbi);
                    }
                }
                int itemcount = listView.getCount();
                for (int i = 0; i < itemcount; i++)
                {
                    View view = listView.getChildAt(i);
                    if (view != null)
                    {
                        DownloadServiceListAdapter.ViewHolder vh = (DownloadServiceListAdapter.ViewHolder) view.getTag();
                        if (vh.video_name != null)
                        {
                            int tag = (Integer) vh.video_name.getTag();
                            if (tag == 1)
                            {
                                //局部刷新BaseExpandableListAdapter
                                vh.video_progressbar.setProgress((int) Float.parseFloat(String.valueOf(progress)));
                                vh.video_size_or_percentage.setText(baifenbi + "%");
                            }
                        }
                    }
                }
            } else if (intent.getAction().equals(Constants.MY_DOWNLOAD_FAIL))
            {
                Bundle bundle = intent.getExtras();
                String updata_id = bundle.getString("id");//视频id
                for (int j = 0; j < childList.size(); j++)
                {
                    HashMap<String, String> hashMap = childList.get(j);
                    String video_id = hashMap.get("id");
                    if (video_id.equals(updata_id))
                    {
                        hashMap.put("downloadStatus", "2");
                    }
                }
                updateAllOperationStatus();
                adapter.notifyDataSetChanged();

            } else if (intent.getAction().equals(Constants.MY_DOWNLOAD_FINSH))
            {
                Bundle bundle = intent.getExtras();
                String url = bundle.getString("url");//视频地址
                String updata_id = bundle.getString("id");//视频id
                String filesize = bundle.getString("filesize");//视频id

                for (int j = 0; j < childList.size(); j++)
                {
                    HashMap<String, String> hashMap = childList.get(j);
                    String video_id = hashMap.get("id");
                    if (video_id.equals(updata_id))
                    {
                        hashMap.put("url_content", url);
                        hashMap.put("isfinish", "1");
                        hashMap.put("downloadedSize", filesize);
                        hashMap.put("downloadStatus", "3");
                    }
                }
                updateAllOperationStatus();
                adapter.notifyDataSetChanged();

            } else if (intent.getAction().equals(Constants.MY_DOWNLOAD_REFRESH))
            {
                Bundle bundle = intent.getExtras();
                String updata_id = bundle.getString("id");//视频id
                for (int j = 0; j < childList.size(); j++)
                {
                    HashMap<String, String> hashMap = childList.get(j);
                    String video_id = hashMap.get("id");
                    if (video_id.equals(updata_id))
                    {
                        hashMap.put("downloadStatus", "1");
                    }
                }
                updateAllOperationStatus();
                adapter.notifyDataSetChanged();
            }
        }
    }


    /**
     * 判断全部开始||全部暂停的状态
     */
    public void updateAllOperationStatus()
    {
        downloadStatusList.clear();
        for (int j = 0; j < childList.size(); j++)
        {
            HashMap<String, String> hashMap = childList.get(j);
            String downloadStatus = hashMap.get("downloadStatus");
            downloadStatusList.add(downloadStatus);
        }
        if (!downloadStatusList.contains("0") && !downloadStatusList.contains("1") && !downloadStatusList.contains("2"))
        {
            all_operation_ly.setVisibility(View.GONE);
        } else
        {
            all_operation_ly.setVisibility(View.VISIBLE);
            if (downloadStatusList.contains("2") && !downloadStatusList.contains("1") && !downloadStatusList.contains("0")
                    || downloadStatusList.contains("0") && !downloadStatusList.contains("2") && !downloadStatusList.contains("1")
                    || downloadStatusList.contains("0") && downloadStatusList.contains("2") && !downloadStatusList.contains("1"))
            {
                all_begin_ly.setVisibility(View.VISIBLE);
                all_pause_ly.setVisibility(View.GONE);
            } else
            {
                all_pause_ly.setVisibility(View.VISIBLE);
                all_begin_ly.setVisibility(View.GONE);
            }
        }
    }


    /**
     * 播放下载的视频
     */
    public void playDownlaodVideo(HashMap<String, String> hashMap)
    {
        if (!fileUtil.isCheckSdVideoExists(DownloadServiceVideoActivity.this, hashMap))
        {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_video), Toast.LENGTH_SHORT).show();
        } else
        {
            for (int i = 0; i < childList.size(); i++)
            {
                CatalogueBean.ItemsBean.VideoBean videoBean = new CatalogueBean.ItemsBean.VideoBean();
                HashMap<String, String> hashMap_selects = childList.get(i);
                if (hashMap_selects.get("isfinish").equals("1"))
                {//求出此课程所有下载完成的视频

                    videoBean.setTitle(hashMap_selects.get("name"));
                    videoBean.setCourse_chapter_id(hashMap_selects.get("tid"));
                    videoBean.setId(hashMap_selects.get("id"));
                    videoBean.setType(hashMap_selects.get("type"));
                    videoBean.setMediaUri(hashMap_selects.get("url_content"));
                    if (hashMap.get("url_content").equals(hashMap_selects.get("url_content")))
                    {
                        videoBean.setLast_location("1");
                    }
                    selectList.add(videoBean);
                }
            }

            CatalogueBean.ItemsBean.VideoBean videoBean = new CatalogueBean.ItemsBean.VideoBean();
            videoBean.setTitle(hashMap.get("name"));
            videoBean.setCourse_chapter_id(hashMap.get("tid"));
            videoBean.setId(hashMap.get("id"));
            videoBean.setType(hashMap.get("type"));
            videoBean.setMediaUri(hashMap.get("url_content"));

            try
            {
                new PlayVideoServer(DownloadServiceVideoActivity.this).start();
                //		LogWriter.v("tangcy", "启动本地视频服务成功");
            } catch (Exception e)
            {
                e.printStackTrace();
                //		LogWriter.v("tangcy", "启动本地视频服务失败");
            }

            Intent intent = new Intent(DownloadServiceVideoActivity.this, VideoViewActivity.class);
            intent.putExtra("flag", Constants.OFFLINE);
            intent.putExtra("treeid", service_id);
            intent.putExtra("treename", service_name);
            intent.putExtra("pic", hashMap.get("treepicture"));
            intent.putExtra("videoBean", videoBean);
            intent.putExtra("selects", (Serializable) selectList);
            startActivity(intent);
            selectList.clear();
        }
    }


    /**
     * 停止下载
     */
    public void stop_Dwonload()
    {
        ArrayList<Callback.Cancelable> cacebles = downloadManager.getAllCacebles();
        if (cacebles.size() >= 0)
        {
            for (int i = 0; i < cacebles.size(); i++)
            {
                Callback.Cancelable cancelable = cacebles.get(i);
                if (cancelable != null && !cancelable.isCancelled())
                {
                    cancelable.cancel();
                }
            }
        }
    }

    /**
     * 点击按钮下载
     *
     * @param video_id
     * @param url
     * @param serviceId 视频id
     * @param name
     */
    public void clickDownloadVideo(String video_id, String url, String serviceId, String name)
    {

        //点击下载视频,更新下载状态为1
        DataBaseDao.getInstance(DownloadServiceVideoActivity.this).updata_DownStatus(video_id, serviceId, 1);

        for (int j = 0; j < childList.size(); j++)
        {
            HashMap<String, String> hashMap = childList.get(j);
            String update_id = hashMap.get("id");
            if (update_id.equals(video_id))
            {
                hashMap.put("downloadStatus", "1");
            }
        }

        //根据视频的 serviceId 和id 查询已经下载到第几个mapkey和下载了多少字节
        HashMap<String, String> query_Continue_DownLoad_Video = DataBaseDao.getInstance(DownloadServiceVideoActivity.this).queryServiceDownLoadingVideo(serviceId, video_id);
        downloadManager.addServiceVideoDownload(url, service_id, video_id, video_id,
                Integer.parseInt(query_Continue_DownLoad_Video.get("mapkey")), name,
                Long.parseLong(query_Continue_DownLoad_Video.get("downloadedSize"))
                , query_Continue_DownLoad_Video.get("video_type"));
        updateAllOperationStatus();
        adapter.notifyDataSetChanged();
    }

    public int getCheckNum()
    {
        return checkNum;
    }

    public void setCheckNum(int checkNum)
    {
        this.checkNum = checkNum;
    }

    @Override
    public void onDestroy()
    {
        unregisterReceiver(receiver);
        if (dialog != null && dialog.isShowing())
        {
            dialog.dismiss();
        }
        super.onDestroy();
    }
}

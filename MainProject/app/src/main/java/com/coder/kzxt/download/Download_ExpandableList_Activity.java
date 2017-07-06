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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.db.DataBaseDao;
import com.coder.kzxt.dialog.util.MyPublicDialog;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.FileUtil;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.PlayVideoServer;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.coder.kzxt.video.activity.VideoViewActivity;
import com.coder.kzxt.video.activity.VrVideoActivity;
import com.coder.kzxt.video.beans.CatalogueBean;
import com.coder.kzxt.views.ScrollHeaderListView;

import org.xutils.common.Callback;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.coder.kzxt.activity.R.id.download_qq_list;
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

public class Download_ExpandableList_Activity extends BaseActivity{

    private Toolbar mToolbarView;
    private SharedPreferencesUtil spu;
    //下载类
    private DownloadManager downloadManager;
    private FileUtil fileUtil;
    private String treeid;
    private String treename;
    private String treepicture;
    private ScrollHeaderListView download_list;
    private BeingDownloadAdapter adapter;
    //选集数据
    private List<CatalogueBean.ItemsBean.VideoBean> selects = new ArrayList<>();
    private ArrayList<String> downloadStatusList;//此课程下所以视频的下载状态
    private ArrayList<HashMap<String, String>> groupList;
    private ArrayList<ArrayList<HashMap<String, String>>> childList;

    private String path_key;
    private String path_urls;
    private String path_m3u8;
    private String path_vr;

    private String path_urls_second;
    private String path_m3u8_second;
    private String path_key_second;
    private String path_vr_second;

    private RelativeLayout all_operation_ly;
    private LinearLayout all_begin_ly;
    private LinearLayout all_pause_ly;
    private ViewGroup mSelectionMenuView;
    private View bottom_fenge;
    private Button selection_all;
    private Button selection_delete;
    private MyReceiver receiver;
    private Dialog dialog;
    private boolean selectAll = false;
    private boolean showCheckbox = false;
    private int checkNum = 0; // 记录选中的条目数量

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_expandable_list);
        spu = new SharedPreferencesUtil(this);
        downloadManager = DownloadService.getDownloadManager(Download_ExpandableList_Activity.this);
        fileUtil = new FileUtil(this);
        treeid = getIntent().getStringExtra("treeid");
        treename = getIntent().getStringExtra("treename");
        treepicture = getIntent().getStringExtra("treepicture");

        mToolbarView = (Toolbar) findViewById(R.id.toolbar);
        mToolbarView.setTitle(treename);
        setSupportActionBar(mToolbarView);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        groupList = new ArrayList<HashMap<String, String>>();
        childList = new ArrayList<ArrayList<HashMap<String, String>>>();
        downloadStatusList = new ArrayList<String>();

        path_urls = getExternalFilesDir(null)+DOWNLOAD_URLS;
        path_m3u8 = getExternalFilesDir(null)+DOWNLOAD_M3U8;
        path_key = getExternalFilesDir(null)+DOWNLOAD_KEY;
        path_vr =  getExternalFilesDir(null)+DOWNLOAD_VR;

        String secondSdcard = spu.getSecondSdcard();
        if (!TextUtils.isEmpty(secondSdcard)) {
            if (Constants.API_LEVEL_19) {
                path_urls_second = secondSdcard + "/Android/data/" + getPackageName() + MAIN_FOLDER + VIDEOS + M3U8_URLS;
                path_m3u8_second = secondSdcard + "/Android/data/" + getPackageName() + MAIN_FOLDER + VIDEOS + M3U8;
                path_key_second = secondSdcard + "/Android/data/" + getPackageName() + MAIN_FOLDER+ VIDEOS + M3U8_KEY;
                path_vr_second = secondSdcard + "/Android/data/" +  getPackageName() + MAIN_FOLDER + VIDEOS + VR_VIDEO;
            }else {
                path_urls_second = secondSdcard + MAIN_FOLDER + VIDEOS + M3U8_URLS;
                path_m3u8_second = secondSdcard + MAIN_FOLDER + VIDEOS + M3U8;
                path_key_second = secondSdcard + MAIN_FOLDER + VIDEOS + M3U8_KEY;
                path_vr_second = secondSdcard + MAIN_FOLDER + VIDEOS + VR_VIDEO;
            }
        }

        initView();
        initData();
        initOnClick();
        updateAllOperationStatus();

        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.MY_DOWNLOAD_DOWNING);
        filter.addAction(Constants.MY_DOWNLOAD_FAIL);
        filter.addAction(Constants.MY_DOWNLOAD_FINSH);
        filter.addAction(Constants.MY_DOWNLOAD_REFRESH);
        // 注册广播
        registerReceiver(receiver, filter);
    }


    private void initView(){
        download_list = (ScrollHeaderListView) findViewById(download_qq_list);
        all_operation_ly = (RelativeLayout) findViewById(R.id.all_operation_ly);
        all_begin_ly = (LinearLayout) findViewById(R.id.all_begin_ly);
        all_pause_ly = (LinearLayout) findViewById(R.id.all_pause_ly);
        mSelectionMenuView = (ViewGroup) findViewById(R.id.selection_menu);
        bottom_fenge = findViewById(R.id.bottom_fenge);
        selection_all = (Button) findViewById(R.id.selection_all);
        selection_delete = (Button) findViewById(R.id.selection_delete);

    }

    private void initData(){
        DataBaseDao.getInstance(this).query_DownLoad_Tree_Chapters(treeid, groupList);
        for (int i = 0; i < groupList.size(); i++) {
            String tid = groupList.get(i).get("tid");
            DataBaseDao.getInstance(this).query_DownLoad_Tree_Videos(treeid, tid, childList);
        }
        adapter = new BeingDownloadAdapter(Download_ExpandableList_Activity.this,download_list,groupList,childList);
        download_list.setHeaderView(LayoutInflater.from(Download_ExpandableList_Activity.this).inflate(R.layout.download_expandable_list_group_item, null));
        download_list.setAdapter(adapter);

        for (int i = 0; i < groupList.size(); i++) {
            download_list.expandGroup(i);
        }

        //根据id初始化childitem的checkbox
        for (int i = 0; i < childList.size(); i++) {
            ArrayList<HashMap<String, String>> arrayList = childList.get(i);
            for (int j = 0; j < arrayList.size(); j++) {
                HashMap<String, String> hashMap = arrayList.get(j);
                String video_id = hashMap.get("id");
                adapter.getIsSelected().put(video_id, false);
            }
        }
    }

    private MenuItem itema;
    private void initOnClick(){

        mToolbarView.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.editor:
                        itema = item;
                        if (!showCheckbox) {
                            mSelectionMenuView.setVisibility(View.VISIBLE);
                            bottom_fenge.setVisibility(View.VISIBLE);
                            mSelectionMenuView.startAnimation(AnimationUtils.loadAnimation(Download_ExpandableList_Activity.this, R.anim.footer_appear));
                            itema.setTitle(getResources().getString(R.string.cancel));
                            showCheckbox = true;

                            for (int i = 0; i < childList.size(); i++) {
                                ArrayList<HashMap<String, String>> arrayList = childList.get(i);
                                for (int j = 0; j < arrayList.size(); j++) {
                                    HashMap<String, String> hashMap = arrayList.get(j);
                                    hashMap.put("showcheckbox", "1");
                                    hashMap.put("showtext", "1");
                                }
                            }

                            selection_delete.setText(getResources().getString(R.string.delete));
                            adapter.notifyDataSetChanged();

                        } else {
                            mSelectionMenuView.setVisibility(View.GONE);
                            bottom_fenge.setVisibility(View.GONE);
                            mSelectionMenuView.startAnimation(AnimationUtils.loadAnimation(Download_ExpandableList_Activity.this, R.anim.footer_disappear));
                            itema.setTitle(getResources().getString(R.string.edit));
                            showCheckbox = false;

                            for (int i = 0; i < childList.size(); i++) {
                                ArrayList<HashMap<String, String>> arrayList = childList.get(i);
                                for (int j = 0; j < arrayList.size(); j++) {
                                    HashMap<String, String> hashMap = arrayList.get(j);
                                    hashMap.put("showcheckbox", "0");
                                    hashMap.put("showtext", "0");
                                    String video_id = hashMap.get("id");
                                    adapter.getIsSelected().put(video_id, false);

                                }
                            }
                            setCheckNum(0);
                            adapter.notifyDataSetChanged();
                        }

                        break;
                }
                return true;
            }
        });


        download_list.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                final HashMap<String, String> hashMap = childList.get(groupPosition).get(childPosition);
                final String video_id = hashMap.get("id");
                final String type = hashMap.get("type");
                final String isfinish = hashMap.get("isfinish");
                CheckBox video_check = (CheckBox) v.findViewById(R.id.video_check);
                if (showCheckbox){
                    // 改变CheckBox的状态
                    video_check.toggle();
                    // 将CheckBox的选中状况记录下来
                    adapter.getIsSelected().put(video_id, video_check.isChecked());
                    // 调整选定条目
                    if (video_check.isChecked() == true) {
                        setCheckNum(getCheckNum() + 1);
                    } else {
                        setCheckNum(getCheckNum() - 1);
                    }
                    selection_delete.setText(getResources().getString(R.string.delete)+"（" + getCheckNum() + "）");
                }else {
                    if (isfinish.equals("1")) {
                        if(type!=null){
                            if(type==null||type.equals("1")){
                                playDownlaodVideo(hashMap);
                            }else {
                                playVRVideo(hashMap,groupPosition,childPosition);
                            }
                        }

                    }
                }
                return false;
            }
        });

        //全部开始
        all_begin_ly.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!NetworkUtil.isNetworkAvailable(Download_ExpandableList_Activity.this)) {
                    if(spu.getDownloadFlag().equals("0")){
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.not_support_flow), Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                stop_Dwonload();
                HashMap<String, String> query_Downing_Download = DataBaseDao.getInstance(Download_ExpandableList_Activity.this).query_Downing_Download();
                String download_id = query_Downing_Download.get("id");
                if (download_id != null) {
                    DataBaseDao.getInstance(Download_ExpandableList_Activity.this).updata_DownStatus(download_id, 0);// 中断下载把在这状态改为等待状态
                }
                //把所有视频设为等待状态(不包括下载完成的视频)
                DataBaseDao.getInstance(Download_ExpandableList_Activity.this).updata_AllDownStatus(treeid, 0);

                //自动下载本课程等待的视频
                ArrayList<HashMap<String, String>> query_Wait_Download = DataBaseDao.getInstance(Download_ExpandableList_Activity.this).query_Wait_Download(treeid);
                if (query_Wait_Download.size() != 0) {
                    HashMap<String, String> hashMap2 = query_Wait_Download.get(0);
                    DataBaseDao.getInstance(Download_ExpandableList_Activity.this).updata_DownStatus(hashMap2.get("id"), 1);

                    int gourpsSum2 = adapter.getGroupCount();//组的数量
                    for (int i = 0; i < gourpsSum2; i++) {
                        int childSum = adapter.getChildrenCount(i);//组中子项的数量
                        for (int j = 0; j < childSum; j++) {
                            HashMap<String, String> hashMap = childList.get(i).get(j);
                            String update_id = hashMap.get("id");
                            if (update_id.equals(hashMap2.get("id"))) {
                                hashMap.put("downloadStatus", "1");
                            }
                        }
                    }

                    updateAllOperationStatus();
                    adapter.notifyDataSetChanged();
                    downloadManager.addCourseVideoDownload(hashMap2.get("url_content"), hashMap2.get("treeid"), hashMap2.get("tid")
                            , hashMap2.get("id"), Integer.parseInt(hashMap2.get("mapKey"))
                            , hashMap2.get("name"), Long.parseLong(hashMap2.get("downloadedSize")),hashMap2.get("video_type"));
                }
            }
        });

        //全部暂停
        all_pause_ly.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                stop_Dwonload();
                HashMap<String, String> query_Downing_Download = DataBaseDao.getInstance(Download_ExpandableList_Activity.this).query_Downing_Download();
                String download_id = query_Downing_Download.get("id");
                DataBaseDao.getInstance(Download_ExpandableList_Activity.this).updata_DownStatus(download_id, 0);// 中断下载把在这状态改为等待状态
                // 停止服务
                DownloadService.stopService(Download_ExpandableList_Activity.this);
                for (int i = 0; i < childList.size(); i++) {
                    ArrayList<HashMap<String, String>> arrayList = childList.get(i);
                    for (int j = 0; j < arrayList.size(); j++) {
                        HashMap<String, String> hashMap = arrayList.get(j);
                        String video_id = hashMap.get("id");
                        if (video_id.equals(download_id)) {
                            hashMap.put("downloadStatus", "0");
                        }
                    }
                }
                updateAllOperationStatus();
                adapter.notifyDataSetChanged();
            }
        });


        //全选
        selection_all.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!selectAll) {
                    selection_all.setText(getResources().getString(R.string.cancel_select_all));
                    int allcheckbox = 0;
                    for (int i = 0; i < childList.size(); i++) {
                        ArrayList<HashMap<String, String>> arrayList = childList.get(i);
                        for (int j = 0; j < arrayList.size(); j++) {
                            HashMap<String, String> hashMap = arrayList.get(j);
                            String video_id = hashMap.get("id");
                            allcheckbox++;
                            adapter.getIsSelected().put(video_id, true);
                        }
                    }
                    setCheckNum(allcheckbox);
                    selection_delete.setText(getResources().getString(R.string.delete)+"（" + getCheckNum() + "）");
                    // 刷新listview和TextView的显示
                    adapter.notifyDataSetChanged();
                    selectAll = true;
                } else {
                    selection_all.setText(getResources().getString(R.string.select_all));
                    //根据id初始化childitem的checkbox
                    for (int i = 0; i < childList.size(); i++) {
                        ArrayList<HashMap<String, String>> arrayList = childList.get(i);
                        for (int j = 0; j < arrayList.size(); j++) {
                            HashMap<String, String> hashMap = arrayList.get(j);
                            String video_id = hashMap.get("id");
                            adapter.getIsSelected().put(video_id, false);
                        }
                    }
                    setCheckNum(0);
                    selection_delete.setText(getResources().getString(R.string.delete));
                    adapter.notifyDataSetChanged();
                    selectAll = false;
                }
            }
        });

        //删除
        selection_delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (Constants.API_LEVEL_11) {
                    new Delete_AsyncTask().executeOnExecutor(Constants.exec);
                } else {
                    new Delete_AsyncTask().execute();
                }
            }
        });

    }

    private void playVRVideo(HashMap<String, String> hashMap, int groupPosition, int childPosition) {

//        if(spu.getEncr(hashMap.get("tid"),hashMap.get("id"))){
//            //解密
//            boolean encry = VideoAesUtil.encrypt(hashMap.get("url_content"));
//            if(encry){
//                spu.setEncr(hashMap.get("tid"),hashMap.get("id"),false);
//            }
//        }
        CatalogueBean.ItemsBean.VideoBean videoBean= new CatalogueBean.ItemsBean.VideoBean();
        videoBean.setTitle(hashMap.get("name"));
        videoBean.setCourse_chapter_id(hashMap.get("tid"));
        videoBean.setId(hashMap.get("id"));
        videoBean.setType(hashMap.get("type"));
        videoBean.setMediaUri(hashMap.get("url_content"));
        videoBean.setIs_drag(Integer.parseInt(hashMap.get("is_drag")));

        Intent intent = new Intent(this, VrVideoActivity.class);
        intent.putExtra("videoBean",videoBean);
        intent.putExtra("download","download");
        intent.putExtra("groupPosition",groupPosition);
        intent.putExtra("childPosition",childPosition);
        startActivity(intent);
//        /storage/emulated/0/Android/data/com.coder.kzxt.activity/files/yunketang/HBSI_videos/woying_vr/129942629959.mp4
    }

    /**
     * 播放下载的m3u8视频
     */
    public void playDownlaodVideo(HashMap<String, String> hashMap) {
        if (!fileUtil.isCheckSdVideoExists(Download_ExpandableList_Activity.this,hashMap)) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_video), Toast.LENGTH_SHORT).show();
        } else {
            for (int i = 0; i < childList.size(); i++) {
                ArrayList<HashMap<String, String>> arrayList = childList.get(i);
                for (int j = 0; j < arrayList.size(); j++) {
                    CatalogueBean.ItemsBean.VideoBean videoBean= new CatalogueBean.ItemsBean.VideoBean();
                    HashMap<String, String> hashMap_selects = arrayList.get(j);

                    if (hashMap_selects.get("isfinish").equals("1")) {//求出此课程所有下载完成的视频
                        videoBean.setTitle(hashMap_selects.get("name"));
                        videoBean.setCourse_chapter_id(hashMap_selects.get("tid"));
                        videoBean.setId(hashMap_selects.get("id"));
                        videoBean.setType(hashMap_selects.get("type"));
                        videoBean.setMediaUri(hashMap_selects.get("url_content"));
                        videoBean.setIs_drag(Integer.parseInt(hashMap_selects.get("is_drag")));
                        if(hashMap.get("url_content").equals(hashMap_selects.get("url_content"))){
                            videoBean.setLast_location("1");
                        }
                        selects.add(videoBean);
                    }
                }
            }
           CatalogueBean.ItemsBean.VideoBean videoBean= new CatalogueBean.ItemsBean.VideoBean();
            videoBean.setTitle(hashMap.get("name"));
            videoBean.setCourse_chapter_id(hashMap.get("tid"));
            videoBean.setId(hashMap.get("id"));
            videoBean.setType(hashMap.get("type"));
            videoBean.setMediaUri(hashMap.get("url_content"));
            videoBean.setIs_drag(Integer.parseInt(hashMap.get("is_drag")));
            try {
                new PlayVideoServer(Download_ExpandableList_Activity.this).start();
                //		LogWriter.v("tangcy", "启动本地视频服务成功");
            } catch (Exception e) {
                e.printStackTrace();
                //		LogWriter.v("tangcy", "启动本地视频服务失败");
            }
            Intent intent = new Intent(Download_ExpandableList_Activity.this, VideoViewActivity.class);
            intent.putExtra("flag", Constants.OFFLINE);
            intent.putExtra("treeid", treeid);
            intent.putExtra("treename", treename);
            intent.putExtra("pic", hashMap.get("treepicture"));
            intent.putExtra("videoBean", videoBean);
            intent.putExtra("selects", (Serializable) selects);
            startActivity(intent);
            selects.clear();
        }
    }

    /**
     * 点击按钮下载
     *
     * @param video_id
     * @param url
     * @param tid
     * @param name
     */
    public void clickDownloadVideo(String video_id, String url, String tid, String name) {

        //点击下载视频,更新下载状态为1
        DataBaseDao.getInstance(Download_ExpandableList_Activity.this).updata_DownStatus(video_id, 1);

        int gourpsSum = adapter.getGroupCount();//组的数量
        for (int i = 0; i < gourpsSum; i++) {
            int childSum = adapter.getChildrenCount(i);//组中子项的数量
            for (int j = 0; j < childSum; j++) {
                HashMap<String, String> hashMap = childList.get(i).get(j);
                String update_id = hashMap.get("id");
                if (update_id.equals(video_id)) {
                    hashMap.put("downloadStatus", "1");
                }
            }
        }

        //根据视频的 tid 和id 查询已经下载到第几个mapkey和下载了多少字节
        HashMap<String, String> query_Continue_DownLoad_Video = DataBaseDao.getInstance(Download_ExpandableList_Activity.this).queryCourseDownLoadingVideo(tid, video_id);
        downloadManager.addCourseVideoDownload(url, treeid, tid, video_id,
                Integer.parseInt(query_Continue_DownLoad_Video.get("mapkey")),
                name, Long.parseLong(query_Continue_DownLoad_Video.get("downloadedSize")),query_Continue_DownLoad_Video.get("video_type"));
        updateAllOperationStatus();
        adapter.notifyDataSetChanged();
    }


    private class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constants.MY_DOWNLOAD_DOWNING)) {
//				Log.v("tangcy", "接收广播更新下载");
                Bundle bundle = intent.getExtras();
                Float progress = bundle.getFloat("progress");//进度
                String baifenbi = bundle.getString("baifenbi");//下载百分比
                String updata_id = bundle.getString("id");//视频id
                int gourpsSum = adapter.getGroupCount();//组的数量
                for (int i = 0; i < gourpsSum; i++) {
                    int childSum = adapter.getChildrenCount(i);//组中子项的数量
                    for (int j = 0; j < childSum; j++) {
                        HashMap<String, String> hashMap = childList.get(i).get(j);
                        String downloadStatus = hashMap.get("downloadStatus");
                        String video_id = hashMap.get("id");
                        if (video_id.equals(updata_id) && downloadStatus.equals("1")) {
                            hashMap.put("downloadpos", String.valueOf(progress));
                            hashMap.put("downloadPercent", baifenbi);
                        }
                    }
                }
                int itemcount = download_list.getCount();
                for (int i = 0; i < itemcount; i++) {
                    View view = download_list.getChildAt(i);
                    if (view != null) {
                        if (view.getTag() instanceof BeingDownloadAdapter.ViewExpandableHolder) {
                            BeingDownloadAdapter.ViewExpandableHolder vh = (BeingDownloadAdapter.ViewExpandableHolder) view.getTag();
                            if (vh.video_name != null) {
                                int tag = (Integer) vh.video_name.getTag();
                                if (tag == 1) {
                                    //局部刷新BaseExpandableListAdapter
                                    vh.video_progressbar.setProgress((int) Float.parseFloat(String.valueOf(progress)));
                                    vh.video_size_or_percentage.setText(baifenbi + "%");
                                }
                            }
                        }
                    }
                }
            } else if (intent.getAction().equals(Constants.MY_DOWNLOAD_FAIL)) {
                Bundle bundle = intent.getExtras();
                String updata_id = bundle.getString("id");//视频id
                int gourpsSum = adapter.getGroupCount();//组的数量
                for (int i = 0; i < gourpsSum; i++) {
                    int childSum = adapter.getChildrenCount(i);//组中子项的数量
                    for (int j = 0; j < childSum; j++) {
                        HashMap<String, String> hashMap = childList.get(i).get(j);
                        String video_id = hashMap.get("id");
                        if (video_id.equals(updata_id)) {
                            hashMap.put("downloadStatus", "2");
                        }
                    }
                }
                updateAllOperationStatus();
                adapter.notifyDataSetChanged();

            } else if (intent.getAction().equals(Constants.MY_DOWNLOAD_FINSH)) {

                Bundle bundle = intent.getExtras();
                String url = bundle.getString("url");//视频地址
                String updata_id = bundle.getString("id");//视频id
                String filesize = bundle.getString("filesize");//视频id

                int gourpsSum = adapter.getGroupCount();//组的数量
                for (int i = 0; i < gourpsSum; i++) {
                    int childSum = adapter.getChildrenCount(i);//组中子项的数量
                    for (int j = 0; j < childSum; j++) {
                        HashMap<String, String> hashMap = childList.get(i).get(j);
                        String video_id = hashMap.get("id");
                        if (video_id.equals(updata_id)) {
                            hashMap.put("url_content", url);
                            hashMap.put("isfinish", "1");
                            hashMap.put("downloadedSize", filesize);
                            hashMap.put("downloadStatus", "3");
                        }
                    }
                }
                updateAllOperationStatus();
                adapter.notifyDataSetChanged();
            } else if (intent.getAction().equals(Constants.MY_DOWNLOAD_REFRESH)) {
                Bundle bundle = intent.getExtras();
                String updata_id = bundle.getString("id");//视频id
                int gourpsSum = adapter.getGroupCount();//组的数量
                for (int i = 0; i < gourpsSum; i++) {
                    int childSum = adapter.getChildrenCount(i);//组中子项的数量
                    for (int j = 0; j < childSum; j++) {
                        HashMap<String, String> hashMap = childList.get(i).get(j);
                        String video_id = hashMap.get("id");
                        if (video_id.equals(updata_id)) {
                            hashMap.put("downloadStatus", "1");
                        }
                    }
                }
                updateAllOperationStatus();
                adapter.notifyDataSetChanged();
            }
        }
    }



    //删除线程
    private class Delete_AsyncTask extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            boolean flag = false;
            publishProgress(1);

            for (int i = 0; i < childList.size(); i++) {
                ArrayList<HashMap<String, String>> arrayList = childList.get(i);
                for (int j = 0; j < arrayList.size(); j++) {
                    HashMap<String, String> hashMap = arrayList.get(j);
                    Boolean boolean1 = adapter.getIsSelected().get(hashMap.get("id"));
                    if (boolean1) {
                        String video_id = hashMap.get("id");
                        String tid = hashMap.get("tid");
                        String isdownloadStatus = hashMap.get("downloadStatus");
                        String type = hashMap.get("type");
                        if(type!=null&&type.equals("6")&&spu.getEncr(tid,video_id)){
                            spu.setEncr(tid,video_id,false);
                        }
//				  		 //删除数据库
                        DataBaseDao.getInstance(Download_ExpandableList_Activity.this).delete_item(video_id);
                        //删除sd卡
                        FileUtil.deleteFolder(path_urls + tid + video_id);
                        FileUtil.deleteFile(path_m3u8 + "//" + tid + video_id + ".m3u8");
                        FileUtil.deleteFile(path_key + "//" + tid + video_id + "key");
                        FileUtil.deleteFile(path_vr + "//" + tid + video_id + ".mp4");
                        //删除外置sd卡
                        if (!TextUtils.isEmpty(spu.getSecondSdcard())) {
                            FileUtil.delteallFile(path_urls_second + tid + video_id);
                            FileUtil.deleteFile(path_m3u8_second + "//" + tid + video_id + ".m3u8");
                            FileUtil.deleteFile(path_key_second + "//" + tid + video_id + "key");
                            FileUtil.deleteFile(path_vr_second + "//" + tid + video_id + ".mp4");
                        }
                        //如果删除的视频正在下载中,就停止下载，继续下载其他等待的视频
                        if (isdownloadStatus.equals("1")) {
                            stop_Dwonload();
                        }
                    }
                }
            }
            flag = true;
            return flag;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if (values[0] == 1) {
                if (!isFinishing()) {
                    dialog = MyPublicDialog.createLoadingDialog(Download_ExpandableList_Activity.this);
                    dialog.show();
                }
            }
        }
        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            if (result) {
                ArrayList<String> query_tree_allvideoids = DataBaseDao.getInstance(Download_ExpandableList_Activity.this).query_tree_allvideoids(treeid);
                if (query_tree_allvideoids.size() == 0) {
                    finish();
                } else {
                    itema.setTitle(getResources().getString(R.string.edit));
                    showCheckbox = false;
                    mSelectionMenuView.setVisibility(View.GONE);
                    bottom_fenge.setVisibility(View.GONE);
                    groupList.clear();
                    childList.clear();
                    DataBaseDao.getInstance(Download_ExpandableList_Activity.this).query_DownLoad_Tree_Chapters(treeid, groupList);
                    for (int i = 0; i < groupList.size(); i++) {
                        String tid = groupList.get(i).get("tid");
                        DataBaseDao.getInstance(Download_ExpandableList_Activity.this).query_DownLoad_Tree_Videos(treeid, tid, childList);
                    }
                    download_list.setAdapter(adapter);
                    for (int i = 0; i < groupList.size(); i++) {
                        download_list.expandGroup(i);
                    }
                    for (int i = 0; i < childList.size(); i++) {
                        ArrayList<HashMap<String, String>> arrayList = childList.get(i);
                        for (int j = 0; j < arrayList.size(); j++) {
                            HashMap<String, String> hashMap = arrayList.get(j);
                            String video_id = hashMap.get("id");
                            adapter.getIsSelected().put(video_id, false);
                        }
                    }
                    //删除后全部设为不选中状态
                    setCheckNum(0);
                }
                self_Download_Next_Video();
            } else {
            }
        }
    }

    /**
     * 自动下载下一个视频
     */
    public void self_Download_Next_Video() {

        ArrayList<String> query_All_DownStatus = DataBaseDao.getInstance(Download_ExpandableList_Activity.this).query_All_DownStatus();
        if (query_All_DownStatus.contains("1")) {
        } else {
            //自动下载本课程等待的视频
            ArrayList<HashMap<String, String>> query_Wait_Download = DataBaseDao.getInstance(Download_ExpandableList_Activity.this).query_Wait_Download(treeid);
            if (query_Wait_Download.size() != 0) {
                HashMap<String, String> hashMap2 = query_Wait_Download.get(0);
                DataBaseDao.getInstance(Download_ExpandableList_Activity.this).updata_DownStatus(hashMap2.get("id"), 1);
                int gourpsSum2 = adapter.getGroupCount();//组的数量
                for (int i = 0; i < gourpsSum2; i++) {
                    int childSum = adapter.getChildrenCount(i);//组中子项的数量
                    for (int j = 0; j < childSum; j++) {
                        HashMap<String, String> hashMap = childList.get(i).get(j);
                        String update_id = hashMap.get("id");
                        if (update_id.equals(hashMap2.get("id"))) {
                            hashMap.put("downloadStatus", "1");
                        }
                    }
                }
                downloadManager.addCourseVideoDownload(hashMap2.get("url_content"), hashMap2.get("treeid"), hashMap2.get("tid")
                        , hashMap2.get("id"), Integer.parseInt(hashMap2.get("mapKey"))
                        , hashMap2.get("name"), Long.parseLong(hashMap2.get("downloadedSize")),hashMap2.get("video_type"));
                updateAllOperationStatus();
            } else {
                //查询其他课程是否有等待的视频。
                ArrayList<HashMap<String, String>> allwaitList = DataBaseDao.getInstance(Download_ExpandableList_Activity.this).queryAllWaitCourseVideos();
                if (allwaitList.size() != 0) {
                    HashMap<String, String> hashMap2 = allwaitList.get(0);
                    DataBaseDao.getInstance(Download_ExpandableList_Activity.this).updata_DownStatus(hashMap2.get("id"), 1);
                    int gourpsSum2 = adapter.getGroupCount();//组的数量
                    for (int i = 0; i < gourpsSum2; i++) {
                        int childSum = adapter.getChildrenCount(i);//组中子项的数量
                        for (int j = 0; j < childSum; j++) {
                            HashMap<String, String> hashMap = childList.get(i).get(j);
                            String update_id = hashMap.get("id");
                            if (update_id.equals(hashMap2.get("id"))) {
                                hashMap.put("downloadStatus", "1");
                            }
                        }
                    }
                    downloadManager.addCourseVideoDownload(hashMap2.get("url_content"), hashMap2.get("treeid"), hashMap2.get("tid")
                            , hashMap2.get("id"), Integer.parseInt(hashMap2.get("mapKey"))
                            , hashMap2.get("name"), Long.parseLong(hashMap2.get("downloadedSize")),hashMap2.get("video_type"));
                } else {
                    //停止服务
                    DownloadService.stopService(Download_ExpandableList_Activity.this);
                }
            }
        }
    }


    /**
     * 判断全部开始||全部暂停的状态
     */
    public void updateAllOperationStatus() {
        downloadStatusList.clear();
        for (int i = 0; i < childList.size(); i++) {
            ArrayList<HashMap<String, String>> arrayList = childList.get(i);
            for (int j = 0; j < arrayList.size(); j++) {
                HashMap<String, String> hashMap = arrayList.get(j);
                String downloadStatus = hashMap.get("downloadStatus");
                downloadStatusList.add(downloadStatus);
            }
        }

        if (!downloadStatusList.contains("0") && !downloadStatusList.contains("1") && !downloadStatusList.contains("2")) {
            all_operation_ly.setVisibility(View.GONE);
        } else {
            all_operation_ly.setVisibility(View.VISIBLE);

            if (downloadStatusList.contains("2") && !downloadStatusList.contains("1") && !downloadStatusList.contains("0")
                    || downloadStatusList.contains("0") && !downloadStatusList.contains("2") && !downloadStatusList.contains("1")
                    || downloadStatusList.contains("0") && downloadStatusList.contains("2") && !downloadStatusList.contains("1")) {

                all_begin_ly.setVisibility(View.VISIBLE);
                all_pause_ly.setVisibility(View.GONE);
            } else {
                all_pause_ly.setVisibility(View.VISIBLE);
                all_begin_ly.setVisibility(View.GONE);
            }
        }
    }

    public int getCheckNum() {
        return checkNum;
    }

    public void setCheckNum(int checkNum) {
        this.checkNum = checkNum;
    }


    /**
     * 停止下载
     */
    public void stop_Dwonload() {

        ArrayList<Callback.Cancelable> getallhandler = downloadManager.getAllCacebles();
        if (getallhandler.size() >= 0) {
            for (int i = 0; i < getallhandler.size(); i++) {
                Callback.Cancelable cancelable= getallhandler.get(i);
                if (cancelable != null && !cancelable.isCancelled()) {
                    cancelable.cancel();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.download_editor_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(receiver);
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        super.onDestroy();
    }
}

package com.coder.kzxt.download;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.coder.kzxt.activity.R;
import com.coder.kzxt.db.DataBaseDao;
import com.coder.kzxt.dialog.util.MyPublicDialog;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.SdcardUtils;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.coder.kzxt.video.beans.CatalogueBean;
import com.coder.kzxt.views.ScrollHeaderListView;

import java.util.ArrayList;
import java.util.List;

public class VideoView_Download_Activity extends BaseActivity {

    private Toolbar mToolbarView;
    private SharedPreferencesUtil spu;
    private ScrollHeaderListView listView;
    private Button selection_all;
    private Button selection_download;
    private String treeid;
    private String tree_name;
    private String treepicture;
    private ArrayList<String> booleans;
    private List<CatalogueBean.ItemsBean> downloadList ;
    private DownloadVideoAdapter adapter;
    private int checkNum = 0; // 记录选中的条目数量
    private boolean selectAll;
    private RelativeLayout activity_video_view_download;
    private Dialog dialog_down_all;
    private DownloadManager downloadManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view_download);

        spu= new SharedPreferencesUtil(this);
        mToolbarView = (Toolbar) findViewById(R.id.toolbar);
        mToolbarView.setTitle(R.string.download_video_tx);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        listView = (ScrollHeaderListView) findViewById(R.id.download_catalogue_list);
        selection_all = (Button) findViewById(R.id.selection_all);
        selection_download = (Button) findViewById(R.id.selection_download);
        booleans = new ArrayList<String>();
        downloadList = new ArrayList<>();
        activity_video_view_download = (RelativeLayout) findViewById(R.id.activity_video_view_download);

        Bundle bundle = getIntent().getExtras();
        treeid = bundle.getString("treeid");
        tree_name = bundle.getString("tree_name");
        treepicture = bundle.getString("treepicture");
        downloadList = (List<CatalogueBean.ItemsBean>) bundle.getSerializable("downloadList");
        adapter= new DownloadVideoAdapter(this,downloadList,listView);

        listView.setHeaderView(LayoutInflater.from(VideoView_Download_Activity.this).inflate(R.layout.videoview_downlaod_group_item, null));
        listView.setAdapter(adapter);
        for (int i = 0; i < downloadList.size(); i++) {
            listView.expandGroup(i);
        }

        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

               CatalogueBean.ItemsBean.VideoBean videoBean = downloadList.get(groupPosition).getVideo().get(childPosition);
                String type = videoBean.getType();
                String video_id = videoBean.getId();

                if (type.equals("1")||type.equals("6")) {
                    // 改变CheckBox的状态
                    CheckBox video_check = (CheckBox) v.findViewById(R.id.video_check);
                    video_check.toggle();
                    // 将CheckBox的选中状况记录下来
                    adapter.getIsSelected().put(video_id, video_check.isChecked());
                    // 调整选定条目
                    if (video_check.isChecked() == true) {
                        setCheckNum(getCheckNum() + 1);
                    } else {
                        setCheckNum(getCheckNum() - 1);
                    }
                    selection_download.setText("下载（" + getCheckNum() + "）");
                }


                return false;
            }
        });

        initCheckBox();

        selection_all.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!selectAll) {
                    selection_all.setText(R.string.cancel_select_all);
                    int allcheckbox = 0;
                    for(int i=0;i<downloadList.size();i++){
                        List<CatalogueBean.ItemsBean.VideoBean> arrayList=  downloadList.get(i).getVideo();
                        for (int j = 0; j < arrayList.size(); j++) {
                           CatalogueBean.ItemsBean.VideoBean videoBean =arrayList.get(j);
                            String video_id = videoBean.getId();
                            String type = videoBean.getType();
                            if (type.equals("1")||type.equals("6")) {
                                allcheckbox++;
                                adapter.getIsSelected().put(video_id, true);
                            }
                        }
                    }
                    setCheckNum(allcheckbox);
                    selection_download.setText("下载（" + getCheckNum() + "）");
                    // 刷新listview和TextView的显示
                    adapter.notifyDataSetChanged();
                    selectAll = true;

                } else {
                    selection_all.setText(R.string.select_all);
                    initCheckBox();
                    setCheckNum(0);
                    selection_download.setText(R.string.download);
                    adapter.notifyDataSetChanged();
                    selectAll = false;

                }

            }
        });


        selection_download.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                    if (NetworkUtil.isNetworkAvailable(VideoView_Download_Activity.this)) {
                        String downloadFlag = spu.getDownloadFlag();
                        if (downloadFlag.equals("1")) {
                            if (Constants.API_LEVEL_11) {
                                new DownAllVideoAsyncTask().executeOnExecutor(Constants.exec);
                            } else {
                                new DownAllVideoAsyncTask().execute();
                            }

                        } else {
                            if (NetworkUtil.isWifiNetwork(VideoView_Download_Activity.this)) {

                                if (Constants.API_LEVEL_11) {
                                    new DownAllVideoAsyncTask().executeOnExecutor(Constants.exec);
                                } else {
                                    new DownAllVideoAsyncTask().execute();
                                }

                            } else {
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.not_support_flow), Toast.LENGTH_LONG).show();
                            }

                        }

                    } else {
                         NetworkUtil.httpNetErrTip(VideoView_Download_Activity.this,activity_video_view_download);
                    }

            }
        });

    }


    /**
     * 下载全部
     */
    private class DownAllVideoAsyncTask extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            boolean flag = false;
            boolean flag_down = false;
            publishProgress(1);

            if (spu.getSelectAddress() == 0) {
                if (!SdcardUtils.isExistSdcard() || SdcardUtils.getSDFreeSize(Environment.getExternalStorageDirectory().getPath()) < 100) {
                    publishProgress(2);
                    flag = false;
                    flag_down = false;
                } else {
                    flag_down = true;
                }

            } else if (spu.getSelectAddress() == 1) {

                if (SdcardUtils.getSDFreeSize(spu.getSecondSdcard()) < 100) {
                    publishProgress(3);
                    flag = false;
                    flag_down = false;
                } else {
                    flag_down = true;
                }

            }

            if (flag_down) {
                // 这个变量作用是把全部下载的第一个视频传递给下载服务
                boolean isdownloadservice = true;

                // 获得数据库中所有的视频id
                ArrayList<String> videoIds = DataBaseDao.getInstance(VideoView_Download_Activity.this).getVideoIds();
                // 查询数据库中所有下载状态
                ArrayList<String> query_All_DownStatus = DataBaseDao.getInstance(VideoView_Download_Activity.this).query_All_DownStatus();

                for (int i = 0; i < downloadList.size(); i++) {
                    List<CatalogueBean.ItemsBean.VideoBean> arrayList = downloadList.get(i).getVideo();
                    for (int j = 0; j < arrayList.size(); j++) {
                        CatalogueBean.ItemsBean.VideoBean videoBean = arrayList.get(j);
                        Boolean isdownload = adapter.getIsSelected().get(videoBean.getId());
                        if (isdownload) {
                            booleans.add("1");
                            String downUrl =videoBean.getMediaUri();
                            Log.v("tangcy","下载的url"+downUrl);
                            if (!TextUtils.isEmpty(downUrl) && downUrl.length() > 0) {
                                if (!videoIds.contains(videoBean.getId())) {
                                    // 判断视频是否有正在下载的视频
                                    if (query_All_DownStatus.contains("1")) {
                                        Log.v("tangcy", "全部下载--不把视频数据传给服务");
                                        // 如果有重复的课时就不添加
                                        String query_item_filename = DataBaseDao.getInstance(VideoView_Download_Activity.this).query_item_filename(videoBean.getId());
                                        if (TextUtils.isEmpty(query_item_filename)) {
                                            DataBaseDao.getInstance(VideoView_Download_Activity.this).initDownloadData("","",videoBean.getId()
                                                    , videoBean.getCourse_chapter_id(), videoBean.getTitle(), downUrl, treepicture
                                                    , downloadList.get(i).getTitle(), 0, 0, Integer.parseInt(treeid), tree_name, treepicture
                                                    , Integer.parseInt(downloadList.get(i).getSequence()), Integer.parseInt(videoBean.getSequence()),videoBean.getType(),videoBean.getIs_drag());
                                        }

                                    } else {
                                        if (isdownloadservice) {

                                            String query_item_filename = DataBaseDao.getInstance(VideoView_Download_Activity.this).query_item_filename(videoBean.getId());
                                            Log.v("tangcy","下载的视频名字"+query_item_filename);
                                            if (TextUtils.isEmpty(query_item_filename)) {
                                                DataBaseDao.getInstance(VideoView_Download_Activity.this).initDownloadData("","",
                                                        videoBean.getId(), videoBean.getCourse_chapter_id(), videoBean.getTitle(), downUrl, treepicture,
                                                        downloadList.get(i).getTitle(), 1, 0, Integer.parseInt(treeid), tree_name, treepicture,
                                                        Integer.parseInt(downloadList.get(i).getSequence()), Integer.parseInt(videoBean.getSequence()),videoBean.getType(),videoBean.getIs_drag());

                                                downloadManager = DownloadService.getDownloadManager(VideoView_Download_Activity.this);
                                                downloadManager.addCourseVideoDownload(downUrl, treeid, videoBean.getCourse_chapter_id(), videoBean.getId(), 0, videoBean.getTitle(), 0,videoBean.getType());
                                            }

                                            isdownloadservice = false;

                                        } else {

                                            Log.v("tangcy", "全部下载--不把视频数据传下载线程");
                                            String query_item_filename = DataBaseDao.getInstance(VideoView_Download_Activity.this).query_item_filename(videoBean.getId());
                                            if (TextUtils.isEmpty(query_item_filename)) {
                                                DataBaseDao.getInstance(VideoView_Download_Activity.this).initDownloadData("","",videoBean.getId(), videoBean.getCourse_chapter_id(),
                                                        videoBean.getTitle(), downUrl, treepicture, downloadList.get(i).getTitle(), 0, 0,
                                                        Integer.parseInt(treeid), tree_name, treepicture, Integer.parseInt(downloadList.get(i).getSequence()),
                                                        Integer.parseInt(videoBean.getSequence()),videoBean.getType(),videoBean.getIs_drag());

                                            }

                                        }
                                    }

                                }
                            }

                        } else {
                            booleans.add("0");
                        }

                    }
                }
                flag = true;

            }

            return flag;

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if (values[0] == 1) {
                dialog_down_all = MyPublicDialog.createLoadingDialog(VideoView_Download_Activity.this);
                dialog_down_all.show();
            } else if (values[0] == 2) {
                Toast.makeText(VideoView_Download_Activity.this, getResources().getString(R.string.dowmload_fail_or_else), Toast.LENGTH_LONG).show();

            } else if (values[0] == 3) {
                Toast.makeText(VideoView_Download_Activity.this, getResources().getString(R.string.dowmload_fail_or_else), Toast.LENGTH_LONG).show();

            } else if (values[0] == 4) {
                Toast.makeText(VideoView_Download_Activity.this, getResources().getString(R.string.dowmload_fail_or_else), Toast.LENGTH_LONG).show();

            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            if (VideoView_Download_Activity.this==null) {
                return;
            }
            if (dialog_down_all != null && dialog_down_all.isShowing()) {
                dialog_down_all.cancel();
            }

            if (result) {
                selection_all.setText("全选");
                initCheckBox();
                setCheckNum(0);
                selection_download.setText("下载");
                adapter.notifyDataSetChanged();

                if (booleans.contains("1")) {
                    Toast.makeText(VideoView_Download_Activity.this, getResources().getString(R.string.add_download_list), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(VideoView_Download_Activity.this, getResources().getString(R.string.select_download_video), Toast.LENGTH_SHORT).show();
                }

            }

        }
    }



    // 根据id初始化childitem的checkbox
    private void initCheckBox() {
        for(int i=0;i<downloadList.size();i++){
           List<CatalogueBean.ItemsBean.VideoBean> videoBeanList = downloadList.get(i).getVideo();
            for(int j=0;j<videoBeanList.size();j++){
               CatalogueBean.ItemsBean.VideoBean videoBean = videoBeanList.get(j);
                String video_id =  videoBean.getId();
                adapter.getIsSelected().put(video_id, false);
            }

        }

    }

    public int getCheckNum() {
        return checkNum;
    }

    public void setCheckNum(int checkNum) {
        this.checkNum = checkNum;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
     }
        return super.onOptionsItemSelected(item);
   }
}
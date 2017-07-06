package com.coder.kzxt.video.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.text.TextUtils;
import android.view.Surface;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.app.http.HttpCallBack;
import com.app.http.builders.HttpPostBuilder;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.beans.BaseBean;
import com.coder.kzxt.db.DataBaseDao;
import com.coder.kzxt.download.DownloadManager;
import com.coder.kzxt.download.DownloadService;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.coder.kzxt.utils.ToastUtils;
import com.app.http.UrlsNew;
import com.coder.kzxt.utils.Utils;
import com.coder.kzxt.video.adapter.SelectsAdapter;
import com.coder.kzxt.video.beans.CatalogueBean;
import com.coder.kzxt.views.SelfLayout;
import com.utovr.player.UVEventListener;
import com.utovr.player.UVInfoListener;
import com.utovr.player.UVMediaPlayer;
import com.utovr.player.UVMediaType;
import com.utovr.player.UVPlayerCallBack;
import com.utovr.player.UVReaderType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by wangtingshun on 2016/7/18.
 */
public class VrVideoActivity extends Activity implements UVPlayerCallBack,View.OnClickListener,SelfLayout.OnClickEventListener {

    /**返回键*/
    private ImageView back;
    /**视频标题*/
    private TextView title;
    /**问答*/
    private LinearLayout ask_ly;
    /**分享*/
    private LinearLayout share_ly;
    /**下载*/
    private LinearLayout download_ly;
    /**视频播放url*/
    private String Path; //setSource UVMediaType.UVMEDIA_TYPE_M3U8;
    /**选集*/
    private ImageView selectView;
    /**播放器布局*/
    private  SelfLayout rlPlayView;
    /**底部工具条*/
    private RelativeLayout rlToolbar;
    private SharedPreferencesUtil spu;
    private Handler handler = null;
    private UVMediaPlayer mMediaplayer = null;  // 媒体视频播放器
    private ToggleButton playpauseBtn;          // 启动、暂停按钮
    protected SeekBar time_Seekbar;             // 播放进度条
    protected TextView time_TextView;           // 时间长度
    private String videoTimeString = null;      // 时间长度文本
    protected ToggleButton gyroBtn;             // 陀螺仪控制按钮
    protected ToggleButton screenBtn;           // 单双屏
    private PowerManager.WakeLock mWakeLock = null;
    private boolean bufferResume = true;
    private boolean needBufferAnim = false;
    private RelativeLayout imgBuffer;
    private SelectsAdapter adapter;// 缓冲动画
    private ListView selectListView; //选集list
    private int isJoin;//0未加入 1学生 2老师 3创建课程的用户
    //选集数据
    private List<CatalogueBean.ItemsBean.VideoBean> selects = new ArrayList<>();
    private static final String TAG = "VR";
    private DownloadManager downloadManager;
    private CatalogueBean.ItemsBean.VideoBean videoBean;
    private ArrayList<HashMap<String, String>> ids;
    private String tid; //章节id
    private String catoTitle; //章节title
    private String catoSequence; //章节sequence
    private String treeid;
    private String tree_name;
    private String treepicture;
    private boolean isReport = false;
    private String download;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vr_layout);
        handler = new Handler();
        spu = new SharedPreferencesUtil(this);
        ids = new ArrayList<>();
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        mWakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "mytag");
        mWakeLock.acquire();
        getIntentData();
        initView();
        initListener();
    }

    private void getIntentData() {
        videoBean = (CatalogueBean.ItemsBean.VideoBean)getIntent().getSerializableExtra("videoBean");
        Path = videoBean.getMediaUri();
        tid = getIntent().getStringExtra("tid");
        catoTitle = getIntent().getStringExtra("catoTitle");
        catoSequence = getIntent().getStringExtra("catoSequence");
        treeid = getIntent().getStringExtra("treeid");
        tree_name = getIntent().getStringExtra("tree_name");
        treepicture = getIntent().getStringExtra("treepicture");
        isJoin = getIntent().getIntExtra("isJoin",-1);
        download = getIntent().getStringExtra("download");
    }


    /**
     * 初始化view
     */
    private void initView() {
        // 工具栏上的按钮
        gyroBtn = (ToggleButton) findViewById(R.id.video_toolbar_btn_gyro);// 陀螺仪
        screenBtn = (ToggleButton) findViewById(R.id.video_toolbar_btn_screen);// 单双屏
        playpauseBtn = (ToggleButton) findViewById(R.id.video_toolbar_btn_playpause);// 播放/暂停
        time_Seekbar = (SeekBar) findViewById(R.id.video_toolbar_time_seekbar);// 进度
        imgBuffer = (RelativeLayout) findViewById(R.id.buffer_progress_layout);
        time_TextView = (TextView) findViewById(R.id.video_toolbar_time_tv);// 时间
        //初始化播放器
        rlPlayView = (SelfLayout)findViewById(R.id.video_rlPlayView);
        mMediaplayer = new UVMediaPlayer(VrVideoActivity.this, rlPlayView);
        rlToolbar = (RelativeLayout) findViewById(R.id.video_rlToolbar);
        selectListView = (ListView) findViewById(R.id.selects_list);
        mMediaplayer.setToolbar(rlToolbar, null, null);
        rlPlayView.getChildAt(1).setVisibility(View.GONE);
        rlPlayView.getChildAt(2).setVisibility(View.GONE);

        selectView = (ImageView) findViewById(R.id.selectImg);
        back = (ImageView) findViewById(R.id.img_back);
        title = (TextView) findViewById(R.id.title);
        share_ly = (LinearLayout) findViewById(R.id.share_ly);
        ask_ly = (LinearLayout) findViewById(R.id.ask_ly);
        download_ly = (LinearLayout) findViewById(R.id.download_ly);
        selectView.setVisibility(View.GONE);  //暂时隐藏选集
        title.setText(videoBean.getTitle());
        if(!TextUtils.isEmpty(download)){
            download_ly.setVisibility(View.GONE);
        } else {
            download_ly.setVisibility(View.VISIBLE);
        }
        gyroBtn.setChecked(true);
        screenBtn.setChecked(true);
    }

    /**
     * 初始化监听器
     */
    private void initListener() {

        // 陀螺仪按钮事件
        gyroBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mMediaplayer != null) {
                    mMediaplayer.setGyroEnabled(!mMediaplayer.isGyroEnabled());
                    gyroBtn.setChecked(mMediaplayer.isGyroEnabled());
                }
            }
        });
        // 单双屏按钮事件
        screenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMediaplayer != null) {
                    boolean isScreen = !mMediaplayer.isDualScreenEnabled();
                    mMediaplayer.setDualScreenEnabled(isScreen);
                    if (isScreen) {
                        mMediaplayer.setGyroEnabled(true);
                        gyroBtn.setChecked(true);
                        gyroBtn.setEnabled(false);
                    } else {
                        mMediaplayer.setGyroEnabled(false);
                        gyroBtn.setChecked(false);
                        gyroBtn.setEnabled(true);
                    }
                }
            }
        });
        // 播放/暂停按钮事件
        playpauseBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (((ToggleButton) v).isChecked()) {
                    if (mMediaplayer != null && mMediaplayer.isInited()) {//暂停媒体视频
                        mMediaplayer.pause();
                    }
                } else {
                    if (mMediaplayer != null && mMediaplayer.isInited()) {//播放媒体视频
                        mMediaplayer.play();
                    }
                }
            }
        });
        // 进度条事件
        time_Seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mMediaplayer != null && mMediaplayer.isInited()) {
                    mMediaplayer.seekTo(seekBar.getProgress());
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }
        });

        rlPlayView.setOnClickEventListener(this);
        selectView.setOnClickListener(this);
        back.setOnClickListener(this);
        title.setOnClickListener(this);
        share_ly.setOnClickListener(this);
        ask_ly.setOnClickListener(this);
        download_ly.setOnClickListener(this);
        selectListView.setOnItemClickListener(onItemListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mMediaplayer != null) {
            mMediaplayer.onResume(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mMediaplayer != null) {
            mMediaplayer.onPause();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWakeLock.release();
        if (mMediaplayer != null) {
            mMediaplayer.release();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
//        if(!spu.getEncr(tid,videoBean.getService_id())){
//            boolean encry = VideoAesUtil.encrypt(videoBean.getMediaUri());
//            if(encry){
//                spu.setEncr(tid,videoBean.getService_id(),true);
//            }
//        }
        super.onStop();
    }


    @Override
    protected void onRestart() {
//        if(spu.getEncr(tid,videoBean.getService_id())){
//            boolean encry = VideoAesUtil.encrypt(videoBean.getMediaUri());
//            if(encry){
//                spu.setEncr(tid,videoBean.getService_id(),false);
//            }
//
//        }
        super.onRestart();
    }

    /**
     * SDK已经将播放器环境已设置好，可以播放了
     */
    @Override
    public void createEnv(final Surface surface) {
        if (mMediaplayer != null && mMediaplayer.isInited()) {
            mMediaplayer.setSurface(surface);
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                {
                    try {
                        // 创建媒体视频播放器
                            mMediaplayer.initPlayer();
                            mMediaplayer.setListener(mListener);
                            mMediaplayer.setSurface(surface);
                            mMediaplayer.setInfoListener(mInfoListener);
                            try {
                                if (Path.contains(".m3u8")) {
                                    mMediaplayer.setSource(UVMediaType.UVMEDIA_TYPE_M3U8, Path);
                                } else if (Path.contains(".mp4")) {
                                    mMediaplayer.setSource(UVMediaType.UVMEDIA_TYPE_MP4, Path);
                                }
                                needBufferAnim = true;
                                mMediaplayer.setGyroEnabled(!mMediaplayer.isGyroEnabled());
                                mMediaplayer.setDualScreenEnabled(!mMediaplayer.isDualScreenEnabled());
                            } catch (IllegalStateException t) {
                                t.printStackTrace();
                            }

                    } catch (Exception e) {
                    }
                }
            }
        });

    }

    /**
     * @param CurPostion 播放进度
     */
    @Override
    public void updateProgress(long CurPostion) {
        Message msg = handleProgress.obtainMessage(0, (int)CurPostion, 0);
        handleProgress.sendMessage(msg);
    }

    private UVEventListener mListener = new UVEventListener() {
        @Override
        public void onRenderTypeChanged(UVReaderType uvReaderType) {

        }

        @Override
        public void onGyroCtrl(int i, String s) {
            switch (i) {
                case UVEventListener.GYRO_CTRL_PLAY:
                    playpauseBtn.setChecked(false);
                    break;
                case UVEventListener.GYRO_CTRL_PAUSE:
                    playpauseBtn.setChecked(true);
                    break;
            }
        }

        @Override
        public void onStateChanged(int playbackState) {
            switch (playbackState) {
                case UVMediaPlayer.STATE_PREPARING:
                    setBufferVisibility(true);
                    break;
                case UVMediaPlayer.STATE_BUFFERING:
                    if (needBufferAnim && mMediaplayer != null && mMediaplayer.isPlaying()) {
                        bufferResume = true;
                        setBufferVisibility(true);
                    }
                    break;
                case UVMediaPlayer.STATE_READY:
                    // 设置时间和进度条
                    setInfo();
                    if (bufferResume) {
                        bufferResume = false;
                        setBufferVisibility(false);
                    }
                    break;
                case UVMediaPlayer.STATE_ENDED:
                    //这里是循环播放，可根据需求更改
//                    mMediaplayer.replay();
//                    mMediaplayer.release();
                    mMediaplayer.pause();
                    playpauseBtn.setChecked(true);
                    postComData();
                    break;
                case UVMediaPlayer.TRACK_DISABLED:
                case UVMediaPlayer.TRACK_DEFAULT:
                    break;
            }
        }

        @Override
        public void onError(Exception e, int ErrType) {
            switch (ErrType) {
                case UVEventListener.ERR_TIMEOUT:
                    toast("网络超时");
                    break;
                case UVEventListener.ERR_INIT:
                case UVEventListener.ERR_RENDER_INIT:
                case UVEventListener.ERR_DECODE:
                    toast("不支持该视频格式");
                    break;
                case UVEventListener.ERR_WRITE:
                    toast("WriteError");
                    break;
                case UVEventListener.ERR_LOAD:
                    toast("获得数据失败");
                    break;
                default:
                    toast("onError");
                    break;
            }
        }

        @Override
        public void onVideoSizeChanged(int width, int height) {
        }
    };

    private UVInfoListener mInfoListener = new UVInfoListener() {
        @Override
        public void onBandwidthSample(int elapsedMs, long bytes, long bitrateEstimate) {
        }

        @Override
        public void onLoadStarted() {
        }

        @Override
        public void onLoadCompleted() {
            if (bufferResume) {
                bufferResume = false;
                setBufferVisibility(false);
            }
            /*
            * 缓冲进度
            * 这里比较偷懒的做法是利用m3u8分片原理更新进度，网络播放MP4这种做法肯定是不对的
            * 你完全可以创建一个定时器调用 mMediaplayer.getBufferedPosition()
            */
            time_Seekbar.setSecondaryProgress((int) mMediaplayer.getBufferedPosition());
        }
    };


    /**
     * 设置时间和进度条初始信息
     */
    public void setInfo() {
        int duration = 0;
        if (mMediaplayer != null) {
            duration = (int)mMediaplayer.getDuration();
        }
        if (duration == time_Seekbar.getMax()) {
            return;
        }
        // 设置控制条,放在加载完成以后设置，防止获取getDuration()错误
        time_Seekbar.setProgress(0);
        time_Seekbar.setMax(duration);
        videoTimeString = Utils.getShowTime(duration);
        time_TextView.setText("00:00:00/" + videoTimeString);
    }

    private void toast(final String msg) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(VrVideoActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 缓冲动画控制
     * @param Visible
     */
    private void setBufferVisibility(boolean Visible) {
        if (Visible) {
            imgBuffer.setVisibility(View.VISIBLE);
        } else {
            imgBuffer.setVisibility(View.GONE);
        }
    }

    /**
     * 更新进度条
     */
    public Handler handleProgress = new Handler() {
        public void handleMessage(Message msg) {
            int position = msg.arg1;
            if (position >= 0 && videoTimeString != null) {
                time_Seekbar.setProgress(position);
                String cur = Utils.getShowTime(position);
                time_TextView.setText(cur + "/" + videoTimeString);
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back://返回
                if(isReport){
                    setResult(8);
                }
                finish();
                break;
            case R.id.ask_ly: //问答

                break;
            case R.id.share_ly: //分享

                break;
            case R.id.selectImg: //选集
                selectVideo();
                break;
            case R.id.download_ly://下载
                downLoadVR();
                break;
            default:
                break;
        }

    }

    private void selectVideo() {
        if(adapter == null){
            adapter = new SelectsAdapter(VrVideoActivity.this, selects,isJoin);
            selectListView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
        for (int i = 0; i < selects.size(); i++) {
            if (selects.get(i).getLast_location().equals("1")) {
                selectListView.setSelection(i);
            }
        }
    }

    @Override
    public void OnClickEvent() {

    }

    /**
     * 点击选集中某一条目
     */
    private AdapterView.OnItemClickListener onItemListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        }
    };

    private void downLoadVR() {
        // 获得数据库中所有的视频id
        ArrayList<String> videoIds = DataBaseDao.getInstance(VrVideoActivity.this).getVideoIds();
        // 查询数据库中所有下载状态
        ArrayList<String> query_All_DownStatus = DataBaseDao.getInstance(VrVideoActivity.this).query_All_DownStatus();

        String downUrl = videoBean.getMediaUri();
        if (!TextUtils.isEmpty(downUrl) && downUrl.length() > 0) {
            if (!videoIds.contains(videoBean.getId())) {
                // 判断视频是否有正在下载的视频
                if (query_All_DownStatus.contains("1")) {
                    // 如果有重复的课时就不添加
                    String query_item_filename = DataBaseDao.getInstance(VrVideoActivity.this).query_item_filename(videoBean.getId());
                    if (TextUtils.isEmpty(query_item_filename)) {
                        DataBaseDao.getInstance(VrVideoActivity.this).initDownloadData("", "", videoBean.getId()
                                , videoBean.getCourse_chapter_id(), videoBean.getTitle(), downUrl, treepicture
                                , catoTitle, 0, 0, Integer.parseInt(treeid), tree_name, treepicture
                                , Integer.parseInt(catoSequence), Integer.parseInt(videoBean.getSequence()), videoBean.getType(), videoBean.getIs_drag());
                    }

                } else {
                    String query_item_filename = DataBaseDao.getInstance(VrVideoActivity.this).query_item_filename(videoBean.getId());

                    if (TextUtils.isEmpty(query_item_filename)) {
                        DataBaseDao.getInstance(VrVideoActivity.this).initDownloadData("", "",
                                videoBean.getId(), videoBean.getCourse_chapter_id(), videoBean.getTitle(), downUrl, treepicture,
                                catoTitle, 1, 0, Integer.parseInt(treeid), tree_name, treepicture,
                                Integer.parseInt(catoSequence), Integer.parseInt(videoBean.getSequence()), videoBean.getType(), videoBean.getIs_drag());

                        downloadManager = DownloadService.getDownloadManager(VrVideoActivity.this);
                        downloadManager.addCourseVideoDownload(downUrl, treeid, videoBean.getCourse_chapter_id(), videoBean.getId(), 0, videoBean.getTitle(), 0, videoBean.getType());
                    }
                }

            }
            ToastUtils.makeText(this,"已加入下载队列",Toast.LENGTH_SHORT).show();
        }

    }

    //上报 //老师不上报视频进度
    private void postComData() {
        if(isJoin!=2&&isJoin!=3){

            new HttpPostBuilder(VrVideoActivity.this).setClassObj(BaseBean.class).setHttpResult(new HttpCallBack() {
                @Override
                public void setOnSuccessCallback(int requestCode, Object resultBean) {
                    isReport = true;
                }

                @Override
                public void setOnErrorCallback(int requestCode, int code, String msg) {
                    //储存未上报成功的视频
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("id", videoBean.getId());
                    ids.add(map);
                    spu.setLessinIdList(ids);
                    isReport = true;
                }
            }).setUrl(UrlsNew.POST_LESSON_FINISH)
//                                    .addBodyParam(jsonArray.toString())
                    .addBodyParam("id", videoBean.getId())
                    .build();
        }
    }

}

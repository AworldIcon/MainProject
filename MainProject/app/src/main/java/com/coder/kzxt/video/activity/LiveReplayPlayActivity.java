package com.coder.kzxt.video.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Display;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.http.HttpCallBack;
import com.app.http.builders.HttpGetBuilder;
import com.app.utils.L;
import com.baidu.cyberplayer.core.BVideoView;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.base.beans.LiveBean;
import com.coder.kzxt.recyclerview.MyRecyclerView;
import com.coder.kzxt.recyclerview.adapter.BaseRecyclerAdapter;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.DateUtil;
import com.coder.kzxt.utils.DensityUtil;
import com.coder.kzxt.utils.FullScreenUtils;
import com.coder.kzxt.utils.GlideUtils;
import com.coder.kzxt.utils.NetworkUtil;
import com.app.http.UrlsNew;
import com.coder.kzxt.video.adapter.LiveReplayDelegate;
import com.coder.kzxt.video.beans.ReplayBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.coder.kzxt.utils.Constants.BUFFERING_END;
import static com.coder.kzxt.utils.Constants.BUFFERING_START;
import static com.coder.kzxt.utils.Constants.EVENT_PLAY;
import static com.coder.kzxt.utils.Constants.GONE_PROGRESSBAR;
import static com.coder.kzxt.utils.Constants.PLAY_COMPLETE;
import static com.coder.kzxt.utils.Constants.UI_EVENT_UPDATE_CURRPOSITION;


public class LiveReplayPlayActivity extends BaseActivity implements BVideoView.OnPreparedListener,
        BVideoView.OnCompletionListener, BVideoView.OnErrorListener,
        BVideoView.OnInfoListener, BVideoView.OnPlayingBufferCacheListener,
        BVideoView.OnCompletionWithParamListener,HttpCallBack {

    private Toolbar toolbar;
    private RelativeLayout activity_live_replay_play;
    private RelativeLayout video_ly;
    private RelativeLayout ssss;
    private RelativeLayout mViewHolder = null;
    private LiveMediaController mediaController = null;
    private ImageView buffer_bj;
    private ProgressBar loading_video;
    private RelativeLayout video_top_ly;
    private RelativeLayout time_ly;
    private TextView time_tv;
    private ImageButton img_back;
    private ImageView lockImg;
    private TextView title;
    private MyRecyclerView replay_recycler;
    private LiveReplayDelegate liveReplayDelegate;
    private BaseRecyclerAdapter listAdapter;
    private LiveBean  liveBean;
    private EventHandler mEventHandler;
    private HandlerThread mHandlerThread;

    private String ak = Constants.BAIDU_VIDEOVIEW_AK; // 请录入您的AK
    private BVideoView videoView = null;
    private ImageButton getScreen_hv;
    private GestureDetector mGestureDetector;// 手势
    private boolean isLock = false;//是否锁住屏幕
    private boolean firstScroll = false;// 每次触摸屏幕后，第一次scroll的标志
    private Timer barTimer;
    private volatile boolean isFullScreen = false;//是否全屏
    private volatile boolean isReadyForQuit = true;

    private RelativeLayout gesture_progress_layout;
    private ImageView gesture_iv_progress;// 快进或快退标志
    private TextView geture_tv_progress_cut_time;// 播放当前时间进度
    private TextView geture_tv_progress_time;// 播放总时间进度
    private int GESTURE_FLAG = 0;// 1,调节进度，2，调节音量
    private static final int GESTURE_MODIFY_PROGRESS = 1;
    private static final int GESTURE_MODIFY_VOLUME = 2;
    private static final float STEP_PROGRESS = 2f;// 设定进度滑动时的步长，避免每次滑动都改变，导致改变过快

    private int currPosition;
    private int duration;

    private int maxVolume = 0; // 最大音量
    private int currentVolume = -1; // 当前音量
    private AudioManager mAudioManager;
    private ImageView operation_bg; // 音量图片
    private View operation_volume_brightness; // 手势用的音量和亮度布局
    private ImageView operation_percent; // 音量进度条
    private float mBrightness = -1f; // 当前亮度

    private String playUrl;

    /**
     * 播放状态
     */
    public enum PlayerStatus {
        PLAYER_IDLE, PLAYER_PREPARING, PLAYER_PREPARED, PLAYER_COMPLETED
    }

    private PlayerStatus mPlayerStatus = PlayerStatus.PLAYER_IDLE;
    private final Object syncPlaying = new Object();

    private List<ReplayBean.ItemsBean> list;

    private LinearLayout jiazai_layout;
    private LinearLayout no_info_layout;
    private LinearLayout load_fail_layout;
    private Button load_fail_button;
    private  ReplayBean replayBean;

    public static void gotoActivity(Context context,LiveBean liveBean) {
        context.startActivity(new Intent(context, LiveReplayPlayActivity.class).putExtra("liveBean", liveBean));
    }

    class EventHandler extends Handler {
        public EventHandler(Looper looper) {
            super(looper);
        }
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case EVENT_PLAY:
                    /**
                     * 如果已经播放了，等待上一次播放结束
                     */
                    if (mPlayerStatus == PlayerStatus.PLAYER_PREPARING
                            || mPlayerStatus == PlayerStatus.PLAYER_PREPARED) {
                        synchronized (syncPlaying) {
                            try {
                                syncPlaying.wait(2 * 1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    /**
                     * 设置播放url
                     * http://gkkskijidms30qudc3v.exp.bcevod.com/mda-gkkswvrb2zhp41ez/mda-gkkswvrb2zhp41ez.m3u8
                     */
                    L.v("tangcy","播放的url="+playUrl);
                    videoView.setVideoPath(playUrl);

                    /**
                     * 续播，如果需要如此
                     */
//                    if (mLastPos > 0) {
//                        videoView.seekTo(mLastPos);
//                    }
                    /**
                     * 显示或者隐藏缓冲提示
                     */
                    videoView.showCacheInfo(false);
                    /**
                     * 开始播放
                     */
                    videoView.start();

                    /**
                     * 已经开启播放，必须有结束消息后才能结束
                     */
                    isReadyForQuit = false;

                    changeStatus(PlayerStatus.PLAYER_PREPARING);
                    break;
                default:
                    break;
            }
        }
    }



    Handler mHandler = new Handler() {

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public void handleMessage(Message msg) {

            switch (msg.what) {

                case UI_EVENT_UPDATE_CURRPOSITION:
//                    duration = videoView.getDuration();
//                    currPosition = videoView.getCurrentPosition();
//                    mHandler.sendEmptyMessageDelayed(UI_EVENT_UPDATE_CURRPOSITION, 200);
                    break;

                case BUFFERING_START:
                    loading_video.setVisibility(View.VISIBLE);
                    break;

                case BUFFERING_END:
                    loading_video.setVisibility(View.GONE);
                    break;

                case GONE_PROGRESSBAR:
                    loading_video.setVisibility(View.GONE);
                    buffer_bj.setVisibility(View.GONE);
                    video_top_ly.setBackgroundResource(R.color.transparent);
                    break;

                case PLAY_COMPLETE:
                    buffer_bj.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_live_replay_play);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);//防闪屏

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        liveBean  = (LiveBean) getIntent().getSerializableExtra("liveBean");
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(liveBean.getLive_title());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        list = new ArrayList<>();
        // 获得当前声音大小
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        /**
         * 开启后台事件处理线程
         */
        mHandlerThread = new HandlerThread("event handler thread", Process.THREAD_PRIORITY_BACKGROUND);
        mHandlerThread.start();
        mEventHandler = new EventHandler(mHandlerThread.getLooper());

        initView();

        liveReplayDelegate = new LiveReplayDelegate(this);
        listAdapter = new BaseRecyclerAdapter(this,list,liveReplayDelegate);
        replay_recycler.setAdapter(listAdapter);

        initOnclick();
        requestReplyData();
        SetVerticalScreen();
    }

    private void initView(){

        jiazai_layout = (LinearLayout)findViewById(R.id.jiazai_layout);
        jiazai_layout.setVisibility(View.VISIBLE);
        no_info_layout = (LinearLayout)findViewById(R.id.no_info_layout);
        load_fail_layout = (LinearLayout)findViewById(R.id.load_fail_layout);
        load_fail_button = (Button)findViewById(R.id.load_fail_button);
        activity_live_replay_play = (RelativeLayout) findViewById(R.id.activity_live_replay_play);
        ssss = (RelativeLayout) findViewById(R.id.ssss);
        video_ly = (RelativeLayout) findViewById(R.id.video_ly);
        mViewHolder = (RelativeLayout) findViewById(R.id.view_holder);
        mediaController = (LiveMediaController) findViewById(R.id.media_controller_bar);
        buffer_bj = (ImageView) findViewById(R.id.buffer_bj);

        GlideUtils.loadCourseImg(LiveReplayPlayActivity.this,liveBean.getPicture(),buffer_bj);

        loading_video = (ProgressBar) findViewById(R.id.loading_video);
        time_ly = (RelativeLayout) findViewById(R.id.time_ly);
        time_tv = (TextView) findViewById(R.id.time_tv);
        time_tv.setText("历史直播时间："+DateUtil.getHourMin(Long.parseLong(liveBean.getStart_time()))
                +"--"+DateUtil.getHourMin(Long.parseLong(liveBean.getEnd_time())));

        video_top_ly = (RelativeLayout) findViewById(R.id.video_top_ly);
        img_back = (ImageButton) findViewById(R.id.img_back);
        lockImg = (ImageView) findViewById(R.id.lockImg);
        title = (TextView) findViewById(R.id.title);

        gesture_progress_layout= (RelativeLayout) findViewById(R.id.gesture_progress_layout);
        gesture_iv_progress = (ImageView) findViewById(R.id.gesture_iv_progress);
        geture_tv_progress_cut_time = (TextView) findViewById(R.id.geture_tv_progress_cut_time);
        geture_tv_progress_time = (TextView) findViewById(R.id.geture_tv_progress_time);
        replay_recycler = (MyRecyclerView) findViewById(R.id.replay_recycler);


        operation_bg = (ImageView) findViewById(R.id.operation_bg);
        operation_volume_brightness = findViewById(R.id.operation_volume_brightness);
        operation_percent = (ImageView) findViewById(R.id.operation_percent);

        /**
         * 设置ak
         */
        BVideoView.setAK(ak);
        videoView = new BVideoView(this);
        videoView.setLogLevel(0);

        mViewHolder.addView(videoView);

        /**
         * 注册listener
         */
        videoView.setOnPreparedListener(this);
        videoView.setOnCompletionListener(this);
        videoView.setOnCompletionWithParamListener(this);
        videoView.setOnErrorListener(this);
        videoView.setOnInfoListener(this);
        mGestureDetector = new GestureDetector(this, new MyGestureListener());
        mediaController.setMediaPlayerControl(videoView);
        getScreen_hv = this.mediaController.getScreen_hv();
        /**
         * 设置解码模式 为保证兼容性，当前仅支持软解
         */
        videoView.setDecodeMode(BVideoView.DECODE_SW);
        videoView.selectResolutionType(BVideoView.RESOLUTION_TYPE_AUTO);

    }


    private void initOnclick(){

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isLock){
                    if(isFullScreen){
                        SetVerticalScreen();
                    }else {
                        finish();
                    }
                }
            }
        });

        getScreen_hv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isFullScreen) {
                    //竖屏
                    SetVerticalScreen();
                } else {
                    //全屏
                    setTransverseScreen();
                }

            }
        });

        lockImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isLock) {
                    lockImg.setImageResource(R.drawable.locked);
                    if (barTimer != null) {
                        barTimer.cancel();
                        barTimer = null;
                    }
                    mediaController.hide();
                    video_top_ly.setVisibility(View.GONE);
                    isLock = true;

                } else {
                    lockImg.setImageResource(R.drawable.unlocked);
                    isLock = false;
                }
            }
        });

        load_fail_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 load_fail_layout.setVisibility(View.GONE);
                 jiazai_layout.setVisibility(View.VISIBLE);
                 requestReplyData();
            }
        });

        listAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                playUrl = replayBean.getItems().get(position).getVideo_url();
                if(!TextUtils.isEmpty(playUrl)){
                    loading_video.setVisibility(View.VISIBLE);
                    videoView.stopPlayback();
                    mEventHandler.sendEmptyMessage(EVENT_PLAY);
                }

                listAdapter.setSelectPosition(position);
                listAdapter.notifyDataSetChanged();

            }
        });

        video_ly.setClickable(true);

    }


    private void requestReplyData(){
        new HttpGetBuilder(this).setClassObj(ReplayBean.class)
                .setUrl(UrlsNew.GET_LIVE_VIDEO)
                .setHttpResult(this)
                .addQueryParams("liveId",liveBean.getId())
                .addQueryParams("page","1")
                .addQueryParams("pageSize","200")
                .addQueryParams("center","1")
                .build();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setTransverseScreen() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
        video_top_ly.setBackgroundResource(R.color.trans_black);
        video_top_ly.setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        ssss.setLayoutParams(lp);
        toolbar.setVisibility(View.GONE);
        video_ly.setClickable(false);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        FullScreenUtils.toggleHideyBar(LiveReplayPlayActivity.this);
        isFullScreen = true;
        getScreen_hv.setBackgroundResource(R.drawable.shrink_screen);
        lockImg.setVisibility(View.VISIBLE);
        time_ly.setVisibility(View.GONE);
        hideOuterAfterFiveSeconds();
    }

    private void SetVerticalScreen() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//清除全屏
        time_ly.setVisibility(View.VISIBLE);
        video_top_ly.setVisibility(View.GONE);
        toolbar.setVisibility(View.VISIBLE);
        video_top_ly.setBackgroundResource(R.color.transparent);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                (int)getResources().getDimension(R.dimen.video_height));
        ssss.setLayoutParams(lp);
        video_ly.setClickable(true);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);

        isFullScreen = false;
        getScreen_hv.setBackgroundResource(R.drawable.enlarge_screen);
        lockImg.setVisibility(View.GONE);
        video_ly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickEmptyArea();
            }
        });
        hideOuterAfterFiveSeconds();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mGestureDetector.onTouchEvent(event)) {
            return true;
        }
        // 处理手势结束
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:

                if (GESTURE_FLAG == GESTURE_MODIFY_PROGRESS) {
                    videoView.resume();
                    gesture_progress_layout.setVisibility(View.GONE);
                    onClickEmptyArea();
                    if (mediaController != null) {
                        mediaController.setIsDragging(false);
                    }
                }

                GESTURE_FLAG = 0;
                currentVolume = -1;
                mBrightness = -1f;
                operation_volume_brightness.setVisibility(View.GONE);

                break;
        }

        return super.onTouchEvent(event);
    }

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        /**
         * 双击
         */
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            return true;
        }

        /**
         * 单击
         */
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            onClickEmptyArea();
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE && buffer_bj.getVisibility() == View.GONE) {

                if (firstScroll) {// 以触摸屏幕后第一次滑动为标准，避免在屏幕上操作切换混乱

                    if (Math.abs(distanceX) >= Math.abs(distanceY)) {

                            gesture_progress_layout.setVisibility(View.VISIBLE);
                            GESTURE_FLAG = GESTURE_MODIFY_PROGRESS;
                            videoView.pause();
                            if (barTimer != null) {
                                barTimer.cancel();
                                barTimer = null;
                            }
                            if (mediaController != null) {
                                mediaController.setIsDragging(true);
                            }
                            currPosition = videoView.getCurrentPosition();
                            duration = videoView.getDuration();


                    } else {
                        gesture_progress_layout.setVisibility(View.GONE);
                        GESTURE_FLAG = GESTURE_MODIFY_VOLUME;
                    }

                }
                if (GESTURE_FLAG == GESTURE_MODIFY_PROGRESS) {
                    if (mediaController != null) {
                        mediaController.show();
                    }
                        if (Math.abs(distanceX) > Math.abs(distanceY)) {// 横向移动大纵向移动
                            if (distanceX >= DensityUtil.dip2px(LiveReplayPlayActivity.this, STEP_PROGRESS)) {// 快退，用步长控制改变速度，可微调
                                gesture_iv_progress.setImageResource(R.drawable.souhu_player_backward);
                                if (currPosition > 0) {// 避免为负
                                    currPosition -= 5;// scroll方法执行一次快退5秒
                                }
                            } else if (distanceX <= -DensityUtil.dip2px(LiveReplayPlayActivity.this, STEP_PROGRESS)) {// 快进
                                gesture_iv_progress.setImageResource(R.drawable.souhu_player_forward);
                                if (currPosition < duration) {// 避免超过总时长
                                    currPosition += 3;// scroll执行一次快进3秒
                                }
                            }
                            if (mediaController != null) {
                                mediaController.gestureUpdatePostion(currPosition,geture_tv_progress_cut_time,geture_tv_progress_time);
                            }
                        }
                } else if (GESTURE_FLAG == GESTURE_MODIFY_VOLUME) {

                    float mOldX = e1.getX();
                    float mOldY = e1.getY();
                    int y = (int) e2.getRawY();
                    Display disp = getWindowManager().getDefaultDisplay();
                    int windowWidth = disp.getWidth();
                    int windowHeight = disp.getHeight();

                    // 右边滑动改变音量大小
                    if (mOldX > windowWidth * 4.0 / 5) {
                        onVolumeSlide((mOldY - y) / windowHeight);

                        // 左边滑动改变亮度大小
                    } else if (mOldX < windowWidth / 5.0) {
                        onBrightnessSlide((mOldY - y) / windowHeight);

                    }

                }

                firstScroll = false;// 第一次scroll执行完成，修改标志
            }

            return false;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            firstScroll = true;
            return false;
        }
    }

    /**
     * 检测'点击'空白区的事件，若播放控制控件未显示，设置为显示，否则隐藏。
     */
    public void onClickEmptyArea() {

        if(!isLock){
            if (barTimer != null) {
                barTimer.cancel();
                barTimer = null;
            }
            if (this.mediaController != null) {
                if (mediaController.getVisibility() == View.VISIBLE) {
                    mediaController.hide();
                    if (isFullScreen) {
                        video_top_ly.setVisibility(View.GONE);
                        lockImg.setVisibility(View.GONE);
                    }
                } else {
                    if(isFullScreen){
                        video_top_ly.setVisibility(View.VISIBLE);
                    }
                    mediaController.show();
                    if (isFullScreen) {
                        lockImg.setVisibility(View.VISIBLE);
                    }

                }
            }
            hideOuterAfterFiveSeconds();
        }
    }

    private void hideOuterAfterFiveSeconds() {
        if (!isFullScreen) {
            return;
        }
        if (barTimer != null) {
            barTimer.cancel();
            barTimer = null;
        }
        barTimer = new Timer();
        barTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                if (!isFullScreen) {
                    return;
                }
                if (mediaController != null) {
                    mediaController.getMainThreadHandler().post(new Runnable() {

                        @Override
                        public void run() {
                            mediaController.hide();
                            video_top_ly.setVisibility(View.GONE);
                            lockImg.setVisibility(View.GONE);

                        }

                    });
                }
            }

        }, 5 * 1000);

    }



    /**
     * 滑动改变声音大小
     *
     * @param percent
     */
    private void onVolumeSlide(float percent) {
        if (currentVolume == -1) {
            currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            if (currentVolume < 0)
                currentVolume = 0;

            // 显示
            operation_bg.setImageResource(R.drawable.video_volumn_bg);
            operation_volume_brightness.setVisibility(View.VISIBLE);
        }

        int index = (int) (percent * maxVolume) + currentVolume;
        if (index > maxVolume)
            index = maxVolume;
        else if (index < 0)
            index = 0;
        // 变更声音
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);

        // 变更进度条
        ViewGroup.LayoutParams lp = operation_percent.getLayoutParams();
        lp.width = findViewById(R.id.operation_full).getLayoutParams().width * index / maxVolume;
        operation_percent.setLayoutParams(lp);
    }


    /**
     * 滑动改变亮度
     *
     * @param percent
     */
    private void onBrightnessSlide(float percent) {
        if (mBrightness < 0) {
            mBrightness = getWindow().getAttributes().screenBrightness;
            if (mBrightness <= 0.00f)
                mBrightness = 0.50f;
            if (mBrightness < 0.01f)
                mBrightness = 0.01f;

            // 显示
            operation_bg.setImageResource(R.drawable.video_brightness_bg);
            operation_volume_brightness.setVisibility(View.VISIBLE);
        }
        WindowManager.LayoutParams lpa = getWindow().getAttributes();
        lpa.screenBrightness = mBrightness + percent;
        if (lpa.screenBrightness > 1.0f)
            lpa.screenBrightness = 1.0f;
        else if (lpa.screenBrightness < 0.01f)
            lpa.screenBrightness = 0.01f;
        getWindow().setAttributes(lpa);

        ViewGroup.LayoutParams lp = operation_percent.getLayoutParams();
        lp.width = (int) (findViewById(R.id.operation_full).getLayoutParams().width * lpa.screenBrightness);
        operation_percent.setLayoutParams(lp);
    }


    @Override
    public void setOnSuccessCallback(int requestCode, Object resultBean) {
        jiazai_layout.setVisibility(View.GONE);
        replayBean = (ReplayBean) resultBean;
        if(replayBean.getItems().size()==0){
                no_info_layout.setVisibility(View.VISIBLE);
                replay_recycler.setVisibility(View.GONE);
        }else {
            no_info_layout.setVisibility(View.GONE);
            replay_recycler.setVisibility(View.VISIBLE);
            listAdapter.addData(replayBean.getItems());
        }

        listAdapter.setSelectPosition(-1);
        listAdapter.notifyDataSetChanged();

    }

    @Override
    public void setOnErrorCallback(int requestCode, int code, String msg) {
        load_fail_layout.setVisibility(View.VISIBLE);
        jiazai_layout.setVisibility(View.GONE);
        NetworkUtil.httpNetErrTip(LiveReplayPlayActivity.this,activity_live_replay_play);
        if(code== Constants.HTTP_CODE_2001||code==Constants.HTTP_CODE_2004){
            NetworkUtil.httpRestartLogin(LiveReplayPlayActivity.this,activity_live_replay_play);
        }

    }

    @Override
    public void onCompletion() {
        synchronized (syncPlaying) {
            isReadyForQuit = true;
            syncPlaying.notifyAll();
        }
        changeStatus(PlayerStatus.PLAYER_COMPLETED);
//        mHandler.removeMessages(UI_EVENT_UPDATE_CURRPOSITION);
        mHandler.sendEmptyMessage(PLAY_COMPLETE);
    }

    @Override
    public void OnCompletionWithParam(int i) {

    }

    @Override
    public boolean onError(int i, int i1) {

        synchronized (syncPlaying) {
            isReadyForQuit = true;
            syncPlaying.notifyAll();
        }
        changeStatus(PlayerStatus.PLAYER_IDLE);

        return false;
    }

    @Override
    public boolean onInfo(int what, int iextra) {
        switch (what) {
            /**
             * 开始缓冲
             */
            case BVideoView.MEDIA_INFO_BUFFERING_START:
                mHandler.sendEmptyMessage(Constants.BUFFERING_START);
                break;
            /**
             * 结束缓冲
             */
            case BVideoView.MEDIA_INFO_BUFFERING_END:
                mHandler.sendEmptyMessage(Constants.BUFFERING_END);
                break;
            default:
                break;
        }

        return false;
    }

    @Override
    public void onPlayingBufferCache(int i) {

    }

    @Override
    public void onPrepared() {
        hideOuterAfterFiveSeconds();
        changeStatus(PlayerStatus.PLAYER_PREPARED);
        mHandler.sendEmptyMessage(GONE_PROGRESSBAR);
//        mHandler.sendEmptyMessage(UI_EVENT_UPDATE_CURRPOSITION);
    }

    private void changeStatus(PlayerStatus status) {
        mPlayerStatus = status;
        if (this.mediaController != null) {
            this.mediaController.changeStatus(status);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        /**
         * 在停止播放前 你可以先记录当前播放的位置,以便以后可以续播
         */
        if (videoView.isPlaying() && (mPlayerStatus != PlayerStatus.PLAYER_IDLE)) {
            // when scree lock,paus is good select than stop
            // don't stop pause
            // mVV.stopPlayback();
            videoView.pause();

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 发起一次播放任务,当然您不一定要在这发起
        if (!videoView.isPlaying() && (mPlayerStatus != PlayerStatus.PLAYER_IDLE)) {
            videoView.resume();
        } else {
//
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 在停止播放前 你可以先记录当前播放的位置,以便以后可以续播
        if (videoView.isPlaying() && (mPlayerStatus != PlayerStatus.PLAYER_IDLE)) {
            // don't stop pause
            // mVV.stopPlayback();
            videoView.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if ((mPlayerStatus != PlayerStatus.PLAYER_IDLE)) {
            videoView.stopPlayback();
        }
        /**
         * 结束后台事件处理线程
         */
        mHandlerThread.quit();
        synchronized (syncPlaying) {
            try {
                if (!isReadyForQuit) {
                    syncPlaying.wait(2 * 1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                if(!isLock){
                    if(isFullScreen){
                        SetVerticalScreen();
                    }else {
                        finish();
                    }
                }
            return true;
        }

        return false;
    }
}

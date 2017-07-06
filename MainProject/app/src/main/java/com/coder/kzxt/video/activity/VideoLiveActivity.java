package com.coder.kzxt.video.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.http.HttpCallBack;
import com.app.http.UrlsNew;
import com.app.http.builders.HttpGetBuilder;
import com.app.http.builders.HttpPostBuilder;
import com.app.http.builders.HttpPutBuilder;
import com.app.utils.L;
import com.baidu.cyberplayer.core.BVideoView;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.base.beans.LiveBean;
import com.coder.kzxt.im.beans.UserInfo;
import com.coder.kzxt.im.view.EmojiViewPage;
import com.coder.kzxt.utils.DateUtil;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.coder.kzxt.video.adapter.LiveImAdapter;
import com.coder.kzxt.video.beans.HeadrtBean;
import com.coder.kzxt.video.beans.InitHeartBean;
import com.com.tencent.qcloud.suixinbo.presenters.LiveHelper;
import com.com.tencent.qcloud.suixinbo.presenters.LiveListViewHelper;
import com.com.tencent.qcloud.suixinbo.presenters.UserServerHelper;
import com.tencent.TIMUserProfile;
import com.tencent.ilivesdk.ILiveConstants;
import com.tencent.ilivesdk.core.ILiveRoomManager;
import com.tencent.ilivesdk.data.ILivePushRes;
import com.tencent.ilivesdk.view.AVRootView;
import com.tencent.ilivesdk.view.AVVideoView;
import com.tencent.livesdk.ILVLiveManager;
import com.tencent.qcloud.suixinbo.model.CurLiveInfo;
import com.tencent.qcloud.suixinbo.model.LiveInfoJson;
import com.tencent.qcloud.suixinbo.model.MemberID;
import com.tencent.qcloud.suixinbo.model.MySelfInfo;
import com.tencent.qcloud.suixinbo.model.RoomInfoJson;
import com.tencent.qcloud.suixinbo.presenters.viewinface.LiveListView;
import com.tencent.qcloud.suixinbo.presenters.viewinface.LiveView;
import com.tencent.qcloud.suixinbo.presenters.viewinface.ProfileView;
import com.tencent.qcloud.suixinbo.utils.Constants;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.coder.kzxt.activity.R.id.pager;
import static com.coder.kzxt.utils.Constants.BAIDU_VIDEOVIEW_AK;
import static com.coder.kzxt.utils.Constants.EVENT_PLAY;
import static com.coder.kzxt.utils.Constants.LAST_GONE;


public class VideoLiveActivity extends BaseActivity implements LiveView, ProfileView, LiveListView, BVideoView.OnPreparedListener,
        BVideoView.OnCompletionListener, BVideoView.OnErrorListener,
        BVideoView.OnInfoListener, BVideoView.OnPlayingBufferCacheListener,
        BVideoView.OnCompletionWithParamListener, AVVideoView.RecvFirstFrameListener,
        HttpCallBack {

    private static final String TAG = VideoLiveActivity.class.getSimpleName();
    private SharedPreferencesUtil spu;
    private final int REQUEST_PHONE_PERMISSIONS = 0;
    private AVRootView mRootView;
    private BVideoView video_view;
    private EventHandler mEventHandler;
    private HandlerThread mHandlerThread;
    private boolean ThreadQuit=false;
    private volatile boolean isReadyForQuit = true;
    private RelativeLayout title_ly;
    private RelativeLayout activity_video_live;
    private ImageView back_iv;
    private TextView title_tv;
    private RelativeLayout live_ly;
    private RelativeLayout transparent_ly;
    private RelativeLayout progress_ly;
    private RelativeLayout live_info_ly;
    private ImageView progress_iv;
    private ProgressBar buffer_progress;
    private ImageButton screen_hv;
    private RelativeLayout top_ly;
    private RelativeLayout btm_ly;
    private ImageButton live_post_ib;
    private ImageButton live_gone_ib;
    private ImageButton live_share_ib;
    private ImageButton img_back;
    private TextView live_sta_tv;
    private TextView live_time_tv;
    private TextView live_num_tv;
    private String shareUrl = "";//分享地址
    private LiveHelper mLiveHelper;
    private LiveListViewHelper mLiveListHelper;

    private int inItIsVideo = 0;//初始化双师状态
    private int liveStatus; //"直播状态 0=>未开始 1=>直播中 2=>已结束 3=>已关闭",
    private int isVideo; //"0=>直播流  1=>录播视频",
    private int position; //录播开始位置]
    private boolean isSeekto = false;
    private String liveUrl;// "视频播放地址",
    private String liveVideoStatus;// "未开始=>''ready''   直播中=>''playing''  切换=>switch  停止=>’stop‘",
    private int rangeTime; //"录播误差时间",
    private String videoState;//"录播视频状态"
    private String studentNum = "";//当前直播在线人数
    private boolean isChangePlay = true;//此标志判断状态改变时在心跳接口里只切换一次
    private boolean isChangeLive = true;//此标志判断状态改变时在心跳接口里只切换一次
    private String changeUrl;
    private boolean isStopvideo = false;//录播是否播放完成过。

    private PlayerStatus mPlayerStatus = PlayerStatus.PLAYER_IDLE;
    private final Object syncPlaying = new Object();
    private List<com.coder.kzxt.im.message.Message> mData;
    private List<com.coder.kzxt.im.message.Message> mDataAll;


    @Override
    public void onFirstFrameRecved(int width, int height, int angle, String identifier) {
        Log.v(TAG, "首帧到达");
        transparent_ly.setBackgroundResource(R.color.transparent);
        buffer_progress.setVisibility(View.GONE);
        progress_ly.setVisibility(View.GONE);

    }

    @Override
    public void setOnSuccessCallback(int requestCode, Object resultBean) {

    }

    @Override
    public void setOnErrorCallback(int requestCode, int code, String msg) {

    }

    /**
     * 播放状态
     */
    public enum PlayerStatus {
        PLAYER_IDLE, PLAYER_PREPARING, PLAYER_PREPARED, PLAYER_COMPLETED
    }

    private volatile boolean isFullScreen = false;//是否全屏
    private boolean bInAvRoom = false;
    private TimerTask timerTask;
    private Timer timer = new Timer();
    private LiveBean liveBean;

    //im部分
    private LinearLayout bar_bottom;
    private EditText mETMsgInput;
    private LinearLayout inputBar;
    private ImageButton btn_emoji;
    private Button btn_send_msg;
    private InputMethodManager inputKeyBoard;
    private ListView lv_msg_items;
    private EmojiViewPage viewPagerEmoji;
    private RelativeLayout im_ly;
    private LiveImAdapter liveImAdapter;
    private LiveImFragment liveImFragment;
    private boolean isShowImMsg = true;
    private int mLastDiff = 0;
    private boolean isShowEmo = false;

    private ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;

    //      "live_status": "直播状态 0=>未开始 1=>直播中 2=>已结束 3=>已关闭 ", start_time ,end_time  Live_title chatroom_group_id
    public static void gotoActivity(Activity activity, LiveBean liveBean) {
        MySelfInfo.getInstance().setIdStatus(Constants.MEMBER);
        MySelfInfo.getInstance().setJoinRoomWay(false);
        CurLiveInfo.setHostID(liveBean.getUser_id());
//        CurLiveInfo.setHostName("");
//        CurLiveInfo.setHostAvator("");
        CurLiveInfo.setRoomNum(Integer.parseInt(liveBean.getRoom_id()));
        CurLiveInfo.setChatRoomId(liveBean.getChatroom_group_id());
//        CurLiveInfo.setMembers(item.getInfo().getMemsize()); // 添加自己
//        CurLiveInfo.setAdmires(item.getInfo().getThumbup());
        activity.startActivityForResult(new Intent(activity, VideoLiveActivity.class).putExtra("liveBean", liveBean), 1);
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
                                L.v(TAG, "waiting for notify invoke or 2s expires");
                                syncPlaying.wait(2 * 1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    String playUrl = (String) msg.getData().get("playUrl");
                    /**
                     * 设置播放url
                     * http://gkkskijidms30qudc3v.exp.bcevod.com/mda-gkkswvrb2zhp41ez/mda-gkkswvrb2zhp41ez.m3u8
                     */
                    L.v(TAG, "播放的url=" + playUrl);
                    video_view.setVideoPath(playUrl);

                    /**
                     * 续播，如果需要如此
                     */
//                    if (mLastPos > 0) {
//                        videoView.seekTo(mLastPos);
//                    }
                    /**
                     * 显示或者隐藏缓冲提示
                     */
                    video_view.showCacheInfo(false);
                    /**
                     * 开始播放
                     */
                    video_view.start();

                    /**
                     * 已经开启播放，必须有结束消息后才能结束
                     */
                    isReadyForQuit = false;

                    mPlayerStatus = PlayerStatus.PLAYER_PREPARING;
                    break;
                default:
                    break;
            }
        }
    }

    Handler mUIHandler = new Handler() {

        public void handleMessage(Message msg) {

            switch (msg.what) {

                case LAST_GONE:
                    top_ly.setVisibility(View.GONE);
                    btm_ly.setVisibility(View.GONE);
                    break;
                //切换录播
                case com.coder.kzxt.utils.Constants.CHANGEPLAY:
                    transparent_ly.setBackgroundResource(R.color.transparent);
                    Log.v(TAG, "切换录播");
                    progress_ly.setVisibility(View.VISIBLE);
                    buffer_progress.setVisibility(View.GONE);
                    progress_iv.setBackgroundResource(R.drawable.live_load_sta);
                    mRootView.setVisibility(View.GONE);
//                    quiteLiveByPurpose();
                    //关闭声音
                    ILiveRoomManager.getInstance().enableSpeaker(false);
                    video_view.setVisibility(View.VISIBLE);
                    startPlayVideo(liveUrl);
                    changeUrl = liveUrl;

                    break;
                //切换直播
                case com.coder.kzxt.utils.Constants.CHANGELIVE:
//                    transparent_ly.setBackgroundResource(R.color.transparent);
//                    progress_ly.setVisibility(View.VISIBLE);
//                    buffer_progress.setVisibility(View.GONE);
//                    progress_iv.setBackgroundResource(R.drawable.live_load_sta);
                    //开启声音
                    ILiveRoomManager.getInstance().enableSpeaker(true);
                    if (mRootView.getVisibility() == View.GONE) {
                        Log.v(TAG, "切换直播");
                        /**
                         * 如果已经播放了，等待上一次播放结束
                         */
                        if (mPlayerStatus == PlayerStatus.PLAYER_PREPARING
                                || mPlayerStatus == PlayerStatus.PLAYER_PREPARED) {
                            synchronized (syncPlaying) {
                                try {
                                    L.v(TAG, "waiting for notify invoke or 2s expires");
                                    syncPlaying.wait(2 * 1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        video_view.setVisibility(View.GONE);
                        mRootView.setVisibility(View.VISIBLE);
//                        mEnterRoomHelper.startEnterRoom(chatRoomId);
                    }

                    break;
                //刷新消息
                case com.coder.kzxt.utils.Constants.LIVE_RES_MSG:
                    ArrayList newlist = msg.getData().getParcelableArrayList("imData");
                    if (newlist.size() > 0) {
                        mData = (List<com.coder.kzxt.im.message.Message>) newlist.get(0);
                        if (mData.size() > 0) {
                            mDataAll.clear();
                            mDataAll.addAll(mData);
                            liveImAdapter.notifyDataSetChanged();
                            lv_msg_items.setSelection(lv_msg_items.getCount() - 1);
                        }
                    }
                    break;
                //接收聊天数据
                case com.coder.kzxt.utils.Constants.lIVE_IM_DATA:
                    ArrayList list = msg.getData().getParcelableArrayList("imData");
                    if (list.size() > 0) {
                        mData = (List<com.coder.kzxt.im.message.Message>) list.get(0);
                        if (mData.size() > 0) {
                            mDataAll.addAll(mData);
                            liveImAdapter.notifyDataSetChanged();
                            lv_msg_items.setSelection(lv_msg_items.getCount() - 1);
                        }
                    }
                    break;
                //播放失败
                case com.coder.kzxt.utils.Constants.LIVE_ERROR:
                    transparent_ly.setBackgroundResource(R.color.black);
                    break;
                //开始缓冲
                case com.coder.kzxt.utils.Constants.BUFFERING_START:
                    progress_ly.setVisibility(View.VISIBLE);
                    progress_iv.setVisibility(View.GONE);
                    buffer_progress.setVisibility(View.VISIBLE);
                    break;
                //结束缓冲
                case com.coder.kzxt.utils.Constants.BUFFERING_END:
                    progress_ly.setVisibility(View.GONE);
                    progress_iv.setVisibility(View.GONE);
                    break;
                //正式播放
                case com.coder.kzxt.utils.Constants.GONE_PROGRESSBAR:
                    transparent_ly.setBackgroundResource(R.color.transparent);
                    progress_ly.setVisibility(View.GONE);
                    progress_iv.setVisibility(View.GONE);
                    break;

                /**
                 * 更新进度及时间
                 */
                case com.coder.kzxt.utils.Constants.UI_EVENT_UPDATE_CURRPOSITION:
                    int currPosition = video_view.getCurrentPosition();
                    mUIHandler.sendEmptyMessageDelayed(com.coder.kzxt.utils.Constants.UI_EVENT_UPDATE_CURRPOSITION, 200);

                    if (position > 0 && rangeTime > 0 && isSeekto) {
                        if (currPosition > position) {
                            if (currPosition - position > rangeTime) {
                                isSeekto = false;
                                Log.v(TAG, "定位录播位置one" + position);
                                Log.v(TAG, "定位当前位置one" + currPosition);
                                video_view.seekTo(position);
                            }
                        }
                        if (position > currPosition) {
                            if (position - currPosition > rangeTime) {
                                isSeekto = false;
                                Log.v(TAG, "定位录播位置two" + position);
                                Log.v(TAG, "定位当前位置two" + currPosition);
                                video_view.seekTo(position);
                            }
                        }

                    }
                    break;
                /**
                 * 播放完成
                 */
                case com.coder.kzxt.utils.Constants.PLAY_COMPLETE:
                    if (isVideo == 1) {
                        isStopvideo = true;
                    }
//                    transparent_ly.setBackgroundResource(R.color.black);
//                    if (video_view.getVisibility() == View.GONE) {
//                        transparent_ly.setBackgroundResource(R.color.transparent);
//                    }
//                    if (isVideo == 1) {
//                        transparent_ly.setBackgroundResource(R.color.transparent);
//                    }
                    break;

                default:
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);   // 不锁屏
        setContentView(R.layout.activity_video_live);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);//防闪屏
        checkPermission();
        mData = new LinkedList<>();
        mDataAll = new LinkedList<>();
        spu = new SharedPreferencesUtil(this);
        //房间内的交互协助类
        mLiveHelper = new LiveHelper(this, this);
        mLiveListHelper = new LiveListViewHelper(this);
        inputKeyBoard = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        liveBean = (LiveBean) getIntent().getSerializableExtra("liveBean");
        initView();
        initOnclick();

        /**
         * 开启后台事件处理线程
         */
        mHandlerThread = new HandlerThread("event handler thread", Process.THREAD_PRIORITY_BACKGROUND);
        mHandlerThread.start();
        mEventHandler = new EventHandler(mHandlerThread.getLooper());

        SetVerticalScreen();
        //进入房间流程
        if (liveBean.getLive_status() == 1) {
            //进入房间流程
            transparent_ly.setBackgroundResource(R.color.black);
            buffer_progress.setVisibility(View.VISIBLE);
            live_sta_tv.setText("直播中");
            live_sta_tv.setTextColor(getResources().getColor(R.color.first_theme));
            mLiveHelper.startEnterRoom();
        } else {
            live_sta_tv.setText("即将开始");
            live_sta_tv.setTextColor(getResources().getColor(R.color.font_red));
            progress_iv.setBackgroundResource(R.drawable.live_beg_sta);
        }
        String startT = liveBean.getStart_time();
        String endt = liveBean.getEnd_time();
        live_time_tv.setText(DateUtil.getHourMin(Long.parseLong(startT)) + "--" + DateUtil.getHourMin(Long.parseLong(endt)));
        //初始化直播心跳
        initPrapreHeartBeat();
    }

    void checkPermission() {
        final List<String> permissionsList = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED))
                permissionsList.add(Manifest.permission.CAMERA);
            if ((checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED))
                permissionsList.add(Manifest.permission.RECORD_AUDIO);
            if ((checkSelfPermission(Manifest.permission.WAKE_LOCK) != PackageManager.PERMISSION_GRANTED))
                permissionsList.add(Manifest.permission.WAKE_LOCK);
            if ((checkSelfPermission(Manifest.permission.MODIFY_AUDIO_SETTINGS) != PackageManager.PERMISSION_GRANTED))
                permissionsList.add(Manifest.permission.MODIFY_AUDIO_SETTINGS);
            if (permissionsList.size() != 0) {
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_PHONE_PERMISSIONS);
            }
        }
    }


    private void initView() {
        liveImFragment = (LiveImFragment) LiveImFragment.newInstance(liveBean);
        activity_video_live = (RelativeLayout) findViewById(R.id.activity_video_live);
        title_ly = (RelativeLayout) findViewById(R.id.title_ly);
        back_iv = (ImageView) findViewById(R.id.back_iv);
        title_tv = (TextView) findViewById(R.id.title_tv);
        title_tv.setText(liveBean.getLive_title());
        mRootView = (AVRootView) findViewById(R.id.av_root_view);
        video_view = (BVideoView) findViewById(R.id.video_view);
        live_ly = (RelativeLayout) findViewById(R.id.live_ly);
        transparent_ly = (RelativeLayout) findViewById(R.id.transparent_ly);
        progress_ly = (RelativeLayout) findViewById(R.id.progress_ly);
        live_info_ly = (RelativeLayout) findViewById(R.id.live_info_ly);
        progress_iv = (ImageView) findViewById(R.id.progress_iv);
        buffer_progress = (ProgressBar) findViewById(R.id.buffer_progress);
        screen_hv = (ImageButton) findViewById(R.id.screen_hv);
        top_ly = (RelativeLayout) findViewById(R.id.top_ly);
        btm_ly = (RelativeLayout) findViewById(R.id.btm_ly);
        im_ly = (RelativeLayout) findViewById(R.id.im_ly);
        live_post_ib = (ImageButton) findViewById(R.id.live_post_ib);
        live_gone_ib = (ImageButton) findViewById(R.id.live_gone_ib);
        live_share_ib = (ImageButton) findViewById(R.id.live_share_ib);
        img_back = (ImageButton) findViewById(R.id.img_back);
        live_sta_tv = (TextView) findViewById(R.id.live_sta_tv);

        live_time_tv = (TextView) findViewById(R.id.live_time_tv);
        live_num_tv = (TextView) findViewById(R.id.live_num_tv);


        bar_bottom = (LinearLayout) findViewById(R.id.bar_bottom);
        lv_msg_items = (ListView) findViewById(R.id.lv_msg_items);

        inputBar = (LinearLayout) findViewById(R.id.inputBar);
        mETMsgInput = (EditText) findViewById(R.id.et_msg_input);
        btn_send_msg = (Button) findViewById(R.id.btn_send_msg);
        btn_emoji = (ImageButton) findViewById(R.id.btn_emoji);
        viewPagerEmoji = (EmojiViewPage) findViewById(R.id.viewPagerEmoji);
        mViewPager = (ViewPager) findViewById(pager);
        viewPagerEmoji.setEditText(mETMsgInput);

        mViewPager.setOffscreenPageLimit(1);
        mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
        //TODO 设置渲染层
        ILVLiveManager.getInstance().setAvVideoView(mRootView);
        mRootView.setGravity(AVRootView.LAYOUT_GRAVITY_RIGHT);
        mRootView.setSubMarginY(getResources().getDimensionPixelSize(R.dimen.small_area_margin_top));
        mRootView.setSubMarginX(getResources().getDimensionPixelSize(R.dimen.small_area_marginright));
        mRootView.setSubPadding(getResources().getDimensionPixelSize(R.dimen.small_area_marginbetween));
        mRootView.setSubWidth(getResources().getDimensionPixelSize(R.dimen.small_area_width));
        mRootView.setSubHeight(getResources().getDimensionPixelSize(R.dimen.small_area_height));

        mRootView.setSubCreatedListener(new AVRootView.onSubViewCreatedListener() {
            @Override
            public void onSubViewCreated() {
                for (int i = 1; i < ILiveConstants.MAX_AV_VIDEO_NUM; i++) {
                    final int index = i;
                    AVVideoView avVideoView = mRootView.getViewByIndex(index);
                    avVideoView.setGestureListener(new GestureDetector.SimpleOnGestureListener() {
                        @Override
                        public boolean onSingleTapConfirmed(MotionEvent e) {
                            mRootView.swapVideoView(0, index);

                            return super.onSingleTapConfirmed(e);
                        }
                    });
                }

                mRootView.getViewByIndex(0).setGestureListener(new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

                        return false;
                    }
                });
            }
        });


        /**
         * 设置ak
         */
        BVideoView.setAK(BAIDU_VIDEOVIEW_AK);
        video_view.setLogLevel(0);

        /**
         * 注册listener
         */
        video_view.setOnPreparedListener(this);
        video_view.setOnCompletionListener(this);
        video_view.setOnCompletionWithParamListener(this);
        video_view.setOnErrorListener(this);
        video_view.setOnInfoListener(this);
        /**
         * 设置解码模式 为保证兼容性，当前仅支持软解
         */
        video_view.setDecodeMode(BVideoView.DECODE_SW);
        video_view.selectResolutionType(BVideoView.RESOLUTION_TYPE_AUTO);

        liveImAdapter = new LiveImAdapter(VideoLiveActivity.this, mDataAll);
        lv_msg_items.setAdapter(liveImAdapter);

    }

    private void initOnclick() {
        screen_hv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTransverseScreen();
            }
        });


        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetVerticalScreen();
            }
        });

        live_post_ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bar_bottom.setVisibility(View.VISIBLE);
                top_ly.setVisibility(View.GONE);
                btm_ly.setVisibility(View.GONE);
                mETMsgInput.requestFocus();
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    public void run() {
                        InputMethodManager im = (InputMethodManager) mETMsgInput.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        im.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
                    }

                }, 500);
            }
        });

        inputBar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                //获取当前界面可视部分
                getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
                //获取屏幕的高度
                int screenHeight = getWindow().getDecorView().getRootView().getHeight();
                //此处就是用来获取键盘的高度的， 在键盘没有弹出的时候 此高度为0 键盘弹出的时候为一个正数
                int heightDifference = screenHeight - r.bottom;
                if (heightDifference <= 0 && mLastDiff > 0) {
                    if (!isShowEmo) {
                        bar_bottom.setVisibility(View.GONE);
                        inputKeyBoard.hideSoftInputFromWindow(mETMsgInput.getWindowToken(), 0);
                    }

                }
                mLastDiff = heightDifference;
            }
        });

        live_gone_ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (im_ly.getVisibility() == View.VISIBLE) {
                    isShowImMsg = false;
                    im_ly.setVisibility(View.GONE);
                    live_gone_ib.setBackgroundResource(R.drawable.live_gone_im);
                } else {
                    isShowImMsg = true;
                    im_ly.setVisibility(View.VISIBLE);
                    live_gone_ib.setBackgroundResource(R.drawable.live_show_im);
                }

            }
        });

        live_share_ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        transparent_ly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isFullScreen) {
                    if (top_ly.getVisibility() == View.VISIBLE) {
                        top_ly.setVisibility(View.GONE);
                        btm_ly.setVisibility(View.GONE);
                    } else {
                        top_ly.setVisibility(View.VISIBLE);
                        btm_ly.setVisibility(View.VISIBLE);
                    }
                }


            }
        });

        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bInAvRoom) {
                    quiteLiveByPurpose();
                } else {
                    clearOldData();
                    finish();
                }
            }
        });

        //表情
        btn_emoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isShowEmo = true;
                if (viewPagerEmoji.getVisibility() == View.GONE) {
                    hideMsgIputKeyboard();
                    viewPagerEmoji.setVisibility(View.VISIBLE);
                } else {
                    showMsgInputKeyboard();
                    viewPagerEmoji.setVisibility(View.GONE);
                }

            }
        });
        //输入框
        viewPagerEmoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isShowEmo = false;
                viewPagerEmoji.setVisibility(View.GONE);
            }
        });

        //发送
        btn_send_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                liveImFragment.sendTextMsg(mETMsgInput);
                bar_bottom.setVisibility(View.GONE);
                inputKeyBoard.hideSoftInputFromWindow(mETMsgInput.getWindowToken(), 0);

            }
        });


    }

    //隐藏键盘
    private void hideMsgIputKeyboard() {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                inputKeyBoard.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    //显示键盘
    private void showMsgInputKeyboard() {
        if (mETMsgInput.requestFocus()) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(mETMsgInput, InputMethodManager.SHOW_IMPLICIT);
        }

    }


    private void setTransverseScreen() {
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);// 强制为横屏
        }

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);

        DisplayMetrics dm = getResources().getDisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int mSurfaceViewWidth = dm.widthPixels;
        int mSurfaceViewHeight = dm.heightPixels;
        if (mRootView.getViewByIndex(0) != null) {
            mRootView.getViewByIndex(0).setPosWidth(mSurfaceViewWidth);
            mRootView.getViewByIndex(0).setPosHeight(mSurfaceViewHeight);
        }

        mRootView.setLayoutParams(lp);
        video_view.setLayoutParams(lp);
        transparent_ly.setLayoutParams(lp);
        progress_ly.setLayoutParams(lp);
        progress_iv.setLayoutParams(lp);
        isFullScreen = true;
        title_ly.setVisibility(View.GONE);
        live_info_ly.setVisibility(View.GONE);
        screen_hv.setVisibility(View.GONE);
        top_ly.setVisibility(View.VISIBLE);
        btm_ly.setVisibility(View.VISIBLE);
        if (isShowImMsg) {
            if (mData.size() != 0) {
                im_ly.setVisibility(View.VISIBLE);
                live_gone_ib.setBackgroundResource(R.drawable.live_show_im);
            } else {
                im_ly.setVisibility(View.GONE);
                live_gone_ib.setBackgroundResource(R.drawable.live_gone_im);
            }
        }

    }

    private void SetVerticalScreen() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//清除全屏
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 强制为竖屏
        }
//        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
//                (int) getResources().getDimension(R.dimen.video_height));

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                (int) getResources().getDimension(R.dimen.woying_180_dip));


        DisplayMetrics dm = getResources().getDisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int mSurfaceViewWidth = dm.widthPixels;
        int mSurfaceViewHeight = (int) getResources().getDimension(R.dimen.woying_180_dip);
        if (mRootView.getViewByIndex(0) != null) {
            mRootView.getViewByIndex(0).setPosWidth(mSurfaceViewWidth);
            mRootView.getViewByIndex(0).setPosHeight(mSurfaceViewHeight);
        }


        mRootView.setLayoutParams(lp);
        video_view.setLayoutParams(lp);
        title_ly.setVisibility(View.VISIBLE);
        transparent_ly.setLayoutParams(lp);
        progress_ly.setLayoutParams(lp);
        progress_iv.setLayoutParams(lp);
        isFullScreen = false;
        live_info_ly.setVisibility(View.VISIBLE);
        screen_hv.setVisibility(View.VISIBLE);
        top_ly.setVisibility(View.GONE);
        btm_ly.setVisibility(View.GONE);
        bar_bottom.setVisibility(View.GONE);
        im_ly.setVisibility(View.GONE);

    }

    // 初始化准备心跳
    private void initPrapreHeartBeat() {
        new HttpPostBuilder(VideoLiveActivity.this).setClassObj(InitHeartBean.class).setHttpResult(new HttpCallBack() {
            @Override
            public void setOnSuccessCallback(int requestCode, Object resultBean) {
                InitHeartBean initHeartBean = (InitHeartBean) resultBean;
                String heartBeatTime = initHeartBean.getItem().getReportTime();
                String deviceId = initHeartBean.getItem().getDeviceId();
                threadHeartBeat(heartBeatTime, deviceId);
            }

            @Override
            public void setOnErrorCallback(int requestCode, int code, String msg) {

            }
        }).setUrl(UrlsNew.POST_LIVE_REPORT)
                .addBodyParam("live_id", liveBean.getId())
                .addBodyParam("type", "study")
                .build();
    }

    // 心跳请求
    private void threadHeartBeat(String heartBeatTime, final String deviceId) {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                httpHeadrt(deviceId);
            }
        };
        timer.schedule(timerTask, 0, Long.valueOf(heartBeatTime));
    }

    private void httpHeadrt(String deviceId) {

        new HttpPutBuilder(VideoLiveActivity.this).setClassObj(HeadrtBean.class).setHttpResult(new HttpCallBack() {
            @Override
            public void setOnSuccessCallback(int requestCode, Object resultBean) {
                HeadrtBean headrtBean = (HeadrtBean) resultBean;
                liveStatus = headrtBean.getItem().getLiveStatus();
                isVideo = headrtBean.getItem().getIsVideo();
                liveUrl = headrtBean.getItem().getLiveUrl();
                liveVideoStatus = headrtBean.getItem().getLiveVideoStatus();
                rangeTime = headrtBean.getItem().getRangeTime();
                videoState = headrtBean.getItem().getVideoState();
                position = headrtBean.getItem().getPosition();
                studentNum = headrtBean.getItem().getStudyNum();
                live_num_tv.setText(studentNum);

                Log.v(TAG, "-------------liveStatus" + liveStatus);
                Log.v(TAG, "-------------isVideo" + isVideo);
                Log.v(TAG, "-------------liveUrl" + liveUrl);
                Log.v(TAG, "-------------liveVideoStatus" + liveVideoStatus);
                Log.v(TAG, "-------------videoState" + videoState);
                Log.v(TAG, "-------------position" + position);
                Log.v(TAG, "-------------rangeTime" + rangeTime);

                if (inItIsVideo != isVideo) {
                    inItIsVideo = isVideo;
                    isChangePlay = true;
                    isChangeLive = true;
                }
                //切换资源
                if (liveVideoStatus.equals("switch")) {
                    //切换时无论什么情况都停掉录播
                    if (video_view.isPlaying()) {
                        video_view.pause();
                    }
                    video_view.stopPlayback();
                    transparent_ly.setBackgroundResource(R.color.transparent);
                    progress_ly.setVisibility(View.VISIBLE);
                    buffer_progress.setVisibility(View.GONE);
                    progress_iv.setBackgroundResource(R.drawable.live_load_sta);
                //停止
                } else if (liveVideoStatus.equals("stop")) {
                    progress_ly.setVisibility(View.VISIBLE);
                    progress_iv.setBackgroundResource(R.drawable.live_close_sta);
                    if (video_view.isPlaying()) {
                        video_view.pause();
                    }
                    video_view.stopPlayback();

                 //未开始
                } else if (liveVideoStatus.equals("ready")) {

                //直播播放中
                }else if(liveVideoStatus.equals("playing")){
                    if(isVideo==0&&video_view.getVisibility() == View.VISIBLE){
                        if (video_view.isPlaying()) {
                            video_view.pause();
                        }
                         video_view.stopPlayback();
                    }
                }

                //切换录播视频
                if (isVideo == 1 && video_view.getVisibility() == View.GONE) {
                    if (isChangePlay) {
                        Timer timer = new Timer();
                        timer.schedule(new TimerTask() {
                            public void run() {
                                mUIHandler.sendEmptyMessage(com.coder.kzxt.utils.Constants.CHANGEPLAY);
                            }

                        }, 500);

                        isChangePlay = false;
                    }

                }

                //切换直播视频
                if (isVideo == 0 && mRootView.getVisibility() == View.GONE) {
                    if (isChangeLive) {
                        Timer timer = new Timer();
                        timer.schedule(new TimerTask() {
                            public void run() {
                                mUIHandler.sendEmptyMessage(com.coder.kzxt.utils.Constants.CHANGELIVE);
                            }

                        }, 500);

                        isChangeLive = false;
                    }

                }

                //录播状态下pc端暂停 停止 重新开始操作
                if (isVideo == 1) {
                    if (videoState.equals("PAUSED")) {
                        if (video_view.isPlaying()) {
                            video_view.pause();
                        }
                        Log.v(TAG, "暂停录播");
                    } else if (videoState.equals("IDLE")) {

                        if (video_view.isPlaying()) {
                            video_view.pause();
                        }
//                        video_view.setVisibility(View.GONE);
//                        transparent_ly.setBackgroundResource(R.color.transparent);
//                        progress_ly.setVisibility(View.VISIBLE);
//                        buffer_progress.setVisibility(View.GONE);
//                        progress_iv.setBackgroundResource(R.drawable.live_load_sta);
                        Log.v(TAG, "录播停止");

                    } else if (videoState.equals("PLAYING")) {
                        if (TextUtils.isEmpty(changeUrl))
                            return;
                        if (isStopvideo || !changeUrl.equals(liveUrl)) {
                            isStopvideo = false;
                            Timer timer = new Timer();
                            timer.schedule(new TimerTask() {
                                public void run() {
                                    mUIHandler.sendEmptyMessage(com.coder.kzxt.utils.Constants.CHANGEPLAY);
                                }

                            }, 500);
                        } else {
                            video_view.resume();
                        }
                    }

                }


            }

            @Override
            public void setOnErrorCallback(int requestCode, int code, String msg) {
                if (code == com.coder.kzxt.utils.Constants.HTTP_CODE_2001
                        || code == com.coder.kzxt.utils.Constants.HTTP_CODE_2004
                        || code == com.coder.kzxt.utils.Constants.HTTP_CODE_8000
                        || code == com.coder.kzxt.utils.Constants.HTTP_CODE_8303) {
                    setResult(2001);
                    clearOldData();
                    finish();
                }
            }
        }).setUrl(UrlsNew.POST_LIVE_REPORT)
                .addBodyParam("deviceId", deviceId)
                .addBodyParam("type", "study")
                .addBodyParam("live_id", liveBean.getId())
                .addQueryParams("center", "1")
                .setPath(liveBean.getId())
                .build();


        new HttpGetBuilder(VideoLiveActivity.this)
                .setUrl(UrlsNew.CHAT_ROOM_LIVE_MEMBER)
                .setHttpResult(VideoLiveActivity.this)
                .addQueryParams("chatroom_group_id", liveBean.getChatroom_group_id())
                .addQueryParams("identifier", UserInfo.getInstance().getId())
                .build();

    }


    public Handler getmHandler() {
        return mUIHandler;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (bInAvRoom) {
                    quiteLiveByPurpose();
                } else {
                    clearOldData();
                    finish();
                }
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void enterRoomComplete(int id_status, boolean succ) {
//        Toast.makeText(VideoLiveActivity.this, "进入房间成功", Toast.LENGTH_SHORT).show();
        //必须得进入房间之后才能初始化UI
        mRootView.getViewByIndex(0).setRotate(true);
        mRootView.getViewByIndex(0).setRecvFirstFrameListener(this);
        //设定在方向不一致情况下，是铺满屏幕还是留黑边
        mRootView.getViewByIndex(0).setDiffDirectionRenderMode(AVVideoView.ILiveRenderMode.BLACK_TO_FILL);
        //设定在方向一致情况下，是铺满屏幕还是留黑边
        mRootView.getViewByIndex(0).setSameDirectionRenderMode(AVVideoView.ILiveRenderMode.BLACK_TO_FILL);
        bInAvRoom = true;
        if (succ == true) {
            if (id_status == Constants.HOST) {//主播方式加入房间成功
                //开启摄像头渲染画面

            } else {
                //发消息通知上线
                mLiveHelper.sendGroupCmd(Constants.AVIMCMD_ENTERLIVE, "");
            }

        }
    }


    /**
     * 开始播放录播
     */
    private void startPlayVideo(String playUrl) {
        Message palymsg = new Message();
        Bundle bundle = new Bundle();
        palymsg.what = com.coder.kzxt.utils.Constants.EVENT_PLAY;
        bundle.putString("playUrl", playUrl);
        palymsg.setData(bundle);
        if(!ThreadQuit){
            mEventHandler.sendMessage(palymsg);
        }

    }

    @Override
    public void quiteRoomComplete(int id_status, boolean succ, LiveInfoJson liveinfo, int errCode) {

        if (MySelfInfo.getInstance().getIdStatus() == Constants.HOST) {

        } else {
            Log.v(TAG, "退出直播完成" + id_status);
            setResult(errCode);
            clearOldData();
            finish();
        }

    }

    @Override
    protected void onResume() {
        ILiveRoomManager.getInstance().onResume();
        if (mRootView.getVisibility() == View.GONE && !video_view.isPlaying() && (mPlayerStatus != PlayerStatus.PLAYER_IDLE)) {
            video_view.resume();
        } else {
//
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        ILiveRoomManager.getInstance().onPause();

        /**
         * 在停止播放前 你可以先记录当前播放的位置,以便以后可以续播
         */
        if (video_view.getVisibility() == View.VISIBLE
                && video_view.isPlaying()
                && (mPlayerStatus != PlayerStatus.PLAYER_IDLE)) {
            // when scree lock,paus is good select than stop
            // don't stop pause
            // mVV.stopPlayback();
            video_view.pause();

        }
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v(TAG, "onStop");
        // 在停止播放前 你可以先记录当前播放的位置,以便以后可以续播
        if (video_view.isPlaying() && (mPlayerStatus != PlayerStatus.PLAYER_IDLE)) {
            // don't stop pause
            // mVV.stopPlayback();
            video_view.pause();
        }
    }

    // 清除老房间数据
    private void clearOldData() {
        mRootView.clearUserView();
    }

    @Override
    public void showInviteDialog() {

    }

    @Override
    public void refreshText(String text, String name) {

    }

    @Override
    public void refreshThumbUp() {

    }

    @Override
    public void refreshUI(String id) {

    }

    @Override
    public boolean showInviteView(String id) {
        L.v("tangcy", "aaaaaaaaaaaaaaa--showInviteView");
        return false;
    }

    @Override
    public void cancelInviteView(String id) {
        L.v("tangcy", "aaaaaaaaaaaaaaa--cancelInviteView");
    }

    @Override
    public void cancelMemberView(String id) {
        L.v("tangcy", "aaaaaaaaaaaaaaa--cancelMemberView");
    }

    @Override
    public void memberJoin(String id, String name) {
        L.v("tangcy", "aaaaaaaaaaaaaaa--memberJoin");
    }

    @Override
    public void hideInviteDialog() {

    }

    @Override
    public void pushStreamSucc(ILivePushRes streamRes) {
        L.v("tangcy", "aaaaaaaaaaaaaaa--pushStreamSucc");
    }

    @Override
    public void stopStreamSucc() {
        L.v("tangcy", "aaaaaaaaaaaaaaa--stopStreamSucc");
    }

    @Override
    public void startRecordCallback(boolean isSucc) {
        L.v("tangcy", "aaaaaaaaaaaaaaa--startRecordCallback");
    }

    @Override
    public void stopRecordCallback(boolean isSucc, List<String> files) {
        L.v("tangcy", "aaaaaaaaaaaaaaa--stopRecordCallback");
    }

    @Override
    public void hostLeave(String id, String name) {

    }

    @Override
    public void hostBack(String id, String name) {

    }

    @Override
    public void refreshMember(ArrayList<MemberID> memlist) {
        L.v("tangcy", "aaaaaaaaaaaaaaa--refreshMember");
    }

    @Override
    public void updateProfileInfo(TIMUserProfile profile) {
        L.v("tangcy", "aaaaaaaaaaaaaaa--updateProfileInfo");
    }

    @Override
    public void updateUserInfo(int requestCode, List<TIMUserProfile> profiles) {
        L.v("tangcy", "aaaaaaaaaaaaaaa--updateUserInfo");
    }

    @Override
    public void showRoomList(UserServerHelper.RequestBackInfo result, ArrayList<RoomInfoJson> roomlist) {

    }

    @Override
    public void onPrepared() {
        Log.v(TAG, "正式播放");
        isSeekto = true;
        mPlayerStatus = PlayerStatus.PLAYER_PREPARED;
        mUIHandler.sendEmptyMessage(com.coder.kzxt.utils.Constants.GONE_PROGRESSBAR);
        mUIHandler.sendEmptyMessage(com.coder.kzxt.utils.Constants.UI_EVENT_UPDATE_CURRPOSITION);
        mUIHandler.sendEmptyMessageDelayed(com.coder.kzxt.utils.Constants.TIMING_HIDE, 5000);
    }

    @Override
    public void onPlayingBufferCache(int i) {

    }

    @Override
    public boolean onInfo(int what, int iextra) {

        switch (what) {
            /**
             * 开始缓冲
             */
            case BVideoView.MEDIA_INFO_BUFFERING_START:
                Log.v(TAG, "开始缓冲");
                mUIHandler.sendEmptyMessage(com.coder.kzxt.utils.Constants.BUFFERING_START);

                break;
            /**
             * 结束缓冲
             */
            case BVideoView.MEDIA_INFO_BUFFERING_END:
                Log.v(TAG, "结束缓冲");
                isSeekto = true;
                mUIHandler.sendEmptyMessage(com.coder.kzxt.utils.Constants.BUFFERING_END);
                break;
            default:
                break;
        }

        return false;
    }

    @Override
    public boolean onError(int i, int i1) {

        synchronized (syncPlaying) {
            isReadyForQuit = true;
            syncPlaying.notify();
        }
        mPlayerStatus = PlayerStatus.PLAYER_IDLE;
        Message playErrorMsg = new Message();
        playErrorMsg.what = com.coder.kzxt.utils.Constants.LIVE_ERROR;
        mUIHandler.sendMessage(playErrorMsg);
        mUIHandler.removeMessages(com.coder.kzxt.utils.Constants.BUFFERING_START);
        mUIHandler.removeMessages(com.coder.kzxt.utils.Constants.BUFFERING_END);
        mUIHandler.removeMessages(com.coder.kzxt.utils.Constants.GONE_PROGRESSBAR);
        mUIHandler.removeMessages(com.coder.kzxt.utils.Constants.UI_EVENT_UPDATE_CURRPOSITION);
        mUIHandler.removeMessages(com.coder.kzxt.utils.Constants.EVENT_PLAY);
        Log.v("tangcy", "播放失败");

        return false;
    }

    @Override
    public void OnCompletionWithParam(int i) {

    }

    @Override
    public void onCompletion() {
        synchronized (syncPlaying) {
            isReadyForQuit = true;
            syncPlaying.notify();
        }
        mPlayerStatus = PlayerStatus.PLAYER_COMPLETED;
        mUIHandler.sendEmptyMessage(com.coder.kzxt.utils.Constants.PLAY_COMPLETE);
        mUIHandler.removeMessages(com.coder.kzxt.utils.Constants.UI_EVENT_UPDATE_CURRPOSITION);

        Log.v("tangcy", "播放完成");
    }


    /**
     * 主动退出直播
     */
    private void quiteLiveByPurpose() {
        if (MySelfInfo.getInstance().getIdStatus() == Constants.HOST) {
        } else {
            mLiveHelper.startExitRoom();

        }
    }


    public class PagerAdapter extends FragmentPagerAdapter {

        private final String[] TITLES = {"Page 1"};

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public Fragment getItem(int position) {
            return liveImFragment;
        }

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (isFullScreen) {
                SetVerticalScreen();
            } else {
                if (bInAvRoom) {
                    quiteLiveByPurpose();
                } else {
                    clearOldData();
                    finish();
                }

            }
            return true;
        }

        return false;
    }

    @Override
    protected void onDestroy() {

        if ((mPlayerStatus != PlayerStatus.PLAYER_IDLE)) {
            video_view.stopPlayback();
        }

        /**
         * 结束后台事件处理线程
         */
        mHandlerThread.quit();
        ThreadQuit = true;
        synchronized (syncPlaying) {
            try {
                if (!isReadyForQuit) {
                    Log.v(TAG, "waiting for notify invoke or 2s expires");
                    syncPlaying.wait(2 * 1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (timer != null) {
            timer.cancel();
        }
        if (timerTask != null) {
            timerTask.cancel();
        }

        CurLiveInfo.setMembers(0);
        CurLiveInfo.setAdmires(0);
        CurLiveInfo.setCurrentRequestCount(0);
        mLiveHelper.onDestory();
        super.onDestroy();
    }
}

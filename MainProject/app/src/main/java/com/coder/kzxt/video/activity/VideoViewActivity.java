package com.coder.kzxt.video.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.graphics.drawable.GradientDrawable;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.SparseArrayCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.http.HttpCallBack;
import com.app.http.UrlsNew;
import com.app.http.builders.HttpGetBuilder;
import com.app.http.builders.HttpPostBuilder;
import com.app.utils.L;
import com.baidu.cyberplayer.core.BVideoView;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.base.beans.BaseBean;
import com.coder.kzxt.base.beans.LiveBean;
import com.coder.kzxt.buildwork.activity.TeaWorkRecourseActivity;
import com.coder.kzxt.course.activity.LiveCourseActivity;
import com.coder.kzxt.course.beans.CourseBelowLiveBean;
import com.coder.kzxt.dialog.util.CustomListDialog;
import com.coder.kzxt.download.VideoView_Download_Activity;
import com.coder.kzxt.message.activity.ChatDetailActivity;
import com.coder.kzxt.question.activity.CourseQuestionsActivity;
import com.coder.kzxt.recyclerview.MyRecyclerView;
import com.coder.kzxt.recyclerview.adapter.BaseRecyclerAdapter;
import com.coder.kzxt.sign.activity.SignMainStudentActivity;
import com.coder.kzxt.sign.activity.SignMainTeacherActivity;
import com.coder.kzxt.sign.beans.SignResult;
import com.coder.kzxt.stuwork.activity.StuCourseWorkActivity;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.DensityUtil;
import com.coder.kzxt.utils.FullScreenUtils;
import com.coder.kzxt.utils.GlideUtils;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.PermissionsUtil;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.coder.kzxt.utils.ToastUtils;
import com.coder.kzxt.video.adapter.FunctionDelegate;
import com.coder.kzxt.video.adapter.SelectsAdapter;
import com.coder.kzxt.video.beans.CatalogueBean;
import com.coder.kzxt.video.beans.ChatRoomResult;
import com.coder.kzxt.video.beans.CourseRoleResult;
import com.coder.kzxt.webview.activity.TextView_Link_Activity;
import com.nineoldandroids.view.ViewHelper;
import com.tencent.TIMConversationType;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.coder.kzxt.activity.R.drawable.enlarge_screen;
import static com.coder.kzxt.activity.R.drawable.shrink_screen;
import static com.coder.kzxt.activity.R.id.media_controller_bar;
import static com.coder.kzxt.activity.R.id.pager;
import static com.coder.kzxt.utils.Constants.BUFFERING_END;
import static com.coder.kzxt.utils.Constants.BUFFERING_START;
import static com.coder.kzxt.utils.Constants.EVENT_PLAY;
import static com.coder.kzxt.utils.Constants.GONE_PROGRESSBAR;
import static com.coder.kzxt.utils.Constants.INIT_DATA;
import static com.coder.kzxt.utils.Constants.INIT_SELECT;
import static com.coder.kzxt.utils.Constants.JOIN_TIP;
import static com.coder.kzxt.utils.Constants.LIST_DATA;
import static com.coder.kzxt.utils.Constants.PLAY_COMPLETE;
import static com.coder.kzxt.utils.Constants.PREPARE_PLAY;
import static com.coder.kzxt.utils.Constants.UI_EVENT_UPDATE_CURRPOSITION;

public class VideoViewActivity extends BaseActivity implements BVideoView.OnPreparedListener,
        BVideoView.OnCompletionListener, BVideoView.OnErrorListener,
        BVideoView.OnInfoListener, BVideoView.OnPlayingBufferCacheListener,
        BVideoView.OnCompletionWithParamListener, ScrollTabHolder,
        ViewPager.OnPageChangeListener, HttpCallBack
{


    private static final String TAG = "videoView";
    private FrameLayout activity_video_view;
    private ViewPager mViewPager;
    private SharedPreferencesUtil spu;
    private View parent_view;
    private RelativeLayout function_view;
    private RelativeLayout video_top_ly;
    private RelativeLayout video_ly;
    private RelativeLayout selects_layout;
    private RelativeLayout join_tip_ly;
    private ListView selects_list;
    private SelectsAdapter adapter;
    private BVideoView videoView = null;
    private ProgressBar loading_video;
    private ImageView lockImg;
    private ImageView selectImg;
    private SimpleMediaController mediaController = null;
    private RelativeLayout mViewHolder = null;
    private PagerAdapter mPagerAdapter;
    private int mMinHeaderHeight;
    private int mHeaderHeight;
    private int mMinHeaderTranslation; //悬停距离值
    private boolean videoIsShow = true;//播放器view是否添加动画滑动到屏幕外
    private OnMainListener mainListener;
    private String ak = Constants.BAIDU_VIDEOVIEW_AK; // 请录入您的AK
    private EventHandler mEventHandler;
    private HandlerThread mHandlerThread;
    private volatile boolean isReadyForQuit = true;
    private Timer barTimer;

    private String flag;//横竖屏标志
    private String treeId;//课程id
    private String tree_name;//课程名字
    private String treepicture;//课程图片
    private String isCenter;//是否是公共课
    private String courseClassId;// 授课班id
    private String classId;  //班级id
    private String isClose;  //班级是否关闭
    private String scanQrcode; //扫码标示
    //选集数据
    private List<CatalogueBean.ItemsBean.VideoBean> selects = new ArrayList<>();
    //下载数据
    private List<CatalogueBean.ItemsBean> downloadList = new ArrayList<>();
    private boolean isCheckSelect = false; //是否点击过选集
    private int selectPostion = 0;//点击选集的postion;
    private ImageButton getScreen_hv;

    private ImageButton img_back;
    private TextView title;
    private ImageButton download_iv;
    private ImageView buffer_bj;
    private ImageView paly_img;

    private MyRecyclerView myRecyclerView;
    private FunctionDelegate functionDelegate;
    private BaseRecyclerAdapter functAdapter;

    private GestureDetector mGestureDetector;// 手势
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
    private int exitPostion;
    private String className;

    private int maxVolume = 0; // 最大音量
    private int currentVolume = -1; // 当前音量
    private AudioManager mAudioManager;
    private ImageView operation_bg; // 音量图片
    private View operation_volume_brightness; // 手势用的音量和亮度布局
    private ImageView operation_percent; // 音量进度条


    private float mBrightness = -1f; // 当前亮度

    private volatile boolean isFullScreen = false;//是否全屏
    private boolean isLock = false;//是否锁住屏幕
    private boolean firstScroll = false;// 每次触摸屏幕后，第一次scroll的标志

    private int scrollY;
    private int initPostion;

    private ArrayList<HashMap<String, String>> ids;

    /**
     * 播放状态
     */
    public enum PlayerStatus
    {
        PLAYER_IDLE, PLAYER_PREPARING, PLAYER_PREPARED, PLAYER_COMPLETED
    }

    private PlayerStatus mPlayerStatus = PlayerStatus.PLAYER_IDLE;
    private final Object syncPlaying = new Object();

    private AbsListView listView;
    private boolean isSelectPosition = false; //此变量判断只有在点击时才触发 视频列表list定位
    /**
     * 当前正在播放的对象
     */
    private CatalogueBean.ItemsBean.VideoBean videoBean;
    private PermissionsUtil permissionsUtil;
    private boolean isPlayCom = false;//判断是否是自动播放完成
    private int PlayPostion;
    private int isJoin;//0未加入 1学生 2老师 3创建课程的用户
    private List<CourseRoleResult.Item.ListBean> classList = new ArrayList<>();

    private OrientationEventListener mOrientationListener;

    class EventHandler extends Handler
    {
        public EventHandler(Looper looper)
        {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {

                case EVENT_PLAY:
                    /**
                     * 如果已经播放了，等待上一次播放结束
                     */
                    if (mPlayerStatus == PlayerStatus.PLAYER_PREPARING
                            || mPlayerStatus == PlayerStatus.PLAYER_PREPARED)
                    {
                        synchronized (syncPlaying)
                        {
                            try
                            {
                                L.v(TAG, "waiting for notify invoke or 2s expires");
                                syncPlaying.wait(2 * 1000);
                            } catch (InterruptedException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }

                    /**
                     * 设置播放url
                     * http://gkkskijidms30qudc3v.exp.bcevod.com/mda-gkkswvrb2zhp41ez/mda-gkkswvrb2zhp41ez.m3u8
                     */
                    L.v("tangcy", "播放的url=" + videoBean.getMediaUri());
                    videoView.setVideoPath(videoBean.getMediaUri());
                    int mLastPos = spu.getLastPosition(treeId, videoBean.getCourse_chapter_id(), videoBean.getId());

                    /**
                     * 续播，如果需要如此
                     */
                    if (mLastPos > 0)
                    {
                        videoView.seekTo(mLastPos);
                    }
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


    Handler mHandler = new Handler()
    {

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public void handleMessage(Message msg)
        {

            switch (msg.what)
            {

                case UI_EVENT_UPDATE_CURRPOSITION:
                    duration = videoView.getDuration();
                    exitPostion = videoView.getCurrentPosition();
                    mHandler.sendEmptyMessageDelayed(UI_EVENT_UPDATE_CURRPOSITION, 200);
                    break;

                case BUFFERING_START:
                    paly_img.setVisibility(View.GONE);
                    loading_video.setVisibility(View.VISIBLE);
                    break;

                case BUFFERING_END:
                    loading_video.setVisibility(View.GONE);
                    paly_img.setVisibility(View.GONE);
                    break;

                case GONE_PROGRESSBAR:
                    loading_video.setVisibility(View.GONE);
                    paly_img.setVisibility(View.GONE);
                    buffer_bj.setVisibility(View.GONE);
                    video_top_ly.setBackgroundResource(R.color.transparent);
                    break;

                case INIT_SELECT:
                    videoBean = (CatalogueBean.ItemsBean.VideoBean) msg.getData().getSerializable("videoBean");
                    isSeekTag();

                    break;
                case INIT_DATA:
                    isJoin = msg.getData().getInt("isJoin");

                    classList.clear();
                    classList.addAll((List<CourseRoleResult.Item.ListBean>) msg.getData().getSerializable("list"));

                    if (isJoin == 1)
                    {
                        workNoticeHttpRequest();
                        getCourseSignNum(classList.get(0).getId());
                    }
                    break;

                case LIST_DATA:
                    ArrayList list = msg.getData().getParcelableArrayList("videosData");
                    selects = (List<CatalogueBean.ItemsBean.VideoBean>) list.get(0);
                    downloadList = (List<CatalogueBean.ItemsBean>) list.get(1);
                    break;

                case PREPARE_PLAY:
                    //记录之前播放对象的播放位置
                    recordPosition();
                    switchItemOnclickPlay(msg);
                    break;

                case JOIN_TIP:
                    join_tip_ly.setVisibility(View.GONE);
                    break;

                case PLAY_COMPLETE:
                    if (!isPlayCom)
                    {
                        if (flag.equals(Constants.OFFLINE))
                        {
                            finish();
                        } else
                        {

                            buffer_bj.setVisibility(View.VISIBLE);
                            //老师和创建者不解锁
                            if (isJoin != 2 && isJoin != 3)
                            {
                                resLockStu();
                            }
                        }
                        postComData();
                    }

                    break;
                default:
                    break;
            }
        }
    };

    Runnable runnable = new Runnable()
    {
        @Override
        public void run()
        {
            // TODO Auto-generated method stub
            join_tip_ly.setVisibility(View.GONE);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);//防闪屏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // 保持屏幕高亮显示,不锁屏

        mOrientationListener = new OrientationEventListener(this, SensorManager.SENSOR_DELAY_NORMAL) {
            @Override
            public void onOrientationChanged(int orientation) {
                if (orientation == OrientationEventListener.ORIENTATION_UNKNOWN) {
                    return; // 手机平放时，检测不到有效的角度
                }
                // 只检测是否有四个角度的改变
                if (orientation > 350 || orientation < 10) {
                    // 0度：手机默认竖屏状态（home键在正下方）
                } else if (orientation > 80 && orientation < 100) {
                    // 90度：手机顺时针旋转90度横屏（home建在左侧）
                    //希望Activity在横向屏幕上显示，但与正常的横向屏幕方向相反
                    //判断是否处于横屏
                    if(isFullScreen){
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
                    }
                } else if (orientation > 170 && orientation < 190) {
                    // 手机顺时针旋转180度竖屏（home键在上方）
                } else if (orientation > 260 && orientation < 280) {
                    // 手机顺时针旋转270度横屏，（home键在右侧）
                    if(isFullScreen){
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    }
                }
            }

        };

        if (mOrientationListener.canDetectOrientation()) {
            Log.v(TAG, "Can detect orientation");
            mOrientationListener.enable();
        } else {
            Log.v(TAG, "Cannot detect orientation");
            mOrientationListener.disable();
        }

        spu = new SharedPreferencesUtil(this);
        permissionsUtil = new PermissionsUtil(this);
        ids = new ArrayList<>();
        flag = getIntent().getStringExtra("flag");
        treeId = getIntent().getStringExtra("treeid");
        tree_name = getIntent().getStringExtra("tree_name");
        treepicture = getIntent().getStringExtra("pic");
        courseClassId = getIntent().getStringExtra("courseClassId");
        isCenter = getIntent().getStringExtra(Constants.IS_CENTER);
        classId = getIntent().getStringExtra("classId");
        isClose = getIntent().getStringExtra("isClose");
        scanQrcode = getIntent().getStringExtra("scanQrcode");

        // 获得当前声音大小
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        initView();
        initVideoView();
        initOnclick();
        /**
         * 开启后台事件处理线程
         */
        mHandlerThread = new HandlerThread("event handler thread", Process.THREAD_PRIORITY_BACKGROUND);
        mHandlerThread.start();
        mEventHandler = new EventHandler(mHandlerThread.getLooper());

        //离线视频播放
        if (flag.equals(Constants.OFFLINE))
        {
            videoBean = (CatalogueBean.ItemsBean.VideoBean) getIntent().getSerializableExtra("videoBean");
            isSeekTag();
            selects = (List<CatalogueBean.ItemsBean.VideoBean>) getIntent().getSerializableExtra("selects");
            setTransverseScreen();
            paly_img.setVisibility(View.GONE);
            loading_video.setVisibility(View.VISIBLE);
            mEventHandler.sendEmptyMessage(EVENT_PLAY);
            download_iv.setVisibility(View.GONE);
            getScreen_hv.setVisibility(View.GONE);
        } else
        {
            SetVerticalScreen();
        }

        getCourseLiveNum();
    }

    private int workNum, examNum, liveNum;

    private void workNoticeHttpRequest()
    {
        new HttpGetBuilder(this).setClassObj(null)
                .setUrl(UrlsNew.URLHeader + "/test/statistics")
                .setHttpResult(this)
                .setRequestCode(1)
                .addQueryParams("course_id", treeId)
                .build();
    }

    private void getCourseLiveNum()
    {
        //获取课程下的直播
        new HttpGetBuilder(this)
                .setHttpResult(this)
                .setClassObj(CourseBelowLiveBean.class)
                .setRequestCode(2)
                .addQueryParams("page", "1")
                .addQueryParams("pageSize", "1")
                .addQueryParams("course_id", treeId)
                .addQueryParams("relation", "live")
                .addQueryParams("center", "1")
                .addQueryParams("non_uid", "1")
                .setUrl(UrlsNew.GET_COURSE_lIVE)
                .build();
    }

    //获取课程下的签到
    private void getCourseSignNum(String classId)
    {
        new HttpGetBuilder(VideoViewActivity.this)
                .setHttpResult(VideoViewActivity.this)
                .setUrl(UrlsNew.SIGN)
                .setClassObj(SignResult.class)
                .setRequestCode(1003)
                .addQueryParams("course_id", treeId)
                .addQueryParams("class_id", classId)
                .build();
    }

    @Override
    public void setOnSuccessCallback(int requestCode, Object resultBean)
    {
        if (requestCode == 1)
        {
            try
            {
                JSONObject jsonObject = new JSONObject(resultBean.toString());
                examNum = jsonObject.getJSONObject("item").getInt("new_test");
                workNum = jsonObject.getJSONObject("item").getInt("new_homework");
                functionDelegate.examNum = examNum;
                functionDelegate.workNum = workNum;
                functAdapter.notifyDataSetChanged();
            } catch (JSONException e)
            {
                e.printStackTrace();
            }

        } else if (requestCode == 1002)
        {
            ChatRoomResult chatRoomResult = (ChatRoomResult) resultBean;
            if (chatRoomResult.getItem().getStatus().equals("1"))
            {
                ChatDetailActivity.gotoActivity(VideoViewActivity.this,
                        chatRoomResult.getItem().getGroup_id(),
                        treeId,
                        chatRoomResult.getItem().getCourse_class_id(),
                        className,
                        TIMConversationType.Group,
                        false
                );
            } else
            {
                ToastUtils.makeText(VideoViewActivity.this, getResources().getString(R.string.chat_room_close));
            }
        } else if (requestCode == 2)
        {
            CourseBelowLiveBean courseBelowLiveBean = (CourseBelowLiveBean) resultBean;
            ArrayList<LiveBean> list = new ArrayList<>();
            for (int i = 0; i < courseBelowLiveBean.getItems().size(); i++)
            {
                LiveBean item = courseBelowLiveBean.getItems().get(i);
                if (item.getLive_status() == 1)
                {
                    list.add(item);
                }
            }
            functionDelegate.liveNum = list.size();
            functAdapter.notifyDataSetChanged();
        } else if (requestCode == 1003)
        {
            SignResult signResult = (SignResult) resultBean;
            if (signResult.getItem().getId() != null)
                functionDelegate.signNum = 1;
            functAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void setOnErrorCallback(int requestCode, int code, String msg)
    {

    }

    private void initView()
    {

        activity_video_view = (FrameLayout) findViewById(R.id.activity_video_view);
        mViewPager = (ViewPager) findViewById(pager);
        parent_view = findViewById(R.id.parent_view);
        function_view = (RelativeLayout) findViewById(R.id.function_view);
        video_top_ly = (RelativeLayout) findViewById(R.id.video_top_ly);
        video_ly = (RelativeLayout) findViewById(R.id.video_ly);
        selects_layout = (RelativeLayout) findViewById(R.id.selects_layout);
        selects_list = (ListView) findViewById(R.id.selects_list);
        loading_video = (ProgressBar) findViewById(R.id.loading_video);
        lockImg = (ImageView) findViewById(R.id.lockImg);
        selectImg = (ImageView) findViewById(R.id.selectImg);
        img_back = (ImageButton) findViewById(R.id.img_back);
        title = (TextView) findViewById(R.id.title);
        download_iv = (ImageButton) findViewById(R.id.download_iv);
        buffer_bj = (ImageView) findViewById(R.id.buffer_bj);
        paly_img = (ImageView) findViewById(R.id.paly_img);
        myRecyclerView = (MyRecyclerView) findViewById(R.id.myRecyclerView);
        List<String> strings = new ArrayList<>();
        strings.add(getResources().getString(R.string.ims));
        strings.add(getResources().getString(R.string.work));
        strings.add(getResources().getString(R.string.exam));
        strings.add(getResources().getString(R.string.live));
        strings.add(getResources().getString(R.string.signin));
        strings.add(getResources().getString(R.string.questions));
        functionDelegate = new FunctionDelegate(this, strings);
        functAdapter = new BaseRecyclerAdapter(this, strings, functionDelegate);
        myRecyclerView.setAdapter(functAdapter);

        gesture_progress_layout = (RelativeLayout) findViewById(R.id.gesture_progress_layout);
        gesture_iv_progress = (ImageView) findViewById(R.id.gesture_iv_progress);
        geture_tv_progress_cut_time = (TextView) findViewById(R.id.geture_tv_progress_cut_time);
        geture_tv_progress_time = (TextView) findViewById(R.id.geture_tv_progress_time);

        operation_bg = (ImageView) findViewById(R.id.operation_bg);
        operation_volume_brightness = findViewById(R.id.operation_volume_brightness);
        operation_percent = (ImageView) findViewById(R.id.operation_percent);

        join_tip_ly = (RelativeLayout) findViewById(R.id.join_tip_ly);
        GradientDrawable myGrad = (GradientDrawable) join_tip_ly.getBackground();
        myGrad.setColor(getResources().getColor(R.color.trans_half));// 设置内部颜色

        GlideUtils.loadCourseImg(VideoViewActivity.this, treepicture, buffer_bj);
        title.setText(tree_name);

        mMinHeaderHeight = getResources().getDimensionPixelSize(R.dimen.video_height);
        mHeaderHeight = getResources().getDimensionPixelSize(R.dimen.video_parent_view);
        mMinHeaderTranslation = -(mMinHeaderHeight - getResources().getDimensionPixelSize(R.dimen.top_ly_height));

        mViewPager.setOffscreenPageLimit(1);
        mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        mPagerAdapter.setTabHolderScrollingContent(this);
        mViewPager.setAdapter(mPagerAdapter);

    }

    private void initVideoView()
    {
        mViewHolder = (RelativeLayout) findViewById(R.id.view_holder);
        mediaController = (SimpleMediaController) findViewById(media_controller_bar);
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


    private void isSeekTag()
    {
        if (videoBean != null)
        {
            if (videoBean.getIs_drag() == 0)
            {
                mediaController.showSeekbar();
            } else
            {
                mediaController.goneSeekbar();
            }
            title.setText(videoBean.getTitle());
        }
    }


    private void initOnclick()
    {

        img_back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (flag.equals(Constants.OFFLINE))
                {
                    isPlayCom = true;
                    finish();
                    return;
                }
                if (!isLock)
                {
                    if (isFullScreen)
                    {
                        SetVerticalScreen();
                    } else
                    {
                        isPlayCom = true;
                        finish();
                    }
                }
            }
        });

        functAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {
                //  Log.d("zw--",spu.getIsLogin());
                if (position == 5)
                {
                    //ToastUtils.makeText(getApplicationContext(),"暂未开放此功能",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(VideoViewActivity.this, CourseQuestionsActivity.class).putExtra("courseId", Integer.valueOf(treeId)).putExtra("isJoin", isJoin));
                    return;
                }
                // 没有登录或者没有加入 显示加入弹框
                if (TextUtils.isEmpty(spu.getIsLogin()) || isJoin == 0)
                {
                    showJoinView();
                } else
                {
                    //群聊
                    if (position == 0)
                    {

                        if (isJoin == 1)
                        {
                            classList.size();
                            gotoChatDetail(classList.get(0).getId());
                        } else
                        {
                            showSelectClassDialog(position);
                        }

                    } else if (position == 1)
                    {

                        if (isJoin == 2 || isJoin == 3)
                        {//老师端作业
                            Intent intent = new Intent(VideoViewActivity.this, TeaWorkRecourseActivity.class);
                            intent.putExtra("course_id", Integer.valueOf(treeId));
                            intent.putExtra("workType", 2);
                            startActivityForResult(intent, 3);
                        }
                        if (isJoin == 1)
                        {//学生端作业
                            workNum = 0;
                            functionDelegate.workNum = 0;
                            functAdapter.notifyDataSetChanged();
                            Intent intent = new Intent(VideoViewActivity.this, StuCourseWorkActivity.class);
                            intent.putExtra("workType", 2);
                            intent.putExtra("course_id", treeId);
                            startActivityForResult(intent, 3);
                        }
                    } else if (position == 2)
                    {
                        if (isJoin == 2 || isJoin == 3)
                        {//老师端考试
                            Intent intent = new Intent(VideoViewActivity.this, TeaWorkRecourseActivity.class);
                            intent.putExtra("course_id", Integer.valueOf(treeId));
                            intent.putExtra("workType", 1);
                            startActivityForResult(intent, 3);
                        }
                        if (isJoin == 1)
                        {//学生端考试
                            examNum = 0;
                            functionDelegate.examNum = 0;
                            functAdapter.notifyDataSetChanged();
                            Intent intent = new Intent(VideoViewActivity.this, StuCourseWorkActivity.class);
                            intent.putExtra("workType", 1);
                            intent.putExtra("course_id", treeId);
                            startActivityForResult(intent, 3);
                        }

                    } else if (position == 3)
                    {
                        //课程直播
                        Intent intent = new Intent(VideoViewActivity.this, LiveCourseActivity.class);
                        intent.putExtra("treeId", treeId);
                        startActivity(intent);
                        functionDelegate.liveNum = 0;
                        functAdapter.notifyDataSetChanged();

                    } else if (position == 4)
                    {
                        //学生
                        if (isJoin == 1)
                        {
                            SignMainStudentActivity.gotoActivity(VideoViewActivity.this, treeId, classList.get(0).getId(), tree_name);
                        } else
                        {
                            //老师
                            showSelectClassDialog(position);
                        }
                    }
                }
            }
        });


        getScreen_hv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                if (isFullScreen)
                {
                    //竖屏
                    SetVerticalScreen();
                } else
                {
                    //全屏
                    setTransverseScreen();
                }

            }
        });

        lockImg.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (!isLock)
                {
                    lockImg.setImageResource(R.drawable.locked);
                    if (barTimer != null)
                    {
                        barTimer.cancel();
                        barTimer = null;
                    }
                    mediaController.hide();
                    video_top_ly.setVisibility(View.GONE);
                    selectImg.setVisibility(View.GONE);
                    isLock = true;

                } else
                {
                    lockImg.setImageResource(R.drawable.unlocked);
                    isLock = false;
                }
            }
        });


        selectImg.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (barTimer != null)
                {
                    barTimer.cancel();
                    barTimer = null;
                }
                show_Selects_View();
                adapter = new SelectsAdapter(VideoViewActivity.this, selects, isJoin);
                selects_list.setAdapter(adapter);
                for (int i = 0; i < selects.size(); i++)
                {
                    if (selects.get(i).getLast_location().equals("1"))
                    {
                        selects_list.setSelection(i);
                    }
                }

            }
        });


        selects_list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                isCheckSelect = true;
                selectPostion = position;
                hide_Selects_View();
                CatalogueBean.ItemsBean.VideoBean videoBean = selects.get(position);
                Message sendmsg = new Message();
                sendmsg.what = Constants.PREPARE_PLAY;
                Bundle bundle = new Bundle();
                bundle.putSerializable("videoBean", videoBean);

                if (flag.equals(Constants.OFFLINE))
                {
                    for (int i = 0; i < selects.size(); i++)
                    {
                        selects.get(i).setLast_location("0");
                    }
                    videoBean.setLast_location("1");
                    adapter.notifyDataSetChanged();
                    sendmsg.setData(bundle);
                    mHandler.sendMessage(sendmsg);
                    spu.setGroupId(treeId, videoBean.getCourse_chapter_id());
                    spu.setChildId(treeId, videoBean.getId());
                } else
                {
                    //未登录
                    if (isJoin == 0)
                    {
                        if (videoBean.getFree().equals("0"))
                        {
                            for (int i = 0; i < selects.size(); i++)
                            {
                                selects.get(i).setLast_location("0");
                            }
                            videoBean.setLast_location("1");
                            adapter.notifyDataSetChanged();
                            sendmsg.setData(bundle);
                            mHandler.sendMessage(sendmsg);
                            spu.setGroupId(treeId, videoBean.getCourse_chapter_id());
                            spu.setChildId(treeId, videoBean.getId());
                        } else
                        {
                            showJoinView();
                        }
                        //学生
                    } else if (isJoin == 1)
                    {
                        //如果加入课程
                        if (position == 0)
                        {
                            for (int i = 0; i < selects.size(); i++)
                            {
                                selects.get(i).setLast_location("0");
                            }
                            videoBean.setLast_location("1");
                            adapter.notifyDataSetChanged();
                            sendmsg.setData(bundle);
                            mHandler.sendMessage(sendmsg);
                            spu.setGroupId(treeId, videoBean.getCourse_chapter_id());
                            spu.setChildId(treeId, videoBean.getId());
                        } else
                        {
                            //判断上一个视频是否播放完成
                            CatalogueBean.ItemsBean.VideoBean upVideoBean = selects.get(position - 1);
                            if (upVideoBean.getIsShowLock() == 0)
                            {

                                for (int i = 0; i < selects.size(); i++)
                                {
                                    selects.get(i).setLast_location("0");
                                }
                                videoBean.setLast_location("1");
                                adapter.notifyDataSetChanged();
                                sendmsg.setData(bundle);
                                mHandler.sendMessage(sendmsg);
                                spu.setGroupId(treeId, videoBean.getCourse_chapter_id());
                                spu.setChildId(treeId, videoBean.getId());
                            } else
                            {
                                ToastUtils.makeText(getApplication(), getResources().getString(R.string.studyuplesssn), Toast.LENGTH_SHORT).show();
                            }
                        }
                        //教师/创建者
                    } else if (isJoin == 2 || isJoin == 3)
                    {
                        for (int i = 0; i < selects.size(); i++)
                        {
                            selects.get(i).setLast_location("0");
                        }
                        videoBean.setLast_location("1");
                        adapter.notifyDataSetChanged();
                        sendmsg.setData(bundle);
                        mHandler.sendMessage(sendmsg);
                        spu.setGroupId(treeId, videoBean.getCourse_chapter_id());
                        spu.setChildId(treeId, videoBean.getId());
                    }

                }
            }
        });


        paly_img.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                if (selects.size() > 0 && videoBean != null)
                {
                    isPlayCom = true;
                    if (videoBean.getType().equals("1"))
                    {
                        //未加入
                        if (isJoin == 0)
                        {
                            if (videoBean.getFree().equals("0"))
                            {
                                paly_img.setVisibility(View.GONE);
                                loading_video.setVisibility(View.VISIBLE);
                                mEventHandler.sendEmptyMessage(EVENT_PLAY);
                            } else
                            {
                                showJoinView();
                            }
                            return;
                            //学生
                        } else if (isJoin == 1)
                        {
                            for (int i = 0; i < selects.size(); i++)
                            {
                                if (videoBean.getId().equals(selects.get(i).getId()))
                                {
                                    initPostion = i;
                                }
                            }

                            if (initPostion == 0)
                            {
                                paly_img.setVisibility(View.GONE);
                                loading_video.setVisibility(View.VISIBLE);
                                mEventHandler.sendEmptyMessage(EVENT_PLAY);
                            } else
                            {
                                //判断上一个视频是否播放完成
                                CatalogueBean.ItemsBean.VideoBean upVideoBean = selects.get(initPostion - 1);
                                if (upVideoBean.getIsShowLock() == 0)
                                {
                                    paly_img.setVisibility(View.GONE);
                                    loading_video.setVisibility(View.VISIBLE);
                                    mEventHandler.sendEmptyMessage(EVENT_PLAY);
                                } else
                                {
                                    showTipVideoDialog();
                                }
                            }
                            //老师/创建者
                        } else if (isJoin == 2 || isJoin == 3)
                        {
                            paly_img.setVisibility(View.GONE);
                            loading_video.setVisibility(View.VISIBLE);
                            mEventHandler.sendEmptyMessage(EVENT_PLAY);
                        }

                    } else if (videoBean.getType().equals("6"))
                    {
                        //播放vr
                        playVrVideo();
                    }
                }
            }
        });

        download_iv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                if (downloadList.size() == 0)
                {
                    return;
                }

                if (isJoin == 0)
                {
                    showJoinView();
                    return;
                }

                //检查权限
                if (permissionsUtil.storagePermissions())
                {
//                    if (roleStudy.endsWith("1")) {
                    if (downloadList.size() > 0)
                    {
                        Intent intent = new Intent(VideoViewActivity.this, VideoView_Download_Activity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("treeid", treeId);
                        bundle.putString("tree_name", tree_name);
                        bundle.putString("treepicture", treepicture);
                        bundle.putSerializable("downloadList", (Serializable) downloadList);
                        intent.putExtras(bundle);
                        startActivityForResult(intent, 1);
                    }
//                    } else {
//                        PublicUtils.makeToast(VideoViewPlayingActivity.this, "请先加入此" + getResources().getString(R.string.courses));
//                    }
                }

            }

        });

//        title.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent =  new Intent(VideoViewActivity.this, CourseSynopsisActivity.class);
//                intent.putExtra("treeId",treeId);
//                intent.putExtra("role",role);
//                startActivity(intent);
//            }
//        });

        video_ly.setClickable(true);

    }

    public void playVrVideo()
    {
        String title = null;
        String sequence = null;
        String tid = null;
        for (int i = 0; i < downloadList.size(); i++)
        {
            List<CatalogueBean.ItemsBean.VideoBean> videos = downloadList.get(i).getVideo();
            for (int j = 0; j < videos.size(); j++)
            {
                CatalogueBean.ItemsBean.VideoBean videoBean2 = videos.get(j);
                if (videoBean2.getId().equals(videoBean.getId()))
                {
                    title = downloadList.get(i).getTitle();
                    sequence = downloadList.get(i).getSequence();
                    tid = downloadList.get(i).getId();
                }
            }
        }
        Intent intent = new Intent(VideoViewActivity.this, VrVideoActivity.class);
        intent.putExtra("videoBean", videoBean);
        intent.putExtra("catoTitle", title);
        intent.putExtra("catoSequence", sequence);
        intent.putExtra("treeid", treeId);
        intent.putExtra("tree_name", tree_name);
        intent.putExtra("tid", tid);
        intent.putExtra("isJoin", isJoin);
        intent.putExtra("treepicture", treepicture);
        startActivityForResult(intent, 1);
    }

    private void gotoChatDetail(String id)
    {
        new HttpGetBuilder(VideoViewActivity.this)
                .setUrl(UrlsNew.CHAT_ROOM)
                .setPath(id)
                .setClassObj(ChatRoomResult.class)
                .setHttpResult(this)
                .setRequestCode(1002)
                .build();

    }

    private CustomListDialog chatDialog;

    private void showSelectClassDialog(final int ps)
    {
        if (chatDialog == null)
        {
            chatDialog = new CustomListDialog(VideoViewActivity.this);
            for (CourseRoleResult.Item.ListBean list : classList)
            {
                chatDialog.addData(list.getClass_name());
            }
        }
        chatDialog.getAdapter().setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {
                if (ps == 0)
                {
                    className = classList.get(position).getClass_name();
                    gotoChatDetail(classList.get(position).getId());
                } else
                {
                    SignMainTeacherActivity.gotoActivity(VideoViewActivity.this, treeId, classList.get(position).getId());
                }
                chatDialog.dismiss();
            }
        });
        chatDialog.show();

    }

    public void showTipVideoDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(VideoViewActivity.this, R.style.custom_dialog);
        builder.setMessage(getResources().getString(R.string.studyuplesssn));
        builder.setPositiveButton(getResources().getString(R.string.password_dialog_ensure), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
            }
        });
        builder.show();
    }

    public void showJoinView()
    {
        join_tip_ly.setVisibility(View.VISIBLE);
        mHandler.postDelayed(runnable, 2000);// 打开定时器，执行操作
    }

    private void setTransverseScreen()
    {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
        video_top_ly.setBackgroundResource(R.color.trans_black);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        video_ly.setLayoutParams(lp);
        video_ly.setClickable(false);
        parent_view.setLayoutParams(lp);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        FullScreenUtils.toggleHideyBar(VideoViewActivity.this);
        isFullScreen = true;
        getScreen_hv.setBackgroundResource(shrink_screen);
        function_view.setVisibility(View.GONE);
        mViewPager.setVisibility(View.GONE);
        lockImg.setVisibility(View.VISIBLE);
        if(selects!=null&&selects.size()!=0){
            selectImg.setVisibility(View.VISIBLE);
        }
        hideOuterAfterFiveSeconds();
    }

    private void SetVerticalScreen()
    {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//清除全屏
        video_top_ly.setVisibility(View.VISIBLE);
        video_top_ly.setBackgroundResource(R.color.transparent);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                (int) getResources().getDimension(R.dimen.video_height));
        video_ly.setLayoutParams(lp);
        video_ly.setClickable(true);
        FrameLayout.LayoutParams lp2 = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                (int) getResources().getDimension(R.dimen.video_parent_view));
        parent_view.setLayoutParams(lp2);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);

        isFullScreen = false;
        getScreen_hv.setBackgroundResource(enlarge_screen);
        function_view.setVisibility(View.VISIBLE);
        mViewPager.setVisibility(View.VISIBLE);
        lockImg.setVisibility(View.GONE);
        selectImg.setVisibility(View.GONE);
        hide_Selects_View();
        video_ly.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onClickEmptyArea();
            }
        });
        hideOuterAfterFiveSeconds();
        if (isCheckSelect)
        {
            mainListener.onSetLastPosition(selectPostion);
        }

    }

    private void switchItemOnclickPlay(Message msg)
    {
        //获得即将要播放的对象
        videoBean = (CatalogueBean.ItemsBean.VideoBean) msg.getData().getSerializable("videoBean");
        isSeekTag();
        //获取目录类型
        String type = videoBean.getType();
        //判断网络
        if (!NetworkUtil.isNetworkAvailable(VideoViewActivity.this) && flag.equals(Constants.ONLINE))
        {
            NetworkUtil.httpNetErrTip(VideoViewActivity.this, activity_video_view);
            return;
        }
        if (type != null)
        {

            if (type.equals("1"))
            {
                if (!NetworkUtil.isWifiNetwork(VideoViewActivity.this))
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(VideoViewActivity.this, R.style.custom_dialog);
                    builder.setMessage(getResources().getString(R.string.no_wifi_to_flow));
                    builder.setPositiveButton(getResources().getString(R.string.password_dialog_ensure), new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            paly_img.setVisibility(View.GONE);
                            loading_video.setVisibility(View.VISIBLE);
                            changeTopCor();
                            buffer_bj.setVisibility(View.VISIBLE);
                            videoView.stopPlayback();
                            mEventHandler.sendEmptyMessage(EVENT_PLAY);
                            if (scrollY != 0)
                            {
                                listView.setSelection(0);
                                isSelectPosition = true;
                            }
                            dialog.dismiss();
                        }
                    });

                    builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener()
                    {

                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                } else
                {
                    paly_img.setVisibility(View.GONE);
                    loading_video.setVisibility(View.VISIBLE);
                    changeTopCor();
                    buffer_bj.setVisibility(View.VISIBLE);
                    videoView.stopPlayback();
                    mEventHandler.sendEmptyMessage(EVENT_PLAY);
                    if (scrollY != 0)
                    {
                        listView.setSelection(0);
                        isSelectPosition = true;
                    }
                }

            } else if (type.equals("6"))
            {
                //播放vr
                playVrVideo();

            } else
            {
                Intent intent = new Intent(VideoViewActivity.this, TextView_Link_Activity.class);
                intent.putExtra("title", videoBean.getTitle());
                if (type.equals("2"))
                {
                    intent.putExtra("web_url", videoBean.getFile_url());
                    startActivity(intent);
                } else if (type.equals("3"))
                {
                    intent.putExtra("web_url", videoBean.getDesc());
                    startActivity(intent);
                } else if (type.equals("4"))
                {
                    intent.putExtra("web_url", videoBean.getResource_url());
                    startActivity(intent);
                } else if (type.equals("practice"))
                {


                } else if (type.equals("unity3d"))
                {
                    String url_content = videoBean.getMediaUri();
                    Uri uri = Uri.parse(url_content);
                    Intent intent2 = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent2);
                }
                postComData();
                resLockStu();

            }

        }
    }


    private void resLockStu()
    {
        if (adapter != null)
        {
            for (int i = 0; i < selects.size(); i++)
            {
                if (videoBean.getId().equals(selects.get(i).getId()))
                {
                    PlayPostion = i;
                    selects.get(i).setIsShowLock(0);
                }
                if (i > PlayPostion)
                {
                    if (selects.get(i).getIs_see().equals("0"))
                    {
                        selects.get(i).setIsShowLock(0);
                    } else
                    {
                        break;
                    }
                }
            }
            adapter.notifyDataSetChanged();
        }
        mainListener.onResAdaterData(videoBean);
        mediaController.setPostionZero();
    }

    private void changeStatus(PlayerStatus status)
    {
        mPlayerStatus = status;
        if (this.mediaController != null)
        {
            this.mediaController.changeStatus(status);
        }
    }

    //上报 //老师不上报视频进度
    private void postComData()
    {

        if (isJoin != 2 && isJoin != 3)
        {

            new HttpPostBuilder(VideoViewActivity.this).setClassObj(BaseBean.class).setHttpResult(new HttpCallBack()
            {
                @Override
                public void setOnSuccessCallback(int requestCode, Object resultBean)
                {

                }

                @Override
                public void setOnErrorCallback(int requestCode, int code, String msg)
                {
                    //储存未上报成功的视频
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("id", videoBean.getId());
                    ids.add(map);
                    spu.setLessinIdList(ids);
                }
            }).setUrl(UrlsNew.POST_LESSON_FINISH)
//                                    .addBodyParam(jsonArray.toString())
                    .addBodyParam("id", videoBean.getId())
                    .build();
        }
    }

    public void stopPlay()
    {
        paly_img.setVisibility(View.GONE);
        buffer_bj.setVisibility(View.VISIBLE);
        if (videoView != null)
            videoView.stopPlayback();
    }

    /**
     * 检测'点击'空白区的事件，若播放控制控件未显示，设置为显示，否则隐藏。
     */
    public void onClickEmptyArea()
    {

        if (!isLock)
        {
            if (barTimer != null)
            {
                barTimer.cancel();
                barTimer = null;
            }
            if (this.mediaController != null)
            {
                if (mediaController.getVisibility() == View.VISIBLE)
                {
                    mediaController.hide();
                    hide_Selects_View();
                    if (isFullScreen)
                    {
                        video_top_ly.setVisibility(View.GONE);
                        lockImg.setVisibility(View.GONE);
                        selectImg.setVisibility(View.GONE);
                    }
                } else
                {
                    mediaController.show();
                    video_top_ly.setVisibility(View.VISIBLE);
                    if (isFullScreen)
                    {
                        lockImg.setVisibility(View.VISIBLE);
                        if(selects!=null&&selects.size()!=0){
                            selectImg.setVisibility(View.VISIBLE);
                        }
                    }

                }
            }
            hideOuterAfterFiveSeconds();
        }

    }

    private void hideOuterAfterFiveSeconds()
    {
        if (!isFullScreen)
        {
            return;
        }
        if (barTimer != null)
        {
            barTimer.cancel();
            barTimer = null;
        }
        barTimer = new Timer();
        barTimer.schedule(new TimerTask()
        {

            @Override
            public void run()
            {
                if (!isFullScreen)
                {
                    return;
                }
                if (mediaController != null)
                {
                    mediaController.getMainThreadHandler().post(new Runnable()
                    {

                        @Override
                        public void run()
                        {
                            mediaController.hide();
                            video_top_ly.setVisibility(View.GONE);
                            lockImg.setVisibility(View.GONE);
                            selectImg.setVisibility(View.GONE);

                        }

                    });
                }
            }

        }, 5 * 1000);

    }

    /**
     * 记录当前播放视频的最后观看位置
     */
    private void recordPosition()
    {
        int mLastPos = videoView.getCurrentPosition();
        int duration = videoView.getDuration();
        if (mLastPos >= duration)
        {
            spu.setLastPosition(treeId, videoBean.getCourse_chapter_id(), videoBean.getId(), 0);
        } else
        {
            spu.setLastPosition(treeId, videoBean.getCourse_chapter_id(), videoBean.getId(), mLastPos);
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        Log.v(TAG, "onPause");
        /**
         * 在停止播放前 你可以先记录当前播放的位置,以便以后可以续播
         */
        if (videoView.isPlaying() && (mPlayerStatus != PlayerStatus.PLAYER_IDLE))
        {
            // when scree lock,paus is good select than stop
            // don't stop pause
            // mVV.stopPlayback();
            recordPosition();
            mediaController.setPlay();
            videoView.pause();


        }
    }


    @Override
    protected void onResume()
    {
        super.onResume();

        Log.v(TAG, "onResume");
        // 发起一次播放任务,当然您不一定要在这发起
        if (!videoView.isPlaying() && (mPlayerStatus != PlayerStatus.PLAYER_IDLE))
        {
            videoView.resume();
            mediaController.setPause();
        } else
        {
//
        }
    }


    @Override
    protected void onStop()
    {
        super.onStop();
        Log.v(TAG, "onStop");
        // 在停止播放前 你可以先记录当前播放的位置,以便以后可以续播
        if (videoView.isPlaying() && (mPlayerStatus != PlayerStatus.PLAYER_IDLE))
        {
            // don't stop pause
            // mVV.stopPlayback();
            mediaController.setPlay();
            videoView.pause();
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        mOrientationListener.disable();
        if ((mPlayerStatus != PlayerStatus.PLAYER_IDLE))
        {
            videoView.stopPlayback();
        }
        /**
         * 结束后台事件处理线程
         */
        mHandlerThread.quit();
        synchronized (syncPlaying)
        {
            try
            {
                if (!isReadyForQuit)
                {
                    Log.v(TAG, "waiting for notify invoke or 2s expires");
                    syncPlaying.wait(2 * 1000);
                }
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * 播放完成
     */
    @Override
    public void onCompletion()
    {
        synchronized (syncPlaying)
        {
            isReadyForQuit = true;
            syncPlaying.notifyAll();
        }
        changeStatus(PlayerStatus.PLAYER_COMPLETED);
        mHandler.removeMessages(UI_EVENT_UPDATE_CURRPOSITION);
        if ((mediaController.getSeekMax() - mediaController.getSeekPos()) < 5)
        {
            isPlayCom = false;
        } else
        {
            isPlayCom = true;
        }

        mHandler.sendEmptyMessage(PLAY_COMPLETE);
    }

    @Override
    public void OnCompletionWithParam(int i)
    {

    }

    /**
     * 播放出错
     */
    @Override
    public boolean onError(int i, int i1)
    {
        Log.v(TAG, "onError");
        synchronized (syncPlaying)
        {
            isReadyForQuit = true;
            syncPlaying.notifyAll();
        }
        changeStatus(PlayerStatus.PLAYER_IDLE);
        return true;
    }

    @Override
    public boolean onInfo(int what, int iextra)
    {
        switch (what)
        {
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

    /**
     * 当前缓冲的百分比， 可以配合onInfo中的开始缓冲和结束缓冲来显示百分比到界面
     */
    @Override
    public void onPlayingBufferCache(int i)
    {

    }

    /**
     * 播放准备就绪
     */
    @Override
    public void onPrepared()
    {
        hideOuterAfterFiveSeconds();
        changeStatus(PlayerStatus.PLAYER_PREPARED);
        mHandler.sendEmptyMessage(GONE_PROGRESSBAR);
        mHandler.sendEmptyMessage(UI_EVENT_UPDATE_CURRPOSITION);

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
    {

    }

    @Override
    public void onPageSelected(int position)
    {
//        SparseArrayCompat<ScrollTabHolder> scrollTabHolders = mPagerAdapter.getScrollTabHolders();
//        ScrollTabHolder currentHolder = scrollTabHolders.valueAt(position);
//        currentHolder.adjustScroll((int) (parent_view.getHeight() + ViewHelper.getTranslationY(parent_view)));
    }

    @Override
    public void onPageScrollStateChanged(int state)
    {

    }

    @Override
    public void adjustScroll(int scrollHeight)
    {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount, int pagePosition)
    {
        listView = view;
        if (mViewPager.getCurrentItem() == pagePosition)
        {
            scrollY = getScrollY(view);
            if (!videoIsShow)
            {
                ViewHelper.setTranslationY(parent_view, Math.max(-scrollY, mMinHeaderTranslation));
            } else
            {
                ViewHelper.setTranslationY(parent_view, Math.max(-scrollY, 0));
            }

            if (firstVisibleItem == 0 && isSelectPosition)
            {
                mainListener.onSetSelectcPostion();
                isSelectPosition = false;

            }

        }
    }

    public int getScrollY(AbsListView view)
    {
        View c = view.getChildAt(0);
        if (c == null)
        {
            return 0;
        }

        int firstVisiblePosition = view.getFirstVisiblePosition();
        int top = c.getTop();

        int headerHeight = 0;
        if (firstVisiblePosition >= 1)
        {
            headerHeight = mHeaderHeight;
        }

        return -top + firstVisiblePosition * c.getHeight() + headerHeight;
    }

    public BVideoView getVideoView()
    {
        return videoView;
    }

    public ProgressBar getProgressBar()
    {
        return loading_video;
    }


    public void changeTopCor()
    {
        video_top_ly.setBackgroundResource(R.color.transparent);
    }

    public void changeTopCor2()
    {
        video_top_ly.setBackgroundResource(R.color.first_theme);
    }

    public void isShowVideo(boolean b)
    {
        this.videoIsShow = b;
    }

    public Handler getmHandler()
    {
        return mHandler;
    }

    @Override
    public void onAttachFragment(Fragment fragment)
    {
        try
        {
            mainListener = (OnMainListener) fragment;
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        super.onAttachFragment(fragment);
    }

    public interface OnMainListener
    {
        public void onSetSelectcPostion();

        public void onSetLastPosition(int selectPostion);

        public void onResAdaterData(CatalogueBean.ItemsBean.VideoBean videoBean);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (mGestureDetector.onTouchEvent(event))
        {
            return true;
        }
        // 处理手势结束
        switch (event.getAction() & MotionEvent.ACTION_MASK)
        {
            case MotionEvent.ACTION_UP:

                if (GESTURE_FLAG == GESTURE_MODIFY_PROGRESS)
                {
                    videoView.resume();
                    gesture_progress_layout.setVisibility(View.GONE);
                    onClickEmptyArea();
                    if (mediaController != null)
                    {
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


    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener
    {
        /**
         * 双击
         */
        @Override
        public boolean onDoubleTap(MotionEvent e)
        {
            return true;
        }

        /**
         * 单击
         */
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e)
        {
            onClickEmptyArea();
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
        {

            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE && buffer_bj.getVisibility() == View.GONE)
            {

                if (firstScroll)
                {// 以触摸屏幕后第一次滑动为标准，避免在屏幕上操作切换混乱

                    if (Math.abs(distanceX) >= Math.abs(distanceY))
                    {

                        if (videoBean.getIs_drag() == 0)
                        {
                            gesture_progress_layout.setVisibility(View.VISIBLE);
                            GESTURE_FLAG = GESTURE_MODIFY_PROGRESS;
                            videoView.pause();
                            if (barTimer != null)
                            {
                                barTimer.cancel();
                                barTimer = null;
                            }
                            if (mediaController != null)
                            {
                                mediaController.setIsDragging(true);
                            }
                            currPosition = videoView.getCurrentPosition();
                            duration = videoView.getDuration();
                        }


                    } else
                    {
                        gesture_progress_layout.setVisibility(View.GONE);
                        GESTURE_FLAG = GESTURE_MODIFY_VOLUME;
                    }

                }
                if (GESTURE_FLAG == GESTURE_MODIFY_PROGRESS)
                {
                    if (mediaController != null)
                    {
                        mediaController.show();
                    }
                    if (videoBean.getIs_drag() == 0)
                    {
                        if (Math.abs(distanceX) > Math.abs(distanceY))
                        {// 横向移动大纵向移动
                            if (distanceX >= DensityUtil.dip2px(VideoViewActivity.this, STEP_PROGRESS))
                            {// 快退，用步长控制改变速度，可微调
                                gesture_iv_progress.setImageResource(R.drawable.souhu_player_backward);
                                if (currPosition > 0)
                                {// 避免为负
                                    currPosition -= 5;// scroll方法执行一次快退5秒
                                }
                            } else if (distanceX <= -DensityUtil.dip2px(VideoViewActivity.this, STEP_PROGRESS))
                            {// 快进
                                gesture_iv_progress.setImageResource(R.drawable.souhu_player_forward);
                                if (currPosition < duration)
                                {// 避免超过总时长
                                    currPosition += 3;// scroll执行一次快进3秒
                                }
                            }
                            if (mediaController != null)
                            {
                                mediaController.gestureUpdatePostion(currPosition, geture_tv_progress_cut_time, geture_tv_progress_time);
                            }
                        }
                    }
                } else if (GESTURE_FLAG == GESTURE_MODIFY_VOLUME)
                {

                    float mOldX = e1.getX();
                    float mOldY = e1.getY();
                    int y = (int) e2.getRawY();
                    Display disp = getWindowManager().getDefaultDisplay();
                    int windowWidth = disp.getWidth();
                    int windowHeight = disp.getHeight();

                    // 右边滑动改变音量大小
                    if (mOldX > windowWidth * 4.0 / 5)
                    {
                        onVolumeSlide((mOldY - y) / windowHeight);

                        // 左边滑动改变亮度大小
                    } else if (mOldX < windowWidth / 5.0)
                    {
                        onBrightnessSlide((mOldY - y) / windowHeight);

                    }

                }

                firstScroll = false;// 第一次scroll执行完成，修改标志
            }

            return false;
        }

        @Override
        public boolean onDown(MotionEvent e)
        {
            firstScroll = true;

            return false;
        }
    }


    /**
     * 滑动改变声音大小
     *
     * @param percent
     */
    private void onVolumeSlide(float percent)
    {
        if (currentVolume == -1)
        {
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
    private void onBrightnessSlide(float percent)
    {
        if (mBrightness < 0)
        {
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


    /**
     * 显示选集view
     */
    private void show_Selects_View()
    {
        if(selects==null||selects.size()==0){
            return;
        }

        if (selects_layout.getVisibility() == View.GONE)
        {
            Animation rt_in_animation = AnimationUtils.loadAnimation(VideoViewActivity.this, R.anim.pop_horizontal_in);
            selects_layout.startAnimation(rt_in_animation);
            selects_layout.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 隐藏选集view
     */
    private void hide_Selects_View()
    {
        if (selects_layout.getVisibility() == View.VISIBLE)
        {
            Animation rt_out_animation = AnimationUtils.loadAnimation(VideoViewActivity.this, R.anim.pop_horizontal_out);
            selects_layout.startAnimation(rt_out_animation);
            selects_layout.setVisibility(View.GONE);
        }

    }


    public class PagerAdapter extends FragmentPagerAdapter
    {

        private SparseArrayCompat<ScrollTabHolder> mScrollTabHolders;
        private final String[] TITLES = {"Page 1"};
        private ScrollTabHolder mListener;

        public PagerAdapter(FragmentManager fm)
        {
            super(fm);
            mScrollTabHolders = new SparseArrayCompat<ScrollTabHolder>();
        }

        public void setTabHolderScrollingContent(ScrollTabHolder listener)
        {
            mListener = listener;
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            return TITLES[position];
        }

        @Override
        public int getCount()
        {
            return TITLES.length;
        }

        @Override
        public Fragment getItem(int position)
        {
            ScrollTabHolderFragment fragment = (ScrollTabHolderFragment) VideoViewFragment.newInstance(position, treeId, classId, isCenter, scanQrcode);
            mScrollTabHolders.put(position, fragment);
            if (mListener != null)
            {
                fragment.setScrollTabHolder(mListener);
            }

            return fragment;
        }

        public SparseArrayCompat<ScrollTabHolder> getScrollTabHolders()
        {
            return mScrollTabHolders;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {

        if (permissionsUtil.permissionsResult(requestCode, permissions, grantResults))
        {

//           if (roleStudy.endsWith("1")) {
            if (downloadList.size() > 0)
            {
                Intent intent = new Intent(VideoViewActivity.this, VideoView_Download_Activity.class);
                Bundle bundle = new Bundle();
                bundle.putString("treeid", treeId);
                bundle.putString("tree_name", tree_name);
                bundle.putString("treepicture", treepicture);
                bundle.putSerializable("downloadList", (Serializable) downloadList);
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);
            }
//            } else {
//               PublicUtils.makeToast(VideoViewPlayingActivity.this, "请先加入此" + getResources().getString(R.string.courses));
//            }

        } else
        {

        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == 8)
        {
            resLockStu();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
        {
            if (flag.equals(Constants.OFFLINE))
            {
                isPlayCom = true;
                finish();
            } else
            {
                if (!isLock)
                {
                    if (isFullScreen)
                    {
                        SetVerticalScreen();
                    } else
                    {
                        isPlayCom = true;
                        finish();
                    }
                }
            }
            return true;
        }

        return false;
    }

}

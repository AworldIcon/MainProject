package com.coder.kzxt.utils;

import android.os.Environment;

import com.app.utils.BaseConstants;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 公用常量类
 */
public class Constants  extends BaseConstants{

    //下载服务action
    public static final String DOWN_SERVICE_FITER = "HBSI.download.service.action";

    public static final String MY_DOWNLOAD_DOWNING = "android.intent.action.HBSI_DOWNLOAD_DOWNING";
    //下载完成广播
    public static final String MY_DOWNLOAD_FINSH = "android.intent.action.HBSI_DOWNLOAD_FINSH";
    //下载失败广播
    public static final String MY_DOWNLOAD_FAIL = "android.intent.action.HBSI_DOWNLOAD_FAIL";

    public static final String MY_DOWNLOAD_REFRESH = "android.intent.action.HBSI_DOWNLOAD_REFRESH";

    /**
     * 平台
     */
    public static final String PLATFORM = "1"; //android
    /*
     * 是否是公共课
     */
    public static final String IS_CENTER = "publicCourse";
    //刷新首页
    public static  final String LOGIN_NOTICE="login_notice";
    //百度播放器ak sk
    public static final String BAIDU_VIDEOVIEW_AK = "7c9c907eee794bc79c5bd25cb5effba7";
//    public static final String BAIDU_VIDEOVIEW_SK = "Sy8G5FiI19wGST5S";
    //在mob平台申请的shareSDK的ID
    public static final String SHARESDKEY = "c234fe165dfc";

    //创建存储普通数据SharedPreferences标识
    public static final String CUURRENT_SP = "currentPosition";

    //文件下载的总文件夹名
    public  static final String MAIN_FOLDER = "/yunketang";
    //更新的apk文件夹名
    public  static final String UPDATE_APK = "/updateApk";
    //apk下载文件路径
    public static final String APK_PATH = Environment.getExternalStorageDirectory().getPath() + MAIN_FOLDER+UPDATE_APK;
    //apk下载全路径
    public static final String SAVE_FILE_NAME = APK_PATH + "HBSI_Formal_Edition.apk";

    // 判断sdk版本是否大于11 即4.0以上
    public static final boolean API_LEVEL_11 = android.os.Build.VERSION.SDK_INT > 11;
    // 判断 sdk版本是否等于19 即4.4系统
    public static final boolean API_LEVEL_19 = android.os.Build.VERSION.SDK_INT >= 19;

    //视频文件存储文件夹名
    public static String VIDEOS = "/HBSI_videos";
    //存放m3u8 urls文件夹名
    public static String M3U8_URLS = "/woying_urls";
    //存放m3u8文件夹名
    public static String M3U8 = "/woying_m3u8";
    //存放m3u8 key文件夹名
    public static String M3U8_KEY = "/woying_key";
    //存放vr mp4文件夹名
    public static String VR_VIDEO = "/woying_vr";

    //存储m3u8 url的全路径
    public static final String DOWNLOAD_URLS =  MAIN_FOLDER + VIDEOS + M3U8_URLS;
    //存储m3u8的全路径
    public static final String DOWNLOAD_M3U8 = MAIN_FOLDER + VIDEOS + M3U8;
    //存储m3u8 key的全路径
    public static final String DOWNLOAD_KEY = MAIN_FOLDER + VIDEOS + M3U8_KEY;
    //存储vr mp4的全路径
    public static final String DOWNLOAD_VR = MAIN_FOLDER + VIDEOS + VR_VIDEO;

    //图片存储总目录总名称
    public static String POST_ORIGINAL_PHOTO = Environment.getExternalStorageDirectory().getPath() + "/yunketang/HBSI_img/";
    //开屏广告本地目录
    public static String SPREAD = POST_ORIGINAL_PHOTO+"HBSI_spread_img/wel_ad.jpg";
    // 用户保存的图片
    public static String SAVE_PICTURE = POST_ORIGINAL_PHOTO+"HBSI_save_picture/";
    //上传压缩图片文件
    public static String POST_PHOTO = POST_ORIGINAL_PHOTO+"HBSI_formats_img/formats/";

    // 计时完成广播action
//    public static final String MY_TIME_TO_COMPLETE = "android.intent.action.HBSI_TIME_TO_COMPLETE";
    //用户信息
    public static final String ISLOGIN = "is_login"; // 标记是否登录：0代表没有登录，1代表已经登录
    // 收藏试题标志
    public final static int FAVORITES = 0;
    // 取消收藏试题标志
    public final static int CANCE_FAVORITES = 1;
    // 完成考试
    public static final int COMPLETE_TEST = 0;
    public static final String MY_REFRESG_TESTPAGER = "android.intent.action.HBSI_REFRESG_TESTPAGER";

    //检查软件盘弹出用到的三个常量
    public static final int BIGGER = 1;
    public static final int SMALLER = 2;
    public static final int MSG_RESIZE = 1;


    // 是否选择外置卡
    public static final String SELECTFLAG = "selectflag";
    // 外置卡路径
    public static final String SECONDSDCARD = "secondSdcard";
    // 拍照
    public static final int TAKE_PICTURE = 0;
    // 选择相册
    public static final int ALBUM_PICTURE = 1;
    // 剪裁图片
    public static final int CROP_PICTURE = 3;
    //activiy销毁前保存拍照图片的路径
    public static final String IMAGE_FILE_PATH = "ImageFilePath";
    /* 照片存储路径 */
    public static final String HEAD_PIC_URL = Environment.getExternalStorageDirectory().getPath() + "/yunketang/HBSI_headbg/";
    /* 录音存储路径 */
    public static String RECORD = Environment.getExternalStorageDirectory().getPath() + "/yunketang/HBSI_record";
    // 全局配置信息
    public static final String ADMIN_MOBILE = "admin_mobile";
    public static final String SHOW_LAUNCHER = "show_launcher";
    public static final String TIP_LOGIN_NO_ACCOUNT = "tip_login_no_account";
    public static final String TIP_LOGIN_ERR_PWD = "tip_login_err_pwd";
    public static final String CREATE_COURSE_EXPECT_TIP = "create_course_expect_tip";
    public static final String PROCESS_WORK_EXPECT_TIP = "process_work_expect_tip";
    public static final String LOOK_SCORE_EXPECT_TIP = "look_score_expect_tip";
    public static final String ADD_WORK_TIP = "add_work_tip";
    public static final String ADD_EXAM_TIP = "add_exam_tip";
    public static final String LOGIN_TIP = "login_tip";
    public static final String COURSE_CLASS_IMPORT_MEMBER_TIP = "course_class_import_member_tip";
    public static final String TASK_ACTIVITY_PAGE = "TASK_ACTIVITY_PAGE";
    public static final String SERVICE_INTRODUCE = "service_introduce";
    public static final String SERVICE_AUTH = "service_auth";
    public static final String SERVICE_JOB = "service_job";
    public static final String WXUSERID = "wxuserid";
    public static final String QQUSERID = "qquserid";
    public static final String SHOWLOGO = "showlogo";
    public static final String LOGIN_IMG = "login_img";

    //显示插屏广告的渠道
    public static final String SPREAD_CHANNEL = "spread_channel";

    //显示开屏广告的渠道
    public static final String CUT_CHANNEL = "cut_spread";
    //开屏广告url
    public static final String SPREAD_URL = "spread_url";


    //可注册的方式(是否显示注册按钮或跳转位置，默认返回3  2种方式都有    0-不能注册  1-只有手机    2-只有邮箱)
    public static final String REGISTER_METHODS = "register_methods";
    //可登录的方式(改变登录的提示语 默认1  可以学号登录    0-没有学号)
    public static final String LOGIN_METHODS = "login_methods";
    //可以三方登录的方式(是否显示三方登录入口,或者显示哪一个入口,0-全显示  1-微信  2-QQ  3-全不显示)
    public static final String OAUTH_INFO = "oauth_info";



    //是否显示qq登录
    public static final String SHOW_Q_lOGIN = "show_q_login";
    //是否显示微信登录
    public static final String SHOW_W_lOGIN = "show_w_login";

    //微信登录需要的id和skey
    public static final String WX_ID = "wx_id";
    public static final String WX_SK = "wx_sk";
    //QQ登录需要的id和skey
    public static final String QQ_ID = "qq_id";
    public static final String QQ_SK = "qq_sk";


    public static final String ISIMLOGIN = "is_im_login"; // 标记是否登录：0代表没有登录，1代表已经登录
    public static final String ISUSER_BG = "is_user_bg";
    public static final String USER_TYPE = "User_Type";
    public static final String ISJOIN = "isjoin";
    public static final String OPENOUID = "openuid";
    public static final String NICKNAME = "nickname";
    public static final String BALANCE = "balance ";
    public static final String COIN = "coin ";
    public static final String USERHEAD = "userhead";
    public static final String PERBANNER = "banner";
    public static final String PHONE = "phone";
    public static final String EMAIL = "email";
    public static final String SEX = "sex";
    public static final String MOTTO = "motto";
    public static final String ClASSSTATE = "classState";
    public static final String ENCR = "encr";
    public static final String JIVE = "jive";
    public static final String NOTICENUM = "noticeNum";
    public static final String MAILNUM = "mailNum";
    public static final String OPEN_MOVE_NET = "open_move_net";
    public static final String ACCOUNT = "account";
    public static final String STUDENTNUM = "studentnum";
    public static final String IDPHOTO = "idphoto";
    public static final String YUNNAME = "yunname";
    public static final String QQNAME = "qqname";
    public static final String WXNAME = "wxname";
    public static final String COUPONS = "coupons";
    public static final String BOTTOMBAREN="bottombaren";
    public static final String BOTTOMBARCN="bottombarcn";



    public static final String CLASSSETTINGIMH = "classsettingimg";
    public static final String CLASSSETTINGSTATUSLIMIT = "classsettingstatuslimitgai";
    public static final String CLASSSETTINGJOINYEAR = "classsettingjoinyear";
    public static final String CLASSSETTINGCLASSNAME = "classsettingclassname";
    public static final String CLASSSETTINGPROFESSIONAL = "classsettingprofessionalname";
    public static final String CLASSSETTINGABOUTTEXT = "classsettingabouttext";
    public static final String CLASSBLACKBOARDTEXT = "blackboardtext";
    public static final String CLASSSETTISELECTPOSITION = "classsettingselectposition";
    public static final String CLASSTOPICLIMIT = "classtopiclimit";
    public static final String WIFICLOSE = "wificlose";
    public static final String POSTERS_TYPE = "Posters_Type";
    public static final String COURSE_NOTIFY = "course_notify";
    public static final String CLASS_NOTIFY = "class_notify";
    public static final String POSTER_NOTIFY = "poster_notify";
    public static final String SIGN_NOTIFY = "sign_notify";
    public static final String CONVERSATION_NOTIFY = "conversation_notify";
    public static final String NIGHT_NOTIFY = "night_notifi";
    public static final String APPLYNUM = "applynum";
    public static final String QUESTION_UPDATE="questions_update_fragment";
    public static final String QUESTION_LOGIN="questions_login_sucess";

    public static final int REFRESHMAIN = 1;
    public static final String REF_USERINFO = "android.intent.action.HBSI_REF_USERINFO";
    public static final String REFRESHBROADCAST = "com.coder.HBSI.activity.refreshdata";
    //用户ID照
    public static String ID_PICTURE = Environment.getExternalStorageDirectory().getPath() + "/yunketang/HBSI_id_picture/";

    /**
     * 搜索的关键字 可以共用
     */
    public static final String SEARCH_KEYWORD = "Search__Keyword";
    public static final String SEARCH_HISTORY_POSTERS = "Search_History_Posters";
    public static final String SEARCH_LOCAL_COURSE = "Search_local_course";
    public static final String SEARCH_LIVEL_COURSE = "Search_live_course";
    public static final String SEARCH_WORK = "Search_work";
    public static final String SEARCH_WORK_BANK = "Search_work_bank";
    public static final String SEARCH_FINISH_STU = "Search_finish_stu";
    public static final String SEARCH_UNFINISH_STU = "Search_unfinish_stu";
    public static final String SEARCH_STU_WORK = "Stu_search_work";
    public static final String SEARCH_SERVICE = "Search_service";

    /**
     * 模块配置缓存
     */
    public static final String MODULE_CONFIG = "module_config";
    /**
     * 关于我们分享地址
     */
    public static final String ABOUT_US_SHARE_URL = "about_us_share_url";
    /**
     * 关于我们官网地址
     */
    public static final String ABOUT_US_ADDRESS_URL = "about_us_address_url";
    /***
     * 订单选择课程名称
     */
    public static final String ORDER_SELECT_CLASS_NAME = "order_select_class_name";

    // 设备串号
    public static final String IMEI_NUM = "imei_num";

    // 关闭登录页面
    public static final String CLOSE_LOGIN = "close_login";

    public static final String Is_Join_Course = "is_join_course";

    // 使AsyncTask可以并行执行
    public static ExecutorService exec = Executors.newCachedThreadPool();

    //上课类型
    public static final String SIGN_CLASS_TYPE = "sign_class_type";
    //聊天昵称
    public static final String CHAT_NICK_NAME = "chat_nick_name";
    //聊天最大长度
    public static final int CHAT_SEND_MESSAGE_MAX_LENGTH = 1000;
    //支付方式
    public static final String PAY_TYPE = "payment_provider"; //1 支付宝 2.微信

    //搜索标示
    public static final String SEARCH_FLAG = "search_flag";
    //本站地址开关 0：关闭 1：开启
    public static final String LOCAL_SWITCH = "local_switch";
    //中心地址开关 0：关闭 1：开启
    public static final String CLOUD_SWITCH = "cloud_switch";
    //地址开关 总开关 0：关闭 1：开启
    public static final String ADDRESSS_WITCH = "address_switch";
    //用户信息入口
    public static final String USER_INFO_ENTRANCE = "user_info_entrance";
    //评分
    public static final int RATING_SCORE = 10011;
    //结果码
    public static final int RESULT_CODE = 10012;
    //请求码
    public static final int REQUEST_CODE = 10013;
    //打开相册请求码
    public static final int REQUEST_PICTURE_OK = 10014;
    //扫码页面
    public static final int REQUEST_SCAN_OK = 10015;
    //扫描结果码
    public static final int RESULT_SCAN_OK = 10016;
    //添加地址
    public static final String ADD_ADDRESS = "add_address";
    //班级相册
    public static String CLASS_PHOTO = Environment.getExternalStorageDirectory().getPath() + "/yunketang/HBSI_class_photo_img/formats/";
    /**
     * 播放器用到的常量
     */
    // 每一次请求播放
    public static final int EVENT_PLAY = 0;
    //播放缓冲
    public static final int BUFFERING_START = 1;
    //缓冲结束
    public static final int BUFFERING_END = 2;
    //隐藏缓冲ProgressBar
    public static final int GONE_PROGRESSBAR = 3;
    //点击视频列表准备播放
    public static final int PREPARE_PLAY = 4;
    //加载完视频列表发送当前选择的播放对象
    public static final int INIT_SELECT = 5;
    //发送选集数据和下载数据标识
    public static final int LIST_DATA = 6;
    //隐藏加入提示
    public static final int JOIN_TIP =7;
    //播放完成
    public final static int PLAY_COMPLETE = 8;
    //切换录播
    public final static int CHANGEPLAY = 9;
    //切换直播
    public final static int CHANGELIVE = 10;
    //定时隐藏
    public final static int TIMING_HIDE = 11;
    //播放错误
    public static final int LIVE_ERROR = 12;
    //更新ui
    public static final int UI_EVENT_UPDATE_CURRPOSITION = 13;
    //发送im聊天数据
    public static final int lIVE_IM_DATA = 14;
    //刷新im聊天数据
    public static final int LIVE_RES_MSG = 15;
    //初始化加入等数据
    public static final int INIT_DATA = 16;

    /**
     * 被抢登用的的两个常量
     */
    //抢登跳转登录页请求码
    public static final int RESTART_LOGIN = 177;
    //登录成功返回码
    public static final int LOGIN_BACK = 277;

    /**
     * 刷新上一个activity常量
     */
    public static final int RES_BACK_AC = 377;

    //搜索全部
    public static final String SEARCH_ALL = "search_all";
    //申请列表的刷新
    public static final int APPLY_LSIT_REFRESH = 10110;

    public static final int APPLY_REQUEST_CODE = 10111;


    //用户id
    public static final String USER_ID = "user_id";
    //用户注册方式
    public static final String REGISTER_TYPE = "register_type";
    //登录类型
    public static final String LOGIN_TYPE ="login_type";
    //注册时间
    public static final String CREATE_TIME = "create_time";
    //用户性别
    public static final String USER_GENDER = "user_gender";
    //用户大头像
    public static final String USER_BIG_HEAD = "user_big_head";
    //用户生日
    public static final String BIRTH_DAY = "birth_day";
    //用户描述
    public static final String USER_DESC = "user_desc";
    //备注
    public static final String USER_REMARK = "user_remark";
    //账户安全类型
    public static final String USER_SAFE_TYPE = "user_safe_type";
    //账号类型
    public static final String ACCOUNT_TYPE = "account_type";

    public static final String OFFLINE = "offline";
    public static final String ONLINE = "online";

    //订单选项卡
    public static final String ORDER_TAB = "tab";
    //订单id
    public static final String ORDER_ID = "order_id";

    //延迟隐藏
    public final static int LAST_GONE = 18;
    //分享的直播url
    public static final int SHARE_URL = 23;

    // 缓存标志
    public static String CACHE_FLAG = "woying_config";
    //支付成功
    public static final int PAY_SUCCESS = 888;
    //用户角色
    public static final String USER_ROLE = "user_role";

    //离线签到缓存
    public static final String SIGN_OFF_LINE = "sign_off_line";

    //扫码标示
    public static final String SCAN_QRCODE = "scanQrcode";

    //支付成功返回
    public static final String PAY_SUCCESS_BACK = "pay_success_back";
}

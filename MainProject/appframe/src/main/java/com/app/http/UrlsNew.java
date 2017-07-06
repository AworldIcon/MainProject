package com.app.http;

/**
 * Created by MaShiZhao on 2017/2/7
 */
public class UrlsNew
{
    // 内网 192.168.3.6:8090
    public static final String URLHeader0 = "http://192.168.3.8";
    // 外网测试
    public static final String URLHeader1 = "http://beta-api.gkk.cn";
    // 正式环境
    public static final String URLHeader2 = "http://hbsi.gkk.cn";
    //霍贞光端口
    public static final String URLHeader3 = "http://192.168.3.6:7405";
    //吝浩伟 端口
    public static final String URLHeader4 = "http://192.168.3.6:7408";
    //王磊
    public static final String URLHeader5 = "http://192.168.3.189:8080";
    public static final String URLHeader6 = "http://beta.gxy.com";
    public static final String URLHeader7 = "http://beta.gkk.cn";
    public static final String URLHeader8 = "http://wyvr.gkk.cn";

    public static String URLHeader = URLHeader0;


    /**
     * 用户模块
     */
    //登录用户
    public static final String LOGIN                   =  "/login";
    //获取我的基础信息
    public static final String USER_PROFILE            =  "/user/profile";

    //首页底部导航栏新接口
    public static final String BOTTOM_BAR              = "/app_tabbar";
    public static final String GLOBAL_CONF_ACTION      = "/app_home";
    public static final String POST_BAIDU              = "/user_device";


    /**
     * 系统模块
     */
    //获取服务器当前时间
    public static final String SYSTEM_TIME             =  "/system/time";

    /**
     * course : 课程模块
     */

    //首页更多接口
    public static final String APP_MORE                =   "/app_more";

    //获取本地课程
    public static final String GET_COURSE              =   "/course";
//    public static final String GET_COURSE              =   "/course";
    //获取课程详情
    public  static final String GET_COURSE_SYNOPSIS    =   "/course";
    //获取课程下所有班级
    public  static final String GET_COURSE_CLASS       =   "/course/class";
    //获取课时列表
    public  static final String GET_COURSE_LESSON      =   "/course/lesson";
    //获取我学的课程
    public  static final String GET_COURSE_LEARNING    =   "/course/learning";

    //获取课程下的教师
    public  static final String COURSE_TEADHCER        =   "/course/class/teacher";
    //获取用户列表
    public static final String GET_USER_LIST           =   "/user";
    //发送验证码|校验验证码
    public static final String POST_SEND_CAPTCHA       =   "/captcha";
    //重置密码或找回密码
    public static final String POST_RESET_PASSWORD     =   "/reset_password";

    //用户注册
    public static final String POST_USER_REGISTER      =   "/user";
    //更新我的基础信息
    public static final String POST_CHANGE_USERINFO    =   "/user/profile";


    /**
     * 老师端作业模块
     * */
    //已发放作业接口以及获取大题型顺序或者班级发放列表的接口
    public static final String TEST_PAPER_PUBLISH      =   "/test_paper";
    //获取各个小题的题干
    public static final String TEST_PAPER_QUESTION     =   "/test_paper/question";
    public static final String TEST_PUBLISH            =   "/test";
    public static final String TEST_RESULT             =   "/test/result";
    public static final String TEST_CHECK              =   "/test/check/";
    public static final String TEST_START              =   "/test/start/";
    public static final String TEST_FINISH             =   "/test/finish/";
    public static final String TEST_DETAIL_BATCH       =   "/test/result/detail/batch";
    // 退出登录
    public static final String GET_LOG_OUT             =   "/logout";
    //更新我的头像
    public static final String POST_USER_AVATAR        =   "/user/avatar";
    //获取我的模块数据
    public static final String GET_APP_ME              =   "/app_me";
    //发现页面模块数据
    public static final String  APP_DISCOVER           =   "/app_discover";
    //院系
    public static final String GET_CATEGORY            =   "/category";
    //重置手机号码
    public static final String POST_RESET_PHONE        =   "/reset_mobile";
    //重置邮箱
    public static final String POST_RESET_EMAIL        =   "/reset_email";
    //更新我的密码
    public static final String POST_UPDATE_PASSWORD    =   "/update_password";
    //问答列表
    public static final String GET_QUESTION_QUESTION   =   "/question/question";
     //获取课程列表
    public static final String GET_COURSE_LIST         =    "/course";
     //获取课程分类列表
    public static final String GET_COURSE_CATEGORY     =    "/course/category";
    //获取课程评价列表
    public static final String GET_COURSE_REVIEW         =    "/course/review";
    //提交课程评价
    public static final String POST_COURSE_REVIEW        =    "/course/review";

    //获取我的im信息
    public static final String USER_IM_PROFILE           =      "/chat_room/init";
    // 直播聊天室心跳接口
    public static final String CHAT_ROOM_LIVE_MEMBER     =      "/chat_room/live/member";
    //获取加入过的聊天室列表 聊天室信息
    public static final String CHAT_ROOM                 =      "/chat_room";
    //通过id获取 用户详情
    public static final String CHAT_USER_DETAIL          =      "/user_qcloud";
    //管理的聊天室 课程下的
    public static final String MY_CHAT_ROOM_MANAGE       =      "/chat_room/manage";
    //获取授课班成员列表
    public static final String GET_COURSE_CLASS_MEMBER   =      "/course/class/member";
    //获取课程里面老师角色 管理班级数据
    public static final String GET_COURSE_ROLE           =      "/course/join";
    //院系上部搜索（课程，直播）
    public static final String GET_SEARCH_ALL_LIST       =       "/course";
    //获取用户地址列表
    public static final String GET_USER_ADDRESS_LIST     =      "/user/address";
    //提交保存用户地址
    public static final String POST_SAVE_ADDRESS         =      "/user/address";
    //删除用户地址
    public static final String DELETE_ADDRESS            =      "/user/address";
    //更新地址信息
    public static final String PUT_UPDATE_ADDRESS        =      "/user/address";
    //设置默认地址
    public static final String PUT_DEFAULT_ADDRESS       =      "/user/address";
    //获取单条课程
    public static final String GET_SINGLE_COURSE         =      "/course";
    //直播列表
    public static final String GET_LIVE                  =      "/live";
    //直播回放列表
    public static final String GET_LIVE_VIDEO            =      "/live/video";
    //创建订单
    public static final String POST_CREATE_ORDER         =      "/order";
    //获取订单列表
    public static final String GET_ORDER_LIST            =      "/order";
    //获取订单支付方式
    public static final String GET_ORDER_PAYMENT         =      "/order/app_payment";
    //获取订单签名信息
    public static final String POST_ORDER_SIGN           =      "/order/signpayment";
    //取消订单
    public static final String PUT_CANCLE_ORDER          =      "/order";
    //观看完成上报
    public static final String POST_LESSON_FINISH         =      "/lesson/finish";
    //直播心跳
    public static final String POST_LIVE_REPORT           =      "/live/report";
    //创建授课班学员
    public static final String POST_COURSE_CLASS_MEMBER   =      "/course/class/member";
    //获取在教课程
    public static final String GET_COURSE_TEACHERING      =      "/course/teaching";
    //获取在学课程
    public static final String GET_COURSE_LEANING         =      "/course/learning";
    //获取课程下的直播
    public static final String GET_COURSE_lIVE            =      "/live/course";
    //上传文件接口
    public static final String POST_SYSTEM_FILE           =      "/System/file";
    //分享
    public static final String GET_SHARE_URL              =      "/share";
    //新版本接口
    public static final String NEW_VERSION                =      "/system/index";
    //海报墙分类
    public static final String GET_POSTER_CATEGORY        =       "/poster/category";
    //海报列表
    public static final String GET_POSTER                 =       "/poster";
    //海报点赞
    public static final String POST_POSETER_POSTERLIKE    =       "/poster/posterlike";
    //取消点赞
    public static final String DELETE_POSTERLIKE_BATCH    =       "/poster/posterlike/batch";
    //签到 当前是否从中签到 心跳
    public static final String SIGN                       =     "/sign";
    //tag标签
    public static final String SIGN_RECORD                =     "/sign_record";
    //签到列表 老师端
    public static final String SIGN_INFO                  =     "/sign_info";
    //老师相关签到
    public static final String SIGN_TEACHER               =     "/sign_teacher";
    //回复海报列表
    public static final String GET_POSTER_COMMENT         =     "/poster/comment";
    //学生相关签到
    public static final String SIGN_STUDENT               =     "/sign_student";
    //喜欢海报的人
    public static final String GET_POSTER_GETUSER         =     "/poster/getUser";
    //获取班级列表
    public static final String GET_CLASS_LIST             =     "/group";
    //我加入的班级列表
    public static final String GET_GROUP_MY               =     "/group/my";
    //获取班级成员
    public static final String GET_CLASS_MEMBER           = "/group/member";
    //获取单条班级详情
    public static final String GET_CLASS_DETAIL           = "/group";
    //解散班级
    public static final String DELETE_DISSOLVE_CLASS      = "/group";
    //申请加入班级用户列表
    public static final String GET_GROUP_MEMBER_APPLY     = "/group/member/apply";

    //服务
    public static final String SERVICE                    =     "/service";
    //服务老师
    public static final String SERVICE_TEACHER            =     "/service/class/teacher";
    //服务加入信息
    public static final String SERVICE_MEMBERED           =     "/service/membered";
    //服务
    public static final String SERVICE_MY_MEMBER          =     "/service/my/member";
    public static final String SERVICE_MEMBER             =     "/service/member";
    //服务登记表
    public static final String SERVICE_JOB_REGISTER       =     "/service/job/register";
    //服务关联课程信息
    public static final String SERVICE_COURSE             =     "/service/course/rel/details";
    //服务资料
    public static final String SERVICE_FILES              =     "/service/files";
    //服务考核
    public static final String SERVICE_MY_TEST            =     "/service/my_test";
    //服务考试记录
    public static final String SERVICE_EXAM               =     "/service/exam";
    //服务结业
    public static final String SERVICE_GRADUATE           =     "/service/graduate";
    public static final String SERVICE_GRADUATE_ITEM      =     "/service/graduate/item";
    //服务考核评语
    public static final String SERVICE_RECORD_ITEM        =     "/service/record/item";
    //服务面试评语
    public static final String SERVICE_INTERVIEW_ITEM     =     "/service/interview/item/";
    //服务视频
    public static final String SERVICE_LESSON             =     "/service/lesson";
    //service作业相关接口
    public static final String TEST_PAPER_PUBLISH_SERVICE =     "/service/test_paper";
    public static final String TEST_CHECK_SERVICE         =     "/service/test/check";
    public static final String TEST_START_SERVICE         =     "/service/test/start";
    public static final String TEST_FINISH_SERVICE        =     "/service/test/finish";
    public static final String TEST_DETAIL_BATCH_SERVICE  =     "/service/test/result/detail/batch";
    public static final String TEST_PAPER_QUESTION_SERVICE=     "/service/test_paper/question";
    public static final String TEST_RESULT_SERVICE        =     "/service/test/result";

    //获取单条班级话题详情
    public static final String GET_SINGLE_TOPIC_DETAIL    =     "/group/topic";
    // 点赞
    public static final String POST_TOPIC_LIKE            =     "/group/topic/like";
    //取消点赞
    public static final String DELETE_TOPIC_LIKE          =     "/group/topic/like";
    //收藏
    public static final String POST_TOPIC_COLLECT         =     "/group/topic/collect";
    //取消收藏
    public static final String DELETE_TOPIC_COLLECT       =     "/group/topic/collect";
    //话题回复列表
    public static final String GET_GROUP_TOPIC_REPLY      =     "/group/topic/reply";
    //删除话题回复
    public static final String DELETE_TOPIC_REPLY         =     "/group/topic/reply";
    //删除班级话题
    public static final String DELETE_CLASS_TOPIC         =     "/group/topic";
    //话题详情的回复
    public static final String POST_TOPIC_DETAIL_REPLY    =     "/group/topic/reply";
    //获取班级成员列表
    public static final String GET_CLASS_MEMBER_LIST      =     "/group/member";
     //修改班级话题
    public static final String PUT_CALSS_TOPIC            =     "/group/topic";
     //添加班级成员
    public static final String POST_ADD_CLASS_MEMBER      = "/group/member";
     //组织机构
    public static final String GET_ORGANICATION           = "/organization";
     //创建班级
    public static final String POST_CREATE_CLASS          = "/group";
     //更新班级
    public static final String PUT_UPDATE_CLASS           = "/group";
    //班级话题列表
    public static final String GET_GROUP_TOPIC            =     "/group/topic";
    //我收藏的班级话题列表
    public static final String GET_GROUP_TOPIC_COLLECT    =     "/group/topic/collect";
    //创建话题
    public static final String POST_CREATE_TOPIC          =     "/group/topic";

     //退出班级
    public static final String DELETE_EXIT_CLASS          = "/group/my";

    //通知相关接口
    public static final String GET_NOTIFY_CLASS           ="/notify_course_class";
    //通知详情接口
    public static final String GET_NOTIFY_DEATIL          ="/notify";
    //发布通知接口
    public static final String POST_NOTIFY                ="/notify";
    //通知人数接口
    public static final String GET_NOTIFY_READ            ="/notify_read";
    //更新通知已读
    public static final String PUT_NOTIFY_CONTENT         ="/notify_content";
    public static final String GET_NOTIFY_COUNT           ="//notify_count";

    //第三方登录配置
    public static final String GET_USER_SETTING            ="/user/setting";
    //查询第三方是否可以直接登录
    public static final String POST_SOCIAL_ACCOUNT            ="/social_account/login";
    //未登录状态下的绑定
    public static final String POST_SOCIAL_ACCOUNT_BIND     ="/social_account/bind";
    //和手机号一起注册第三方登录信息
    public static final String POST_SOCIAL_ACCOUNT_REGISTER     ="/social_account/register";




}

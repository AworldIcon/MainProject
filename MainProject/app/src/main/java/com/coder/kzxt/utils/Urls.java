package com.coder.kzxt.utils;

/**
 * Created by MaShiZhao on 2017/2/7
 */
public class Urls
{
    // 内网 192.168.3.6:8090
    public static final String URLHeader0 = "http://192.168.3.6:88";
    // 外网测试
    public static final String URLHeader1 = "http://test.gkk.cn";
    // 正式环境
    public static final String URLHeader2 = "http://hbsi.gkk.cn";
    //霍贞光端口
    public static final String URLHeader3 = "http://192.168.3.6:7405";
    //吝浩伟 端口
    public static final String URLHeader4 = "http://192.168.3.6:7408";

    public static String URLHeader = URLHeader0;

//    public static final String GET_COURSE_LIST_ACTION = URLHeader + "/Mobile/Index/getCourseTestListAction;courseId;classId;type;publicCourse";

    //院系接口-获取全部课程
    public static final String GET_ALL_COURSE_ACTION = URLHeader + "/Mobile/Index/getDirectionAction?&mid=0&oauth_token=&oauth_token_secret=&deviceId=861735031544595";

    public static final String GET_DISCOVERY_ACTION = URLHeader + "/Mobile/Index/getDiscoverModuleAction";

    // 海报相关接口
    public static final String POSETER_LIST = URLHeader + "/Mobile/Index/getPosterListAction;page;preNum;cid";
    public static final String POSETER_LIST_SEARCH = URLHeader + "/Mobile/Index/getPosterListAction;page;preNum;keyword";
    public static final String POSETER_PARTICULRR = URLHeader + "/Mobile/Index/getPosterAction;id";
    public static final String POSETER_COMMENT = URLHeader + "/Mobile/Index/getCommentAction;cmtIdStr;cmtType;page;pageNum";
    public static final String SUBMIT_POSETER_COMMENT = URLHeader + "/Mobile/Index/addCommentAction;cmtIdStr;cmtType;comment";
    public static final String DELETERL_POSETER = URLHeader + "/Mobile/Index/deletePosterAction;id";
    public static final String REPORT_POSETER = URLHeader + "/Mobile/Index/deletePosterAction;id;type";
    public static final String LIKE_PERSION = URLHeader + "/Mobile/Index/getUsersOfLikePosterAction;id;page;pageNum";
    public static final String PRAISE_PERSION = URLHeader + "/Mobile/Index/getUsersOfLikePosterAction;id;appraiseid;page;pageNum;type";
    public static final String PRAISE = URLHeader + "/Mobile/Index/appraiseAction;type;appraiseid";
    public static final String COMMNETREPLY = URLHeader + "/Mobile/Index/getReplyByCmtIdAction;cmtId;cmtStatId;cmtType;page;pageNum";
    //
    public static final String LIKE_POSTER_ACTION = URLHeader + "/Mobile/Index/LikePosterAction;id;status";
    public static final String COMMIT_COMMENT = URLHeader + "/Mobile/Index/replyCommentAction;cmtIdStr;cmtType;reply;cmtStatId;cmtId;replyId";
    //获取海报模板
    public static final String CREATE_POSTE = URLHeader + "/Mobile/Index/createPosterAction";
    //创建海报
    public static final String STORE_POSTER = URLHeader + "/Mobile/Index/storePosterAction;type;cid;desc;bgColorId";
    //我的海报
    public static final String MY_POSETER_LIST = URLHeader + "/Mobile/Index/getMyPosterListAction;page;preNum";
    //我喜欢的海报
    public static final String MY_LIKE_POSETER_LIST = URLHeader + "/Mobile/Index/getMyLikePosterListAction;page;preNum";

    //播放页相关接口
    public static final String VIDEO_CATALIST = URLHeader + "/Mobile/Index/getTreeVideoAction;treeid;type;publicCourse;classid";

    //开屏广告
    public static final String SPREAD = URLHeader + "/Mobile/Index/getSpreadAction";
    //插屏广告
    public static final String CUT_SPREAD = URLHeader + "/Mobile/Index/getCutAction";
    //检查版本
    public static String NEW_VERSION = URLHeader + "/Mobile/Index/getVersionInfoAction;type";

    public static final String REPORT_LIKE = URLHeader + "/Mobile/Index/LikePosterAction;id;status";

    //登录接口
    public static final String POST_LOGIN_ACTION = URLHeader + "/Mobile/Index/loginAction;account;password;isCloudLogin;deviceId";
    //注册校验账号
    public static final String POST_REGISTER_CHECK_ACTION = URLHeader + "/Mobile/Index/checkAccountAction;account;type;isCheckExist;iden;deviceId";
    //获取手机验证码
    public static final String POST_PHONE_CODE_ACTION = URLHeader + "/Mobile/Index/getMobileCodeAction;mobile;type;deviceId";
    //获取邮箱验证码
    public static final String POST_EMAIL_CODE_ACTION = URLHeader + "/Mobile/Index/getEmailCodeAction;email;type;deviceId";
    //校验验证码
    public static final String POST_CHECK_CODE_ACTION = URLHeader + "/Mobile/Index/checkCodeAction;account;type;code;ignoreCheckAccount;deviceId";
    //设置密码
    public static final String POST_CHANGE_PASSWORD_ACTION = URLHeader + "/Mobile/Index/resetBySmsAction;account;sms_code;password;confirmPassword;type;from;deviceId";
    //注册接口
    public static final String POST_REGISTER_ACTION = URLHeader + "/Mobile/Index/regAction;account;password;type;code;ignoreCode;from;appType;deviceId";
    //修改绑定账号
    public static final String POST_CHANGE_BIND_ACCOUNT_ACTION = URLHeader + "/Mobile/Index/modifyAccountAction;mid;account;type;code;oauth_token;oauth_token_secret;codeFrom;deviceId";
    //解除绑定的三方账号信息
    public static final String POST_UNBIND_OTHER_ACCOUNT_ACTION = URLHeader + "/Mobile/Index/UserInfoUnBindAction;uid;type;token;oauth_token;oauth_token_secret;deviceId";
    //三方登陆绑定本地手机或邮箱
    public static final String POST_BIND_OTHER_ACCOUNT_ACTION = URLHeader + "/Mobile/Index/UserInfoBindAction;uid;type;token;thirdName;deviceId";
    //获取可关联的公开课联盟帐号
    public static final String GET_RELCENTER_USER_ACTION = URLHeader + "/Mobile/Index/getRelCenterUserAction;mid;account;oauth_token;oauth_token_secret;deviceId";
    //绑定账号
    public static final String POST_BIND_ACCOUNT_ACTION = URLHeader + "/Mobile/Index/bindUserAction;account;localUid;centerUid;deviceId";
    //创建并绑定账号
    public static final String POST_CREATE_BIND_ACCOUNT_ACTION = URLHeader + "/Mobile/Index/addBindUserAction;account;uid;type;deviceId";
    //解绑账号
    public static final String POST_UNBIND_ACCOUNT_ACTION = URLHeader + "/Mobile/Index/unbindUserAction;localUid;centerUid;code;deviceId";
    //检测第三方是否绑定
    public static final String GET_CHECK_BIND_THIRD_ACTION = URLHeader + "/Mobile/Index/checkBindThirdAction;type;token;deviceId";
    //获取账号信息
    public static final String GET_ACCOUNT_INFO_ACTION = URLHeader + "/Mobile/Index/getUserInfoAction;mid;oauth_token;oauth_token_secret;deviceId";
    //保存提交用户信息
    public static final String POST_SAVE_USER_INFO_ACTION = URLHeader + "/Mobile/Index/setUserInfoAction;mid;sex;nickname;signature;oauth_token;oauth_token_secret;deviceId";

    //首页模块化接口
    public static final String GLOBAL_CONF_ACTION = URLHeader + "/Mobile/Chip/globalConfAction;version";
    //banner图接口
    public static final String BABBER_URL = URLHeader + "/Mobile/Chip/sliderImageAction;imgWidth";
    //名师
    public static final String FAMOUS_TEACHER_URL = URLHeader + "/Mobile/Chip/recommendTeacherListAction;preNum";
    //直播课程
    public static final String LIVE_LESSON_LIST = URLHeader + "/Mobile/Chip/liveCourseListAction;preNum;isMerge";
    //本校课程
    public static final String LOCAL_COURSE = URLHeader + "/Mobile/Chip/localCourseListAction;preNum";
    //最新资讯
    public static final String NEWS_LIST_ACTION = URLHeader + "/Mobile/Chip/newsListAction;preNum";
    //更多资讯
    public static final String NEWS_LIST_ACTION_MORE = URLHeader + "/Mobile/Chip/newsListAction;preNum;page";
    //推荐课程
    public static final String QUALITY_COURSE_LIST = URLHeader + "/Mobile/Chip/qualityCourseListAction;preNum";
    //公共课联盟
    public static final String PUBLIC_COURSE_LIST = URLHeader + "/Mobile/Chip/publicCourseListAction;preNum";
    //最新课程
    public static final String LATE_COURSE_LIST = URLHeader + "/Mobile/Chip/latestCourseListAction;preNum";
    //免费好课
    public static final String FREE_COURSE_LIST = URLHeader + "/Mobile/Chip/freeCourseListAction;preNum";
    //畅销好课
    public static final String POPULAR_COURSE_LIST = URLHeader + "/Mobile/Chip/popularCourseListAction;preNum";
    //精品推荐
    public static final String RECOMMEND_CATEGORY_LIST = URLHeader + "/Mobile/Chip/recommendCategoryListAction;preNum";
    //学生功能区
    public static final String FEATURE_LIST = URLHeader + "/Mobile/Chip/featureListAction;preNum";
    //我的课程
    public static final String MY_COURSE_LIST = URLHeader + "/Mobile/Chip/myLearnCourseListAction;preNum";
    //我的授课
    public static final String MY_TEA_COURSE = URLHeader + "/Mobile/Chip/myTeachCourseListAction;preNum";
    //发现页精品课程接口
    public static final String GET_RECOMM_COURSE = URLHeader + "/Mobile/index/getRecommendCourseAction";


    //老师创建作业接口
    //随机选题提交
    public static final String RANDOM_SELQUESTION = URLHeader + "/Mobile/Index/randomSelQuestionAction;courseId;testId;single_choice;choice;determine;fill;essay;publicCourse";
    //获取作业或者考试列表
    public static final String GET_COURSE_TEST_LIST = URLHeader + "/Mobile/Index/getCourseTestListAction;courseId;classId;type;publicCourse";
    //创建作业或考试接口
    public static final String CREAT_TEST_PAPER = URLHeader + "/Mobile/Index/createTestPaperAction;courseId;classId;type;name;publicCourse";
    //获取题库各类型试题数量
    public static final String GET_QUESTION_NUM = URLHeader + "/Mobile/Index/getQuestionBankNumAction;courseId;testId;publicCourse";
    //预览已发布试卷
    public static final String TO_VIEW_TEST = URLHeader + "/Mobile/Index/toViewTestPaperAction;resultId;publicCourse";
    //批阅试卷
    public static final String MARKING_ACTION = URLHeader + "/Mobile/Index/markingAction;data;publicCourse";
    //查看未发布试卷
    public static final String PREVIEW_TEST_ACTION = URLHeader + "/Mobile/Index/previewTestAction;testId;publicCourse";
    //发布作业接口
    public static final String PUBLISH_TEST_ACTION = URLHeader + "/Mobile/Index/publishTestAction;testId;id;startTime;endTime;title;publicCourse";
    //删除作业试题接口
    public static final String DELE_TEST_PAPER_ITEM = URLHeader + "/Mobile/Index/delTestPaperItemAction;testId;id;publicCourse";
    //删除作业接口
    public static final String DELE_TEST_PAPER = URLHeader + "/Mobile/Index/delectTestPaperAction;testId;publicCourse";
    //获取学生答题结果接口
    public static final String GET_TEST_RESULT = URLHeader + "/Mobile/Index/getTestResultsAction;testId;publicCourse";
    //题库接口
    public static final String GET_QUESTION_BANK = URLHeader + "/Mobile/Index/getQuestionsListAction;testId;courseId;type;difficulty;publicCourse";
    //题库选题添加接口
    public static final String ADD_TEST_PAPER = URLHeader + "/Mobile/Index/addTestPaperAction;testId;questionId;publicCourse";
    //创建选择试题
    public static final String ADD_QUESTION_ITEM = URLHeader + "/Mobile/Index/ManuallyAddQuestionAction;answer;stem;type;choices;difficulty;score;target;itemId;testId;publicCourse";


    //学生作业接口
    public static final String GET_TEST_PAPER = URLHeader + "/Mobile/Index/getCourseTestpaperAction;courseId;publicCourse";
    //试卷详情首页
    public static final String GET_TEST_PAPER_INFO = URLHeader + "/Mobile/Index/getTestpaperInfoAction;testId;publicCourse";
    //答题页
    public static final String GET_TEST_PAPER_QUES = URLHeader + "/Mobile/Index/getTestpaperQesAction;resultId;publicCourse";
    //试卷预览接口
    public static final String GET_TEST_PAPER_PREVIEW = URLHeader + "/Mobile/Index/previewTestAction;resultId;publicCourse";
    //提交单个答案
    public static final String TEST_SUSPEND = URLHeader + "/Mobile/Index/testSuspendAction;resultId;data;publicCourse";
    //提交全部试题答案
    public static final String FINISH_TEST = URLHeader + "/Mobile/Index/finishTestAction;resultId;data;publicCourse";
    //收藏接口
    public static final String FAVOURITE_QUESTION = URLHeader + "/Mobile/Index/favoriteQuestionAction;testId;id;publicCourse";
    //取消收藏接口
    public static final String UN_FAVOURITE_QUESTION = URLHeader + "/Mobile/Index/unFavoriteQuestionAction;testId;id;publicCourse";
    //考试结果接口
    public static final String TEST_RESULT = URLHeader + "/Mobile/Index/testResultAction;resultId;publicCourse";
    //个人中心我的作业和考试接口
    public static final String GET_MY_TEST_PAPER = URLHeader + "/Mobile/Index/getMyTestpaterAction;type;publicCourse";

    //获取通知设置
    public static final String NOTICE_SETTING = URLHeader + "/Mobile/Index/getNotificationSetAction;mid;oauth_token;oauth_token_secret;platform";
    //提交通知设置
    public static final String FINISI_NOTICE_SETTING = URLHeader + "/Mobile/Index/setNotificationAction;platform;setting";

    //获取我的收获地址
    public static final String GET_MY_ADDRESS_ACTION = URLHeader + "/Mobile/Index/getMyAddressAction;mid;oauth_token;oauth_token_secret;deviceId;publicCourse";
    //提交保存地址
    public static final String POST_SUBMIT_SAVE_ADDRESS = URLHeader + "/Mobile/Index/setNewAddressAction;mid;oauth_token;oauth_token_secret;deviceId;type;id;receiver;mobile;province;city;district;addressDetail;publicCourse";
    //设置默认地址和删除地址
    public static final String POST_SET_DEFAULT_AND_DELADDRESS_ACTION = URLHeader + "/Mobile/Index/setDefaultAndDelAddressAction;mid;oauth_token;oauth_token_secret;deviceId;type;id;publicCourse";
    //获取课程列表数据
    public static final String GET_COURSE_LIST_ACTION = URLHeader + "/Mobile/Index/getCourseListAction;major_id;deviceId";
    //获取周精选课程
    public static final String GET_WEEK_COURSE_ACTION = URLHeader + "/Mobile/Index/getWeekFineCourseAction;deviceId";
    //获取班级列表
    public static final String GET_CLASS_LIST_ACTION = URLHeader + "/Mobile/Index/getClassListAction;mid;oauth_token;oauth_token_secret;categoryId;deviceId;page;preNum";
    //搜索全部
    public static final String GET_SEARCH_ALL_ACTION = URLHeader + "/Mobile/Index/searchAllAction;mid;deviceId;keywords";
    // 获取我的问答类型
    public static final String GET_MY_QUESTION = URLHeader + "/Mobile/Index/getMyQuestionAction;type";
    // 获取我的问答列表数据
    public static final String GET_MY_QUESTION_LIST = URLHeader + "/Mobile/Index/getMyQuestionAction;type;page;preNum;publicCourse;";

    //发表话题接口
    public static final String ADD_CLASS_THREAD = URLHeader + "/Mobile/Index/addClassThreadAction;classId;title;content;publicCourse;audioDuration";
    //获取班级详情
    public static final String GET_CLASS_DETAIL_ACTION = URLHeader +"/Mobile/Index/getClassDetailAction;classId;mid;oauth_token;oauth_token_secret;deviceId";
    //退出班级
    public static final String GET_EXIT_CLASS_ACTION = URLHeader + "/Mobile/Index/exitClassAction;classId;mid;oauth_token;oauth_token_secret;deviceId";
    //话题列表接口
    public static final String GET_MY_CLASS_THREAD = URLHeader + "/Mobile/Index/getMyClassThreadAction;preNum;p";
    //针对话题的点赞或取消,type="1"
    public static final String CLASS_THREAD_APPRAISE = URLHeader + "/Mobile/Index/classThreadAppraiseAction;threadId;opType;type";
    //针对话题中评论item的点赞或取消，type="2"
    public static final String CLASS_THREAD_APPRAISE_ITEM = URLHeader + "/Mobile/Index/classThreadAppraiseAction;threadId;opType;type;cmtId";
    //班级文件
    public static final String GET_SHARED_FILE_ACTION = URLHeader + "/Mobile/Index/getSharedFileAction;classId;mid;oauth_token;oauth_token_secret;deviceId;page";
    //删除班级共享文件
    public static final String POST_DELETE_SHARED_FILE_ACTION = URLHeader + "/Mobile/Index/deleteSharedFileAction;mid;oauth_token;oauth_token_secret;deviceId;classId;fileId";
    //个人话题详情接口
    public static final String GET_CLASS_THREAD_APPRAISE = URLHeader + "/Mobile/Index/getClassThreadDetailAction;threadId;preNum;p;commentNum;isDecodeHtml;center";
    //班级话题详情
    public static final String GET_CLASS_THREAD_APPRAISE_ACTION = URLHeader + "/Mobile/Index/getClassThreadAction;classId;preNum;p;type";
    //评论话题
    public static final String POST_CLASS_THREAD = URLHeader + "/Mobile/Index/postClassThreadAction;threadId;content;publicCourse";
    //回复评论
    public static final String POST_CLASS_THREAD_ACTION = URLHeader + "/Mobile/Index/postClassThreadAction;threadId;postId;fromUserId;content";
    //更多话题回复详情
    public static final String GET_POST_CLASS_DETAIL_ACTION = URLHeader + "/Mobile/Index/getClassPostDetailAction;postId;preNum;p;commentNum;isDecodeHtml";
    //置顶以及精华操作
    public static final String TRIGGER_CLASS_THREAD = URLHeader + "/Mobile/Index/triggerClassThreadAction;threadId;opType";
    //删除话题
    public static final String CLOSE_CLASS_THREAD = URLHeader + "/Mobile/Index/closeClassThreadAction;threadId";

    //问题详情
    public static final String GET_QUESTION_DETAIL = URLHeader + "/Mobile/Index/getQuestionDetailAction;courseId;questionId;publicCourse;page;preNum";
    // 收藏 取消收藏问题
    public static final String COLLECT_QUESTION = URLHeader + "/Mobile/Index/collectQuestionAction;courseId;questionId;publicCourse;type";
    // 关闭问题
    public static final String CLOSE_QUESTION = URLHeader + "/Mobile/Index/closeQuestionAction;courseId;questionId;publicCourse";
    // 恢复问题
    public static final String REVERT_QUESTION = URLHeader + "/Mobile/Index/revertQuestionAction;courseId;questionId;publicCourse";
    // 举报
    public static final String REPORT_ACTION = URLHeader + "/Mobile/Index/reportAction;id;type";
    // 老师 学生 打分
    public static final String ADOPTIONQUESTION_STUDENT = URLHeader + "/Mobile/Index/adoptionQuestionAction;courseId;questionId;publicCourse;answerId";
    public static final String ADOPTIONQUESTION_TEACHER = URLHeader + "/Mobile/Index/adoptionQuestionAction;courseId;questionId;publicCourse;answerId;score";
    //上传证件照
    public static final String POST_JOINZ_CLASS_ACTION = URLHeader + "/Mobile/Index/joinClassAction;classId;mobile;studNum";
    //获取班级课程数据
    public static final String GET_CLASS_COURSE_ACTION = URLHeader + "/Mobile/Index/getClassCourseAction;page;pageNum;classId";
     //班级课程表
    public static final String GET_CLASS_SYLLABUS_ACTION = URLHeader + "/Mobile/Index/getClassSyllabusAction;classId";
    //获取班级相册
    public static final String GET_CLASS_PHOTO_ACTION = URLHeader + "/Mobile/Index/getClassPhotoAction;classId;page;preNum;isMyPhoto";
    //班级相册删除照片
    public static final String POST_DELETE_CLASS_PHOTO_ACTION = URLHeader + "/Mobile/Index/deleteClassPhotoAction;classId;photoId";
    //班级相册照片点赞
    public static final String POST_APPRAISE_ACTION = URLHeader + "/Mobile/Index/appraiseAction;type;appraiseid";
    //上传班级相册照片
    public static final String POST_UPLOAD_CLASS_PHOTO_ACTION = URLHeader + "/Mobile/Index/uploadClassPhotoAction;classId;title";
    //班级申请列表
    public static final String GET_CLASS_APPLY_LIST_ACTION = URLHeader + "/Mobile/Index/getClassApplyListAction;classId;status;positionId;preNum;p";
    //申请列表审核
    public static final String POST_CHECK_CLASS_APPLY_ACTION = URLHeader + "/Mobile/Index/checkClassApplyAction;id;status;reason";

//    public static final String POST_LOGIN = "http://192.168.3.8/login";
    //获取班级成员列表
    public static final String GET_CLASS_MEMBER_LIST_ACTION = URLHeader + "/Mobile/Index/getClassMemberListAction;classId";

}

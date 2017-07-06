package com.coder.kzxt.broadcast;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import com.app.http.HttpCallBack;
import com.app.http.UrlsNew;
import com.app.http.builders.HttpPostBuilder;
import com.app.utils.L;
import com.baidu.android.pushservice.PushMessageReceiver;
import com.coder.kzxt.classe.activity.ApplyListActivity;
import com.coder.kzxt.classe.activity.ClassPhotoActivity;
import com.coder.kzxt.gambit.activity.ClassGambitParticularsActivity;
import com.coder.kzxt.main.activity.MainActivity;
import com.coder.kzxt.main.beans.MyCourseBean;
import com.coder.kzxt.message.activity.NotificationDetailActivity;
import com.coder.kzxt.poster.activity.Posters_Particulars_Activity;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.coder.kzxt.webview.activity.TextView_Link_Activity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/*
 * Push消息处理receiver。请编写您需要的回调函数， 一般来说： onBind是必须的，用来处理startWork返回值；
 *onMessage用来接收透传消息； onSetTags、onDelTags、onListTags是tag相关操作的回调；
 *onNotificationClicked在通知被点击时回调； onUnbind是stopWork接口的返回值回调

 * 返回值中的errorCode，解释如下：
 *0 - Success
 *10001 - Network Problem
 *30600 - Internal Server Error
 *30601 - Method Not Allowed
 *30602 - Request Params Not Valid
 *30603 - Authentication Failed
 *30604 - Quota Use Up Payment Required
 *30605 -Data Required Not Found
 *30606 - Request Time Expires Timeout
 *30607 - Channel Token Timeout
 *30608 - Bind Relation Not Found
 *30609 - Bind Number Too Many

 * 当您遇到以上返回错误时，如果解释不了您的问题，请用同一请求的返回值requestId和errorCode联系我们追查问题。
 *
 */

public class BaiduMessageReceiver extends PushMessageReceiver {

    private SharedPreferencesUtil pu;

    /**
     * 调用PushManager.startWork后，sdk将对push
     * server发起绑定请求，这个过程是异步的。绑定请求的结果通过onBind返回。 如果您需要用单播推送，需要把这里获取的channel
     * id和user id上传到应用server中，再调用server接口用channel id和user id给单个手机或者用户推送。
     *
     * @param context   BroadcastReceiver的执行Context
     * @param errorCode 绑定接口返回值，0 - 成功
     * @param appid     应用id。errorCode非0时为null
     * @param userId    应用user id。errorCode非0时为null
     * @param channelId 应用channel id。errorCode非0时为null
     * @param requestId 向服务端发起的请求id。在追查问题时有用；
     * @return none
     */

    @Override
    public void onBind(Context context, int errorCode, String appid,
                       String userId, String channelId, String requestId) {
        String responseString = "onBind errorCode=" + errorCode + " appid="
                + appid + " userId=" + userId + " channelId=" + channelId
                + " requestId=" + requestId;
        Log.d("zw--baidu", responseString);
        pu = new SharedPreferencesUtil(context.getApplicationContext());
        String devideId=pu.getDevicedId();
        if (errorCode == 0) {
            // 绑定成功
            postBaiduData(context.getApplicationContext(),userId, channelId, appid,devideId);
        }

    }

    @Override
    public void onDelTags(Context arg0, int arg1, List<String> arg2,
                          List<String> arg3, String arg4) {

    }

    @Override
    public void onListTags(Context arg0, int arg1, List<String> arg2,
                           String arg3) {

    }

    /**
     * 接收透传消息的函数。
     * <p/>
     * //@param context
     * 上下文
     * //@param message
     * 推送的消息
     * //@param customContentString
     * 自定义内容,为空或者json字符串
     */

    @Override
    public void onMessage(Context arg0, String arg1, String arg2) {
        // TODO Auto-generated method stub

    }

    /**
     * 接收通知点击的函数。
     *
     * @param context             上下文
     * @param title               推送的通知的标题
     * @param description         推送的通知的描述
     * @param customContentString 自定义内容，为空或者json字符串
     */

    @Override
    public void onNotificationClicked(Context context, String title,
                                      String description, String customContentString) {
        String notifyString = "通知点击 title=\"" + title + "\" description=\""
                + description + "\" customContent=" + customContentString;
        L.d("zw--baidu", notifyString);

        /**
         * 跳转的都需要先跳转到首页
         */
        Intent intent3 = new Intent();
        intent3.setClass(context.getApplicationContext(), MainActivity.class);
        intent3.putExtra("from", "main");
        intent3.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Intent intent2 = new Intent();
        intent2.putExtra("from", "MainFragment");
        intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        try {
            JSONObject jsonO = new JSONObject(customContentString);
            if (jsonO.has("module")) {
                String type = jsonO.getString("module");
                String opera = jsonO.getString("operation");
                String data = jsonO.getString("data");
                JSONObject json = new JSONObject(data);
                if (!TextUtils.isEmpty(type)) {
                    switch (type) {
                        case "noticeclass":// 班级相关
                            switch (opera) {
                                case "deleteImage":// （老师，管理员，超管）删除照片
                                case "deleteFile":// （老师，管理员，超管）删除班级文件
                                case "applyStudentNum":// 申请学号
                                case "applyStudentNumOk":// 通过
                                case "applyStudentNumNo":// 不通过
                                case "beCST":// 设置为班委
                                case "revokeCST":// 撤销班委
                                case "beMaster":// 设置为班主任
                                case "revokeMaster":// 撤销班主任
                                    context.getApplicationContext().startActivity(intent3);
                                    // 跳转通知列表页
                                    //intent2.setClass(context.getApplicationContext(), Notification_Activity.class);
                                    context.getApplicationContext().startActivity(intent2);
                                    break;
                                case "appriseImage":// 点赞照片-->班级相册
                                    context.getApplicationContext().startActivity(intent3);
                                    // 跳转班级相册页
                                    intent2.setClass(context.getApplicationContext(), ClassPhotoActivity.class);
                                    intent2.putExtra("classId", json.getString("classId"));
                                    intent2.putExtra("identity", "member");
                                    context.getApplicationContext().startActivity(intent2);
                                    break;
                                case "commentTopic":// 回复话题 跳转到话题详情
                                case "stickTopic":// 话题置顶
                                case "eliteTopic":// 话题设为精华
                                case "appriseTopic":// 话题点赞
                                    context.getApplicationContext().startActivity(intent3);
                                    intent2.setClass(context.getApplicationContext(), ClassGambitParticularsActivity.class);
                                    intent2.putExtra("gambitId", json.getString("topicId"));
                                    intent2.putExtra("isCenter", String.valueOf(json.getString("publicCourse")));
                                    context.getApplicationContext().startActivity(intent2);
                                    break;
                                case "replyTopic":// 回复话题的回复     跳转到评论详情页（通知人不一样，这里发给话题作者）
                                case "replyTopicComment"://这里发给回复者
                                    context.getApplicationContext().startActivity(intent3);
                                    //intent2.setClass(context.getApplicationContext(), Gambit_Reply_Particulars_Activity.class);
                                    intent2.putExtra("reply_id", json.getString("replyId"));
                                    intent2.putExtra("gambitId", json.getString("topicId"));
                                    intent2.putExtra("topicTitle", json.getString("topicTitle"));
                                    context.getApplicationContext().startActivity(intent2);
                                    break;
                                case "addMember":// 加入班级，跳转到班级详情
                                case "applyClassOk":// 加入班级申请审核通过
                                case "applyClassNo":// 加入班级申请审核不通过
                                case "removeMember"://被踢出班级
                                case "inviteMember"://邀请加入班级
                                    context.getApplicationContext().startActivity(intent3);
                                    //intent2.setClass(context.getApplicationContext(), Class_Particulars_Activity.class);
                                    intent2.putExtra("classId", json.getString("classId"));
                                    intent2.putExtra("myClassState", String.valueOf(json.getString("publicCourse")));
                                    context.getApplicationContext().startActivity(intent2);
                                    break;
                                case "applyClass":// 申请加入班级-->申请列表
                                    context.getApplicationContext().startActivity(intent3);
                                    intent2.setClass(context.getApplicationContext(), ApplyListActivity.class);
                                    intent2.putExtra("classid", json.getString("classId"));
                                    intent2.putExtra("className", String.valueOf(json.getString("className")));
                                    context.getApplicationContext().startActivity(intent2);
                                    break;
                            }
                            break;
                        case "poster":// 海报相关
                            switch (opera) {
                                case "apprisePoster":// 点赞，评论---->海报详情
                                case "commentPoster":
                                    context.getApplicationContext().startActivity(intent3);
                                    intent2.setClass(context.getApplicationContext(), Posters_Particulars_Activity.class);
                                    intent2.putExtra("postersId", json.getString("posterId"));
                                    context.getApplicationContext().startActivity(intent2);
                                    break;
                                case "replyPoster":// 评论回复--->回复的评论详情--通知给海报作者
                                case "replyPosterComment":// 评论回复--->回复的评论详情--通知给评论作者
                                    context.getApplicationContext().startActivity(intent3);
                                    //intent2.setClass(context.getApplicationContext(), Posters_Comment_Particulars_Activity.class);
                                    intent2.putExtra("postersId", json.getString("posterId"));
                                    intent2.putExtra("comment_id", json.getString("replyId"));
                                    intent2.putExtra("cmtStatId", json.getString("cmtStatId"));
                                    context.getApplicationContext().startActivity(intent2);
                                    break;
                            }
                            break;
                        case "course":// 课程相关
                            switch (opera) {
                                case "submitHomework":
                                case "submitTest":// 完成作业 完成考试,关闭课程，私转公未通过-->通知列表
                                case "closeCourse":
                                case "privateToPublicNo":
                                case "privateToPublicOk"://私转公通过
                                    context.getApplicationContext().startActivity(intent3);
                                    //intent2.setClass(context.getApplicationContext(), Notification_Activity.class);
                                    context.getApplicationContext().startActivity(intent2);
                                    break;
                                case "liveJust2Begin"://直播课-->直播列表
                                case "liveJust3Begin"://直播通知，-->直播课程即将开始
                                case "liveJust4Begin"://直播通知，-->是你的课程直播已经开始
                                case "liveJust5Begin"://直播通知，-->是直播课程已经开始
                                    context.getApplicationContext().startActivity(intent3);
                                    //intent2.setClass(context.getApplicationContext(), LiveLesson_SeeMore_Activity.class);
                                    context.getApplicationContext().startActivity(intent2);
                                    break;
                                case "beginHomework":// 作业开始,已批阅-->作业列表
                                case "markHomework":
                                    context.getApplicationContext().startActivity(intent3);
                                    //intent2.setClass(context.getApplicationContext(), Course_Examination_Activity.class);
                                    intent2.putExtra("type", json.getString("homework"));
                                    intent2.putExtra("courseId", json.getString("courseId"));
                                    intent2.putExtra("isCenter", String.valueOf(json.getString("publicCourse")));
                                    context.getApplicationContext().startActivity(intent2);
                                    break;
                                case "beginTest":// 考试开始，已批阅-->考试列表
                                case "markTest":
                                    context.getApplicationContext().startActivity(intent3);
                                   // intent2.setClass(context.getApplicationContext(), Course_Examination_Activity.class);
                                    intent2.putExtra("type", json.getString("testpaper"));
                                    intent2.putExtra("courseId", json.getString("courseId"));
                                    intent2.putExtra("isCenter", String.valueOf(json.getString("publicCourse")));
                                    context.getApplicationContext().startActivity(intent2);
                                    break;
                                case "submitThread":// 提出问题，设为精华,采纳回答-->问题详情
                                case "eliteThread":
                                case "adoptedThread":
                                    context.getApplicationContext().startActivity(intent3);
                                   // intent2.setClass(context.getApplicationContext(), QuestionDetaiActivity.class);
                                    intent2.putExtra("courseId", json.getString("courseId"));
                                    intent2.putExtra("questionId", json.getString("threadId"));
                                    intent2.putExtra("isCenter", String.valueOf(json.getString("publicCourse")));
                                    intent2.putExtra("username", json.getString("sendUserName"));
                                    context.getApplicationContext().startActivity(intent2);
                                    break;
                                case "answerThread":// 回答问题，追问回答，回答追问-->回答详情
                                case "appendAnswer":
                                case "answerAppend":
                                    context.getApplicationContext().startActivity(intent3);
                                 //   intent2.setClass(context.getApplicationContext(), QuestionAskDetaiActivity.class);
                                    intent2.putExtra("isCenter", String.valueOf(json.getString("publicCourse")));
                                    intent2.putExtra("courseId", json.getString("courseId"));
                                    intent2.putExtra("questionId", json.getString("threadId"));
                                    intent2.putExtra("answerId", json.getString("answerId"));
                                    intent2.putExtra("username", json.getString("sendUserName"));
                                    context.getApplicationContext().startActivity(intent2);
                                    break;
                                case "publicToPrivateOk":// 公转私通过-->课程详情
                                    context.getApplicationContext().startActivity(intent3);
                                 //   intent2.setClass(context.getApplicationContext(), CourseDetailTeacherActivity.class);
                                    MyCourseBean courseBean = new MyCourseBean();
                                    courseBean.setCourseId(json.getString("courseId"));
                                    courseBean.setCourseName(json.getString("courseTitle"));
                                    courseBean.setClassNum("0");
                                    courseBean.setPublicCourse(json.getString("publicCourse"));
                                    courseBean.setPic(json.getString("image"));
                                    courseBean.setStatus("unpublished");
                                    intent2.putExtra("Bean", courseBean);
                                    context.getApplicationContext().startActivity(intent2);
                                    break;
                                case "courseJustOver":  //下课前10分钟推送
                                    context.getApplicationContext().startActivity(intent3);
                                   // intent2.setClass(context, LectureClassActivity.class);
                                    intent2.putExtra("className", json.getString("className"));
                                    intent2.putExtra("classId", json.getString("classId"));
                                    intent2.putExtra("flag", "online");
                                    intent2.putExtra("courseId", json.getString("courseId"));
                                    intent2.putExtra("courseName", json.getString("courseName"));
                                  //  intent2.putExtra(Constants.IS_CENTER, json.getString("publicCourse"));
                                    context.startActivity(intent2);
                                    break;
                                case "courseOverGrade":  //评分-上课记录
                                    context.getApplicationContext().startActivity(intent3);
                                  //  intent2.setClass(context, SignClassRecordActivity.class);
                                    intent2.putExtra("classId", json.getString("classId"));
                                    intent2.putExtra("courseId", json.getString("courseId"));
                                    intent2.putExtra("publicCourse", json.getString("publicCourse"));
                                    context.startActivity(intent2);
                                    break;
                            }
                            break;
                        case "identity":// 身份相关，全跳转到通知列表
                            switch (opera) {
                                case "edit":
                                    context.getApplicationContext().startActivity(intent3);
                                 //   intent2.setClass(context.getApplicationContext(), Notification_Activity.class);
                                    context.getApplicationContext().startActivity(intent2);
                                    break;
                            }
                            break;
                        case "sign":// (点名)签到相关
                            switch (opera) {
                                case "onLineSign":// 发起点名签到--->学院签到页面
                                    context.getApplicationContext().startActivity(intent3);
                                 //   intent2.setClass(context.getApplicationContext(), Sign_Student_Activity_New.class);
                                    intent2.putExtra("courseId", json.getString("courseId"));
                                    intent2.putExtra("classId", json.getString("courseClassId"));
                                    intent2.putExtra("number", json.getString("signId"));
                                    intent2.putExtra("status", "signIn");
                                    intent2.putExtra("isCenter", String.valueOf(json.getString("publicCourse")));
                                    context.getApplicationContext().startActivity(intent2);
                                    break;
                                case "offLineSign":// 离线签到成功，将已签到改为未签到-->签到记录
                                    context.getApplicationContext().startActivity(intent3);
                                    //intent2.setClass(context.getApplicationContext(), My_ShakeSignRecord_Activity.class);
                                    intent2.putExtra("courseId", json.getString("courseId"));
                                    intent2.putExtra("classId", json.getString("courseClassId"));
                                    intent2.putExtra("publicCourse", json.getString("publicCourse"));
                                    intent2.putExtra("number", json.getString("signId"));
                                    context.getApplicationContext().startActivity(intent2);
                                    break;
                                case "revertSignStatus":
                                    context.getApplicationContext().startActivity(intent3);
                                    //intent2.setClass(context, SignRelateOperation_Activity.class);
                                    intent2.putExtra("courseId", json.getString("courseId"));
                                    intent2.putExtra("classId", json.getString("courseClassId"));
                                    intent2.putExtra("publicCourse", json.getString("publicCourse"));
                                    intent2.putExtra("number", json.getString("signId"));
                                    context.getApplicationContext().startActivity(intent2);
                                    break;
                                case "saveSignStatus":  //打签到标签
                                    context.getApplicationContext().startActivity(intent3);
                                    //intent2.setClass(context, SignRelateOperation_Activity.class);
                                    intent2.putExtra("courseId", json.getString("courseId"));
                                    intent2.putExtra("classId", json.getString("classId"));  //以前接口是courseClassId
                                    intent2.putExtra("publicCourse", json.getString("publicCourse"));
                                    intent2.putExtra("number", json.getString("signId"));
                                    context.getApplicationContext().startActivity(intent2);
                                    break;
                            }
                            break;
                        case "custom"://通知（由管理员发送）
                            String code =""; //Constants.URLHeader2;
                            String webCode = "";
                            if (code.startsWith("http")) {
                                webCode = code.substring(7, code.indexOf(".gkk"));
                            } else if (code.startsWith("https")) {
                                webCode = code.substring(8, code.indexOf(".gkk"));
                            }
                            switch (opera) {
                                case "push":
                                    context.getApplicationContext().startActivity(intent3);
                                    Intent notifyDetailIntent=new Intent(context.getApplicationContext(), NotificationDetailActivity.class);
                                    if(!TextUtils.isEmpty(json.getString("msg_id"))){
                                        notifyDetailIntent.putExtra("notifyId",Integer.valueOf(json.getString("msg_id")));
                                        notifyDetailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        context.getApplicationContext().startActivity(notifyDetailIntent);
                                        Log.d("zw--click",json.getString("msg_id"));
                                    }
//                                    if (!json.getString("url").equals(webCode) && (json.getString("url").startsWith("http") || json.getString("url").startsWith("https"))) {
//                                        intent2.setClass(context, TextView_Link_Activity.class);
//                                        intent2.putExtra("web_url", json.getString("url"));
//                                        intent2.putExtra("title", "");
//                                        context.getApplicationContext().startActivity(intent2);
//                                    }else {
//                                        if(TextUtils.isEmpty(json.getString("msg_id"))){
//                                            notifyDetailIntent.putExtra("notifyId",Integer.valueOf(json.getString("msg_id")));
//                                            context.getApplicationContext().startActivity(notifyDetailIntent);
//                                        }
//                                    }
                                    break;
                            }
                            break;
                    }
                } else {
                    context.getApplicationContext().startActivity(intent3);
                    // 跳转通知页
                    //intent2.setClass(context.getApplicationContext(), Notification_Activity.class);
                    intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.getApplicationContext().startActivity(intent2);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSetTags(Context arg0, int arg1, List<String> arg2,
                          List<String> arg3, String arg4) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onUnbind(Context context, int errorCode, String requestId) {
        String responseString = "onUnbind errorCode=" + errorCode
                + " requestId = " + requestId;
        L.d("zw--baidu", responseString);
        if (errorCode == 0) {
            // 解绑定成功
            L.d("zw--baidu", "解绑成功");
        }
    }

    /**
     * 上报百度数据给自己服务器
     */
    private void postBaiduData(Context context,String userId,String  channelId, String appid,String devideId){
        new HttpPostBuilder(context).setUrl(UrlsNew.POST_BAIDU).setClassObj(null).setHttpResult(new HttpCallBack() {
            @Override
            public void setOnSuccessCallback(int requestCode, Object resultBean) {
                L.d("ZW--baidu", "上报百度数据成功");
            }

            @Override
            public void setOnErrorCallback(int requestCode, int code, String msg) {
                L.d("ZW--baidu", "上报百度数据失败");
            }
        })
                .addBodyParam("channel_id",channelId)
                .addBodyParam("device_id",devideId)
                .addBodyParam("app_id",appid)
                .addBodyParam("back_id",userId)
                .build();
    }

    public void onNotificationArrived(Context context, String title, String description, String customContentString) {
    }
}

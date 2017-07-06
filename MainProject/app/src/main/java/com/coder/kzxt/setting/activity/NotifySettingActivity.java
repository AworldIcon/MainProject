package com.coder.kzxt.setting.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Switch;

import com.app.http.HttpGetOld;
import com.app.http.HttpPostOld;
import com.app.http.InterfaceHttpResult;
import com.app.utils.L;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.setting.beans.NotifySettingBean;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.coder.kzxt.utils.Urls;

import java.util.HashMap;
import java.util.Map;

/**
 * 通知设置
 * Created by Administrator on 2017/3/8.
 */

public class NotifySettingActivity extends BaseActivity implements InterfaceHttpResult {
    private SharedPreferencesUtil spu;
    private RelativeLayout notify_setting_ll;
    private ScrollView notify_setting_scrollview;
    private Switch course_notify_switch;
    private Switch class_notify_switch;
    private Switch poster_notify_switch;
    private Switch sign_notify_switch;
    private Switch conversation_notify_switch;
    private Switch night_notify_switch;
    private LinearLayout jiazai_layout;
    private NotifySettingBean notifySettingBean;
    private String courseChecked = "1";
    private String classChecked = "1";
    private String posterChecked = "0";
    private String signChecked = "1";
    private String chatChecked = "1";
    private String nightChecked = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify_setting);
        initView();
        check();
        initListener();
    }

    private void initView() {
        spu = new SharedPreferencesUtil(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.notification_setting);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        notify_setting_scrollview = (ScrollView) findViewById(R.id.notify_setting_scrollview);
        notify_setting_ll = (RelativeLayout) findViewById(R.id.notify_setting_ll);
        course_notify_switch = (Switch) findViewById(R.id.course_notify_switch);
        class_notify_switch = (Switch) findViewById(R.id.class_notify_switch);
        poster_notify_switch = (Switch) findViewById(R.id.poster_notify_switch);
        sign_notify_switch = (Switch) findViewById(R.id.sign_notify_switch);
        conversation_notify_switch = (Switch) findViewById(R.id.conversation_notify_switch);
        night_notify_switch = (Switch) findViewById(R.id.night_notify_switch);
        jiazai_layout = (LinearLayout) findViewById(R.id.jiazai_layout);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home://提交设置信息
                commit();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void check() {
        jiazai_layout.setVisibility(View.VISIBLE);
        notify_setting_scrollview.setVisibility(View.GONE);
        new HttpGetOld(this, this, this, NotifySettingBean.class, Urls.NOTICE_SETTING, spu.getUid(), spu.getIsLogin(), spu.getTokenSecret(), "android").excute(1000);
    }

    @Override
    public void getCallback(int requestCode, int code, String msg, Object baseBean) {
        if(requestCode == 1000) {
            jiazai_layout.setVisibility(View.GONE);
            notify_setting_scrollview.setVisibility(View.VISIBLE);
            if (code == Constants.HTTP_CODE_SUCCESS) {
                notifySettingBean = (NotifySettingBean) baseBean;
                setNotify();
            } else if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
                //重新登录
                NetworkUtil.httpRestartLogin(NotifySettingActivity.this, notify_setting_ll);
            }else{
                NetworkUtil.httpNetErrTip(NotifySettingActivity.this,notify_setting_ll);
                //请求网络失败则展示上次的数据（SP中存储的）
                getNotify();
            }
        }
    }

    private void initListener() {
        // 课程相关消息推送设置
        course_notify_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if (isChecked) {
                    course_notify_switch.setChecked(true);
                    courseChecked = "1";
                } else {
                    course_notify_switch.setChecked(false);
                    courseChecked = "0";
                }
            }
        });

        // 班级相关消息推送设置
        class_notify_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if (isChecked) {
                    class_notify_switch.setChecked(true);
                    classChecked = "1";
                } else {
                    class_notify_switch.setChecked(false);
                    classChecked = "0";
                }
            }
        });
        // 海报相关详细推送设置
        poster_notify_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if (isChecked) {
                    poster_notify_switch.setChecked(true);
                    posterChecked = "1";
                } else {
                    poster_notify_switch.setChecked(false);
                    posterChecked = "0";
                }
            }
        });
        // 签到推送设置
        sign_notify_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                        if (isChecked) {
                            sign_notify_switch.setChecked(true);
                            signChecked = "1";
                        } else {
                            sign_notify_switch.setChecked(false);
                            signChecked = "0";
                        }
                    }
                });
        // 会话推送设置
        conversation_notify_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                        if (isChecked) {
                            conversation_notify_switch.setChecked(true);
                            chatChecked = "1";
                        } else {
                            conversation_notify_switch.setChecked(false);
                            chatChecked = "0";
                        }
                    }
                });
        // 夜间免打扰设置
        night_notify_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if (isChecked) {
                    night_notify_switch.setChecked(true);
                    nightChecked = "1";
                } else {
                    night_notify_switch.setChecked(false);
                    nightChecked = "0";
                }
            }
        });
    }

    private void setNotify() {
        NotifySettingBean.Data data = notifySettingBean.getData();
        spu.setCourseNotify(data.getCourse());
        spu.setClassNotify(data.getNoticeclass());
        spu.setPosterNotify(data.getPoster());
        spu.setSignNotify(data.getSign());
        spu.setConversationNotify(data.getChat());
        spu.setNightNotify(data.getNight());
        if (data.getCourse() != null && !data.getCourse().equals("") && data.getCourse().equals("1")) {
            course_notify_switch.setChecked(true);
        } else {
            course_notify_switch.setChecked(false);
        }
        if (data.getNoticeclass() != null && !data.getNoticeclass().equals("") && data.getNoticeclass().equals("1")) {
            class_notify_switch.setChecked(true);
        } else {
            class_notify_switch.setChecked(false);
        }
        if(data.getPoster() != null && !data.getPoster().equals("") && data.getPoster().equals("1")){
            poster_notify_switch.setChecked(true);
        }else{
            poster_notify_switch.setChecked(false);
        }
        if(data.getSign() != null && !data.getSign().equals("") && data.getSign().equals("1")){
            sign_notify_switch.setChecked(true);
        }else{
            sign_notify_switch.setChecked(false);
        }
        if(data.getChat() != null && !data.getChat().equals("") && data.getChat().equals("1")){
            conversation_notify_switch.setChecked(true);
        }else{
            conversation_notify_switch.setChecked(false);
        }
        if(data.getNight() != null && !data.getNight().equals("") && data.getNight().equals("1")){
            night_notify_switch.setChecked(true);
        }else{
            night_notify_switch.setChecked(false);
        }
    }
    private void getNotify(){
        courseChecked = spu.getCourseNotify();
        classChecked = spu.getClassNotify();
        posterChecked = spu.getPosterNotify();
        signChecked = spu.getSignNotify();
        chatChecked = spu.getConversationNotify();
        nightChecked = spu.getNightNotify();
        if(spu.getCourseNotify().equals("1")){
            course_notify_switch.setChecked(true);
        }else{
            course_notify_switch.setChecked(false);
        }
        if(spu.getClassNotify().equals("1")){
            class_notify_switch.setChecked(true);
        }else{
            class_notify_switch.setChecked(false);
        }
        if(spu.getPosterNotify().equals("1")){
            poster_notify_switch.setChecked(true);
        }else{
            poster_notify_switch.setChecked(false);
        }
        if(spu.getSignNotify().equals("1")){
            sign_notify_switch.setChecked(true);
        }else{
            sign_notify_switch.setChecked(false);
        }
        if(spu.getConversationNotify().equals("1")){
            conversation_notify_switch.setChecked(true);
        }else{
            conversation_notify_switch.setChecked(false);
        }
        if(spu.getNightNotify().equals("1")){
            night_notify_switch.setChecked(true);
        }else{
            night_notify_switch.setChecked(false);
        }
    }
    private void commit(){
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("course", courseChecked);
        hashMap.put("noticeclass", classChecked);
        hashMap.put("poster", posterChecked);
        hashMap.put("sign", signChecked);
        hashMap.put("chat", chatChecked);
        hashMap.put("night", nightChecked);
        final String json = hashMapToJson(hashMap);
        new HttpPostOld(this, this, new InterfaceHttpResult() {
            @Override
            public void getCallback(int requestCode, int code, String msg, Object baseBean) {
                L.d(R.string.notification_setting + msg + code);
            }
        }, null, Urls.FINISI_NOTICE_SETTING,"android",json).excute();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            commit();
            finish();
            return true;
        }
        return false;
    }
    public static String hashMapToJson(HashMap map) {
        String string = "{";
        for (Object o : map.entrySet()) {
            Map.Entry e = (Map.Entry) o;
            string += "\"" + e.getKey() + "\":";
            string += "\"" + e.getValue() + "\",";
        }
        string = string.substring(0, string.lastIndexOf(","));
        string += "}";
        return string;
    }
}

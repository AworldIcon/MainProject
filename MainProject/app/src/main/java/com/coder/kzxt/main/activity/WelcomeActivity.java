package com.coder.kzxt.main.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.http.HttpCallBack;
import com.app.http.UrlsNew;
import com.app.http.builders.HttpGetBuilder;
import com.app.utils.Utils;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.db.DataBaseDao;
import com.coder.kzxt.im.business.ImLoginBusiness;
import com.coder.kzxt.im.event.RefreshEvent;
import com.coder.kzxt.main.beans.LoginSetBean;
import com.coder.kzxt.utils.PermissionsUtil;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.com.tencent.qcloud.suixinbo.presenters.InitBusinessHelper;

public class WelcomeActivity extends BaseActivity implements HttpCallBack {

    private PermissionsUtil permissionsUtil;
    private SharedPreferencesUtil preferencesUtil;
    private ImageView wel_ad;
    private TextView goto_app;
    public static final int TOSPLASh = 0;
    public static final int TOMAIN = 1;


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TOSPLASh:
                    preferencesUtil.setShowLauncher(true);
                    Intent intent = new Intent();
                    intent.setClass(WelcomeActivity.this, SplashActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case TOMAIN:
                    goMain();
                    break;
            }


            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        preferencesUtil = new SharedPreferencesUtil(this);
        permissionsUtil = new PermissionsUtil(this);
        wel_ad = (ImageView) findViewById(R.id.wel_ad);
        goto_app = (TextView) findViewById(R.id.goto_app);

        if (!isTaskRoot()) {
            finish();
            return;
        }
        // 2G/3G下载标示 1:on；0:off
        String downloadFlag = preferencesUtil.getDownloadFlag();
        if (TextUtils.isEmpty(downloadFlag)) {
            preferencesUtil.setDownloadFlag("0");
        } else {
            preferencesUtil.setDownloadFlag(preferencesUtil.getDownloadFlag());
        }
        // 创建数据库
        DataBaseDao.getInstance(getApplication()).getConnection().close();
        if (TextUtils.isEmpty(preferencesUtil.getDevicedId())) {
            //获取电话CALL_PHONE权限
            if (permissionsUtil.read_phone_statePermissions()) {
                preferencesUtil.setDevicedId(Utils.getDeviceId(this));
                //继续
                continueGo();
            }
        } else {
            //继续
            continueGo();
        }
        preferencesUtil.setVersionName(Utils.getVersionName(this));

        initListener();
        initImStatus();
        initLoginSet();
    }

    private void initImStatus() {
        //初始化IMSDK
//        InitBusiness.start(getApplicationContext());
        InitBusinessHelper.initApp(getApplicationContext());
        //设置刷新监听
        RefreshEvent.getInstance();
        if (preferencesUtil.getIsLogin().equals("1")) {
            new ImLoginBusiness().login();
        }

//        TLSUserInfo userInfo = TLSLoginHelper.getInstance().getLastUserInfo();
//        if (userInfo != null )
//        {
//            UserInfo.getInstance().setId(userInfo.identifier);
//            UserInfo.getInstance().setUserSig(TLSLoginHelper.getInstance().getUserSig(userInfo.identifier));
//        }

    }

    private void initListener() {
        goto_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.removeMessages(TOMAIN);
                handler.removeMessages(TOSPLASh);
                goMain();
            }
        });

    }

    /**
     * 获取第三方登录配置
     */
    private void initLoginSet(){
        new HttpGetBuilder(WelcomeActivity.this)
                .setHttpResult(this)
                .setClassObj(LoginSetBean.class)
                .setUrl(UrlsNew.GET_USER_SETTING)
                .build();
    }


    //获取权限后执行的代码
    private void continueGo() {
        /**
         * 用户点击home键后，紧接着点“一键清除“功能。会出现程序已经退出了，但是记录当前位置的不为0.
         * 为了防止这种现像，我们在启动app的时候，设置下它的值
         */
        if (preferencesUtil.getShowLauncher()) {
            handler.sendEmptyMessageDelayed(TOMAIN, 2000);
        } else {
            handler.sendEmptyMessageDelayed(TOSPLASh, 2000);
        }
    }


    private void goMain() {
        Intent intent = new Intent();
        intent.setClass(WelcomeActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (permissionsUtil.permissionsResult(requestCode, permissions, grantResults)) {
            preferencesUtil.setDevicedId(Utils.getDeviceId(this));
            //继续
            continueGo();
        } else {

        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            handler.removeMessages(TOMAIN);
            handler.removeMessages(TOSPLASh);
            finish();
            return true;
        }
        return false;
    }

    @Override
    public void setOnSuccessCallback(int requestCode, Object resultBean) {
        LoginSetBean loginSetBean  = (LoginSetBean) resultBean;
        String setQ = loginSetBean.getItem().getSocial_account_login().getQq();
        String setW = loginSetBean.getItem().getSocial_account_login().getWechat();
        String qAppID =  loginSetBean.getItem().getMobile_qq_setting().getApp_id();
        String qSecret =  loginSetBean.getItem().getMobile_qq_setting().getApp_secret();
        String WAppID  = loginSetBean.getItem().getMobile_wechat_setting().getApp_id();
        String WSecret =loginSetBean.getItem().getMobile_wechat_setting().getApp_secret();

        preferencesUtil.setShowQLogin(setQ);
        preferencesUtil.setShowWLogin(setW);
        preferencesUtil.setQQID(qAppID);
        preferencesUtil.setQQSK(qSecret);
        preferencesUtil.setWXID(WAppID);
        preferencesUtil.setWXSK(WSecret);


    }

    @Override
    public void setOnErrorCallback(int requestCode, int code, String msg) {

    }
}

package com.coder.kzxt.login.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.http.HttpPostOld;
import com.app.http.InterfaceHttpResult;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.dialog.util.MyPublicDialog;
import com.coder.kzxt.login.beans.LoginDataBean;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.coder.kzxt.utils.ToastUtils;
import com.coder.kzxt.utils.Urls;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 绑定输入验证码
 */

public class BindSecondActivity extends BaseActivity {

    private SharedPreferencesUtil spu;
    private String from;
    private String account;
    private TextView phone_or_email;
    private TextView change_to_email;
    private EditText et_auth_code;
    private Button get_autn_code_button;
    private Button submit;
    private InputMethodManager imm;
    private Dialog dialog;
    private String ignoreCheckAccount = "1";
    String uid;
    String userface;
    String openId;
    private String type;
    String codeFrom;
    private TimeCount timeCount;
    private  String auth;
    private String token;//三方标示  token
    private String oauthtype;//三方登录类型  1 qq  2 微信
    private Dialog infodialog;
    private Toolbar mToolbarView;
     private RelativeLayout myLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpwd_second);

        spu = new SharedPreferencesUtil(this);
        from = getIntent().getStringExtra("from");
        account = getIntent().getStringExtra("account");
        token = getIntent().getStringExtra("token");
        oauthtype = getIntent().getStringExtra("oauthtype");
        uid = String.valueOf(spu.getUid());
        userface = spu.getUserHead();
        openId = String.valueOf(spu.getOpenUid());
        change_to_email = (TextView) findViewById(R.id.change_to_email);
        if (from.equals("removephoneoauth") && !TextUtils.isEmpty(spu.getEmail())) {
            change_to_email.setVisibility(View.VISIBLE);
        } else {
            change_to_email.setVisibility(View.GONE);
        }
        initView();
        initListener();
    }

    private void initView() {
        mToolbarView = (Toolbar) findViewById(R.id.toolbar);
        if (from.equals("bindphone")) {
            codeFrom = "2";
            type = "2";
            mToolbarView.setTitle(getResources().getString(R.string.bind_phone));
            getPhoneAuthCode(account, "2");
        } else if (from.equals("bindemail")) {
            codeFrom = "1";
            type = "1";
            mToolbarView.setTitle(getResources().getString(R.string.bind_email));
            getEmailAuthCode(account, "2");
        } else if (from.equals("oauthbindphone") || from.equals("changephone")) {//三方登陆绑定手机号
            codeFrom = "2";
            type = "2";
            mToolbarView.setTitle(getResources().getString(R.string.phone_num_auth));
            getPhoneAuthCode(account, "2");
        } else if (from.equals("oauthbindemail") || from.equals("changeemail")) {//三方登陆绑定邮箱
            codeFrom = "1";
            type = "1";
            mToolbarView.setTitle(getResources().getString(R.string.bind_email));
            getEmailAuthCode(account, "2");
        } else if (from.equals("oauthphonereg")) {//三方登陆注册
            codeFrom = "2";
            type = "2";
            mToolbarView.setTitle(getResources().getString(R.string.phone_num_auth));
            getPhoneAuthCode(account, "2");
        } else if (from.equals("oauthemailreg")) {//三方登陆注册
            codeFrom = "1";
            type = "1";
            mToolbarView.setTitle(getResources().getString(R.string.bind_email));
            getEmailAuthCode(account, "2");
        } else if (from.equals("removephoneoauth")) {
            codeFrom = "2";
            type = "2";
            mToolbarView.setTitle(getResources().getString(R.string.check_identity));
            getPhoneAuthCode(account, "2");
        } else if (from.equals("removeemailoauth")) {
            codeFrom = "1";
            type = "1";
            mToolbarView.setTitle(getResources().getString(R.string.check_identity));
            getEmailAuthCode(account, "2");
        }

        setSupportActionBar(mToolbarView);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        phone_or_email = (TextView) findViewById(R.id.phone_or_email);
        phone_or_email.setText(account);
        myLayout = (RelativeLayout) findViewById(R.id.rl_layout);
        et_auth_code = (EditText) findViewById(R.id.et_auth_code);
        et_auth_code.setFocusable(true);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                imm = (InputMethodManager) et_auth_code.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(et_auth_code, 0);
            }
        }, 500);
        get_autn_code_button = (Button) findViewById(R.id.get_autn_code_button);
        submit = (Button) findViewById(R.id.submit);
        if (!TextUtils.isEmpty(from)) {
            if (from.equals("oauthbindphone") || from.equals("changephone") || from.equals("removephoneoauth")) {//三方登陆后为老用户或者更换手机需绑定，解绑三方信息
                submit.setText(getResources().getString(R.string.check_identity));
            } else if (from.equals("oauthbindemail") || from.equals("changeemail") || from.equals("removeemailoauth")) {//三方登陆后为老用户或者更换邮箱需绑定，解绑三方信息
                submit.setText(getResources().getString(R.string.check_identity));
            } else {
                submit.setText(getResources().getString(R.string.bind));
            }
        } else {
            submit.setText(getResources().getString(R.string.bind));
        }
        submit.setAlpha(0.5f);
        submit.setLongClickable(false);
        submit.setClickable(false);
    }

    private void initListener() {
        change_to_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                account = spu.getEmail();
                if (timeCount != null) {
                    timeCount.cancel();
                }
                if (dialog != null && dialog.isShowing()) {
                    dialog.cancel();
                }
                from = "removeemailoauth";
                initView();
                change_to_email.setVisibility(View.GONE);
            }
        });
        get_autn_code_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (from.equals("bindphone")) {
                    getPhoneAuthCode(account, "2");
                } else if (from.equals("bindemail")) {
                    getEmailAuthCode(account, "2");
                } else if (from.equals("oauthbindphone") || from.equals("changephone") || from.equals("removephoneoauth")) {//三方登陆后为老用户或者更换手机需绑定，解绑三方信息
                    submit.setText(getResources().getString(R.string.check_identity));
                    getPhoneAuthCode(account, "2");
                } else if (from.equals("oauthbindemail") || from.equals("changeemail") || from.equals("removeemailoauth")) {//三方登陆后为老用户或者更换邮箱需绑定，解绑三方信息
                    getEmailAuthCode(account, "2");
                    submit.setText(getResources().getString(R.string.check_identity));
                } else if (from.equals("oauthphonereg")) {//三方登陆后为新用户需注册
                    getPhoneAuthCode(account, "2");
                } else if (from.equals("oauthemailreg")) {//三方登陆后为新用户需注册
                    getEmailAuthCode(account, "2");
                }
            }
        });
        et_auth_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s.toString().trim()) || s.length() == 0) {
                    submit.setAlpha(0.5f);
                    submit.setLongClickable(false);
                    submit.setClickable(false);
                } else {
                    submit.setAlpha(1.0f);
                    submit.setClickable(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString().trim()) || s.length() == 0) {
                    submit.setAlpha(0.5f);
                    submit.setClickable(false);
                } else {
                    submit.setAlpha(1.0f);
                    submit.setClickable(true);
                }
            }
        });
        //验证码是否一致，一致则进入下一步
        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (submit.getAlpha() == 0.5f) {
                    return;
                }
                imm.hideSoftInputFromWindow(et_auth_code.getWindowToken(), 0);
                auth = et_auth_code.getText().toString().trim();
                if (TextUtils.isEmpty(auth)) {
                    et_auth_code.setError(getResources().getString(R.string.input_auth_code_hint));
                    et_auth_code.setHintTextColor(getResources().getColor(R.color.font_red));
                    return;
                } else {
                    dialog = MyPublicDialog.createLoadingDialog(BindSecondActivity.this);
                    dialog.show();
                    //判断验证码
                    checkAuthCode(auth,type);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                if (!from.equals("removeemailoauth") || !from.equals("removephoneoauth")) {
                    setResult(1, new Intent().putExtra("data", ""));
                }
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 校验验证码
     * @param code
     * @param type
     */
    private void checkAuthCode(String code, String type) {
        new HttpPostOld(this,this, new InterfaceHttpResult() {
            @Override
            public void getCallback(int requestCode, int code, String msg, Object baseBean) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (requestCode == 1000 && code == Constants.HTTP_CODE_SUCCESS) {
                    successProcess();
                }else if (code == Constants.HTTP_CODE_4000 || code == Constants.HTTP_CODE_5000) {
                    NetworkUtil.httpNetErrTip(BindSecondActivity.this, myLayout);
                } else {
                    errorProcess(msg);
                }
            }
        }, null, Urls.POST_CHECK_CODE_ACTION, account, type, code, ignoreCheckAccount, spu.getDevicedId()).excute(1000);

    }

    /**
     * 成功处理
     */
    private void successProcess() {
        //验证码正确   则绑定
        if(!TextUtils.isEmpty(from)) {
            if (from.equals("oauthbindemail") || from.equals("oauthbindphone")) {
                if (oauthtype.equals("1")) {//1--qq登录，传QQ昵称
                    BindThirdOauthTask(token, oauthtype, spu.getQQName());
                } else {
                    BindThirdOauthTask(token, oauthtype, spu.getWXName());
                }
            } else if (from.equals("removephoneoauth") || from.equals("removeemailoauth")) {
                unBindOtherAuthTask(token, oauthtype);
            } else if (from.equals("oauthphonereg") || from.equals("oauthemailreg")) {//三方登陆后注册本地手机或邮箱
                Intent intent = new Intent();
                intent.setClass(BindSecondActivity.this, LoginThirdActivity.class);
                intent.putExtra("account", account);
                intent.putExtra("code", auth);
                intent.putExtra("type", type);
                intent.putExtra("comefrom", from);
                intent.putExtra("token", token);
                intent.putExtra("oauthtype", oauthtype);
                startActivityForResult(intent, 1);
            } else {
                bindPhoneEmailTask(auth, codeFrom, type);
            }
        }

    }

    /**
     * 绑定三方登录
     * @param token
     * @param oauthtype
     * @param thirdName
     */
    private void BindThirdOauthTask(String token, String oauthtype, String thirdName) {
        new HttpPostOld(this,this, new InterfaceHttpResult() {
            @Override
            public void getCallback(int requestCode, int code, String msg, Object baseBean) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (requestCode == 1000 && code == Constants.HTTP_CODE_SUCCESS) {
                    LoginDataBean.LoginBean bean = ((LoginDataBean) baseBean).getData();
                    bindThirdSuccess(bean);
                } else if (code == Constants.HTTP_CODE_4000 || code == Constants.HTTP_CODE_5000) {
                    NetworkUtil.httpNetErrTip(BindSecondActivity.this, myLayout);
                } else {
                    errorProcess(msg);
                }
            }
        }, LoginDataBean.class, Urls.POST_BIND_OTHER_ACCOUNT_ACTION,spu.getUid(),oauthtype,token,thirdName,spu.getDevicedId()).excute(1000);

    }

    /**
     * 绑定三方账号成功
     */
    private void bindThirdSuccess(LoginDataBean.LoginBean bean) {
        spu.setUid(bean.getUid());
        spu.setOpenUid(bean.getOpenId());
        spu.setIdPhoto(bean.getIdPhoto());
        spu.setStudentNum(bean.getStudNum());
        spu.setNickName(bean.getNickname());
        spu.setMotto(bean.getSignature());
        spu.setUserHead(bean.getUserface());
        spu.setBannerUrl(bean.getBannerurl());
        spu.setUserAccount(bean.getAccount());
        spu.setUserType(bean.getUserType());
        spu.setSex(bean.getSex());
        spu.setBalance(bean.getCash());
        spu.setCoin(bean.getGold());
        spu.setPhone(bean.getMobile());
        spu.setEmail(bean.getEmail());
        spu.setDevicedId(bean.getDeviceId());

 //        new LoginImAsynTask(BindSecondActivity.this).execute();
        setResult(4);
        finish();

    }

    /**
     * 解绑第三方账号
     * @param token
     * @param oauthtype
     */
    private void unBindOtherAuthTask(String token, String oauthtype) {
        new HttpPostOld(this,this, new InterfaceHttpResult() {
            @Override
            public void getCallback(int requestCode, int code, String msg, Object baseBean) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (requestCode == 1000 && code == Constants.HTTP_CODE_SUCCESS) {
                    unBindSuccess();
                } else if (code == Constants.HTTP_CODE_4000 || code == Constants.HTTP_CODE_5000) {
                    NetworkUtil.httpNetErrTip(BindSecondActivity.this, myLayout);
                } else {
                    errorProcess(msg);
                }
            }
        }, null, Urls.POST_UNBIND_OTHER_ACCOUNT_ACTION,spu.getUid(),oauthtype,token,spu.getIsLogin(),spu.getTokenSecret(),spu.getDevicedId()).excute(1000);

    }

    /**
     * 解绑成功
     */
    private void unBindSuccess() {
        ToastUtils.makeText(BindSecondActivity.this,"解绑成功！",Toast.LENGTH_SHORT);
        if (oauthtype.equals("1")) {
            setResult(5);
        } else if (oauthtype.equals("2")) {
            setResult(6);
        }
        finish();
    }

    /**
     * 绑定手机号或邮箱
     * @param auth
     * @param codeFrom
     * @param type
     */
    private void bindPhoneEmailTask(String auth, String codeFrom, String type) {
        new HttpPostOld(this,this, new InterfaceHttpResult() {
            @Override
            public void getCallback(int requestCode, int code, String msg, Object baseBean) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (requestCode == 1000 && code == Constants.HTTP_CODE_SUCCESS) {
                   bindSuccess();
                } else if (code == Constants.HTTP_CODE_4000 || code == Constants.HTTP_CODE_5000) {
                    NetworkUtil.httpNetErrTip(BindSecondActivity.this, myLayout);
                } else {
                    errorProcess(msg);
                }
            }
        }, null, Urls.POST_CHANGE_BIND_ACCOUNT_ACTION, spu.getUid(),account, type,auth,spu.getIsLogin(),spu.getTokenSecret(),codeFrom,spu.getDevicedId()).excute(1000);

    }

    /**
     * 绑定成功
     */
    private void bindSuccess() {
        if (from.equals("bindphone")) {
            spu.setPhone(account);
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.bind_success), Toast.LENGTH_LONG).show();
        } else if (from.equals("bindemail")) {
            spu.setEmail(account);
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.bind_success), Toast.LENGTH_LONG).show();
        } else if (from.equals("changephone")) {//更换手机
            spu.setPhone(account);
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.phone_changed), Toast.LENGTH_LONG).show();
        } else if (from.equals("changeemail")) {//更换邮箱
            spu.setEmail(account);
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.email_changed), Toast.LENGTH_SHORT).show();
        }
        //绑定成功之后判断是否关联云账号
//        if (Integer.parseInt(openId) > 0) {//openId大于0  表示已关联，直接进入
//            setResult(2, new Intent().putExtra("data", account));
//            finish();
//        } else {//需要关联，先获取是否有课关联的云中心账号,有-->直接关联，没有-->创建
//            Intent intent = new Intent();
//            intent.setClass(BindSecondActivity.this, RelateCenterActivity.class);
//            startActivityForResult(intent, 2);
//        }
    }

    /**
     * 获取手机验证码
     * @param account
     * @param type
     */
    private void getPhoneAuthCode(String account, String type) {
        new HttpPostOld(this,this, new InterfaceHttpResult() {
            @Override
            public void getCallback(int requestCode, int code, String msg, Object baseBean) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (requestCode == 1000 && code == Constants.HTTP_CODE_SUCCESS) {
                    timeCount = new TimeCount(60000, 1000);
                    timeCount.start();
                    ToastUtils.makeText(BindSecondActivity.this, msg, Toast.LENGTH_SHORT).show();
                } else if (code == Constants.HTTP_CODE_4000 || code == Constants.HTTP_CODE_5000) {
                    NetworkUtil.httpNetErrTip(BindSecondActivity.this, myLayout);
                } else {
                    errorProcess(msg);
                }
            }
        }, null, Urls.POST_PHONE_CODE_ACTION, account, type,spu.getDevicedId()).excute(1000);

    }


    /**
     * 获取邮箱验证码
     * @param account
     * @param type
     */
    private void getEmailAuthCode(String account, String type) {
        new HttpPostOld(this,this, new InterfaceHttpResult() {
            @Override
            public void getCallback(int requestCode, int code, String msg, Object baseBean) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (requestCode == 1000 && code == Constants.HTTP_CODE_SUCCESS) {
                    timeCount = new TimeCount(60000, 1000);
                    timeCount.start();
                    ToastUtils.makeText(BindSecondActivity.this, msg, Toast.LENGTH_SHORT).show();
                }else if (code == Constants.HTTP_CODE_4000 || code == Constants.HTTP_CODE_5000) {
                    NetworkUtil.httpNetErrTip(BindSecondActivity.this, myLayout);
                } else {
                    errorProcess(msg);
                }
            }
        }, null, Urls.POST_EMAIL_CODE_ACTION, account, type,spu.getDevicedId()).excute(1000);
    }


    /**
     * 异常提示
     * @param msg
     */
    private void errorProcess(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            Toast.makeText(BindSecondActivity.this, msg, Toast.LENGTH_SHORT).show();
        }
    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            get_autn_code_button.setTextColor(getResources().getColor(R.color.font_gray));
            get_autn_code_button.setBackgroundResource(R.drawable.round_getdynamic);
            get_autn_code_button.setClickable(false);
            get_autn_code_button.setText(millisUntilFinished / 1000 + getResources().getString(R.string.second));
        }

        @Override
        public void onFinish() {
            get_autn_code_button.setTextColor(getResources().getColor(R.color.first_theme));
            get_autn_code_button.setText(getResources().getString(R.string.get_auth_code_again));
            get_autn_code_button.setClickable(true);
            get_autn_code_button.setBackgroundResource(R.drawable.round_setdynamic);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        if (timeCount != null) {
            timeCount.cancel();
        }
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        if (requestCode == 1 && resultCode == 1) {
            et_auth_code.setText("");
        } else if (requestCode == 2 && resultCode == 1) {
            setResult(2);
            finish();
        } else if (requestCode == 1 && resultCode == 3) {
            setResult(3);
            finish();
        } else if (requestCode == 1 && resultCode == 4) {
            setResult(4);
            finish();
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (!from.equals("removeemailoauth") || !from.equals("removephoneoauth")) {
                setResult(1, new Intent().putExtra("data", ""));
            }
            finish();
            return true;
        }
        return false;
    }


}

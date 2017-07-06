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
import com.app.http.HttpCallBack;
import com.app.http.builders.HttpGetBuilder;
import com.app.http.builders.HttpPostBuilder;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.dialog.util.MyPublicDialog;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.app.http.UrlsNew;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 校验验证码
 */

public class CheckCodeActivity extends BaseActivity implements HttpCallBack{

    private SharedPreferencesUtil spu;
    private String from;
    private String account;
    private TextView phone_or_email;
    private EditText et_auth_code;
    private Button get_autn_code_button;
    private Button submit;
    private InputMethodManager imm;
    private Dialog dialog;
    private String ignoreCheckAccount;
    private TextView change_to_email;
    private String type,oauthType;
    private TimeCount timeCount;
    private String code;
    private Toolbar mToolbarView;
    private RelativeLayout myLayout;
    private int isTencent;//是否是第三方注册的流程，1是；

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_code);
        spu = new SharedPreferencesUtil(this);
        from = getIntent().getStringExtra("from");
        account = getIntent().getStringExtra("account");
        isTencent=getIntent().getIntExtra("isTencent",0);
        oauthType=getIntent().getStringExtra("oauthType");
        initView();
        initListener();
    }

    private void initView() {
        mToolbarView = (Toolbar) findViewById(R.id.toolbar);
        mToolbarView.setTitle(getResources().getString(R.string.email_num_register));
        change_to_email = (TextView) findViewById(R.id.change_to_email);
        if ((from.equals("phonechangepwd") || from.equals("changephone") ||
                from.equals("changeemailbyphone")) &&
                !TextUtils.isEmpty(spu.getEmail())) {
            change_to_email.setVisibility(View.VISIBLE);
        }
        if (from.equals("findbyphone")) {
            type = "2";
            mToolbarView.setTitle(getResources().getString(R.string.phone_num_auth));
            ignoreCheckAccount = "1";
            getPhoneAuthCode(account, "1");
        } else if (from.equals("findbyemail")) {
            type = "1";
            mToolbarView.setTitle(getResources().getString(R.string.email_num_auth));
            ignoreCheckAccount = "1";
            getEmailAuthCode(account,"1");
        } else if (from.equals("registerbyphone")) {
            type = "2";
            mToolbarView.setTitle(getResources().getString(R.string.phone_num_register));
            ignoreCheckAccount = "0";
            getPhoneAuthCode(account, "0");
        } else if (from.equals("registerbyemail")) {
            type = "1";
            mToolbarView.setTitle(getResources().getString(R.string.email_num_register));
            ignoreCheckAccount = "0";
            getEmailAuthCode(account,"0");
        } else if (from.equals("phonechangepwd") || from.equals("changephone") || from.equals("changeemailbyphone")) {   //更改密码或更改手机验证身份
            mToolbarView.setTitle(getResources().getString(R.string.check_identity));
            type = "2";
            ignoreCheckAccount = "1";
            getPhoneAuthCode(account, "1");
        } else if (from.equals("emailchangepwd") || from.equals("changeemail") || from.equals("changephonebyemail")
                || from.equals("onlyemail1") || from.equals("onlyemail2") || from.equals("omlyemail3")) {         //更改密码（没有手机）或更改邮箱验证身份
            mToolbarView.setTitle(getResources().getString(R.string.check_identity));
            type = "1";
            ignoreCheckAccount = "1";
            getEmailAuthCode(account,"1");
        }

        setSupportActionBar(mToolbarView);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        phone_or_email = (TextView) findViewById(R.id.phone_or_email);
        phone_or_email.setText(getResources().getString(R.string.authcode_send_to) +
                account + getResources().getString(R.string.receive_auth_code));
        et_auth_code = (EditText) findViewById(R.id.et_auth_code);
        et_auth_code.setFocusable(true);
        myLayout = (RelativeLayout) findViewById(R.id.rl_layout);
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
        submit.setAlpha(0.5f);
        submit.setClickable(false);
    }

    private void initListener() {

        change_to_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timeCount != null) {
                    timeCount.cancel();
                }
                if (dialog != null && dialog.isShowing()) {
                    dialog.cancel();
                }
                change_to_email.setVisibility(View.GONE);
                //切换邮箱验证身份
                if (from.equals("phonechangepwd")) {
                    from = "onlyemail1";
                } else if (from.equals("changephone")) {
                    from = "onlyemail2";
                } else if (from.equals("changeemailbyphone")) {
                    from = "onlyemail3";
                }
                account = spu.getEmail();
                phone_or_email.setText(getResources().getString(R.string.authcode_send_to) +
                        account + getResources().getString(R.string.receive_auth_code));
                et_auth_code.setText("");
                submit.setAlpha(0.5f);
                submit.setClickable(false);
                mToolbarView.setTitle(getResources().getString(R.string.check_identity));
                setSupportActionBar(mToolbarView);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                type = "1";
                ignoreCheckAccount = "1";
                getEmailAuthCode(account,"1");
            }
        });
        get_autn_code_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (from.equals("findbyphone") || from.equals("registerbyphone") || from.equals("phonechangepwd") || from.equals("changephone") || from.equals("changeemailbyphone")) {
                    getPhoneAuthCode(account, type);
                } else if (from.equals("findbyemail") || from.equals("registerbyemail") || from.equals("emailchangepwd") || from.equals("changeemail")
                        || from.equals("changephonebyemail") || from.equals("onlyemail1") || from.equals("onlyemail2") || from.equals("omlyemail3")) {
                    getEmailAuthCode(account,"1");
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
                    submit.setClickable(false);
                    submit.setLongClickable(false);
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
                code = et_auth_code.getText().toString().trim();
                //判断验证码
                dialog = MyPublicDialog.createLoadingDialog(CheckCodeActivity.this);
                dialog.show();
                checkAuthCode(code, type, ignoreCheckAccount);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                backFinish();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void backFinish() {
        if (from.equals("changephone") || from.equals("changeemail") || from.equals("phonechangepwd")
                || from.equals("emailchangepwd") || from.equals("changephonebyemail") || from.equals("changeemailbyphone")
                || from.equals("onlyemail1") || from.equals("onlyemail2") || from.equals("omlyemail3")) {
            setResult(7);
        } else {
            setResult(1);
        }
    }

    /**
     * 校验验证码
     * @param code
     * @param type
     * @param ignoreCheckAccount
     */
    private void checkAuthCode(String code, String type, String ignoreCheckAccount) {
        if(!account.contains("@")){
            checkPhoneCode(code);  //校验手机获取的验证码
        } else {
            checkEmailCode(code);  //校验邮箱的验证码
        }
    }

    /**
     * 校验邮箱的验证码
     * @param code
     */
    private void checkEmailCode(String code) {
        new HttpGetBuilder(this)
                .setUrl(UrlsNew.POST_SEND_CAPTCHA)            // 设置url   包含queryParams
                .setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        if (requestCode == 0) {
                            successProcess();
                        }
                    }
                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        if (code == Constants.HTTP_CODE_4000 || code == Constants.HTTP_CODE_5000) {
                            if(!NetworkUtil.isNetworkAvailable(CheckCodeActivity.this)){
                                NetworkUtil.httpNetErrTip(CheckCodeActivity.this, myLayout);
                            } else {
                                errorProcess(msg);
                            }
                        }
                    }
                })
                .setClassObj(null)
                .addQueryParams("email", account)
                .addQueryParams("send_method", "2")
                .addQueryParams("code",code)
                .addQueryParams("type", "register")
                .build();
    }

    /**
     * 校验手机获取的验证码
     * @param code
     */
    private void checkPhoneCode(String code) {

        new HttpGetBuilder(this)
                .setUrl(UrlsNew.POST_SEND_CAPTCHA)            // 设置url   包含queryParams
                .setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        if (requestCode == 0) {
                            successProcess();
                        }
                    }
                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        if (code == Constants.HTTP_CODE_4000 || code == Constants.HTTP_CODE_5000) {
                            if(!NetworkUtil.isNetworkAvailable(CheckCodeActivity.this)){
                                NetworkUtil.httpNetErrTip(CheckCodeActivity.this, myLayout);
                            } else {
                                errorProcess(msg);
                            }
                        }
                    }
                })
                .setClassObj(null)
                .addQueryParams("mobile", account)
                .addQueryParams("send_method", "1")
                .addQueryParams("code",code)
                .addQueryParams("type", "register")
                .build();
    }

    private void successProcess() {
        if (from.equals("changephone") || from.equals("changephonebyemail") || from.equals("onlyemail2")) {
            Intent intent = new Intent();
            intent.setClass(CheckCodeActivity.this, BindPhoneActivity.class);
            intent.putExtra("type", "changephone");
            intent.putExtra("token", "");
            intent.putExtra("oauthtype", "");
            startActivityForResult(intent, 2);
        } else if (from.equals("changeemail") || from.equals("changeemailbyphone") || from.equals("onlyemail3")) {
            Intent intent = new Intent();
            intent.setClass(CheckCodeActivity.this, BindEmailActivity.class);
            intent.putExtra("type", "changeemail");
            intent.putExtra("token", "");
            intent.putExtra("oauthtype", "");
            startActivityForResult(intent, 2);
        } else if (from.equals("phonechangepwd")) {
            Intent intent = new Intent();
            intent.setClass(CheckCodeActivity.this, RegisterThirdActivity.class);
            intent.putExtra("account", account);
            intent.putExtra("code", code);
            intent.putExtra("type", type);
            intent.putExtra("comefrom", "phonechangepwd");
            intent.putExtra("token", "");
            intent.putExtra("oauthtype", "");
            startActivityForResult(intent, 1);
        } else if (from.equals("emailchangepwd") || from.equals("onlyemail1")) {
            Intent intent = new Intent();
            intent.setClass(CheckCodeActivity.this, RegisterThirdActivity.class);
            intent.putExtra("account", account);
            intent.putExtra("code", code);
            intent.putExtra("type", type);
            intent.putExtra("comefrom", "emailchangepwd");
            intent.putExtra("token", "");
            intent.putExtra("oauthtype", "");
            startActivityForResult(intent, 1);
        } else {
            Intent intent = new Intent();
            intent.setClass(CheckCodeActivity.this, RegisterThirdActivity.class);
            intent.putExtra("account", account);
            intent.putExtra("code", code);
            intent.putExtra("type", type);
            intent.putExtra("oauthType", oauthType);
            intent.putExtra("comefrom", from);
            intent.putExtra("token", "");
            intent.putExtra("isTencent",isTencent);
            startActivityForResult(intent, 1);
        }
    }

    /**
     * 获取邮箱验证码
     * @param account
     * @param type
     */
    private void getEmailAuthCode(String account, String type) {
        new HttpPostBuilder(this)
                .setUrl(UrlsNew.POST_SEND_CAPTCHA)
                .setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        if(requestCode == 200){
                            timeCount = new TimeCount(60000, 1000);
                            timeCount.start();
                        }
                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        if (code == Constants.HTTP_CODE_4000 || code == Constants.HTTP_CODE_5000) {
                            if (!NetworkUtil.isNetworkAvailable(CheckCodeActivity.this)) {
                                NetworkUtil.httpNetErrTip(CheckCodeActivity.this, myLayout);
                            }
                        } else {
                            errorProcess(msg);
                        }
                    }
                })
                .setClassObj(null)
                .addBodyParam("email", account)
                .addBodyParam("send_method", "2")
                .addBodyParam("type", "register")
                .build();
    }

    /**
     * 获取手机验证码
     * @param account
     * @param type
     */
    private void getPhoneAuthCode(String account, String type) {
        new HttpPostBuilder(this)
                .setUrl(UrlsNew.POST_SEND_CAPTCHA)
                .setHttpResult(this)
                .setClassObj(null)
                .addBodyParam("mobile", account)
                .addBodyParam("send_method", "1")
                .addBodyParam("type", "register")
                .build();

    }

    private void errorProcess(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            Toast.makeText(CheckCodeActivity.this, msg, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void setOnSuccessCallback(int requestCode, Object resultBean) {
        if (requestCode == 0) {
            timeCount = new TimeCount(60000, 1000);
            timeCount.start();
        }
    }

    @Override
    public void setOnErrorCallback(int requestCode, int code, String msg) {
        if (code == Constants.HTTP_CODE_4000 || code == Constants.HTTP_CODE_5000) {
            NetworkUtil.httpNetErrTip(CheckCodeActivity.this, myLayout);
        } else {
            errorProcess(msg);
        }
    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            get_autn_code_button.setTextColor(getResources().getColor(R.color.font_white));
            get_autn_code_button.setBackgroundResource(R.drawable.round_getdynamic);
            get_autn_code_button.setClickable(false);
            get_autn_code_button.setText("重新发送"+"  ("+ millisUntilFinished / 1000+")");
        }

        @Override
        public void onFinish() {
            get_autn_code_button.setTextColor(getResources().getColor(R.color.font_white));
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
        if (requestCode == 1 && resultCode == 1) {
            et_auth_code.setText("");
            type = data.getStringExtra("type");
            account = data.getStringExtra("account");
        } else if (requestCode == 1 && resultCode == 3) {
            setResult(3);
            finish();
        } else if (requestCode == 2 && resultCode == 1) {//更换手机号
            String info = data.getStringExtra("data");
            setResult(3, new Intent().putExtra("data", info));
            finish();
        } else if (requestCode == 2 && resultCode == 2) {//更换邮箱
            String info = data.getStringExtra("data");
            setResult(4, new Intent().putExtra("data", info));
            finish();
        } else if(requestCode == 1 && resultCode == 2){ //注册成功
            setResult(2);
            finish();
        }

        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (from.equals("changephone") || from.equals("changeemail") || from.equals("phonechangepwd") ||
                    from.equals("emailchangepwd") || from.equals("onlyemail1") || from.equals("onlyemail2") || from.equals("onlyemail3")) {
                setResult(7);
            } else {
                setResult(1);
            }
            finish();
            return true;
        }
        return false;
    }

}

package com.coder.kzxt.login.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.app.http.HttpCallBack;
import com.app.http.builders.HttpGetBuilder;
import com.app.http.builders.HttpPostBuilder;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.dialog.util.MyPublicDialog;
import com.coder.kzxt.login.beans.LoginResultBean;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.coder.kzxt.utils.ToastUtils;
import com.app.http.UrlsNew;
import com.coder.kzxt.utils.ValidatorUtil;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 忘记密码
 * Created by wangtingshun on 2017/3/25.
 */
public class ForgetPassword extends BaseActivity implements HttpCallBack{

    private EditText numberEdit;
    private Button next;
    private SharedPreferencesUtil spu;
    private String phone_email;
    private RelativeLayout clearEdit;
    private Dialog dialog;
    private InputMethodManager imm;
    private Toolbar mToolbarView;
    private RelativeLayout myLayout;
    private Button mCodeButton;
    private TimeCount timeCount;
    private EditText et_auth_ode;
    private String authCode;
    private int type = 0;  //1.手机 2.邮箱

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        this.overridePendingTransition(R.anim.login_up_in,R.anim.login_stay);
        spu = new SharedPreferencesUtil(this);
        initView();
        initListener();
    }

    private void initView(){
        mToolbarView = (Toolbar) findViewById(R.id.toolbar);
        mToolbarView.setTitle(getResources().getString(R.string.password_error_text2_centre));
        setSupportActionBar(mToolbarView);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //删除已输入的账号
        clearEdit = (RelativeLayout) findViewById(R.id.delete_login);
        numberEdit = (EditText) findViewById(R.id.et_login_phone_email);
        numberEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(25)});
        numberEdit.setFocusable(true);

        myLayout = (RelativeLayout) findViewById(R.id.rl_layout);
        mCodeButton = (Button) findViewById(R.id.get_autn_code_button);
        et_auth_ode = (EditText) findViewById(R.id.et_auth_code);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                imm = (InputMethodManager) numberEdit.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(numberEdit, 0);
            }
        }, 500);

        next = (Button) findViewById(R.id.submit);
        next.setAlpha(0.5f);
        next.setClickable(false);
    }
    private void initListener(){
        
        numberEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s.toString().trim()) || s.length() == 0) {
                    clearEdit.setVisibility(View.INVISIBLE);
                    next.setAlpha(0.5f);
                    next.setClickable(false);
                    mCodeButton.setClickable(false);
                } else {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < s.length(); i++) {
                        if (i != 3 && i != 8 && s.charAt(i) == ' ') {
                            continue;
                        } else {
                            sb.append(s.charAt(i));
                            if ((sb.length() == 4 || sb.length() == 9) && sb.charAt(sb.length() - 1) != ' ') {
                                sb.insert(sb.length() - 1, ' ');
                            }
                        }
                    }
                    if (!sb.toString().equals(s.toString())) {
                        int index = start + 1;
                        if (sb.charAt(start) == ' ') {
                            if (before == 0) {
                                index++;
                            } else {
                                index--;
                            }
                        } else {
                            if (before == 1) {
                                index--;
                            }
                        }
                        numberEdit.setText(sb.toString());
                        numberEdit.setSelection(index);
                    }
                    clearEdit.setVisibility(View.VISIBLE);
                    next.setAlpha(1.0f);
                    next.setClickable(true);
                    mCodeButton.setClickable(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String number = s.toString().trim();
                if (TextUtils.isEmpty(number) || number.length() == 0) {
                    type = 0;
                    next.setAlpha(0.5f);
                    next.setClickable(false);
                    next.setLongClickable(false);
                    clearEdit.setVisibility(View.INVISIBLE);
                } else {
                    next.setAlpha(1.0f);
                    next.setClickable(true);
                    clearEdit.setVisibility(View.VISIBLE);
                }
            }
        });

        //清空输入框
        clearEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberEdit.setText("");
            }
        });

        //验证码输入框
        et_auth_ode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(TextUtils.isEmpty(editable.toString().trim()) ||
                        editable.length() == 0){
                    et_auth_ode.setHintTextColor(getResources().getColor(R.color.font_gray));
                }
            }
        });

        //下一步
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (next.getAlpha() == 0.5f) {
                    return;
                }
                imm.hideSoftInputFromWindow(numberEdit.getWindowToken(), 0);
                phone_email = numberEdit.getText().toString().replace(" ", "").trim();
                authCode = et_auth_ode.getText().toString().trim();
                if(phone_email.contains("@")){
                    type = 2;
                } else {
                    type = 1;
                }
                if (TextUtils.isEmpty(phone_email)) {
                    numberEdit.setError(getResources().getString(R.string.input_email_phone_hint));
                    numberEdit.setHintTextColor(getResources().getColor(R.color.font_red));
                    return;
                } else if(TextUtils.isEmpty(authCode)){
                    et_auth_ode.setError(getResources().getString(R.string.input_auth_code_hint));
                    et_auth_ode.setHintTextColor(getResources().getColor(R.color.font_red));
                    return;
                } else {
                    //判断手机号并获取验证码，获取成功进入下一页
                    if(ValidatorUtil.isMobile(phone_email) || ValidatorUtil.isEmail(phone_email)){
                        dialog = MyPublicDialog.createLoadingDialog(ForgetPassword.this);
                        dialog.show();
                        checkAccountTask(phone_email,type);
                    } else {
                        ToastUtils.makeText(ForgetPassword.this,"格式不正确",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        
        //获取验证码
        mCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAuthCode();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                setResult(3);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 检查账号
     * @param phoneEmail
     */
    private void checkAccountTask(String phoneEmail,int type) {
        if (type == 1) {   //校验手机
            checkPhoneAccount(phoneEmail);
        } else if (type == 2) {
            checkEmailAcocunt(phoneEmail);
        }
    }

    /**
     * 检查邮箱账号
     * @param phoneEmail
     */
    private void checkEmailAcocunt(String phoneEmail) {
        new HttpGetBuilder(this)
                .setUrl(UrlsNew.GET_USER_LIST)
                .setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
                        if (requestCode == 0) {
                            LoginResultBean bean = (LoginResultBean) resultBean;
                            LoginResultBean.Paginate paginate = bean.getPaginate();
                            if (paginate.getTotalNum() == 0) {  //没有被注册
                                if (dialog != null && dialog.isShowing()) {
                                    dialog.dismiss();
                                }
                                ToastUtils.makeText(ForgetPassword.this, getResources().getString(R.string.no_register), Toast.LENGTH_SHORT).show();
                            } else {
                                //校验验证码
                                checkEmailAuthCode();
                            }
                        }
                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        if (requestCode == Constants.HTTP_CODE_4000 ||
                                requestCode == Constants.HTTP_CODE_5000) {
                            if (!NetworkUtil.isNetworkAvailable(ForgetPassword.this)) {
                                NetworkUtil.httpNetErrTip(ForgetPassword.this, myLayout);
                            } else {
                                //其他处理
                            }
                        }
                        errorProcess(msg);
                    }
                })
                .setClassObj(LoginResultBean.class)
                .addQueryParams("email", phoneEmail)
                .build();
    }

    /**
     * 校验邮箱验证码
     */
    private void checkEmailAuthCode() {
        new HttpGetBuilder(this)
                .setUrl(UrlsNew.POST_SEND_CAPTCHA)
                .setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        if (requestCode == 0) {
                            setPassword();
                        }
                    }
                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        if (code == Constants.HTTP_CODE_4000 || code == Constants.HTTP_CODE_5000) {
                            if(!NetworkUtil.isNetworkAvailable(ForgetPassword.this)){
                                NetworkUtil.httpNetErrTip(ForgetPassword.this, myLayout);
                            } else {
                                errorProcess(msg);
                            }
                        }
                    }
                })
                .setClassObj(null)
                .addQueryParams("email", phone_email)
                .addQueryParams("send_method", "2")
                .addQueryParams("code",authCode)
                .addQueryParams("type", "reset_password")
                .build();
    }

    /**
     * 检查手机账号
     * @param phoneEmail
     */
    private void checkPhoneAccount(String phoneEmail) {
        new HttpGetBuilder(this)
                .setUrl(UrlsNew.GET_USER_LIST)
                .setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
                        if (requestCode == 0) {
                            LoginResultBean bean = (LoginResultBean) resultBean;
                            LoginResultBean.Paginate paginate = bean.getPaginate();
                            if (paginate.getTotalNum() == 0) {  //没有被注册
                                if (dialog != null && dialog.isShowing()) {
                                    dialog.dismiss();
                                }
                                ToastUtils.makeText(ForgetPassword.this, getResources().getString(R.string.no_register), Toast.LENGTH_SHORT).show();
                            } else {
                                //校验验证码
                                checkPhneAuthCode();
                            }
                        }
                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {
                        if (requestCode == Constants.HTTP_CODE_4000 ||
                                requestCode == Constants.HTTP_CODE_5000) {
                            if (!NetworkUtil.isNetworkAvailable(ForgetPassword.this)) {
                                NetworkUtil.httpNetErrTip(ForgetPassword.this, myLayout);
                            } else {
                                //其他处理
                            }
                        }
                        errorProcess(msg);
                    }
                })
                .setClassObj(LoginResultBean.class)
                .addQueryParams("mobile", phoneEmail)
                .build();
    }

    /**
     * 校验手机验证码
     */
    private void checkPhneAuthCode() {
        new HttpGetBuilder(this)
                .setUrl(UrlsNew.POST_SEND_CAPTCHA)
                .setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        if (requestCode == 0) {
                            setPassword();
                        }
                    }
                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        if (code == Constants.HTTP_CODE_4000 || code == Constants.HTTP_CODE_5000) {
                            if(!NetworkUtil.isNetworkAvailable(ForgetPassword.this)){
                                NetworkUtil.httpNetErrTip(ForgetPassword.this, myLayout);
                            }
                        }
                        errorProcess(msg);
                    }
                })
                .setClassObj(null)
                .addQueryParams("mobile", phone_email)
                .addQueryParams("send_method", "1")
                .addQueryParams("code",authCode)
                .addQueryParams("type", "reset_password")
                .build();

    }

    /**
     * 设置密码
     */
    private void setPassword() {
        Intent intent = new Intent(ForgetPassword.this, RegisterThirdActivity.class);
        intent.putExtra("account", phone_email);
        intent.putExtra("comefrom",String.valueOf(type));
        intent.putExtra("code", authCode);
        startActivityForResult(intent, 1);
        finish();
    }

    /**
     * 获取验证码
     */
    private void getAuthCode() {
        phone_email = numberEdit.getText().toString().replace(" ", "").trim();
        if(ValidatorUtil.isEmail(phone_email) ||
                ValidatorUtil.isMobile(phone_email)){
            if (!phone_email.contains("@")) {
                if (ValidatorUtil.isMobile(phone_email)) {
                    getPhoneAuthCode(phone_email);
                } else {
                    ToastUtils.makeText(this, getResources().getString(R.string.number_fomat_error), Toast.LENGTH_SHORT).show();
                }
            } else {
                if (ValidatorUtil.isEmail(phone_email)) {
                    getEmailAuthCode(phone_email);
                } else {
                    ToastUtils.makeText(this, getResources().getString(R.string.email_fomat_error), Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            ToastUtils.makeText(this,getResources().getString(R.string.email_phone_fomat),Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 获取邮箱验证码
     * @param phoneEmail
     */
    private void getEmailAuthCode(String phoneEmail) {
        new HttpPostBuilder(this)
                .setUrl(UrlsNew.POST_SEND_CAPTCHA)
                .setHttpResult(this)
                .setClassObj(null)
                .addBodyParam("email", phoneEmail)
                .addBodyParam("send_method", "2")
                .addBodyParam("type", "reset_password")
                .build();
    }

    /**
     * 获取手机验证码
     * @param phoneEmail
     */
    private void getPhoneAuthCode(String phoneEmail) {
        new HttpPostBuilder(this)
                .setUrl(UrlsNew.POST_SEND_CAPTCHA)
                .setHttpResult(this)
                .setClassObj(null)
                .addBodyParam("mobile", phoneEmail)
                .addBodyParam("send_method", "1")
                .addBodyParam("type", "reset_password")
                .build();
    }

    /**
     * 异常处理
     * @param msg
     */
    private void errorProcess(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            Toast.makeText(ForgetPassword.this, msg, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK &&
                event.getRepeatCount() == 0) {
            setResult(3);
            finish();
            return true;
        }
        return false;
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 1) {
          //  numberEdit.setText("");
        } else if (requestCode == 1 && resultCode == 3) {
            setResult(3);
            finish();
        } else if (requestCode == 1 && resultCode == 2) {
            setResult(2);
            finish();
        }
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(R.anim.login_stay,R.anim.login_down_out);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void setOnSuccessCallback(int requestCode, Object resultBean) {
        if (requestCode == 0) {
            timeCount = new TimeCount(60000, 1000);
            timeCount.start();
            ToastUtils.makeText(ForgetPassword.this,"验证码已发送",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void setOnErrorCallback(int requestCode, int code, String msg) {
        ToastUtils.makeText(ForgetPassword.this,msg,Toast.LENGTH_SHORT).show();
    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            mCodeButton.setTextColor(getResources().getColor(R.color.font_white));
            mCodeButton.setBackgroundResource(R.drawable.round_getdynamic);
            mCodeButton.setClickable(false);
            mCodeButton.setText("重新发送"+"  ("+ millisUntilFinished / 1000+")");
        }

        @Override
        public void onFinish() {
            mCodeButton.setTextColor(getResources().getColor(R.color.first_theme));
            mCodeButton.setText(getResources().getString(R.string.get_auth_code_again));
            mCodeButton.setClickable(true);
            mCodeButton.setBackgroundResource(R.drawable.round_theme1);
        }
    }
}

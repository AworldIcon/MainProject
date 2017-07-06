package com.coder.kzxt.login.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
 * 设置密码
 */

public class LoginThirdActivity extends BaseActivity {

    private EditText et_password_first;
    private ImageView open_password_first;
    private View iv_pwd_line_first;
    private EditText et_password_confirm;
    private ImageView open_password_confirm;
    private View iv_pwd_line_confirm;
    private Button finish_btn;
    private SharedPreferencesUtil spu;
    private String account;
    private String type;
    private String from;
    private String password;
    private String confirm_password;
    private String code;
    private Dialog dialog;
    private String comefrom;
    private boolean first_click = true;
    private boolean confirm_click = true;
    private boolean hasPwd = false;
    private boolean hasConfirmPwd = false;
    private boolean pwdFocus = false;
    private boolean confirmPwdFocus = false;
    private String token;
    private String oauthtype;
     private Toolbar mToolbarView;
    private RelativeLayout myLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpwd_third);
//        MyApplication.list.add(this);
        spu = new SharedPreferencesUtil(LoginThirdActivity.this);
         account = getIntent().getStringExtra("account");
        type = getIntent().getStringExtra("type");
        code = getIntent().getStringExtra("code");
        comefrom = getIntent().getStringExtra("comefrom");
        token = getIntent().getStringExtra("token");
        oauthtype = getIntent().getStringExtra("oauthtype");
        from = "2";
        initView();
        initListener();
    }

    private void initView() {
        mToolbarView = (Toolbar) findViewById(R.id.toolbar);
        mToolbarView.setTitle(getResources().getString(R.string.set_password));
        setSupportActionBar(mToolbarView);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        et_password_first = (EditText) findViewById(R.id.et_password_first);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) et_password_first.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(et_password_first, 0);
            }
        }, 500);
        open_password_first = (ImageView) findViewById(R.id.open_password_first);
        iv_pwd_line_first = findViewById(R.id.iv_pwd_line_first);
        et_password_confirm = (EditText) findViewById(R.id.et_password_confirm);
        open_password_confirm = (ImageView) findViewById(R.id.open_password_confirm);
        iv_pwd_line_confirm = findViewById(R.id.iv_pwd_line_confirm);
        myLayout = (RelativeLayout) findViewById(R.id.rl_layout);
        finish_btn = (Button) findViewById(R.id.finish);
        finish_btn.setAlpha(0.5f);
        finish_btn.setClickable(false);
    }

    private void initListener() {

        open_password_first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (first_click) {
                    open_password_first.setImageResource(R.drawable.open_password);
                    et_password_first.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    first_click = false;
                } else {
                    open_password_first.setImageResource(R.drawable.close_password);
                    et_password_first.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    first_click = true;
                }
            }
        });
        open_password_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (confirm_click) {
                    open_password_confirm.setImageResource(R.drawable.open_password);
                    et_password_confirm.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    confirm_click = false;
                } else {
                    open_password_confirm.setImageResource(R.drawable.close_password);
                    et_password_confirm.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    confirm_click = true;
                }
            }
        });
        et_password_first.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s.toString().trim()) || s.length() == 0 || s.length() < 5) {
                    hasPwd = false;
                } else {
                    hasPwd = true;
                }
                isFinish();
                isChangeLine();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString().trim()) || s.length() == 0 || s.length() < 5) {
                    hasPwd = false;
                } else {
                    hasPwd = true;
                }
                isFinish();
                isChangeLine();
                if (s.length() > 20) {
                    ToastUtils.makeText(LoginThirdActivity.this, getResources().getString(R.string.pwd_length_limit), Toast.LENGTH_SHORT);
                }
            }
        });
        et_password_confirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s.toString().trim()) || s.length() == 0 || s.length() < 5) {
                    hasConfirmPwd = false;
                } else {
                    hasConfirmPwd = true;
                }
                isFinish();
                isChangeLine();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString().trim()) || s.length() == 0 || s.length() < 5) {
                    hasConfirmPwd = false;
                } else {
                    hasConfirmPwd = true;
                }
                isFinish();
                isChangeLine();
                if (s.length() > 20) {
                    ToastUtils.makeText(LoginThirdActivity.this, getResources().getString(R.string.pwd_length_limit), Toast.LENGTH_SHORT);
                }
            }
        });
        et_password_first.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    pwdFocus = true;
                } else {
                    pwdFocus = false;
                }
                isChangeLine();
            }
        });
        et_password_confirm.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    confirmPwdFocus = true;
                } else {
                    confirmPwdFocus = false;
                }
                isChangeLine();
            }
        });
        finish_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finish_btn.getAlpha() == 0.5f) {
                    return;
                }
                password = et_password_first.getText().toString().trim().replace(" ", "");
                confirm_password = et_password_confirm.getText().toString().trim().replace(" ", "");

                if (!password.equals(confirm_password)) {
                    ToastUtils.makeText(LoginThirdActivity.this, getResources().getString(R.string.two_password_not_same), Toast.LENGTH_SHORT);
                } else {
                    //两次密码一致则设置成功
                    dialog = MyPublicDialog.createLoadingDialog(LoginThirdActivity.this);
                    dialog.show();
                    if (comefrom.equals("findbyphone") || comefrom.equals("findbyemail")) {//找回
//                        StatService.onEvent(getApplicationContext(), "UserLogin", getResources().getString(R.string.login), 1);
                        findPasswordTask(password, confirm_password);
                    } else if (comefrom.equals("registerbyphone") || comefrom.equals("registerbyemail")) {//注册
//                        StatService.onEvent(getApplicationContext(), "UserRegister", getResources().getString(R.string.register), 1);
                         RegisterTask(password, from);
                    } else if (comefrom.equals("phonechangepwd") || comefrom.equals("emailchangepwd")) {//修改密码
                        findPasswordTask(password, confirm_password);
                    } else if (comefrom.equals("oauthphonereg") || comefrom.equals("oauthemailreg")) {
//                        StatService.onEvent(getApplicationContext(), "UserRegister", getResources().getString(R.string.register), 1);
                        RegisterTask(password, from);
                    }
                }
            }
        });
    }

    /**
     * 注册接口
     * @param password
     * @param from
     */
    private void RegisterTask(String password, String from) {
        new HttpPostOld(this,this, new InterfaceHttpResult() {
            @Override
            public void getCallback(int requestCode, int code, String msg, Object baseBean) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (requestCode == 1000 && code == Constants.HTTP_CODE_SUCCESS) {
                    LoginDataBean.LoginBean bean = ((LoginDataBean) baseBean).getData();
                    if (bean != null) {
                        registerSuccess(bean);
                    } else {
                        errorProcess(msg);
                    }
                }  else if (code == Constants.HTTP_CODE_4000 || code == Constants.HTTP_CODE_5000) {
                    NetworkUtil.httpNetErrTip(LoginThirdActivity.this, myLayout);
                } else {
                    errorProcess(msg);
                }
            }
        },  LoginDataBean.class, Urls.POST_REGISTER_ACTION, account,password,type,code,"1",from,"Android",spu.getDevicedId()).excute(1000);
    }

    private void registerSuccess(LoginDataBean.LoginBean bean) {

        ToastUtils.makeText(LoginThirdActivity.this,
                getResources().getString(R.string.register_pwd_success), Toast.LENGTH_SHORT);
        spu.setUserHead(bean.getUserface());
        spu.setUid(bean.getUid());
        spu.setNickName(bean.getNickname());
        spu.setSex(bean.getSex());
        spu.setEmail(bean.getEmail());
        spu.setPhone(bean.getMobile());
        spu.setMotto(bean.getSignature());
        spu.setUserAccount(bean.getAccount());
        spu.setUserType(bean.getUserType());
        spu.setBalance(bean.getCash());
        spu.setCoin(bean.getGold());
        spu.setOpenUid(bean.getOpenId());
        spu.setStudentNum(bean.getStudNum());

        if (comefrom.equals("oauthphonereg") || comefrom.equals("oauthemailreg")) {
            if (oauthtype.equals("1")) {//1--qq登录，传QQ昵称
                BindThirdOauthTask(token, oauthtype, spu.getQQName());
            } else {
                BindThirdOauthTask(token, oauthtype, spu.getWXName());
            }
        } else {
//            new LoginImAsynTask(LoginThirdActivity.this).execute();
//            PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY, PublicUtils.getMetaValue(LoginThirdActivity.this, "api_key"));
            setResult(3);
            finish();
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
                    NetworkUtil.httpNetErrTip(LoginThirdActivity.this, myLayout);
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
     * 提交修改密码
     * @param password
     * @param confirm_password
     */
    private void findPasswordTask(String password, String confirm_password) {
        new HttpPostOld(this,this, new InterfaceHttpResult() {
            @Override
            public void getCallback(int requestCode, int code, String msg, Object baseBean) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                if(requestCode == 1000 && code == Constants.HTTP_CODE_SUCCESS) {
                    ToastUtils.makeText(LoginThirdActivity.this,getResources().getString(R.string.pwd_set_success),Toast.LENGTH_SHORT);
                    setResult(3);
                    finish();
                } else if (code == Constants.HTTP_CODE_4000 || code == Constants.HTTP_CODE_5000) {
                    NetworkUtil.httpNetErrTip(LoginThirdActivity.this, myLayout);
                } else {
                    errorProcess(msg);
                }

            }
        }, null, Urls.POST_CHANGE_PASSWORD_ACTION, account, code,password,confirm_password,type,from,spu.getDevicedId()).excute(1000);

    }

    /**
     * 异常处理
     * @param msg
     */
    private void errorProcess(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            ToastUtils.makeText(LoginThirdActivity.this, msg, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                backPrevices();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void backPrevices() {
        Intent intent = new Intent();
        intent.putExtra("type", type);
        intent.putExtra("account", account);
        setResult(1, intent);
    }


    private void isFinish() {
        if (hasPwd && hasConfirmPwd) {
            finish_btn.setClickable(true);
            finish_btn.setAlpha(1.0f);
        } else {
            finish_btn.setClickable(false);
            finish_btn.setLongClickable(false);
            finish_btn.setAlpha(0.5f);
        }
    }

    private void isChangeLine() {
        if (hasPwd || pwdFocus) {
            iv_pwd_line_first.setBackgroundResource(R.color.first_theme);
        } else {
            iv_pwd_line_first.setBackgroundResource(R.color.font_gray);
        }
        if (hasConfirmPwd || confirmPwdFocus) {
            iv_pwd_line_confirm.setBackgroundResource(R.color.first_theme);
        } else {
            iv_pwd_line_confirm.setBackgroundResource(R.color.font_gray);
        }
    }

    @Override
    public void onPause() {
        /**
         * 页面结束（每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加）
         * 不能与StatService.onPageStart一级onPageEnd函数交叉使用
         */
//        StatService.onPause(this);
        super.onPause();
    }

    @Override
    public void onResume() {
        /**
         * 页面起始（每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加）
         * 不能与StatService.onPageStart一级onPageEnd函数交叉使用
         */
//        StatService.onResume(this);
        super.onResume();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Intent intent = new Intent();
            intent.putExtra("type", type);
            intent.putExtra("account", account);
            setResult(1, intent);
            finish();
            return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && requestCode == 1) {//三方登陆后，不关联或者创建云联盟账号，则没有个人信息
            setResult(3);
        } else if (requestCode == 1 && resultCode == 2) {//三方登陆，先注册本地账号然后新建或者关联云联盟账号
            setResult(4);
        }
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }


}

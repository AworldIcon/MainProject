package com.coder.kzxt.login.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
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

import com.app.http.HttpCallBack;
import com.app.http.builders.HttpPostBuilder;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.dialog.util.MyPublicDialog;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.coder.kzxt.utils.ToastUtils;
import com.app.http.UrlsNew;
import com.coder.kzxt.utils.ValidatorUtil;

import java.util.Timer;
import java.util.TimerTask;

import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * 设置密码
 */

public class RegisterThirdActivity extends BaseActivity {

    private EditText et_password_first;
    private ImageView open_password_first;
    private EditText et_password_confirm;
    private ImageView open_password_confirm;
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
    private RelativeLayout delFirstClear;
    private RelativeLayout delSecondClear;
    private int isTencent;//是否是第三方注册的流程，1是；

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpwd_third);
        spu = new SharedPreferencesUtil(this);
        isTencent=getIntent().getIntExtra("isTencent",0);
        account = getIntent().getStringExtra("account");
        type = getIntent().getStringExtra("type");
        code = getIntent().getStringExtra("code");
        comefrom = getIntent().getStringExtra("comefrom");
        token = getIntent().getStringExtra("token");
        oauthtype = getIntent().getStringExtra("oauthType");
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
        et_password_confirm = (EditText) findViewById(R.id.et_password_confirm);
        open_password_confirm = (ImageView) findViewById(R.id.open_password_confirm);
        et_password_confirm.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
        et_password_first.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
        delFirstClear = (RelativeLayout) findViewById(R.id.delete_first);
        delSecondClear = (RelativeLayout) findViewById(R.id.delete_confirm);
        finish_btn = (Button) findViewById(R.id.finish);
        myLayout = (RelativeLayout) findViewById(R.id.rl_layout);
        finish_btn.setAlpha(0.5f);
        finish_btn.setClickable(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                backPrevious();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 返回上一页面
     */
    private void backPrevious() {
        Intent intent = new Intent();
        intent.putExtra("type", type);
        intent.putExtra("account", account);
        setResult(1, intent);
        finish();
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
        //清空
        delFirstClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_password_first.setText("");
            }
        });

        delSecondClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_password_confirm.setText("");
            }
        });

        et_password_first.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s.toString().trim()) || s.length() == 0) {
                    hasPwd = false;
                } else {
                    hasPwd = true;
                }
                isFinish();

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString().trim()) || s.length() == 0) {
                    hasPwd = false;
                    delFirstClear.setVisibility(View.INVISIBLE);
                } else {
                    hasPwd = true;
                    delFirstClear.setVisibility(View.VISIBLE);
                }
                isFinish();
                if (s.length() >= 16) {
                    ToastUtils.makeText(RegisterThirdActivity.this,getResources().getString(R.string.input_num_limit_pwd_hint), Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
        et_password_confirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s.toString().trim()) || s.length() == 0) {
                    hasConfirmPwd = false;
                    delSecondClear.setVisibility(View.INVISIBLE);
                } else {
                    hasConfirmPwd = true;
                    delSecondClear.setVisibility(View.VISIBLE);
                }
                isFinish();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString().trim()) || s.length() == 0 ) {
                    hasConfirmPwd = false;
                } else {
                    hasConfirmPwd = true;
                }
                isFinish();
                if (s.length() >= 16) {
                    ToastUtils.makeText(RegisterThirdActivity.this,getResources().getString(R.string.input_num_limit_pwd_hint), Toast.LENGTH_SHORT).show();
                    return;
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

                if (password.length() < 6 || confirm_password.length() < 6) {
                    ToastUtils.makeText(RegisterThirdActivity.this, getResources().getString(R.string.input_num_limit_pwd_hint), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!password.equals(confirm_password)) {
                    Toast.makeText(RegisterThirdActivity.this,getResources().getString(R.string.two_password_not_same), Toast.LENGTH_SHORT).show();
                } else {
                    //两次密码一致则设置成功
                    dialog = MyPublicDialog.createLoadingDialog(RegisterThirdActivity.this);
                    dialog.show();
                    if (ValidatorUtil.isPassword(password)) {
                        if (!TextUtils.isEmpty(comefrom) && (comefrom.equals("1") || comefrom.equals("2"))) {//找回
                            commitChangePassword(password, confirm_password);
                        } else if (comefrom.equals("registerbyphone") || comefrom.equals("registerbyemail")) {//注册
                            if(isTencent==0){
                                RegisterTask(password, from);
                            }else {
                                registerTencentThird();
                            }

                        } else if (comefrom.equals("phonechangepwd") || comefrom.equals("emailchangepwd")) {//修改密码
                            commitChangePassword(password, confirm_password);
                        } else if (comefrom.equals("oauthphonereg") || comefrom.equals("oauthemailreg")) {
                            RegisterTask(password, from);
                        } else {
                            if (dialog != null && dialog.isShowing()) {
                                dialog.cancel();
                            }
                            ToastUtils.makeText(RegisterThirdActivity.this, "设置密码失败", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        ToastUtils.makeText(RegisterThirdActivity.this, "密码格式不正确", Toast.LENGTH_SHORT).show();
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
        new HttpPostBuilder(this)
                .setUrl(UrlsNew.POST_USER_REGISTER)
                .setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
                        Toast.makeText(RegisterThirdActivity.this,
                                getResources().getString(R.string.register_pwd_success), Toast.LENGTH_SHORT).show();
                        setResult(2);
                        finish();

                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {
                        if(dialog != null && dialog.isShowing()){
                            dialog.cancel();
                        }
                        if (code == Constants.HTTP_CODE_4000 || code == Constants.HTTP_CODE_5000) {
                            if(!NetworkUtil.isNetworkAvailable(RegisterThirdActivity.this)){
                                NetworkUtil.httpNetErrTip(RegisterThirdActivity.this, myLayout);
                            }
                        } else {
                            errorProcess(msg);
                        }
                    }
                })
                .setClassObj(null)
                .addBodyParam("nickname", "匿名")
                .addBodyParam("mobile", account)
                .addBodyParam("password", password)
                .addBodyParam("register_type","1")
                .addBodyParam("register_code",code)
                .build();

    }
/**
 * 第三方注册
 *
 * */
    private void registerTencentThird(){
        String nickName="";
        String headUrl="";
        String openId="";

        if(oauthtype.equals("qq")){
            nickName=new QQ(this).getDb().getUserName();
            headUrl=new QQ(this).getDb().getUserIcon();
            openId=new QQ(this).getDb().getUserId();
        }else {
            nickName=new Wechat(this).getDb().getUserName();
            headUrl=new Wechat(this).getDb().getUserIcon();
            openId=new Wechat(this).getDb().getToken();
        }
        new HttpPostBuilder(this).setUrl(UrlsNew.POST_SOCIAL_ACCOUNT_REGISTER).setClassObj(null)
                .setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
                        Toast.makeText(RegisterThirdActivity.this,
                                getResources().getString(R.string.register_pwd_success), Toast.LENGTH_SHORT).show();
                        setResult(2);
                        finish();

                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {
                        if(dialog != null && dialog.isShowing()){
                            dialog.cancel();
                        }
                        if (code == Constants.HTTP_CODE_4000 || code == Constants.HTTP_CODE_5000) {
                            if(!NetworkUtil.isNetworkAvailable(RegisterThirdActivity.this)){
                                NetworkUtil.httpNetErrTip(RegisterThirdActivity.this, myLayout);
                            }
                        } else {
                            errorProcess(msg);
                        }
                    }
                }) .addBodyParam("type",oauthtype)
                .addBodyParam("nickname", nickName)
                .addBodyParam("avatar",headUrl)
                .addBodyParam("mobile", account)
                .addBodyParam("password", password)
                .addBodyParam("register_code",code)
                .addBodyParam("open_id",openId)
                .build();
    }

    /**
     * 提交修改密码
     * @param password
     * @param confirm_password
     */
    private void commitChangePassword(String password, String confirm_password) {
        new HttpPostBuilder(this)
                .setUrl(UrlsNew.POST_RESET_PASSWORD)
                .setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
                        if(dialog != null && dialog.isShowing()){
                            dialog.cancel();
                        }
                        setResult(3);
                        finish();
                    }
                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {
                        if(dialog != null && dialog.isShowing()){
                            dialog.cancel();
                        }
                        if (code == Constants.HTTP_CODE_4000 || code == Constants.HTTP_CODE_5000) {
                            if(!NetworkUtil.isNetworkAvailable(RegisterThirdActivity.this)){
                                NetworkUtil.httpNetErrTip(RegisterThirdActivity.this, myLayout);
                            }
                        }
                        errorProcess(msg);
                    }
                })
                .setClassObj(null)
                .addBodyParam("account", account)
                .addBodyParam("password", password)
                .addBodyParam("confirm_password",confirm_password)
                .addBodyParam("reset_password_code",code)
                .build();

    }

    private void errorProcess(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            ToastUtils.makeText(RegisterThirdActivity.this, msg, Toast.LENGTH_SHORT).show();
        }
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

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
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

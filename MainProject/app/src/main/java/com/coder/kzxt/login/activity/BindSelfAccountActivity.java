package com.coder.kzxt.login.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.app.http.HttpCallBack;
import com.app.http.UrlsNew;
import com.app.http.builders.HttpGetBuilder;
import com.app.http.builders.HttpPostBuilder;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.base.beans.UserInfoBean;
import com.coder.kzxt.dialog.util.MyPublicDialog;
import com.coder.kzxt.im.business.ImLoginBusiness;
import com.coder.kzxt.login.beans.UserInfoResult;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.DateUtil;
import com.coder.kzxt.utils.GlideUtils;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.PublicUtils;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.coder.kzxt.utils.ToastUtils;
import com.coder.kzxt.views.VerifiCodeView;

import cn.sharesdk.framework.ShareSDK;

/**
 * 第三方绑定已有账号
 */

public class BindSelfAccountActivity extends BaseActivity implements HttpCallBack {

    private Button btn_login_submit;
    private EditText et_login_email;
    private EditText et_login_password;
    private SharedPreferencesUtil spu;
    private String email;
    private String password;
    private String phoneCode;
    private RelativeLayout delete_login;
    private RelativeLayout open_password_rl;
    private ImageView open_password;
    private View iv_pwd_line;
    private TextView register;
    private Context context;
    private Dialog dialog;
    private TextView forget_password;
    boolean click = true;//查看密码
    boolean hasAccount = false;
    boolean hasPwd = false;
    boolean accountFocus = false;
    boolean pwdFocus = false;//是否获取了焦点
    private ImageView img_head;
    private Toolbar mToolbarView;
    private RelativeLayout mylayout;
    private RelativeLayout rl_auth_code; //验证码
    private EditText et_auth_code; //输入验证码
    private RelativeLayout rl_code;  //点击刷新
    private ImageView iv_auth_code; //放置图片验证码
    private int count = 0;  //记录用户输错次数
    //产生的验证码
    private String realCode;
    private String headUrl;

    private String type,openId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_self_account);
        overridePendingTransition(R.anim.login_up_in, R.anim.login_stay);
        context = this;
        spu = new SharedPreferencesUtil(this);
        type=getIntent().getStringExtra("type");
        openId=getIntent().getStringExtra("openId");
        initView();
        initType();
        initListenner();
    }

    private void initView() {
        mToolbarView = (Toolbar) findViewById(R.id.toolbar);
        mToolbarView.setTitle("绑定账号");

        setSupportActionBar(mToolbarView);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // 输入的手机号或邮箱号
        et_login_email = (EditText) findViewById(R.id.et_login_email);
        et_login_password = (EditText) findViewById(R.id.et_login_password);
        //删除已输入的账号
        delete_login = (RelativeLayout) findViewById(R.id.delete_login);
        //查看密码
        open_password_rl = (RelativeLayout) findViewById(R.id.open_password_rl);
        open_password = (ImageView) findViewById(R.id.open_password);
        //登录
        btn_login_submit = (Button) findViewById(R.id.btn_login_submit);

        btn_login_submit.setAlpha(0.5f);
        btn_login_submit.setClickable(false);
        // 注册
        register = (TextView) findViewById(R.id.register);
        // 忘记密码
        forget_password = (TextView) findViewById(R.id.forget_password);
        img_head = (ImageView) findViewById(R.id.img_head);
        headUrl = spu.getUserHead();
        if (!TextUtils.isEmpty(headUrl)) {
            GlideUtils.loadCircleHeaderOfCommon(BindSelfAccountActivity.this, headUrl, img_head);
        }

        iv_pwd_line = findViewById(R.id.iv_pwd_line);
        mylayout = (RelativeLayout) findViewById(R.id.rl_layout);
        rl_auth_code = (RelativeLayout) findViewById(R.id.rl_auth_code);
        et_auth_code = (EditText) findViewById(R.id.et_auth_code);
        rl_code = (RelativeLayout) findViewById(R.id.rl_code);
        iv_auth_code = (ImageView) findViewById(R.id.iv_auth_code);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (NetworkUtil.isNetworkAvailable(this)) {
                    loginHideSoftInputWindow();
                }
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initType() {
        String account = spu.getUserAccount();
        if (!TextUtils.isEmpty(account)) {
            et_login_email.setText(account);
            delete_login.setVisibility(View.VISIBLE);
            et_login_email.setSelection(et_login_email.getText().length());
            hasAccount = true;
        } else {
            hasAccount = false;
        }

        isLogin();
    }


    private void initListenner() {

        et_login_email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    accountFocus = true;
                } else {
                    accountFocus = false;
                }
            }
        });
        et_login_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    pwdFocus = true;
                } else {
                    pwdFocus = false;
                }
            }
        });

        et_login_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s.toString().trim()) || s.length() == 0) {
                    delete_login.setVisibility(View.INVISIBLE);
                    hasAccount = false;
                } else {
                    delete_login.setVisibility(View.VISIBLE);
                    hasAccount = true;
                }
                isLogin();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString().trim()) || s.length() == 0) {
                    delete_login.setVisibility(View.INVISIBLE);
                    hasAccount = false;
                } else {
                    delete_login.setVisibility(View.VISIBLE);
                    hasAccount = true;
                }
                isLogin();
            }
        });
        et_login_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s.toString().trim()) || s.length() == 0) {
                    hasPwd = false;
                } else {
                    if (s.length() >= 6) {
                        hasPwd = true;
                    }
                }
                isLogin();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString().trim()) || s.length() == 0) {
                    hasPwd = false;
                } else {
                    if (s.length() >= 6) {
                        hasPwd = true;
                    }
                }
                isLogin();
            }
        });
        delete_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_login_email.setText("");
            }
        });
        open_password_rl.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (click) {
                    open_password.setImageResource(R.drawable.open_password);
                    et_login_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    click = false;
                } else {
                    open_password.setImageResource(R.drawable.close_password);
                    et_login_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    click = true;
                }
            }
        });


        // 登录
        btn_login_submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (btn_login_submit.getAlpha() == 0.5f) {
                    return;
                }
                email = et_login_email.getText().toString();
                password = et_login_password.getText().toString();
                phoneCode = et_auth_code.getText().toString().toLowerCase();
                if (count >= 3) {
                    if (TextUtils.isEmpty(phoneCode)) {
                        ToastUtils.makeText(context, getResources().getString(R.string.input_auth_code_hint), Toast.LENGTH_SHORT).show();
                        return;
                    } else if (!phoneCode.equals(realCode)) {
                        ToastUtils.makeText(context, getResources().getString(R.string.error_code), Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                if (NetworkUtil.isNetworkAvailable(BindSelfAccountActivity.this)) {
                    if (TextUtils.isEmpty(email)) {
                        et_login_email.setHintTextColor(getResources().getColor(R.color.font_red));
                        return;
                    } else if (TextUtils.isEmpty(password)) {
                        et_login_password.setHintTextColor(getResources().getColor(R.color.font_red));
                        return;
                    } else {
                        // 获得登录后的信息
                        dialog = MyPublicDialog.createLoadingDialog(BindSelfAccountActivity.this);
                        dialog.show();
                        //点击登录 隐藏键盘
                        loginHideSoftInputWindow();
                        goToLogin();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.net_inAvailable), Toast.LENGTH_SHORT).show();
                }
            }

        });

        // 忘记密码
        forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BindSelfAccountActivity.this, ForgetPassword.class);
                startActivityForResult(intent, 1);
            }
        });
        // 注册界面
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                    finish();
//                Intent intent = new Intent(BindSelfAccountActivity.this, RegisterPhoneActivity.class);
//                startActivityForResult(intent, 1);
            }
        });
        //点击验证码区域刷新
        rl_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createVerifiCode();
            }
        });

    }

    /**
     * 生成验证码
     */
    private void createVerifiCode() {
        iv_auth_code.setImageBitmap(VerifiCodeView.getInstance().createBitmap());
        realCode = VerifiCodeView.getInstance().getCode().toLowerCase();
    }

    /**
     * 去登录
     */
    private void goToLogin() {
        String userName = et_login_email.getText().toString().trim();
        String password = et_login_password.getText().toString().trim();
        new HttpPostBuilder(this)
                .setUrl(UrlsNew.POST_SOCIAL_ACCOUNT_BIND)              // 设置url   包含queryParams
                .setHttpResult(this)                // 回调 现在有两个 success fail
                .setClassObj(null)
                .addBodyParam("type",type)
                .addBodyParam("open_id",openId)
                .addBodyParam("account", userName)         // 添加参数是query类型参数 无序 可以添加多个
                .addBodyParam("password", password)
                .addBodyParam("auto_login", "0")
                .build();                           // 最后需要执行build方法

    }


    /**
     * 获取用户信息
     */
    public void getUserInfo() {
        new HttpGetBuilder(this)
                .setHttpResult(this)
                .setClassObj(UserInfoResult.class)
                .setUrl(UrlsNew.USER_PROFILE)
                .setRequestCode(1000)
                .build();
    }


    /**
     * 登录成功后隐藏软键盘
     */
    private void loginHideSoftInputWindow() {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isActive(et_login_email) || inputMethodManager.isActive(et_login_password)
                || getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED) {
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    //登录按钮的状态
    private void isLogin() {
        if (hasAccount && hasPwd) {
            btn_login_submit.setClickable(true);
            btn_login_submit.setAlpha(1.0f);
        } else {
            btn_login_submit.setClickable(false);
            btn_login_submit.setLongClickable(false);
            btn_login_submit.setAlpha(0.5f);
        }
    }


    /**
     * 异常处理
     *
     * @param msg
     */
    private void errorProcess(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            ToastUtils.makeText(BindSelfAccountActivity.this, msg, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (dialog != null && dialog.isShowing()) {
            dialog.isShowing();
        }
    }

    @Override
    protected void onResume() {
        if (dialog != null && dialog.isShowing()) {
            dialog.cancel();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (dialog != null && dialog.isShowing()) {
            dialog.cancel();
        }
        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        if (requestCode == 1 && resultCode == 1) {//未登录
            setResult(1);
            finish();
        } else if (requestCode == 1 && resultCode == 2) {//登陆成功
            setResult(2);
            finish();
        } else if (requestCode == 3 && resultCode == 3)

        {
        } else if (requestCode == 1 && resultCode == 3) {//注册或找回密码成功返回
            onResume();
            et_login_password.setText("");
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (dialog != null && dialog.isShowing()) {
            dialog.cancel();
        }
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            finish();
            return true;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        ShareSDK.stopSDK(this);
        super.onDestroy();
    }

    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(R.anim.login_stay, R.anim.login_down_out);
    }

    @Override
    public void setOnSuccessCallback(int requestCode, Object resultBean) {
        count = 0;
        if (requestCode == 1000) {
            if (dialog != null && dialog.isShowing()) {
                dialog.cancel();
            }
            UserInfoBean item = ((UserInfoResult) resultBean).getItem();
            saveLoginData(item);
        } else if (requestCode == 1001) {
//            setResult(Constants.LOGIN_BACK);
//            finish();
        } else {
            getUserInfo();
            new ImLoginBusiness()
                    .setRequestCode(1001)
                    .setHttpCallBack(this)
                    .login();

        }
    }
    protected void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.custom_dialog);
        if(type.equals("qq")){
            builder.setMessage(et_login_email.getText().toString().trim()+"已经与您QQ号成功关联，该账号和您的QQ号都可以作为登陆账号");
        }else {
            builder.setMessage(et_login_email.getText().toString().trim()+"已经与您微信号成功关联，该账号和您的微信号都可以作为登陆账号");
        }
        builder.setTitle("提示");
        builder.setPositiveButton("朕知道了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                setResult(2);
                finish();
            }
        });

        builder.create().show();
    }
    private void saveLoginData(UserInfoBean info) {
        UserInfoBean.ProfileBean profile = info.getProfile();

        spu.setUid(info.getId());
        spu.setNickName(profile.getNickname());
        spu.setUserHead(profile.getAvatar()); //用户头像
        spu.setBigHead(profile.getBig_avatar());//大头像
        String birthday = profile.getBirthday();
        if (!TextUtils.isEmpty(birthday)) {
            if (!birthday.contains("-")) {
                long b = Integer.parseInt(birthday);
                String bir = DateUtil.getYearMonthAndDay(b);
                spu.setBirthDay(bir);
            } else {
                spu.setBirthDay(birthday);
            }
        } else {
            spu.setBirthDay("1970-01-01");
        }
        if (!TextUtils.isEmpty(info.getMobile())) {
            spu.setUserAccount(info.getMobile());
        } else if (!TextUtils.isEmpty(info.getEmail())) {
            spu.setUserAccount(info.getEmail());
        }
        spu.setMobile(info.getMobile());
        spu.setEmail(info.getEmail());
        spu.setRegisterType(info.getRegister_type()); //注册类型
        spu.setCreateTime(info.getCreate_time());
        spu.setGender(profile.getGender()); //性别
        spu.setDesc(profile.getDesc());
        spu.setRemark(profile.getRemark());
        spu.setIsLogin("1");
        //"是否老师:0.否 1.是"
        spu.setUserRole(info.getIs_teacher());
        sendBroadcast(new Intent().setAction(Constants.LOGIN_NOTICE));

        //因为oppo手机这里会卡住，登录后开启百度服务这里放在一个新线程里。
        new Thread(new Runnable() {
            @Override
            public void run() {
                PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY, PublicUtils.getMetaValue(BindSelfAccountActivity.this, "api_key"));
            }
        }).start();
        dialog();
    }

    @Override
    public void setOnErrorCallback(int requestCode, int code, String msg) {
        if (dialog != null && dialog.isShowing()) {
            dialog.cancel();
        }
        count++;
        if (count >= 3) {
            rl_auth_code.setVisibility(View.VISIBLE);
            iv_pwd_line.setVisibility(View.GONE);
            createVerifiCode();
        } else {
            iv_pwd_line.setVisibility(View.VISIBLE);
            rl_auth_code.setVisibility(View.GONE);
        }
        if (code == Constants.HTTP_CODE_4000 || code == Constants.HTTP_CODE_5000) {
            if (!NetworkUtil.isNetworkAvailable(BindSelfAccountActivity.this)) {
                NetworkUtil.httpNetErrTip(BindSelfAccountActivity.this, mylayout);
            } else {

            }
        }
        errorProcess(msg);
    }

}

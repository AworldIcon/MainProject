package com.coder.kzxt.login.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
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
import com.app.utils.L;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.base.beans.UserInfoBean;
import com.coder.kzxt.dialog.util.MyPublicDialog;
import com.coder.kzxt.im.business.ImLoginBusiness;
import com.coder.kzxt.login.beans.LoginResultBean;
import com.coder.kzxt.login.beans.UserInfoResult;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.DateUtil;
import com.coder.kzxt.utils.GlideUtils;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.PublicUtils;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.coder.kzxt.utils.ToastUtils;
import com.coder.kzxt.views.VerifiCodeView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * 个人登录
 */

public class LoginActivity extends BaseActivity implements HttpCallBack,PlatformActionListener {

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
    private RelativeLayout qq_login_rl;
    private RelativeLayout wx_login_rl;
    private Context context;
    private Dialog dialog;
    private TextView forget_password;

    boolean click = true;//查看密码
    boolean hasAccount = false;
    boolean hasPwd = false;
    boolean accountFocus = false;
    boolean pwdFocus = false;//是否获取了焦点
    private ImageView img_head;

    private String oauthName;
    private String oauthUserId;
    private String oauthGender;
    private String oauthImage;
    private String oauthTokenId;
    private String oauthType ;
    private String unionid;
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
    private RelativeLayout third_party_login; //三方登录
    private boolean clickqq = false;
    private boolean clickwx = false;
    private static final int ON_COMPLETE = 0;
    private static final int ON_ERROR = 1;
    private static final int ON_CANCEL = 2;
    private boolean isShowLoad=false;
    private Handler tencentHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case ON_COMPLETE:
                    // 执行相关业务逻辑操作
                    if (clickqq) {
                        oauthName = new QQ(context).getDb().getUserName();
                        oauthUserId = new QQ(context).getDb().getUserId();
                        oauthGender = new QQ(context).getDb().getUserGender();
                        oauthImage = new QQ(context).getDb().getUserIcon();
                        oauthTokenId = new QQ(context).getDb().getToken();
                        oauthType = "qq";
                        //调用服务器接口,第三方登录(本地服务器)
                        L.d("zw--qq",oauthName+"--"+oauthGender+"--"+oauthImage+"****"+new QQ(context).getDb().get("figureurl_qq_2"));
                        clickqq = false;
                        loginThirdRequest(oauthType,oauthUserId);
                    } else if (clickwx) {
                        oauthName = new Wechat(context).getDb().getUserName();
                        unionid = new Wechat(context).getDb().get("unionid");
                        oauthGender = new Wechat(context).getDb().getUserGender();
                        oauthImage = new Wechat(context).getDb().getUserIcon();
                        oauthTokenId = new Wechat(context).getDb().getToken();
                        oauthType = "wechat";
                        clickwx = false;
                        L.d("zw--wechat",oauthName+"--"+oauthGender+"--"+oauthImage);
                        //调用服务器接口,第三方登录(本地服务器)
                        loginThirdRequest(oauthType,unionid);
                    }
                    L.d("zw--qq--back",oauthUserId+"--userid--"+oauthTokenId+"--token");
                    break;
                case ON_ERROR:
                    clickqq = false;
                    clickwx = false;
                    if(dialog != null && dialog.isShowing()) {
                        dialog.cancel();
                    }
                    if (msg.arg1 == ((Platform)msg.obj).ACTION_USER_INFOR) {
                        ToastUtils.makeText(LoginActivity.this, "授权操作遇到错误",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case ON_CANCEL:
                    clickqq = false;
                    clickwx = false;
                    if(dialog != null && dialog.isShowing()) {
                        dialog.cancel();
                    }
                    if (msg.arg1 == ((Platform)msg.obj).ACTION_USER_INFOR) {
                        ToastUtils.makeText(LoginActivity.this, "授权操作已取消", Toast.LENGTH_SHORT).show();
                    }
                    break;

            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_login);
        overridePendingTransition(R.anim.login_up_in, R.anim.login_stay);
        context = this;
        spu = new SharedPreferencesUtil(this);

        initShareSdk();
        initView();
        initType();
        initListenner();
    }

    private void initShareSdk(){
        ShareSDK.initSDK(this,Constants.SHARESDKEY);
        HashMap<String,Object> qqMap = new HashMap<String, Object>();
        qqMap.put("Id","1");
        qqMap.put("SortId","1");
        qqMap.put("AppId","1104585641");
        qqMap.put("AppSecret","0W6B3F3Je3udsNQr");
        qqMap.put("ShareByAppClient","true");
        qqMap.put("Enable","true");
        ShareSDK.setPlatformDevInfo(QQ.NAME,qqMap);

        HashMap<String,Object> wxMap = new HashMap<String, Object>();
        wxMap.put("Id","2");
        wxMap.put("SortId","2");
        wxMap.put("AppId","wx7928b2d447773c91");
        wxMap.put("AppSecret","2c617802faf35644b19162e75ee7fa24");
//        wxMap.put("AppId",spu.getWXID());
//        wxMap.put("AppSecret",spu.getWXSK());
        wxMap.put("BypassApproval","false");
        wxMap.put("Enable","true");
        ShareSDK.setPlatformDevInfo(Wechat.NAME,wxMap);
    }


    private void initView() {
        mToolbarView = (Toolbar) findViewById(R.id.toolbar);
        mToolbarView.setTitle(R.string.login);

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
            GlideUtils.loadCircleHeaderOfCommon(LoginActivity.this, headUrl, img_head);
        }

        iv_pwd_line = findViewById(R.id.iv_pwd_line);
        third_party_login = (RelativeLayout) findViewById(R.id.third_party_login);
        qq_login_rl = (RelativeLayout) findViewById(R.id.qq_login_rl);
        wx_login_rl = (RelativeLayout) findViewById(R.id.wx_login_rl);

        mylayout = (RelativeLayout) findViewById(R.id.rl_layout);
        rl_auth_code = (RelativeLayout) findViewById(R.id.rl_auth_code);
        et_auth_code = (EditText) findViewById(R.id.et_auth_code);
        rl_code = (RelativeLayout) findViewById(R.id.rl_code);
        iv_auth_code = (ImageView) findViewById(R.id.iv_auth_code);


        if(spu.getShowQLogin().equals("1")){
            third_party_login.setVisibility(View.VISIBLE);
            qq_login_rl.setVisibility(View.VISIBLE);
        }else {
            qq_login_rl.setVisibility(View.GONE);
        }
        if(spu.getShowWLogin().equals("1")){
            third_party_login.setVisibility(View.VISIBLE);
            wx_login_rl.setVisibility(View.VISIBLE);
        }else {
            wx_login_rl.setVisibility(View.GONE);
        }

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

                if (NetworkUtil.isNetworkAvailable(LoginActivity.this)) {
                    if (TextUtils.isEmpty(email)) {
                        et_login_email.setHintTextColor(getResources().getColor(R.color.font_red));
                        return;
                    } else if (TextUtils.isEmpty(password)) {
                        et_login_password.setHintTextColor(getResources().getColor(R.color.font_red));
                        return;
                    } else {
                        // 获得登录后的信息
                        dialog = MyPublicDialog.createLoadingDialog(LoginActivity.this);
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
                Intent intent = new Intent(LoginActivity.this, ForgetPassword.class);
                startActivityForResult(intent, 1);
            }
        });
        // 注册界面
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(LoginActivity.this, RegisterPhoneActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        //QQ登录
        qq_login_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                loginThirdRequest("qq","89508C2823AEC12084C3E9452CFC083B");
                clickqq = true;
                dialog = MyPublicDialog.createLoadingDialog(LoginActivity.this);
                dialog.show();
                Platform platform = ShareSDK.getPlatform(QQ.NAME);
                platform.SSOSetting(false);
                login(platform);
            }
        });
        //微信登录
        wx_login_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickwx = true;
                // 获取平台
                Platform plat = ShareSDK.getPlatform(Wechat.NAME);
                plat.SSOSetting(true);
                if(isWeixinAvilible(context)){
                    dialog = MyPublicDialog.createLoadingDialog(LoginActivity.this);
                    dialog.show();
                    login(plat);
                }else{
                    ToastUtils.makeText(context,"请安装微信后再登录！",Toast.LENGTH_SHORT).show();
                    return;
                }
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


    public  boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }

        return false;
    }

    private void login(Platform platform){
        isShowLoad=true;
        platform.setPlatformActionListener(this);
        if (platform.isAuthValid()){//是否授权登录了
            platform.removeAccount(true);
            ShareSDK.removeCookieOnAuthorize(true);
        }
        if((!spu.getWXID().startsWith("wx") || spu.getWXID().length() != 18)){
            Toast.makeText(this, "授权操作遇到错误",   Toast.LENGTH_SHORT).show();
            if(dialog != null && dialog.isShowing()) {
                dialog.cancel();
            }
        }else{
            platform.showUser(null);
        }
    }
    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        tencentHandler.sendEmptyMessage(ON_COMPLETE);
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        Message msg = tencentHandler.obtainMessage();
        msg.what = ON_ERROR;
        msg.arg1 = i;
        msg.obj=platform;
        tencentHandler.sendMessage(msg);
        throwable.printStackTrace();
    }

    @Override
    public void onCancel(Platform platform, int i) {
        Message msg = tencentHandler.obtainMessage();
        msg.what = ON_CANCEL;
        msg.arg1 = i;
        msg.obj=platform;
        tencentHandler.sendMessage(msg);
    }

    private void loginThirdRequest(final String type, final String openId) {
        new HttpPostBuilder(this).setUrl(UrlsNew.POST_SOCIAL_ACCOUNT).setClassObj(null).setHttpResult(new HttpCallBack() {
            @Override
            public void setOnSuccessCallback(int requestCode, Object resultBean) {
                if(resultBean==null){
                    return;
                }
                L.d("zw--qq--back-su",resultBean.toString());
                JSONObject jsonObject= null;
                try {
                    jsonObject = new JSONObject(resultBean.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if(jsonObject.getJSONObject("item").getInt("status")==2){
                        if(dialog != null && dialog.isShowing()) {
                            dialog.cancel();
                        }
                        Intent intent=new Intent(LoginActivity.this,TencentLoginActivity.class);
                        intent.putExtra("type",type);
                        intent.putExtra("openId",openId);
                        startActivityForResult(intent,1);
                    }else {
                        getUserInfo();
                        new ImLoginBusiness()
                                .setRequestCode(1001)
                                .setHttpCallBack(this)
                                .login();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void setOnErrorCallback(int requestCode, int code, String msg) {
                ToastUtils.makeText(LoginActivity.this,msg);

            }
        }).addBodyParam("type",type)
                .addBodyParam("open_id",openId)
                .build();
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
                .setUrl(UrlsNew.LOGIN)              // 设置url   包含queryParams
                .setHttpResult(this)                // 回调 现在有两个 success fail
                .setClassObj(LoginResultBean.class)
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
            ToastUtils.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
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
        if (!isShowLoad&&dialog != null && dialog.isShowing()) {
            dialog.cancel();
        }else if(isShowLoad){
            dialog = MyPublicDialog.createLoadingDialog(LoginActivity.this);
            dialog.show();
            isShowLoad=false;
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

        } else {
            getUserInfo();
            new ImLoginBusiness()
                    .setRequestCode(1001)
                    .setHttpCallBack(this)
                    .login();

        }
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
                PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY, PublicUtils.getMetaValue(LoginActivity.this, "api_key"));
            }
        }).start();
        setResult(Constants.LOGIN_BACK);
        finish();
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
            if (!NetworkUtil.isNetworkAvailable(LoginActivity.this)) {
                NetworkUtil.httpNetErrTip(LoginActivity.this, mylayout);
            } else {

            }
        }
        errorProcess(msg);
    }

}

//package com.coder.kzxt.login.activity;
//
//import android.app.Dialog;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v7.widget.Toolbar;
//import android.text.Editable;
//import android.text.TextUtils;
//import android.text.TextWatcher;
//import android.text.method.HideReturnsTransformationMethod;
//import android.text.method.PasswordTransformationMethod;
//import android.view.KeyEvent;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.WindowManager;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.app.http.HttpPostOld;
//import com.app.http.InterfaceHttpResult;
//import com.coder.kzxt.activity.R;
//import com.coder.kzxt.dialog.util.MyPublicDialog;
//import com.coder.kzxt.login.beans.LoginDataBean;
//import com.coder.kzxt.base.activity.BaseActivity;
//import com.coder.kzxt.utils.Constants;
//import com.coder.kzxt.utils.NetworkUtil;
//import com.coder.kzxt.utils.SharedPreferencesUtil;
//import com.coder.kzxt.utils.Urls;
//
//import java.util.ArrayList;
//
///**
// * 云登录
// */
//
//public class YunLoginActivity extends BaseActivity implements InterfaceHttpResult{
//
//    private Dialog dialog;
//    private Button btn_login_submit;
//    private EditText et_login_email;
//    private EditText et_login_password;
//    private SharedPreferencesUtil spu;
//    private String email;
//    private String password;
//    private RelativeLayout delete_login;
//    private RelativeLayout open_password_rl;
//    private ImageView open_password;
//    private View iv_login_line;
//    private View iv_pwd_line;
//    private TextView forget_password;
//    boolean click=true;//查看密码
//    boolean hasAccount=false;
//    boolean hasPwd=false;
//    boolean accountFocus=false;
//    boolean pwdFocus=false;//是否获取了焦点
//    private Toolbar mToolbarView;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//
//        super.onCreate(savedInstanceState);
//        this.overridePendingTransition(R.anim.login_up_in,R.anim.login_stay);
//        setContentView(R.layout.activity_yun_login);
////        MyApplication.list.add(this);
//        spu = new SharedPreferencesUtil(this);
//        initView();
//        initListener();
//     }
//
//    private void initView(){
//        mToolbarView = (Toolbar) findViewById(R.id.toolbar);
//        mToolbarView.setTitle(getResources().getString(R.string.yun_league_login));
//        setSupportActionBar(mToolbarView);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        // 输入的手机号或邮箱号
//        et_login_email = (EditText) findViewById(R.id.et_login_email);
//        et_login_password = (EditText) findViewById(R.id.et_login_password);
//        //删除已输入的账号
//        delete_login= (RelativeLayout) findViewById(R.id.delete_login);
//        //查看密码
//        open_password_rl= (RelativeLayout) findViewById(R.id.open_password_rl);
//        open_password= (ImageView) findViewById(R.id.open_password);
//        //登录
//        btn_login_submit = (Button) findViewById(R.id.btn_login_submit);
//        btn_login_submit.setAlpha(0.5f);
//        btn_login_submit.setLongClickable(false);
//        btn_login_submit.setClickable(false);
//        // 忘记密码    目前没有云账号找回流程，暂时关闭，已有的页面勿动
//        forget_password = (TextView) findViewById(R.id.forget_password);
//        forget_password.setVisibility(View.GONE);
//        forget_password.setClickable(false);
//
//        iv_login_line=findViewById(R.id.iv_login_line);
//        iv_pwd_line=findViewById(R.id.iv_pwd_line);
//    }
//    private void initListener(){
//        et_login_email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if(hasFocus){
//                    accountFocus=true;
//                }else {
//                    accountFocus=false;
//                }
//                isChange();
//            }
//        });
//        et_login_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if(hasFocus){
//                    pwdFocus=true;
//                }else {
//                    pwdFocus=false;
//                }
//                isChange();
//            }
//        });
//        et_login_email.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if(TextUtils.isEmpty(s.toString().trim())||s.length()==0){
//                    delete_login.setVisibility(View.INVISIBLE);
//                    hasAccount=false;
//                }else {
//                    delete_login.setVisibility(View.VISIBLE);
//                    hasAccount=true;
//                }
//                isLogin();
//                isChange();
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if(TextUtils.isEmpty(s.toString().trim())||s.length()==0){
//                    delete_login.setVisibility(View.INVISIBLE);
//                    hasAccount=false;
//                }else {
//                    delete_login.setVisibility(View.VISIBLE);
//                    hasAccount=true;
//                }
//                isLogin();
//                isChange();
//            }
//        });
//        et_login_password.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if(TextUtils.isEmpty(s.toString().trim())||s.length()==0||s.length()<5){
//                    hasPwd=false;
//                }else {
//                    hasPwd=true;
//                }
//                isLogin();
//                isChange();
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if(TextUtils.isEmpty(s.toString().trim())||s.length()==0||s.length()<5){
//                    hasPwd=false;
//                }else {
//                    hasPwd=true;
//                }
//                isLogin();
//                isChange();
//            }
//        });
//        delete_login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                et_login_email.setText("");
//            }
//        });
//        open_password_rl.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                if(click){
//                    open_password.setImageResource(R.drawable.open_password);
//                    et_login_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
//                    click=false;
//                }else{
//                    open_password.setImageResource(R.drawable.close_password);
//                    et_login_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
//                    click=true;
//                }
//            }
//        });
//
//
//        // 登录
//        btn_login_submit.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                if(btn_login_submit.getAlpha() == 0.5f){
//                    return;
//                }
//                email = et_login_email.getText().toString();
//                password = et_login_password.getText().toString();
////                StatService.onEvent(getApplicationContext(), "UserLogin", getResources().getString(R.string.login), 1);
//                if (NetworkUtil.isNetworkAvailable(YunLoginActivity.this)) {
//                    // 获得登录后的信息
//                    dialog = MyPublicDialog.createLoadingDialog(YunLoginActivity.this);
//                    dialog.show();
//                    goToLogin(email,password);
//                } else {
//                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.net_inAvailable), Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//        // 忘记密码 暂无此功能
////        forget_password.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                Intent intent = new Intent(YunLoginActivity.this, YunLoginPhoneActivity.class);
////                startActivityForResult(intent,1);
////            }
////        });
//    }
//
//    private void goToLogin(String userName, String password) {
//        new HttpPostOld(this,this, YunLoginActivity.this, LoginDataBean.class,
//                Urls.POST_LOGIN_ACTION,userName,password,"0",spu.getDevicedId()).excute(1000);
//    }
//
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                //判断软件盘是否显示，需要先隐藏掉
//                hideSoftInputWindow();
//                setResult(1);
//                finish();
//                break;
//            default:
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    private void hideSoftInputWindow( ) {
//        final InputMethodManager inputMethodManager = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
//        if(inputMethodManager.isActive(et_login_email) || inputMethodManager.isActive(et_login_password)
//                || getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED){
//            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//        }
//    }
//
//    //登录按钮的状态
//    private void isLogin() {
//        if(hasAccount&&hasPwd) {
//            btn_login_submit.setClickable(true);
//            btn_login_submit.setAlpha(1.0f);
//        }else {
//            btn_login_submit.setClickable(false);
//            btn_login_submit.setLongClickable(false);
//            btn_login_submit.setAlpha(0.5f);
//        }
//    }
//    //账号和密码下边线的状态
//    private void isChange(){
//        if(accountFocus||hasAccount){
//            iv_login_line.setBackgroundResource(R.color.font_blue);
//        }else{
//            iv_login_line.setBackgroundResource(R.color.font_gray);
//        }
//
//        if(pwdFocus||hasPwd){
//            iv_pwd_line.setBackgroundResource(R.color.font_blue);
//        }else {
//            iv_pwd_line.setBackgroundResource(R.color.font_gray);
//        }
//    }
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
//            finish();
//            return true;
//        }
//        return false;
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode==1&&resultCode==1){//找回密码返回
//            et_login_email.setText("");
//            et_login_password.setText("");
//        }else if(requestCode==1&&resultCode==2){//关联或新建本地账号成功返回值
//            setResult(1);
//            finish();
//        }else if(requestCode==1&&resultCode==3){//找回密码成功返回
//            onResume();
//            et_login_password.setText("");
//        }
//        if(dialog!=null&&dialog.isShowing()) {
//            dialog.dismiss();
//        }
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        /**
//         * 页面起始（每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加）
//         * 不能与StatService.onPageStart一级onPageEnd函数交叉使用
//         */
////        StatService.onResume(this);
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        /**
//         * 页面结束（每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加）
//         * 不能与StatService.onPageStart一级onPageEnd函数交叉使用
//         */
////        StatService.onPause(this);
//    }
//    @Override
//    protected void onDestroy() {
//        if (dialog != null && dialog.isShowing()) {
//            dialog.dismiss();
//        }
//        super.onDestroy();
//    }
//    @Override
//    public void finish() {
//        super.finish();
//        this.overridePendingTransition(R.anim.login_stay,R.anim.login_down_out);
//    }
//
//    @Override
//    public void getCallback(int requestCode, int code, String msg, Object baseBean) {
//        if (dialog != null && dialog.isShowing()) {
//            dialog.cancel();
//        }
//        if (requestCode == 1000 && code == Constants.HTTP_CODE_SUCCESS) {
//            LoginDataBean.LoginBean bean = ((LoginDataBean) baseBean).getData();
//            loginSuccessOperator(bean);
//        } else {
//            errorProcess(msg);
//        }
//    }
//
//    private void loginSuccessOperator(LoginDataBean.LoginBean bean) {
//        ArrayList<LoginDataBean.CloundCenterUnbindUser> cloudLoginUnbindLocalList = bean.getCloudLogin_unbindLocalUsers();
//        LoginDataBean.CloudCenterUser cloudLoginCenter = bean.getCloudLogin_centerUser();
//        LoginDataBean.CloundCenterBindUser cloudLoginBindCenter = bean.getCloudLogin_bindCenterUser();
//
//        if (bean.getIsCloudLogin().equals("1") && cloudLoginUnbindLocalList.size() != 0
//                          && cloudLoginCenter == null) {//云登录且未关联本地,则进行关联
//            Intent intent = new Intent();
//            intent.setClass(YunLoginActivity.this, RelateLocalActivity.class);
//            intent.putExtra("arrayList", cloudLoginUnbindLocalList);
//            intent.putExtra("centerBean",cloudLoginCenter);
//            startActivityForResult(intent, 1);
//        } else if (bean.getIsCloudLogin().equals("1") && cloudLoginUnbindLocalList.size() == 0
//                && cloudLoginCenter == null) {//云登录且未关联本地，未找到包含账号得信息的账号，则新建
//            Intent intent = new Intent();
//            intent.setClass(YunLoginActivity.this, CreateLocalActivity.class);
//            intent.putExtra("centerBean",cloudLoginCenter);
//            startActivityForResult(intent, 1);
//        } else if (bean.getIsCloudLogin().equals("1") && cloudLoginUnbindLocalList.size() != 0
//                && cloudLoginCenter != null) {//云登录后，发现本地账号已关联过，则需要先解绑,再看情况是否需要新建关联
//            Intent intent = new Intent();
//            intent.setClass(YunLoginActivity.this, UnbindLocalActivity.class);
//            intent.putExtra("centerBean", cloudLoginCenter);//登录的云账号
//            intent.putExtra("arrayList", cloudLoginUnbindLocalList);//可关联的本地账号(此种情况下，只有一条数据)
//            intent.putExtra("bindCenter", cloudLoginBindCenter);//和可关联的本地账号关联的云账号
//            startActivityForResult(intent, 1);
//        } else if (Integer.parseInt(bean.getOpenId()) > 0
//                || !bean.getOpenId().equals(null) || !bean.getOpenId().equals("")) {
//            saveLoginData(bean);
// //            new LoginImAsynTask(YunLoginActivity.this).execute();
//            setResult(1);
//            finish();
//        }
//
//    }
//
//    private void errorProcess(String msg) {
//        if (!TextUtils.isEmpty(msg)) {
//            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(getApplicationContext(), getResources().getString(R.string.net_inAvailable), Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    /**
//     * 保存登录数据到本地
//     * @param bean
//     */
//    private void saveLoginData(LoginDataBean.LoginBean bean) {
//        spu.setUid(bean.getUid());
//        spu.setOpenUid(bean.getOpenId());
//        spu.setIdPhoto(bean.getIdPhoto());
//        spu.setStudentNum(bean.getStudNum());
//        spu.setNickName(bean.getNickname());
//        spu.setMotto(bean.getSignature());
//        spu.setUserHead(bean.getUserface());
//        spu.setBannerUrl(bean.getBannerurl());
//        spu.setUserAccount(bean.getAccount());
//        spu.setUserType(bean.getUserType());
//        spu.setSex(bean.getSex());
//        spu.setBalance(bean.getCash());
//        spu.setCoin(bean.getGold());
//        spu.setPhone(bean.getMobile());
//        spu.setEmail(bean.getEmail());
//        spu.setDevicedId(bean.getDeviceId());
//    }
//
//}

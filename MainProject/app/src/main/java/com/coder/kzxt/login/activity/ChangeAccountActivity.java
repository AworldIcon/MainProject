package com.coder.kzxt.login.activity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.http.HttpCallBack;
import com.app.http.builders.HttpGetBuilder;
import com.app.http.builders.HttpPostBuilder;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.dialog.util.CustomDialog;
import com.coder.kzxt.dialog.util.MyPublicDialog;
import com.coder.kzxt.login.beans.LoginResultBean;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.ToastUtils;
import com.app.http.UrlsNew;
import com.coder.kzxt.utils.ValidatorUtil;

import java.util.Timer;
import java.util.TimerTask;


/**
 * 修改账号
 * Created by wangtingshun on 2017/3/31.
 */

public class ChangeAccountActivity extends BaseActivity implements HttpCallBack{

    private TextView describtion; //描述
    private LinearLayout changePhoneLayout;  //修改手机号
    private EditText et_phne;
    private RelativeLayout verifyLayout;  //验证码
    private LinearLayout passwordLayout;  //修改密码
    private Button btn_commit;   //提交
    private String type;  //用户类型 1.手机  2.邮箱  3.修改密码
    private Toolbar mToolbarView;
    private String account;
    private TextView tv_phone;
    private String accountNumber;//账号
    private Dialog dialog;
    private Button verifyButton;
    private TimeCount timeCount;
    private LinearLayout myLayout;
    private EditText et_verify_code; //验证码
    private String verifyCode;
    private boolean isAddPhone = false;
    private boolean isAddVerify = false;
    private final String VERIFY_TYPE = "0";   //验证码类型
    private final String PHONE_TYPE = "1"; //手机类型
    private final String EMAIL_TYPE = "2";  //邮箱类型
    private final String PASSWORD_TYPE = "3"; //密码类型
    private EditText etOriginalPassword; //原密码
    private EditText etNewPassword;  //新密码
    private EditText etAffirmPassword; //确认密码
    private String originalPassword;
    private String nerPassword;
    private String affirmPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_layout);
        type = getIntent().getStringExtra(Constants.USER_SAFE_TYPE);
        account = getIntent().getStringExtra(Constants.ACCOUNT_TYPE);
        initView();
        initListener();
    }

    private void initView() {
        mToolbarView = (Toolbar) findViewById(R.id.toolbar);
        describtion = (TextView) findViewById(R.id.tv_describtion);
        changePhoneLayout = (LinearLayout) findViewById(R.id.ll_change_phone);
        et_phne = (EditText) findViewById(R.id.et_phone);
        verifyLayout = (RelativeLayout) findViewById(R.id.rl_verify);
        passwordLayout = (LinearLayout) findViewById(R.id.ll_change_password);
        btn_commit = (Button) findViewById(R.id.btn_commit);
        tv_phone = (TextView) findViewById(R.id.tv_phone);
        verifyButton = (Button) findViewById(R.id.btn_verify_code);
        myLayout = (LinearLayout) findViewById(R.id.my_layout);
        et_verify_code = (EditText) findViewById(R.id.et_verify);
        etOriginalPassword = (EditText) findViewById(R.id.et_original_password);
        etNewPassword = (EditText) findViewById(R.id.et_new_password);
        etAffirmPassword = (EditText) findViewById(R.id.et_affirm_password);
        et_verify_code.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
        changeType(type);
        btn_commit.setAlpha(0.5f);
        btn_commit.setClickable(false);
        btn_commit.setLongClickable(false);
    }

    /**
     * 弹出软键盘
     */
    private void softInputWindow(final EditText editText) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                InputMethodManager  imm = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editText, 0);
            }
        }, 500);
    }

    private void changeType(String type) {
        if (type.equals(PHONE_TYPE)) {
            changePhoneLayout.setVisibility(View.VISIBLE);
            verifyLayout.setVisibility(View.VISIBLE);
            btn_commit.setVisibility(View.VISIBLE);
            et_phne.setInputType(InputType.TYPE_CLASS_PHONE);
            et_phne.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
//            String replace = account.replace(account.substring(5, account.length() - 3), "****");
            describtion.setText(getResources().getString(R.string.change_mobile_number) + account);
            mToolbarView.setTitle(getResources().getString(R.string.change_phone_number));
            softInputWindow(et_phne);
        } else if (type.equals(EMAIL_TYPE)) {
            changePhoneLayout.setVisibility(View.VISIBLE);
            verifyLayout.setVisibility(View.VISIBLE);
            btn_commit.setVisibility(View.VISIBLE);
            et_phne.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            et_phne.setHint(getResources().getString(R.string.input_new_email_hint));
            tv_phone.setText(getResources().getString(R.string.email_number));
            if(!TextUtils.isEmpty(account)){
                describtion.setText(getResources().getString(R.string.change_account_after)+ account);
            } else {
                describtion.setText(getResources().getString(R.string.change_email_account));
            }
            mToolbarView.setTitle(getResources().getString(R.string.change_email_number));
            softInputWindow(et_phne);
        } else if (type.equals(PASSWORD_TYPE)) {
            passwordLayout.setVisibility(View.VISIBLE);
            describtion.setText(getResources().getString(R.string.change_account_password));
            mToolbarView.setTitle(getResources().getString(R.string.change_password));
            softInputWindow(etOriginalPassword);
        }
        setSupportActionBar(mToolbarView);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void initListener() {

        et_phne.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if (TextUtils.isEmpty(s.toString().trim()) || s.length() == 0) {
                    isAddPhone = false;
                } else {
                    isAddPhone = true;
                }
                changeState();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et_verify_code.addTextChangedListener(new TextWatcher() {  //验证码
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if (TextUtils.isEmpty(s.toString().trim()) || s.length() == 0) {
                    isAddVerify = false;
                } else {
                    isAddVerify = true;
                }
                changeState();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //提交
        btn_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accountNumber = et_phne.getText().toString().trim();
                verifyCode = et_verify_code.getText().toString().trim();
                if (TextUtils.isEmpty(accountNumber)) {
                    et_phne.setError(getResources().getString(R.string.account_no_empty));
                    et_phne.setHintTextColor(getResources().getColor(R.color.font_red));
                    return;
                } else {
                    if (TextUtils.isEmpty(verifyCode)) {
                        ToastUtils.makeText(ChangeAccountActivity.this, getResources().getString(R.string.input_verify), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    commitChangeAccount(accountNumber);
                }
            }
        });

        //获取验证码
        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accountNumber = et_phne.getText().toString().trim();
                checkRegister();
            }
        });

        //右侧保存按钮
        mToolbarView.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.right_item:
                        save();
                        break;
                }
                return false;
            }
        });
    }

    //校验是否注册
    private void checkRegister() {
        if(type.equals(PHONE_TYPE)){
            if (TextUtils.isEmpty(accountNumber)) {
                et_phne.setError(getResources().getString(R.string.input_new_phone_hint));
                et_phne.setHintTextColor(getResources().getColor(R.color.font_red));
                return;
            }
            if (ValidatorUtil.isMobile(accountNumber)) {
                getPhoneUserList();
            } else {
                ToastUtils.makeText(ChangeAccountActivity.this, getResources().getString(R.string.phone_legal_number), Toast.LENGTH_SHORT).show();
            }
        } else if(type.equals(EMAIL_TYPE)){
            if (TextUtils.isEmpty(accountNumber)) {
                et_phne.setError(getResources().getString(R.string.input_new_email_hint));
                et_phne.setHintTextColor(getResources().getColor(R.color.font_red));
                return;
            }
            if (ValidatorUtil.isEmail(accountNumber)) {
                getEmailUserList();
            } else {
                ToastUtils.makeText(ChangeAccountActivity.this, getResources().getString(R.string.email_fomat_error), Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 保存按钮
     */
    private void save() {
        originalPassword = etOriginalPassword.getText().toString().trim();
        nerPassword = etNewPassword.getText().toString().trim();
        affirmPassword = etAffirmPassword.getText().toString().trim();
        if (TextUtils.isEmpty(originalPassword)) {
            ToastUtils.makeText(ChangeAccountActivity.this, "原密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        } else if (!(originalPassword.length() >= 6 && originalPassword.length() <= 16)) {
            ToastUtils.makeText(ChangeAccountActivity.this, "密码必须是6-16为密码区分大小", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(nerPassword)) {
            ToastUtils.makeText(ChangeAccountActivity.this, "新密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        } else if (!(nerPassword.length() >= 6 && nerPassword.length() <= 16)) {
            ToastUtils.makeText(ChangeAccountActivity.this, "密码必须是6-16为密码区分大小", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(affirmPassword)) {
            ToastUtils.makeText(ChangeAccountActivity.this, "确认密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        } else if (!(affirmPassword.length() >= 6 && affirmPassword.length() <= 16)) {
            ToastUtils.makeText(ChangeAccountActivity.this, "密码必须是6-16为密码区分大小", Toast.LENGTH_SHORT).show();
            return;
        } else if (!nerPassword.equals(affirmPassword)) {
            ToastUtils.makeText(ChangeAccountActivity.this, "新密码不一致", Toast.LENGTH_SHORT).show();
            return;
        } else {
            commitChangePassword(originalPassword, nerPassword, affirmPassword);
        }

    }

    /**
     * 提交修改密码
     * @param originalPassword
     * @param nerPassword
     * @param affirmPassword
     */
    private void commitChangePassword(String originalPassword, String nerPassword, String affirmPassword) {
        loadingDialog();
        new HttpPostBuilder(this)
                .setUrl(UrlsNew.POST_UPDATE_PASSWORD)
                .setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.cancel();
                        }
                        finish();
                        ToastUtils.makeText(ChangeAccountActivity.this,getResources().getString(R.string.change_password_success),Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.cancel();
                        }
                        if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
                            NetworkUtil.httpRestartLogin(ChangeAccountActivity.this, myLayout);
                        } else {
                            NetworkUtil.httpNetErrTip(ChangeAccountActivity.this, myLayout);
                        }
                        ToastUtils.makeText(ChangeAccountActivity.this,getResources().getString(R.string.change_password_fail),Toast.LENGTH_SHORT).show();
                    }
                })
                .setClassObj(null)
                .addBodyParam("password", originalPassword)
                .addBodyParam("new_password",nerPassword)
                .addBodyParam("confirm_password",affirmPassword)
                .build();

    }

    private void changeState() {
        if (isAddPhone && isAddVerify) {
            btn_commit.setAlpha(1.0f);
            btn_commit.setEnabled(true);
            btn_commit.setClickable(true);
        } else {
            btn_commit.setAlpha(0.5f);
            btn_commit.setClickable(false);
            btn_commit.setEnabled(false);
            btn_commit.setLongClickable(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.right_menu,menu);
        MenuItem item = menu.findItem(R.id.right_item);
        if(type.equals(PHONE_TYPE) || type.equals(EMAIL_TYPE)){
            item.setVisible(false);
        } else if(type.equals(PASSWORD_TYPE)){
            item.setVisible(true);
        }
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 提交更换账号
     */
    private void commitChangeAccount(String accountNumber) {
        if (type.equals(PHONE_TYPE)) {
            if (ValidatorUtil.isMobile(accountNumber)) {
                if (account.equals(accountNumber)) {
                    remindDialog(type,null);
                    return;
                }
                resetPhoneAccount();
            } else {
                ToastUtils.makeText(ChangeAccountActivity.this, getResources().getString(R.string.phone_legal_number), Toast.LENGTH_SHORT).show();
            }
        } else if (type.equals(EMAIL_TYPE)) {
            if (ValidatorUtil.isEmail(accountNumber)) {
                if (account.equals(accountNumber)) {
                    remindDialog(type,null);
                    return;
                }
                resetEmailAccount();
            } else {
                ToastUtils.makeText(ChangeAccountActivity.this, getResources().getString(R.string.email_fomat_error), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void remindDialog(String type,String verifyType) {
        final CustomDialog customDialog = new CustomDialog(this, R.layout.dlg_custom_view);
        TextView title = (TextView) customDialog.findViewById(R.id.title);
        TextView message = (TextView) customDialog.findViewById(R.id.message);
        TextView cancle = (TextView) customDialog.findViewById(R.id.leftTextView);
        TextView confirm = (TextView) customDialog.findViewById(R.id.rightTextView);
        cancle.setVisibility(View.GONE);
        title.setVisibility(View.GONE);
        if(TextUtils.isEmpty(verifyType)){
            if(type.equals(PHONE_TYPE)){
                message.setText(getResources().getString(R.string.phone_same_account));
            } else if(type.equals(EMAIL_TYPE)){
                message.setText(getResources().getString(R.string.email_same_account));
            }
        } else {
            if(type.equals(PHONE_TYPE)){
                message.setText("验证码已发送至你的手机"+accountNumber+" 请及时查看");
            } else if(type.equals(EMAIL_TYPE)){
                message.setText("一封验证码邮件已发送至"+accountNumber+"请登录你的邮箱查收");
            }
        }
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(customDialog != null && customDialog.isShowing()){
                    customDialog.cancel();
                }
            }
        });
        customDialog.show();
    }

    /**
     * 重置邮箱账号
     */
    private void resetEmailAccount() {
        loadingDialog();
        new HttpPostBuilder(this)
                .setUrl(UrlsNew.POST_RESET_EMAIL)
                .setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.cancel();
                        }
                        setResult(1);
                        finish();
                        ToastUtils.makeText(ChangeAccountActivity.this,getResources().getString(R.string.reset_email),Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.cancel();
                        }
                        if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
                            NetworkUtil.httpRestartLogin(ChangeAccountActivity.this, myLayout);
                        } else {
                            NetworkUtil.httpNetErrTip(ChangeAccountActivity.this, myLayout);
                        }
                    }
                })
                .setClassObj(null)
                .addBodyParam("email", accountNumber)
                .addBodyParam("reset_email_code",verifyCode)
                .build();
    }

    /**
     * 重置手机号
     */
    private void resetPhoneAccount() {
        loadingDialog();
        new HttpPostBuilder(this)
                .setUrl(UrlsNew.POST_RESET_PHONE)
                .setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.cancel();
                        }
                        setResult(1);
                        finish();
                        ToastUtils.makeText(ChangeAccountActivity.this,getResources().getString(R.string.reset_phone),Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.cancel();
                        }
                        if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
                            NetworkUtil.httpRestartLogin(ChangeAccountActivity.this, myLayout);
                        } else {
                            NetworkUtil.httpNetErrTip(ChangeAccountActivity.this, myLayout);
                        }
                        ToastUtils.makeText(ChangeAccountActivity.this,msg,Toast.LENGTH_SHORT).show();
                    }
                })
                .setClassObj(null)
                .addBodyParam("mobile", accountNumber)
                .addBodyParam("reset_mobile_code",verifyCode)
                .build();

    }

    private void loadingDialog() {
        dialog = MyPublicDialog.createLoadingDialog(ChangeAccountActivity.this);
        dialog.show();
    }

    /**
     * 获取用户信息列表（手机）
     */
    private void getPhoneUserList() {
        loadingDialog();
        new HttpGetBuilder(this)
                .setUrl(UrlsNew.GET_USER_LIST)
                .setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
                        if (requestCode == 0) {
                            LoginResultBean bean = (LoginResultBean) resultBean;
                            LoginResultBean.Paginate paginate = bean.getPaginate();
                            if (paginate.getTotalNum() == 0) {  //没有被注册
                                getVerifyCode();
                            } else {
                                if (dialog != null && dialog.isShowing()) {
                                    dialog.dismiss();
                                }
                                ToastUtils.makeText(ChangeAccountActivity.this, getResources().getString(R.string.already_register), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
                            NetworkUtil.httpRestartLogin(ChangeAccountActivity.this, myLayout);
                        } else {
                            NetworkUtil.httpNetErrTip(ChangeAccountActivity.this, myLayout);
                        }
                        ToastUtils.makeText(ChangeAccountActivity.this,msg,Toast.LENGTH_SHORT).show();
                    }
                })
                .setClassObj(LoginResultBean.class)
                .addQueryParams("mobile", accountNumber)
                .build();

    }


    /**
     * 获取用户列表（邮箱）
     */
    private void getEmailUserList() {
        loadingDialog();
        new HttpGetBuilder(this)
                .setUrl(UrlsNew.GET_USER_LIST)
                .setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
                        if (requestCode == 0) {
                            LoginResultBean bean = (LoginResultBean) resultBean;
                            LoginResultBean.Paginate paginate = bean.getPaginate();
                            if (paginate.getTotalNum() == 0) {  //没有被注册
                                getVerifyCode();
                            } else {
                                if (dialog != null && dialog.isShowing()) {
                                    dialog.dismiss();
                                }
                                ToastUtils.makeText(ChangeAccountActivity.this, getResources().getString(R.string.already_register), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
                            NetworkUtil.httpRestartLogin(ChangeAccountActivity.this, myLayout);
                        } else {
                            NetworkUtil.httpNetErrTip(ChangeAccountActivity.this, myLayout);
                        }
                        ToastUtils.makeText(ChangeAccountActivity.this,msg,Toast.LENGTH_SHORT).show();
                    }
                })
                .setClassObj(LoginResultBean.class)
                .addQueryParams("email", accountNumber)
                .build();

    }


    /**
     * 获取验证码
     */
    private void getVerifyCode() {
        if(type.equals(PHONE_TYPE)){
            if (TextUtils.isEmpty(accountNumber)) {
                et_phne.setError(getResources().getString(R.string.input_new_phone_hint));
                et_phne.setHintTextColor(getResources().getColor(R.color.font_red));
                return;
            }
            if (ValidatorUtil.isMobile(accountNumber)) {
                getPhoneVerify();
            } else {
                ToastUtils.makeText(ChangeAccountActivity.this, getResources().getString(R.string.phone_legal_number), Toast.LENGTH_SHORT).show();
            }
        } else if(type.equals(EMAIL_TYPE)){
            if (TextUtils.isEmpty(accountNumber)) {
                et_phne.setError(getResources().getString(R.string.input_new_email_hint));
                et_phne.setHintTextColor(getResources().getColor(R.color.font_red));
                return;
            }
            if (ValidatorUtil.isEmail(accountNumber)) {
                getEmailVerify();
            } else {
                ToastUtils.makeText(ChangeAccountActivity.this, getResources().getString(R.string.email_fomat_error), Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 获取邮箱验证码
     */
    private void getEmailVerify() {
        new HttpPostBuilder(this)
                .setUrl(UrlsNew.POST_SEND_CAPTCHA)
                .setHttpResult(ChangeAccountActivity.this)
                .setClassObj(null)
                .addBodyParam("email", accountNumber)
                .addBodyParam("send_method", EMAIL_TYPE)
                .addBodyParam("type", "reset_email")
                .build();

    }

    /**
     * 获取手机验证码
     */
    private void getPhoneVerify() {
        new HttpPostBuilder(this)
                .setUrl(UrlsNew.POST_SEND_CAPTCHA)
                .setHttpResult(ChangeAccountActivity.this)
                .setClassObj(null)
                .addBodyParam("mobile", accountNumber)
                .addBodyParam("send_method", PHONE_TYPE)
                .addBodyParam("type", "reset_mobile")
                .build();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void setOnSuccessCallback(int requestCode, Object resultBean) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        timeCount = new TimeCount(60000, 1000);
        timeCount.start();
        delayToastDialog();
    }

    /**
     * 延时弹出提示
     */
    private void delayToastDialog() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(type.equals(PHONE_TYPE)){
                    remindDialog(PHONE_TYPE,VERIFY_TYPE);
                } else if(type.equals(EMAIL_TYPE)){
                    remindDialog(EMAIL_TYPE,VERIFY_TYPE);
                }
            }
        },2000);
    }

    @Override
    public void setOnErrorCallback(int requestCode, int code, String msg) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
            NetworkUtil.httpRestartLogin(ChangeAccountActivity.this, myLayout);
        } else {
            NetworkUtil.httpNetErrTip(ChangeAccountActivity.this, myLayout);
        }
    }


    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            verifyButton.setTextColor(getResources().getColor(R.color.font_white));
            verifyButton.setBackgroundResource(R.drawable.round_hui_banyuan1);
            verifyButton.setEnabled(false);
            verifyButton.setText("重新发送"+"  ("+ millisUntilFinished / 1000+")");
        }

        @Override
        public void onFinish() {
            verifyButton.setTextColor(getResources().getColor(R.color.font_white));
            verifyButton.setText(getResources().getString(R.string.get_auth_code_again));
            verifyButton.setEnabled(true);
            verifyButton.setBackgroundResource(R.drawable.round_huang3_loginsetting);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            finish();
            return true;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}

package com.coder.kzxt.login.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.coder.kzxt.login.beans.LoginDataBean;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.dialog.util.MyPublicDialog;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.coder.kzxt.utils.ToastUtils;
import com.coder.kzxt.utils.Urls;
import com.coder.kzxt.views.CustomNewDialog;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 学号登录，或者是在账号安全页面没有手机或邮箱进行绑定手机或邮箱
 * Created by Administrator on 2017/3/2.
 */

public class BindPhoneActivity extends BaseActivity {

    private EditText et_login_phone_email;
    private View fenge;
    private Button next;
    private TextView changetoemail;
    private SharedPreferencesUtil spu;
    private String phone_email;
    private RelativeLayout delete_login;
    private Dialog dialog;
    private InputMethodManager imm;
    private String uid;
    private String userface;
    private String openId;
    private String type;
    private String isexist = "0";
    private String token;
    private String oauthtype;
    private String iden = "0";
    private Toolbar mToolbarView;
    private RelativeLayout myLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(R.anim.login_up_in, 0);
        setContentView(R.layout.activity_register_phone);
        spu = new SharedPreferencesUtil(this);
        uid = String.valueOf(spu.getUid());
        userface = spu.getUserHead();
        openId = String.valueOf(spu.getOpenUid());
        type = getIntent().getStringExtra("type");
        token = getIntent().getStringExtra("token");
        oauthtype = getIntent().getStringExtra("oauthtype");
        initView();
        initListener();
    }

    private void initView() {
        mToolbarView = (Toolbar) findViewById(R.id.toolbar);
        if (!TextUtils.isEmpty(type)) {
            if (type.equals("change")) {
                mToolbarView.setTitle(getResources().getString(R.string.input_new_phone_hint));
                isexist = "0";
            } else if (type.equals("oauthlogin") || type.equals("change") || type.equals("removephoneoauth")) {//三方登陆，需要绑定本地账号，先验证是新老用户
                mToolbarView.setTitle(getResources().getString(R.string.phone_num_auth));
                isexist = "1";
            } else if (type.equals("phonesafe") || type.equals("changephone")) {
                mToolbarView.setTitle(getResources().getString(R.string.input_phone_hint));
                isexist = "0";
            } else {
                isexist = "0";
                mToolbarView.setTitle(getResources().getString(R.string.bind_phone));
            }
        } else {
            mToolbarView.setTitle(getResources().getString(R.string.bind_phone));
        }
        setSupportActionBar(mToolbarView);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        delete_login = (RelativeLayout) findViewById(R.id.delete_login);
        et_login_phone_email = (EditText) findViewById(R.id.et_login_phone_email);
        et_login_phone_email.setFocusable(true);
        myLayout = (RelativeLayout) findViewById(R.id.rl_layout);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                imm = (InputMethodManager) et_login_phone_email.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(et_login_phone_email, 0);
            }
        }, 500);

        fenge = findViewById(R.id.fenge);
        next = (Button) findViewById(R.id.next);
        if (type.equals("oauthlogin") || type.equals("phonesafe") || type.equals("change") || type.equals("removephoneoauth") || type.equals("changephone")) {
            next.setText("验证");
        }
        next.setAlpha(0.5f);
        next.setLongClickable(false);
        next.setClickable(false);
        changetoemail = (TextView) findViewById(R.id.change_to_email);
        changetoemail.setText(getResources().getString(R.string.change_email_bind));
        if (!TextUtils.isEmpty(type)) {
            if (type.equals("phonesafe") || type.equals("change") || type.equals("removephoneoauth") || type.equals("oauthlogin") || type.equals("changephone")) {
                changetoemail.setVisibility(View.GONE);
            }
        }
    }

    private void judge() {
        final CustomNewDialog customNewDialog = new CustomNewDialog(BindPhoneActivity.this, R.layout.yun_exit_info);
        TextView yun_exit_cancel = (TextView) customNewDialog.findViewById(R.id.yun_exit_cancel);
        TextView yun_exit_sure = (TextView) customNewDialog.findViewById(R.id.yun_exit_sure);
        customNewDialog.show();
        yun_exit_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customNewDialog.cancel();
            }
        });
        yun_exit_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customNewDialog.cancel();
                finish();
            }
        });
    }

    private void initListener() {

        et_login_phone_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s.toString().trim()) || s.length() == 0) {
                    next.setAlpha(0.5f);
                    next.setLongClickable(false);
                    next.setClickable(false);
                } else {
                    delete_login.setVisibility(View.VISIBLE);
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
                        et_login_phone_email.setText(sb.toString());
                        et_login_phone_email.setSelection(index);
                    }
                    next.setAlpha(1.0f);
                    next.setClickable(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString().trim()) || s.length() == 0) {
                    next.setAlpha(0.5f);
                    next.setClickable(false);
                } else {
                    delete_login.setVisibility(View.VISIBLE);
                    next.setAlpha(1.0f);
                    next.setClickable(true);
                }
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (next.getAlpha() == 0.5f) {
                    return;
                }
                imm.hideSoftInputFromWindow(et_login_phone_email.getWindowToken(), 0);
                phone_email = et_login_phone_email.getText().toString().replace(" ", "").trim();
                if (TextUtils.isEmpty(phone_email)) {
                    et_login_phone_email.setError(getResources().getString(R.string.input_phone_hint));
                    et_login_phone_email.setHintTextColor(getResources().getColor(R.color.font_red));
                    return;
                } else if (phone_email.length() > 11) {
                    ToastUtils.makeText(BindPhoneActivity.this, getResources().getString(R.string.phone_num_limit), Toast.LENGTH_SHORT);
                    return;
                } else {
                    //判断手机号并获取验证码，获取成功进入下一页
                    dialog = MyPublicDialog.createLoadingDialog(BindPhoneActivity.this);
                    dialog.show();
                    if (type.equals("oauthlogin")) {//三方登陆，需要绑定本地账号，先验证是新老用户
                        iden = oauthtype;
                        checkAccountTask(phone_email, isexist);
                    } else {
                        checkAccountTask(phone_email, isexist);
                    }
                }
            }
        });
        delete_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_login_phone_email.setText("");
            }
        });
        //切换为绑定邮箱
        changetoemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(BindPhoneActivity.this, BindEmailActivity.class);
                if (TextUtils.isEmpty(type)) {
                } else {
                    if (type.equals("oauthlogin")) {
                        intent.putExtra("type", "oauthlogin");
                        intent.putExtra("token", token);
                        intent.putExtra("oauthtype", oauthtype);
                    } else {
                        intent.putExtra("type", "");
                        intent.putExtra("token", "");
                        intent.putExtra("oauthtype", "");
                    }
                }
                startActivityForResult(intent, 1);
            }
        });
    }

    private void checkAccountTask(String phone_email, String isexist) {
        new HttpPostOld(this, this, new InterfaceHttpResult() {         //1邮箱 2手机
            @Override
            public void getCallback(int requestCode, int code, String msg, Object baseBean) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (requestCode == 1000 && code == Constants.HTTP_CODE_SUCCESS) {
                    LoginDataBean.LoginBean bean = ((LoginDataBean) baseBean).getData();
                    spu.setUid(bean.getUid());
                    checkSuccess();
                } else if (code == 1004 || code == 1007) {
                    if (type.equals("oauthlogin")) {
                        bindSecondActivity();
                    }
                } else if (code == Constants.HTTP_CODE_4000 || code == Constants.HTTP_CODE_5000) {
                    NetworkUtil.httpNetErrTip(BindPhoneActivity.this, myLayout);
                } else {
                    errorProcess(msg);
                }
            }
        }, LoginDataBean.class, Urls.POST_REGISTER_CHECK_ACTION, phone_email, "2", isexist, "0", spu.getDevicedId()).excute(1000);

    }

    private void errorProcess(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            Toast.makeText(BindPhoneActivity.this, msg, Toast.LENGTH_SHORT).show();
        }
    }

    private void bindSecondActivity() {
        Intent intent = new Intent(BindPhoneActivity.this, BindSecondActivity.class);
        intent.putExtra("from", "oauthphonereg");
        intent.putExtra("token", token);
        intent.putExtra("oauthtype", oauthtype);
        intent.putExtra("account", phone_email);
        startActivityForResult(intent, 1);
    }

    private void checkSuccess() {
        Intent intent = new Intent();
        intent.setClass(BindPhoneActivity.this, BindSecondActivity.class);
        if (TextUtils.isEmpty(type)) {

        } else {
            if (type.equals("changephone")) {//更换手机号
                intent.putExtra("from", "changephone");
                intent.putExtra("token", "");
                intent.putExtra("oauthtype", "");
            } else if (type.equals("oauthlogin")) {//三方登陆先验证再绑定手机号
                intent.putExtra("from", "oauthbindphone");
                intent.putExtra("token", token);
                intent.putExtra("oauthtype", oauthtype);
            } else if (type.equals("phonesafe")) {
                intent.putExtra("from", "bindphone");
                intent.putExtra("token", "");
                intent.putExtra("oauthtype", "");
            } else {//绑定手机号type=log,safe
                intent.putExtra("from", "bindphone");
                intent.putExtra("token", "");
                intent.putExtra("oauthtype", "");
            }
        }
        intent.putExtra("account", phone_email);
        startActivityForResult(intent, 1);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                judge();
                break;
        }
        return super.onOptionsItemSelected(item);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 1) {
            et_login_phone_email.setText("");
        } else if (requestCode == 1 && resultCode == 2) {
            String info = data.getStringExtra("data");
            setResult(1, new Intent().putExtra("data", info));
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
    protected void onResume() {
        super.onResume();
        /**
         * 页面起始（每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加）
         * 不能与StatService.onPageStart一级onPageEnd函数交叉使用
         */
//        StatService.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        /**
         * 页面结束（每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加）
         * 不能与StatService.onPageStart一级onPageEnd函数交叉使用
         */
//        StatService.onPause(this);
    }

    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(0, R.anim.login_down_out);
    }

}

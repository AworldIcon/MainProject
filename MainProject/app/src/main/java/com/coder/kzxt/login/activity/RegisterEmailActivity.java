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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.http.HttpCallBack;
import com.app.http.builders.HttpGetBuilder;
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
import com.coder.kzxt.views.EmailAutoCompleteTextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 邮箱注册
 */

public class RegisterEmailActivity extends BaseActivity {

    private EditText et_login_phone_email;
    private EmailAutoCompleteTextView et_login_phone_email_auto;
    private View fenge;
    private Button next;
    private String phone_email;
    private SharedPreferencesUtil spu;
    private RelativeLayout delete_login;
    private TextView change_to_email;
    private InputMethodManager imm;
    private Dialog dialog;
    private Toolbar mToolbarView;
    private RelativeLayout myLayout;
    private CheckBox checkBox; //是否勾选

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpwd_first);
        overridePendingTransition(R.anim.login_up_in, R.anim.login_stay);
        spu = new SharedPreferencesUtil(this);
        initView();
        initListener();
    }

    private void initView() {
        mToolbarView = (Toolbar) findViewById(R.id.toolbar);
        mToolbarView.setTitle(getResources().getString(R.string.email_num_register));
        setSupportActionBar(mToolbarView);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        delete_login = (RelativeLayout) findViewById(R.id.delete_login);
        et_login_phone_email = (EditText) findViewById(R.id.et_login_phone_email);
        checkBox = (CheckBox) findViewById(R.id.cb_protocol);
        checkBox.setChecked(true);
        et_login_phone_email.setVisibility(View.GONE);
        et_login_phone_email_auto = (EmailAutoCompleteTextView) findViewById(R.id.et_login_phone_email_auto);
        et_login_phone_email_auto.setVisibility(View.VISIBLE);
        et_login_phone_email_auto.setFocusable(true);
        myLayout = (RelativeLayout) findViewById(R.id.rl_layout);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                imm = (InputMethodManager) et_login_phone_email_auto.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(et_login_phone_email_auto, 0);
            }
        }, 500);

        fenge = findViewById(R.id.fenge);
        next = (Button) findViewById(R.id.next);
        next.setText(getResources().getString(R.string.register));
        next.setAlpha(0.5f);
        next.setClickable(false);
        change_to_email = (TextView) findViewById(R.id.change_to_email);
        change_to_email.setVisibility(View.GONE);
    }

    private void initListener() {

        et_login_phone_email_auto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s.toString().trim()) || s.length() == 0) {
                    next.setAlpha(0.5f);
                    next.setClickable(false);
                } else {
                    et_login_phone_email_auto.setThreshold(1);
                    delete_login.setVisibility(View.VISIBLE);
                    if (checkBox.isChecked()) {
                        next.setClickable(true);
                        next.setAlpha(1.0f);
                    } else {
                        next.setAlpha(0.5f);
                        next.setClickable(false);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString().trim()) || s.length() == 0) {
                    next.setAlpha(0.5f);
                    next.setLongClickable(false);
                    next.setClickable(false);
                    delete_login.setVisibility(View.GONE);
                } else {
                    delete_login.setVisibility(View.VISIBLE);
                    if(checkBox.isChecked()){
                        next.setAlpha(1.0f);
                        next.setClickable(true);
                    } else {
                        next.setAlpha(0.5f);
                        next.setClickable(false);
                    }
                }
            }
        });
        delete_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_login_phone_email_auto.setText("");
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (next.getAlpha() == 0.5f) {
                    return;
                }
                imm.hideSoftInputFromWindow(et_login_phone_email_auto.getWindowToken(), 0);
                phone_email = et_login_phone_email_auto.getText().toString().trim();
                if (TextUtils.isEmpty(phone_email)) {
                    et_login_phone_email_auto.setError(getResources().getString(R.string.input_email_hint));
                    et_login_phone_email_auto.setHintTextColor(getResources().getColor(R.color.font_red));
                    return;
                } else {
                    //判断邮箱并获取验证码，获取成功进入下一页
                    if(ValidatorUtil.isEmail(phone_email)){
                        dialog = MyPublicDialog.createLoadingDialog(RegisterEmailActivity.this);
                        dialog.show();
                        emailRegisterTask();
                    } else {
                        ToastUtils.makeText(RegisterEmailActivity.this, getResources().getString(R.string.email_style_wrong), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    next.setClickable(true);
                    next.setAlpha(1.0f);
                } else {
                    next.setClickable(false);
                    next.setAlpha(0.5f);
                }
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * 邮箱注册
     */
    private void emailRegisterTask() {
        new HttpGetBuilder(this)
                .setUrl(UrlsNew.GET_USER_LIST)
                .setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.cancel();
                        }
                        if (requestCode == 0) {
                            LoginResultBean bean = (LoginResultBean) resultBean;
                            LoginResultBean.Paginate paginate = bean.getPaginate();
                            if (paginate.getTotalNum() == 0) {
                                checkCodeActivity();
                            }
                        }
                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {
                        if (code == Constants.HTTP_CODE_4000 || code == Constants.HTTP_CODE_5000) {
                            if (!NetworkUtil.isNetworkAvailable(RegisterEmailActivity.this)) {
                                NetworkUtil.httpNetErrTip(RegisterEmailActivity.this, myLayout);
                            }
                        } else {
                            errorProcess(msg);
                        }
                    }
                })
                .setClassObj(LoginResultBean.class)
                .addQueryParams("email", phone_email)
                .build();

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
            et_login_phone_email.setText("");
        } else if (requestCode == 1 && resultCode == 3) {
            setResult(3);
            finish();
        }
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            setResult(1);
            finish();
            return true;
        }
        return false;
    }

    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(R.anim.login_stay, R.anim.login_down_out);
    }

    /**
     * 异常处理
     * @param msg
     */
    private void errorProcess(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            Toast.makeText(RegisterEmailActivity.this, msg, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(RegisterEmailActivity.this, getResources().getString(R.string.net_inAvailable), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 校验码校验
     */
    private void checkCodeActivity() {
        Intent intent = new Intent();
        intent.setClass(RegisterEmailActivity.this, CheckCodeActivity.class);
        intent.putExtra("from", "registerbyemail");
        intent.putExtra("account", phone_email);
        startActivityForResult(intent, 1);
    }

}

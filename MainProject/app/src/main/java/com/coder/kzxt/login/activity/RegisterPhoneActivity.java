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
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import java.util.Timer;
import java.util.TimerTask;

/**
 * 手机注册
 */
public class RegisterPhoneActivity extends BaseActivity implements HttpCallBack {

    private Toolbar mToolbarView;
    private SharedPreferencesUtil spu;
    private EditText et_login_phone_email;
    private View fenge;
    private Button next;
    private TextView change_to_email;
    private String phone_email;
    private RelativeLayout delete_login;
    private Dialog dialog;
    private InputMethodManager imm;
    private RelativeLayout myLayout;
    private CheckBox checkBox; //是否勾选
    private LinearLayout ll_protocol;//协议
    private TextView tv_protocol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_phone);
        overridePendingTransition(R.anim.login_up_in,R.anim.login_stay);
        spu = new SharedPreferencesUtil(this);
        initView();
        initListener();
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
                    if(checkBox.isChecked()){
                        next.setClickable(true);
                    } else {
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
                } else if (phone_email.length() != 11) {
                    ToastUtils.makeText(RegisterPhoneActivity.this, getResources().getString(R.string.phone_num_limit), Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    //判断手机号并获取验证码，获取成功进入下一页
                    if(checkBox.isChecked()){
                        if(ValidatorUtil.isMobile(phone_email)){
                            dialog = MyPublicDialog.createLoadingDialog(RegisterPhoneActivity.this);
                            dialog.show();
                            getUserList();
                        } else {
                            ToastUtils.makeText(RegisterPhoneActivity.this, getResources().getString(R.string.phone_legal_number), Toast.LENGTH_SHORT).show();
                        }
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

        //切换为邮箱注册
        change_to_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(RegisterPhoneActivity.this, RegisterEmailActivity.class);
                startActivityForResult(intent, 1);
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

    /**
     * 获取用户信息列表
     */
    private void getUserList() {
        new HttpGetBuilder(this)
                .setUrl(UrlsNew.GET_USER_LIST)
                .setHttpResult(this)
                .setClassObj(LoginResultBean.class)
                .addQueryParams("mobile", phone_email)
                .build();

    }

    private void initView() {
        mToolbarView = (Toolbar) findViewById(R.id.toolbar);
        mToolbarView.setTitle(getResources().getString(R.string.phone_num_register));
        setSupportActionBar(mToolbarView);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        delete_login = (RelativeLayout) findViewById(R.id.delete_login);
        et_login_phone_email = (EditText) findViewById(R.id.et_login_phone_email);
        et_login_phone_email.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13)});
        et_login_phone_email.setFocusable(true);
        myLayout = (RelativeLayout) findViewById(R.id.rl_layout);
        checkBox = (CheckBox) findViewById(R.id.cb_protocol);
        ll_protocol = (LinearLayout) findViewById(R.id.ll_protocol);
        tv_protocol = (TextView) findViewById(R.id.tv_agree);
        checkBox.setChecked(true);
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
        next.setText(getResources().getString(R.string.register));
        next.setAlpha(0.5f);
        next.setClickable(false);
        change_to_email = (TextView) findViewById(R.id.change_to_email);
        if (spu.getRegistType().equals("1")) {
            change_to_email.setVisibility(View.GONE);
        }
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
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
            et_login_phone_email.setText("");
        } else if (requestCode == 1 && resultCode == 3) {
            setResult(3);
            finish();
        }else if(requestCode == 1 && resultCode == 2){
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
        overridePendingTransition(R.anim.login_stay,R.anim.login_down_out);
    }


    private void jumpNextActivity() {
        Intent intent=new Intent();
        intent.setClass(RegisterPhoneActivity.this,CheckCodeActivity.class);
        intent.putExtra("from","registerbyphone");
        intent.putExtra("account",phone_email);
        startActivityForResult(intent,1);
    }

    /**
     * 异常提示
     * @param msg
     */
    private void errorProcess(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            Toast.makeText(RegisterPhoneActivity.this, msg, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(RegisterPhoneActivity.this, getResources().getString(R.string.net_inAvailable), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void setOnSuccessCallback(int requestCode, Object resultBean) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        if (requestCode == 0) {
            LoginResultBean bean = (LoginResultBean) resultBean;
            LoginResultBean.Paginate paginate = bean.getPaginate();
            if (paginate.getTotalNum() == 0) {  //没有被注册
                jumpNextActivity();
            } else {
                ToastUtils.makeText(this, getResources().getString(R.string.already_register), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void setOnErrorCallback(int requestCode, int code, String msg) {
        if (requestCode == Constants.HTTP_CODE_4000 ||
                requestCode == Constants.HTTP_CODE_5000) {
            if (!NetworkUtil.isNetworkAvailable(this)) {
                NetworkUtil.httpNetErrTip(RegisterPhoneActivity.this, myLayout);
            } else {
                //其他处理
            }
        } else {
          errorProcess(msg);
        }
    }
}

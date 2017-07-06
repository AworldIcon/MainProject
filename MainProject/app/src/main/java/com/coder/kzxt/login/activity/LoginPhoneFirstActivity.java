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

import java.util.Timer;
import java.util.TimerTask;

/**
 * 手机验证
 */

public class LoginPhoneFirstActivity extends BaseActivity {

    private EditText et_login_phone_email;
    private Button next;
    private TextView changetoemail;
    private SharedPreferencesUtil spu;
    private String phone_email;
    private RelativeLayout delete_login;
    private Dialog dialog;
    private InputMethodManager imm;
    private Toolbar mToolbarView;
    private RelativeLayout myLayout;
    private LinearLayout ll_protocol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpwd_first);
        this.overridePendingTransition(R.anim.login_up_in,R.anim.login_stay);
        spu = new SharedPreferencesUtil(this);
        initView();
        initListener();
    }
    private void initView(){
        mToolbarView = (Toolbar) findViewById(R.id.toolbar);
        mToolbarView.setTitle(getResources().getString(R.string.phone_num_auth));
        setSupportActionBar(mToolbarView);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //删除已输入的账号
        delete_login = (RelativeLayout) findViewById(R.id.delete_login);
        et_login_phone_email = (EditText) findViewById(R.id.et_login_phone_email);
        et_login_phone_email.setHint(getResources().getString(R.string.input_registered_phone_hint));
        et_login_phone_email.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13)});
        et_login_phone_email.setFocusable(true);
        myLayout = (RelativeLayout) findViewById(R.id.rl_layout);
        ll_protocol = (LinearLayout) findViewById(R.id.ll_protocol);
        ll_protocol.setVisibility(View.INVISIBLE);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                imm = (InputMethodManager) et_login_phone_email.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(et_login_phone_email, 0);
            }
        }, 500);

        next = (Button) findViewById(R.id.next);
        next.setAlpha(0.5f);
        next.setClickable(false);
        changetoemail = (TextView) findViewById(R.id.change_to_email);
    }
    private void initListener(){
        et_login_phone_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(TextUtils.isEmpty(s.toString().trim())||s.length()==0){
                    delete_login.setVisibility(View.INVISIBLE);
                    next.setAlpha(0.5f);
                    next.setClickable(false);
                }else {
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
                    delete_login.setVisibility(View.VISIBLE);
                    next.setAlpha(1.0f);
                    next.setClickable(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString().trim()) || s.length() == 0) {
                    delete_login.setVisibility(View.INVISIBLE);
                    next.setAlpha(0.5f);
                    next.setClickable(false);
                    next.setLongClickable(false);
                } else {
                    delete_login.setVisibility(View.VISIBLE);
                    next.setAlpha(1.0f);
                    next.setClickable(true);
                }
            }
        });
        delete_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_login_phone_email.setText("");
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(next.getAlpha() == 0.5f){
                    return;
                }
                imm.hideSoftInputFromWindow(et_login_phone_email.getWindowToken(), 0);
                phone_email=et_login_phone_email.getText().toString().replace(" ","").trim();
                if(TextUtils.isEmpty(phone_email)){
                    et_login_phone_email.setError(getResources().getString(R.string.input_phone_hint));
                    et_login_phone_email.setHintTextColor(getResources().getColor(R.color.font_red));
                    return;
                }else{
                    //判断手机号并获取验证码，获取成功进入下一页
                    dialog = MyPublicDialog.createLoadingDialog(LoginPhoneFirstActivity.this);
                    dialog.show();
                    checkAccountTask(phone_email);
                }
            }
        });
        //切换为邮箱找回或绑定
        changetoemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {        //TODO:暂时注掉
//                Intent intent=new Intent();
//                intent.setClass(LoginPhoneFirstActivity.this,LoginEmailFirstActivity.class);
//                intent.putExtra("type","findpwd");
//                startActivityForResult(intent,1);
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
    private void checkAccountTask(String phoneEmail) {
        new HttpGetBuilder(this)
                .setUrl(UrlsNew.GET_USER_LIST)
                .setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        if (requestCode == 0) {
                            LoginResultBean bean = (LoginResultBean) resultBean;
                            LoginResultBean.Paginate paginate = bean.getPaginate();
                            if (paginate.getTotalNum() == 0) {  //没有被注册
                                ToastUtils.makeText(LoginPhoneFirstActivity.this, getResources().getString(R.string.no_register), Toast.LENGTH_SHORT).show();
                            } else {
                                checkSuccess();
                            }
                        }
                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {
                        if (requestCode == Constants.HTTP_CODE_4000 ||
                                requestCode == Constants.HTTP_CODE_5000) {
                            if (!NetworkUtil.isNetworkAvailable(LoginPhoneFirstActivity.this)) {
                                NetworkUtil.httpNetErrTip(LoginPhoneFirstActivity.this, myLayout);
                            } else {
                                //其他处理
                            }
                        } else {
                            errorProcess(msg);
                        }
                    }
                })
                .setClassObj(LoginResultBean.class)
                .addQueryParams("mobile", phoneEmail)
                .build();

    }

    private void checkSuccess() {
        Intent intent = new Intent();
        intent.setClass(LoginPhoneFirstActivity.this, CheckCodeActivity.class);
        intent.putExtra("from", "findbyphone");
        intent.putExtra("account", phone_email);
        startActivityForResult(intent, 1);
    }

    /**
     * 异常处理
     * @param msg
     */
    private void errorProcess(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            Toast.makeText(LoginPhoneFirstActivity.this, msg, Toast.LENGTH_SHORT).show();
        }
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
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
            et_login_phone_email.setText("");
        } else if (requestCode == 1 && resultCode == 3) {
            setResult(3);
            finish();
        } else if (requestCode == 1 && resultCode == 2) {
            setResult(2);
            finish();
        }
        if(dialog!=null&&dialog.isShowing()) {
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
        if(dialog!=null&&dialog.isShowing()) {
            dialog.dismiss();
        }
    }

}

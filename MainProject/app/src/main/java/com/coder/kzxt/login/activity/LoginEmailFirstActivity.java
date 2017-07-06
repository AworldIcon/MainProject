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
//import android.view.KeyEvent;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//import com.app.http.HttpPostOld;
//import com.app.http.InterfaceHttpResult;
//import com.coder.kzxt.activity.R;
//import com.coder.kzxt.base.activity.BaseActivity;
//import com.coder.kzxt.dialog.util.MyPublicDialog;
//import com.coder.kzxt.utils.Constants;
//import com.coder.kzxt.utils.NetworkUtil;
//import com.coder.kzxt.utils.SharedPreferencesUtil;
//import com.coder.kzxt.utils.Urls;
//import com.coder.kzxt.views.EmailAutoCompleteTextView;
//import java.util.Timer;
//import java.util.TimerTask;
//
///**
// * 邮箱验证
// */
//
//public class LoginEmailFirstActivity extends BaseActivity {
//
//    private EditText et_login_phone_email;
//    private EmailAutoCompleteTextView et_login_phone_email_auto;
//    private Button next;
//    private String phone_email;
//    private SharedPreferencesUtil spu;
//    private RelativeLayout delete_login;
//    private TextView changetoemail;
//    private Dialog dialog;
//    private InputMethodManager imm;
//    private Toolbar mToolbarView;
//    private RelativeLayout myLayout;
//    private LinearLayout ll_protocol;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_forgetpwd_first);
//        this.overridePendingTransition(R.anim.login_up_in,R.anim.login_stay);
//        spu = new SharedPreferencesUtil(this);
//        initView();
//        initListener();
//    }
//    private void initView(){
//        changetoemail= (TextView) findViewById(R.id.change_to_email);
//        changetoemail.setVisibility(View.GONE);
//        mToolbarView = (Toolbar) findViewById(R.id.toolbar);
//        mToolbarView.setTitle(getResources().getString(R.string.email_num_auth));
//        setSupportActionBar(mToolbarView);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        //删除已输入的账号
//        delete_login= (RelativeLayout) findViewById(R.id.delete_login);
//        et_login_phone_email= (EditText) findViewById(R.id.et_login_phone_email);
//        et_login_phone_email.setVisibility(View.GONE);
//        et_login_phone_email_auto= (EmailAutoCompleteTextView) findViewById(R.id.et_login_phone_email_auto);
//        et_login_phone_email_auto.setHint(getResources().getString(R.string.input_registed_email_address_hint));
//        et_login_phone_email_auto.setVisibility(View.VISIBLE);
//        myLayout = (RelativeLayout) findViewById(R.id.rl_layout);
//        ll_protocol = (LinearLayout) findViewById(R.id.ll_protocol);
//        ll_protocol.setVisibility(View.INVISIBLE);
//        et_login_phone_email_auto.setFocusable(true);
//        Timer timer=new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                imm = (InputMethodManager) et_login_phone_email_auto.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.showSoftInput(et_login_phone_email_auto, 0);
//            }
//        },500);
//
//        next= (Button) findViewById(R.id.next);
//        next.setAlpha(0.5f);
//        next.setClickable(false);
//    }
//    private void initListener() {
//        et_login_phone_email_auto.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (TextUtils.isEmpty(s.toString().trim()) || s.length() == 0) {
//                    delete_login.setVisibility(View.INVISIBLE);
//                    next.setAlpha(0.5f);
//                    next.setClickable(false);
//                    next.setLongClickable(false);
//                } else {
//                    et_login_phone_email_auto.setThreshold(1);
//                    delete_login.setVisibility(View.VISIBLE);
//                    next.setAlpha(1.0f);
//                    next.setClickable(true);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (TextUtils.isEmpty(s.toString().trim()) || s.length() == 0) {
//                    delete_login.setVisibility(View.INVISIBLE);
//                    next.setAlpha(0.5f);
//                    next.setClickable(false);
//                } else {
//                    delete_login.setVisibility(View.VISIBLE);
//                    next.setAlpha(1.0f);
//                    next.setClickable(true);
//                }
//            }
//        });
//        delete_login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                et_login_phone_email_auto.setText("");
//            }
//        });
//        next.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(next.getAlpha() == 0.5f){
//                    return;
//                }
//                imm.hideSoftInputFromWindow(et_login_phone_email_auto.getWindowToken(), 0);
//                phone_email = et_login_phone_email_auto.getText().toString().trim();
//                if (TextUtils.isEmpty(phone_email)) {
//                    et_login_phone_email_auto.setError(getResources().getString(R.string.input_email_hint));
//                    et_login_phone_email_auto.setHintTextColor(getResources().getColor(R.color.font_red));
//                    return;
//                } else {
//                    //判断邮箱并获取验证码，获取成功进入下一页
//                    dialog = MyPublicDialog.createLoadingDialog(LoginEmailFirstActivity.this);
//                    dialog.show();
//                    checkAccountTask(phone_email);
//                }
//            }
//        });
//    }
//
//    private void checkAccountTask(String phone_email) {
//        new HttpPostOld(this,this, new InterfaceHttpResult() {         //1邮箱 2手机
//            @Override
//            public void getCallback(int requestCode, int code, String msg, Object baseBean) {
//                if (dialog != null && dialog.isShowing()) {
//                    dialog.dismiss();
//                }
//                if (requestCode == 1000 && code == Constants.HTTP_CODE_SUCCESS) {
//                    checkSuccess();
//                }else if (code == Constants.HTTP_CODE_4000 || code == Constants.HTTP_CODE_5000)
//                {
//                    NetworkUtil.httpNetErrTip(LoginEmailFirstActivity.this, myLayout);
//                } else {
//                    errorProcess(msg);
//                }
//
//            }
//        },  null, Urls.POST_REGISTER_CHECK_ACTION, phone_email, "1", "1", "0", spu.getDevicedId()).excute(1000);
//
//    }
//
//    private void errorProcess(String msg) {
//        if (!TextUtils.isEmpty(msg)) {
//            Toast.makeText(LoginEmailFirstActivity.this, msg, Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private void checkSuccess() {
//        Intent intent=new Intent();
//        intent.setClass(LoginEmailFirstActivity.this,CheckCodeActivity.class);
//        intent.putExtra("from", "findbyemail");
//        intent.putExtra("account",phone_email);
//        startActivityForResult(intent,1);
//    }
//
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()){
//            case android.R.id.home:
//                setResult(1);
//                finish();
//                break;
//            default:
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//
//    @Override
//    public void onPause() {
//        /**
//         * 页面结束（每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加）
//         * 不能与StatService.onPageStart一级onPageEnd函数交叉使用
//         */
////        StatService.onPause(this);
//        super.onPause();
//    }
//
//    @Override
//    public void onResume() {
//        /**
//         * 页面起始（每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加）
//         * 不能与StatService.onPageStart一级onPageEnd函数交叉使用
//         */
////        StatService.onResume(this);
//        super.onResume();
//    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode==1&&resultCode==1){
//            et_login_phone_email.setText("");
//        }else if(requestCode==1&&resultCode==3){
//            setResult(3);
//            finish();
//        }
//        if(dialog!=null&&dialog.isShowing()) {
//            dialog.dismiss();
//        }
//    }
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
//            setResult(1);
//            finish();
//            return true;
//        }
//        return false;
//    }
//    @Override
//    public void finish() {
//        super.finish();
//        this.overridePendingTransition(R.anim.login_stay,R.anim.login_down_out);
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if(dialog!=null&&dialog.isShowing()) {
//            dialog.dismiss();
//        }
//    }
//
//
//
//}

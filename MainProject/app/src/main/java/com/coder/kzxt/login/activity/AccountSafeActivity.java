package com.coder.kzxt.login.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.http.HttpCallBack;
import com.app.http.builders.HttpGetBuilder;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.beans.UserInfoBean;
import com.coder.kzxt.login.beans.UserInfoResult;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.app.http.UrlsNew;
import com.coder.kzxt.views.CustomNewDialog;


/**
 * 账号安全
 * Created by Administrator on 2017/3/7.
 */

public class AccountSafeActivity extends BaseActivity  {

    private LinearLayout jiazai_layout;
    private LinearLayout load_fail_layout;
    private LinearLayout no_info_layout;
    private Button loadFailBtn;
    private RelativeLayout mylayout;
    private Dialog infodialog;
    private SharedPreferencesUtil spu;
    private CustomNewDialog dialog;
    private RelativeLayout phoneLayout;
    private TextView phoneBind; //手机绑定
    private RelativeLayout emailLayout;
    private RelativeLayout socialLayout;//社交账号
    private RelativeLayout changePasword; //修改密码
    private TextView emailBind; //邮箱绑定
    private Toolbar mToolbarView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_safe);
        spu = new SharedPreferencesUtil(this);
        initView();
        initListener();
        getAccountInfo();
    }


    private void initView() {
        mToolbarView = (Toolbar) findViewById(R.id.toolbar);
        mToolbarView.setTitle(getResources().getString(R.string.account_safe));
        setSupportActionBar(mToolbarView);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        jiazai_layout = (LinearLayout) findViewById(R.id.jiazai_layout);
        load_fail_layout = (LinearLayout) findViewById(R.id.load_fail_layout);
        no_info_layout = (LinearLayout) findViewById(R.id.no_info_layout);
        loadFailBtn = (Button) findViewById(R.id.load_fail_button);
        mylayout = (RelativeLayout) findViewById(R.id.account_safe_layout);
        dialog = new CustomNewDialog(AccountSafeActivity.this);
        phoneLayout = (RelativeLayout) findViewById(R.id.phone_rl);
        phoneBind = (TextView) findViewById(R.id.tv_phone_bind);
        emailLayout = (RelativeLayout) findViewById(R.id.email_rl);
        emailBind = (TextView) findViewById(R.id.tv_email_bind);
        socialLayout = (RelativeLayout) findViewById(R.id.rl_social_account);
        changePasword = (RelativeLayout) findViewById(R.id.change_pwd_rl);
        if(spu.getShowQLogin().equals("1")||spu.getShowWLogin().equals("1")){
            socialLayout.setVisibility(View.VISIBLE);
        }
    }
    private void initListener(){
        //加载失败的button
        loadFailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAccountInfo();
            }
        });
        //点击手机
        phoneLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 Intent intent = new Intent(AccountSafeActivity.this,ChangeAccountActivity.class);
                 intent.putExtra(Constants.USER_SAFE_TYPE,"1");
                 intent.putExtra(Constants.ACCOUNT_TYPE,spu.getMobile());
                 startActivityForResult(intent,1);
            }
        });

        //点击邮箱
        emailLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccountSafeActivity.this,ChangeAccountActivity.class);
                intent.putExtra(Constants.ACCOUNT_TYPE,spu.getEmail());
                intent.putExtra(Constants.USER_SAFE_TYPE,"2");
                startActivityForResult(intent,1);
            }
        });

        //社交账号
        socialLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AccountSafeActivity.this,SocialAccountActivity.class));
            }
        });

        //修改密码
        changePasword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccountSafeActivity.this,ChangeAccountActivity.class);
                intent.putExtra(Constants.USER_SAFE_TYPE,"3");
                startActivityForResult(intent,1);
            }
        });

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


    /**
     * 获取账户信息
     */
    private void getAccountInfo() {
        new HttpGetBuilder(this)
                .setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
                        if(requestCode == 1000){
                            UserInfoBean item = ((UserInfoResult) resultBean).getItem();
                            saveLoginData(item);
                        }
                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {
                        if (code == Constants.HTTP_CODE_2001 || code == Constants.HTTP_CODE_2004) {
                            NetworkUtil.httpRestartLogin(AccountSafeActivity.this,mylayout);
                        } else {
                            NetworkUtil.httpNetErrTip(AccountSafeActivity.this, mylayout);
                        }
                    }
                })
                .setClassObj(UserInfoResult.class)
                .setUrl(UrlsNew.USER_PROFILE)
                .setRequestCode(1000)
                .build();
    }

    private void saveLoginData(UserInfoBean info) {
        spu.setMobile(info.getMobile());
        spu.setEmail(info.getEmail());
        //"是否老师:0.否 1.是"
        spu.setUserRole(info.getIs_teacher());
        phoneBind.setText(spu.getMobile());
        emailBind.setText(spu.getEmail());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == 1){
            getAccountInfo();
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
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    /**
     * 加载失败页
     */
    private void loadFailPage() {
        load_fail_layout.setVisibility(View.VISIBLE);
        jiazai_layout.setVisibility(View.GONE);
        no_info_layout.setVisibility(View.GONE);
    }


    /**
     * 显示数据
     */
    private void visibleData() {
        jiazai_layout.setVisibility(View.GONE);
        load_fail_layout.setVisibility(View.GONE);
        no_info_layout.setVisibility(View.GONE);
    }

    /**
     * 显示加载页
     */
    private void loadingPage() {
        jiazai_layout.setVisibility(View.VISIBLE);
        load_fail_layout.setVisibility(View.GONE);
        no_info_layout.setVisibility(View.GONE);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

}

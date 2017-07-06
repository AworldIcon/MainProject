package com.coder.kzxt.login.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.http.HttpPostOld;
import com.app.http.InterfaceHttpResult;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.login.beans.LoginDataBean;
import com.coder.kzxt.dialog.util.MyPublicDialog;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.NetworkUtil;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.coder.kzxt.utils.Urls;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 绑定邮箱
 */

public class BindEmailActivity extends BaseActivity
{

    private EditText et_login_phone_email;
    private AutoCompleteTextView et_login_phone_email_auto;
    private TextView changetoemail;
    private Button next;
    private String phone_email;
    private SharedPreferencesUtil spu;
    private RelativeLayout delete_login;
    private InputMethodManager imm;
    private Dialog dialog;
    private String isexist = "0";
    private String type = "";
    private String token;
    private String oauthtype;
    private String iden = "0";
    private Toolbar mToolbarView;
    private RelativeLayout myLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpwd_first);
        overridePendingTransition(R.anim.login_up_in, R.anim.login_stay);
        spu = new SharedPreferencesUtil(this);
        type = getIntent().getStringExtra("type");
        token = getIntent().getStringExtra("token");
        oauthtype = getIntent().getStringExtra("oauthtype");
        initView();
        initListener();
    }

    private void initView()
    {
        mToolbarView = (Toolbar) findViewById(R.id.toolbar);
        if (type.equals("changeemail"))
        {
            mToolbarView.setTitle(getResources().getString(R.string.input_new_email_hint));
        } else if (type.equals("oauthlogin"))
        {//三方登陆，需要绑定本地账号，先验证是新老用户
            isexist = "1";
            mToolbarView.setTitle(getResources().getString(R.string.input_email_hint));
        } else
        {
            mToolbarView.setTitle(getResources().getString(R.string.bind_email));
        }
        setSupportActionBar(mToolbarView);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        delete_login = (RelativeLayout) findViewById(R.id.delete_login);
        et_login_phone_email = (EditText) findViewById(R.id.et_login_phone_email);
        et_login_phone_email.setVisibility(View.GONE);
        myLayout = (RelativeLayout) findViewById(R.id.rl_layout);
        et_login_phone_email_auto = (AutoCompleteTextView) findViewById(R.id.et_login_phone_email_auto);
        et_login_phone_email_auto.setVisibility(View.VISIBLE);
        et_login_phone_email_auto.setFocusable(true);
        Timer timer = new Timer();
        timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                imm = (InputMethodManager) et_login_phone_email_auto.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(et_login_phone_email_auto, 0);
            }
        }, 500);

        changetoemail = (TextView) findViewById(R.id.change_to_email);
        changetoemail.setVisibility(View.GONE);
        next = (Button) findViewById(R.id.next);
        next.setAlpha(0.5f);
        next.setClickable(false);
        next.setLongClickable(false);
    }

    private void initListener()
    {

        et_login_phone_email_auto.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (TextUtils.isEmpty(s.toString().trim()) || s.length() == 0)
                {
                    next.setAlpha(0.5f);
                    next.setLongClickable(false);
                    next.setClickable(false);
                } else
                {
                    delete_login.setVisibility(View.VISIBLE);
                    next.setAlpha(1.0f);
                    next.setClickable(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                if (TextUtils.isEmpty(s.toString().trim()) || s.length() == 0)
                {
                    next.setAlpha(0.5f);
                    next.setClickable(false);
                } else
                {
                    delete_login.setVisibility(View.VISIBLE);
                    next.setAlpha(1.0f);
                    next.setClickable(true);
                }
            }
        });
        delete_login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                et_login_phone_email_auto.setText("");
            }
        });
        next.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (next.getAlpha() == 0.5f)
                {
                    return;
                }
                imm.hideSoftInputFromWindow(et_login_phone_email_auto.getWindowToken(), 0);
                phone_email = et_login_phone_email_auto.getText().toString().trim();
                if (TextUtils.isEmpty(phone_email))
                {
                    et_login_phone_email_auto.setError(getResources().getString(R.string.input_email_hint));
                    et_login_phone_email_auto.setHintTextColor(ContextCompat.getColor(BindEmailActivity.this, R.color.font_red));
                    return;
                } else
                {
                    dialog = MyPublicDialog.createLoadingDialog(BindEmailActivity.this);
                    dialog.show();
                    //判断邮箱并获取验证码，获取成功进入下一页
                    if (type.equals("oauthlogin"))
                    {
                        iden = oauthtype;
                        CheckAccountTask(phone_email, isexist);
                    } else
                    {
                        CheckAccountTask(phone_email, isexist);
                    }
                }
            }
        });
    }

    /**
     * 校验账号
     *
     * @param phone_email
     * @param isexist
     */
    private void CheckAccountTask(String phone_email, String isexist)
    {
        new HttpPostOld(this, this, new InterfaceHttpResult()
        {         //1邮箱 2手机
            @Override
            public void getCallback(int requestCode, int code, String msg, Object baseBean)
            {
                if (dialog != null && dialog.isShowing())
                {
                    dialog.dismiss();
                }
                if (requestCode == 1000 && code == Constants.HTTP_CODE_SUCCESS)
                {
                    LoginDataBean.LoginBean bean = ((LoginDataBean) baseBean).getData();
                    spu.setUid(bean.getUid());
                    checkSuccess();
                } else if (code == 1004 || code == 1007)
                {
                    if (type.equals("oauthlogin"))
                    {
                        bindSecondActivity();
                    }
                } else if (code == Constants.HTTP_CODE_4000 || code == Constants.HTTP_CODE_5000)
                {
                    NetworkUtil.httpNetErrTip(BindEmailActivity.this, myLayout);
                } else {
                    errorProcess(msg);
                }

            }
        }, LoginDataBean.class, Urls.POST_REGISTER_CHECK_ACTION, phone_email, "1", isexist, "0", spu.getDevicedId()).excute(1000);
    }

    private void bindSecondActivity()
    {
        Intent intent = new Intent(BindEmailActivity.this, BindSecondActivity.class);
        intent.putExtra("from", "oauthemailreg");
        intent.putExtra("token", token);
        intent.putExtra("oauthtype", oauthtype);
        intent.putExtra("account", phone_email);
        startActivityForResult(intent, 1);
    }

    private void errorProcess(String msg)
    {
        if (!TextUtils.isEmpty(msg))
        {
            Toast.makeText(BindEmailActivity.this, msg, Toast.LENGTH_SHORT).show();
        }
    }

    private void checkSuccess()
    {
        if (type.equals("changeemail"))
        {//更换邮箱
            Intent intent = new Intent();
            intent.setClass(BindEmailActivity.this, BindSecondActivity.class);
            intent.putExtra("from", "changeemail");
            intent.putExtra("token", token);
            intent.putExtra("oauthtype", oauthtype);
            intent.putExtra("account", phone_email);
            startActivityForResult(intent, 1);
        } else if (type.equals("oauthlogin"))
        {//三方登陆先验证再绑定手机号
            Intent intent = new Intent();
            intent.setClass(BindEmailActivity.this, BindSecondActivity.class);
            intent.putExtra("from", "oauthbindemail");
            intent.putExtra("token", token);
            intent.putExtra("oauthtype", oauthtype);
            intent.putExtra("account", phone_email);
            startActivityForResult(intent, 1);
        } else if (type.equals("emailsafe"))
        {//绑定邮箱
            Intent intent = new Intent();
            intent.setClass(BindEmailActivity.this, BindSecondActivity.class);
            intent.putExtra("from", "bindemail");
            intent.putExtra("token", token);
            intent.putExtra("oauthtype", oauthtype);
            intent.putExtra("account", phone_email);
            startActivityForResult(intent, 1);
        } else
        {//绑定
            Intent intent = new Intent();
            intent.setClass(BindEmailActivity.this, BindSecondActivity.class);
            intent.putExtra("from", "bindemail");
            intent.putExtra("token", "");
            intent.putExtra("oauthtype", "");
            intent.putExtra("account", phone_email);
            startActivityForResult(intent, 1);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause()
    {
        /**
         * 页面结束（每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加）
         * 不能与StatService.onPageStart一级onPageEnd函数交叉使用
         */
//        StatService.onPause(this);
        super.onPause();
    }

    @Override
    public void onResume()
    {
        /**
         * 页面起始（每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加）
         * 不能与StatService.onPageStart一级onPageEnd函数交叉使用
         */
//        StatService.onResume(this);
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 1)
        {
            et_login_phone_email_auto.setText("");
        } else if (requestCode == 1 && resultCode == 2)
        {
            String info = data.getStringExtra("data");
            setResult(2, new Intent().putExtra("data", info));
            finish();
        } else if (requestCode == 1 && resultCode == 3)
        {
            setResult(3);
            finish();
        } else if (requestCode == 1 && resultCode == 4)
        {
            setResult(4);
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
        {
            finish();
            return true;
        }
        return false;
    }

    @Override
    public void finish()
    {
        super.finish();
        this.overridePendingTransition(R.anim.login_stay, R.anim.login_down_out);
    }

}

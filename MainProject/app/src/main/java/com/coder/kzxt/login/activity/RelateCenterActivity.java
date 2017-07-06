//package com.coder.kzxt.login.activity;
//
//import android.app.Dialog;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.CountDownTimer;
//import android.support.v7.widget.Toolbar;
//import android.text.TextUtils;
//import android.view.KeyEvent;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.app.http.HttpGetOld;
//import com.app.http.HttpPostOld;
//import com.app.http.InterfaceHttpResult;
//import com.coder.kzxt.activity.R;
//import com.coder.kzxt.login.beans.LoginDataBean;
//import com.coder.kzxt.login.beans.RelCenterBean;
//import com.coder.kzxt.base.activity.BaseActivity;
//import com.coder.kzxt.utils.Constants;
//import com.coder.kzxt.utils.GlideUtils;
//import com.coder.kzxt.utils.NetworkUtil;
//import com.coder.kzxt.utils.SharedPreferencesUtil;
//import com.coder.kzxt.utils.ToastUtils;
//import com.coder.kzxt.utils.Urls;
//import com.coder.kzxt.views.CustomNewDialog;
//
//import java.util.ArrayList;
//import java.util.Timer;
//import java.util.TimerTask;
//
//
///**
// * 本地登录关联云
// * Created by Administrator on 2017/3/2.
// */
//
//public class RelateCenterActivity extends BaseActivity {
//
//    private SharedPreferencesUtil spu;
//    private Dialog dialog;
//    private Dialog andialog;
//    private LinearLayout yun_account_ll;
//    private ImageView account_img;
//    private TextView yun_account;
//    private TextView warn_info;
//    private Button btn_login_submit;
//    private TextView yun_league;
//    private ImageView connect;
//    private TextView app_name;
//    private LinearLayout local_account_ll_one;
//    private ImageView local_account_img_one;
//    private TextView local_account_one;
//    private LinearLayout local_account_ll_two;
//    private ImageView local_account_img_two;
//    private TextView local_account_two;
//    private String localAccount;
//    private String localUserFace;
//    private RelativeLayout info;
//    private LinearLayout load_fail_layout;
//    private Button load_fail_button;
//    boolean oneclick = true;
//    boolean twoclick = true;
//    private boolean choose = true;
//    private Toolbar mToolbarView;
//    private RelativeLayout myLayout;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        this.overridePendingTransition(R.anim.login_up_in, R.anim.login_stay);
//        setContentView(R.layout.activity_relate);
//        spu = new SharedPreferencesUtil(this);
//
//        if (spu.getUserAccount().contains("@")) {//是邮箱登录，就显示邮箱
//            localAccount = spu.getEmail();
//        } else {//手机或学号登录优先显示手机
//            if (!TextUtils.isEmpty(spu.getPhone())) {
//                localAccount = spu.getPhone();
//            } else if (TextUtils.isEmpty(spu.getPhone())) {
//                localAccount = spu.getEmail();
//            }
//        }
//        localUserFace = spu.getUserHead();
//        initView();
//        initListener();
//    }
//
//    private void initView() {
//        mToolbarView = (Toolbar) findViewById(R.id.toolbar);
//        mToolbarView.setTitle(getResources().getString(R.string.relate_account));
//        setSupportActionBar(mToolbarView);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        load_fail_layout = (LinearLayout) findViewById(R.id.load_fail_layout);
//        load_fail_button = (Button) findViewById(R.id.load_fail_button);
//        info = (RelativeLayout) findViewById(R.id.info);
//        yun_league = (TextView) findViewById(R.id.yun_league);
//        yun_league.setText(getResources().getString(R.string.app_name));
//        account_img = (ImageView) findViewById(R.id.account_img);
//        yun_account = (TextView) findViewById(R.id.yun_account);
//        yun_account_ll = (LinearLayout) findViewById(R.id.yun_account_ll);
//        warn_info = (TextView) findViewById(R.id.warn_info);
//        btn_login_submit = (Button) findViewById(R.id.btn_login_submit);
//        yun_account.setText(localAccount);
//
//        connect = (ImageView) findViewById(R.id.connect);
//        app_name = (TextView) findViewById(R.id.app_name);
//        app_name.setText(getResources().getString(R.string.yun_league));
//        local_account_ll_one = (LinearLayout) findViewById(R.id.local_account_ll_one);
//        local_account_one = (TextView) findViewById(R.id.local_account_one);
//        local_account_img_one = (ImageView) findViewById(R.id.local_account_img_one);
//
//        local_account_ll_two = (LinearLayout) findViewById(R.id.local_account_ll_two);
//        local_account_img_two = (ImageView) findViewById(R.id.local_account_img_two);
//        local_account_two = (TextView) findViewById(R.id.local_account_two);
//        myLayout = (RelativeLayout) findViewById(R.id.rl_layout);
//    }
//
//    private void initListener() {
//        load_fail_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                load_fail_layout.setVisibility(View.GONE);
//                getRelateAcoountTask();
//            }
//        });
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                judge();
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        //先获取可关联的公开课联盟帐号
//        info.setVisibility(View.GONE);
//        getRelateAcoountTask();
//    }
//
//    private void getRelateAcoountTask() {
//        new HttpGetOld(this,this, new InterfaceHttpResult() {
//            @Override
//            public void getCallback(int requestCode, int code, String msg, Object baseBean) {
//                if (dialog != null && dialog.isShowing()) {
//                    dialog.dismiss();
//                }
//                if (requestCode == 1000 && code == Constants.HTTP_CODE_SUCCESS) {
//                    RelCenterBean bean = (RelCenterBean) baseBean;
//                    ArrayList<RelCenterBean.RelateCenterBean> centerBeens = bean.getData();
//                    initInfo(centerBeens);
//                } else if (code == Constants.HTTP_CODE_4000 || code == Constants.HTTP_CODE_5000) {
//                    NetworkUtil.httpNetErrTip(RelateCenterActivity.this, myLayout);
//                } else {
//                    errorProcess(msg);
//                }
//            }
//        }, RelCenterBean.class, Urls.GET_RELCENTER_USER_ACTION,spu.getUid(),spu.getUserAccount(),
//                spu.getIsLogin(),spu.getTokenSecret(),spu.getDevicedId()).excute(1000);
//    }
//
//    private void errorProcess(String code) {
//        if (code != null) {
//            if (code.equals("2001") || code.equals("2004")) {
//
//            }
//        }
//        info.setVisibility(View.GONE);
//        load_fail_layout.setVisibility(View.VISIBLE);
//    }
//
//
//    private void select() {
//        if (oneclick || twoclick) {
//            btn_login_submit.setAlpha(1.0f);
//            btn_login_submit.setClickable(true);
//        } else {
//            btn_login_submit.setAlpha(0.5f);
//            btn_login_submit.setClickable(false);
//        }
//    }
//
//    CustomNewDialog customNewDialog;
//    Button get_autn_code_button;
//
//    private void initInfo(final ArrayList<RelCenterBean.RelateCenterBean> centerBeens) {
//
//        if (centerBeens.size() == 0) {//没有相关账号   需要新建
//            mToolbarView.setTitle(getResources().getString(R.string.create_new_account));
//            connect.setBackgroundResource(R.drawable.yun_copy);
//            warn_info.setText(getResources().getString(R.string.create_yun_account));
//            btn_login_submit.setText(getResources().getString(R.string.create_new_account));
//
//            local_account_one.setText(localAccount);
//            GlideUtils.loadCircleHeaderOfCommon(RelateCenterActivity.this,localUserFace,local_account_img_one);
//        } else if (centerBeens.size() == 2) {//有2个可关联，任选其一
//            btn_login_submit.setAlpha(0.5f);
//            btn_login_submit.setClickable(false);
//
//            local_account_ll_one.setClickable(true);
//            local_account_ll_two.setClickable(true);
//            local_account_ll_two.setVisibility(View.VISIBLE);
//            mToolbarView.setTitle(getResources().getString(R.string.relate_account));
//            connect.setBackgroundResource(R.drawable.yun_connect);
//            warn_info.setText(getResources().getString(R.string.choose_yun_account));
//            btn_login_submit.setText(getResources().getString(R.string.relate_account));
//
//            if (localAccount.contains("@")) {//邮箱登录优先显示邮箱
//                if (TextUtils.isEmpty(centerBeens.get(0).getEmail())) {
//                    local_account_one.setText(centerBeens.get(0).getMobile());
//                } else {
//                    local_account_one.setText(centerBeens.get(0).getEmail());
//                }
//
//                GlideUtils.loadCircleHeaderOfCommon(RelateCenterActivity.this,centerBeens.get(0).getUserface(),local_account_img_one);
//                if (TextUtils.isEmpty(centerBeens.get(1).getEmail())) {
//                    local_account_two.setText(centerBeens.get(1).getMobile());
//                } else {
//                    local_account_two.setText(centerBeens.get(1).getEmail());
//                }
//                GlideUtils.loadCircleHeaderOfCommon(RelateCenterActivity.this,centerBeens.get(1).getUserface(),local_account_img_two);
//            } else {//学号或手机登录优先显示手机
//                if (TextUtils.isEmpty(centerBeens.get(0).getMobile())) {
//                    local_account_one.setText(centerBeens.get(0).getEmail());
//                } else {
//                    local_account_one.setText(centerBeens.get(0).getMobile());
//                }
//                GlideUtils.loadCircleHeaderOfCommon(RelateCenterActivity.this,centerBeens.get(0).getUserface(),local_account_img_one);
//                if (TextUtils.isEmpty(centerBeens.get(1).getMobile())) {
//                    local_account_two.setText(centerBeens.get(1).getEmail());
//                } else {
//                    local_account_two.setText(centerBeens.get(1).getMobile());
//                }
//
//                GlideUtils.loadCircleHeaderOfCommon(RelateCenterActivity.this,centerBeens.get(1).getUserface(),local_account_img_two);
//            }
//        } else if (centerBeens.size() == 1) {//只找到一个可关联账号，则分情况
//            local_account_ll_one.setClickable(false);
//            local_account_ll_one.setSelected(false);
//            if (TextUtils.isEmpty(centerBeens.get(0).getBindLocalUser().getUid())) {//没有绑定本地账号，直接关联即可
//                mToolbarView.setTitle(getResources().getString(R.string.relate_account));
//                connect.setBackgroundResource(R.drawable.yun_connect);
//                warn_info.setText(getResources().getString(R.string.relate_yun_account));
//                btn_login_submit.setText(getResources().getString(R.string.relate_account));
//
//                if (localAccount.contains("@")) {//邮箱登录优先显示邮箱
//                    if (TextUtils.isEmpty(centerBeens.get(0).getEmail())) {
//                        local_account_one.setText(centerBeens.get(0).getMobile());
//                    } else {
//                        local_account_one.setText(centerBeens.get(0).getEmail());
//                    }
//                    GlideUtils.loadCircleHeaderOfCommon(RelateCenterActivity.this,centerBeens.get(0).getUserface(),local_account_img_one);
//                } else {
//                    if (TextUtils.isEmpty(centerBeens.get(0).getMobile())) {
//                        local_account_one.setText(centerBeens.get(0).getEmail());
//                    } else {
//                        local_account_one.setText(centerBeens.get(0).getMobile());
//                    }
//                    GlideUtils.loadCircleHeaderOfCommon(RelateCenterActivity.this,centerBeens.get(0).getUserface(),local_account_img_one);
//                }
//            } else {//已关联过本地，需要解绑
//                local_account_ll_one.setBackgroundResource(R.drawable.yun_disconnect_edit);
//                mToolbarView.setTitle(getResources().getString(R.string.relate_account));
//                connect.setBackgroundResource(R.drawable.yun_cannot_connect);
//                warn_info.setText(getResources().getString(R.string.disconnect_account_left) + getResources().getString(R.string.app_name) + getResources().getString(R.string.disconnect_account_right));
//                warn_info.setTextColor(getResources().getColor(R.color.font_red));
//                btn_login_submit.setText(getResources().getString(R.string.btn_disconnect));
//                btn_login_submit.setBackgroundResource(R.drawable.yun_button_unbind);
//                local_account_one.setTextColor(getResources().getColor(R.color.font_red));
//                if (localAccount.contains("@")) {//邮箱登录优先显示邮箱
//                    if (TextUtils.isEmpty(centerBeens.get(0).getEmail())) {
//                        local_account_one.setText(centerBeens.get(0).getMobile());
//                    } else {
//                        local_account_one.setText(centerBeens.get(0).getEmail());
//                    }
//                    GlideUtils.loadCircleHeaderOfCommon(RelateCenterActivity.this,centerBeens.get(0).getUserface(),local_account_img_one);
//                } else {
//                    if (TextUtils.isEmpty(centerBeens.get(0).getMobile())) {
//                        local_account_one.setText(centerBeens.get(0).getEmail());
//                    } else {
//                        local_account_one.setText(centerBeens.get(0).getMobile());
//                    }
//                    GlideUtils.loadCircleHeaderOfCommon(RelateCenterActivity.this,centerBeens.get(0).getUserface(),local_account_img_one);
//                }
//            }
//        }
//        local_account_ll_one.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (oneclick) {
//                    choose = true;//选择标记，用于获取UID
//                    local_account_ll_one.setBackgroundResource(R.drawable.edit_selected);
//                    local_account_one.setTextColor(getResources().getColor(R.color.font_blue));
//                    local_account_ll_two.setBackgroundResource(R.drawable.edit_not_selected);
//                    local_account_two.setTextColor(getResources().getColor(R.color.font_black));
//                    select();
//                } else {
//                    local_account_one.setTextColor(getResources().getColor(R.color.font_black));
//                    local_account_ll_one.setBackgroundResource(R.drawable.edit_not_selected);
//                }
//            }
//        });
//        local_account_ll_two.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (twoclick) {
//                    choose = false;
//                    local_account_ll_two.setBackgroundResource(R.drawable.edit_selected);
//                    local_account_two.setTextColor(getResources().getColor(R.color.font_blue));
//                    local_account_ll_one.setBackgroundResource(R.drawable.edit_not_selected);
//                    local_account_one.setTextColor(getResources().getColor(R.color.font_black));
//                    select();
//                } else {
//                    local_account_two.setTextColor(getResources().getColor(R.color.font_black));
//                    local_account_ll_two.setBackgroundResource(R.drawable.edit_not_selected);
//                }
//            }
//        });
//        btn_login_submit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (btn_login_submit.getText().equals(getResources().getString(R.string.create_new_account))) {//新建
//                    final CustomNewDialog customNewDialog = new CustomNewDialog(RelateCenterActivity.this, R.layout.create_account_info);
//                    customNewDialog.show();
//                    ImageView account_img = (ImageView) customNewDialog.findViewById(R.id.account_img);
//                    GlideUtils.loadCircleHeaderOfCommon(RelateCenterActivity.this,localUserFace,account_img);
//                    TextView tv_account = (TextView) customNewDialog.findViewById(R.id.tv_account);
//                    tv_account.setText(localAccount);
//                    TextView warn_info = (TextView) customNewDialog.findViewById(R.id.warn_info);
//                    warn_info.setText(getResources().getString(R.string.yun_account_same_left) + getResources().getString(R.string.app_name) + getResources().getString(R.string.yun_account_same_right));
//                    TextView make_sure = (TextView) customNewDialog.findViewById(R.id.make_sure);
//                    make_sure.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            if(customNewDialog != null && customNewDialog.isShowing()){
//                                customNewDialog.cancel();
//                            }
//                            createBindAccountTask();
//                        }
//                    });
//                } else if (btn_login_submit.getText().equals(getResources().getString(R.string.btn_disconnect))) {//解绑
//                    final String phone_email;
//                    customNewDialog = new CustomNewDialog(RelateCenterActivity.this, R.layout.dlg_unbindaccount);
//                    customNewDialog.show();
//                    ImageView account_img = (ImageView) customNewDialog.findViewById(R.id.account_img);
//                    GlideUtils.loadCircleHeaderOfCommon(RelateCenterActivity.this,centerBeens.get(0).getUserface(),account_img);
//                    TextView yun_account = (TextView) customNewDialog.findViewById(R.id.yun_account);
//                    if (TextUtils.isEmpty(centerBeens.get(0).getMobile())) {
//                        yun_account.setText(centerBeens.get(0).getEmail());
//                        phone_email = centerBeens.get(0).getEmail();
//                    } else {
//                        yun_account.setText(centerBeens.get(0).getMobile());
//                        phone_email = centerBeens.get(0).getMobile();
//                    }
//
//                    TextView app_name = (TextView) customNewDialog.findViewById(R.id.app_name);
//                    ImageView local_account_img_one = (ImageView) customNewDialog.findViewById(R.id.local_account_img_one);
//                    GlideUtils.loadCircleHeaderOfCommon(RelateCenterActivity.this,centerBeens.get(0).getBindLocalUser().getUserface(),local_account_img_one);
//                    TextView local_account_one = (TextView) customNewDialog.findViewById(R.id.local_account_one);
//
//                    if (TextUtils.isEmpty(centerBeens.get(0).getBindLocalUser().getMobile())) {
//                        local_account_one.setText(centerBeens.get(0).getBindLocalUser().getEmail());
//                    } else {
//                        local_account_one.setText(centerBeens.get(0).getBindLocalUser().getMobile());
//                    }
//                    getAuthCode(phone_email,"1");
//                    final EditText et_auth_code = (EditText) customNewDialog.findViewById(R.id.et_auth_code);
//                    et_auth_code.setFocusable(true);
//                    Timer timer = new Timer();
//                    timer.schedule(new TimerTask() {
//                        @Override
//                        public void run() {
//                            InputMethodManager imm = (InputMethodManager) et_auth_code.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                            imm.showSoftInput(et_auth_code, 0);
//                        }
//                    }, 500);
//                    get_autn_code_button = (Button) customNewDialog.findViewById(R.id.get_autn_code_button);
//                    get_autn_code_button.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            et_auth_code.setText("");
//                            getAuthCode(phone_email,"1");
//                        }
//                    });
//                    TextView cancel = (TextView) customNewDialog.findViewById(R.id.cancel);
//                    cancel.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            customNewDialog.dismiss();
//                        }
//                    });
//                    TextView make_sure = (TextView) customNewDialog.findViewById(R.id.make_sure);
//                    make_sure.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            String center = centerBeens.get(0).getUid();
//                            String local = centerBeens.get(0).getBindLocalUser().getUid();
//                            String code = et_auth_code.getText().toString().trim();
//                            unBindAccountTask(center,local,code,centerBeens);
//                        }
//                    });
//                } else if (btn_login_submit.getText().equals(getResources().getString(R.string.relate_account))) {//关联
//                    String center = "";
//                    if (centerBeens.size() == 1) {
//                        center = centerBeens.get(0).getUid();
//                    } else if (centerBeens.size() == 2) {
//                        if (choose) {//选择第一个
//                            center = centerBeens.get(0).getUid();
//                        } else {
//                            center = centerBeens.get(1).getUid();
//                        }
//                    }
//                    String local = String.valueOf(spu.getUid());
//                    relateAccountTask(center, local);
//                }
//            }
//        });
//    }
//
//    /**
//     * 绑定账号
//     * @param center
//     * @param local
//     */
//    private void relateAccountTask(String center, String local) {
//        new HttpPostOld(this,this, new InterfaceHttpResult() {
//            @Override
//            public void getCallback(int requestCode, int code, String msg, Object baseBean) {
//                if (dialog != null && dialog.isShowing()) {
//                    dialog.dismiss();
//                }
//                if (requestCode == 1000 && code == Constants.HTTP_CODE_SUCCESS) {
//                    LoginDataBean.LoginBean bean = ((LoginDataBean) baseBean).getData();
//                    ToastUtils.makeText(RelateCenterActivity.this, getResources().getString(R.string.relate_success), Toast.LENGTH_SHORT);
//                    bindSuccess(bean);
//                    loginIm(bean);
//                    otherProcess();
//                } else if (code == Constants.HTTP_CODE_4000 || code == Constants.HTTP_CODE_5000) {
//                    NetworkUtil.httpNetErrTip(RelateCenterActivity.this, myLayout);
//                } else {
//                    netErrorProcess(msg);
//                }
//            }
//        }, LoginDataBean.class, Urls.POST_BIND_ACCOUNT_ACTION,spu.getUserAccount(),local,center,spu.getDevicedId()).excute(1000);
//
//    }
//
//    /**
//     * 解绑账号
//     * @param center
//     * @param local
//     * @param code
//     */
//    private void unBindAccountTask(String center, String local, String code,final ArrayList<RelCenterBean.RelateCenterBean> centerBeens) {
//        new HttpPostOld(this,this, new InterfaceHttpResult() {
//            @Override
//            public void getCallback(int requestCode, int code, String msg, Object baseBean) {
//                if (dialog != null && dialog.isShowing()) {
//                    dialog.dismiss();
//                }
//                if (requestCode == 1000 && code == Constants.HTTP_CODE_SUCCESS) {
//                    unBindSuccess(centerBeens);
//                }else if (code == Constants.HTTP_CODE_4000 || code == Constants.HTTP_CODE_5000) {
//                    NetworkUtil.httpNetErrTip(RelateCenterActivity.this, myLayout);
//                } else {
//                    netErrorProcess(msg);
//                }
//            }
//        }, null, Urls.POST_UNBIND_ACCOUNT_ACTION,local,center,code,spu.getDevicedId()).excute(1000);
//
//    }
//
//    private void unBindSuccess(final ArrayList<RelCenterBean.RelateCenterBean> centerBeens) {
//        customNewDialog.dismiss();
//        ToastUtils.makeText(RelateCenterActivity.this,getResources().getString(R.string.unbind_success),Toast.LENGTH_SHORT);
//        connect.setBackgroundResource(R.drawable.yun_connect);
//        warn_info.setText(getResources().getString(R.string.relate_yun_account));
//        warn_info.setTextColor(getResources().getColor(R.color.font_gray));
//        btn_login_submit.setText(getResources().getString(R.string.relate_account));
//        local_account_one.setTextColor(getResources().getColor(R.color.font_black));
//        local_account_ll_one.setBackgroundResource(R.drawable.edit_not_selected);
//        btn_login_submit.setBackgroundResource(R.drawable.yun_button);
//        if(localAccount.contains("@")) {//邮箱登录优先显示邮箱
//            if (TextUtils.isEmpty(centerBeens.get(0).getEmail())) {
//                local_account_one.setText(centerBeens.get(0).getMobile());
//            } else {
//                local_account_one.setText(centerBeens.get(0).getEmail());
//            }
//            GlideUtils.loadCircleHeaderOfCommon(RelateCenterActivity.this,centerBeens.get(0).getUserface(),local_account_img_one);
//        }else{
//            if (TextUtils.isEmpty(centerBeens.get(0).getMobile())) {
//                local_account_one.setText(centerBeens.get(0).getEmail());
//            } else {
//                local_account_one.setText(centerBeens.get(0).getMobile());
//            }
//            GlideUtils.loadCircleHeaderOfCommon(RelateCenterActivity.this,centerBeens.get(0).getUserface(),local_account_img_one);
//        }
//
//        btn_login_submit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String center = centerBeens.get(0).getUid();
//                String local = String.valueOf(spu.getUid());
//                relateAccountTask(center, local);
//            }
//        });
//
//    }
//
//
//    private void createBindAccountTask() {
//        new HttpPostOld(this,this, new InterfaceHttpResult() {
//            @Override
//            public void getCallback(int requestCode, int code, String msg, Object baseBean) {
//                if (dialog != null && dialog.isShowing()) {
//                    dialog.dismiss();
//                }
//                if (requestCode == 1000 && code == Constants.HTTP_CODE_SUCCESS) {
//                    LoginDataBean.LoginBean bean = ((LoginDataBean) baseBean).getData();
//                    bindSuccess(bean);
//                    loginIm(bean);
//                    otherProcess();
//                }else if (code == Constants.HTTP_CODE_4000 || code == Constants.HTTP_CODE_5000) {
//                    NetworkUtil.httpNetErrTip(RelateCenterActivity.this, myLayout);
//                } else {
//                    netErrorProcess(msg);
//                    spu.clearUserInfo();
//                 }
//            }
//        }, LoginDataBean.class, Urls.POST_CREATE_BIND_ACCOUNT_ACTION,localAccount,spu.getUid(),"1",spu.getDevicedId()).excute(1000);
//
//    }
//
//    private void netErrorProcess(String msg) {
//        if (!TextUtils.isEmpty(msg)) {
//            ToastUtils.makeText(RelateCenterActivity.this, msg, Toast.LENGTH_SHORT);
//        }
//    }
//
//    private void bindSuccess(LoginDataBean.LoginBean bean) {
//        saveLoginData(bean);
//    }
//
//    private void otherProcess() {
//        Intent intent = new Intent(Constants.REF_USERINFO);
//        sendBroadcast(intent);
//        setResult(2);
//        finish();
//    }
//
//    private void loginIm(LoginDataBean.LoginBean bean) {
////        String str = spu.getDevicedId() + spu.getIsLogin();
////        String encodeStr = Base64.encodeToString(str.getBytes(), Base64.DEFAULT);
////        spu.setIsLogin(encodeStr);
// //        new LoginImAsynTask(RelateCenterActivity.this).execute();
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
//
//    /**
//     * 获取验证码
//     * @param account
//     * @param type
//     */
//    private void getAuthCode(String account, String type) {
//        new HttpPostOld(this,this, new InterfaceHttpResult() {
//            @Override
//            public void getCallback(int requestCode, int code, String msg, Object baseBean) {
//                if (dialog != null && dialog.isShowing()) {
//                    dialog.dismiss();
//                }
//                if (requestCode == 1000 && code == Constants.HTTP_CODE_SUCCESS) {
//                    TimeCount timeCount= new TimeCount(60000, 1000);
//                    timeCount.start();
//                    ToastUtils.makeText(RelateCenterActivity.this, getResources().getString(R.string.auth_code_info), Toast.LENGTH_SHORT);
//                } else if (code == Constants.HTTP_CODE_4000 || code == Constants.HTTP_CODE_5000) {
//                    NetworkUtil.httpNetErrTip(RelateCenterActivity.this, myLayout);
//                } else {
//                    netErrorProcess(msg);
//                }
//            }
//        }, null, Urls.POST_PHONE_CODE_ACTION, account, type,spu.getDevicedId()).excute(1000);
//
//    }
//
//    class TimeCount extends CountDownTimer {
//        public TimeCount(long millisInFuture, long countDownInterval) {
//            super(millisInFuture, countDownInterval);
//        }
//
//        @Override
//        public void onTick(long millisUntilFinished) {
//            get_autn_code_button.setTextColor(getResources().getColor(R.color.font_gray));
//            get_autn_code_button.setBackgroundResource(R.drawable.round_getdynamic);
//            get_autn_code_button.setClickable(false);
//            get_autn_code_button.setText(millisUntilFinished / 1000 + getResources().getString(R.string.second));
//        }
//
//        @Override
//        public void onFinish() {
//            get_autn_code_button.setTextColor(getResources().getColor(R.color.first_theme));
//            get_autn_code_button.setText(getResources().getString(R.string.get_auth_code_again));
//            get_autn_code_button.setClickable(true);
//            get_autn_code_button.setBackgroundResource(R.drawable.round_setdynamic);
//        }
//    }
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
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
//            judge();
//            return true;
//        }
//        return false;
//    }
//
//    @Override
//    public void finish() {
//        super.finish();
//        this.overridePendingTransition(R.anim.login_stay, R.anim.login_down_out);
//    }
//
//    private void judge() {
//        final CustomNewDialog customNewDialog = new CustomNewDialog(RelateCenterActivity.this, R.layout.yun_exit_info);
//        TextView yun_exit_cancel = (TextView) customNewDialog.findViewById(R.id.yun_exit_cancel);
//        TextView yun_exit_sure = (TextView) customNewDialog.findViewById(R.id.yun_exit_sure);
//        customNewDialog.show();
//        yun_exit_cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                customNewDialog.cancel();
//            }
//        });
//        yun_exit_sure.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                customNewDialog.cancel();
//                setResult(1);
//                finish();
//            }
//        });
//    }
//
//}

//package com.coder.kzxt.login.activity;
//
//import android.app.Dialog;
//import android.content.Context;
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
//import com.app.http.HttpPostOld;
//import com.app.http.InterfaceHttpResult;
//import com.coder.kzxt.activity.R;
//import com.coder.kzxt.login.beans.LoginDataBean;
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
///**
// * 解绑本地账号，然后关联
// * Created by Administrator on 2017/3/6.
// */
//
//public class UnbindLocalActivity extends BaseActivity {
//
////    private HashMap<String,String> centerMap=new HashMap<>();
////    private HashMap<String,String> bindCenterMap=new HashMap<>();
////    private ArrayList<HashMap<String,String>> arrayList=new ArrayList<>();
////    private ImageLoader imageLoader;
//    private Dialog dialog;
//    private SharedPreferencesUtil spu;
////    private TextView title;
////    private ImageView rightImage;
//
//    private ImageView account_img;
//    private TextView yun_account;
//    private TextView warn_info;
//    private ImageView connect;
//    private Button btn_login_submit;
//
//    private LinearLayout local_account_ll_one;
//    private ImageView local_account_img_one;
//    private TextView local_account_one;
//    private LoginDataBean.CloudCenterUser centerBean;
//    private LoginDataBean.CloundCenterBindUser bindBean;
//    private ArrayList<LoginDataBean.CloundCenterUnbindUser> cloundCenterUnbindUsers;
//    private Toolbar mToolbarView;
//    private RelativeLayout myLayout;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        this.overridePendingTransition(R.anim.login_up_in,R.anim.login_stay);
//        setContentView(R.layout.activity_relate);
////        MyApplication.list.add(this);
//        spu = new SharedPreferencesUtil(this);
////        imageLoader=ImageLoader.getInstance();
////        handler = MyApplication.getInstance().getHandler();
////        centerMap= (HashMap<String, String>) getIntent().getSerializableExtra("centerMap");//登录的云账号
////        bindCenterMap= (HashMap<String, String>) getIntent().getSerializableExtra("bindCenter");//已关联的云账号
////        arrayList= (ArrayList<HashMap<String, String>>) getIntent().getSerializableExtra("arrayList");//已关联的本地账号
//
//        centerBean = (LoginDataBean.CloudCenterUser)getIntent().getSerializableExtra("centerBean");
//        bindBean = (LoginDataBean.CloundCenterBindUser)getIntent().getSerializableExtra("bindCenter");
//        cloundCenterUnbindUsers = ( ArrayList<LoginDataBean.CloundCenterUnbindUser> )getIntent().getSerializableExtra("arrayList");
//
//        initView();
//        initListener();
//    }
//    private void initView(){
//        mToolbarView = (Toolbar) findViewById(R.id.toolbar);
//        mToolbarView.setTitle(getResources().getString(R.string.relate_account));
//        setSupportActionBar(mToolbarView);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        account_img= (ImageView) findViewById(R.id.account_img);
//        yun_account= (TextView) findViewById(R.id.yun_account);
//        warn_info= (TextView) findViewById(R.id.warn_info);
//        connect= (ImageView) findViewById(R.id.connect);
//        btn_login_submit= (Button) findViewById(R.id.btn_login_submit);
//        yun_account.setText(centerBean.getAccount());
////        imageLoader.displayImage(centerMap.get("centerUserFace"),account_img, ImageLoaderOptions.headerOptions);
//        GlideUtils.loadCircleHeaderOfCommon(this,centerBean.getUserface(),account_img);
//        local_account_ll_one= (LinearLayout) findViewById(R.id.local_account_ll_one);
//        local_account_one= (TextView) findViewById(R.id.local_account_one);
//        local_account_img_one= (ImageView) findViewById(R.id.local_account_img_one);
//
////        imageLoader.displayImage(arrayList.get(0).get("unbindUserFace"),local_account_img_one,ImageLoaderOptions.headerOptions);
//        GlideUtils.loadCircleHeaderOfCommon(this,cloundCenterUnbindUsers.get(0).getUserface(),local_account_img_one);
//        local_account_one.setText(cloundCenterUnbindUsers.get(0).getAccount());
//        local_account_one.setTextColor(getResources().getColor(R.color.font_red));
//
//        warn_info.setText(getResources().getString(R.string.disconnect_local_account_left)+getResources().getString(R.string.app_name)+getResources().getString(R.string.disconnect_local_account_right));
//        warn_info.setTextColor(getResources().getColor(R.color.font_red));
//        connect.setBackgroundResource(R.drawable.yun_cannot_connect);
//        local_account_ll_one.setBackgroundResource(R.drawable.yun_disconnect_edit);
//        btn_login_submit.setText(getResources().getString(R.string.btn_disconnect));
//        btn_login_submit.setBackgroundResource(R.drawable.yun_button_unbind);
//        myLayout = (RelativeLayout) findViewById(R.id.rl_layout);
//    }
//    private void initType(){
//        warn_info.setText(getResources().getString(R.string.relate_account_left)+getResources().getString(R.string.app_name)+getResources().getString(R.string.relate_account_right));
//        warn_info.setTextColor(getResources().getColor(R.color.font_gray));
//        local_account_one.setText(cloundCenterUnbindUsers.get(0).getAccount());
////        imageLoader.displayImage(arrayList.get(0).get("unbindUserFace"),local_account_img_one,ImageLoaderOptions.headerOptions);
//        GlideUtils.loadCircleHeaderOfCommon(this,cloundCenterUnbindUsers.get(0).getUserface(),local_account_img_one);
//    }
//    private void judge(){
//        final CustomNewDialog customNewDialog=new CustomNewDialog(UnbindLocalActivity.this,R.layout.yun_exit_info);
//        TextView yun_exit_cancel= (TextView) customNewDialog.findViewById(R.id.yun_exit_cancel);
//        TextView yun_exit_sure=(TextView)customNewDialog.findViewById(R.id.yun_exit_sure);
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
//    CustomNewDialog customNewDialog;
//    Button get_autn_code_button;
//    private void initListener(){
//
//        btn_login_submit.setOnClickListener(new View.OnClickListener() {
//            String phone_email;
//            @Override
//            public void onClick(View v) {
//                customNewDialog = new CustomNewDialog(UnbindLocalActivity.this, R.layout.dlg_unbindaccount);
//                customNewDialog.show();
//
//                phone_email = cloundCenterUnbindUsers.get(0).getAccount();
////                new GetAuthCodeAsy(phone_email).executeOnExecutor(Constants.exec);
//                getAuthCodeTask(phone_email);
//
//                TextView unbind_league = (TextView) customNewDialog.findViewById(R.id.unbind_league);
//                unbind_league.setText(getResources().getString(R.string.app_name));
//                TextView app_name = (TextView) customNewDialog.findViewById(R.id.app_name);
//                app_name.setText(getResources().getString(R.string.yun_league));
//                ImageView account_img = (ImageView) customNewDialog.findViewById(R.id.account_img);
////                imageLoader.displayImage(arrayList.get(0).get("unbindUserFace"),account_img,ImageLoaderOptions.headerOptions);
//
//                GlideUtils.loadCircleHeaderOfCommon(UnbindLocalActivity.this,cloundCenterUnbindUsers.get(0).getUserface(),account_img);
//                TextView yun_account = (TextView) customNewDialog.findViewById(R.id.yun_account);
//                yun_account.setText(cloundCenterUnbindUsers.get(0).getAccount());
//
//                ImageView local_account_img_one = (ImageView) customNewDialog.findViewById(R.id.local_account_img_one);
////                imageLoader.displayImage(bindCenterMap.get("bindCenterUserFace"),local_account_img_one,ImageLoaderOptions.headerOptions);
//                GlideUtils.loadCircleHeaderOfCommon(UnbindLocalActivity.this,bindBean.getUserface(),local_account_img_one);
//                TextView local_account_one = (TextView) customNewDialog.findViewById(R.id.local_account_one);
//                local_account_one.setText(bindBean.getAccount());
//                final EditText et_auth_code = (EditText) customNewDialog.findViewById(R.id.et_auth_code);
//                et_auth_code.setFocusable(true);
//                Timer timer = new Timer();
//                timer.schedule(new TimerTask() {
//                    @Override
//                    public void run() {
//                        InputMethodManager imm = (InputMethodManager) et_auth_code.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                        imm.showSoftInput(et_auth_code, 0);
//                    }
//                }, 500);
//                get_autn_code_button = (Button) customNewDialog.findViewById(R.id.get_autn_code_button);
//                get_autn_code_button.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
////                        new GetAuthCodeAsy(phone_email).executeOnExecutor(Constants.exec);
//                        getAuthCodeTask(phone_email);
//
//                    }
//                });
//                TextView cancel = (TextView) customNewDialog.findViewById(R.id.cancel);
//                cancel.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        customNewDialog.dismiss();
//                    }
//                });
//                TextView make_sure = (TextView) customNewDialog.findViewById(R.id.make_sure);
//                make_sure.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        String center = bindBean.getUid();
//                        String local = cloundCenterUnbindUsers.get(0).getUid();
//                        String code = et_auth_code.getText().toString().trim();
////                        new UnbindAccountAsy(center, local, code).executeOnExecutor(Constants.exec);
//                        unBindAccountTask(center,local,code);
//                    }
//                });
//            }
//        });
//    }
//
//    /**
//     * 解绑账号
//     * @param center
//     * @param local
//     * @param code
//     */
//    private void unBindAccountTask(String center, String local, String code) {
//        new HttpPostOld(this,this, new InterfaceHttpResult() {
//            @Override
//            public void getCallback(int requestCode, int code, String msg, Object baseBean) {
//                if (dialog != null && dialog.isShowing()) {
//                    dialog.dismiss();
//                }
//                if (requestCode == 1000 && code == Constants.HTTP_CODE_SUCCESS) {
//                    unBindSuccess();
//                }else if (code == Constants.HTTP_CODE_4000 || code == Constants.HTTP_CODE_5000) {
//                    NetworkUtil.httpNetErrTip(UnbindLocalActivity.this, myLayout);
//                } else {
//                    errorProcess(msg);
//                }
//            }
//        }, null, Urls.POST_UNBIND_ACCOUNT_ACTION,local,center,code,spu.getDevicedId()).excute(1000);
//
//    }
//
//    private void unBindSuccess() {
//        ToastUtils.makeText(UnbindLocalActivity.this,getResources().getString(R.string.unbind_success), Toast.LENGTH_SHORT);
//        if(customNewDialog != null && customNewDialog.isShowing()){
//            customNewDialog.cancel();
//        }
//        initType();
//
//        connect.setBackgroundResource(R.drawable.yun_connect);
//        btn_login_submit.setText(getResources().getString(R.string.relate_account));
//        btn_login_submit.setBackgroundResource(R.drawable.yun_button);
//
//        yun_account.setText(centerBean.getAccount());
////        imageLoader.displayImage(centerMap.get("centerUserFace"),account_img,ImageLoaderOptions.headerOptions);
//
//        GlideUtils.loadCircleHeaderOfCommon(UnbindLocalActivity.this,centerBean.getUserface(),account_img);
//
////        imageLoader.displayImage(arrayList.get(0).get("unbindUserFace"),local_account_img_one,ImageLoaderOptions.headerOptions);
//        GlideUtils.loadCircleHeaderOfCommon(UnbindLocalActivity.this,cloundCenterUnbindUsers.get(0).getUserface(),local_account_img_one);
//
//        local_account_one.setText(cloundCenterUnbindUsers.get(0).getAccount());
//        local_account_one.setTextColor(getResources().getColor(R.color.font_black));
//        local_account_ll_one.setBackgroundResource(R.drawable.edit_not_selected);
//        local_account_ll_one.setClickable(false);
//
//        btn_login_submit.setOnClickListener(new View.OnClickListener() {
//            String center = centerBean.getUid();
//            String local = cloundCenterUnbindUsers.get(0).getUid();
//            String account = centerBean.getAccount();
//            @Override
//            public void onClick(View v) {
////                new RelateAccountAsy(center,local).executeOnExecutor(Constants.exec);
//                bindAccountTask(account,center,local);
//            }
//        });
//    }
//
//    private void bindAccountTask(String account,String center, String local) {
//        new HttpPostOld(this,this, new InterfaceHttpResult() {
//            @Override
//            public void getCallback(int requestCode, int code, String msg, Object baseBean) {
//                if (dialog != null && dialog.isShowing()) {
//                    dialog.dismiss();
//                }
//                if (requestCode == 1000 && code == Constants.HTTP_CODE_SUCCESS) {
//                    LoginDataBean.LoginBean bean = ((LoginDataBean) baseBean).getData();
//                    bindSuccess(bean);
//                } else if (code == Constants.HTTP_CODE_4000 || code == Constants.HTTP_CODE_5000) {
//                    NetworkUtil.httpNetErrTip(UnbindLocalActivity.this, myLayout);
//                } else {
//                    errorProcess(msg);
//                }
//            }
//        }, LoginDataBean.class, Urls.POST_BIND_ACCOUNT_ACTION,account,local,center,spu.getDevicedId()).excute(1000);
//    }
//
//    private void bindSuccess(LoginDataBean.LoginBean bean) {
//        ToastUtils.makeText(UnbindLocalActivity.this,getResources().getString(R.string.relate_success), Toast.LENGTH_SHORT).show();
//        saveLoginData(bean);
//        otherProcess(bean);
//    }
//
//    private void otherProcess(LoginDataBean.LoginBean bean) {
////        String str = spu.getDevicedId() + bean.getOauth_token();
////        String encodeStr = Base64.encodeToString(str.getBytes(), Base64.DEFAULT);
////        spu.setIsLogin(encodeStr);
// //        new LoginImAsynTask(RelateLocalActivity.this).execute();
//        setResult(2);
//        finish();
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
//    private void getAuthCodeTask(String account) {
//        new HttpPostOld(this,this, new InterfaceHttpResult() {
//            @Override
//            public void getCallback(int requestCode, int code, String msg, Object baseBean) {
//                if (dialog != null && dialog.isShowing()) {
//                    dialog.dismiss();
//                }
//                if (requestCode == 1000 && code == Constants.HTTP_CODE_SUCCESS) {
//                    TimeCount timeCount= new TimeCount(60000, 1000);
//                    timeCount.start();
//                    ToastUtils.makeText(UnbindLocalActivity.this, getResources().getString(R.string.auth_code_to_local), Toast.LENGTH_SHORT).show();
//                } else if (code == Constants.HTTP_CODE_4000 || code == Constants.HTTP_CODE_5000) {
//                    NetworkUtil.httpNetErrTip(UnbindLocalActivity.this, myLayout);
//                } else {
//                    errorProcess(msg);
//                }
//            }
//        }, null, Urls.POST_PHONE_CODE_ACTION, account, "1",spu.getDevicedId()).excute(1000);
//
//    }
//
//    private void errorProcess(String msg) {
//        if (!TextUtils.isEmpty(msg)) {
//            Toast.makeText(UnbindLocalActivity.this, msg, Toast.LENGTH_SHORT).show();
//        }
//    }
//
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()){
//            case android.R.id.home:
//                judge();
//                break;
//            default:
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//
//    class TimeCount extends CountDownTimer {
//        public TimeCount(long millisInFuture, long countDownInterval) {
//            super(millisInFuture, countDownInterval);
//        }
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
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
//            setResult(1);
//            judge();
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
//}

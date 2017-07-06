//package com.coder.kzxt.login.activity;
//
//import android.os.Bundle;
//import android.support.v7.widget.Toolbar;
//import android.text.TextUtils;
//import android.view.KeyEvent;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.Button;
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
//
//
///**
// * 云登录关联本地账号,创建本地并绑定
// * Created by Administrator on 2017/3/6.
// */
//
//public class CreateLocalActivity extends BaseActivity {
//    private SharedPreferencesUtil spu;
//    private ImageView account_img;
//    private TextView yun_account;
//    private ImageView connect;
//    private ImageView local_account_img_one;
//    private TextView local_account_one;
//    private TextView warn_info;
//    private Button btn_login_submit;
//    private LinearLayout yun_account_ll;
//    private LinearLayout local_account_ll_one;
//    private String account;
//    private String uid;
//    private String userFace;
//    private LoginDataBean.CloudCenterUser centerBean;
//    private Toolbar mToolbarView;
//     private RelativeLayout myLayout;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        this.overridePendingTransition(R.anim.login_up_in,R.anim.login_stay);
//        setContentView(R.layout.activity_relate);
//        spu = new SharedPreferencesUtil(this);
//        centerBean = (LoginDataBean.CloudCenterUser)getIntent().getSerializableExtra("centerBean");
//        uid = centerBean.getUid();
//        account = centerBean.getAccount();
//        userFace = centerBean.getUserface();
//
//        initView();
//        initListener();
//    }
//    private void initView(){
//
//        mToolbarView = (Toolbar) findViewById(R.id.toolbar);
//        mToolbarView.setTitle(getResources().getString(R.string.create_local_left)+getResources().getString(R.string.app_name)+getResources().getString(R.string.create_local_right));
//        setSupportActionBar(mToolbarView);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        account_img= (ImageView) findViewById(R.id.account_img);
//        yun_account= (TextView) findViewById(R.id.yun_account);
//        yun_account.setText(account);
//
//        GlideUtils.loadCircleHeaderOfCommon(this,userFace,account_img);
//        yun_account_ll= (LinearLayout) findViewById(R.id.yun_account_ll);
//        yun_account_ll.setClickable(false);
//        yun_account_ll.setSelected(false);
//
//        connect= (ImageView) findViewById(R.id.connect);
//        connect.setBackgroundResource(R.drawable.yun_copy);
//        myLayout = (RelativeLayout) findViewById(R.id.rl_layout);
//        local_account_img_one= (ImageView) findViewById(R.id.local_account_img_one);
//        local_account_one= (TextView) findViewById(R.id.local_account_one);
//        local_account_one.setText(account);
//
//        GlideUtils.loadCircleHeaderOfCommon(this,userFace,local_account_img_one);
//        local_account_ll_one= (LinearLayout) findViewById(R.id.local_account_ll_one);
//        local_account_ll_one.setSelected(false);
//        local_account_ll_one.setClickable(false);
//        warn_info= (TextView) findViewById(R.id.warn_info);
//        warn_info.setText(getResources().getString(R.string.local_relate_yun_left)+getResources().getString(R.string.app_name)+getResources().getString(R.string.local_relate_yun_right));
//        btn_login_submit= (Button) findViewById(R.id.btn_login_submit);
//        btn_login_submit.setText(getResources().getString(R.string.create_local_left)+getResources().getString(R.string.app_name)+getResources().getString(R.string.create_local_right));
//    }
//    private void judge(){
//        final CustomNewDialog customNewDialog=new CustomNewDialog(CreateLocalActivity.this,R.layout.yun_exit_info);
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
//    private void initListener(){
//
//        btn_login_submit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final CustomNewDialog customNewDialog=new CustomNewDialog(CreateLocalActivity.this,R.layout.create_account_info);
//                customNewDialog.show();
//                ImageView account_img= (ImageView) customNewDialog.findViewById(R.id.account_img);
//
//                GlideUtils.loadCircleHeaderOfCommon(CreateLocalActivity.this,userFace,account_img);
//                TextView tv_account= (TextView) customNewDialog.findViewById(R.id.tv_account);
//                tv_account.setText(account);
//                TextView warn_info= (TextView) customNewDialog.findViewById(R.id.warn_info);
//                warn_info.setText(getResources().getString(R.string.yun_account_same_left)+getResources().getString(R.string.app_name)+getResources().getString(R.string.yun_account_same_right));
//                TextView make_sure= (TextView) customNewDialog.findViewById(R.id.make_sure);
//                make_sure.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if(customNewDialog != null && customNewDialog.isShowing()){
//                            customNewDialog.cancel();
//                        }
//                        createBindAccountTask();
//                    }
//                });
//            }
//        });
//    }
//
//    private void createBindAccountTask() {
//        new HttpPostOld(this,this, new InterfaceHttpResult() {
//            @Override
//            public void getCallback(int requestCode, int code, String msg, Object baseBean) {
//                if (requestCode == 1000 && code == Constants.HTTP_CODE_SUCCESS) {
//                    LoginDataBean.LoginBean bean = ((LoginDataBean) baseBean).getData();
//                    bindSuccess(bean);
//                } else if (code == Constants.HTTP_CODE_4000 || code == Constants.HTTP_CODE_5000) {
//                    NetworkUtil.httpNetErrTip(CreateLocalActivity.this, myLayout);
//                } else {
//                    errorProcess(msg);
//                }
//            }
//        }, LoginDataBean.class, Urls.POST_CREATE_BIND_ACCOUNT_ACTION,account,uid,"2",spu.getDevicedId()).excute(1000);
//
//    }
//
//    /**
//     * 异常处理
//     * @param msg
//     */
//    private void errorProcess(String msg) {
//        if (!TextUtils.isEmpty(msg)) {
//            ToastUtils.makeText(CreateLocalActivity.this, msg, Toast.LENGTH_SHORT).show();
//        }
//    }
//
//
//    private void bindSuccess(LoginDataBean.LoginBean bean) {
//        saveLoginData(bean);
//        otherProcess(bean);
//    }
//
//
//    private void otherProcess(LoginDataBean.LoginBean bean) {
// //
////        new LoginImAsynTask(RelateLocalActivity.this).execute();
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
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
//            judge();
//            return true;
//        }
//        return false;
//    }
//    @Override
//    protected void onResume() {
//        super.onResume();
//        /**
//         * 页面起始（每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加）
//         * 不能与StatService.onPageStart一级onPageEnd函数交叉使用
//         */
////        StatService.onResume(this);
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        /**
//         * 页面结束（每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加）
//         * 不能与StatService.onPageStart一级onPageEnd函数交叉使用
//         */
////        StatService.onPause(this);
//    }
//    @Override
//    public void finish() {
//        super.finish();
//        this.overridePendingTransition(R.anim.login_stay,R.anim.login_down_out);
//    }
//
//
//}

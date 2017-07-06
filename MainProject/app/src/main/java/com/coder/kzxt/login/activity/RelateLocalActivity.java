//package com.coder.kzxt.login.activity;
//
//import android.app.Dialog;
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
//import java.util.ArrayList;
//
///**
// * 云登录关联本地账号
// */
//
//public class RelateLocalActivity extends BaseActivity {
//
////    private HashMap<String,String> hashMap=new HashMap<>();
////    private ArrayList<HashMap<String,String>> arrayList=new ArrayList<>();
////    private ImageLoader imageLoader;
//     private Dialog dialog;
//    private SharedPreferencesUtil spu;
//    private TextView title;
////    private ImageView rightImage;
//
//    private ImageView account_img;
//    private TextView yun_account;
//    private TextView warn_info;
//    private Button btn_login_submit;
//
//    private LinearLayout local_account_ll_one;
//    private ImageView local_account_img_one;
//    private TextView local_account_one;
//
//    private LinearLayout local_account_ll_two;
//    private ImageView local_account_img_two;
//    private TextView local_account_two;
//
//    private String centerUid;
//    private String centerAccount;
//    private String centerUserFace;
//    private String localUid;
//    private Toolbar mToolbarView;
//
//    boolean account_one=true;
//    boolean account_two=true;
//    private LoginDataBean.CloudCenterUser centerBean;
//    private ArrayList<LoginDataBean.CloundCenterUnbindUser> cloundCenterUnbindUsers;
//    private RelativeLayout myLayout;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        this.overridePendingTransition(R.anim.login_up_in,R.anim.login_stay);
//        setContentView(R.layout.activity_relate);
//        spu = new SharedPreferencesUtil(this);
// //        imageLoader=ImageLoader.getInstance();
////        hashMap= (HashMap<String, String>) getIntent().getSerializableExtra("centerMap");
////        arrayList= (ArrayList<HashMap<String, String>>) getIntent().getSerializableExtra("arrayList");
//        centerBean = (LoginDataBean.CloudCenterUser)getIntent().getSerializableExtra("centerBean");
//        cloundCenterUnbindUsers = ( ArrayList<LoginDataBean.CloundCenterUnbindUser> )getIntent().getSerializableExtra("arrayList");
//        centerUid = centerBean.getUid();
//        centerAccount = centerBean.getAccount();
//        centerUserFace = centerBean.getUserface();
//
//        initView();
//        initListener();
//    }
//    private void initView() {
//        mToolbarView = (Toolbar) findViewById(R.id.toolbar);
//        mToolbarView.setTitle(getResources().getString(R.string.relate_account));
//        setSupportActionBar(mToolbarView);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        account_img = (ImageView) findViewById(R.id.account_img);
//        yun_account = (TextView) findViewById(R.id.yun_account);
//        warn_info = (TextView) findViewById(R.id.warn_info);
//        btn_login_submit = (Button) findViewById(R.id.btn_login_submit);
//        yun_account.setText(centerAccount);
//        GlideUtils.loadCircleHeaderOfCommon(this,centerUserFace,account_img);
//        local_account_ll_one = (LinearLayout) findViewById(R.id.local_account_ll_one);
//        local_account_one = (TextView) findViewById(R.id.local_account_one);
//        local_account_img_one = (ImageView) findViewById(R.id.local_account_img_one);
//        myLayout = (RelativeLayout) findViewById(R.id.rl_layout);
//        local_account_ll_two = (LinearLayout) findViewById(R.id.local_account_ll_two);
//        local_account_img_two = (ImageView) findViewById(R.id.local_account_img_two);
//        local_account_two = (TextView) findViewById(R.id.local_account_two);
//        if (cloundCenterUnbindUsers.size() == 2) {
//            warn_info.setText(getResources().getString(R.string.choose_account_relate_left) + getResources().getString(R.string.app_name) + getResources().getString(R.string.choose_account_relate_right));
//            btn_login_submit.setAlpha(0.5f);
//            btn_login_submit.setClickable(false);
//            local_account_ll_two.setVisibility(View.VISIBLE);
//            local_account_one.setText(cloundCenterUnbindUsers.get(0).getAccount());
////            imageLoader.displayImage(cloundCenterUnbindUsers.get(0).getUserface(),local_account_img_one,ImageLoaderOptions.headerOptions);
//            GlideUtils.loadCircleHeaderOfCommon(this,cloundCenterUnbindUsers.get(0).getUserface(),local_account_img_one);
//            local_account_two.setText(cloundCenterUnbindUsers.get(1).getAccount());
////            imageLoader.displayImage(cloundCenterUnbindUsers.get(1).getUserface(),local_account_img_two,ImageLoaderOptions.headerOptions);
//            GlideUtils.loadCircleHeaderOfCommon(this,cloundCenterUnbindUsers.get(1).getUserface(),local_account_img_two);
//        } else if (cloundCenterUnbindUsers.size() == 1) {
//            local_account_ll_one.setClickable(false);
//            localUid = cloundCenterUnbindUsers.get(0).getUid();
//            local_account_ll_two.setVisibility(View.GONE);
//            warn_info.setText(getResources().getString(R.string.relate_account_left) + getResources().getString(R.string.app_name) + getResources().getString(R.string.relate_account_right));
//            local_account_one.setText(cloundCenterUnbindUsers.get(0).getAccount());
////            imageLoader.displayImage(cloundCenterUnbindUsers.get(0).getUserface(),local_account_img_one,ImageLoaderOptions.headerOptions);
//            GlideUtils.loadCircleHeaderOfCommon(this,cloundCenterUnbindUsers.get(0).getUserface(),local_account_img_one);
//        }
//    }
//    private void judge(){
//        final CustomNewDialog customNewDialog=new CustomNewDialog(RelateLocalActivity.this,R.layout.yun_exit_info);
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
//        local_account_ll_one.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (account_one) {
//                    local_account_ll_one.setBackgroundResource(R.drawable.edit_selected);
//                    local_account_ll_two.setBackgroundResource(R.drawable.edit_not_selected);
//                    localUid = cloundCenterUnbindUsers.get(0).getUid();
//                    select();
//                    account_two = false;
//                } else {
//                    local_account_ll_one.setBackgroundResource(R.drawable.edit_not_selected);
//                }
//            }
//        });
//        local_account_ll_two.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (account_two) {
//                    local_account_ll_one.setBackgroundResource(R.drawable.edit_not_selected);
//                    local_account_ll_two.setBackgroundResource(R.drawable.edit_selected);
//                    localUid = cloundCenterUnbindUsers.get(1).getUid();
//                    select();
//                    account_one = false;
//                } else {
//                    local_account_ll_two.setBackgroundResource(R.drawable.edit_not_selected);
//                }
//            }
//        });
//        btn_login_submit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                bindAccountTask();
//            }
//        });
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
//    private void bindAccountTask() {
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
//                    NetworkUtil.httpNetErrTip(RelateLocalActivity.this, myLayout);
//                } else {
//                    errorProcess(msg);
//                }
//            }
//        }, LoginDataBean.class, Urls.POST_BIND_ACCOUNT_ACTION,centerAccount,localUid,centerUid,spu.getDevicedId()).excute(1000);
//
//    }
//
//    private void errorProcess(String msg) {
//        if (!TextUtils.isEmpty(msg)) {
//            ToastUtils.makeText(RelateLocalActivity.this, msg, Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private void bindSuccess(LoginDataBean.LoginBean bean) {
//        ToastUtils.makeText(RelateLocalActivity.this,getResources().getString(R.string.relate_success), Toast.LENGTH_SHORT).show();
//        saveLoginData(bean);
//        otherProcess(bean);
//    }
//
//    private void otherProcess(LoginDataBean.LoginBean bean) {
////        String str = spu.getDevicedId() + bean.getOauth_token();
////        String encodeStr = Base64.encodeToString(str.getBytes(), Base64.DEFAULT);
////        spu.setIsLogin(encodeStr);
// //      new LoginImAsynTask(RelateLocalActivity.this).execute();
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
//    private void select(){
//        if (account_one || account_two) {
//            btn_login_submit.setAlpha(1.0f);
//            btn_login_submit.setClickable(true);
//        } else {
//            btn_login_submit.setAlpha(0.5f);
//            btn_login_submit.setClickable(false);
//        }
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
//
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
//}

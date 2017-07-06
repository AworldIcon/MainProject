package com.coder.kzxt.login.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.app.http.HttpCallBack;
import com.app.http.UrlsNew;
import com.app.http.builders.HttpDeleteBuilder;
import com.app.http.builders.HttpGetBuilder;
import com.app.http.builders.HttpPostBuilder;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.dialog.util.MyPublicDialog;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.coder.kzxt.utils.ToastUtils;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.List;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

public class SocialAccountActivity extends BaseActivity implements PlatformActionListener {
    private Toolbar mToolbarView;
    private RelativeLayout weixin_layout,qq_layout;
    private TextView bind_weixin,bind_qq;
    private int qq,wechat;
    private String type,open_id;
    private SharedPreferencesUtil spu;
    private LinearLayout jiazai_layout;
    private Dialog dialog;
    private boolean isCilckBind=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_account);
        mToolbarView= (Toolbar) findViewById(R.id.toolbar);
        mToolbarView.setTitle("社交账号");
        mToolbarView.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initView();
        httpRequest();
        clickEvents();
        dialog = MyPublicDialog.createLoadingDialog(SocialAccountActivity.this);
    }


    private void initView() {
        spu=new SharedPreferencesUtil(this);
        jiazai_layout= (LinearLayout) findViewById(R.id.jiazai_layout);
        weixin_layout= (RelativeLayout) findViewById(R.id.weixin_layout);
        qq_layout= (RelativeLayout) findViewById(R.id.qq_layout);
        bind_weixin= (TextView) findViewById(R.id.bind_weixin);
        bind_qq= (TextView) findViewById(R.id.bind_qq);
        jiazai_layout.setVisibility(View.VISIBLE);
    }


    private void clickEvents() {
        weixin_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type="wechat";
                if(wechat==0){
                    //绑定微信dialog
                    bindTencent();
                }else {
                    //解绑微信dialog
                    deletebindDialog(type);
                }

            }
        });
        qq_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type="qq";
                if(qq==0){
                    //绑定qqdialog
                    bindTencent();
                }else {
                    //解绑qqdialog
                    deletebindDialog(type);
                }
            }
        });
    }

    private void deleteBindTencent(final String type) {
        jiazai_layout.setVisibility(View.VISIBLE);
        new HttpDeleteBuilder(SocialAccountActivity.this).setUrl(UrlsNew.URLHeader+"/user/social_account/").setClassObj(null)
                .setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
                        jiazai_layout.setVisibility(View.GONE);
                        ToastUtils.makeText(SocialAccountActivity.this,"解绑成功");
                        if(type.equals("qq")){
                            qq=0;
                        }else {
                            wechat=0;
                        }
                        updateData();
                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {
                        jiazai_layout.setVisibility(View.GONE);
                    }
                }).setPath(type).build();

    }

    private void deletebindDialog(final String type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.custom_dialog);
        builder.setMessage("您是否要解除绑定");
        builder.setTitle("提示");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                deleteBindTencent(type);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    protected void bindDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.custom_dialog);
        if(type.equals("qq")){
            builder.setMessage("QQ账号绑定成功");
        }else {
            builder.setMessage("微信账号绑定成功");
        }
        builder.setTitle("提示");
        builder.setPositiveButton("朕知道了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
    private void bindTencent() {
        if(type.equals("qq")){
            Platform platform = ShareSDK.getPlatform(QQ.NAME);
            platform.SSOSetting(false);
            login(platform);
        }else {
            // 获取平台
            Platform plat = ShareSDK.getPlatform(Wechat.NAME);
            plat.SSOSetting(true);
            if(isWeixinAvilible(this)){
                login(plat);
            }else{
                AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.custom_dialog);
                builder.setMessage("您尚未安装微信无法绑定");
                builder.setTitle("提示");
                builder.setPositiveButton("朕知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
                return;
            }
        }

    }

    private void login(Platform platform) {
        isCilckBind=true;
        dialog.show();
        platform.setPlatformActionListener(this);
        if (platform.isAuthValid()){//是否授权登录了
            platform.removeAccount(true);
            ShareSDK.removeCookieOnAuthorize(true);
        }
        if((!spu.getWXID().startsWith("wx") || spu.getWXID().length() != 18)){
            Toast.makeText(this, "授权操作遇到错误",   Toast.LENGTH_SHORT).show();
        }else{
            platform.showUser(null);
        }
    }
    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        // 执行相关业务逻辑操作
        isCilckBind=false;
        if(type.equals("qq")){
            open_id=new QQ(SocialAccountActivity.this).getDb().getUserId();
        }else {
            open_id=new Wechat(SocialAccountActivity.this).getDb().get("unionid");
        }

        //上报服务器
        new HttpPostBuilder(SocialAccountActivity.this).setUrl(UrlsNew.URLHeader+"/user/social_account").setClassObj(null)
                .setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
                        dialog.dismiss();
                        if(type.equals("qq")){
                                qq=1;
                            }else {
                                wechat=1;
                            }
                        bindDialog();
                        updateData();
                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {
                        dialog.dismiss();
                        ToastUtils.makeText(SocialAccountActivity.this,msg);
                    }
                })
                .addBodyParam("type",type)
                .addBodyParam("open_id",open_id)
                .build();
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        dialog.dismiss();
        isCilckBind=false;
        type="";
        if (i == platform.ACTION_USER_INFOR) {
            ToastUtils.makeText(this, "授权操作遇到错误",Toast.LENGTH_SHORT).show();
        }
        throwable.printStackTrace();
    }

    @Override
    public void onCancel(Platform platform, int i) {
        type="";
        dialog.dismiss();
        isCilckBind=false;
        if (i == platform.ACTION_USER_INFOR) {
            ToastUtils.makeText(this, "授权操作已取消", Toast.LENGTH_SHORT).show();
        }
    }

    public  boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }

        return false;
    }
    private void updateData(){
        if(qq==1){
            bind_qq.setText("已绑定");
            bind_qq.setTextColor(getResources().getColor(R.color.first_theme));
        }else {
            bind_qq.setText("未绑定");
            bind_qq.setTextColor(getResources().getColor(R.color.font_gray));
        }
        if(wechat==1){
            bind_weixin.setText("已绑定");
            bind_weixin.setTextColor(getResources().getColor(R.color.first_theme));
        }else {
            bind_weixin.setText("未绑定");
            bind_weixin.setTextColor(getResources().getColor(R.color.font_gray));
        }

    }
    //首次进入请求绑定状态
    private void httpRequest() {
        new HttpGetBuilder(this).setClassObj(null).setUrl(UrlsNew.URLHeader+"/user/social_account")
                .setHttpResult(new HttpCallBack() {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean) {
                        jiazai_layout.setVisibility(View.GONE);
                        if(resultBean==null){
                            return;
                        }
                        try {
                            JSONObject jsonObject=new JSONObject(resultBean.toString());
                            qq=jsonObject.getJSONObject("item").optInt("qq",0);
                            wechat=jsonObject.getJSONObject("item").optInt("wechat",0);

                        } catch (JSONException e) {

                        }

                        updateData();

                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg) {
                        jiazai_layout.setVisibility(View.GONE);
                    }
                }).build();
    }
//第三方登录返回后，获取数据又是一个异步请求，因此需要判断dialog。只有cancle（用户返回操作）最快
    @Override
    protected void onResume() {
        super.onResume();
        if(!TextUtils.isEmpty(type)&&isCilckBind){
            dialog.show();
            isCilckBind=false;
        }
    }
}

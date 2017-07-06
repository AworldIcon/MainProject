package com.coder.kzxt.setting.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.os.storage.StorageManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.app.http.HttpCallBack;
import com.app.http.builders.HttpGetBuilder;
import com.baidu.android.pushservice.PushManager;
import com.bumptech.glide.Glide;
import com.coder.kzxt.activity.R;
import com.coder.kzxt.base.activity.BaseActivity;
import com.coder.kzxt.base.beans.BaseBean;
import com.coder.kzxt.db.DataBaseDao;
import com.coder.kzxt.dialog.util.CustomDialog;
import com.coder.kzxt.dialog.util.CustomListDialog;
import com.coder.kzxt.download.NotificationUpdateService;
import com.coder.kzxt.http.utils.CheckVersion;
import com.coder.kzxt.im.business.ImLoginBusiness;
import com.coder.kzxt.login.activity.AccountSafeActivity;
import com.coder.kzxt.login.activity.LoginActivity;
import com.coder.kzxt.login.activity.UserInfoActivity;
import com.coder.kzxt.recyclerview.adapter.BaseRecyclerAdapter;
import com.coder.kzxt.setting.beans.CheckVersionBean;
import com.coder.kzxt.utils.Constants;
import com.coder.kzxt.utils.FileUtil;
import com.coder.kzxt.utils.MyApplication;
import com.coder.kzxt.utils.PublicUtils;
import com.coder.kzxt.utils.SdcardUtils;
import com.coder.kzxt.utils.SharedPreferencesUtil;
import com.coder.kzxt.utils.ToastUtils;
import com.app.http.UrlsNew;
import com.coder.kzxt.utils.Utils;
import com.coder.kzxt.webview.activity.HelpActivity;

import java.util.ArrayList;


public class SettingActivity extends BaseActivity
{

    private Toolbar mToolbarView;
    private Switch public_switch;
    private RelativeLayout presonalLayout; //个人信息
    private RelativeLayout accountLayout;   //账号管理
    //    private RelativeLayout notificationLayout; //消息通知
    private RelativeLayout wifiDowloadLayout;  //非wifi下载
    private RelativeLayout clearCacheLayout;   //清除缓存
    private RelativeLayout helpLayout;  //帮助与反馈
    private RelativeLayout aboutLayout; //关于我赢
    private TextView tv_about; //关于
    private Button btnLogout; //退出
    private SharedPreferencesUtil spu;
    private boolean existSdcard;
    private String[] paths;
    private int select_download_address;
    private String formetFileSize;
    private String formetFileSize_Second;
    private boolean checkFsWritable;
    private RelativeLayout downloadLayout; //下载保存位置
    private RelativeLayout checkVersionLayout; //检查更新
    private TextView current_version_text; //当前版本
    private TextView dowload_address_text; //下载地址

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        spu = new SharedPreferencesUtil(this);
        initView();
        setUserInfo();
        getStoragePath();
        initListener();

    }

    private void initView()
    {
        mToolbarView = (Toolbar) findViewById(R.id.toolbar);
        mToolbarView.setTitle(R.string.profile_set);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        presonalLayout = (RelativeLayout) findViewById(R.id.presonal_layout);
        accountLayout = (RelativeLayout) findViewById(R.id.account_layout);
//        notificationLayout = (RelativeLayout) findViewById(R.id.notification_layout);
        wifiDowloadLayout = (RelativeLayout) findViewById(R.id.allow_dowload_layout);
        clearCacheLayout = (RelativeLayout) findViewById(R.id.cache_cleaner_layout);
        helpLayout = (RelativeLayout) findViewById(R.id.help_ticking_layout);
        public_switch = (Switch) findViewById(R.id.public_switch);
        aboutLayout = (RelativeLayout) findViewById(R.id.about_layout);
        downloadLayout = (RelativeLayout) findViewById(R.id.dowload_save_layout);
        checkVersionLayout = (RelativeLayout) findViewById(R.id.check_version_layout);
        current_version_text = (TextView) findViewById(R.id.current_version_text);
        dowload_address_text = (TextView) findViewById(R.id.dowload_address_text);
        btnLogout = (Button) findViewById(R.id.btn_logout);
        tv_about = (TextView) findViewById(R.id.tv_about);
        tv_about.setText(getResources().getString(R.string.about_we));
        String version2 = Utils.getVersion(SettingActivity.this);
        current_version_text.setText(getResources().getString(R.string.version_rightnow) + version2);
        String downloadFlag = spu.getDownloadFlag();
        isDownLoadFlag(downloadFlag);
//        notificationLayout.setVisibility(View.GONE);
    }

    /**
     * 获取存储路径
     */
    private void getStoragePath()
    {
        // 获取sdcard的路径：外置和内置
        StorageManager sm = (StorageManager) SettingActivity.this.getSystemService(Context.STORAGE_SERVICE);
        try
        {
            paths = (String[]) sm.getClass().getMethod("getVolumePaths", new Class[0]).invoke(sm, new Class[0]);
            if (paths != null && paths.length >= 2)
            {
                spu.setSecondSdcard(paths[1]);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        select_download_address = spu.getSelectAddress();
        if (select_download_address == 0)
        {
            dowload_address_text.setText(getResources().getString(R.string.sd_card_in));
        } else if (select_download_address == 1)
        {
            dowload_address_text.setText(getResources().getString(R.string.sd_card_out));
        }
        existSdcard = SdcardUtils.isExistSdcard();
        if (existSdcard)
        {
            // 获得内置卡sd卡剩余空间
            long sdFreeSize = SdcardUtils.getSDFreeSizeByte(Environment.getExternalStorageDirectory().getPath());
            // 转换单位
            formetFileSize = FileUtil.FormetFileSize(sdFreeSize);
        }

        if (paths != null && paths.length >= 2)
        {
            // 检查外置sd卡路径是否可用
            checkFsWritable = SdcardUtils.checkFsWritable(paths[1] + "/Android/data/" + getPackageName());
            if (checkFsWritable)
            {
                long sdFreeSize_Second = SdcardUtils.getSDFreeSizeByte(paths[1]);
                formetFileSize_Second = FileUtil.FormetFileSize(sdFreeSize_Second);
            } else
            {
                formetFileSize_Second = "00B";
            }
        }
    }

    private int clickCount = 0;

    private void initListener()
    {

        mToolbarView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                clickCount++;
                if (clickCount == 5)
                {
                    clickCount = 0;
                    showSelectNetDiaglog();
                }

            }
        });

        // 3G下载
        public_switch.setOnCheckedChangeListener(new android.widget.CompoundButton.OnCheckedChangeListener()
        {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked)
                {
                    spu.setDownloadFlag("1");
                } else
                {
                    spu.setDownloadFlag("0");
                }
            }
        });

        //账号管理
        accountLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

            }
        });
//
//        //消息通知
//        notificationLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (TextUtils.isEmpty(spu.getToken())) {
//                    Intent intent = new Intent();
//                    intent.setClass(SettingActivity.this, LoginActivity.class);
//                    intent.putExtra("from", "notify_setting_activity");
//                    startActivity(intent);
//                } else {
//                    Intent intent = new Intent();
//                    intent.setClass(SettingActivity.this, NotifySettingActivity.class);
//                    startActivity(intent);
//                }
//            }
//        });

        //清除缓存
        clearCacheLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (Utils.isFastClick())
                {
                    return;
                }
                if (clearCacheMemory())
                {
                    ToastUtils.makeText(getApplicationContext(), getResources().getString(R.string.clear_success), Toast.LENGTH_SHORT).show();
                }
            }
        });

        //非wifi下载处理
        wifiDowloadLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

            }
        });

        //下载保存位置
        downloadLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (Utils.isFastClick())
                {
                    return;
                }
                Boolean isSelectFlag = false;
                // 检测下载中所有视频的百分比
                ArrayList<String> query_All_DownPercent = DataBaseDao.getInstance(SettingActivity.this).query_All_DownPercent();
                for (int i = 0; i < query_All_DownPercent.size(); i++)
                {
                    String string = query_All_DownPercent.get(i);
                    if (!string.equals("0"))
                    {
                        isSelectFlag = true;
                    }
                }

                if (!isSelectFlag)
                {
                    if (existSdcard)
                    {
                        if (paths != null && paths.length >= 2)
                        {
                            AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this, R.style.custom_dialog);

                            builder.setSingleChoiceItems(new String[]{getResources().getString(R.string.sd_card_in) + getResources().getString(R.string.canbe_used_space) + formetFileSize, getResources().getString(R.string.sd_card_out) + getResources().getString(R.string.canbe_used_space) + formetFileSize_Second}, spu.getSelectAddress(), new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    spu.setSelectAddress(which);
                                    if (which == 0)
                                    {
                                        dowload_address_text.setText(getResources().getString(R.string.sd_card_in));
                                    } else if (which == 1)
                                    {
                                        dowload_address_text.setText(getResources().getString(R.string.sd_card_out));
                                    }
                                    dialog.dismiss();
                                }
                            });
                            builder.show();
                        } else
                        {
                            ToastUtils.makeText(SettingActivity.this, getResources().getString(R.string.sd_card_space_wrong), Toast.LENGTH_SHORT).show();
                        }
                    } else
                    {
                        ToastUtils.makeText(SettingActivity.this, getResources().getString(R.string.take_photo_no_card), Toast.LENGTH_SHORT).show();
                    }
                } else
                {
                    ToastUtils.makeText(SettingActivity.this, getResources().getString(R.string.video_is_downloading), Toast.LENGTH_SHORT).show();
                }

            }
        });

        //检查更新
        checkVersionLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (Utils.isFastClick())
                {
                    return;
                }
                httpCheckVersion();
            }
        });


        //个人信息
        presonalLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (TextUtils.isEmpty(spu.getIsLogin()))
                {
                    Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                    startActivityForResult(intent, 3);
                } else
                {
                    startActivity(new Intent(SettingActivity.this, UserInfoActivity.class));
                }
            }
        });

        //帮助与反馈
        helpLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (Utils.isFastClick())
                {
                    return;
                }
                Intent intent = new Intent(SettingActivity.this, HelpActivity.class);
                startActivity(intent);
            }
        });

        //关于我们
        aboutLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(SettingActivity.this, AboutusActivity.class);
                startActivity(intent);
            }
        });

        //账号管理
        accountLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (TextUtils.isEmpty(spu.getIsLogin()))
                {
                    Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                    startActivityForResult(intent, 1);
                } else
                {
                    Intent intent = new Intent(SettingActivity.this, AccountSafeActivity.class);
                    startActivity(intent);
                }
            }
        });

        //退出
        btnLogout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this, R.style.custom_dialog);
                builder.setMessage(getResources().getString(R.string.exit_makesure));
                builder.setPositiveButton(getResources().getString(R.string.password_dialog_ensure), new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
//                        TIMManager.getInstance().logout();
//                        UserInfoManagerNew.getInstance().ClearData();
//                        QavsdkControl.getInstance().stopContext();
//                        handler.sendEmptyMessage(Constants.REFRESHMAIN);
                        logout();

                    }
                });

                builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener()
                {

                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                    }
                });
                builder.show();

            }
        });

    }

    private CustomListDialog customListDialog;

    private void showSelectNetDiaglog()
    {
        if (customListDialog == null)
        {
            final String[] data = {UrlsNew.URLHeader0, UrlsNew.URLHeader1,
                    UrlsNew.URLHeader6, UrlsNew.URLHeader7, UrlsNew.URLHeader8};
            customListDialog = new CustomListDialog(SettingActivity.this);
            customListDialog.addData(data);
            customListDialog.getAdapter().setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener()
            {
                @Override
                public void onItemClick(View view, int position)
                {
                    UrlsNew.URLHeader = data[position];
                    customListDialog.cancel();
                }
            });
        }
        customListDialog.show();

    }


    private void httpCheckVersion()
    {
        new CheckVersion(SettingActivity.this, new HttpCallBack()
        {
            @Override
            public void setOnSuccessCallback(int requestCode, Object resultBean)
            {
                CheckVersionBean checkVersionBean = (CheckVersionBean) resultBean;
                String name = checkVersionBean.getItem().getName();// 名称
                String version = checkVersionBean.getItem().getVersion(); // 版本
                String function = checkVersionBean.getItem().getFunction();// 新加功能
                //String publicTm = checkVersionBean.getItem().getPublicTm(); // 发布时间
                final String url = checkVersionBean.getItem().getUrl(); // 下载页面
                final int isForceUpdate = checkVersionBean.getItem().getIsForceUpdate(); // 是否强制升级

                int versionCodetwo = PublicUtils.getVersionCodetwo(SettingActivity.this);
                double parseDouble = Double.parseDouble(version);
                double anotherDoubleValue = Math.floor(parseDouble);
                int intValue = (int) anotherDoubleValue;
                // 如果本地版本小于服务器版本提示升级
                if (versionCodetwo < intValue)
                {
                    final CustomDialog customDialog = new CustomDialog(SettingActivity.this);
                    customDialog.setTitleVisibility(getResources().getString(R.string.update_warn));
                    // customDialog.setMessage(publicTm);

                    if (isForceUpdate == 1)
                    {
                        customDialog.setCanceledOnTouchOutside(false);
                        customDialog.setCancelable(false);
                    } else
                    {
                        customDialog.setCanceledOnTouchOutside(true);
                        customDialog.setCancelable(true);
                    }
                    if (!TextUtils.isEmpty(function))
                    {
                        customDialog.setMessage(function);
                    }

                    if (isForceUpdate == 0)
                    {
                        customDialog.setRightText(getResources().getString(R.string.update_next));
                    } else
                    {
                        customDialog.setRightText(getResources().getString(R.string.exit_app));
                    }
                    customDialog.setLeftText(getResources().getString(R.string.update_now));
                    customDialog.show();

                    customDialog.setLeftClick(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            Intent intent = new Intent(SettingActivity.this, NotificationUpdateService.class);
                            intent.putExtra("apkUrl", url);
                            startService(intent);
                            if (isForceUpdate == 0)
                            {
                                customDialog.dismiss();
                            } else
                            {
                            }
                            spu.setShowLauncher(false);
                        }
                    });

                    customDialog.setRightClick(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            // 判断是否强制升级
                            if (isForceUpdate == 1)
                            {
                                MyApplication.getInstance().exit();
                            } else
                            {
                                customDialog.dismiss();
                            }
                        }
                    });

                } else
                {
                    ToastUtils.makeText(getApplicationContext(), getResources().getString(R.string.is_new_version), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void setOnErrorCallback(int requestCode, int code, String msg)
            {

            }

        });

    }


    @Override
    protected void onResume()
    {
        super.onResume();
    }


    /**
     * 下载标记
     *
     * @param downloadFlag
     */
    private void isDownLoadFlag(String downloadFlag)
    {
        if (downloadFlag.equals("1"))
        {
            public_switch.setChecked(true);
        } else if (downloadFlag.equals("0"))
        {
            public_switch.setChecked(false);
        }
    }


    private void setUserInfo()
    {
        if (TextUtils.isEmpty(spu.getIsLogin()))
        {
            btnLogout.setVisibility(View.GONE);
        } else
        {
            btnLogout.setVisibility(View.VISIBLE);
        }
    }


    // 清除Glide内存缓存
    public boolean clearCacheMemory()
    {
        try
        {
            if (Looper.myLooper() == Looper.getMainLooper())
            { //只能在主线程执行
                Glide.get(SettingActivity.this).clearMemory();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.get(SettingActivity.this).clearDiskCache();
                    }
                });

                return true;
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Constants.LOGIN_BACK)
        {

        } else if (requestCode == 3 && resultCode == Constants.LOGIN_BACK)
        {
            setUserInfo();
        }

    }

    /**
     * 退出登录
     */
    private void logout()
    {
        new HttpGetBuilder(SettingActivity.this)
                .setUrl(UrlsNew.GET_LOG_OUT)
                .setHttpResult(new HttpCallBack()
                {
                    @Override
                    public void setOnSuccessCallback(int requestCode, Object resultBean)
                    {
                        new ImLoginBusiness().logout(false);
                        spu.clearUserInfo();

                        sendBroadcast(new Intent().setAction(Constants.LOGIN_NOTICE));
                        PushManager.stopWork(getApplicationContext());
                        setResult(Constants.LOGIN_BACK);
                        finish();
                    }

                    @Override
                    public void setOnErrorCallback(int requestCode, int code, String msg)
                    {
                        ToastUtils.makeText(SettingActivity.this, "退出失败", Toast.LENGTH_SHORT).show();
                    }
                })
                .setClassObj(BaseBean.class)
                .build();
    }
}

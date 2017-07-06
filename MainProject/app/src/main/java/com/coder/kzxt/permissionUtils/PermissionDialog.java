package com.coder.kzxt.permissionUtils;

/**
 * Created by zw on 2017/6/12.
 */

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.coder.kzxt.activity.R;

public class PermissionDialog {

    private AlertDialog.Builder builder;
    private Activity activity;
    private String title;
    private String message;

    public PermissionDialog(Activity activity) {
        this.activity = activity;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMessage(String message) {
        this.message = message;
    }
//可自定义弹框标题以及内容
    private String getTitle() {
        if (TextUtils.isEmpty(title)) {
            return "权限提示";
        } else {
            return title;
        }
    }

    private String getMessage() {
        if (TextUtils.isEmpty(message)) {
            return "未获得应用运行所需的基本权限，请在设置中开启权限后再使用";
        } else {
            return message;
        }
    }

    public void init() {
        builder = new AlertDialog.Builder(activity, R.style.custom_dialog);
        builder.setTitle(getTitle());
        builder.setMessage(getMessage());

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setPositiveButton("去设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startAppSettings();
            }
        });
    }

    public void show() {
        builder.show();
    }
//跳转权限管理页面
    private void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + activity.getPackageName()));
        activity.startActivity(intent);
    }
}

package com.coder.kzxt.permissionUtils;

/**
 * Created by zw on 2017/6/12.
 */

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

public class PermissionsUtil {

    public static final int PERMISSION_REQUEST_CODE = 200;
    private String[] permissions;
    private Activity activity;
    private PermissionDialog dialog;

    public PermissionsUtil(Activity activity) {
        this.activity = activity;
        this.dialog = new PermissionDialog(activity);
    }

    public boolean isNeedPermissions(String[] permissions) {
        this.permissions = permissions;
        Checker checker = new Checker(activity);
        if(checker.checkPermissions(permissions)){
            requestPermissions();
        }
        return checker.checkPermissions(permissions);
    }

    public void requestPermissions() {
        ActivityCompat.requestPermissions(activity, permissions, PERMISSION_REQUEST_CODE);
    }

    public void setTitle(String title){
        dialog.setTitle(title);
    }

    public void setMessage(String message){
        dialog.setMessage(message);
    }

    public boolean isRequestAllPermissions(@NonNull int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    public void showDialog(){
        dialog.init();
        dialog.show();
    }

}

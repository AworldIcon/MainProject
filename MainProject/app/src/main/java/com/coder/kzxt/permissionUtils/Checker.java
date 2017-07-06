package com.coder.kzxt.permissionUtils;

/**
 * Created by zw on 2017/6/12.
 */

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

public class Checker {

    private final Context mContext;

    public Checker(Context context) {
        mContext = context.getApplicationContext();
    }

    // Check All Permissions
    public boolean checkPermissions(String... permissions) {
        for (String permission : permissions) {
            if (isAllowedPermission(permission)) {
                return true;
            }
        }
        return false;
    }

    private boolean isAllowedPermission(String permission) {
        return ContextCompat.checkSelfPermission(mContext, permission) == PackageManager.PERMISSION_DENIED;
    }

}
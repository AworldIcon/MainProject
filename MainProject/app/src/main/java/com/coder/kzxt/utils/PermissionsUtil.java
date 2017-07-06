package com.coder.kzxt.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.coder.kzxt.activity.R;
import com.coder.kzxt.permissionUtils.Checker;

/**
 * Created by zw on 2016/12/23.
 *   1，调用每种权限判断：返回true则直接执行要做的事，false不需判断，因为activity和fragment中重写的
 * onRequestPermissionsResult方法会在返回false情况下自动执行。
 *    2，  需要在activity和fragment中重写的onRequestPermissionsResult方法里
 * 执行语句permissionsGet.permissionsResult(requestCode, permissions, grantResults)，然后会弹窗让用户点击，
 * 用户同意则返回true，则做自己要做的事；用户拒绝则返回false。
 */
public class PermissionsUtil {
    private boolean result=false;
    private int click=0;
    private Activity activity;
    private String[] PERMISSIONS_CAMEAR = {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private String[] PERMISSIONS_STORAGE = { Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private String[] PERMISSIONS_SETTINGS = { Manifest.permission.WRITE_SETTINGS};
    private String[] PERMISSIONS_CHANGE_NET_STATE = { Manifest.permission.CHANGE_NETWORK_STATE};
    private String[] PERMISSIONS_LOCATION = {Manifest.permission.ACCESS_COARSE_LOCATION};
    private String[] PERMISSIONS_RECORD = {Manifest.permission.RECORD_AUDIO};
    private String[] PERMISSIONS_READ_PHONE_STATE = {Manifest.permission.READ_PHONE_STATE};
    private String[] PERMISSIONS_CALL_PHONE = {Manifest.permission.CALL_PHONE};
    private String[] PERMISSIONS_GET_ACCOUNTS = {Manifest.permission.GET_ACCOUNTS};
    private String[] PERMISSIONS_PROCESS_OUTGOING_CALLS = {Manifest.permission.PROCESS_OUTGOING_CALLS};
    private String[] PERMISSIONS_READ_CALL_LOG= {Manifest.permission.READ_CALL_LOG};
    private String[] PERMISSIONS_RECEIVE_SMS= {Manifest.permission.READ_SMS};


    private final int REQUEST_CAMEAR = 1;
    private final int REQUEST_STORAGE = 0;
    private final int REQUEST_LOCATION = 2;
    private final int REQUEST_RECORD = 3;
    private final int REQUEST_CALL_PHONE = 6;
    private final int REQUEST_GET_ACCOUNTS = 5;
    private final int REQUEST_READ_PHONE_STATE = 4;
    private final int REQUEST_PROCESS_OUTGOING_CALLS = 7;
    private final int REQUEST_READ_CALL_LOG = 8;
    private final int REQUEST_RECEIVE_SMS = 9;
    private final int REQUEST_SEETINGS = 10;
    private final int REQUEST_CHANGE_NEW_STATE = 11;

    public PermissionsUtil(Activity activity){
        this.activity=activity;
    }
    //
    /**
     * 获取storage的权限
     *
     * group:android.permission-group.STORAGE
     permission:android.permission.READ_EXTERNAL_STORAGE
     permission:android.permission.WRITE_EXTERNAL_STORAGE
     * */
    public boolean storagePermissions() {
        // Check if we have write permission
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {
            // We don't have permission so prompt the user
            click=1;
            result=false;
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_STORAGE);
          //  Log.i("TAG-CALL","NEED");
        }else {
            //当用户手机默认某权限允许，返回true，直接执行想要做的事
            result=true;
          //  Log.i("TAG-CALL","UNNEED");

        }

        return result;
    }
    //
    /**
     * 获取相机的权限
     *
     * group:android.permission-group.CAMERA
     permission:android.permission.CAMERA
     * */
    public boolean camearPermissions() {
//        // Check if we have write permission
//        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ) {
//            // We don't have permission so prompt the user
//            click=1;
//            result=false;
//            ActivityCompat.requestPermissions(activity, PERMISSIONS_CAMEAR, REQUEST_CAMEAR);
//        }else {
//            //当用户手机默认某权限允许
//            result=true;
//         //   Log.i("TAG-CALL","UNNEED");
//
//        }
//        return result;
        Checker checker = new Checker(activity);
        if(checker.checkPermissions(PERMISSIONS_CAMEAR)){
            ActivityCompat.requestPermissions(activity, PERMISSIONS_CAMEAR, REQUEST_CAMEAR);
        }
        return !checker.checkPermissions(PERMISSIONS_CAMEAR);
    }
    /**
     * 获取gps定位权限
     *
     * group:android.permission-group.LOCATION
     permission:android.permission.ACCESS_FINE_LOCATION
     permission:android.permission.ACCESS_COARSE_LOCATION
     * */
    public boolean gpsPermissions() {
        // Check if we have write permission
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ){
            // We don't have permission so prompt the user
            click=1;
            result=false;
            ActivityCompat.requestPermissions(activity, PERMISSIONS_LOCATION, REQUEST_LOCATION);
          //  Log.i("TAG-CALL","NEED");
        }else {
            //当用户手机默认某权限允许
            result=true;
         //   Log.i("TAG-CALL","UNNEED");
        }
        return  result;
    }


    public boolean settingPermissions() {
        // Check if we have write permission
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_SETTINGS) != PackageManager.PERMISSION_GRANTED ){
            // We don't have permission so prompt the user
            click=1;
            result=false;
            ActivityCompat.requestPermissions(activity, PERMISSIONS_SETTINGS, REQUEST_SEETINGS);
           // Log.i("TAG-CALL","NEED");
        }else {
            //当用户手机默认某权限允许
            result=true;
          //  Log.i("TAG-CALL","UNNEED");
        }
        return  result;
    }


    public boolean changeNetStatePermissions() {
        // Check if we have write permission
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CHANGE_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED ){
            // We don't have permission so prompt the user
            click=1;
            result=false;
            ActivityCompat.requestPermissions(activity, PERMISSIONS_CHANGE_NET_STATE, REQUEST_CHANGE_NEW_STATE);
         //   Log.i("TAG-CALL","NEED");
        }else {
            //当用户手机默认某权限允许
            result=true;
         //   Log.i("TAG-CALL","UNNEED");
        }
        return  result;
    }

    /**
     * 获取RECORD_AUDIO权限
     *
     * group:android.permission-group.MICROPHONE
     permission:android.permission.RECORD_AUDIO
     * */
    public boolean record_audioPermissions() {
        // Check if we have write permission
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ){
            // We don't have permission so prompt the user
            click=1;
            result=false;
            ActivityCompat.requestPermissions(activity, PERMISSIONS_RECORD, REQUEST_RECORD);
        }else {
            //当用户手机默认某权限允许
            result=true;
         //   Log.i("TAG-CALL","UNNEED");
        }
        return  result;
    }

    /**
     * 获取电话CALL_PHONE权限
     *
     * group:android.permission-group.PHONE
     permission:android.permission.READ_CALL_LOG
     permission:android.permission.READ_PHONE_STATE
     permission:android.permission.CALL_PHONE
     permission:android.permission.WRITE_CALL_LOG
     permission:android.permission.USE_SIP
     permission:android.permission.PROCESS_OUTGOING_CALLS
     permission:com.android.voicemail.permission.ADD_VOICEMAIL
     * */
    public boolean call_phonePermissions() {
        // Check if we have write permission
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED ){
            // We don't have permission so prompt the user
            click=1;
            ActivityCompat.requestPermissions(activity, PERMISSIONS_CALL_PHONE, REQUEST_CALL_PHONE);
           // Log.i("TAG-CALL","NEED");
        }else {
            //当用户手机默认某权限允许
            result=true;
          //  Log.i("TAG-CALL","UNNEED");
        }
        return  result;
    }
    public boolean read_phone_statePermissions() {
        // Check if we have write permission
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED ){
            // We don't have permission so prompt the user
            click=1;
            result=false;
            ActivityCompat.requestPermissions(activity, PERMISSIONS_READ_PHONE_STATE, REQUEST_READ_PHONE_STATE);
          //  Log.i("TAG-CALL","NEED");
        }else {
            //当用户手机默认某权限允许
            result=true;
          //  Log.i("TAG-CALL","UNNEED");
        }
        return  result;
    }
    public boolean process_outgoing_callsPermissions() {
        // Check if we have write permission
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.PROCESS_OUTGOING_CALLS) != PackageManager.PERMISSION_GRANTED ){
            // We don't have permission so prompt the user
            click=1;
            result=false;
            ActivityCompat.requestPermissions(activity, PERMISSIONS_PROCESS_OUTGOING_CALLS, REQUEST_PROCESS_OUTGOING_CALLS);
        }else {
            //当用户手机默认某权限允许
            result=true;
          //  Log.i("TAG-CALL","UNNEED");
        }
        return  result;
    }
    public boolean read_call_logPermissions() {
        // Check if we have write permission
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED ){
            // We don't have permission so prompt the user
            click=1;
            result=false;
            ActivityCompat.requestPermissions(activity, PERMISSIONS_READ_CALL_LOG, REQUEST_READ_CALL_LOG);
        }else {
            //当用户手机默认某权限允许
            result=true;
           // Log.i("TAG-CALL","UNNEED");
        }
        return  result;
    }
    /**
     * 获取通讯录READ_CONTACTS权限
     *
    * group:android.permission-group.CONTACTS
  permission:android.permission.WRITE_CONTACTS
  permission:android.permission.GET_ACCOUNTS
  permission:android.permission.READ_CONTACTS
    * */
    public boolean contactsPermissions() {
        // Check if we have write permission
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED ){
            // We don't have permission so prompt the user
            click=1;
            result=false;
            ActivityCompat.requestPermissions(activity, PERMISSIONS_GET_ACCOUNTS, REQUEST_GET_ACCOUNTS);
        }else {
            //当用户手机默认某权限允许
            result=true;
           // Log.i("TAG-CALL","UNNEED");
        }
        return  result;
    }


    /**
     * 获取短信READ_SMS权限
     *
     * group:android.permission-group.SMS
     permission:android.permission.READ_SMS
     permission:android.permission.RECEIVE_WAP_PUSH
     permission:android.permission.RECEIVE_MMS
     permission:android.permission.RECEIVE_SMS
     permission:android.permission.SEND_SMS
     permission:android.permission.READ_CELL_BROADCASTS
     * */
    public boolean receive_smsPermissions() {
        // Check if we have write permission
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED ){
            // We don't have permission so prompt the user
            click=1;
            result=false;
            ActivityCompat.requestPermissions(activity, PERMISSIONS_RECEIVE_SMS, REQUEST_RECEIVE_SMS);
        }else {
            //当用户手机默认某权限允许
            result=true;
        }
        return  result;
    }
    /**
     *     权限回调结果
     *
     *     此方法在activity或者fragment中的系统权限回调方法里调用
     *
     * */
    public  boolean permissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    click=1;
                    if(ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                        //若需要用户必须允许的权限可以打开（只要用户拒绝就会一直弹窗），如下一致
                       // storagePermissions();
                        Toast.makeText(activity,"需要允许存储权限",Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    if(grantResults[0]==0){
                        return true;
                    }
                    click=0;
                } else {
                    if(ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                        Toast.makeText(activity,"需要允许存储权限",Toast.LENGTH_SHORT).show();
                        //若需要用户必须允许的权限可以打开（只要用户拒绝就会一直弹窗），如下一致
                        // storagePermissions();
                        return false;
                    }
                    click=0;
                }
                if(grantResults[0]==-1&click==0){
                    dialog();
                }

                return false;
            case REQUEST_CAMEAR:
                for (int grantResult : grantResults) {
                    if (grantResult == PackageManager.PERMISSION_DENIED) {
                        dialog();
                        return false;
                    }
                }
                return true;
            case REQUEST_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    click=1;
                    if(ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_COARSE_LOCATION)){
                        Toast.makeText(activity,"需要允许定位权限",Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    if(grantResults[0]==0){
                        return true;
                    }
                    click=0;
                }else {
                    if(ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_COARSE_LOCATION)){
                        Toast.makeText(activity,"需要允许定位权限",Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    click=0;
                }
                if(grantResults[0]==-1&click==0){
                    dialog();
                }
                return false;
            case REQUEST_SEETINGS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    click=1;
                    if(ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_SETTINGS)){
                        Toast.makeText(activity,"需要允许SEET权限",Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    if(grantResults[0]==0){
                        return true;
                    }
                    click=0;
                }else {
                    if(ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_SETTINGS)){
                        Toast.makeText(activity,"需要SEET",Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    click=0;
                }
                if(grantResults[0]==-1&click==0){
                    dialog();
                }
                return false;
            case REQUEST_CHANGE_NEW_STATE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    click=1;
                    if(ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CHANGE_NETWORK_STATE)){
                        Toast.makeText(activity,"需要允许STATE权限",Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    if(grantResults[0]==0){
                        return true;
                    }
                    click=0;
                }else {
                    if(ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CHANGE_NETWORK_STATE)){
                        Toast.makeText(activity,"需要允许STATE权限",Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    click=0;
                }
                if(grantResults[0]==-1&click==0){
                    dialog();
                }
                return false;
            case REQUEST_RECORD:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    click=1;
                    if(ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.RECORD_AUDIO)){
                        // Toast.makeText(activity,"需要允许权限",Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    if(grantResults[0]==0){
                        return true;
                    }
                    click=0;
                }else {
                    if(ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.RECORD_AUDIO)){
                        // Toast.makeText(activity,"需要允许定位权限",Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    click=0;
                }
                if(grantResults[0]==-1&click==0){
                    dialog();
                }
                return false;
            case REQUEST_READ_PHONE_STATE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    click=1;
                    if(ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_PHONE_STATE)){
                        // Toast.makeText(activity,"需要允许权限",Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    if(grantResults[0]==0){
                        return true;
                    }
                    click=0;
                }else {
                    if(ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_PHONE_STATE)){
                        // Toast.makeText(activity,"需要允许定位权限",Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    click=0;
                }
                if(grantResults[0]==-1&click==0){
                    dialog();
                }
                return false;
            case REQUEST_GET_ACCOUNTS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    click=1;
                    if(ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.GET_ACCOUNTS)){
                        // Toast.makeText(activity,"需要允许权限",Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    if(grantResults[0]==0){
                        return true;
                    }
                    click=0;
                }else {
                    if(ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.GET_ACCOUNTS)){
                        // Toast.makeText(activity,"需要允许定位权限",Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    click=0;
                }
                if(grantResults[0]==-1&click==0){
                    dialog();
                }
                return false;
            case REQUEST_CALL_PHONE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    click=1;
                    if(ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CALL_PHONE)){
                        // Toast.makeText(activity,"需要允许权限",Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    if(grantResults[0]==0){
                        return true;
                    }
                    click=0;
                }else {
                    if(ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CALL_PHONE)){
                        // Toast.makeText(activity,"需要允许定位权限",Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    click=0;
                }
                if(grantResults[0]==-1&click==0){
                    dialog();
                }
                return false;
            case REQUEST_PROCESS_OUTGOING_CALLS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    click=1;
                    if(ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.PROCESS_OUTGOING_CALLS)){
                        // Toast.makeText(activity,"需要允许权限",Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    if(grantResults[0]==0){
                        return true;
                    }
                    click=0;
                }else {
                    if(ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.PROCESS_OUTGOING_CALLS)){
                        // Toast.makeText(activity,"需要允许定位权限",Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    click=0;
                }
                if(grantResults[0]==-1&click==0){
                    dialog();
                }
                return false;
            case REQUEST_READ_CALL_LOG:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    click=1;
                    if(ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_CALL_LOG)){
                        // Toast.makeText(activity,"需要允许权限",Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    if(grantResults[0]==0){
                        return true;
                    }
                    click=0;
                }else {
                    if(ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_CALL_LOG)){
                        // Toast.makeText(activity,"需要允许定位权限",Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    click=0;
                }
                if(grantResults[0]==-1&click==0){
                    dialog();
                }
                return false;
            case REQUEST_RECEIVE_SMS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    click=1;
                    if(ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_SMS)){
                        // Toast.makeText(activity,"需要允许权限",Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    if(grantResults[0]==0){
                        return true;
                    }
                    click=0;
                }else {
                    if(ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_SMS)){
                        // Toast.makeText(activity,"需要允许定位权限",Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    click=0;
                }
                if(grantResults[0]==-1&click==0){
                    dialog();
                }
                return false;
        }
        return false;
    }
    protected void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.custom_dialog);
        builder.setMessage("当前应用缺少必要条件，是否跳转设置打开相应权限？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //跳转手机权限

                //未测
//                Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
//                intent.addCategory(Intent.CATEGORY_DEFAULT);
//                intent.putExtra("com.cc.pc.designproject", BuildConfig.APPLICATION_ID);
//                try {
//                    activity.startActivity(intent);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }

                    //可以
//                Uri packageURI = Uri.parse("package:" + activity.getPackageName());
//                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
//                activity.startActivity(intent);

                Intent localIntent = new Intent();
                localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //判断android版本
                if (Build.VERSION.SDK_INT >= 9) {
                    localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                    localIntent.setData(Uri.fromParts("package", activity.getPackageName(), null));
                }
//                else if (Build.VERSION.SDK_INT <= 8) {
//                    localIntent.setAction(Intent.ACTION_VIEW);
//                    localIntent.setClassName("com.android.settings","com.android.settings.InstalledAppDetails");
//                    localIntent.putExtra("com.android.settings.ApplicationPkgName",activity.getPackageName());
//                }
                activity.startActivity(localIntent);
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
}

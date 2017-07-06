package com.app.utils;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * Created by MaShiZhao on 2016/12/6.
 */
public class Utils
{

    public static void makeToast(final Context context, final String text)
    {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static void makeToast(final Context context, final String text,
                                 final int time)
    {
        Toast.makeText(context, text, time).show();
    }


    public static boolean strIsPhone(final String inputStr)
    {
        //		String regExp = "^1(3[4-9]|5[012789]|8[23478])\\d{8}$|^18[019]\\d{8}$|^1(3[0-2]|5[56]|8[56])\\d{8}$|^1[35]3\\d{8}$|^14[57]\\d{8}$|^17[0678]\\d{8}$";
        //		Pattern p = Pattern.compile(regExp);
        //		Matcher m = p.matcher(text);
        //		return m.matches();
        //正则表达式
        //		String phone="^1[34578]\\d{9}$" ;
        //		Pattern pattern = Pattern.compile(phone);
        //		Matcher matcher = pattern.matcher(inputStr);
        //		if(matcher.matches()) return true;
        //		return false;
        if (TextUtils.isEmpty(inputStr)) return false;
        return true;
    }


    /**
     * dip转像素,不同分辨率适配
     *
     * @param context
     * @param dipValue
     * @return
     */
    public static int dip2px(final Context context, final float dipValue)
    {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) ((dipValue * scale) + 0.5f);
    }


    /**
     * 返回manifest文件里的版本名（versionName）
     *
     * @param context
     * @return
     */
    public static String getVersionName(final Context context)
    {
        String version = "0.0";
        final PackageManager packageManager = context.getPackageManager();
        try
        {
            final PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            version = packageInfo.versionName;
        } catch (final PackageManager.NameNotFoundException e)
        {
            L.e(e.getMessage());
        }
        return version;
    }


    /**
     * 获取设备序列号
     */
    public static String getDeviceId(Context context)
    {
        String num = getIMEI(context);
        if (num == null || num.equals(""))
        {
            num = getLocalMacAddress(context);
            if (num == null || num.equals(""))
            {
                num = getSimSerialNumber(context);
            }
        }
        return num;
    }

    /**
     * 获取手机串号
     */
    public static String getIMEI(Context context)
    {
        TelephonyManager telephone = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imei = telephone.getDeviceId();
        return imei;
    }

    /**
     * 获取设备序列号
     */
    public static String getSimSerialNumber(Context context)
    {
        TelephonyManager telephone = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String sn = telephone.getSimSerialNumber();
        return sn;
    }

    /**
     * 获取无线mac地址
     */
    public static String getLocalMacAddress(Context context)
    {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }


    public static void callPhone(Context c, String d)
    {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + d);
        intent.setData(data);
        c.startActivity(intent);
    }




    //zxing
    public static final boolean isChineseCharacter(String chineseStr) {
        char[] charArray = chineseStr.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            // 是否是Unicode编码,除了"�"这个字符.这个字符要另外处理
            if ((charArray[i] >= '\u0000' && charArray[i] < '\uFFFD')
                    || ((charArray[i] > '\uFFFD' && charArray[i] < '\uFFFF'))) {
                continue;
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context
     *            The context.
     * @param uri
     *            The Uri to query.
     * @author paulburke
     */
    @SuppressLint("NewApi")
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/"
                            + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] { split[1] };

                return getDataColumn(context, contentUri, selection,
                        selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context
     *            The context.
     * @param uri
     *            The Uri to query.
     * @param selection
     *            (Optional) Filter used in the query.
     * @param selectionArgs
     *            (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri,
                                       String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = { column };

        try {
            cursor = context.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri
     *            The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri
     *            The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri
     *            The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri
                .getAuthority());
    }
}

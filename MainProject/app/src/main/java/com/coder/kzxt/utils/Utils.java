package com.coder.kzxt.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/3/7.
 */

public class Utils {

    /**
     * 判断系统内intent存不存在
     */
    @SuppressWarnings("WrongConstant")
    public static boolean isIntentAvailable(Context context, Intent intent) {
        final PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.GET_ACTIVITIES);
        return list.size() > 0;
    }

    private static long lastClickTime;
    public synchronized static boolean isFastClick() {
        long time = System.currentTimeMillis();
        if ( time - lastClickTime < 1000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * 格式化浮点数据，保留1位小数
     */
    public static String formatFloat(double value) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(value);
    }


    public static String decimalForString(float number){
        DecimalFormat dFormat = new DecimalFormat("#.00");
        String resultString = dFormat.format(number);
        return resultString;
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        if(listAdapter != null){
            for (int i = 0; i < listAdapter.getCount(); i++) {
                View listItem = listAdapter.getView(i, null, listView);
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
            listView.setLayoutParams(params);
        }
    }

    /**
     * 解析提取url中的字段
     * @param url
     * @return
     */
    public static Map<String, Object> urlSplit(String url) {
        StringBuffer strbuf = new StringBuffer();
        StringBuffer strbuf2 = new StringBuffer();
        Map<String, Object> map = new HashMap<String, Object>();
        for (int i = 0; i < url.length(); i++) {
            if (url.substring(i, i + 1).equals("/")) {
                for (int n = i + 1; n < url.length(); n++) {
                    if (url.substring(n, n + 1).equals("/") || n == url.length() - 1) {
                        if (n == url.length() - 1) {
                            map.put(strbuf.toString(), url.substring(n, n + 1));
                        } else {
                            map.put(strbuf.toString(), strbuf2);
                        }
                        strbuf = new StringBuffer("");
                        strbuf2 = new StringBuffer("");
                        i = n;
                        break;
                    }
                    strbuf2.append(url.substring(n, n + 1));
                }
                continue;
            }
            strbuf.append(url.substring(i, i + 1));
        }
        return map;
    }

    /**
     * 保存照片到文件
     * @param bitmap
     * @param _file
     * @param png
     * @throws IOException
     */
    public static void saveBitmapToFile(final Bitmap bitmap, final String _file, boolean png) throws IOException {
        if (bitmap == null) {
            return;
        }

        BufferedOutputStream os = null;
        try {
            final File file = new File(_file);
            // String _filePath_file.replace(File.separatorChar +
            // file.getName(), "");
            final int end = _file.lastIndexOf(File.separator);
            final String _filePath = _file.substring(0, end);
            final File filePath = new File(_filePath);
            if (!filePath.exists()) {
                filePath.mkdirs();
            }
            file.createNewFile();
            os = new BufferedOutputStream(new FileOutputStream(file));
            if (png) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 80, os);
            } else {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 60, os);
            }
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (final IOException e) {
                }
            }
        }
    }

    /**
     * 设置价格
     * @param view
     * @param price
     */
    public static void setPrice(TextView view, String price){
         if(Float.parseFloat(price) == 0.0){
             view.setText("¥ "+ 0.00);
         } else {
             view.setText("¥ "+ price);
         }
    }


    /**
     * 获取版本号
     * @param context
     * @return
     */
    public static String getVersion(Context context) {// 获取版本号
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "获取版本名异常";
        }
    }

    public static String getShowTime(long milliseconds) {
        // 获取日历函数
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);
        SimpleDateFormat dateFormat = null;
        // 判断是否大于60分钟，如果大于就显示小时。设置日期格式
        if (milliseconds / 60000 > 60) {
            dateFormat = new SimpleDateFormat("hh:mm:ss");
        } else {
            dateFormat = new SimpleDateFormat("00:mm:ss");
        }
        return dateFormat.format(calendar.getTime());
    }



    //判断是否是纯数字
    public static Boolean isAllDigital(String resultString) {
        if (resultString == null) return false;
        Pattern p = Pattern.compile("[0-9]*");
        Matcher m = p.matcher(resultString);
        if (!m.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 提取参数
     * @param url
     * @return
     */
    public static Map<String, String> extracParameter(String url) {
        int n = 0;
        int m = 0;
        int conunt = 0;
        StringBuffer strbuf = new StringBuffer();
        Map<String, String> map = new HashMap<String, String>();
        for (int i = 0; i < url.length(); i++) {
            if (url.substring(i, i + 1).equals("|")) {
                conunt++;
                if (conunt == 1) {
                    n = i + 1;
                    map.put("timeStamp", url.substring(0, n-1));
                } else if (conunt == 2) {
                    m = i + 1;
                    map.put("courseId", url.substring(n, m-1));
                } else if (conunt == 3) {
                    map.put("classId", url.substring(m, i));
                    map.put("type", url.substring(i + 1, url.length()));
                }
            }
        }
        return map;
    }


    /**
     * 生成二维码.
     * @param url
     * @return
     */
    public static Bitmap createQRImage(String url) {
        try {
            // 判断URL合法性
            if (url == null || "".equals(url) || url.length() < 1) {
                return null;
            }
            Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            hints.put(EncodeHintType.MARGIN, 1);
            // 图像数据转换，使用了矩阵转换
            BitMatrix matrix = new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE, 300, 320,hints);
            int width = matrix.getWidth();
            int height = matrix.getHeight();
            int[] pixels = new int[width * height];
            // 下面这里按照二维码的算法，逐个生成二维码的图片，
            // 两个for循环是图片横列扫描的结果
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (matrix.get(x, y)) {
                        pixels[y * width + x] = 0xff000000;
                    } else {
                        pixels[y * width + x] = 0xffffffff;
                    }
                }
            }
            // 生成二维码图片的格式，使用ARGB_8888
            Bitmap bitmap = Bitmap.createBitmap(width, height,
                    Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }


}

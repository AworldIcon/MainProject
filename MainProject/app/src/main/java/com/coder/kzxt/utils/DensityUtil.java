package com.coder.kzxt.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;

public class DensityUtil {

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
//	public static int px2dip(Context context, float pxValue) {
//		final float scale = context.getResources().getDisplayMetrics().density;
//		return (int) (pxValue / scale + 0.5f);
//	}
	
	/**
	 * @param activity
	 * @return
	 * 获取屏幕物理尺寸
	 */
    @SuppressLint("NewApi")
    public static double getScreenSizeOfDevice(Activity activity) {
    	double screenInches;
        Point point = new Point();
        activity.getWindowManager().getDefaultDisplay().getRealSize(point);
        DisplayMetrics dm = activity.getResources().getDisplayMetrics();
        double x = Math.pow(point.x/ dm.xdpi, 2);
        double y = Math.pow(point.y / dm.ydpi, 2);
        screenInches = Math.sqrt(x + y);
        return screenInches;
    }
}

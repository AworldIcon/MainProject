package com.app.http;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.SparseArray;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Call;

/**
 * Activity声明周期回调
 *
 * @author zhousf
 */
public class BaseActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {

    private static final String TAG = BaseActivityLifecycleCallbacks.class.getSimpleName();

    /**
     * 请求集合: key=Activity value=Call集合
     */
    private static Map<Class<?>, SparseArray<Call>> callsMap = new ConcurrentHashMap<>();

    /**
     * 保存请求集合
     *
     * @param tag  请求标识
     * @param call 请求
     */
    public static void putCall(Class<?> tag, Call call) {
        if (null != tag) {
            SparseArray<Call> callList = callsMap.get(tag);
            if (null == callList) {
                callList = new SparseArray<>();
            }
            callList.put(call.hashCode(), call);
            callsMap.put(tag, callList);
            showLog(false);
        }

    }

    /**
     * 取消请求
     *
     * @param tag 请求标识
     */
    private static void cancelCallByActivityDestroy(Class<?> tag) {
        if (null == tag)
            return;
        SparseArray<Call> callList = callsMap.get(tag);
        if (null != callList) {
            final int len = callList.size();
            for (int i = 0; i < len; i++) {
                Call call = callList.valueAt(i);
                if (null != call && !call.isCanceled())
                    call.cancel();
            }
            callList.clear();
            callsMap.remove(tag);
            showLog(true);
        }
    }

    /**
     * 取消请求
     *
     * @param info 请求标识
     * @param call 请求
     */
    public static void cancelCall(Class<?> info, Call call) {
        if (null != call && null != info) {
            SparseArray<Call> callList = callsMap.get(info);
            if (null != callList) {
                Call c = callList.get(call.hashCode());
                if (null != c && !c.isCanceled())
                    c.cancel();
                callList.delete(call.hashCode());
                if (callList.size() == 0)
                    callsMap.remove(info);
                showLog(true);
            }
        }
    }

    private static void showLog(boolean isCancel) {
        String callDetail = isCancel ? "取消请求" : "增加请求";
        int originalSize = callsMap.size();
        int rest;
        if (originalSize > 0) {
            for (Map.Entry<Class<?>, SparseArray<Call>> entry : callsMap.entrySet()) {
                rest = entry.getValue().size();
//                L.d(TAG, callDetail + ": size = " + rest + " [" + entry.getKey().getName() + "]");
            }
        } else {
//            L.d(TAG, callDetail + ": size = 0 ");
        }
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        cancelCallByActivityDestroy(activity.getClass());
    }

}

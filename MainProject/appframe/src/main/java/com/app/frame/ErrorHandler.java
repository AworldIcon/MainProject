package com.app.frame;


import com.app.utils.L;

import java.lang.Thread.UncaughtExceptionHandler;



public class ErrorHandler implements UncaughtExceptionHandler
{
    /**
     * CrashHandler实例
     */
    private static ErrorHandler INSTANCE;
    private static volatile boolean onError = false;

    /**
     * 获取CrashHandler实例 ,单例模式
     */
    public static ErrorHandler getInstance()
    {
        if (ErrorHandler.INSTANCE == null)
        {
            ErrorHandler.INSTANCE = new ErrorHandler();
        }
        return ErrorHandler.INSTANCE;
    }

    /**
     * 保证只有一个CrashHandler实例
     */
    private ErrorHandler()
    {
    }

    /**
     * 初始化,注册Context对象, 获取系统默认的UncaughtException处理器, 设置该CrashHandler为程序的默认处理器
     *
     */
    public void setToErrorHandler()
    {
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    public void uncaughtException(final Thread thread, final Throwable ex)
    {
        // 处理异常
        L.e("崩溃简短信息:" + ex.getMessage());
        L.e("崩溃线程名称:" + thread.getName() + "崩溃线程ID:" + thread.getId());

        final StackTraceElement[] trace = ex.getStackTrace();
        for (final StackTraceElement element : trace)
        {
            L.e("Line " + element.getLineNumber() + " : " + element.toString());
        }
        ex.printStackTrace();

    }
}

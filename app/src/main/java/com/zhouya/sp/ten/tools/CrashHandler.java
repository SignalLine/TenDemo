package com.zhouya.sp.ten.tools;

import android.content.Context;
import android.util.Log;

/**
 * Project:Ten
 * Author:zhouya
 * Date: 2016/6/3
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler{
    private static CrashHandler instance;//单利模式

    private CrashHandler(){

    }

    public void init(Context ct){//初始化，把当前对象设置为UncaughtExceptionHandler处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    public synchronized static CrashHandler getInstance(){
        if(instance == null){
            instance = new CrashHandler();
        }
        return  instance;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        //当发生未处理异常，就会来到这里
        Log.d("uncaught-----","uncaughtException, thread: " + thread
                + " name: " + thread.getName() + " id: " + thread.getId() + "exception: "
                + ex);
        String threadName = thread.getName();
        if("NullPointException".equals(threadName)){
            Log.d("uncaught-----","threadName=-=-=--=" + threadName);
        }
        // TODO:根据异常进行日志记录，方便后面分析
    }
}

package com.example.fuckvivo;

import android.util.Log;


/**
 * Created on 2018-08-08 10:41.
 * @author zhangyf
 * 日志相关工具类
 *
 * @author zhangyf
 */
public class LogUtils {
    private static String logMember="yz_dev";

    public static void ffLog(String content){
        if (true){
            e(logMember,content);
        }
    }

    public static void v(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.v(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.i(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.w(tag, msg);
        }
    }
}

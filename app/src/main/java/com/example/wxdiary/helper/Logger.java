package com.example.wxdiary.helper;

import android.util.Log;

public class Logger {
    // Log 函数用于Debug时打印一些日志信息
    public static void D(String str) {
        Log.d("MyLogger", "-------"+str+"-------");
    }
}

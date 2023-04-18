package com.lhb.player.playerapp.utils;

import android.util.Log;

public class LogUtils {
    private static final int CURRENT_LEV = 5;
    private static final int DEBUG_LEV = 4;
    private static final int INFO_LEV = 3;
    private static final int WARNING_LEV = 2;
    private static final int ERROR_LEV = 1;

    public static void d(Class clazz, String msg) {
        if (CURRENT_LEV > DEBUG_LEV) {
            Log.d(clazz.getSimpleName(), msg);
        }
    }

    public static void i(Class clazz, String msg) {
        if (CURRENT_LEV > INFO_LEV) {
            Log.i(clazz.getSimpleName(), msg);
        }
    }

    public static void w(Class clazz, String msg) {
        if (CURRENT_LEV > WARNING_LEV) {
            Log.w(clazz.getSimpleName(), msg);
        }
    }

    public static void e(Class clazz, String msg) {
        if (CURRENT_LEV > ERROR_LEV) {
            Log.e(clazz.getSimpleName(), msg);
        }
    }
}

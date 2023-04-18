package com.lhb.player.playerapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by howe.zhong
 * on 2022/8/15  16:27
 */
public class ShareUtil {
    public static final String App = "user_info";
    private static SharedPreferences mShare;
    private static SharedPreferences.Editor mEditor;

    public static void setUp(Context context) {
        if (mShare == null) {
            mShare = context.getSharedPreferences(App, Context.MODE_PRIVATE);
            mEditor = mShare.edit();
        }
    }

    public static String getStringValue(String key) {
        checkSetUp();
        return mShare.getString(key, "");
    }

    public static boolean getBooleanValue(String key) {
        checkSetUp();
        return mShare.getBoolean(key, false);
    }

    public static boolean removeValue(String key) {
        checkSetUp();
        mEditor.remove(key);
        return commit();
    }

    public static void saveValue(String key, String value) {
        checkSetUp();
        mEditor.putString(key, value);
        commit();
    }

    public static void saveBoolean(String key, boolean value) {
        checkSetUp();
        mEditor.putBoolean(key, value);
        commit();
    }

    private static boolean commit() {
        mEditor.apply();
        return mEditor.commit();
    }

    private static void checkSetUp() {
        if (mShare == null || mEditor == null) {
            throw new RuntimeException("请先调用setUp方法");
        }
    }
}

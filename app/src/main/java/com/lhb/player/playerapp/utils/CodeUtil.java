package com.lhb.player.playerapp.utils;

import android.util.Base64;
import android.util.Log;

/**
 * Created by howe.zhong
 * on 2022/8/15  11:02
 */
public class CodeUtil {

    public static String getB64Auth(String login, String pass) {
        String source = login + ":" + pass;
        String ret = "Basic " + Base64.encodeToString(source.getBytes(), Base64.URL_SAFE | Base64.NO_WRAP);
        Log.e("Authorization", ret);
        return ret;
    }
}

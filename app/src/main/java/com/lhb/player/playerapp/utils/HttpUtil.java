package com.lhb.player.playerapp.utils;

import android.os.Handler;
import android.util.Log;

import com.lhb.player.playerapp.base.IBaseCallback;
import com.lhb.player.playerapp.model.bean.DetailsData;
import com.lhb.player.playerapp.view.IFileListCallback;
import com.lhb.player.playerapp.view.IDetailsCallback;
import com.thegrizzlylabs.sardineandroid.DavResource;
import com.thegrizzlylabs.sardineandroid.impl.OkHttpSardine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by howe.zhong
 * on 2022/8/14  17:19
 */
public class HttpUtil {
    private final static String TAG = "HttpUtil";
    private static HttpUtil sHttpUtil;
    private static OkHttpSardine mOkHttpSardine;
    private static Handler mHandler;

    private HttpUtil() {
        // todo: 获取 user password
        mOkHttpSardine = new OkHttpSardine();
        mOkHttpSardine.setCredentials("p1", "zcg20010911");
    }

    public static OkHttpSardine getInstance() {
        if (sHttpUtil == null) {
            sHttpUtil = new HttpUtil();
        }
        return mOkHttpSardine;
    }
}

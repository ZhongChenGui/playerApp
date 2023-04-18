package com.lhb.player.playerapp.presenters.Impl;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.lhb.player.playerapp.model.bean.DetailsData;
import com.lhb.player.playerapp.presenters.IDetailsListPresenter;
import com.lhb.player.playerapp.presenters.IFileListPresenter;
import com.lhb.player.playerapp.utils.CheckDataUtil;
import com.lhb.player.playerapp.utils.Constants;
import com.lhb.player.playerapp.utils.HttpUtil;
import com.lhb.player.playerapp.view.IDetailsCallback;
import com.lhb.player.playerapp.view.IFileListCallback;
import com.thegrizzlylabs.sardineandroid.DavResource;
import com.thegrizzlylabs.sardineandroid.impl.OkHttpSardine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by howe.zhong
 * on 2022/8/15  9:31
 */
public class DetailsPresenterImpl implements IDetailsListPresenter {
    private static final String TAG = "FileListPresenter";
    private final String mBaseURL;
    private IDetailsCallback mCallback;
    private Handler mHandler;

    public DetailsPresenterImpl(String baseURL) {
       mBaseURL = baseURL;
    }

    @Override
    public void registerViewCallback(IDetailsCallback callback) {
        this.mCallback = callback;
        this.mHandler = new MyHandler();
    }

    @Override
    public void unregisterViewCallback(IDetailsCallback callback) {
        mCallback = null;
        mHandler = null;
    }

    @Override
    public void onError() {
        if (mCallback != null) {
            Message message = new Message();
            message.what = ERROR;
            mHandler.sendMessage(message);
        }
    }

    @Override
    public void onEmpty() {
        if (mCallback != null) {
            Message message = new Message();
            message.what = EMPTY;
            mHandler.sendMessage(message);
        }
    }

    @Override
    public void getFileList(String url, String user, String password, boolean isCheckMp4) {
        if (mCallback != null) {
            mCallback.onLoading();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpSardine mOkHttpSardine = new OkHttpSardine();
                    mOkHttpSardine.setCredentials(user, password);
                    List<DavResource> list = mOkHttpSardine.list(url);
                    if (list.size() < 2) {
                        onEmpty();
                        return;
                    }
                    list = list.subList(1, list.size());   // 去掉第一个目录
                    Log.d(TAG, "run: list is -> " + list);
                    List<DetailsData> data = new ArrayList<>();
                    List<DavResource> targetList = CheckDataUtil.checkDirectory(list);
                    for (DavResource davResource : targetList) {
                        try {
                            List<DavResource> childList = mOkHttpSardine.list(mBaseURL + davResource.getHref());
                            childList = childList.subList(1, childList.size());
                            childList = CheckDataUtil.checkMp4(childList);
                            data.add(new DetailsData(davResource, childList));
                        } catch (Exception e) {
                            continue;
                        }
                    }
                    if (data.size() <= 0) {
                        onEmpty();
                        return;
                    }
                    onLoadSuccess(data);
                } catch (IOException e) {
                    onError();
                    Log.e(TAG, "run: " + e.toString());
                }
            }
        }).start();

    }

    @Override
    public void onLoadSuccess(List<DetailsData> list) {
        if (mCallback != null) {
            Message message = new Message();
            message.what = SUCCESS;
            message.obj = list;
            mHandler.sendMessage(message);
        }
    }

    class MyHandler extends Handler {

        public MyHandler() {
            super(Looper.getMainLooper());
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            if (mCallback == null) {
                return;
            }
            switch (msg.what) {
                case SUCCESS:
                    try {
                        List<DetailsData> list = (List<DetailsData>) msg.obj;
                        mCallback.onDetailsLoadedSuccess(list);
                    } catch (Exception e) {
                        mCallback.onError();
                    }
                    break;
                case ERROR:
                    mCallback.onError();
                    break;
                case EMPTY:
                    mCallback.onEmpty();
                    break;
            }
        }
    }
}

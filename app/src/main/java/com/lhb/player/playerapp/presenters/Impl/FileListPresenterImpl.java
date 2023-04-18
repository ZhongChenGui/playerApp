package com.lhb.player.playerapp.presenters.Impl;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.material.card.MaterialCardView;
import com.lhb.player.playerapp.presenters.IFileListPresenter;
import com.lhb.player.playerapp.utils.CheckDataUtil;
import com.lhb.player.playerapp.view.IFileListCallback;
import com.thegrizzlylabs.sardineandroid.DavResource;
import com.thegrizzlylabs.sardineandroid.impl.OkHttpSardine;

import java.io.IOException;
import java.util.List;

/**
 * Created by howe.zhong
 * on 2022/8/15  9:31
 */
public class FileListPresenterImpl implements IFileListPresenter {
    private static final String TAG = "FileListPresenter";
    private IFileListCallback mCallback;
    private Handler mHandler;

    @Override
    public void registerViewCallback(IFileListCallback callback) {
        this.mCallback = callback;
        this.mHandler = new MyHandler();
    }

    @Override
    public void unregisterViewCallback(IFileListCallback callback) {
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
                    if (list.size() <= 1) {
                        onEmpty();
                        return;
                    }
                    list = list.subList(1, list.size());   // 去掉第一个目录
                    List<DavResource> davResourceList;
                    if (isCheckMp4) {
                        davResourceList = CheckDataUtil.checkMp4(list);
                    } else {
                        davResourceList = CheckDataUtil.checkDirectory(list);
                    }
                    if (davResourceList.size() <= 0) {
                        onEmpty();
                    } else {
                        onLoadSuccess(davResourceList);
                    }
                } catch (Exception e) {
                    onError();
                    Log.e(TAG, "run: " + e.toString());
                }
            }
        }).start();

    }

    @Override
    public void onLoadSuccess(List<DavResource> list) {
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
                        List<DavResource> list = (List<DavResource>) msg.obj;
                        mCallback.onLoadedSuccess(list);
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

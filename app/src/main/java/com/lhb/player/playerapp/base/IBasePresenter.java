package com.lhb.player.playerapp.base;


public interface IBasePresenter<T> {

    int SUCCESS = 1;
    int ERROR = 2;
    int EMPTY = 3;

    void registerViewCallback(T callback);

    void unregisterViewCallback(T callback);

    void getFileList(String url, String user, String password, boolean isCheckMp4);

    void onError();

    void onEmpty();
}

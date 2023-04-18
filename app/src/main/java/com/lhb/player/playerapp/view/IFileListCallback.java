package com.lhb.player.playerapp.view;

import com.lhb.player.playerapp.base.IBaseCallback;
import com.thegrizzlylabs.sardineandroid.DavResource;

import java.util.List;

/**
 * Created by howe.zhong
 * on 2022/8/14  17:44
 */
public interface IFileListCallback extends IBaseCallback {
    void onLoadedSuccess(List<DavResource> list);
}

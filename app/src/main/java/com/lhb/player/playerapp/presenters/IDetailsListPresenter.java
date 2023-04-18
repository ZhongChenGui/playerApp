package com.lhb.player.playerapp.presenters;

import com.lhb.player.playerapp.base.IBasePresenter;
import com.lhb.player.playerapp.model.bean.DetailsData;
import com.lhb.player.playerapp.view.IDetailsCallback;
import com.lhb.player.playerapp.view.IFileListCallback;
import com.thegrizzlylabs.sardineandroid.DavResource;

import java.util.List;

/**
 * Created by howe.zhong
 * on 2022/8/15  9:29
 */
public interface IDetailsListPresenter extends IBasePresenter<IDetailsCallback> {


    void onLoadSuccess(List<DetailsData> list);


}

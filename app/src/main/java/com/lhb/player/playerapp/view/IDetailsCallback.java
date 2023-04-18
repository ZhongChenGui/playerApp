package com.lhb.player.playerapp.view;

import com.lhb.player.playerapp.base.IBaseCallback;
import com.lhb.player.playerapp.model.bean.DetailsData;

import java.util.List;

/**
 * Created by howe.zhong
 * on 2022/8/14  17:44
 */
public interface IDetailsCallback extends IBaseCallback {

    void onDetailsLoadedSuccess(List<DetailsData> list);
}

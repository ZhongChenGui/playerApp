package com.lhb.player.playerapp.view;

import com.lhb.player.playerapp.base.IBaseCallback;
import com.lhb.player.playerapp.model.bean.HistoryData;

public interface IHistoryCallback extends IBaseCallback {
    /**
     * 数据加载成功
     */
    void onHistoryDataLoaded(HistoryData historyData);
}

package com.lhb.player.playerapp.model.bean;

import java.util.List;

/**
 * Created by howe.zhong
 * on 2022/8/9  13:48
 */
public class HistoryData {

    private List<DataBean> mDataBeans;

    public List<DataBean> getDataBeans() {
        return mDataBeans;
    }

    public void setDataBeans(List<DataBean> dataBeans) {
        mDataBeans = dataBeans;
    }

    public static class DataBean{
        private String title;
        private int imgUri;
        private float playTime;
        private float countTime;

        public DataBean(String title, int imgUri, float playTime, float countTime) {
            this.title = title;
            this.imgUri = imgUri;
            this.playTime = playTime;
            this.countTime = countTime;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getImgUri() {
            return imgUri;
        }

        public void setImgUri(int imgUri) {
            this.imgUri = imgUri;
        }

        public float getPlayTime() {
            return playTime;
        }

        public void setPlayTime(float playTime) {
            this.playTime = playTime;
        }

        public float getCountTime() {
            return countTime;
        }

        public void setCountTime(float countTime) {
            this.countTime = countTime;
        }
    }
}

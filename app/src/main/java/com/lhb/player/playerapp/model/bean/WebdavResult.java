package com.lhb.player.playerapp.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by howe.zhong
 * on 2022/8/16  15:26
 */
public class WebdavResult implements Parcelable {

    private List<WebdavResultBean> mBeans;

    public WebdavResult() {
    }

    protected WebdavResult(Parcel in) {
        mBeans = in.createTypedArrayList(WebdavResultBean.CREATOR);
    }

    public static final Creator<WebdavResult> CREATOR = new Creator<WebdavResult>() {
        @Override
        public WebdavResult createFromParcel(Parcel in) {
            return new WebdavResult(in);
        }

        @Override
        public WebdavResult[] newArray(int size) {
            return new WebdavResult[size];
        }
    };

    public List<WebdavResultBean> getBeans() {
        return mBeans;
    }

    public void setBeans(List<WebdavResultBean> beans) {
        mBeans = beans;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(mBeans);
    }

    @Override
    public String toString() {
        return "WebdavResult{" +
                "mBeans=" + mBeans +
                '}';
    }

    public static class  WebdavResultBean implements Parcelable{
        private String name;
        private String href;

        protected WebdavResultBean(Parcel in) {
            name = in.readString();
            href = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(name);
            dest.writeString(href);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<WebdavResultBean> CREATOR = new Creator<WebdavResultBean>() {
            @Override
            public WebdavResultBean createFromParcel(Parcel in) {
                return new WebdavResultBean(in);
            }

            @Override
            public WebdavResultBean[] newArray(int size) {
                return new WebdavResultBean[size];
            }
        };

        @Override
        public String toString() {
            return "WebdavResultBean{" +
                    "name='" + name + '\'' +
                    ", href='" + href + '\'' +
                    '}';
        }

        public WebdavResultBean(String name, String href) {
            this.name = name;
            this.href = href;
        }


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getHref() {
            return href;
        }

        public void setHref(String href) {
            this.href = href;
        }


    }


}

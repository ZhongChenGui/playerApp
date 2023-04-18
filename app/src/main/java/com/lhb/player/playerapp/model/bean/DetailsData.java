package com.lhb.player.playerapp.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.thegrizzlylabs.sardineandroid.DavResource;

import java.io.Serializable;
import java.util.List;

/**
 * Created by howe.zhong
 * on 2022/8/14  17:57
 */
public class DetailsData {
    private DavResource title;
    private List<DavResource> childDavResources;

    public DetailsData() {
    }

    public DetailsData(DavResource title, List<DavResource> childDavResources) {
        this.title = title;
        this.childDavResources = childDavResources;
    }

    public DavResource getTitle() {
        return title;
    }

    public void setTitle(DavResource title) {
        this.title = title;
    }

    public List<DavResource> getChildDavResources() {
        return childDavResources;
    }

    public void setChildDavResources(List<DavResource> childDavResources) {
        this.childDavResources = childDavResources;
    }
}

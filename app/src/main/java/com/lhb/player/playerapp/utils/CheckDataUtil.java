package com.lhb.player.playerapp.utils;

import com.thegrizzlylabs.sardineandroid.DavResource;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by howe.zhong
 * on 2022/8/15  9:49
 */
public class CheckDataUtil {
    public static List<DavResource> checkDirectory(List<DavResource> list) {
        List<DavResource> davResourceList = new ArrayList<>();
        for (DavResource davResource : list) {
            String name = davResource.getName();
            if (davResource.getContentType().equals("httpd/unix-directory") && !name.startsWith("#") && !name.startsWith(".")) {
                davResourceList.add(davResource);
            }
        }
        return davResourceList;
    }

    public static List<DavResource> checkMp4(List<DavResource> list) {
        List<DavResource> davResourceList = new ArrayList<>();
        for (DavResource davResource : list) {
            String name = davResource.getName();
            if (davResource.getContentType().equals("video/mp4") && !name.startsWith("#") && !name.startsWith(".")) {
                davResourceList.add(davResource);
            }
        }
        return davResourceList;
    }

}

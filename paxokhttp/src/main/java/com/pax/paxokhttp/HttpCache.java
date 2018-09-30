package com.pax.paxokhttp;

import java.io.File;

import okhttp3.Cache;

/**
 * @author ligq
 * @date 2018/9/30
 */

class HttpCache {
    private static final int HTTP_RESPONSE_DISK_CACHE_MAX_SIZE = 50 * 1024 * 1024;

    public static Cache getCache() {
        return new Cache(new File(AppUtils.getApp().getCacheDir().getAbsolutePath() + File
                .separator + "data/NetCache"),
                HTTP_RESPONSE_DISK_CACHE_MAX_SIZE);
    }
}

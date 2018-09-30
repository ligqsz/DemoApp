package com.pax.paxokhttp;

import android.annotation.SuppressLint;
import android.app.Application;

/**
 * @author ligq
 * @date 2018/9/30
 */

public class AppUtils {
    @SuppressLint("StaticFieldLeak")
    private static Application mApp;

    private AppUtils() {
        throw new IllegalArgumentException();
    }

    public static void init(Application application) {
        synchronized (AppUtils.class) {
            if (mApp == null) {
                mApp = application;
                OkHttpUtils.initClient(mApp, OkHttpUtils.DEFAULT_TIMEOUT);
            }
        }
    }

    public static Application getApp() {
        return mApp;
    }
}

package com.pax.demoapp;

import android.app.Application;

import com.pax.demoapp.config.MenuConfig;
import com.pax.demoapp.db.greendao.manager.DaoManager;
import com.pax.demoapp.utils.OtherUtils;
import com.pax.paxokhttp.okhttp.AppUtils;
import com.pax.utils.LogUtils;
import com.pax.utils.Utils;

/**
 * @author ligq
 * @date 2018/7/5
 */

public class DemoApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        AppUtils.init(this);
        initUtils();
        DaoManager.initDb(this, MenuConfig.DB_NAME);
    }

    private void initUtils() {
        Utils.init(this);
        boolean showLog = 0 == OtherUtils.getMetaInt("SHOW_LOG");
        LogUtils.getConfig().setShowLog(showLog);
    }
}

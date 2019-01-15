package com.pax.demoapp;

import android.app.Application;

import com.pax.demoapp.common.design.Singleton;
import com.pax.demoapp.config.MenuConfig;
import com.pax.demoapp.db.greendao.manager.DaoManager;
import com.pax.demoapp.utils.OtherUtils;
import com.pax.paxokhttp.okhttp.AppUtils;
import com.pax.utils.LogUtils;
import com.pax.utils.Utils;

/**
 * 1.少量使用if+else if方式,可参照:<a href = "https://mp.weixin.qq.com/s?__biz=MzA3MjgwNDIzNQ==&mid=2651943897&idx=1&sn=92e27c915125c70629121c7d9117881e&chksm=84fd613db38ae82bd0b0374b562a44d7e026b822cfbc046d619d079f9d7100a3675040b4c3a3&mpshare=1&scene=1&srcid=11139E6btWmUX9AjOJNc9fCx#rd"/>
 * 2.今日头条方案详见androidTest.com.pax.demoapp.DensityTest
 * 3.单例模式创建方式推荐:{@link Singleton#getInstance()}
 *
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

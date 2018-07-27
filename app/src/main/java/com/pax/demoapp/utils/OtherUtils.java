package com.pax.demoapp.utils;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.pax.demoapp.DemoApp;
import com.pax.demoapp.db.orm.dao.StudentDao;

import java.util.Random;

/**
 * @author ligq
 * @date 2018/7/17
 */

public class OtherUtils {
    private static final String[] WORDS = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",};

    private OtherUtils() {
        throw new IllegalArgumentException();
    }

    public static int getRandomNum(int baseNum) {
        return new Random().nextInt(10) + baseNum;
    }

    public static String getRandomNum() {
        String num = String.valueOf(new Random().nextInt(100));
        StudentDao dao = StudentDao.getInstance();
        while (!dao.findListByNum(num).isEmpty()) {
            num = String.valueOf(new Random().nextInt(100));
        }
        return num;
    }

    public static String getRandomWord() {
        return WORDS[new Random().nextInt(WORDS.length)];
    }

    /**
     * 获取meta数据
     *
     * @param metaKey metaKey
     * @return meta data
     */
    public static String getMeta(String metaKey) {
        ApplicationInfo appInfo;
        try {
            DemoApp app = DemoApp.getApp();
            appInfo = app.getPackageManager()
                    .getApplicationInfo(app.getPackageName(), PackageManager.GET_META_DATA);
            return appInfo.metaData.getString(metaKey);
        } catch (PackageManager.NameNotFoundException e) {
            LogUtils.e(e);
        }
        return "";
    }

    public static int getMetaInt(String metaKey) {
        ApplicationInfo appInfo;
        try {
            DemoApp app = DemoApp.getApp();
            appInfo = app.getPackageManager()
                    .getApplicationInfo(app.getPackageName(), PackageManager.GET_META_DATA);
            return appInfo.metaData.getInt(metaKey);
        } catch (PackageManager.NameNotFoundException e) {
            LogUtils.e(e);
        }
        return 0;
    }
}

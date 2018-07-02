package com.pax.demoapp;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;

/**
 * @author ligq
 * @date 2018/6/28
 */

public class Density {
    private static float appDensity;

    private static float appScaledDensity;

    private Density() {
        throw new IllegalArgumentException();
    }

    /**
     * 此方法在Application的onCreate方法中调用    Density.setDensity(this);
     *
     * @param application application
     */
    public static void setDensity(@NonNull Application application, Activity activity) {
        //获取application的DisplayMetrics
        DisplayMetrics appDisplayMetrics = application.getResources().getDisplayMetrics();

        if (appDensity == 0) {
            //初始化的时候赋值（只在Application里面初始化的时候会调用一次）
            appDensity = appDisplayMetrics.density;
            appScaledDensity = appDisplayMetrics.scaledDensity;

            //添加字体变化的监听
            application.registerComponentCallbacks(new ComponentCallbacks() {
                @Override
                public void onConfigurationChanged(Configuration newConfig) {
                    //字体改变后,将appScaledDensity重新赋值
                    if (newConfig != null && newConfig.fontScale > 0) {
                        appScaledDensity = application.getResources().getDisplayMetrics().scaledDensity;
                    }
                }

                @Override
                public void onLowMemory() {
                    //do nothing
                }
            });
        }

        //调用修改density值的方法(默认以宽度作为基准)
        final float targetDensity = appDisplayMetrics.widthPixels / 360f;
        final float targetScaleDensity = targetDensity * (appScaledDensity / appDensity);
        final int targetDensityDpi = (int) (160 * targetDensity);

        appDisplayMetrics.density = targetDensity;
        appDisplayMetrics.scaledDensity = targetScaleDensity;
        appDisplayMetrics.densityDpi = targetDensityDpi;

        final DisplayMetrics activityDisplayMetrics = activity.getResources().getDisplayMetrics();
        activityDisplayMetrics.density = targetDensity;
        activityDisplayMetrics.scaledDensity = targetScaleDensity;
        activityDisplayMetrics.densityDpi = targetDensityDpi;
    }
}

package com.pax.demoapp.utils;

import android.content.Context;
import android.support.annotation.NonNull;

/**
 * @author ligq
 * @date 2018/8/7
 */

public class DensityUtils {

    private DensityUtils() {
        throw new IllegalStateException();
    }

    /**
     * Value of dp to value of px.
     *
     * @param dpValue The value of dp.
     * @return value of px
     */
    public static int dp2px(final float dpValue) {
        return dp2px(Utils.getApp(), dpValue);
    }

    /**
     * Value of dp to value of px.
     *
     * @param context The context.
     * @param dpValue The value of dp.
     * @return value of px
     */
    public static int dp2px(@NonNull final Context context, final float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * Value of px to value of dp.
     *
     * @param pxValue The value of px.
     * @return value of dp
     */
    public static int px2dp(final float pxValue) {
        return px2dp(Utils.getApp(), pxValue);
    }

    /**
     * Value of px to value of dp.
     *
     * @param context The context.
     * @param pxValue The value of px.
     * @return value of dp
     */
    public static int px2dp(@NonNull final Context context, final float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    //3.px转sp
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    //4.sp转px
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}

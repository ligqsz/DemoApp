package com.pax.demoapp.utils;

import android.widget.Toast;

import com.pax.demoapp.DemoApp;

/**
 * @author ligq
 * @date 2018/7/27
 */

public class ToastUtils {
    private ToastUtils() {
        throw new IllegalArgumentException();
    }

    public static void showShort(String msg) {
        Toast.makeText(DemoApp.getApp(), msg, Toast.LENGTH_SHORT).show();
    }
}

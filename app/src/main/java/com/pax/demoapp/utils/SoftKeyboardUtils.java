package com.pax.demoapp.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.pax.demoapp.DemoApp;

/**
 * @author ligq
 * @date 2018/7/5
 */

public class SoftKeyboardUtils {

    private SoftKeyboardUtils() {
        throw new IllegalArgumentException();
    }

    /**
     * 显示软键盘
     * 当前界面必须已经加载完成，不能直接在Activity的onCreate()，onResume()，onAttachedToWindow()
     * 中使用，可以在这些方法中通过postDelayed的方式来延迟执行showSoftInput()。
     *
     * @param view view必须是VISIBLE的EditText
     */
    public static void showKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) DemoApp.getApp().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            view.requestFocus();
            imm.showSoftInput(view, 0);
        }
    }

    /**
     * 隐藏软键盘
     *
     * @param view view可以当前布局中已经存在的任何一个View，如果找不到可以用getWindow().getDecorView()
     */
    public static void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) DemoApp.getApp().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}

package com.pax.demoapp.config;

/**
 * @author ligq
 * @date 2018/7/5
 */

public class MenuConfig {
    public static final String[] MENU_MAIN = {"弹出按钮框", "弹出ProgressBar", "跳转pager界面"
            , "测试改变软键盘Enter键"};

    public static final String[] MENU_EDIT = {"actionDone", "actionGo", "actionNext"
            , "actionNone", "actionSearch", "actionSend"};

    public static final int DEFAULT_HEIGHT = 120;

    private MenuConfig() {
        throw new IllegalArgumentException();
    }
}

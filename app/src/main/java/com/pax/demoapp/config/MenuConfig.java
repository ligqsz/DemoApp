package com.pax.demoapp.config;

/**
 * @author ligq
 * @date 2018/7/5
 */

public class MenuConfig {
    public static final String[] MENU_MAIN = {"弹出按钮框", "弹出ProgressBar", "跳转pager界面"
            , "测试改变软键盘Enter键", "ToolBarActivity", "OrmLite Demo", "Green Dao Demo"
            , "custom view demo", "动画效果", "test", "Okhttp+Retrofit+RxJava请求测试", "RxJava测试"};

    public static final String[] MENU_EDIT = {"actionDone", "actionGo", "actionNext"
            , "actionNone", "actionSearch", "actionSend"};
    public static final String[] MENU_ORM = {"insert", "delete(id)", "deleteAll"
            , "update", "query(id)", "query(All)", "query(reverse,limit)", "count(province,age)"};

    public static final String[] MENU_GREEN_DAO = {"insert", "deleteAll"
            , "update", "query(All)"};

    public static final String[] MENU_ANIM = {"属性动画", "触摸反馈动画", "揭露动画", "转场动画"};
    public static final String[] MENU_RX_JAVA = {"Create-Timer", "Create-Interval", "Filter-Skip",
            "Filter-Debounce", "Merge-MergeDelayError", "Merge-SwitchOnNext", "Sub-Delay", "Sub-TimeInterval", "Sub-Timestamp", "Sub-SubscribeOn"};
    public static final int DEFAULT_HEIGHT = 120;
    public static final String DB_NAME = "data";
    public static final boolean ENCRYPTED = false;

    private MenuConfig() {
        throw new IllegalArgumentException();
    }

}

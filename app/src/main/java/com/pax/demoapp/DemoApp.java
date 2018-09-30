package com.pax.demoapp;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;

import com.pax.demoapp.config.MenuConfig;
import com.pax.demoapp.db.greendao.manager.DaoManager;
import com.pax.demoapp.ui.activity.EditTextActivity;
import com.pax.demoapp.ui.activity.IActivity;
import com.pax.demoapp.utils.LogUtils;
import com.pax.demoapp.utils.OtherUtils;
import com.pax.demoapp.utils.ScreenUtils;
import com.pax.demoapp.utils.Utils;
import com.pax.paxokhttp.okhttp.AppUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author ligq
 * @date 2018/7/5
 */

public class DemoApp extends Application {
    public static final String TAG = DemoApp.class.getSimpleName();
    private static DemoApp app;
    private List<Activity> activityLinkedList;
    private ScheduledExecutorService executorService;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        init();
    }

    private void init() {
        AppUtils.init(this);
        initUtils();
        initExecutorService();
        initLifeCycleCallBack();
        DaoManager.initDb(this, MenuConfig.DB_NAME);
    }

    private void initUtils() {
        Utils.init(this);
        boolean showLog = 0 == OtherUtils.getMetaInt("SHOW_LOG");
        LogUtils.getConfig().setLogSwitch(showLog).setLog2FileSwitch(showLog);
    }

    private void initExecutorService() {
        executorService = new ScheduledThreadPoolExecutor(1, r -> {
            Thread thread = new Thread(r, "ScheduledThreadPoolExecutor Task");
            thread.setPriority(Thread.MIN_PRIORITY);
            thread.setDaemon(true);
            return thread;
        });
    }

    private void initLifeCycleCallBack() {
        activityLinkedList = new LinkedList<>();
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                LogUtils.d(TAG, "onActivityCreated: " + activity.getLocalClassName());
                LogUtils.d(TAG, "Pid: " + Process.myPid());
                activityLinkedList.add(activity);
                ScreenUtils.adaptScreen4VerticalSlide(activity, 360);
                if (activity instanceof IActivity) {
                    IActivity iActivity = (IActivity) activity;
                    activity.setContentView(iActivity.getLayoutId());
                    iActivity.initData();
                    iActivity.initView();
                }
            }

            @Override
            public void onActivityStarted(Activity activity) {
                LogUtils.d(TAG, "onActivityStarted: " + activity.getLocalClassName());
            }

            @Override
            public void onActivityResumed(Activity activity) {
                LogUtils.d(TAG, "onActivityResumed: " + activity.getLocalClassName());
            }

            @Override
            public void onActivityPaused(Activity activity) {
                LogUtils.d(TAG, "onActivityPaused: " + activity.getLocalClassName());
            }

            @Override
            public void onActivityStopped(Activity activity) {
                LogUtils.d(TAG, "onActivityStopped: " + activity.getLocalClassName());
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                LogUtils.d(TAG, "onActivitySaveInstanceState: " + activity.getLocalClassName());
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                LogUtils.d(TAG, "onActivityDestroyed: " + activity.getLocalClassName());
                activityLinkedList.remove(activity);
            }
        });
    }

    public void runInBackGround(Runnable runnable) {
        executorService.execute(runnable);
    }

    public void startTask() {
        executorService.scheduleAtFixedRate(() -> {
            LogUtils.d("TASK:jump main");
            if (activityLinkedList.get(activityLinkedList.size() - 1) instanceof EditTextActivity) {
                return;
            }
            jump(EditTextActivity.class);
        }, 3, 3, TimeUnit.SECONDS);
    }


    public void showList() {
        for (Activity activity : activityLinkedList) {
            LogUtils.d(TAG, "showList: " + activity.getLocalClassName());
        }
    }

    public void exitAppList() {
        for (Activity activity : activityLinkedList) {
            activity.finish();
        }
    }

    public static DemoApp getApp() {
        return app;
    }

    public void jump(Class clazz) {
        Intent intent = new Intent(this, clazz);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}

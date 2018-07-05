package com.pax.demoapp;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Process;
import android.util.Log;

import com.blankj.utilcode.util.Utils;
import com.pax.demoapp.ui.activity.IActivity;

import java.util.LinkedList;
import java.util.List;

/**
 * @author ligq
 * @date 2018/7/5
 */

public class DemoApp extends Application {
    public static final String TAG = DemoApp.class.getSimpleName();
    private static DemoApp app;
    private List<Activity> activityLinkedList;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        activityLinkedList = new LinkedList<>();
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                Log.d(TAG, "onActivityCreated: " + activity.getLocalClassName());
                Log.d(TAG, "Pid: " + Process.myPid());
                activityLinkedList.add(activity);
                if (activity instanceof IActivity) {
                    IActivity iActivity = (IActivity) activity;
                    activity.setContentView(iActivity.getLayoutId());
                    iActivity.initData();
                    iActivity.initView();
                }
            }

            @Override
            public void onActivityStarted(Activity activity) {
                Log.d(TAG, "onActivityStarted: " + activity.getLocalClassName());
            }

            @Override
            public void onActivityResumed(Activity activity) {
                Log.d(TAG, "onActivityResumed: " + activity.getLocalClassName());
            }

            @Override
            public void onActivityPaused(Activity activity) {
                Log.d(TAG, "onActivityPaused: " + activity.getLocalClassName());
            }

            @Override
            public void onActivityStopped(Activity activity) {
                Log.d(TAG, "onActivityStopped: " + activity.getLocalClassName());
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                Log.d(TAG, "onActivitySaveInstanceState: " + activity.getLocalClassName());
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                Log.d(TAG, "onActivityDestroyed: " + activity.getLocalClassName());
                activityLinkedList.remove(activity);
            }
        });

        Utils.init(this);
    }


    public void showList() {
        for (Activity activity : activityLinkedList) {
            Log.d(TAG, "showList: " + activity.getLocalClassName());
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
}

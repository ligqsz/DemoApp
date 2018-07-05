package com.pax.demoapp.utils;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.pax.demoapp.DemoApp;
import com.pax.demoapp.ui.activity.MainActivity;

import java.util.List;

/**
 * 几种退出应用的方式
 *
 * @author ligq
 * @date 2018/7/5
 */

@SuppressWarnings("unused")
public class ActivityUtils {

    private ActivityUtils() {
        throw new IllegalArgumentException();
    }

    /**
     * 方式1:直接调用系统API获取当前的任务栈，
     * 把里面的Activity全部finish掉，再结束进程（如果不想结束进程，可以不调用System.exit(0)）
     * 缺点:这个方法只能结束当前任务栈,如果该应用有多个任务栈(SingleInstance),则不适用
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void exitAPP1() {
        ActivityManager activityManager = (ActivityManager) DemoApp.getApp().getSystemService(Context.ACTIVITY_SERVICE);
        assert activityManager != null;
        List<ActivityManager.AppTask> appTaskList = activityManager.getAppTasks();
        for (ActivityManager.AppTask appTask : appTaskList) {
            appTask.finishAndRemoveTask();
        }
//        <p>appTaskList.get(0).finishAndRemoveTask();<p>
//         <p>System.exit(0);<p>
    }

    /**
     * 方式二:在BaseApplication存放activityLinkedList
     * ,通过registerActivityLifecycleCallbacks()方法来控制所有Activity实例的增删。
     * 缺点:采用这种方法，遇到Activity不是经过正常生命周期创建和结束的情况，是达不到退出的效果的
     */
    public static void exitApp2() {
        //打印存储的activity
        DemoApp.getApp().showList();
        //销毁存储的activity
        DemoApp.getApp().exitAppList();
    }

    /**
     * 方式三:让APP的入口Activity采用SingleTask启动模式,如果在后面的Activity启动这个处于任务栈底部
     * 的Activity的时候，就会调用它的onNewIntent()方法，在这个方法里根据Intent判断是否调用finish，
     * 是的话那么整个任务栈的Activity就都结束了
     * 这里必须要重写入口Activity的onNewIntent方法,{@link MainActivity#onNewIntent(Intent)}
     */
    public static void exitAPP3() {
        Intent intent = new Intent(DemoApp.getApp(), MainActivity.class);
        intent.putExtra("exit", true);
        DemoApp.getApp().startActivity(intent);
        System.exit(0);
    }
}
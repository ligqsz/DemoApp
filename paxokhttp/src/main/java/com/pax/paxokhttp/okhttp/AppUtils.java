package com.pax.paxokhttp.okhttp;

import android.annotation.SuppressLint;
import android.app.Application;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author ligq
 * @date 2018/9/30
 */

public class AppUtils {
    @SuppressLint("StaticFieldLeak")
    private static Application mApp;
    private static Disposable mDisposable;

    private AppUtils() {
        throw new IllegalArgumentException();
    }

    public static void init(Application application) {
        synchronized (AppUtils.class) {
            if (mApp == null) {
                mApp = application;
                OkHttpUtils.initClient(mApp, OkHttpUtils.DEFAULT_TIMEOUT);
            }
        }
    }

    public static Application getApp() {
        return mApp;
    }

    public static void runInBackground(Runnable runnable) {
        exeTask(runnable, false);
    }

    public static void runOnUiThread(Runnable runnable) {
        exeTask(runnable, true);
    }

    private static void exeTask(Runnable runnable, boolean runOnUi) {
        disposable();
        mDisposable = Observable.just(runnable)
                .observeOn(runOnUi ? AndroidSchedulers.mainThread() : Schedulers.newThread())
                .subscribe(new Consumer<Runnable>() {
                    @Override
                    public void accept(Runnable runnable) throws Exception {
                        runnable.run();
                    }
                });
    }

    private static void disposable() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
        mDisposable = null;
    }
}

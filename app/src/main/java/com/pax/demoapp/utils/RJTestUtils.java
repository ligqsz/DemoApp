package com.pax.demoapp.utils;

import android.os.SystemClock;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @author ligq
 * @date 2018/10/15
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
public class RJTestUtils {
    private static Disposable disposable;

    public static void testTimer() {
        disposable();
        disposable = Observable.timer(2, TimeUnit.SECONDS, Schedulers.io())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        ToastUtils.showShort(aLong.toString());
                    }
                });
    }

    public static void testInterval() {
        disposable();
        disposable = Observable.interval(3, TimeUnit.SECONDS)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        ToastUtils.showShort(aLong.toString());
                    }
                });
    }

    /**
     * todo
     */
    public static void testSkip() {
        disposable();
        Observable.interval(500, TimeUnit.MICROSECONDS)
                .skip(2, TimeUnit.SECONDS)
                .observeOn(Schedulers.io())
                .subscribe(new Observer<Long>() {
                    Long num = 0L;

                    @Override
                    public void onSubscribe(Disposable d) {
                        if (num > 10) {
                            d.dispose();
                        }
                    }

                    @Override
                    public void onNext(Long aLong) {
                        num = aLong;
                        LogUtils.d(aLong.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e(e);
                    }

                    @Override
                    public void onComplete() {
                        LogUtils.d("onComplete");
                    }
                });
    }

    /**
     * todo
     */
    public static void testDebounce() {
        disposable();
        Integer[] ints = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        disposable = Observable.fromArray(ints).flatMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(Integer integer) throws Exception {
                SystemClock.sleep(integer * 200);
                return Observable.just(integer.toString());
            }
        }).subscribeOn(Schedulers.newThread())
                .debounce(1, TimeUnit.SECONDS)
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        ToastUtils.showShort(s);
                    }
                });
    }

    public static void disposable() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
        disposable = null;
    }
}

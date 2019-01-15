package com.pax.demoapp.rxjava;

import android.support.annotation.NonNull;

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author ligq
 * @date 2018/12/5 14:34
 */
public class RxTest {
    @Test
    public void testFrom() {
        ExecutorService executor = Executors.newScheduledThreadPool(1, new ThreadFactory() {
            @Override
            public Thread newThread(@NonNull Runnable r) {
                return new Thread(r, "my executor");
            }
        });
        Scheduler observeOnSchedule = Schedulers.from(Executors.newSingleThreadExecutor(new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "observeOnThread");
            }
        }));
        Observable<Integer> observable = Observable.just(1).observeOn(Schedulers.from(executor));
        Observable<Integer> observable1 = Observable.just(2).observeOn(observeOnSchedule);
        Observable.concat(observable, observable1)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        System.out.println("current thread name:" + Thread.currentThread());
                    }
                });

    }
}

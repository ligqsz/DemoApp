package com.pax.demoapp.rxjava;


import org.junit.Test;

import java.util.Random;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

import static com.pax.demoapp.rxjava.Utils.parIntSafe;
import static com.pax.demoapp.rxjava.Utils.print;

/**
 * @author ligq
 * @date 2018/10/16
 */
public class RxJavaErrorTest {
    /**
     * 遇到错误时，发送1个特殊事件 & 正常终止
     * 可捕获在它之前发生的异常
     */
    @Test
    public void testOnErrorReturn() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onError(new Throwable("发生了错误!!!"));
            }
        })
                .onErrorReturn(new Function<Throwable, Integer>() {
                    @Override
                    public Integer apply(Throwable throwable) throws Exception {
                        print(throwable.toString());
                        return -1;
                    }
                })
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        print(d.toString());
                    }

                    @Override
                    public void onNext(Integer integer) {
                        print("接收到了事件:" + integer);
                    }

                    @Override
                    public void onError(Throwable e) {
                        print("对Error事件作出响应:" + e.toString());
                    }

                    @Override
                    public void onComplete() {
                        print("对Complete事件作出响应");
                    }
                });
    }

    /**
     * 遇到错误时，发送1个新的Observable
     * onErrorResumeNext（）拦截的错误 = Throwable；若需拦截Exception请用onExceptionResumeNext（）
     * 若onErrorResumeNext（）拦截的错误 = Exception，则会将错误传递给观察者的onError方法
     */
    @Test
    public void testONErrorResumeNext() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onError(new Throwable("发生了错误!!!"));
            }
        })
                .onErrorResumeNext(new Function<Throwable, ObservableSource<Integer>>() {
                    @Override
                    public ObservableSource<Integer> apply(Throwable throwable) throws Exception {
                        print(throwable.toString());
                        return Observable.just(-1);
                    }
                })
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        print(d.toString());
                    }

                    @Override
                    public void onNext(Integer integer) {
                        print("接收到了事件:" + integer);
                    }

                    @Override
                    public void onError(Throwable e) {
                        print("对Error事件作出响应:" + e.toString());
                    }

                    @Override
                    public void onComplete() {
                        print("对Complete事件作出响应");
                    }
                });
    }

    /**
     * <-- 1. retry（） -->
     * // 作用：出现错误时，让被观察者重新发送数据
     * // 注：若一直错误，则一直重新发送
     * <p>
     * <-- 2. retry（long time） -->
     * // 作用：出现错误时，让被观察者重新发送数据（具备重试次数限制
     * // 参数 = 重试次数
     * <p>
     * <-- 3. retry（Predicate predicate） -->
     * // 作用：出现错误后，判断是否需要重新发送数据（若需要重新发送& 持续遇到错误，则持续重试）
     * // 参数 = 判断逻辑
     *
     * @see #testRetry2()
     * <--  4. retry（new BiPredicate<Integer, Throwable>） -->
     * // 作用：出现错误后，判断是否需要重新发送数据（若需要重新发送 & 持续遇到错误，则持续重试)
     * // 参数 =  判断逻辑（传入当前重试次数 & 异常错误信息）
     * <p>
     * <-- 5. retry（long time,Predicate predicate） -->
     * // 作用：出现错误后，判断是否需要重新发送数据（具备重试次数限制)
     * // 参数 = 设置重试次数 & 判断逻辑
     */
    @Test
    public void testRetry() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onError(new Throwable("发生了错误!!!"));
            }
        })
                .retry(5)
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        print(d.toString());
                    }

                    @Override
                    public void onNext(Integer integer) {
                        print("接收到了事件:" + integer);
                    }

                    @Override
                    public void onError(Throwable e) {
                        print("对Error事件作出响应:" + e.toString());
                    }

                    @Override
                    public void onComplete() {
                        print("对Complete事件作出响应");
                    }
                });

    }

    @Test
    public void testRetry2() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onError(new Throwable("" + new Random().nextInt(10)));
            }
        })
                .retry(new Predicate<Throwable>() {
                    @Override
                    public boolean test(Throwable throwable) throws Exception {
                        print(throwable.toString());
                        return parIntSafe(throwable.getMessage()) > 5;
                    }
                })
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        print(d.toString());
                    }

                    @Override
                    public void onNext(Integer integer) {
                        print("接收到了事件:" + integer);
                    }

                    @Override
                    public void onError(Throwable e) {
                        print("对Error事件作出响应:" + e.toString());
                    }

                    @Override
                    public void onComplete() {
                        print("对Complete事件作出响应");
                    }
                });
    }

    /**
     * 出现错误后，判断是否需要重新发送数据
     * 若需要重新发送 & 持续遇到错误，则持续重试
     * 作用类似于retry（Predicate predicate）
     * 具体使用
     * 具体使用类似于retry（Predicate predicate），唯一区别：返回 true 则不重新发送数据事件。
     *
     * @see #testRetry2()
     */
    @Test
    public void testRetryUntil() {

    }

    /**
     * 作用 有条件地、重复发送 被观察者事件
     * 遇到错误时，将发生的错误传递给一个新的被观察者（Observable），并决定是否需要重新订阅原始被观察者Observable）& 发送事件
     * 将原始 Observable 停止发送事件的标识（Complete（） / Error（））转换成1个 Object 类型数据传递给1个新被观察者（Observable），
     * 以此决定是否重新订阅 & 发送原来的 Observable
     * 若新被观察者（Observable）返回1个Complete / Error事件，则不重新订阅 & 发送原来的 Observable
     * 若新被观察者（Observable）返回其余事件时，则重新订阅 & 发送原来的 Observable
     */
    @Test
    public void testRepeatWhen() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                e.onNext(2);
                e.onError(new Exception("发生错误了"));
                e.onNext(3);
            }
        })
                // 遇到error事件才会回调
                .retryWhen(new Function<Observable<Throwable>, ObservableSource<?>>() {

                    @Override
                    public ObservableSource<?> apply(@NonNull Observable<Throwable> throwableObservable) throws Exception {
                        // 参数Observable<Throwable>中的泛型 = 上游操作符抛出的异常，可通过该条件来判断异常的类型
                        // 返回Observable<?> = 新的被观察者 Observable（任意类型）
                        // 此处有两种情况：
                        // 1. 若 新的被观察者 Observable发送的事件 = Error事件，那么 原始Observable则不重新发送事件：
                        // 2. 若 新的被观察者 Observable发送的事件 = Next事件 ，那么原始的Observable则重新发送事件：
                        return throwableObservable.flatMap(new Function<Throwable, ObservableSource<?>>() {
                            @Override
                            public ObservableSource<?> apply(@NonNull Throwable throwable) throws Exception {

                                // 1. 若返回的Observable发送的事件 = Error事件，则原始的Observable不重新发送事件
                                // 该异常错误信息可在观察者中的onError（）中获得
//                                return Observable.error(new Throwable("retryWhen终止啦"));

                                // 2. 若返回的Observable发送的事件 = Next事件，则原始的Observable重新发送事件（若持续遇到错误，则持续重试）
                                return Observable.just(1);
                            }
                        });

                    }
                })
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        print(d.toString());
                    }

                    @Override
                    public void onNext(Integer integer) {
                        print("接收到了事件:" + integer);
                    }

                    @Override
                    public void onError(Throwable e) {
                        print("对Error事件作出响应:" + e.toString());
                    }

                    @Override
                    public void onComplete() {
                        print("对Complete事件作出响应");
                    }
                });

    }
}

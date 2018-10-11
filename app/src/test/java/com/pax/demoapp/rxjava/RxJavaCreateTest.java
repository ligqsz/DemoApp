package com.pax.demoapp.rxjava;

import com.pax.demoapp.ui.model.WeatherRequest;

import org.junit.Test;

import java.io.Serializable;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.pax.demoapp.rxjava.Utils.print;

/**
 * 创建被观察者的操作符
 *
 * @author ligq
 * @date 2018/10/10
 */
@SuppressWarnings("Convert2Lambda")
public class RxJavaCreateTest {
    @Test
    public void testBefore() {
        //OnError()和onCompleted()是互斥的
        //写成科技新闻.subscribe(张三)
        Observable.just("也许当初忙着微笑和哭泣", "忙着追逐天空中的流星", "人理所当然的忘记", "是谁风里雨里一直默默守护在原地")
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        print(s);
                    }
                });

    }

    /**
     * 我们可以使用该操作符从零开始创建一个Observable，给这个操作符传递一个接受观察者作为参数的函数，
     * 并调用观察者的onNext，onError和onCompleted方法
     */
    @Test
    public void testCreate() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext("也许当初忙着微笑和哭泣");
                emitter.onNext("忙着追逐天空中的流星");
                emitter.onNext("人理所当然的忘记");
                emitter.onNext("是谁风里雨里一直默默守护在原地");
                emitter.onComplete();
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                print(s);
            }
        });
    }

    /**
     * 该操作符是将其它种类的对象和数据类型转换为Observable，如果当你发射的的数据是同一种类型，
     * 而不是混合使用Observables和其它类型的数据，会非常方便
     */
    @Test
    public void testFrom() {
        int[] ints = {1, 2, 3, 4};
        Integer[] integers = {1, 2, 3, 4};
        Observable.fromArray(ints, new int[]{5, 6, 7, 8}).subscribe(new Consumer<int[]>() {
            @Override
            public void accept(int[] ints) throws Exception {
                print(Arrays.toString(ints));
            }
        });

        Observable.fromArray(integers).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                print("" + integer);
            }
        });
    }

    /**
     * just将单个数据转换为发射那个数据的Observable，Just类似于From，但是From会将数组或Iterable的数据取出然后逐个发射，
     * 而Just只是简单的原样发射，将数组或Iterable当做单个数据，如果你传递null给Just，它会返回一个发射null值的Observable。
     * 对于just参数类型可以是多种
     */
    @Test
    public void testJust() {
        Observable.just(1, "one", 2, "two")
                .subscribe(new Consumer<Serializable>() {
                    @Override
                    public void accept(Serializable serializable) throws Exception {
                        print(serializable.toString());
                    }
                });
        Observable.just(new WeatherRequest()).subscribe(new Consumer<WeatherRequest>() {
            @Override
            public void accept(WeatherRequest request) throws Exception {
                print(request.toString());
            }
        });
    }

    /**
     * Empty:创建一个不发射任何数据但是正常终止的Observable，此时会回调onCompleted（）
     */
    @Test
    public void testEmpty() {
        Observable.empty().subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable d) {
                print("onSubscribe");
            }

            @Override
            public void onNext(Object o) {
                print("onNext");
            }

            @Override
            public void onError(Throwable e) {
                print("onError");
            }

            @Override
            public void onComplete() {
                print("onComplete");
            }
        });
    }

    /**
     * Never:创建一个不发射数据也不终止的Observable
     */
    @Test
    public void testNever() {
        Observable.never().subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable d) {
                print("onSubscribe");
            }

            @Override
            public void onNext(Object o) {
                print("onNext");
            }

            @Override
            public void onError(Throwable e) {
                print("onError");
            }

            @Override
            public void onComplete() {
                print("onComplete");
            }
        });
    }

    /**
     * Error:创建一个不发射数据以一个错误终止的Observable
     * error操作符需要一个Throwable参数，你的Observable会以此终止。
     */
    @Test
    public void testError() {
        Observable.error(new ArrayIndexOutOfBoundsException("out of bounds"))
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        print("onNext" + o.toString());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        print(throwable.toString());
                    }
                });
    }

    /**
     * 该操作符创建特定整数序列的Observable，它接受两个参数，一个是范围的起始值，一个是范围的数据的数目。
     * 如果你将第二个参数设为0，将导致Observable不发射任何数据（如果设置为负数，会抛异常）
     */
    @Test
    public void testRange() {
        Observable.range(1, 5).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                print(integer.toString());
            }
        });
    }

    /**
     * todo
     * Timer操作符创建一个在给定的时间段之后返回一个特殊值的Observable。它在延迟一段给定的时间后发射一个简单的数字0
     */
    @Test
    public void testTimer() {
        Observable.timer(0, TimeUnit.SECONDS, Schedulers.io())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        print("------------" + aLong.toString());
                    }
                });
    }

    /**
     * todo
     * 该操作符按固定的时间间隔发射一个无限递增的整数序列，它接受一个表示时间间隔的参数和一个表示时间单位的参数，
     * 当然该操作符合Timer一样，是在computation调度器上执行的，若想更新UI需要指定Scheduler 为AndroidSchedulers.mainThread()
     * 通过上面代码就会每隔1秒在tv上追加一个数字，并且会永远执行,如果不想执行,就需要解除订阅disposable.dispose();
     */
    @Test
    public void testInterval() {
        Observable.interval(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {

                    }
                });
    }

    /**
     * 该操作符是重复的发射某个数据序列，并且可以自己设置重复的次数。
     * 当接收到onComplete()会触发重订阅再次重复发射数据,当重复发射数据次数到达后执行onCompleted()
     */
    @Test
    public void testRepeat() {
        String[] strs = {"也许当初忙着微笑和哭泣", "忙着追逐天空中的流星"};
        Observable.fromArray(strs)
                .repeat(2)
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        print(s);
                    }
                });
    }

    private String test;

    /**
     * 直到有观察者订阅时才创建Observable，并且为每个观察者创建一个新的Observable，
     * 该操作符能保证订阅执行时数据源是最新的数据。
     */
    @Test
    public void testDefer() {
        test = "121212";
        Observable<String> defer = Observable.defer(new Callable<ObservableSource<? extends String>>() {
            @Override
            public ObservableSource<? extends String> call() throws Exception {
                return Observable.just(test);
            }
        });

        test = "新数据";
        defer.subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                print(s);
            }
        });
    }

}

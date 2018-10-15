package com.pax.demoapp.rxjava;

import android.os.SystemClock;

import com.pax.demoapp.utils.RJTestUtils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

import static com.pax.demoapp.rxjava.Utils.print;

/**
 * @author ligq
 * @date 2018/10/11
 */

@SuppressWarnings("Convert2Lambda")
public class RxJavaFilterTest {
    /**
     * 该操作符接收一个Predicate参数，我们可以在其中通过运用你自己的判断条件去判断我们要过滤的数据，
     * 当数据通过判断条件后返回true表示发射该项数据，否则就不发射，这样就过滤出了我们想要的数据。
     */
    @Test
    public void testFilter() {
        Integer[] integers = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        Observable.fromArray(integers).filter(new Predicate<Integer>() {
            @Override
            public boolean test(Integer integer) throws Exception {
                return integer % 3 == 0;
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                print(integer.toString());
            }
        });
        print("----------------------------------------------------------");
        List<Integer> list = new ArrayList<>();
        for (int i = 1; i <= 1000; i++) {
            list.add(i);
        }
        List<Integer> result = new ArrayList<>();
        Observable.fromIterable(list)
                .filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        if (integer == 1) {
                            return true;
                        }
                        for (Integer i = 1; i <= integer / 2; i++) {
                            if (i != 1 && integer % i == 0) {
                                return false;
                            }
                        }
                        return true;
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        result.add(integer);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        print(throwable.toString());
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        print(result.toString());
                    }
                });

    }

    /**
     * 操作符是filter操作符的一个特殊形式。它过滤一个Observable只返回指定类型的数据
     * 当然除了过滤基本类型的数据，也可以过滤自定义类型数据。
     */
    @Test
    public void testOfType() {
        Observable.just("sdde", 32, "de", "akc", 326)
                .ofType(String.class)
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        print(s);
                    }
                });
    }

    /**
     * 如果我们只对Observable发射的第一项数据，或者满足某个条件的第一项数据感兴趣，则可以使用First操作符
     */
    @Test
    public void testFirst() {
        Observable.range(20, 8).firstElement().subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                print(integer.toString());
            }
        });
    }

    /**
     * 获取后面几个数据
     */
    @Test
    public void testTakeLast() {
        Observable.just(11, 12, 13).takeLast(2).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                print(integer.toString());
            }
        });
    }

    /**
     * 如果原始Observable在完成之前不是正好发射一次数据，它会抛出一个IllegalArgumentException，
     * 白话可以理解为发送数据是一项的话输出此项的值，若是多个数据则抛出异常执行onError()方法。
     */
    @Test
    public void testSingle() {
        Observable.just(11, 12, 13, 14)
                .filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        return integer > 12;
                    }
                })
                .singleElement()
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        print(integer.toString());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        print(throwable.toString());
                    }
                });
    }

    /**
     * 该操作符与first意义相反，若我们只对Observable发射的最后一项数据，或者满足某个条件的最后一项数据感兴趣时使用该操作符
     */
    @Test
    public void testLast() {
        Observable.just(11, 12, 13, 14)
                .lastElement()
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        print(integer.toString());
                    }
                });

    }

    /**
     * 该操作符是跳过之前的前几项数据，然后再发射数据。
     * skip还有两个重载方法.skip(long time, TimeUnit unit)默认是在computation调度器上执行，
     * 如果要有更新UI操作需要通过observeOn方法指定为AndroidSchedulers.mainThread()，
     * 当然还有一个重载方法skip(long time, TimeUnit unit, Scheduler scheduler)可以指定调度器。
     * 注意的一点是这两个重载方法的第一个参数不是跳过的数据数量，指的是时间。{@link RJTestUtils#testSkip()}
     */
    @Test
    public void testSkip() {
        Observable.range(10, 10)
                .skip(5)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        print(integer.toString());
                    }
                });

        Observable.interval(500, TimeUnit.MILLISECONDS)
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
                        print(num.toString());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 正好和skip 相反，忽略最后产生的n个数据项
     */
    @Test
    public void testSkipLast() {
        Observable.range(2, 12).skipLast(4)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        print(integer.toString());
                    }
                });
    }

    /**
     * Take操作符可以修改Observable的行为，只返回前面的N项数据，然后发射完成通知，忽略剩余的数据。
     * take和skip一样也有其它两个重载方法take(long time, TimeUnit unit)，take(long time, TimeUnit unit, Scheduler scheduler)
     * 默认在computation调度器上执行。
     */
    @Test
    public void testTake() {
        Observable.range(2, 9)
                .take(3)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        print(integer.toString());
                    }
                });
    }

    /**
     * @see RJTestUtils#testDebounce()
     * 可以理解对源Observable间隔期产生的结果进行过滤，如果在这个规定的间隔期内没有别的结果产生，
     * 则将这个结果提交给订阅者，否则忽略该结果
     */
    @Test
    public void testDebounce() {
        Integer[] ints = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        Observable.fromArray(ints).flatMap(new Function<Integer, ObservableSource<String>>() {
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
                        print(s);
                    }
                });
    }

    /**
     * 这个比较好理解，它就是过滤掉重复的数据，只允许还没有发射过的数据项通过。
     */
    @Test
    public void testDistinct() {
        Observable.just(0, 0, 6, 4, 2, 1, 5, 1, 7)
                .distinct()
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        print(integer.toString());
                    }
                });
    }

    /**
     * 该操作符获取原始Observable发射的数据序列指定索引位置的数据项，然后当做自己的唯一数据发射。
     * 给它传递一个基于0的索引值，它会发射原始Observable数据序列对应索引位置的值，
     * 如果你传递给elementAt的值为4，那么它会发射第5项的数据。
     */
    @Test
    public void testElementAt() {
        Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9)
                .elementAt(4)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        print(integer.toString());
                    }
                });
    }

    /**
     * 操作符抑制原始Observable发射的所有数据，只允许它的终止通知（onError或onCompleted）通过，使用该操作符onNext()方法不会执行。
     * 这个操作符效果就如同empty（）方法创建一个空的Observable,只会执行onCompleted()方法，
     * 不同的是ignoreElements是对数据源的处理，而empty（）是创建Observable。
     */
    @Test
    public void testIgnoreElements() {
        Observable.just(1, 2, 3)
                .ignoreElements()
                .subscribe(new Action() {
                    @Override
                    public void run() throws Exception {
                        print("complete");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        print(throwable.toString());
                    }
                });
    }

}

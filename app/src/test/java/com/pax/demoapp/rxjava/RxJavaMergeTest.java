package com.pax.demoapp.rxjava;

import com.pax.demoapp.utils.RJTestUtils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static com.pax.demoapp.rxjava.Utils.print;

/**
 * @author ligq
 * @date 2018/10/11
 */

@SuppressWarnings({"Convert2Lambda", "ResultOfMethodCallIgnored", "RedundantThrows", "Anonymous2MethodRef"})
public class RxJavaMergeTest {
    /**
     * 该操作符可以将多个Observables的输出合并，就好像它们是一个单个的Observable一样，
     * 他可能让我们让合并的Observables发射的数据交错（顺序发生变化），
     * 在此过程中任何一个原始Observable的onError通知都会被立即传递给观察者，而且会终止合并后的Observable。
     */
    @Test
    public void testMerge() {
        Observable<Integer> just1 = Observable.just(1, 2);
        Observable<Integer> just2 = Observable.just(6, 7, 9);
        Observable.merge(just1, just2)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        if (integer % 3 == 0) {
                            /* throw new IllegalArgumentException("error parameters");*/
                            Thread.sleep(500);
                        }
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
     * @see RJTestUtils#testMergeDelayError()
     * 对于merge操作符的任何一个的Observable发射了onError通知终止了，merge操作符生成的Observable也会立即以onError通知终止。
     * 如果你想让它继续发射数据，在最后才报告错误，可以使用mergeDelayError。
     * MergeDelayError的使用有个坑，就是subscribeOn和observeOn的调用问题，
     * 如果先mergeDelayError之后再用subscribeOn和observeOn指定调度器发现该操作符并不起作用。
     * 需要在单独创建Observable时使用
     */
    @Test
    public void testMergeDelayError() {
        Observable<Integer> just1 = Observable.just(1, 2).subscribeOn(Schedulers.newThread()).observeOn(Schedulers.io());
        Observable<Integer> just2 = Observable.just(6, 7).subscribeOn(Schedulers.newThread()).observeOn(Schedulers.io());
        Observable.mergeDelayError(just1, just2)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        if (integer % 3 == 0) {
                            throw new IllegalArgumentException("error parameters");
                        }
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
     * 该操作符和merge操作符相似，不同之处就是该操作符按顺序一个接着一个发射多个Observables的发射物。
     * 保证顺序性。
     */
    @Test
    public void testConcat() {
        Observable<Integer> just1 = Observable.just(1, 2);
        Observable<Integer> just2 = Observable.just(6, 7);
        Observable.concat(just1, just2)
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
     * 该操作符返回一个Observable，它使用这个函数按顺序结合两个或多个Observables发射的数据项，
     * 然后它发射这个函数返回的结果。它按照严格的顺序应用这个函数。
     * 它只发射与发射数据项最少的那个Observable一样多的数据，
     * 假如两个Observable数据分布为4项，5项，则最终合并是4项。
     */
    @Test
    public void testZip() {
        List<String> names = new ArrayList<>();
        List<Integer> ages = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            names.add("张三" + i);
            ages.add(20 + i);
        }
        ages.add(15);
        Observable<String> just1 = Observable.fromIterable(names);
        Observable<Integer> just2 = Observable.fromIterable(ages);
        Observable.zip(just1, just2, new BiFunction<String, Integer, String>() {
            @Override
            public String apply(String s, Integer integer) throws Exception {
                return s + ":" + integer;
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                print(s);
            }
        });

    }

    /**
     * 该操作符作用是在一个Observable在发射数据之前先发射一个指定的数据序列
     */
    @Test
    public void testStartWith() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add(10 + i);
        }
        Observable.range(1, 3).startWith(Observable.range(10, 5)).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                print(integer.toString());
            }
        });
    }

    /**
     * CombineLatest操作符行为类似于zip，但是只有当原始的Observable中的每一个都发射了一条数据时zip才发射数据。
     * CombineLatest则在原始的Observable中任意一个发射了数据时发射一条数据。
     * 当原始Observables的任何一个发射了一条数据时，CombineLatest使用一个函数结合它们最近发射的数据，然后发射这个函数的返回值。
     */
    @Test
    public void testCombineLatest() {
        Observable<Integer> range1 = Observable.range(1, 4);
        Observable<Integer> range2 = Observable.range(10, 5);
        Observable.combineLatest(range1, range2, new BiFunction<Integer, Integer, String>() {
            @Override
            public String apply(Integer integer, Integer integer2) throws Exception {
                return "observableA:" + integer + "  observableB:" + integer2;
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                print(s);
            }
        });

    }

    /**
     * 该操作符只要在另一个Observable发射的数据定义的时间窗口内，这个Observable发射了一条数据，
     * 就结合两个Observable发射的数据.例如A作为基础窗口，当A发射了数据1,2,3,4,5时，B发射了一个数据a.
     * 则此时合并数据（1，a）,（2，a）,（3，a），（4，a），（5，a），此时将窗口清除并重新打开一个窗口循环此种操作直到数据输出完毕。
     */
    @Test
    public void testJoin() {
        Observable<Integer> range1 = Observable.range(1, 2);
        Observable<Integer> range2 = Observable.range(7, 3);
        range1.join(range2, new Function<Integer, ObservableSource<Integer>>() {
            @Override
            public ObservableSource<Integer> apply(Integer integer) throws Exception {
                return Observable.just(integer).delay(1, TimeUnit.SECONDS);
            }
        }, new Function<Integer, ObservableSource<Integer>>() {
            @Override
            public ObservableSource<Integer> apply(Integer integer) throws Exception {
                return Observable.just(integer).delay(1, TimeUnit.SECONDS);
            }
        }, new BiFunction<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer integer, Integer integer2) throws Exception {
                return integer + integer2;
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                print(integer.toString());
            }
        });
    }

    /**
     * @see RJTestUtils#testSwitchOnNext()
     * switchOnNext 运算符采用一个发出 observables 的observable ，返回的 observable 从最近的 observable 中发射出来。
     * 当一个新的 observable 出现时，旧的被丢弃，而新的值被发出。
     */
    @Test
    public void testSwitchOnNext() {
        Observable<Observable<Long>> take = Observable.interval(0, 500, TimeUnit.MILLISECONDS)
                .map(new Function<Long, Observable<Long>>() {
                    @Override
                    public Observable<Long> apply(Long aLong) throws Exception {
                        return Observable.interval(0, 200, TimeUnit.MILLISECONDS)
                                .map(new Function<Long, Long>() {
                                    @Override
                                    public Long apply(Long aLong) throws Exception {
                                        print("take:" + aLong.toString());
                                        return aLong * 10;
                                    }
                                })
                                .take(5);
                    }
                })
                .take(2);

        Observable.switchOnNext(take).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                print(aLong.toString());
            }
        });
    }

}

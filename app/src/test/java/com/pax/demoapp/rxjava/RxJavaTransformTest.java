package com.pax.demoapp.rxjava;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Action;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observables.GroupedObservable;
import io.reactivex.schedulers.Schedulers;

import static com.pax.demoapp.rxjava.Utils.print;


/**
 * @author ligq
 * @date 2018/10/10
 */
@SuppressWarnings({"Convert2Lambda", "ResultOfMethodCallIgnored", "RedundantThrows", "Anonymous2MethodRef"})
public class RxJavaTransformTest {

    /**
     * 该操作符是对原始Observable发射的每一项数据运用一个函数，然后返回一个发射这些结果的Observable。
     * 对于map,他可以将将数据源变换为你想要的类型
     */
    @Test
    public void testMap() {
        String str = "adks1sl3s3lsd4ls9..s";
        Observable.just(str)
                .map(new Function<String, List<Integer>>() {
                    @Override
                    public List<Integer> apply(String s) throws Exception {
                        List<Integer> list = new ArrayList<>();
                        char[] chars = s.toCharArray();
                        for (char aChar : chars) {
                            Integer integer = Utils.parIntSafe(aChar);
                            if (integer != -1) {
                                list.add(integer);
                            }
                        }
                        return list;
                    }
                }).map(new Function<List<Integer>, String>() {
            @Override
            public String apply(List<Integer> list) throws Exception {
                StringBuilder sb = new StringBuilder();
                for (Integer integer : list) {
                    sb.append(integer);
                }
                return sb.toString();
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                print(s);
            }
        });
    }

    /**
     * 该操作符就是做一些强制类型转换操作的。例如，当我们在页面跳转时数据对象往往是序列化的，
     * 当我们在新的页面收到数据后就要强制转换为我们想要的类型。cast操作符也可以实现这样的功能。
     */
    @Test
    public void testCast() {
        Object o = 123;
        Observable.just(o).cast(Integer.class).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer aByte) throws Exception {
                print(aByte.toString());
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                print(throwable.toString());
            }
        });
    }

    /**
     * 该操作符与map操作符的区别是它将一个发射数据的Observable变换为多个Observables，
     * 然后将它们发射的数据合并后放进一个单独的Observable.
     */
    @Test
    public void testFlatMap() {
        Integer[] integers = {1, 2, 3};
        Observable.fromArray(integers).flatMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(Integer integer) throws Exception {
                return Observable.create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                        print("call: FlatMap ");
                        emitter.onNext(integer + 100 + "");
                        emitter.onComplete();
                    }
                });
            }
        }).observeOn(Schedulers.io())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        print(s);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        print(throwable.toString());
                    }
                });
    }

    /**
     * 该操作符是类似于最简单版本的flatMap，但是它按次序连接而不是合并那些生成的Observables，然后产生自己的数据序列.
     * flatMap并没有保证数据源的顺序性，但是ConcatMap操作符保证了数据源的顺序性。
     * 在应用中，如果你对数据的顺序性有要求的话，就需要使用ConcatMap。若没有要求，二者皆可使用。
     */
    @Test
    public void testConcatMap() {
        Integer[] integers = {1, 2, 3};
        Observable.fromArray(integers).concatMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(Integer integer) throws Exception {
                return Observable.create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                        print("call: ConcatMap ");
//                        SystemClock.sleep(20);
                        emitter.onNext(integer + 100 + "");
                        emitter.onComplete();
                    }
                });
            }
        }).observeOn(Schedulers.io())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        print(s);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        print(throwable.toString());
                    }
                });
    }

    /**
     * 当原始Observable发射一个新的数据（Observable）时，它将取消订阅并停止监视产生执之前那个数据的Observable，只监视当前这一个.
     * 当数据源较多时，并不一定是只输出最后一项数据，有可能输出几项数据，也可能是全部。
     * 应用场景:当用户的输入操作停止几秒钟之后再去搜索,使用switchMap来替代flatMap
     */
    @Test
    public void testSwitchMap() {
        Integer[] integers = {1, 2, 3};
        Observable.fromArray(integers).switchMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(Integer integer) throws Exception {
                print("call: SwitchMap ");
                return Observable.just((integer + 100) + "switchMap");
            }
        }).observeOn(Schedulers.io())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        print(s);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        print(throwable.toString());
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        print("complete");
                    }
                });
    }

    /**
     * 看到这个词你就应该想到了这个操作符的作用，就是你理解的含义，他将数据源按照你的约定进行分组。
     */
    @Test
    public void testGroupBy() {
        Observable.range(1, 100).groupBy(new Function<Integer, String>() {
            @Override
            public String apply(Integer integer) throws Exception {
                if (integer % 3 == 0) {
                    return "i%3==0:";
                } else if (integer % 2 == 0) {
                    return "i%2==0&&i%3!=0:";
                } else {
                    return "rest:";
                }
            }
        }).subscribe(new Consumer<GroupedObservable<String, Integer>>() {
            @Override
            public void accept(GroupedObservable<String, Integer> booleanIntegerGroupedObservable) throws Exception {
                booleanIntegerGroupedObservable.toList().subscribe(new Consumer<List<Integer>>() {
                    @Override
                    public void accept(List<Integer> list) throws Exception {
                        print(booleanIntegerGroupedObservable.getKey() + "");
                        print(list.toString());
                    }
                });
            }
        });
    }

    private Integer result;

    /**
     * 操作符对原始Observable发射的第一项数据应用一个函数，然后将那个函数的结果作为自己的第一项数据发射。
     * 它将函数的结果同第二项数据一起填充给这个函数来产生它自己的第二项数据。它持续进行这个过程来产生剩余的数据序列。
     */
    @Test
    public void testScan() {
        Observable.range(1, 6).scan(10, new BiFunction<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer integer, Integer integer2) throws Exception {
                return integer * integer2;
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                result = integer;
                print(integer.toString());
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
        print("----------------------------------------------");
        List<Integer> list = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            list.add(i);
        }
        Observable.fromIterable(list)
                .scan(new BiFunction<Integer, Integer, Integer>() {
                    @Override
                    public Integer apply(Integer integer, Integer integer2) throws Exception {
                        return integer + integer2;
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        result = integer;
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
     * 将一个Observable变换为另一个，原来的Observable正常发射数据，变换产生的Observable发射这些数据的缓存集合，
     * 如果原来的Observable发射了一个onError通知，Buffer会立即传递这个通知，而不是首先发射缓存的数据，
     * 即使在这之前缓存中包含了原始Observable发射的数据。
     */
    @Test
    public void testBuffer() {
        Observable.range(10, 10).buffer(5, 3).subscribe(new Consumer<List<Integer>>() {
            @Override
            public void accept(List<Integer> list) throws Exception {
                Integer[] integers = new Integer[list.size()];
                print(Arrays.toString(list.toArray(integers)));
            }
        });
    }

    /**
     * Window和Buffer类似，但不是发射来自原始Observable的数据包，它发射的是Observables，
     * 这些Observables中的每一个都发射原始Observable数据的一个子集，最后发射一个onCompleted通知
     */
    @Test
    public void testWindow() {
        Observable.range(10, 6).window(4).subscribe(new Consumer<Observable<Integer>>() {
            @Override
            public void accept(Observable<Integer> integerObservable) throws Exception {
                integerObservable.subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        print(integer.toString());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        print(throwable.toString());
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        print("complete");
                    }
                });
            }
        });
    }

}

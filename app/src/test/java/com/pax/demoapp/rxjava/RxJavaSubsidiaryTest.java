package com.pax.demoapp.rxjava;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.pax.demoapp.DemoApp;
import com.pax.demoapp.R;
import com.pax.demoapp.utils.RJTestUtils;

import org.junit.Test;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.schedulers.Timed;

import static com.pax.demoapp.rxjava.Utils.print;

/**
 * @author ligq
 * @date 2018/10/11
 */

@SuppressWarnings({"Convert2Lambda", "ResultOfMethodCallIgnored", "RedundantThrows", "Anonymous2MethodRef"})
public class RxJavaSubsidiaryTest {
    /**
     * @see RJTestUtils#testDelay()
     * 该操作符让原始Observable在发射每项数据之前都暂停一段指定的时间。它接受一个定义时长的参数（包括long型数据和单位）。
     * 每当原始Observable发射一项数据，delay就启动一个定时器，当定时器过了给定的时间段时，delay返回的Observable发射相同的数据项。
     * 他默认是在computation调度器上执行，当然也有重载方法可以指定调度器，若发射数据后有更新UI操作需将调度器指定AndroidSchedulers.mainThread()。
     * （注意重载方法delay(Fun1)，delay(Fun0,Fun1)是默认不在任何特定的调度器上执行）
     */
    @Test
    public void testDelay() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
                emitter.onNext(4);
                emitter.onComplete();
            }
        }).delay(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        print(integer.toString());
                    }
                });
    }

    /**
     * 该操作符也是delay的一种实现，它和delay的区别是delay是延迟数据的发送，而此操作符是延迟数据的注册，
     * 指定延迟时间的重载方法是执行在computation调度器的。
     */
    @Test
    public void testDelaySubscription() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
                emitter.onNext(4);
                emitter.onComplete();
            }
        })
                .delaySubscription(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        print(integer.toString());
                    }
                });
    }

    /**
     * 对于do系列操作符理解比较容易，他相当于给Observable执行周期的关键节点添加回调。
     * 当Observable执行到这个阶段的时候，这些回调就会被触发。在Rxjava do系列操作符有多个，
     * 如doOnNext，doOnSubscribe，doOnCompleted，doOnError，doOnTerminate和doOnEach。
     * 当Observable每发送一个数据时，doOnNext会被首先调用，然后再onNext。若发射中途出现异常doOnError会被调用，然后onError。
     * 若数据正常发送完毕doOnCompleted会被触发，然后执行onCompleted。当订阅doOnSubscribe会被执行。
     * <p>
     * 对于doOnEach操作符，他接收的是一个Observable参数，相当于doOnNext，doOnError，doOnCompleted综合体
     *
     * @see #doOnEach()
     */
    @Test
    public void testDo() {
        Observable.just(1, 2, 3)
                .doOnNext(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        print("doOnNext:" + integer);
                    }
                })
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        print("doOnError:" + throwable.toString());
                    }
                })
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        print("doOnComplete!!!");
                    }
                })
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        print("doOnSubscribe");
                    }
                })
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        print("doOnTerminate");
                    }
                })
                .doAfterTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        print("doAfterTerminate");
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
//                        if (integer % 2 == 0) {
//                            throw new IllegalArgumentException("error param!!!");
//                        }
                        print("subscribe:" + integer);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        print(throwable.toString());
                    }
                });

        doOnEach();
    }

    private void doOnEach() {
        Observable.just(1, 2, 3)
                .doOnEach(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        print("onSubscribe");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        print("onNext");
                    }

                    @Override
                    public void onError(Throwable e) {
                        print("onError:" + e.toString());
                    }

                    @Override
                    public void onComplete() {
                        print("onComplete");
                    }
                }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
//                if (integer % 2 == 0) {
//                    throw new IllegalArgumentException("error param!!!");
//                }
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
     * @see RJTestUtils#testSubscribeOn(LinearLayout)
     * ObservableOn指定Observable在一个特定的调度器上发送通知给观察者 (调用观察者的onNext, onCompleted, onError方法)，
     * 当遇到一个异常时ObserveOn会立即向前传递这个onError终止通知，它不会等待慢速消费的Observable接受任何之前它已经收到但还没有发射的数据项。
     * 这可能意味着onError通知会跳到（并吞掉）原始Observable发射的数据项前面。
     * SubscribeOn操作符的作用类似，但它是用于指定Observable本身在特定的调度器上执行，它同样会在那个调度器上给观察者发通知。
     * 该操作符只能指定一次，如果指定多次则以第一次为准。而observeOn可以指定多次，每次指定会在observeOn下一句代码处生效。
     */
    @Test
    public void testSubscribeOn() {
        Observable.create(new ObservableOnSubscribe<Drawable>() {
            @Override
            public void subscribe(ObservableEmitter<Drawable> emitter) throws Exception {
                Drawable drawable = DemoApp.getApp().getDrawable(R.mipmap.ic_launcher);
                emitter.onNext(drawable);
                emitter.onComplete();
            }
        })
                //指定创建Observable在io线程中执行
                .subscribeOn(Schedulers.io())
                //由于map中做耗时操作,通过Observable指定发射数据在新的线程
                .observeOn(Schedulers.newThread())
                .map(new Function<Drawable, ImageView>() {
                    @Override
                    public ImageView apply(Drawable drawable) throws Exception {
                        ImageView imageView = new ImageView(DemoApp.getApp());
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        imageView.setLayoutParams(params);
                        imageView.setImageDrawable(drawable);
                        return imageView;
                    }
                })
                //操作UI，需要指定在主线程
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ImageView>() {
                    @Override
                    public void accept(ImageView imageView) throws Exception {
                        new LinearLayout(DemoApp.getApp()).addView(imageView);
                    }
                });
    }

    /**
     * @see RJTestUtils#testTimeInterval()
     * 这个操作符将原始Observable转换为另一个Observable，后者发射一个标志替换前者的数据项，这个标志表示前者的两个连续发射物之间流逝的时间长度。
     * 新的Observable的第一个发射物表示的是在观察者订阅原始Observable到原始Observable发射它的第一项数据之间流逝的时间长度。
     * 不存在与原始Observable发射最后一项数据和发射onCompleted通知之间时长对应的发射物。
     * 通过日志发现，返回的Timed类型数据，包含时间间隔和值。
     */
    @Test
    public void testTimeInterval() {
        Observable.interval(1, TimeUnit.SECONDS)
                .filter(new Predicate<Long>() {
                    @Override
                    public boolean test(Long aLong) throws Exception {
                        return aLong < 5;
                    }
                })
                .timeInterval()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Timed<Long>>() {
                    @Override
                    public void accept(Timed<Long> longTimed) throws Exception {
                        print("onNext: value:" + longTimed.value() + "getIntervalInMilliseconds" + longTimed.time());
                    }
                });
    }

    /**
     * @see RJTestUtils#testTimestamp()
     * 该操作符和TimeInterval一样最终发射的都是Timed类型数据。
     * 但是不同的是，改操作符发射数据每一项包含数据的原始发射时间（TimeInterval是时间间隔）
     */
    @Test
    public void testTimestamp() {
        Observable.just(1, 2, 3, 4)
                .timestamp()
                .subscribe(new Consumer<Timed<Integer>>() {
                    @Override
                    public void accept(Timed<Integer> integerTimed) throws Exception {
                        print("onNext: value:" + integerTimed.value() + "getIntervalInMilliseconds" + integerTimed.time());
                    }
                });
    }

    /**
     * 如果原始Observable过了指定的一段时间没有发射任何数据，Timeout操作符会以一个onError通知终止这个Observable。
     * timeout还有重载方法可以在超时的时候切换到一个我们指定的备用的Observable，而不是发错误通知。它也默认在computation调度器上执行。
     */
    @Test
    public void testTimeout() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                Thread.sleep(100);
                emitter.onNext(2);
                Thread.sleep(200);
                emitter.onNext(3);
                Thread.sleep(300);
                emitter.onNext(4);
                Thread.sleep(400);
                emitter.onComplete();
            }
        })
                .timeout(250, TimeUnit.MILLISECONDS)
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


        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                Thread.sleep(100);
                emitter.onNext(2);
                Thread.sleep(200);
                emitter.onNext(3);
                Thread.sleep(300);
                emitter.onNext(4);
                Thread.sleep(400);
                emitter.onComplete();
            }
        })
                .timeout(250, TimeUnit.MILLISECONDS, Observable.just(10, 11))
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
     * To:此系列操作符的作用是将Observable转换为另一个对象或数据结构。
     * <p>
     * ToList:发射多项数据的Observable会为每一项数据调用onNext方法。你可以用toList操作符改变这个行为，
     * 让Observable将多项数据组合成一个List，然后调用一次onNext方法传递整个列表，
     * 如果原始Observable没有发射任何数据就调用了onCompleted，toList返回的Observable会在调用onCompleted之前发射一个空列表。
     * 如果原始Observable调用了onError，toList返回的Observable会立即调用它的观察者的onError方法。
     *
     * @see #testToList()
     * <p>
     * ToMap:该操作符收集原始Observable发射的所有数据项到一个Map（默认是HashMap）然后发射这个Map。
     * 我们可以提供一个用于生成Map的Key的函数，还可以提供一个函数转换数据项到Map存储的值（默认数据项本身就是值）。
     * @see #testToMap()
     * <p>
     * ToMutiMap:类似于toMap，不同的是，它生成的这个Map同时还是一个ArrayList（默认是这样，你可以传递一个可选的工厂方法修改这个行为）。
     * toMap(Func1)是将原Observable发送的数据保存到一个MAP中，并在参数函数中，设定key。
     * 但toMultimap操作符在将数据保存到MAP前，先将数据保存到Collection，而toMap操作符将数据直接保存到MAP中，并没有再包裹一层Collection。
     * @see #testToMutiMap()
     * <p>
     * ToSortedList:该操作符类似于toList，区别是它可以对数据进行自然排序。
     * @see #testToSortedList()
     */
    @Test
    public void testToList() {
        Observable.just(1, 2, 3, 4, 5)
                .toList()
                .subscribe(new Consumer<List<Integer>>() {
                    @Override
                    public void accept(List<Integer> list) throws Exception {
                        print(list.toString());
                    }
                });
    }

    @Test
    public void testToMap() {
        Observable.just(1, 2, 3, 4, 5)
                .toMap(new Function<Integer, String>() {
                    @Override
                    public String apply(Integer integer) throws Exception {
                        return "key" + integer;
                    }
                })
                .subscribe(new Consumer<Map<String, Integer>>() {
                    @Override
                    public void accept(Map<String, Integer> stringIntegerMap) throws Exception {
                        print(stringIntegerMap.toString());
                    }
                });
    }

    @Test
    public void testToMutiMap() {
        Observable.just(1, 2, 3, 4, 5)
                .toMultimap(new Function<Integer, String>() {
                    @Override
                    public String apply(Integer integer) throws Exception {
                        return "key" + integer;
                    }
                })
                .subscribe(new Consumer<Map<String, Collection<Integer>>>() {
                    @Override
                    public void accept(Map<String, Collection<Integer>> stringCollectionMap) throws Exception {
                        print(stringCollectionMap.toString());
                    }
                });
    }

    @Test
    public void testToSortedList() {
        Integer[] integers = {2, 1, 5, 3, 7, 8, 4, 9, 4, 2, 5, 7, 6};
        Observable.fromArray(integers)
                .toSortedList()
                .subscribe(new Consumer<List<Integer>>() {
                    @Override
                    public void accept(List<Integer> list) throws Exception {
                        print(list.toString());
                    }
                });
    }
}

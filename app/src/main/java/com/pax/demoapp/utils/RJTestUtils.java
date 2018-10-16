package com.pax.demoapp.utils;

import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.pax.demoapp.DemoApp;
import com.pax.demoapp.R;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.schedulers.Timed;

/**
 * @author ligq
 * @date 2018/10/15
 */
@SuppressWarnings({"AlibabaClassNamingShouldBeCamel", "Convert2Lambda", "RedundantThrows", "Anonymous2MethodRef"})
public class RJTestUtils {
    private static Disposable disposable;
    private static final String TAG = RJTestUtils.class.getSimpleName();
    private static final String[] STRINGS = {"也许当初忙着微笑和哭泣", "忙着追逐天空中的流星",
            "人理所当然的忘记", "是谁风里雨里一直默默守护在原地"};

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
     * 防抖动
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
                .debounce(2, TimeUnit.SECONDS)
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        LogUtils.d(s);
                    }
                });
    }


    public static void testMergeDelayError() {
        disposable();
        disposable = Observable.mergeDelayError(Observable.error(new Exception("错误"))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()),
                Observable.just("数据").subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()))
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        LogUtils.d(o.toString());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtils.e(throwable);
                    }
                });
    }

    public static void testSwitchOnNext() {
        disposable();
        Observable<Observable<Long>> take = Observable.interval(0, 500, TimeUnit.MILLISECONDS)
                .map(new Function<Long, Observable<Long>>() {
                    @Override
                    public Observable<Long> apply(Long aLong) throws Exception {
                        return Observable.interval(0, 200, TimeUnit.MILLISECONDS)
                                .map(new Function<Long, Long>() {
                                    @Override
                                    public Long apply(Long aLong) throws Exception {
                                        Log.i(TAG, "apply: " + aLong.toString());
                                        return aLong * 10;
                                    }
                                })
                                .take(5);
                    }
                })
                .take(2);

        disposable = Observable.switchOnNext(take).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                Log.i(TAG, "accept: " + aLong.toString());
            }
        });
    }

    public static void testDelay() {
        disposable();
        disposable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext(STRINGS[0]);
                SystemClock.sleep(3000);
                emitter.onNext(STRINGS[1]);
                SystemClock.sleep(3000);
                emitter.onNext(STRINGS[2]);
                SystemClock.sleep(3000);
                emitter.onNext(STRINGS[3]);
                emitter.onComplete();
            }
        })
                .delay(2, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String string) throws Exception {
                        ToastUtils.showShort(string);
                    }
                });
    }

    public static void testSubscribeOn(LinearLayout layout) {
        disposable();
        disposable = Observable.create(new ObservableOnSubscribe<Drawable>() {
            @Override
            public void subscribe(ObservableEmitter<Drawable> emitter) throws Exception {
                emitter.onNext(ContextCompat.getDrawable(DemoApp.getApp(), R.mipmap.qq));
                emitter.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .map(new Function<Drawable, ImageView>() {
                    @Override
                    public ImageView apply(Drawable drawable) throws Exception {
                        ImageView imageView = new ImageView(layout.getContext());
                        imageView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                                , ViewGroup.LayoutParams.WRAP_CONTENT));
                        imageView.setImageDrawable(drawable);
                        return imageView;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ImageView>() {
                    @Override
                    public void accept(ImageView imageView) throws Exception {
                        layout.addView(imageView);
                    }
                });
    }

    public static void testTimeInterval() {
        disposable();
        disposable = Observable.interval(0, 3, TimeUnit.SECONDS)
                .filter(new Predicate<Long>() {
                    @Override
                    public boolean test(Long aLong) throws Exception {
                        return aLong < STRINGS.length;
                    }
                })
                .timeInterval()
                .observeOn(Schedulers.io())
                .map(new Function<Timed<Long>, String>() {
                    @Override
                    public String apply(Timed<Long> longTimed) throws Exception {
                        return longTimed.time() + ":" + STRINGS[Integer.parseInt(longTimed.value().toString())];
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        ToastUtils.showShort(s);
                    }
                });
    }

    public static void testTimestamp() {
        disposable();
        disposable = Observable.fromArray(STRINGS)
                .timestamp()
                .concatMap(new Function<Timed<String>, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(Timed<String> stringTimed) throws Exception {
                        return Observable.create(new ObservableOnSubscribe<String>() {
                            @Override
                            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                                emitter.onNext(stringTimed.time() + ":" + stringTimed.value());
                                emitter.onComplete();
                                SystemClock.sleep(3000);
                            }
                        });
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
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

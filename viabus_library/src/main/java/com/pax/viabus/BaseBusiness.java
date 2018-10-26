package com.pax.viabus;

import com.pax.viabus.bus.BaseBus;
import com.pax.viabus.bus.Result;
import com.pax.viabus.bus.ResultCode;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author KunMinX
 * @date 2018/8/22
 */
public class BaseBusiness<B extends BaseBus> {

    /**
     * send message in main thread.
     *
     * @param e      ObservableEmitter
     * @param result result
     */
    protected void sendMessage(ObservableEmitter<Result> e, Result result) {
        e.onNext(result);
    }

    /**
     * send progress info in main thread.
     *
     * @param e      ObservableEmitter
     * @param result result
     */
    protected void onProgress(ObservableEmitter<Result> e, Result result) {
        sendMessage(e, result);
    }

    /**
     * handle request
     *
     * @param iAsync IAsync
     */
    protected void handleRequest(final IAsync iAsync) {
        Observable.create(new ObservableOnSubscribe<Result>() {
            @Override
            public void subscribe(ObservableEmitter<Result> e) {
                try {
                    if (iAsync != null) {
                        e.onNext(iAsync.onExecute(e));
                    }
                } catch (Exception e1) {
                    e.onError(e1);
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Result>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result value) {
                        B.response(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Result result = new Result(ResultCode.FAILURE, e.toString());
                        B.response(result);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    protected interface IAsync {
        /**
         * executing request in io thread
         *
         * @param e ObservableEmitter
         * @return Result
         * @throws IOException IOException
         */
        Result onExecute(ObservableEmitter<Result> e) throws IOException;
    }

}

package com.pax.demoapp.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blankj.utilcode.util.NetworkUtils;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.pax.demoapp.R;
import com.pax.demoapp.ui.model.WeatherApi;
import com.pax.demoapp.ui.model.WeatherRequest;
import com.pax.demoapp.ui.model.WeatherResponse;
import com.pax.demoapp.utils.LogUtils;
import com.pax.paxokhttp.okhttp.RetrofitHelper;
import com.pax.paxokhttp.okhttp.RxHelper;
import com.pax.paxokhttp.rxbus.RxBus;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * @author ligq
 */
@SuppressWarnings({"Convert2Lambda", "RedundantThrows"})
public class TestNetActivity extends AppCompatActivity implements IActivity {

    private EditText city;
    private TextView requestResult;
    private ProgressBar progressBar;
    private LinearLayout content;
    private int requestMode;
    private String url;
    private String key;
    private Disposable disposable;
    private boolean networkError;

    @Override
    public int getLayoutId() {
        return R.layout.activity_test_net;
    }

    @Override
    public void initData() {
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
        requestMode = 1;
        url = "http://v.juhe.cn/weather/";
        key = "1c353f780748ee3e9616da568d846038";
        networkError = !NetworkUtils.getMobileDataEnabled() && !NetworkUtils.getWifiEnabled();
    }

    private void doRequest() {
        LogUtils.e("-----------------------------------");
        showProgress();
        disposable();
        disposable = getWeatherResponseObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WeatherResponse>() {
                    @Override
                    public void accept(WeatherResponse weatherResponse) throws Exception {
                        if (weatherResponse != null && weatherResponse.getResult() != null) {
                            requestResult.setText(weatherResponse.getResult().toString());
                        } else if (weatherResponse != null) {
                            requestResult.setText(weatherResponse.getReason());
                        } else {
                            requestResult.setText("查询不到该城市的信息");
                        }
                        hideProgress();
                        networkError = false;
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        requestResult.setText(throwable.toString());
                        hideProgress();
                        networkError = true;
                        doRequest();
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        LogUtils.d("complete!!");
                        hideProgress();
                    }
                });
    }

    private Observable<WeatherResponse> getWeatherResponseObservable() {
        return RxTextView.textChanges(city)
                .debounce(1, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .filter(new Predicate<CharSequence>() {
                    @Override
                    public boolean test(CharSequence charSequence) throws Exception {
                        LogUtils.d("filter:length:" + (charSequence.toString().trim().length() > 0));
                        LogUtils.d("filter:networkError:" + networkError);
                        if (networkError) {
                            hideProgress();
                            requestResult.setText("网络连接错误,请重试!");
                        }
                        boolean b = charSequence.toString().trim().length() > 0
                                && !networkError;
                        networkError = false;
                        return b;
                    }
                })
                .observeOn(Schedulers.io())
                .switchMap(new Function<CharSequence, ObservableSource<WeatherResponse>>() {
                    @Override
                    public ObservableSource<WeatherResponse> apply(CharSequence charSequence) throws Exception {
                        LogUtils.d("-------------------网络请求");
                        return RetrofitHelper.createApi(WeatherApi.class, url)
                                .doGetWeather(charSequence.toString(), "", "", key);
                    }
                });
    }

    private void doRequestWeather() {
        showProgress();
        disposable();
        if (requestMode == 1) {
            disposable = RetrofitHelper.createApi(WeatherApi.class, url)
                    .doGetWeather(city.getText().toString(), "", "", key)
                    .compose(RxHelper.rxSchedulerHelper())
                    .subscribe(weatherResponse -> requestResult.setText(weatherResponse.getResult().toString())
                            , throwable -> {
                                requestResult.setText(throwable.toString());
                                hideProgress();
                            }
                            , this::hideProgress);
        } else {
            WeatherRequest request = new WeatherRequest();
            request.setCityname(city.getText().toString());
            request.setDtype("1");
            request.setFormat("1");
            disposable = RetrofitHelper.createApi(WeatherApi.class, url)
                    .doPost(key, request)
                    .compose(RxHelper.rxSchedulerHelper())
                    .subscribe(rsp -> requestResult.setText(rsp.string())
                            , throwable -> {
                                requestResult.setText(throwable.toString());
                                hideProgress();
                            }
                            , this::hideProgress);
        }
    }

    @SuppressLint("CheckResult")
    @Override
    public void initView() {
        RxBus.get().send("I'm from TestNetActivity");
        requestResult = findViewById(R.id.request_result);
        Button request = findViewById(R.id.request);
        city = findViewById(R.id.city);

        progressBar = findViewById(R.id.progress_bar);
        content = findViewById(R.id.content);
        //noinspection ResultOfMethodCallIgnored
        RxView.clicks(request).subscribe(o -> {
            requestResult.setText("");
            doRequestWeather();
        });
    }

    private void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        content.setVisibility(View.GONE);
    }

    private void hideProgress() {
        progressBar.setVisibility(View.GONE);
        content.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.net_test_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.get:
                requestMode = 1;
                break;
            case R.id.post:
                requestMode = 2;
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        doRequest();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable();
    }

    private void disposable() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
        disposable = null;
    }
}

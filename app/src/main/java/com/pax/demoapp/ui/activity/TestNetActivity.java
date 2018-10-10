package com.pax.demoapp.ui.activity;

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

import com.pax.demoapp.R;
import com.pax.demoapp.ui.model.WeatherApi;
import com.pax.demoapp.ui.model.WeatherRequest;
import com.pax.paxokhttp.okhttp.RetrofitHelper;
import com.pax.paxokhttp.okhttp.RxHelper;
import com.pax.paxokhttp.rxbus.RxBus;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class TestNetActivity extends AppCompatActivity implements IActivity {

    private EditText city;
    private TextView requestResult;
    private ProgressBar progressBar;
    private LinearLayout content;
    private int requestMode;
    private String url;
    private String key;

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
    }

    private void doRequest() {
        showProgress();
        RetrofitHelper.createApi(WeatherApi.class, url)
                .doGet(city.getText().toString(), "", "", key)
//                .compose(RxHelper.rxSchedulerHelper())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseBody -> requestResult.setText(responseBody.string())
                        , throwable -> {
                            requestResult.setText(throwable.toString());
                            hideProgress();
                        }
                        , this::hideProgress);
    }

    private void doRequestWeather() {
        showProgress();
        if (requestMode == 1) {
            RetrofitHelper.createApi(WeatherApi.class, url)
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
            RetrofitHelper.createApi(WeatherApi.class, url)
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

    @Override
    public void initView() {
        RxBus.get().send("I'm from TestNetActivity");
        requestResult = findViewById(R.id.request_result);
        Button request = findViewById(R.id.request);
        city = findViewById(R.id.city);

        progressBar = findViewById(R.id.progress_bar);
        content = findViewById(R.id.content);
        doRequest();
        request.setOnClickListener(view -> {
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
}

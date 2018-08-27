package com.pax.demoapp.ui.activity;

import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;

import com.pax.demoapp.DemoApp;
import com.pax.demoapp.R;
import com.pax.demoapp.view.PercentView;

/**
 * @author ligq
 */
public class CustomViewActivity extends AppCompatActivity implements IActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_custom_view;
    }

    @Override
    public void initData() {

    }

    @Override
    public void initView() {
        PercentView percentView = findViewById(R.id.percent_view);
        percentView.setOnClickListener(view ->
                DemoApp.getApp().runInBackGround(() -> {
                    for (int i = 1; i <= 100; i++) {
                        SystemClock.sleep(100);
                        percentView.reset();
                        percentView.updateProgress(i);
                    }
                }));
    }
}

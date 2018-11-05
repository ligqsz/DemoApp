package com.pax.demoapp.ui.activity;

import android.os.Bundle;
import android.os.SystemClock;

import com.pax.demoapp.R;
import com.pax.demoapp.template.base.BaseActivity;
import com.pax.demoapp.view.PercentView;
import com.pax.paxokhttp.okhttp.AppUtils;

/**
 * @author ligq
 */
public class CustomViewActivity extends BaseActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_custom_view;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        PercentView percentView = findViewById(R.id.percent_view);
        percentView.setOnClickListener(view ->
                AppUtils.runInBackground(() -> {
                    if (percentView.isStart()) {
                        return;
                    }
                    for (int i = 1; i <= 100; i++) {
                        SystemClock.sleep(100);
                        percentView.updateProgress(i);
                    }
                }));
    }
}

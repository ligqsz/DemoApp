
package com.pax.demoapp.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.pax.demoapp.DemoApp;
import com.pax.demoapp.R;

/**
 * @author ligq
 */
public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        View decorView = getWindow().getDecorView();

        LogUtils.d("decView:" + decorView.getClass());

        findViewById(R.id.bt).setOnClickListener(v -> DemoApp.getApp().startTask());
    }


}

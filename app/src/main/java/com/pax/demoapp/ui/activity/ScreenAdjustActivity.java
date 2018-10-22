package com.pax.demoapp.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.pax.demoapp.R;
import com.pax.demoapp.utils.LogUtils;
import com.pax.demoapp.utils.ScreenUtils;

/**
 * @author ligq
 */
public class ScreenAdjustActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.d("screen width:" + ScreenUtils.getScreenWidth());
        ScreenUtils.adaptScreen4VerticalSlide(this, 360);
        setContentView(R.layout.activity_screen_adjust);
    }
}

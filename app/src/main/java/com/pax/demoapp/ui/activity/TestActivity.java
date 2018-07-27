
package com.pax.demoapp.ui.activity;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.pax.demoapp.R;
import com.pax.demoapp.utils.OtherUtils;

/**
 * @author ligq
 */
public class TestActivity extends AppCompatActivity implements IActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_test;
    }

    @Override
    public void initData() {
        // do nothing
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void initView() {
        Button btTest = findViewById(R.id.bt_test);
        TextView tvResult = findViewById(R.id.tv_result);

        btTest.setOnClickListener(v ->
                tvResult.setText("channel:" + OtherUtils.getMeta("UMENG_CHANNEL") + "\n" +
                        "save log:" + OtherUtils.getMetaInt("SAVE_LOG") + "\n" +
                        "show log:" + OtherUtils.getMetaInt("SHOW_LOG")));
    }
}

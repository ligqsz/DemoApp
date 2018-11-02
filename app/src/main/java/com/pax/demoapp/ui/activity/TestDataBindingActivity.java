package com.pax.demoapp.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.pax.demoapp.R;
import com.pax.demoapp.SchoolBinding;
import com.pax.demoapp.db.greendao.bean.School;
import com.pax.demoapp.utils.LogUtils;

import java.util.Random;

/**
 * @author ligq
 */
public class TestDataBindingActivity extends AppCompatActivity {

    private SchoolBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_test_data_binding);
        testSchool();
    }

    private void testSchool() {
        School school = new School(2L, "深圳" + new Random().nextInt(100), "张三" + new Random().nextInt(100));
        binding.setSchool(school);
    }

    public void onClick(View v) {
        LogUtils.d("onClick:" + v);
        testSchool();
    }
}

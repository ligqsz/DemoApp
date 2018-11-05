package com.pax.demoapp.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.pax.demoapp.R;
import com.pax.demoapp.SchoolBinding;
import com.pax.demoapp.ui.model.School;
import com.pax.utils.LogUtils;

import java.util.Random;

/**
 * android DataBinding Test
 * detail refer to the project <a href="https://github.com/LyndonChin/MasteringAndroidDataBinding">
 * and <a href = "https://juejin.im/post/5b02cf8c6fb9a07aa632146d">
 *
 * @author ligq
 */
public class TestDataBindingActivity extends AppCompatActivity {

    private SchoolBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_test_data_binding);
        testSchool();
//        binding.btTest.setText("");
    }

    private void testSchool() {
//        ObservableField<String> name = new ObservableField<>();
//        name.set("深圳" + new Random().nextInt(100));
        School school = new School("深圳" + new Random().nextInt(100), "张三" + new Random().nextInt(100));
        binding.setSchool(school);
    }

    public void onClick(View v) {
        LogUtils.d("onClick:" + v);
        testSchool();
    }
}

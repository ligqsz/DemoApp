package com.pax.demoapp.ui.activity;

import android.content.res.TypedArray;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.pax.demoapp.R;

/**
 * 触摸反馈动画
 *
 * @author ligq
 */
public class RippleEffectActivity extends AppCompatActivity implements IActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_ripple_effect;
    }

    @Override
    public void initData() {

    }

    @Override
    public void initView() {
        initButtonSysCode();
    }

    private void initButtonSysCode() {
        Button sysCode = findViewById(R.id.sys_code);
        int[] attrs = new int[]{R.attr.selectableItemBackground};
        TypedArray typedArray = obtainStyledAttributes(attrs);
        int backgroundResource = typedArray.getResourceId(0, 0);
        sysCode.setBackgroundResource(backgroundResource);
    }
}

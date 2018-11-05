package com.pax.demoapp.ui.activity;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.widget.Button;

import com.pax.demoapp.R;
import com.pax.demoapp.template.base.BaseActivity;

/**
 * 触摸反馈动画
 *
 * @author ligq
 */
public class RippleEffectActivity extends BaseActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_ripple_effect;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        initView();
    }

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

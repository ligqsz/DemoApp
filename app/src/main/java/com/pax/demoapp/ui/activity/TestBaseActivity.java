package com.pax.demoapp.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;

import com.pax.demoapp.R;
import com.pax.demoapp.template.base.BaseActivity;

import java.util.Random;

/**
 * @author ligq
 */
public class TestBaseActivity extends BaseActivity {

    @Override
    public int getContentViewResId() {
        return R.layout.activity_test_base;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        setIsShowStatusBar(true);
        setToolBar(R.color.greed, "测试Base", R.color.white, 20
                , R.menu.menu_test_base, R.drawable.ic_back_white_24dp);
        setStatusBarColor(R.color.greed);
    }

    @Override
    protected void onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_state_bg:
                setStatusBarColor(Color.parseColor(randomColor()));
                break;
            case R.id.action_toolbar_bg:
                setToolBarBg(Color.parseColor(randomColor()));
                break;
            default:
                break;
        }
    }

    private String randomColor() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        sb.append("#");
        for (int i = 0; i < 6; i++) {
            int temp = random.nextInt(15) + 1;
            sb.append(Integer.toHexString(temp));
        }
        return sb.toString();
    }
}

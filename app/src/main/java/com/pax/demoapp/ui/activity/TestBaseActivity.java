package com.pax.demoapp.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;

import com.blankj.utilcode.util.LogUtils;
import com.pax.demoapp.R;
import com.pax.demoapp.template.base.BaseActivity;
import com.pax.utils.StatusBarUtil;

import java.util.Random;

/**
 * can also refer to <a href = "https://github.com/xiewenfeng/statusbartextcolorchange"/>
 *
 * @author ligq
 */
public class TestBaseActivity extends BaseActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_test_base;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        setIsShowStatusBar(true);
        LogUtils.d("test base activity");
        setToolBar(R.color.greed, "测试Base", R.color.white, 20
                , R.menu.menu_test_base, R.drawable.ic_back_white_24dp);
        setStatusBarColor(R.color.greed);
        StatusBarUtil.setDarkMode(this);
    }

    @Override
    protected void onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_state_bg:
                setStatusBarColor(Color.parseColor(randomColor()));
                if (new Random().nextInt(10) % 2 == 0) {
                    StatusBarUtil.setLightMode(this);
                } else {
                    StatusBarUtil.setDarkMode(this);
                }
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

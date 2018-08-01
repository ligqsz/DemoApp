
package com.pax.demoapp.ui.activity;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.TextView;

import com.pax.demoapp.R;
import com.pax.demoapp.utils.OtherUtils;
import com.pax.demoapp.utils.ToastUtils;

/**
 * @author ligq
 */
public class ToolBarActivity extends AppCompatActivity implements IActivity {

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
        initToolBar();
        Button btTest = findViewById(R.id.bt_test);
        TextView tvResult = findViewById(R.id.tv_result);

        btTest.setOnClickListener(v ->
                tvResult.setText("channel:" + OtherUtils.getMeta("UMENG_CHANNEL") + "\n" +
                        "save log:" + OtherUtils.getMetaInt("SAVE_LOG") + "\n" +
                        "show log:" + OtherUtils.getMetaInt("SHOW_LOG")));
    }

    private void initToolBar() {
        TextView tvTitle = findViewById(R.id.title);
        tvTitle.setText(getClass().getSimpleName());
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.toolbar_menu);
        toolbar.setNavigationIcon(R.drawable.ic_back_white_24dp);
        toolbar.setNavigationOnClickListener(v -> ToastUtils.showShort("back!!"));
        toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_search:
                    ToastUtils.showShort("search!!");
                    break;
                case R.id.action_delete:
                    ToastUtils.showShort("delete!!");
                    break;
                case R.id.action_settings:
                    ToastUtils.showShort("settings!!");
                    break;
                default:
                    break;
            }
            return true;
        });
    }

}

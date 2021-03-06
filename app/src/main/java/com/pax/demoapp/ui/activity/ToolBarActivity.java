
package com.pax.demoapp.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.pax.demoapp.R;
import com.pax.demoapp.template.base.BaseActivity;
import com.pax.demoapp.utils.OtherUtils;
import com.pax.demoapp.view.JustifyTextView;
import com.pax.utils.ToastUtils;

/**
 * @author ligq
 */
public class ToolBarActivity extends BaseActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_toolbar;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        initView();
    }

    @SuppressLint("SetTextI18n")
    public void initView() {
        initToolBar();
        Button btTest = findViewById(R.id.bt_test);
        TextView tvResult = findViewById(R.id.tv_result);
        JustifyTextView test = findViewById(R.id.tv_test);
        //webp格式图片在Android4.0以下不支持,4.2.1以下只支持完全不透明的webp图
        ImageView ivWebpTest = findViewById(R.id.iv_webp_test);
        ivWebpTest.setImageResource(R.mipmap.webp_test);
        btTest.setOnClickListener(v -> {
                    tvResult.setText("channel:" + OtherUtils.getMeta("UMENG_CHANNEL") + "\n" +
                            "save log:" + OtherUtils.getMetaInt("SAVE_LOG") + "\n" +
                            "show log:" + OtherUtils.getMetaInt("SHOW_LOG"));
                    test.setScaleX(2);
                    test.setText(R.string.test_string);
                }
        );

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

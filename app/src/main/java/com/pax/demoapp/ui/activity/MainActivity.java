package com.pax.demoapp.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.KeyEvent;

import com.pax.demoapp.R;
import com.pax.demoapp.config.MenuConfig;
import com.pax.demoapp.ui.adapter.MenuAdapter;
import com.pax.demoapp.utils.ActivityUtils;
import com.pax.demoapp.utils.Density;
import com.pax.demoapp.utils.ToastUtils;
import com.pax.demoapp.view.dialog.CustomDialog;
import com.pax.demoapp.view.dialog.DialogUtils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author ligq
 */
public class MainActivity extends AppCompatActivity implements MenuAdapter.MenuAdapterListener, IActivity {

    private List<String> dataList;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initData() {
        Density.setDensity(getApplication(), this);
        dataList = new LinkedList<>();
        dataList.addAll(Arrays.asList(MenuConfig.MENU_MAIN));
    }

    @Override
    public void initView() {
        RecyclerView rvMenu = findViewById(R.id.rv_menu);
        rvMenu.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        MenuAdapter adapter = new MenuAdapter(this, dataList);
        adapter.setOnItemClick(this);
        rvMenu.setAdapter(adapter);
    }

    /**
     * ActivityUtils方式三调用,必须要重写onNewIntent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            boolean isExitApp = intent.getBooleanExtra("exit", false);
            if (isExitApp) {
                this.finish();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ActivityUtils.exitAPP1();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onItemClick(int position) {
        switch (position) {
            case 0:
                DialogUtils.showConfirmDialog(MainActivity.this, new CustomDialog.DialogButtonListener() {
                    @Override
                    public void onOk() {
                        ToastUtils.showShort("ok");
                    }

                    @Override
                    public void onCancel() {
                        ToastUtils.showShort("cancel");
                    }
                });
                break;
            case 1:
                DialogUtils.showProgressDialog(this);
                break;
            case 2:
                startActivity(new Intent(this, PagerActivity.class));
                break;
            case 3:
                startActivity(new Intent(this, EditTextActivity.class));
                break;
            case 4:
                startActivity(new Intent(this, ToolBarActivity.class));
                break;
            case 5:
                startActivity(new Intent(this, OrmLiteActivity.class));
                break;
            case 6:
                ActivityUtils.jumpActivity(GreenDaoActivity.class);
                break;
            default:
                break;
        }
    }
}

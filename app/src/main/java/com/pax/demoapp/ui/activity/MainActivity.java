package com.pax.demoapp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.KeyEvent;
import android.widget.Toast;

import com.pax.demoapp.R;
import com.pax.demoapp.dialog.CustomDialog;
import com.pax.demoapp.dialog.DialogUtils;
import com.pax.demoapp.ui.adapter.MenuAdapter;
import com.pax.demoapp.utils.ActivityUtils;
import com.pax.demoapp.utils.Density;

import java.util.LinkedList;
import java.util.List;

/**
 * @author ligq
 */
public class MainActivity extends AppCompatActivity implements MenuAdapter.MenuAdapterListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Density.setDensity(getApplication(), this);
        initView();
    }

    private void initView() {
        List<String> dataList = new LinkedList<>();
        dataList.add("弹出按钮框");
        dataList.add("弹出ProgressBar");
        dataList.add("跳转pager界面");
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
                        Toast.makeText(MainActivity.this, "ok", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(MainActivity.this, "cancel", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case 1:
                DialogUtils.showProgressDialog(this);
                break;
            case 2:
                startActivity(new Intent(this, PagerActivity.class));
                break;
            default:
                break;
        }
    }
}

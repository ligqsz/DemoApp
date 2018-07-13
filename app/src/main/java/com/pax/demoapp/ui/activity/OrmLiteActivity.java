package com.pax.demoapp.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.pax.demoapp.R;
import com.pax.demoapp.config.IntentKeys;
import com.pax.demoapp.config.MenuConfig;
import com.pax.demoapp.ui.adapter.MenuAdapter;
import com.pax.demoapp.utils.ActivityUtils;

import java.util.Arrays;

/**
 * @author ligq
 */
public class OrmLiteActivity extends AppCompatActivity implements IActivity, MenuAdapter.MenuAdapterListener {

    @Override
    public int getLayoutId() {
        return R.layout.activity_orm_lite;
    }

    @Override
    public void initData() {
        //do nothing
    }

    @Override
    public void initView() {
        RecyclerView rvOrmMenu = findViewById(R.id.rv_orm_menu);
        rvOrmMenu.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        MenuAdapter adapter = new MenuAdapter(this, Arrays.asList(MenuConfig.MENU_ORM), false);
        adapter.setOnItemClick(this);
        rvOrmMenu.setAdapter(adapter);
    }


    @Override
    public void onItemClick(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt(IntentKeys.ORM_POSITION, position);
        ActivityUtils.jumpActivity(bundle, OrmResultActivity.class);
    }
}

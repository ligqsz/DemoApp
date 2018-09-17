package com.pax.demoapp.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.pax.demoapp.R;
import com.pax.demoapp.config.MenuConfig;
import com.pax.demoapp.ui.adapter.MenuAdapter;
import com.pax.demoapp.utils.ActivityUtils;

import java.util.Arrays;
import java.util.List;

/**
 * @author ligq
 */
public class AnimActivity extends AppCompatActivity implements IActivity, MenuAdapter.MenuAdapterListener {

    private List<String> menuList;

    @Override
    public int getLayoutId() {
        return R.layout.activity_anim;
    }

    @Override
    public void initData() {
        menuList = Arrays.asList(MenuConfig.MENU_ANIM);
    }

    @Override
    public void initView() {
        RecyclerView animMenu = findViewById(R.id.anim_menu);
        animMenu.setLayoutManager(new GridLayoutManager(this, 2));
        MenuAdapter adapter = new MenuAdapter(this, menuList, false);
        adapter.setOnItemClick(this);
        animMenu.setAdapter(adapter);
    }

    @Override
    public void onItemClick(int position) {
        switch (position) {
            case 0:
                ActivityUtils.jumpActivity(PropertyActivity.class);
                break;
            case 1:
                ActivityUtils.jumpActivity(RippleEffectActivity.class);
                break;
            case 2:
                ActivityUtils.jumpActivity(ExposeActivity.class);
                break;
            default:
                break;
        }
    }
}

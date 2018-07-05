package com.pax.demoapp.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * @author ligq
 * @date 2018/7/2
 */

public class MyFragmentStateAdapter extends FragmentStatePagerAdapter {
    List<Fragment> list;

    public MyFragmentStateAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.list = list;
    }

    /**
     * 返回需要展示的fragment
     *
     * @param position
     * @return
     */
    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    /**
     * 返回需要展示的fangment数量
     *
     * @return
     */
    @Override
    public int getCount() {
        return list.size();
    }
}

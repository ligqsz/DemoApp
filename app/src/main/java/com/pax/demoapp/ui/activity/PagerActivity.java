package com.pax.demoapp.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.pax.demoapp.R;
import com.pax.demoapp.ui.fragment.FragmentView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ligq
 */
public class PagerActivity extends AppCompatActivity {
    private ViewPager pager;
    public static final String TITLE = "Title";
    public static final String PAGER_NUM = "pagerNum";
    private TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager);
        pager = findViewById(R.id.vp);
        tabLayout = findViewById(R.id.tabLayout);
        initData();
        initListeners();
    }

    private void initListeners() {
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setTabTextColors(0, ContextCompat.getColor
                (this, R.color.red));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
                View view = tab.getCustomView();
                if (null != view && view instanceof TextView) {
                    setTextColorAndSize(R.color.red, 16, ((TextView) view));
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                if (null != view && view instanceof TextView) {
                    setTextColorAndSize(R.color.colorPrimary, 14, ((TextView) view));
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //do nothing
            }
        });
    }

    private void initData() {
        List<Fragment> list = new ArrayList<>();

        Bundle bundle1 = new Bundle();
        bundle1.putString(TITLE, "第一个Fragment");
        bundle1.putInt(PAGER_NUM, 1);
        Fragment fg1 = FragmentView.newInstance(bundle1);

        Bundle bundle2 = new Bundle();
        bundle2.putString(TITLE, "第二个Fragment");
        bundle2.putInt(PAGER_NUM, 2);
        Fragment fg2 = FragmentView.newInstance(bundle2);

        Bundle bundle3 = new Bundle();
        bundle3.putString(TITLE, "第三个Fragment");
        bundle3.putInt(PAGER_NUM, 3);
        Fragment fg3 = FragmentView.newInstance(bundle3);

        Bundle bundle4 = new Bundle();
        bundle4.putString(TITLE, "第四个Fragment");
        bundle4.putInt(PAGER_NUM, 4);
        Fragment fg4 = FragmentView.newInstance(bundle4);

        list.add(fg1);
        list.add(fg2);
        list.add(fg3);
        list.add(fg4);
        pager.setAdapter(new MyFragmentStateAdapter(getSupportFragmentManager(), list));

        //添加标签
        tabLayout.addTab(tabLayout.newTab().setCustomView(getTextView("tab1", R.color.red, 16)));
        tabLayout.addTab(tabLayout.newTab().setCustomView(getTextView("tab2", R.color.colorPrimary, 14)));
        tabLayout.addTab(tabLayout.newTab().setCustomView(getTextView("tab3", R.color.colorPrimary, 14)));
        tabLayout.addTab(tabLayout.newTab().setCustomView(getTextView("tab4", R.color.colorPrimary, 14)));
    }

    private TextView getTextView(String tab, int color, float size) {
        TextView tv = new TextView(this);
        tv.setGravity(Gravity.CENTER);
        setTextColorAndSize(color, size, tv);
        tv.setText(tab);
        return tv;
    }

    private void setTextColorAndSize(int color, float size, TextView tv) {
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, size);
        tv.setTextColor(ContextCompat.getColor(this, color));
    }
}

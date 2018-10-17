package com.pax.demoapp.template.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.pax.demoapp.R;
import com.pax.demoapp.utils.LogUtils;
import com.pax.demoapp.utils.StatusBarUtil;

import java.util.Objects;

/**
 * Activity基类
 *
 * @author ligq
 * @date 2018/10/17
 */

@SuppressWarnings({"SameParameterValue", "unused", "Convert2Lambda", "Anonymous2MethodRef"})
public abstract class BaseActivity extends BasePermissionsActivity {
    protected Toolbar mToolbar;

    public abstract int getContentViewResId();

    public abstract void init(Bundle savedInstanceState);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置无ActionBar
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(getContentViewResId());
        init(savedInstanceState);
    }


    /**
     * 检查网络是否可用
     *
     * @return boolean
     */
    protected boolean checkNetWork() {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert mConnectivityManager != null;
        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        if (mNetworkInfo != null) {
            return mNetworkInfo.isAvailable();
        }
        return false;
    }

    /**
     * 设置是否显示状态栏 如果显示 则会恢复默认颜色的状态栏
     *
     * @param isShow isShow
     */
    @SuppressLint("ObsoleteSdkInt")
    protected void setIsShowStatusBar(boolean isShow) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色
                Window window = getWindow();
                View decorView = window.getDecorView();
                if (isShow) {
                    int option = View.SYSTEM_UI_FLAG_VISIBLE;
                    decorView.setSystemUiVisibility(option);
                } else {
                    //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
                    int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                    decorView.setSystemUiVisibility(option);
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(Color.TRANSPARENT);
                }

            } else {
                Window window = getWindow();
                WindowManager.LayoutParams attributes = window.getAttributes();
                int flagTranslucentStatus;
                if (isShow) {
                    flagTranslucentStatus = WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
                } else {
                    flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
                }
                attributes.flags |= flagTranslucentStatus;
                window.setAttributes(attributes);
            }
        }
        if (isShow) {
            setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }
    }

    /**
     * 设置状态栏颜色
     *
     * @param color color
     */
    protected void setStatusBarColor(int color) {
        setStatusBarColor(color, 0);
    }

    /**
     * 设置状态栏颜色
     *
     * @param color color
     * @param alpha alpha
     */
    protected void setStatusBarColor(int color, int alpha) {
        StatusBarUtil.setColor(this, color, alpha);
    }

    protected void setToolBar(int bgColor, String title, int titleColor, float size, int menu, int navIcon) {
        TextView tvTitle = findViewById(R.id.title);
        if (tvTitle == null) {
            LogUtils.d("ToolBar not exist!");
            return;
        }
        tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        tvTitle.setTextColor(ContextCompat.getColor(this, titleColor));
        tvTitle.setText(title);
        mToolbar = findViewById(R.id.toolbar);
        mToolbar.setBackgroundColor(bgColor);
        mToolbar.inflateMenu(menu);
        mToolbar.setNavigationIcon(navIcon);
        if (navIcon != 0) {
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onTitleBack(v);
                }
            });
        }
        if (menu != 0) {
            mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    BaseActivity.this.onMenuItemClick(item);
                    return true;
                }
            });
        }
    }

    /**
     * 点击toolbar的menu监听
     *
     * @param item item
     */
    protected void onMenuItemClick(MenuItem item) {

    }

    protected void onTitleBack(View v) {
        finish();
    }

    protected void setToolBarBg(int color) {
        if (mToolbar != null) {
            mToolbar.setBackgroundColor(color);
        }
    }
}
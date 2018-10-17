package com.pax.demoapp.template.base;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author ligq
 * @date 2018/2/1
 */

@SuppressWarnings({"FieldCanBeLocal", "unused"})
public abstract class BaseFragment extends BasePermissionsFragment {
    protected Activity mActivity;
    protected Context mContext;
    private View mRootView;

    public abstract int getLayoutResId();

    /**
     * butterKnife 绑定
     *
     * @param target this
     * @param view   view
     */
    public abstract void initBindInject(Object target, View view);

    public abstract void init(Bundle savedInstanceState);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //获取Activity对象
        mActivity = getActivity();
        //获取上下文对象
        mContext = getContext();
        mRootView = inflater.inflate(getLayoutResId(), container, false);
        initBindInject(this, mRootView);
        init(savedInstanceState);
        return mRootView;
    }

    /**
     * 判断是否有网络
     *
     * @return boolean
     */
    protected boolean checkNetWork() {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert mConnectivityManager != null;
        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        if (mNetworkInfo != null) {
            return mNetworkInfo.isAvailable();
        }
        return false;
    }
}
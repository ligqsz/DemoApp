package com.pax.demoapp.common.adapter.list;

import android.content.Context;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * @author ligq
 * @date 2018-9-27 09:29:56
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public abstract class BaseCommonAdapter<T> extends BaseAdapter {
    private Context mContext;
    private List<T> list;
    private int resLayoutId;

    /**
     * 构造函数 传值初始化
     *
     * @param mContext    Context
     * @param list        List<T>
     * @param resLayoutId resLayoutId
     */
    public BaseCommonAdapter(Context mContext, List<T> list, int resLayoutId) {
        this.mContext = mContext;
        this.list = list;
        this.resLayoutId = resLayoutId;
    }

    public Context getContext() {
        return mContext;
    }

    /**
     * 固定位置添加元素
     *
     * @param position position
     * @param data     T
     */
    public void addData(@IntRange(from = 0) int position, @NonNull T data) {
        list.add(position, data);
        notifyDataSetChanged();
    }

    /**
     * 添加元素
     *
     * @param data T
     */
    public void addData(@NonNull T data) {
        list.add(data);
        notifyDataSetChanged();
    }

    /**
     * 删除固定位置元素
     *
     * @param position position
     */
    public void remove(@IntRange(from = 0) int position) {
        list.remove(position);
        notifyDataSetChanged();
    }

    /**
     * 删除元素
     *
     * @param data T
     */
    public void remove(@NonNull T data) {
        list.remove(data);
        notifyDataSetChanged();
    }

    /**
     * 设置新元素
     *
     * @param index index
     * @param data  T
     */
    public void setData(int index, @NonNull T data) {
        list.set(index, data);
        notifyDataSetChanged();
    }

    /**
     * 设置新列表
     *
     * @param data T
     */
    public void setNewData(@NonNull List<T> data) {
        this.list = data;
        notifyDataSetChanged();
    }

    /**
     * 获取数据列表
     *
     * @return List
     */
    @NonNull
    public List<T> getData() {
        return list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public T getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(resLayoutId, parent, false);
        }
        Converter converter = Converter.get(convertView);
        convert(converter, list.get(position), position);
        return convertView;
    }

    /**
     * 抽象方法 自定义数据处理
     *
     * @param holder   Converter
     * @param item     T
     * @param position position
     */
    public abstract void convert(Converter holder, T item, int position);
}
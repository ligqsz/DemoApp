package com.pax.demoapp.ui.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

/**
 * 双向数据绑定 {@link School#name}
 *
 * @author ligq
 * @date 2018/11/5 10:41
 */
public class School extends BaseObservable {
    //    private ObservableField<String> name;
    private String name;
    private String address;

    public School(String name, String address) {
        this.name = name;
        this.address = address;
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyChange();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

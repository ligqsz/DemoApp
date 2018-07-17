package com.pax.demoapp.db.greendao.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 与Student和Teacher关系都为一对多
 *
 * @author ligq
 * @date 2018/7/17
 */
@Entity(nameInDb = "SCHOOL_TABLE")
public class School {
    @Id
    private Long id;
    private String address;
    private String name;
    @Generated(hash = 1009682238)
    public School(Long id, String address, String name) {
        this.id = id;
        this.address = address;
        this.name = name;
    }
    @Generated(hash = 1579966795)
    public School() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getAddress() {
        return this.address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
}

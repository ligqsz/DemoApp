package com.pax.demoapp.db.greendao.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * 与Student关系为多对多,与School关系为多对一
 *
 * @author ligq
 * @date 2018/7/17
 */
@Entity(nameInDb = "TEACHER_TABLE")
public class Teacher {
    @Id
    private long id;
    private String name;
    private int age;
    private String address;

    @Generated(hash = 91447476)
    public Teacher(long id, String name, int age, String address) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.address = address;
    }

    @Generated(hash = 1630413260)
    public Teacher() {
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
